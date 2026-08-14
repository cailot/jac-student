package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.OnlineSession;
import hyung.jin.seo.jae.repository.EnrolmentRepository;
import hyung.jin.seo.jae.repository.OnlineSessionRepository;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.OnlineSessionService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Service
public class OnlineSessionServiceImpl implements OnlineSessionService {

    private final EnrolmentRepository enrolmentRepository;

	@Autowired
	private CycleService cycleService;
	

	@Autowired
	private OnlineSessionRepository onlineSessionRepository;

    OnlineSessionServiceImpl(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> allOnlineSessions() {
		List<OnlineSession> sessions = new ArrayList<>();
		try{
			sessions = onlineSessionRepository.findAll();
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		for(OnlineSession session: sessions){
			OnlineSessionDTO dto = new OnlineSessionDTO(session);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> findOnlineSessionByClazz(Long clazzId) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.findOnlineSessionByClazzId(clazzId);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> findActiveOnlineSessionByClazz(Long clazzId) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.findActiveOnlineSessionByClazzId(clazzId);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> findInactiveOnlineSessionByClazz(Long clazzId) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.findInactiveOnlineSessionByClazzId(clazzId);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> filterOnlineSessionByGradeNYear(String grade, int year) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.filterOnlineSessionByGradeNYear(grade, year);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public long checkCount() {
		long count = onlineSessionRepository.count();
		return count;
	}

	@Override
	@Transactional
	public OnlineSession addOnlineSession(OnlineSession session) {
		OnlineSession add = onlineSessionRepository.save(session);
	 	return add;
	}

	@Override
	@Transactional
	public OnlineSession updateOnlineSession(OnlineSession session, Long id) {
		// search by getId
		OnlineSession existing = onlineSessionRepository.findById(session.getId())
				.orElseThrow(() -> new EntityNotFoundException("OnlineSession Not Found"));
		// Update info
		// active
		boolean newActive = session.isActive();
		existing.setActive(newActive);
		// address
		String newAddress = session.getAddress();
		existing.setAddress(newAddress);
		// day
		String newDay = session.getDay();
		existing.setDay(newDay);
		// startTime
		String newStart = session.getStartTime();
		existing.setStartTime(newStart);
		// endTime
		String newEnd = session.getEndTime();
		existing.setEndTime(newEnd);
		// week
		int newWeek = session.getWeek();
		existing.setWeek(newWeek);
		// update the existing record
		OnlineSession updated = onlineSessionRepository.save(existing);
		return updated;		
	}

	// @Override
	// public List<Long> findSessionIdByClazzId(Long clazzId) {
	// 	// TODO Auto-generated method stub
	// 	throw new UnsupportedOperationException("Unimplemented method 'findSessionIdByClazzId'");
	// }

	@Override
	@Transactional(readOnly = true)
	public OnlineSessionDTO getOnlineSession(Long id) {
		OnlineSession session = null;
		OnlineSessionDTO dto = null;
		try{
			session = onlineSessionRepository.findById(id).get();
			dto = new OnlineSessionDTO(session);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> filterOnlineSessionByGrade(String grade) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.filterOnlineSessionByGrade(grade);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> filterOnlineSessionByYear(int year) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.filterOnlineSessionByYear(year);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
	}

    @Override
    @Transactional(readOnly = true)
    public List<OnlineSessionDTO> findSessionByClazzNWeek(Long clazzId, int week) {
        List<OnlineSessionDTO> dtos = new ArrayList<>();
		
		try{
			dtos = onlineSessionRepository.getOnlineSessionByClazzNWeek(clazzId, week);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
    }

	@Override
	@Transactional(readOnly = true)
	public List<OnlineSessionDTO> getOnlineSessionByGradeNSetNYear(String grade, int set, int year) {
		List<OnlineSessionDTO> dtos = new ArrayList<>();
		try{
			dtos = onlineSessionRepository.filterOnlineSessionByGradeNSetNYear(grade, set, year);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public String getOnlineSessionGrade(Long studentId, int year, int week) {
		String grade = "";
		try{
			List<Object[]> rows = enrolmentRepository.findEnrolmentGradesForWeekByStudentId(studentId, year, week);
			if (week == JaeConstants.FIRST_WEEK) {
				int previousYear = year - 1;
				int lastWeek = cycleService.lastAcademicWeek(previousYear);
				rows = enrolmentRepository.findEnrolmentGradesForWeekByStudentId(studentId, previousYear, lastWeek);
			}
			if (rows != null && !rows.isEmpty()) {
				for (Object[] row : rows) {
					String g = (row[0] != null) ? row[0].toString().trim() : "";
					if (StringUtils.isBlank(g)) {
						continue;
					}
					if (!JaeUtils.isSrwCourseGradeCode(g)) {
						return g;
					}
				}
				Object[] first = rows.get(0);
				grade = (first[0] != null) ? first[0].toString().trim() : "";
			}
		}catch(Exception e){
			System.out.println("No OnlineSession found: " + e.getMessage());	
		}
		return grade;
	}

	@Override
	@Transactional(readOnly = true)
	public String getLatestEnrolmentGrade(Long studentId) {
		String grade = "";
		try {
			grade = StringUtils.trimToEmpty(enrolmentRepository.findLatestEnrolmentGradeByStudentId(studentId));
		} catch (Exception e) {
			System.out.println("No OnlineSession found: " + e.getMessage());
		}
		return grade;
	}

}
