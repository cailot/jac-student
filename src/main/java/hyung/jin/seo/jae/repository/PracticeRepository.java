package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.dto.PracticeDTO;
import hyung.jin.seo.jae.model.Practice;

public interface PracticeRepository extends JpaRepository<Practice, Long>{  
	
	@SuppressWarnings("null")
	List<Practice> findAll();
	
	@SuppressWarnings("null")
	Optional<Practice> findById(Long id);
	
	// bring PracticeDTO by type, grade & volume
	@Query("SELECT new hyung.jin.seo.jae.dto.PracticeDTO(p.id, p.pdfPath, p.volume, p.questionCount, p.active, p.info, p.grade.code, p.practiceType.id, p.registerDate) FROM Practice p WHERE (p.practiceType.id = ?1) AND (p.grade.code = ?2) AND (p.volume = ?3)")
	PracticeDTO findPractice(int type, String grade, int volume);

	// filter PracticeDTO by type, grade & volume
	@Query("SELECT new hyung.jin.seo.jae.dto.PracticeDTO(p.id, p.pdfPath, p.volume, p.questionCount, p.active, p.info, p.grade.code, p.practiceType.id, p.registerDate) FROM Practice p WHERE (?1 = 0 OR p.practiceType.id = ?1) AND (?2 = 'All' OR p.grade.code = ?2) AND (?3 = 0 OR p.volume = ?3)")
	List<PracticeDTO> filterPracticeByTypeNGradeNVolume(int type, String grade, int volume);

	// summarise Extawork by grade
	@Query(value = "SELECT p.volume, p.id FROM Practice p WHERE p.practiceTypeId = ?1 AND p.gradeId = ?2", nativeQuery = true)   
	List<Object[]> summaryPractice(int type, int grade);

}
