package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.model.Teacher;
import hyung.jin.seo.jae.repository.TeacherRepository;
import hyung.jin.seo.jae.service.TeacherService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherRepository teacherRepository;

	@Override
	public List<Teacher> allTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		try {
			teachers = teacherRepository.findAll();
		} catch (Exception e) {
			System.out.println("No teacher found");
		}
		return teachers;
	}

	@Override
	public List<Teacher> currentTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		try {
			teachers = teacherRepository.findAllByEndDateIsNull();
		} catch (Exception e) {
			System.out.println("No teacher found");
		}
		return teachers;
	}

	@Override
	public List<Teacher> stoppedTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		try {
			teachers = teacherRepository.findAllByEndDateIsNotNull();
		} catch (Exception e) {
			System.out.println("No teacher found");
		}
		return teachers;
	}

	@Override
	public List<Teacher> listTeachers(String state, String branch) {
		List<Teacher> teachers = teacherRepository.findByState(state);
		boolean isStateAll = StringUtils.equals(state, JaeConstants.ALL);
		boolean isBranchAll = StringUtils.equals(branch, JaeConstants.ALL);
		if(isStateAll){
			// branch 
			if(isBranchAll){
				// all
				teachers = teacherRepository.findAll();
			}else{
				// branch only
				teachers = teacherRepository.findByBranch(branch);
			}
		}else{
			// state & branch
			if(isBranchAll){
				// state only
				teachers = teacherRepository.findByState(state);
			}else{
				// state & branch
				teachers = teacherRepository.findByStateAndBranch(state, branch);
			}
		}
		return teachers;
	}

	@Override
	public Teacher getTeacher(Long id) {
		Teacher teacher = null;
		try {
			teacher = teacherRepository.findById(id).get();
		} catch (Exception e) {
			System.out.println("No teacher found");
		}
		return teacher;
	}

	@Override
	public Teacher addTeacher(Teacher teacher) {
		Teacher add = teacherRepository.save(teacher);
		return add;
	}

	@Override
	public long checkCount() {
		long count = teacherRepository.count();
		return count;
	}

	@Override
	// @Transactional
	public Teacher updateTeacher(Teacher newVal, Long id) {
		// search by getId
		Teacher existing = teacherRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
		// Update info
		String newFirstName = StringUtils.defaultString(newVal.getFirstName());
		if (StringUtils.isNotBlank(newFirstName)) {
			existing.setFirstName(newFirstName);
		}
		String newLastName = StringUtils.defaultString(newVal.getLastName());
		if (StringUtils.isNotBlank(newLastName)) {
			existing.setLastName(newLastName);
		}
		String newTitle = StringUtils.defaultString(newVal.getTitle());
		if (StringUtils.isNotBlank(newTitle)) {
			existing.setTitle(newTitle);
		}
		String newPhone = StringUtils.defaultString(newVal.getPhone());
		if (StringUtils.isNotBlank(newPhone)) {
			existing.setPhone(newPhone);
		}
		String newEmail = StringUtils.defaultString(newVal.getEmail());
		if (StringUtils.isNotBlank(newEmail)) {
			existing.setEmail(newEmail);
		}
		String newAddress = StringUtils.defaultString(newVal.getAddress());
		if (StringUtils.isNotBlank(newAddress)) {
			existing.setAddress(newAddress);
		}
		String newState = StringUtils.defaultString(newVal.getState());
		if (StringUtils.isNotBlank(newState)) {
			existing.setState(newState);
		}
		String newBranch = StringUtils.defaultString(newVal.getBranch());
		if (StringUtils.isNotBlank(newBranch)) {
			existing.setBranch(newBranch);
		}
		String newMemo = StringUtils.defaultString(newVal.getMemo());
		if (StringUtils.isNotBlank(newMemo)) {
			existing.setMemo(newMemo);
		}

		String newBank = StringUtils.defaultString(newVal.getBank());
		if (StringUtils.isNotBlank(newBank)) {
			existing.setBank(newBank);
		}
		String newBsb = StringUtils.defaultString(newVal.getBsb());
		if (StringUtils.isNotBlank(newBsb)) {
			existing.setBsb(newBsb);
		}
		Long newAccountNumber = (newVal.getAccountNumber()) != null ? (newVal.getAccountNumber()) : 0;
		existing.setAccountNumber(newAccountNumber);

		String newsuperannuation = StringUtils.defaultString(newVal.getSuperannuation());
		if (StringUtils.isNotBlank(newsuperannuation)) {
			existing.setSuperannuation(newsuperannuation);
		}

		String newVitNumber = StringUtils.defaultString(newVal.getVitNumber());
		if (StringUtils.isNotBlank(newVitNumber)) {
			existing.setVitNumber(newVitNumber);
		}

		String newSuperMember = StringUtils.defaultString(newVal.getSuperMember());
		if (StringUtils.isNotBlank(newSuperMember)) {
			existing.setSuperMember(newSuperMember);
		}
		Long newTaxNumber = (newVal.getTfn()) != null ? (newVal.getTfn()) : 0;
		existing.setTfn(newTaxNumber);
		if (newVal.getStartDate() != null) {
			LocalDate newStartDate = newVal.getStartDate();
			existing.setStartDate(newStartDate);
		}
		if (newVal.getEndDate() != null) {
			LocalDate newEndDate = newVal.getEndDate();
			existing.setEndDate(newEndDate);
		}

		// update the existing record
		Teacher updated = teacherRepository.save(existing);
		return updated;
	}

	@Override
	public void reactivateTeacher(Long id) {
		try {
			Optional<Teacher> end = teacherRepository.findById(id);
			if (!end.isPresent())
				return; // if not found, terminate.
			Teacher teacher = end.get();
			teacher.setEndDate(null);
			teacherRepository.save(teacher);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to discharge");
		}
	}

	@Override
	public void dischargeTeacher(Long id) {
		try {
			Optional<Teacher> end = teacherRepository.findById(id);
			if (!end.isPresent())
				return; // if not found, terminate.
			Teacher teacher = end.get();
			teacher.setEndDate(LocalDate.now());
			teacherRepository.save(teacher);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to discharge");
		}
	}

	@Override
	public void deleteTeacher(Long id) {
		try {
			teacherRepository.deleteById(id);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to delete");
		}
	}

	@Override
	public void updateTeacherMemo(Long id, String memo) {
		try {
			Optional<Teacher> teacher = teacherRepository.findById(id);
			if (teacher.isPresent()) {
				Teacher t = teacher.get();
				t.setMemo(memo);
				teacherRepository.save(t);
			}
		} catch (Exception e) {
			System.out.println("No teacher found");
		}
	}

	@Override
	public List<Long> getClazzIdByTeacher(Long id) {
		List<Long> ids = new ArrayList<>();
		try {
			ids = teacherRepository.findClazzIdByTeacherId(id);
		} catch (Exception e) {
			System.out.println("No teacher found");
		}
		return ids;
	}

}
