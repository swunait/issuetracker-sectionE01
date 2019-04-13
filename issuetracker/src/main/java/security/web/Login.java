package security.web;

import java.io.Serializable;

import javax.faces.annotation.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityContext securityContext;
		
	@Inject @ManagedProperty("#{param.new}")
	private boolean isNew;	// added for Caller-Initiated Authentication
	
	@NotBlank(message="Username value is required.")
	@Getter @Setter
	private String username;
	
	@NotBlank(message="Password value is required.")
	@Getter @Setter
	private String password;

	public void submit() {
				
		switch (continueAuthentication()) {
			case SEND_CONTINUE:
				Faces.responseComplete();
				break;
			case SEND_FAILURE:
				Messages.addGlobalError("Login failed. Incorrect login credentaials.");
				break;
			case SUCCESS:
				Messages.addFlashGlobalInfo("Login succeed");
				Faces.redirect(Faces.getRequestContextPath() + "/index.xhtml");		// added for Caller-Initiated Authentication
				break;
			case NOT_DONE:
				// JSF does not need to take any special action here
				break;
		}				
	}
	
	private AuthenticationStatus continueAuthentication() {
		Credential credential = new UsernamePasswordCredential(username, new Password(password) );		
		HttpServletRequest request = Faces.getRequest();
		HttpServletResponse response = Faces.getResponse();
		return securityContext.authenticate(request, response, 
				AuthenticationParameters.withParams()
					.newAuthentication(isNew)	// added for Caller-Initiated Authentication
					.credential(credential));
	}
}
