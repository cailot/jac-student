package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{  
	
	@Query("SELECT DISTINCT s.name FROM Course c JOIN c.subjects s WHERE c.grade = ?1")
	List<String> findSubjectNamesForGrade(String grade);

	@Query("SELECT DISTINCT s.abbr FROM Course c JOIN c.subjects s WHERE c.grade = ?1")
	List<String> findSubjectAbbrForGrade(String grade);
}
