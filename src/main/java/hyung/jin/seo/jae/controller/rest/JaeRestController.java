package hyung.jin.seo.jae.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
import hyung.jin.seo.jae.dto.AttendanceDTO;
import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.dto.mobile.AttendanceRollClazzDTO;
import hyung.jin.seo.jae.dto.mobile.AttendanceRollStudentDTO;
import hyung.jin.seo.jae.dto.mobile.AttendanceRollTeacherDTO;
import hyung.jin.seo.jae.dto.mobile.TeacherLoginRequestDTO;
import hyung.jin.seo.jae.dto.mobile.TeacherLoginResponseDTO;
import hyung.jin.seo.jae.dto.mobile.TeacherPasswordUpdateRequestDTO;
import hyung.jin.seo.jae.model.Clazz;
import hyung.jin.seo.jae.model.Teacher;
import hyung.jin.seo.jae.service.AttendanceService;
import hyung.jin.seo.jae.service.ClazzService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.EnrolmentService;
import hyung.jin.seo.jae.service.TeacherService;
import hyung.jin.seo.jae.utils.JaeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("api")
public class JaeRestController {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private ClazzService clazzService;

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/clazzList/{id}")
	List<AttendanceRollClazzDTO> getClazzList(@PathVariable Long id) {
		List<AttendanceRollClazzDTO> dtos = new ArrayList<>();
		// 1. clazz ids
		List<Long> clazzIds = teacherService.getClazzIdByTeacher(id);
		int currentWeek = cycleService.academicWeeks();
		// 2. get clazz info & number of students
		for (Long clazzId : clazzIds) {
			AttendanceRollClazzDTO dto = new AttendanceRollClazzDTO();
			Clazz clazz = clazzService.getClazz(clazzId);
			ClazzDTO clazzDto = new ClazzDTO(clazz);
			Integer number = enrolmentService.getStudentNumberByClazz(clazzId, currentWeek);
			dto.setId(Long.toString(clazzId));
			dto.setName(clazzDto.getName());
			dto.setDescription(clazzDto.getDescription());
			dto.setDay(clazzDto.getDay());
			dto.setGrade(clazzDto.getGrade());
			dto.setNumber(number.toString());
			dtos.add(dto);
		}
		// 3. return dtos
		return dtos;
	}

	@GetMapping("/attendList/{id}")
	List<AttendanceRollStudentDTO> getAttendList(@PathVariable Long id) {
		List<AttendanceRollStudentDTO> dtos = new ArrayList<>();
		// 1. check currentWeek
		int currentWeek = cycleService.academicWeeks();
		// 2. get attend list
		List<AttendanceDTO> attendList = attendanceService.findAttendanceByClazzAndWeek(id, currentWeek);
		// 3. populate AttendanceRollStudentDTO
		for (AttendanceDTO attend : attendList) {
			AttendanceRollStudentDTO dto = new AttendanceRollStudentDTO();
			dto.setId(attend.getId());
			String status = attend.getStatus();
			if (status.equalsIgnoreCase(JaeConstants.ATTEND_OTHER)) {
				dto.setStatus(JaeConstants.ATTEND_NO); // by default, set status to 'N'
			} else {
				dto.setStatus(status);
			}
			dto.setStudentId(attend.getStudentId());
			dto.setStudentName(attend.getStudentName());
			dtos.add(dto);
		}
		// 4. return dtos
		return dtos;
	}

	@PutMapping("/updateAttend")
	@ResponseBody
	public ResponseEntity<String> updateAttendance(@RequestBody(required = false) List<Map<String, String>> infos) {
		// 1. check passed info
		if (infos == null || infos.isEmpty()) {
			return ResponseEntity.badRequest().body("\"Attendance update failed\"");
		}
		for (Map<String, String> info : infos) {
			info.entrySet().forEach(entry -> {
				String attendId = entry.getKey();
				String attendStatus = entry.getValue();
				// 2. update attendance if status is 'Y' or 'N'
				// if (attendStatus.equalsIgnoreCase(JaeConstants.ATTEND_YES)
				// || attendStatus.equalsIgnoreCase(JaeConstants.ATTEND_NO)) {
				attendanceService.updateStatus(attendId, attendStatus);
				// }
			});
		}
		// 4-1. return flag
		return ResponseEntity.ok("\"Attendance Update Success\"");
		// }
	}

	// get Teacher Info
	@GetMapping("/getTeacher/{id}")
	AttendanceRollTeacherDTO getTeacher(@PathVariable Long id) {
		AttendanceRollTeacherDTO dto = new AttendanceRollTeacherDTO();
		Teacher teacher = teacherService.getTeacher(id);
		if (teacher == null) {
			return dto;
		}
		dto.setId(Long.toString(teacher.getId()));
		dto.setFirstName(teacher.getFirstName());
		dto.setLastName(teacher.getLastName());
		dto.setEmail(teacher.getEmail());
		dto.setPhone(teacher.getPhone());
		dto.setPassword("");
		dto.setAddress(teacher.getAddress());
		dto.setVit(teacher.getVitNumber());
		return dto;
	}

	@PutMapping("/updateTeacher")
	@ResponseBody
	public ResponseEntity<String> updateTeacher(@RequestBody(required = true) AttendanceRollTeacherDTO info) {
		// 1. check passed info
		if (info == null || info.getId() == null || info.getId().isEmpty()) {
			return ResponseEntity.badRequest().body("\"Teacher update failed\"");
		}
		System.out.println(info);

		// 2. get Teacher
		Teacher existing = teacherService.getTeacher(Long.parseLong(info.getId()));
		
		existing.setFirstName(info.getFirstName());
		existing.setLastName(info.getLastName());
		existing.setPhone(info.getPhone());
		existing.setAddress(info.getAddress());
		existing.setVitNumber(info.getVit());

		// 3. update Teacher
		teacherService.updateTeacher(existing, existing.getId());

		// 4-1. return flag
		return ResponseEntity.ok("\"Teacher Information Updated Successfully\"");
		// }
	}

	@PutMapping("/updateTeacherPassword")
	@ResponseBody
	public ResponseEntity<String> updateTeacherPassword(
			@RequestBody(required = false) TeacherPasswordUpdateRequestDTO info) {
		if (info == null || StringUtils.isBlank(info.getTeacherId())
				|| StringUtils.isBlank(info.getCurrentPassword())
				|| StringUtils.isBlank(info.getNewPassword())) {
			return ResponseEntity.badRequest()
					.body("\"Current password and new password are required\"");
		}

		Teacher teacher;
		try {
			teacher = teacherService.getTeacher(Long.parseLong(info.getTeacherId().trim()));
		} catch (NumberFormatException ex) {
			return ResponseEntity.badRequest().body("\"Invalid teacher id\"");
		}
		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\"Teacher not found\"");
		}

		if (!passwordEncoder.matches(info.getCurrentPassword(), teacher.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("\"Current password is incorrect\"");
		}

		String newPassword = info.getNewPassword().trim();
		if (newPassword.length() < 4) {
			return ResponseEntity.badRequest()
					.body("\"New password must be at least 4 characters\"");
		}

		teacherService.updatePassword(teacher.getId(), encodeTeacherPassword(newPassword));
		return ResponseEntity.ok("\"Password updated successfully\"");
	}

	@PutMapping("/teacherLogin")
	@ResponseBody
	public ResponseEntity<TeacherLoginResponseDTO> loginCheck(
			@RequestBody(required = false) TeacherLoginRequestDTO info) {
		if (info == null || StringUtils.isBlank(info.getUserEmail())
				|| StringUtils.isBlank(info.getUserPassword())) {
			return ResponseEntity.badRequest().body(TeacherLoginResponseDTO.fail("Email and password are required"));
		}

		Teacher teacher = teacherService.findByEmail(info.getUserEmail());
		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(TeacherLoginResponseDTO.fail("Invalid email or password"));
		}

		if (teacher.getActive() != 1) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(TeacherLoginResponseDTO.fail("Teacher account is inactive"));
		}

		if (!passwordEncoder.matches(info.getUserPassword(), teacher.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(TeacherLoginResponseDTO.fail("Invalid email or password"));
		}

		return ResponseEntity.ok(TeacherLoginResponseDTO.ok(
				teacher.getId(),
				teacher.getEmail(),
				teacher.getFirstName(),
				teacher.getLastName()));
	}

	private String encodeTeacherPassword(String rawPassword) {
		String encoded = passwordEncoder.encode(rawPassword);
		if (encoded.startsWith("{bcrypt}")) {
			return encoded.substring("{bcrypt}".length());
		}
		return encoded;
	}

}
