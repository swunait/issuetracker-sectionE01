package dmit2015.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;

@DataSourceDefinition(
	name="java:app/datasources/OracleIssueDS",
	className="oracle.jdbc.pool.OracleDataSource",
	url="jdbc:oracle:thin:@localhost:1521:xe",
	user="dmit2015",
	password="Password2015")
@ApplicationScoped
public class DataSourceDefinitionConfig {

}