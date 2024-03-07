package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.model.Extrawork;

public interface ExtraworkRepository extends JpaRepository<Extrawork, Long>{  
	
	@SuppressWarnings("null")
	List<Extrawork> findAll();
	
	@SuppressWarnings("null")
	Optional<Extrawork> findById(Long id);
	
	// bring HomeworkDTO by id
	@Query("SELECT new hyung.jin.seo.jae.dto.ExtraworkDTO(e.id, e.videoPath, e.pdfPath, e.name, e.active, e.grade.code, e.registerDate) FROM Extrawork e WHERE (?1 = 'All' OR e.grade.code = ?1) AND (e.active=1) ORDER BY e.id")
	List<ExtraworkDTO> findActiveExtrawork(String grade);

	// filter ExtraworkDTO by grade
	@Query("SELECT new hyung.jin.seo.jae.dto.ExtraworkDTO(e.id, e.videoPath, e.pdfPath, e.name, e.active, e.grade.code, e.registerDate) FROM Extrawork e WHERE (?1 = 'All' OR e.grade.code = ?1)")
	List<ExtraworkDTO> filterExtraworkByGrade(String grade);

	// summarise Extawork by grade
	@Query(value = "SELECT e.name, e.id FROM Extrawork e WHERE e.gradeId = ?1", nativeQuery = true)   
	List<Object[]> summaryExtrawork(String grade);

}
