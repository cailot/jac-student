package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.model.Teacher;

public interface TeacherService {

	// bring all students
	List<Teacher> allTeachers();

	// bring active students
	List<Teacher> currentTeachers();

	// bring inactive students
	List<Teacher> stoppedTeachers();

	// bring student list base on the condition
	List<Teacher> listTeachers(String state, String branch);

	// // search student list base on keyword where id, firstName or lastName
	// List<Teacher> searchTeachers(String keyword);

	Teacher getTeacher(Long id);

	Teacher addTeacher(Teacher std);

	long checkCount();

	Teacher updateTeacher(Teacher newTeacher, Long id);

	void reactivateTeacher(Long id);

	void dischargeTeacher(Long id);

	void deleteTeacher(Long id);

	void updateTeacherMemo(Long id, String memo);

	List<Long> getClazzIdByTeacher(Long id);
}
