package security.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@CustomFormAuthenticationMechanismDefinition(
	loginToContinue = @LoginToContinue(
		loginPage="/login.xhtml", 
		useForwardToLogin = false,
		errorPage="")
)

@DatabaseIdentityStoreDefinition(
	dataSourceLookup="java:app/datasources/OracleIssueDS",
	callerQuery="SELECT password FROM LoginUser WHERE username = ?",
	groupsQuery="SELECT g.groupname FROM LoginUser u, LoginUserGroup ug, LoginGroup g WHERE u.username = ? AND u.id = ug.userid AND ug.groupid = g.id",
	priority = 10
)

@FacesConfig @ApplicationScoped
public class ApplicationSecurityConfig {

}
