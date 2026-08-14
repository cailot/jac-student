package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import hyung.jin.seo.jae.model.GuestStudent;

public interface GuestStudentRepository extends JpaRepository<GuestStudent, Long>{  
	
	List<GuestStudent> findAll();	
	
}
