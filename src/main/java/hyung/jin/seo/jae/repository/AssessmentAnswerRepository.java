package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.AssessmentAnswer;

public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, Long>{  
	
	@SuppressWarnings("null")
	List<AssessmentAnswer> findAll();
	
	@SuppressWarnings("null")
	Optional<AssessmentAnswer> findById(Long id);

	@Query(value = "SELECT * FROM AssessmentAnswer aa where aa.assessmentId = :assessmentId", nativeQuery = true)
	AssessmentAnswer findAssessmentAnswerByAssessment(Long assessmentId);

	@Modifying
	@Query(value = "DELETE FROM AssessmentAnswer where assessmentId = :assessmentId", nativeQuery = true)
	void deleteAssessmentAnswerByAssessment(Long assessmentId);
}
