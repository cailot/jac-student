package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.PracticeAnswer;

public interface PracticeAnswerRepository extends JpaRepository<PracticeAnswer, Long>{  
	
	@SuppressWarnings("null")
	List<PracticeAnswer> findAll();
	
	@SuppressWarnings("null")
	Optional<PracticeAnswer> findById(Long id);

	@Query(value = "SELECT * FROM PracticeAnswer pa where pa.practiceId = :practiceId", nativeQuery = true)
	PracticeAnswer findPracticeAnswerByPractice(Long practiceId);
}
