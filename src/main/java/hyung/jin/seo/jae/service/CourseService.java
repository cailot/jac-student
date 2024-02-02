package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.CourseDTO;
import hyung.jin.seo.jae.model.Course;

public interface CourseService {
	// list all Course
	List<CourseDTO> allCourses();

	// list all Course by grade
	List<CourseDTO> findByGrade(String grade);
	
	// return total count
	long checkCount();

	// get Course by id
    Course getCourse(Long id);

	// register Course
	Course addCourse(Course course);	

	// update Course
	CourseDTO updateCourse(Course course);
}