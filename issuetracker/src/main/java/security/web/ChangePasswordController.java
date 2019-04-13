package security.web;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import security.service.LoginService;

@Named
@ViewScoped
public class ChangePasswordController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private LoginService loginService;

	@NotBlank(message="Current Password field value is required")
	private String currentPassword;					// +getter +setter
	@NotBlank(message="New Password field value is required")
	private String newPassword;						// +getter +setter
	@NotBlank(message="Confrim new Password field value is required")
	private String confirmedNewPassword;			// +getter +setter
	
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmedNewPassword() {
		return confirmedNewPassword;
	}
	public void setConfirmedNewPassword(String confirmedNewPassword) {
		this.confirmedNewPassword = confirmedNewPassword;
	}
	
	
	public String changePassword() {
		String nextUrl = null;
		try {
			if (newPassword.equals(confirmedNewPassword)) {
				String currentUsername = Faces.getRemoteUser();
				loginService.changePassword(currentUsername, currentPassword, newPassword);
				Messages.addFlashGlobalInfo("Change password was successful.");
				nextUrl = "/index?faces-redirect=true";				
			} else {
				Messages.addGlobalError("New password must match Confirmed new password.");
			}
		} catch(Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
		return nextUrl;
	}
}
