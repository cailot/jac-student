package hyung.jin.seo.jae.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long>{  
	
	List<Grade> findAll();
	
	@Query(value = "SELECT g.name, g.code FROM Grade g", nativeQuery = true)   
	List<Object[]> loadGrade();

	@Query(value = "SELECT g.previous FROM Grade g WHERE g.code = ?1")   
	String getPrevious(String code);
}
