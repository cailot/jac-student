package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.CourseDTO;
import hyung.jin.seo.jae.model.Course;
import hyung.jin.seo.jae.repository.CourseRepository;
import hyung.jin.seo.jae.repository.SubjectRepository;
import hyung.jin.seo.jae.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Override
	public long checkCount() {
		long count = courseRepository.count();
		return count;
	}

	@Override
	public List<CourseDTO> allCourses() {
		List<Course> crs = new ArrayList<>();
		try{
			crs = courseRepository.findAll();
		}catch(Exception e){
			System.out.println("No course found");
		}
		// courseRepository.findAll();
		List<CourseDTO> dtos = new ArrayList<>();
		for(Course course: crs){
			CourseDTO dto = new CourseDTO(course);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<CourseDTO> findByGrade(String grade) {
		// 1. get courses
		List<Course> crs = courseRepository.findByGrade(grade);
		// 2. get subjects
		List<String> subjects = subjectRepository.findSubjectAbbrForGrade(grade);
		// 3. create DTOs
		List<CourseDTO> dtos = new ArrayList<>();
		for(Course course: crs){
			CourseDTO dto = new CourseDTO(course);
			// 4. assign subjects to classes
			for(String subject : subjects){
				dto.addSubject(subject);
			}
			dtos.add(dto);
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
	public Course addCourse(Course course) {
		Course add = courseRepository.save(course);
		return add;
	}

	@Override
	public CourseDTO updateCourse(Course course) {
		// search by Id
		Course existing = courseRepository.findById(course.getId()).orElseThrow(() -> new EntityNotFoundException("Course Not Found"));
		// update grade
		String newGrade = course.getGrade();
		existing.setGrade(newGrade);
		// update name
		String newName = course.getName();
		existing.setName(newName);
		// update price
		double newPrice = course.getPrice();
		existing.setPrice(newPrice);
		// update online
		boolean newOnline = course.isOnline();
		existing.setOnline(newOnline);
		// update description
		String newDescription = course.getDescription();
		existing.setDescription(newDescription);
		// update the existing record
		Course updated = courseRepository.save(existing);
		CourseDTO dto = new CourseDTO(updated);
		return dto;
	}
}

