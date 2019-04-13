package dmit2015.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Issue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long issueId;
	
	@NotBlank(message="Enter an issue to report")
	@Column(nullable=false, length=99)
	private String issueLabel;
	
	@NotNull(message="Enter or select the Date of the issue")
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date issueDate = Calendar.getInstance().getTime();
}