package security.web;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.omnifaces.util.Messages;

import security.entity.LoginGroup;
import security.entity.LoginUser;
import security.service.LoginService;

@Named
@ViewScoped
public class LoginUserController implements Serializable {
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(LoginUserController.class.getName());
	
	@Inject
	private LoginService loginService;
	
	private List<LoginGroup> loginGroups;						// +getter
	private List<LoginUser> loginUsers;							// +getter
	
	private LoginUser currentLoginUser = new LoginUser();		// +getter
	
	@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
			message="Password value must contain at least 6 characters with at least 1 uppercase letter, 1 lowercase letter, and 1 number")
	private String password;									// +getter +setter

	@NotBlank(message="A user must be assigned to at least one group")
	private String selectedGroups;								// +getter +setter
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<LoginGroup> getLoginGroups() {
		return loginGroups;
	}
	public List<LoginUser> getLoginUsers() {
		return loginUsers;
	}
	public LoginUser getCurrentLoginUser() {
		return currentLoginUser;
	}
	public String getSelectedGroups() {
		return selectedGroups;
	}
	public void setSelectedGroups(String selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	
	@PostConstruct
	void init() {
		loginGroups = loginService.findAllLoginGroup();
		loginUsers = loginService.findAllLoginUser();
	}
	
	public void createLoginUser() {
		try {
			currentLoginUser.setPassword(password);
//			loginService.addLoginUser(currentLoginUser.getUsername(), password, selectedGroups);
			String[] groups = selectedGroups.split(",");
			loginService.addLoginUser(currentLoginUser.getUsername(), password, groups);
			Messages.addGlobalInfo("Username \"{0}\" was added successfully", currentLoginUser.getUsername());
			currentLoginUser = new LoginUser();
			loginUsers = loginService.findAllLoginUser();
			selectedGroups = "";
		} catch(Exception e) {
			logger.log(Level.FINE, e.getMessage());
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void removeLoginUser(LoginUser selectedLoginUser) {
		try {
			loginService.deleteLoginUser(selectedLoginUser);
			Messages.addGlobalInfo("Successfully removed \"{0}\"", selectedLoginUser.getUsername());
			loginUsers = loginService.findAllLoginUser();
		} catch (Exception e) {
			logger.log(Level.FINE, e.getMessage());
			Messages.addGlobalError("Removal of \"{0}\" was not successful.", selectedLoginUser.getUsername());
		}	
	}
}
