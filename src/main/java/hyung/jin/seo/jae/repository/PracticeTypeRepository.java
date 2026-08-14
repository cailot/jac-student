package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.PracticeType;

public interface PracticeTypeRepository extends JpaRepository<PracticeType, Long>{  
	
	@Query(value = "SELECT p.name, p.id FROM PracticeType p", nativeQuery = true)   
	List<Object[]> loadPracticeType();

	// get groupId by id
	@Query(value = "SELECT p.practiceGroup FROM PracticeType p WHERE p.id = ?1", nativeQuery = true)
	int getPracticeGroupById(Long id);
	
}
