package security.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

//import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

import security.entity.*;

@Singleton
//@DeclareRoles({"Administrator,Employee,Customer"})
public class LoginService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private Pbkdf2PasswordHash passwordHash;
	
//	@PermitAll
	public List<LoginGroup> findAllLoginGroup() {
		return entityManager.createQuery("SELECT lg FROM LoginGroup lg ORDER BY lg.groupname", LoginGroup.class).getResultList();
	}
	
	@Lock(LockType.READ)
	private Long findNextGroupId() {
		Long nextGroupId = entityManager.createQuery("SELECT MAX(lg.id) + 1 FROM LoginGroup lg", Long.class).getSingleResult();
		if (nextGroupId == null) {
			nextGroupId = 1L;
		}
		return nextGroupId;
	}
	
//	@RolesAllowed({"Administrator"})
	public void addLoginGroup(String groupname) throws Exception {
		if (findGroupByName(groupname) != null) {
			throw new Exception("The group name " + groupname + " already exists");
		}
		LoginGroup newLoginGroup = new LoginGroup();
		newLoginGroup.setGroupname(groupname);
		newLoginGroup.setId(findNextGroupId());
		try {
			entityManager.persist(newLoginGroup);
		} catch(Exception e) {
			throw new Exception("The group name already exists");
		}
	}
	
//	@RolesAllowed({"Administrator"})
	public void deleteLoginGroup(LoginGroup existingLoginGroup) {
		existingLoginGroup = entityManager.merge(existingLoginGroup);
		entityManager.remove(existingLoginGroup);
	}
	
	public LoginGroup findGroupByName(String groupname) {
		LoginGroup querySingleResult = null;
		
		try {
			querySingleResult = entityManager.createQuery(
				"SELECT lg FROM LoginGroup lg WHERE lg.groupname = :groupnameValue "
				, LoginGroup.class)
				.setParameter("groupnameValue", groupname)
				.getSingleResult();
		} catch(NoResultException e) {
			querySingleResult = null;
		}
		
		return querySingleResult;
	}
	
//	@RolesAllowed({"Administrator","Employee"})
	public List<LoginUser> findAllLoginUser() {
		return entityManager.createQuery("SELECT lu FROM LoginUser lu ORDER BY lu.username", LoginUser.class).getResultList();
	}
	
	@Lock(LockType.READ)
	private Long findNextUserId() {
		Long nextUserId = entityManager.createQuery("SELECT MAX(lu.id) + 1 FROM LoginUser lu", Long.class).getSingleResult();
		if (nextUserId == null) {
			nextUserId = 1L;
		}
		return nextUserId;
	}
	
//	private String generateHashPassword(String plainTextPassword) {
//		Map<String, String> parameters= new HashMap<>();
//		parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//		parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//		parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
//		passwordHash.initialize(parameters);
//        return passwordHash.generate(plainTextPassword.toCharArray());
//	}
	
	@Lock(LockType.WRITE)
	public void addLoginUser(String username, String plainTextPassword, String[] groupNames) throws NoSuchAlgorithmException {

		String hashedPassword = passwordHash.generate(plainTextPassword.toCharArray());
        
        LoginUser newLoginUser = new LoginUser();
        newLoginUser.setId( findNextUserId() );
        newLoginUser.setUsername(username);
        newLoginUser.setPassword(hashedPassword);

        if (groupNames != null && groupNames.length > 0 ) {
            List<LoginGroup> userGroups = new ArrayList<>();
            for(String singleGroupName : groupNames ) {
            	LoginGroup userGroup = findGroupByName(singleGroupName);
            	userGroups.add(userGroup);
            }
            newLoginUser.setGroups(userGroups);                  
        }
        
        entityManager.persist(newLoginUser);

	}
	
//	@RolesAllowed({"Administrator"})
	public void deleteLoginUser(LoginUser existingLoginUser) {
		existingLoginUser = entityManager.merge(existingLoginUser);
		entityManager.remove(existingLoginUser);
	}
	
	public LoginUser findOneUserByUsername(String username) {
		return entityManager.createQuery(
			"SELECT lu FROM LoginUser lu WHERE lu.username = :usernameValue"
			, LoginUser.class)
			.setParameter("usernameValue", username)
			.getSingleResult();
	}
	
	public void changePassword(String username, String currentPlainTextPassword, String newPlainTextPassword) throws Exception {
		char[] currentPassword = currentPlainTextPassword.toCharArray();
		
		LoginUser existingLoginUser = findOneUserByUsername(username);
		
//		String hasedPassword = passwordHash.generate(existingLoginUser.getPassword());
		// verify currentPlainTextPassword is valid
		if (passwordHash.verify(currentPassword, existingLoginUser.getPassword())) {
			String newHashedPassword = passwordHash.generate(newPlainTextPassword.toCharArray());
			existingLoginUser.setPassword(newHashedPassword);
			entityManager.merge(existingLoginUser);
		} else {
			throw new Exception("Current password is incorrect.");
		}
	}
}
