package dmit2015.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import dmit2015.domain.Issue;

@ApplicationScoped
public class IssueService {

	final String BASE_URI = "http://localhost:8080/issuetracker/resources/issues";	
	private Client restClient;
	private WebTarget resource;

	@PostConstruct
	void init() {
		restClient = ClientBuilder.newClient();
		resource = restClient.target(BASE_URI);
	}
	
	public void createIssue(Issue newIssue) {
		resource.request(MediaType.APPLICATION_JSON).post( Entity.json(newIssue) );
	}

	public void updateIssue(Issue existingIssue) {
		resource.path("{id}").resolveTemplate("id", existingIssue.getIssueId())
			.request(MediaType.APPLICATION_JSON).put( Entity.json(existingIssue) );
	}
	
	public void deleteIssue(Issue existingIssue) {
		resource.path("{id}").resolveTemplate("id", existingIssue.getIssueId())
			.request().delete();
	}
	
	public Issue findOneIssue(long issueID) {
//		GenericType<Issue> responseType = new GenericType<Issue>() {};
		return resource.path("{id}").resolveTemplate("id", issueID)
				.request(MediaType.APPLICATION_JSON).get(Issue.class);
	}
	
	public List<Issue> findAllIssue() {
		GenericType<List<Issue>> responseType = new GenericType<List<Issue>>() {};
		return resource.request(MediaType.APPLICATION_JSON).get(responseType);
	}
	
}