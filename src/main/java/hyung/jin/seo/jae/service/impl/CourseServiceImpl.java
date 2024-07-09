package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.CourseDTO;
import hyung.jin.seo.jae.model.Course;
import hyung.jin.seo.jae.model.Subject;
import hyung.jin.seo.jae.repository.CourseRepository;
import hyung.jin.seo.jae.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public long checkCount() {
		long count = courseRepository.count();
		return count;
	}

	@Override
	public List<CourseDTO> allCourses() {
		List<CourseDTO> dtos = new ArrayList<>();
		try{
			dtos = courseRepository.getAll();
		}catch(Exception e){
			System.out.println("No course found");
		}
		return dtos;
	}

	@Override
	public List<CourseDTO> findByGrade(String grade) {
		// 1. get courses
		List<CourseDTO> dtos = new ArrayList<>();
		try{
			dtos = courseRepository.getByGrade(grade);
		}catch(Exception e){
			System.out.println("No course found");
		}
		return dtos;
	}

	@Override
	public List<CourseDTO> findActiveByGrade(String grade) {
		// 1. get courses
		List<CourseDTO> dtos = new ArrayList<>();
		try{
			dtos = courseRepository.getActiveByGrade(grade);
		}catch(Exception e){
			System.out.println("No course found");
		}
		return dtos;
	}

	@Override
	public List<CourseDTO> findByGradeNYear(String grade, int year) {
		// 1. get courses
		List<CourseDTO> dtos = new ArrayList<>();
		try{
			dtos = courseRepository.getByGradeNYear(grade, year);
		}catch(Exception e){
			System.out.println("No course found");
		}
		return dtos;
	}


	@Override
	public Course getCourse(Long id) {
		Optional<Course> course = courseRepository.findById(id);
		if(course.isPresent()) {
			return course.get();
		}else {
			return null;
		}
	}
	
	@Override
	@Transactional
	public Course addCourse(Course course) {
		Course add = courseRepository.save(course);
		return add;
	}

	@Override
	@Transactional
	public CourseDTO updateCourse(Course course, Long id) {
		// search by Id
		Course existing = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course Not Found"));
		// update grade
		String newGrade = course.getGrade();
		existing.setGrade(newGrade);
		// update name
		String newName = course.getName();
		existing.setName(newName);
		// update online
		boolean newOnline = course.isOnline();
		existing.setOnline(newOnline);
		// update description
		String newDescription = course.getDescription();
		existing.setDescription(newDescription);
		// update price
		double newPrice = course.getPrice();
		existing.setPrice(newPrice);
		// update active
		boolean newActive = course.isActive();
		existing.setActive(newActive);
		// update associated subjects
		List<Subject> newSubjects = course.getSubjects();
		existing.setSubjects(newSubjects);
		// update the existing record
		Course updated = courseRepository.save(existing);
		CourseDTO dto = new CourseDTO(updated);
		return dto;
	}


	@Override
	public List<CourseDTO> findOnsiteByGrade(String grade) {
		// 1. get courses
		List<CourseDTO> dtos = courseRepository.findOnsiteByGrade(grade);
		// 2. get subjects
		//List<String> subjects = subjectRepository.findSubjectAbbrForGrade(grade);
		return dtos;
	}

	@Override
	public List<CourseDTO> findOnlineByGrade(String grade) {
		// 1. get courses
		List<CourseDTO> dtos = courseRepository.findOnlineByGrade(grade);
		// 2. get subjects
		//List<String> subjects = subjectRepository.findSubjectAbbrForGrade(grade);
		return dtos;
	}

	@Override
	@Transactional
	public void deactivateCourse(Long id) {
		// 1. get Course
		// Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course Not Found"));
		// 2. remove the associations between subjects and Course
		// course.setSubjects(new ArrayList<>());
		// courseRepository.save(course);
		// 3. set flag to false
		courseRepository.updateCourseSetActiveFalseById(id);
	}

	@Override
	@Transactional
	public void reactivateCourse(Long id) {
		// 3. set flag to true
		courseRepository.updateCourseSetActiveTrueById(id);
	}
	
}
