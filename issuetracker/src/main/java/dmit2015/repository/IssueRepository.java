package dmit2015.repository;

import dmit2015.entity.Issue;

public class IssueRepository extends AbstractJpaRepository<Issue> {

	public IssueRepository() {
		super(Issue.class);
	}

}