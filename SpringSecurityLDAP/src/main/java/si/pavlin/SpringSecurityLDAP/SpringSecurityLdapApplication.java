package si.pavlin.SpringSecurityLDAP;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


@SpringBootApplication
public class SpringSecurityLdapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityLdapApplication.class, args);
		List<String> osebList = new getAllPersonNames();
		System.out.println(Arrays.toString(osebList.toArray()));
	}

	private LdapTemplate ldapTemplate;
	
	@Autowired
	public List<String> getPerson(String Username) {
		LdapQuery query = query()
			.base("dc=springframework,dc=org")
			.attributes("cn", "uid")
			.where("objectclass").is("person")
			.and("uid").is(Username);

		return ldapTemplate.search(query,
		new AttributesMapper<String>() {
				public String mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
				return	(String) attrs.get("uid").get();
			}
		});
	}

	@Autowired
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Autowired
	public List<String> getAllPersonNames() {
		return ldapTemplate.search(
			query().where("objectclass").is("person"),
			new AttributesMapper<String>() {
				public String mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {
				return (String) attrs.get("cn").get();
				}
			});
	}

}
