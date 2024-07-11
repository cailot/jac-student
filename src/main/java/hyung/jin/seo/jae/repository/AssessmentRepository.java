package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.dto.AssessmentDTO;
import hyung.jin.seo.jae.model.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long>{  
	
	@SuppressWarnings("null")
	List<Assessment> findAll();
	
	@SuppressWarnings("null")
	Optional<Assessment> findById(Long id);
	
	// bring AssessmentDTO by grade & subject
	@Query("SELECT new hyung.jin.seo.jae.dto.AssessmentDTO(a.id, a.pdfPath, a.active, a.grade.code, a.subject.id, a.registerDate) FROM Assessment a WHERE (a.grade.code = ?1) AND (a.subject.id = ?2)")
	AssessmentDTO findAssessment(String grade, long subjectId);

	// bring AssessmentDTO by grade
	@Query("SELECT new hyung.jin.seo.jae.dto.AssessmentDTO(a.id, a.pdfPath, a.active, a.grade.code, a.subject.id, a.registerDate) FROM Assessment a WHERE (?1 = '0' OR a.grade.code = ?1) AND (?2 = 0L OR a.subject.id = ?2)")
	List<AssessmentDTO> findAssessmentByGradeNSubject(String grade, long subject);
	
	// bring AssessmentDTO by grade
	@Query("SELECT new hyung.jin.seo.jae.dto.AssessmentDTO(a.id, a.pdfPath, a.active, a.grade.code, a.subject.id, a.registerDate) FROM Assessment a WHERE ?1 = '0' OR a.grade.code = ?1")
	List<AssessmentDTO> findAssessmentByGrade(String grade);

	// bring AssessmentDTO by subject
	@Query("SELECT new hyung.jin.seo.jae.dto.AssessmentDTO(a.id, a.pdfPath, a.active, a.grade.code, a.subject.id, a.registerDate) FROM Assessment a WHERE ?1 = 0L OR a.subject.id = ?1")
	List<AssessmentDTO> findAssessmentBySubject(long subject);

}
