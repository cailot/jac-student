package hyung.jin.seo.jae;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class SqlScriptRunner {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Environment env;

	@PostConstruct
	public void runSqlScripts() throws SQLException {
		String ddl = env.getProperty("spring.jpa.hibernate.ddl-auto");
		// run scripts only for 'create-drop' or 'create'
		if (StringUtils.startsWithIgnoreCase(ddl, "create")) {
			Connection connection = dataSource.getConnection();
			// ScriptUtils.executeSqlScript(connection,
			// resourceLoader.getResource("classpath:sql/elearning.sql")); // eLearning
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/course.sql")); // Course
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/subject.sql")); // Subject
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/cycle.sql")); // Cycle
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/course_subject.sql")); // Course_Subject
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/book.sql")); // Book																								
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/class.sql")); // Class
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/state.sql")); // State
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/branch.sql")); // Branch
			
			ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/braybrook_student.sql")); // Student
			//ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:sql/attendance_test.sql")); // attendane_test
		}
	}
}
