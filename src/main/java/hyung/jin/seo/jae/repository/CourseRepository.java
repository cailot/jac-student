package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import hyung.jin.seo.jae.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{  
	
	List<Course> findAll();

	List<Course> findByGrade(String grade);

	long count();
}
