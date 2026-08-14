package hyung.jin.seo.jae.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	// bring TestDTO by group & volume
	@Query("SELECT new hyung.jin.seo.jae.dto.TestDTO(t.id, t.pdfPath, t.volume, t.active, t.info, t.grade.code, t.testType.id, t.registerDate, t.testType.name) FROM Test t WHERE (t.testType.testGroup = ?1) AND (t.grade.code = ?2) AND (t.volume = ?3) AND (t.active = true)")
	List<TestDTO> getTestByGroupNGradeNWeek(int type, String grade, int volume);

	// summarise Test by grade
	@Query(value = "SELECT t.volume, t.id FROM Test t WHERE t.testTypeId = ?1 AND t.gradeId = ?2  AND t.active = true", nativeQuery = true)   
	List<Object[]> summaryTest(int type, int grade);

	// bring TestDTO by id
	@Query("SELECT new hyung.jin.seo.jae.dto.TestDTO(t.id, t.pdfPath, t.volume, t.active, t.info, t.grade.code, t.testType.id, t.registerDate, t.testType.name) FROM Test t WHERE (t.id = ?1) AND (t.active = true)")
	TestDTO getTestById(Long id);

	// bring all TestDTO of which 'testTypeId' is same
	@Query("SELECT new hyung.jin.seo.jae.dto.TestDTO(t.id, t.volume, t.average, t.testType.id) FROM Test t WHERE (t.testType.id = (SELECT t2.testType.id FROM Test t2 WHERE t2.id = :id)) AND (t.registerDate BETWEEN :fromTime AND :toTime)")
	List<TestDTO> getTestByType( @Param("id") long id, @Param("fromTime") LocalDate fromTime, @Param("toTime") LocalDate toTime);

	// get Test name by id
	@Query("SELECT t.testType.name FROM Test t WHERE t.id = ?1")
	String getTestTypeName(Long id);

	// get test group by id
	@Query("SELECT t.testType.testGroup FROM Test t WHERE t.id = ?1")
	int getTestGroup(Long id);

	// get test volume by id
	@Query("SELECT t.volume FROM Test t WHERE t.id = ?1")
	int getTestVolume(Long id);

	// get test grade by id
	@Query("SELECT t.grade.code FROM Test t WHERE t.id = ?1")
	String getTestGrade(Long id);
}