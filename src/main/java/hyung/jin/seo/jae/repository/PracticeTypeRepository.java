package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.PracticeType;

public interface PracticeTypeRepository extends JpaRepository<PracticeType, Long>{  
	
	@Query(value = "SELECT p.name, p.id FROM PracticeType p", nativeQuery = true)   
	List<Object[]> loadPracticeType();
}
