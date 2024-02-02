package hyung.jin.seo.jae.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.model.State;

public interface StateRepository extends JpaRepository<State, Long>{  
	
	List<State> findAll();
	
	@Query(value = "SELECT s.name, s.code FROM State s", nativeQuery = true)   
	List<Object[]> loadState();
}
