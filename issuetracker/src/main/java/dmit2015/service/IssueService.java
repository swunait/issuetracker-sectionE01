package dmit2015.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import dmit2015.entity.Issue;
import dmit2015.repository.IssueRepository;

@Stateless
public class IssueService {

	@Inject
	private IssueRepository issueRepository;
	
	public void createIssue(Issue newIssue) {
		issueRepository.create(newIssue);
	}

	public void updateIssue(Issue existingIssue) {
		issueRepository.edit(existingIssue);
	}
	
	public void deleteIssue(Issue existingIssue) {
		issueRepository.remove(existingIssue);
	}
	
	public Issue findOneIssue(long issueID) {
		return issueRepository.find(issueID);
	}
	
	public List<Issue> findAllIssue() {
		return issueRepository.findAll();
	}
	
}