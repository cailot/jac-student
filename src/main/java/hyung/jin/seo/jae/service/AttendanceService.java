package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.AttendanceDTO;
import hyung.jin.seo.jae.model.Attendance;

public interface AttendanceService {

	// list all attendances
	List<AttendanceDTO> allAttendances();

	// return total count
	long checkCount();

	// find attendance by id
	Attendance getAttendance(Long id);

	// find attendance by student Id, clazz Id and week
	Attendance getAttendanceByStudentAndClazzAndWeek(Long studentId, Long clazzId, int week);

	// add attendance
	AttendanceDTO addAttendance(Attendance attendance);

	// update attendance
	Attendance updateAttendance(Attendance attendance, Long id);

	// update status
	void updateStatus(String id, String status);

	// update status
	void updateStatus(Long studentId, Long clazzId, int week, String status);

	// update day
	void updateDay(Long id, String day);

	// delete attendance
	void deleteAttendance(Long id);

	// list attendances by student Id
	List<AttendanceDTO> findAttendanceByStudent(Long studentId);

	// list attendances by clazz Id
	List<AttendanceDTO> findAttendanceByClazz(Long claszzId);

	// list attendances by clazz Id and week
	List<AttendanceDTO> findAttendanceByClazzAndWeek(Long claszzId, int week);

	// list attendances by student Id and clazz Id
	List<AttendanceDTO> findAttendanceByStudentAndClazz(Long studentId, Long clazzId);

	// find attendance Id by student Id
	List<Long> findAttendanceIdByStudent(Long studentId);

	// find attendance Id by clazz Id
	List<Long> findAttendanceIdByClazz(Long clazzId);

	// find attendance Id by student Id & clazz Id
	List<Long> findAttendanceIdByStudentAndClazz(Long studentId, long clazzId);

	// find stduent Id by clazz Id
	List<Long> findStudentIdByClazz(Long clazzId);

}