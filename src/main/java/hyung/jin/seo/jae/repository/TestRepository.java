package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.dto.TestDTO;
import hyung.jin.seo.jae.model.Test;

public interface TestRepository extends JpaRepository<Test, Long>{  
	
	@SuppressWarnings("null")
	List<Test> findAll();
	
	@SuppressWarnings("null")
	Optional<Test> findById(Long id);
	
	// bring PracticeDTO by type, grade & volume
	@Query("SELECT new hyung.jin.seo.jae.dto.TestDTO(t.id, t.pdfPath, t.volume, t.active, t.info, t.grade.code, t.testType.id, t.registerDate) FROM Test t WHERE (t.testType.id = ?1) AND (t.grade.code = ?2) AND (t.volume = ?3)")
	TestDTO findTest(int type, String grade, int volume);

	// filter PracticeDTO by type, grade & volume
	@Query("SELECT new hyung.jin.seo.jae.dto.TestDTO(t.id, t.pdfPath, t.volume, t.active, t.info, t.grade.code, t.testType.id, t.registerDate) FROM Test t WHERE (?1 = 0 OR t.testType.id = ?1) AND (?2 = 'All' OR t.grade.code = ?2) AND (?3 = 0 OR t.volume = ?3)")
	List<TestDTO> filterTestByTypeNGradeNVolume(int type, String grade, int volume);

	// summarise Test by grade
	@Query(value = "SELECT t.volume, t.id FROM Test t WHERE t.testTypeId = ?1 AND t.gradeId = ?2", nativeQuery = true)   
	List<Object[]> summaryTest(int type, int grade);

}
