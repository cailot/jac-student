package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.AttendanceDTO;
import hyung.jin.seo.jae.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	// bring AttendanceDTO by clazz id
	@Query("SELECT new hyung.jin.seo.jae.dto.AttendanceDTO(a.id, a.attendDate, a.status, a.week, a.info, a.student.id, a.student.firstName, a.student.lastName, a.clazz.id, a.day, a.clazz.course.grade, a.clazz.name) FROM Attendance a WHERE a.clazz.id = ?1 ORDER BY a.attendDate")
	List<AttendanceDTO> findAttendanceByClazzId(long clazzId);

	// bring AttendanceDTO by student id
	@Query("SELECT new hyung.jin.seo.jae.dto.AttendanceDTO(a.id, a.attendDate, a.status, a.week, a.info, a.student.id, a.student.firstName, a.student.lastName, a.clazz.id, a.day, a.clazz.course.grade, a.clazz.name) FROM Attendance a WHERE a.student.id = ?1 ORDER BY a.attendDate")
	List<AttendanceDTO> findAttendanceByStudentId(long studentId);

	// bring AttendanceDTO by student id & clazz id
	@Query("SELECT new hyung.jin.seo.jae.dto.AttendanceDTO(a.id, a.attendDate, a.status, a.week, a.info, a.student.id, a.student.firstName, a.student.lastName, a.clazz.id, a.day, a.clazz.course.grade, a.clazz.name) FROM Attendance a WHERE a.student.id = ?1 AND a.clazz.id = ?2 ORDER BY a.attendDate")
	List<AttendanceDTO> findAttendanceByStudentIdAndClazzId(long studentId, long clazzId);

	// return attendance by student id, clazz id and week
	@Query("SELECT a FROM Attendance a WHERE a.student.id = ?1 AND a.clazz.id = ?2 AND a.week = ?3")
	Attendance getAttendanceByStudentIdAndClazzIdAndWeek(long studentId, long clazzId, String week);

	// upate day
	@Modifying
	@Transactional
	@Query("UPDATE Attendance a SET a.day = ?2, a.status = 'Y' WHERE a.id = ?1")
	void updateDay(long id, String day);

	@Modifying
	@Transactional
	@Query("UPDATE Attendance a SET a.status = ?2 WHERE a.id = ?1")
	void updateStatusById(
			long id,
			String newStatus);

	@Modifying
	@Transactional
	@Query("UPDATE Attendance a SET a.status = ?4 WHERE a.student.id = ?1 AND a.clazz.id = ?2 AND a.week = ?3")
	void updateStatusByStudentIdAndClazzIdAndWeek(
			long studentId,
			long clazzId,
			String week,
			String newStatus);

	// return attendance id by clazz id
	@Query("SELECT a.id FROM Attendance a WHERE a.clazz.id = ?1")
	List<Long> findAttendanceIdByClazzId(long clazzId);

	// return attendance dto by clazz id and week
	@Query("SELECT new hyung.jin.seo.jae.dto.AttendanceDTO(a.id, a.attendDate, a.status, a.week, a.info, a.student.id, a.student.firstName, a.student.lastName, a.clazz.id, a.day, a.clazz.course.grade, a.clazz.name) FROM Attendance a WHERE a.clazz.id = ?1 AND a.week = ?2 ORDER BY a.student.id")
	List<AttendanceDTO> findAttendanceIdByClazzIdAndWeek(long clazzId, String week);

	// return attendance id by student id
	@Query("SELECT a.id FROM Attendance a WHERE a.student.id = ?1")
	List<Long> findAttendanceIdByStudentId(long studentId);

	// return attendance id by student id & clazz id
	@Query("SELECT a.id FROM Attendance a WHERE a.student.id = ?1 AND a.clazz.id = ?2")
	List<Long> findAttendanceIdByStudentIdAndClazzId(long studentId, long clazzId);

	// return student id by clazz id
	@Query("SELECT DISTINCT a.student.id FROM Attendance a WHERE a.clazz.id = ?1")
	List<Long> findStudentIdByClazzId(long clazzId);

}
