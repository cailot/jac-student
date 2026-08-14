package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.OnlineActivityDTO;
import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.OnlineActivity;
import hyung.jin.seo.jae.model.OnlineSession;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.repository.OnlineActivityRepository;
import hyung.jin.seo.jae.repository.OnlineSessionRepository;
import hyung.jin.seo.jae.repository.StudentRepository;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.OnlineActivityService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Service
public class OnlineActivityServiceImpl implements OnlineActivityService {
	private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	@Autowired
	private OnlineActivityRepository onlineActivityRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private OnlineSessionRepository onlineSessionRepository;

	@Autowired
	private CycleService cycleService;

	@Override
	@Transactional
	public void addOnlineActivity(Long studentId, Long onlineSessionId, String onlineFileNm) {
		OnlineActivity activity = new OnlineActivity();
		Student student = studentRepository.findById(studentId).get();
		activity.setStudent(student);
		if (onlineSessionId != null && onlineSessionId > 0) {
			OnlineSession session = onlineSessionRepository.findById(onlineSessionId).orElse(null);
			activity.setOnlineSession(session);
		}
		activity.setOnlineFileNm(StringUtils.trimToEmpty(onlineFileNm));
		LocalDateTime now = LocalDateTime.now();
		activity.setStartDateTime(now);
		activity.setStatus(JaeConstants.STATUS_PROCESSING);
		onlineActivityRepository.save(activity);
	}

	@Override
	public OnlineActivity getOnlineActivity(Long studentId, Long onlineSessionId) {
		return onlineActivityRepository.findByStudentIdAndOnlineSessionId(studentId, onlineSessionId);
	}

	@Override
	public OnlineActivity getOnlineActivity(Long studentId, String onlineFileNm) {
		if (studentId == null || StringUtils.isBlank(onlineFileNm)) {
			return null;
		}
		int currentYear = cycleService.academicYear();
		int currentWeek = cycleService.academicWeeks();
		if (currentYear <= 0 || currentWeek <= 0) {
			return null;
		}
		String weekStartText = cycleService.academicStartMonday(currentYear, currentWeek);
		String weekEndText = cycleService.academicEndSunday(currentYear, currentWeek);
		LocalDate weekStart = LocalDate.parse(weekStartText, DD_MM_YYYY);
		LocalDate weekEnd = LocalDate.parse(weekEndText, DD_MM_YYYY);
		return onlineActivityRepository.findTopByStudentIdAndOnlineFileNmAndRegisterDateBetweenOrderByIdDesc(
				studentId, StringUtils.trim(onlineFileNm), weekStart, weekEnd);
	}

	@Override
	@Transactional
	public OnlineActivity updateOnlineActivity(OnlineActivity activity, Long id, String onlineFileNm) {
		// search by id
		OnlineActivity existingActivity = onlineActivityRepository.findById(id).orElseThrow();
		// update info
		// start time
		if(activity.getStartDateTime() != existingActivity.getStartDateTime()) {
			existingActivity.setStartDateTime(activity.getStartDateTime());
		}
		// end time
		if(activity.getEndDateTime() != existingActivity.getEndDateTime()) {
			existingActivity.setEndDateTime(activity.getEndDateTime());
		}
		// status
		if(activity.getStatus() > 0) {
			existingActivity.setStatus(activity.getStatus());
		}
		// file name
		if (StringUtils.isNotBlank(onlineFileNm)) {
			existingActivity.setOnlineFileNm(StringUtils.trim(onlineFileNm));
		}
		// save
		return onlineActivityRepository.save(existingActivity);
	}

	// @Override
	// public List<OnlineActivityDTO> getStudentStatus(Long studentId, int week) {
	// 	List<OnlineActivityDTO> dtos = new ArrayList<>();
	// 	try{
	// 		dtos = onlineActivityRepository.getStudentStatus(studentId, week);
	// 	}catch(Exception e){
	// 		System.out.println("No student found");
	// 	}
	// 	return dtos;
	// }

	@Override
	public OnlineActivityDTO getStudentStatus(Long studentId, OnlineSessionDTO session) {
		OnlineActivityDTO dto = null;
		try{
			dto = onlineActivityRepository.getStudentStatus(studentId, Long.parseLong(session.getId()));
		}catch(Exception e){
			System.out.println("No student found");
		}
		// if dto = null, then create skeleton
		if(dto == null) {
			dto = new OnlineActivityDTO();
			Optional<Student> std = studentRepository.findById(studentId);
			if(std.isPresent()) {
				dto.setFirstName(std.get().getFirstName());
				dto.setLastName(std.get().getLastName());
				dto.setGrade(std.get().getGrade());
				dto.setContactNo(std.get().getContactNo1());
				dto.setEmail(std.get().getEmail1());
			}
			dto.setStudentId(Long.toString(studentId));
			dto.setOnlineSessionId(session.getId());
			dto.setOnlineName(session.getTitle());
			dto.setSet(session.getWeek());
			dto.setStatus(JaeConstants.STATUS_NOTHING);
		}
		return dto;
	}
		
}
