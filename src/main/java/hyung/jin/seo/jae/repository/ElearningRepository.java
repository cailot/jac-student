package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.model.Elearning;

public interface ElearningRepository extends JpaRepository<Elearning, Long>{  
	
	List<Elearning> findAll();
	
	List<Elearning> findAllByGrade(String grade);

	// @Query("SELECT e FROM Elearning e WHERE e.id IN (SELECT se.elearningId FROM Student_Elearning se WHERE se.studentId = ?1)")
	@Query(value = "SELECT * FROM Elearning WHERE id IN (SELECT elearningId FROM Student_Elearning WHERE studentId = ?1)", nativeQuery = true)   
	List<Elearning> findByStudentId(Long id);

	Optional<Elearning> findById(Long id);
	
	long count();
}
