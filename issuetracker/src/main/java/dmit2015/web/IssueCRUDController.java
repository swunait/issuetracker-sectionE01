package dmit2015.web;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import lombok.Getter;
import lombok.Setter;
import dmit2015.entity.Issue;
import dmit2015.service.IssueService;

@Named
@ViewScoped
public class IssueCRUDController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(IssueCRUDController.class.getName());
	
	@Inject
	private IssueService issueService;
	
	private List<Issue> issues;					// +getter
	
	/** The current Issue to create, edit, update, or delete */
	@Getter @Setter 
	private  Issue issueDetail = new Issue();	// +getter +setter	
	
	@Getter @Setter 
	private boolean editMode = false;
	
	@Getter @Setter 
	private Long editId = null;
	
	@PostConstruct
	void init() {
		try {
			issues = issueService.findAllIssue();
		} catch(Exception e) {
			Messages.addGlobalError("Error retreiving issues");
			log.fine(e.getMessage());
		}
	}
	
	public String create() {
		String outcome = null;
		try {
			issueService.createIssue(issueDetail);
			issueDetail = new Issue();
			Messages.addFlashGlobalInfo("Create was succesful");
			outcome = "list?faces-redirect=true";
		} catch(Exception e) {
			Messages.addGlobalError("Create was not succesful");
			log.fine(e.getMessage());
		}
		
		return outcome;
	}
	
	public String update() {
		String outcome = null;
		try {
			issueService.updateIssue(issueDetail);
			issueDetail = new Issue();
			editMode = false;
			editId = null;
			Messages.addFlashGlobalInfo("Update was succesful");
			outcome = "list?faces-redirect=true";
		} catch(Exception e) {
			Messages.addGlobalError("Update was not successful");
			log.fine(e.getMessage());
		}
		
		return outcome;
	}
	
	public void delete(Issue existingIssue) {
		try {
			issueService.deleteIssue(existingIssue);
			issues.remove(existingIssue);
			Messages.addGlobalInfo("Delete was successful");
		} catch (Exception e) {
			Messages.addGlobalError("Delete was not successful");
			log.fine(e.getMessage());
		}
	}

	public String delete() {
		String nextUrl = null;
		try {
			issueService.deleteIssue(issueDetail);
			issues.remove(issueDetail);
			issueDetail = null;
			editId = null;
			Messages.addFlashGlobalInfo("Delete was successful");			
			nextUrl = "list?faces-redirect=true";
		} catch (Exception e) {
			Messages.addGlobalError("Delete was not successful");			
			log.fine(e.getMessage());
		}
		return nextUrl;
	}
	
	
	public void edit() {
		if (!Faces.isPostback() && !Faces.isValidationFailed() ) {
			if (editId != null) {
				try {
					issueDetail = issueService.findOneIssue(editId);
					if (issueDetail != null) {
						editMode = true;
					} else {
						Messages.addFlashGlobalError("{0} is not a valid id value", editId);
						Faces.navigate("list?faces-redirect=true");						
					}
				} catch (Exception e) {
					Messages.addGlobalError("Query unsucessful");
					log.fine(e.getMessage());	
				}	
			} else {
				Faces.navigate("list?faces-redirect=true");	
			}
		} 
	}
	
	public String cancel() {
		issueDetail = null;
		editMode = false;
		return "list?faces-redirect=true";
	}
	
	
	public List<Issue> getIssues() {
		return issues;
	}	
}