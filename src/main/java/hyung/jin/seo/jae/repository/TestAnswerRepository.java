package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.TestAnswer;

public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long>{  
	
	@SuppressWarnings("null")
	List<TestAnswer> findAll();
	
	@SuppressWarnings("null")
	Optional<TestAnswer> findById(Long id);

	Optional<TestAnswer> findByTestId(Long testId);

	@Query(value = "SELECT * FROM TestAnswer ta where ta.testId = :testId", nativeQuery = true)
	TestAnswer findTestAnswerByTest(Long testId);
}
