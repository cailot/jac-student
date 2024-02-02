package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.StudentDTO;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.repository.StudentRepository;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.specification.StudentSpecification;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public List<Student> allStudents() {
		List<Student> students = new ArrayList<>();
		try{
			students = studentRepository.findAll();
		}catch(Exception e){
			System.out.println("No student found");
		}
		// studentRepository.findAll();
		return students;
	}
	
	@Override
	public List<Student> currentStudents() {
		List<Student> students = new ArrayList<>();
		try{
			students = studentRepository.findAllByEndDateIsNull();
		}catch(Exception e){
			System.out.println("No student found");
		}
		// studentRepository.findAllByEndDateIsNull();
		return students;
	}

	@Override
	public List<Student> stoppedStudents() {
		List<Student> students = new ArrayList<>();
		try{
			students = studentRepository.findAllByEndDateIsNotNull();
		}catch(Exception e){
			System.out.println("No student found");
		}
		// studentRepository.findAllByEndDateIsNotNull();
		return students;
	}



	@Override
	public List<StudentDTO> listStudents(String state, String branch, String grade, String year, String active) {
		
		String stateParam = StringUtils.equalsIgnoreCase(state, JaeConstants.ALL) ? "%" : state;
		String branchParam = StringUtils.equalsIgnoreCase(branch, JaeConstants.ALL) ? "%" : branch;
		String gradeParam = StringUtils.equalsAnyIgnoreCase(grade, JaeConstants.ALL) ? "%" : grade;
		int yearParam = StringUtils.equalsAnyIgnoreCase(year, JaeConstants.ALL) ? 0 : Integer.parseInt(year);
		
		List<StudentDTO> dtos = null;
		switch(active){
			case JaeConstants.CURRENT:
				dtos = studentRepository.listActiveStudent(stateParam, branchParam, gradeParam, yearParam);
				break;
			case JaeConstants.STOPPED:
				dtos = studentRepository.listInactiveStudent(stateParam, branchParam, gradeParam, yearParam);
		}
		return dtos;
	}
	
	@Override
	public List<Student> searchStudents(String keyword) {
		List<Student> students = new ArrayList<>();
		Specification<Student> spec = Specification.where(null);
		
		if(StringUtils.isNumericSpace(keyword)) {
			spec = spec.and(StudentSpecification.idEquals(keyword));
		}else {
			// firstName or lastName search
			spec = spec.and(StudentSpecification.nameContains(keyword));
		}
		try{
			students = studentRepository.findAll(spec);
		}catch(Exception e){
			System.out.println("No student found");
		}		
// students = studentRepository.findAll(spec);
		return students;
	}

	@Override
	public Student getStudent(Long id) {
		Student std = null;
		try{
			std = studentRepository.findById(id).get();
		}catch(Exception e){
			System.out.println("No student found");
		}
		// studentRepository.findById(id).get();	
		return std;
	}

	@Override
	public Student addStudent(Student std) {
		Student add = studentRepository.save(std);
		return add;
	}

	@Override
	public long checkCount() {
		long count = studentRepository.countByEndDateIsNull();
		return count;
	}

	@Override
	@Transactional
	public Student updateStudent(Student newStudent, Long id) {
		// search by getId
		Student existing = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found"));
		// Update info
		String newFirstName = StringUtils.defaultString(newStudent.getFirstName());
		existing.setFirstName(newFirstName);
		String newLastName = StringUtils.defaultString(newStudent.getLastName());
		existing.setLastName(newLastName);
		String newGrade = StringUtils.defaultString(newStudent.getGrade());
		existing.setGrade(newGrade);
		String newGender = StringUtils.defaultString(newStudent.getGender());
		existing.setGender(newGender);
		String newContactNo1 = StringUtils.defaultString(newStudent.getContactNo1());
		existing.setContactNo1(newContactNo1);
		String newContactNo2 = StringUtils.defaultString(newStudent.getContactNo2());
		existing.setContactNo2(newContactNo2);
		String newEmail1 = StringUtils.defaultString(newStudent.getEmail1());
		existing.setEmail1(newEmail1);
		String newEmail2 = StringUtils.defaultString(newStudent.getEmail2());
		existing.setEmail2(newEmail2);
		String newRelation1 = StringUtils.defaultString(newStudent.getRelation1());
		existing.setRelation1(newRelation1);
		String newRelation2 = StringUtils.defaultString(newStudent.getRelation2());
		existing.setRelation2(newRelation2);
		String newAddress = StringUtils.defaultString(newStudent.getAddress());
		existing.setAddress(newAddress);
		String newState = StringUtils.defaultString(newStudent.getState());
		existing.setState(newState);
		String newBranch = StringUtils.defaultString(newStudent.getBranch());
		existing.setBranch(newBranch);
		LocalDate newRegisterDate = newStudent.getRegisterDate();
		existing.setRegisterDate(newRegisterDate);
		String newMemo = StringUtils.defaultString(newStudent.getMemo());
		String existMemo = StringUtils.defaultString(existing.getMemo());
		
		// in case that user wants to clear memo
		if(StringUtils.isBlank(newMemo)) {
			// simply clear memo
			existing.setMemo("");
		}else if(!newMemo.equals(existMemo)) {
			// update memo with timestamp
			existing.setMemo(newMemo + JaeUtils.getTodayForMemo());
		}
		// update the existing record
		Student updated = studentRepository.save(existing);
		return updated;
	}

	@Override
	@Transactional
	public Student activateStudent(Long id) {
		Student student = null;
		try {
			// studentRepository.deleteById(id);
			Optional<Student> end = studentRepository.findById(id);
			if(end.isPresent()){
				Student std = end.get();
				std.setEndDate(null);
				student = studentRepository.save(std);
			}
			return student;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to activate");
		}
		return student;
	}
	
	@Override
	public void deactivateStudent(Long id) {
		try {
			// studentRepository.deleteById(id);
			Optional<Student> end = studentRepository.findById(id);
			if(!end.isPresent()) return; // if not found, terminate.
			Student std = end.get();
			std.setEndDate(LocalDate.now());
			studentRepository.save(std);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to discharge");
		}
	}

	@Override
	public void deleteStudent(Long id) {
		try {
			studentRepository.deleteById(id);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to delete");
		}

	}

	

}
