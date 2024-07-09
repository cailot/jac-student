package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.CourseDTO;
import hyung.jin.seo.jae.model.Course;

public interface CourseService {
	// list all Course
	List<CourseDTO> allCourses();

	// list all Course by grade
	List<CourseDTO> findByGrade(String grade);

	// list all active Course by grade
	List<CourseDTO> findActiveByGrade(String grade);

	// list all Course by grade & year
	List<CourseDTO> findByGradeNYear(String grade, int year);

	// list onsite Course by grade
	List<CourseDTO> findOnsiteByGrade(String grade);

	// list online Course by grade
	List<CourseDTO> findOnlineByGrade(String grade);
	
	// return total count
	long checkCount();

	// get Course by id
    Course getCourse(Long id);

	// register Course
	Course addCourse(Course course);	

	// update Course
	CourseDTO updateCourse(Course course, Long id);

	// de-activate Course
	void deactivateCourse(Long id);

	// re-activate Course
	void reactivateCourse(Long id);
}