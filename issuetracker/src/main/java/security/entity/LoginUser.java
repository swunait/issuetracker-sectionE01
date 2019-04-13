package security.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the User database table.
 * 
 */
@Entity
@NamedQuery(name="LoginUser.findAll", query="SELECT u FROM LoginUser u")
public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@NotBlank(message="Password value is required")
	@Column(nullable=false)
	private String password;

	@Size(min=3, max=64, message="Username value must contain 3 to 64 characters.")
	@Column(length=64, unique=true, nullable=false)
	private String username;

	//bi-directional many-to-many association to Role
//	@ManyToMany(mappedBy="users", fetch=FetchType.EAGER, cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="LoginUserGroup"
		, joinColumns={
			@JoinColumn(name="userid")
			}
		, inverseJoinColumns={
			@JoinColumn(name="groupid")
			}
		)
	private List<LoginGroup> groups = new ArrayList<>();

	public LoginUser() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<LoginGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<LoginGroup> groups) {
		this.groups = groups;
	}
	
}