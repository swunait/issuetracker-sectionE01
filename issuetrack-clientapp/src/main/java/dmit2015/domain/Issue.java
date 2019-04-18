package dmit2015.domain;

import java.time.LocalDate;
import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// This class is generated from http://www.jsonschema2pojo.org/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "issueId", "issueLabel", "issueDate" })
public class Issue {

	@JsonProperty("issueId")
	private Long issueId;
	
	@JsonProperty("issueLabel")
	private String issueLabel;
	
	@JsonbDateFormat(value = "yyyy-MM-dd")
	@JsonProperty("issueDate")
	private LocalDate issueDate;

	@JsonProperty("issueId")
	public Long getIssueId() {
		return issueId;
	}

	@JsonProperty("issueId")
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	@JsonProperty("issueLabel")
	public String getIssueLabel() {
		return issueLabel;
	}

	@JsonProperty("issueLabel")
	public void setIssueLabel(String issueLabel) {
		this.issueLabel = issueLabel;
	}

	@JsonProperty("issueDate")
	public LocalDate getIssueDate() {
		return issueDate;
	}

	@JsonProperty("issueDate")
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	
}
