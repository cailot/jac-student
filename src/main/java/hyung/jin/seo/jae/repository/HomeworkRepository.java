package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Long>{  
	
	@SuppressWarnings("null")
	List<Homework> findAll();
	
	@SuppressWarnings("null")
	Optional<Homework> findById(Long id);
	
	// bring HomeworkDTO by id
	@Query("SELECT new hyung.jin.seo.jae.dto.HomeworkDTO(h.id, h.videoPath, h.pdfPath, h.week, h.year, h.info, h.active, h.grade.code, h.subject.id, h.registerDate) FROM Homework h WHERE h.subject.id = ?1 AND h.year = ?2  AND h.week = ?3")
	HomeworkDTO findHomework(long subjectId, int year, int week);

	// filter HomeworkDTO by subject, grade, year & year
	@Query("SELECT new hyung.jin.seo.jae.dto.HomeworkDTO(h.id, h.videoPath, h.pdfPath, h.week, h.year, h.info, h.active, h.grade.code, h.subject.id, h.registerDate) FROM Homework h WHERE (?1 = 0 OR h.subject.id = ?1) AND (?2 = 'All' OR h.grade.code = ?2) AND (?3 = 0 OR h.year = ?3) AND (?4 = 0 OR h.week = ?4)")
	List<HomeworkDTO> filterHomeworkBySubjectNGradeNYearNWeek(int subject, String grade, int year, int week);

}
