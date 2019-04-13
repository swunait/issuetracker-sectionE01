package security.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;


/**
 * The persistent class for the Role database table.
 * 
 */
@Entity
@NamedQuery(name="LoginGroup.findAll", query="SELECT g FROM LoginGroup g")
public class LoginGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@NotBlank(message="Group Name value is required")
	@Column(length=64, unique=true, nullable=false)
	private String groupname;

	//bi-directional many-to-many association to User
//	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
//	@JoinTable(
//		name="LoginUserGroup"
//		, joinColumns={
//			@JoinColumn(name="groupid")
//			}
//		, inverseJoinColumns={
//			@JoinColumn(name="userid")
//			}
//		)
	@ManyToMany(mappedBy="groups")
	private List<LoginUser> users;

	public LoginGroup() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public List<LoginUser> getUsers() {
		return this.users;
	}

	public void setUsers(List<LoginUser> users) {
		this.users = users;
	}
	
	public void addLoginUser(LoginUser currentLoginUser) {
		this.users.add(currentLoginUser);
		currentLoginUser.getGroups().add(this);
	}
	
	public void removeLoginUser(LoginUser currentLoginUser) {
		this.users.remove(currentLoginUser);
		currentLoginUser.getGroups().remove(this);
	}

}