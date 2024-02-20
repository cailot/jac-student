package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.OnlineSession;

public interface OnlineSessionService {
	// list all online
	List<OnlineSessionDTO> allOnlineSessions();

	// list session by clazz Id
	List<OnlineSessionDTO> findOnlineSessionByClazz(Long clazzId);

	// list active session by clazz Id
	List<OnlineSessionDTO> findActiveOnlineSessionByClazz(Long clazzId);

	// list inactive session by clazz Id
	List<OnlineSessionDTO> findInactiveOnlineSessionByClazz(Long clazzId);

	// filter session by grade
	List<OnlineSessionDTO> filterOnlineSessionByGrade(String grade);

	// filter session by year
	List<OnlineSessionDTO> filterOnlineSessionByYear(int year);

	// filter session by grade and year
	List<OnlineSessionDTO> filterOnlineSessionByGradeNYear(String grade, int year);

	// return total count
	long checkCount();

	// add session
	OnlineSession addOnlineSession(OnlineSession session);

	// update session
	OnlineSession updateOnlineSession(OnlineSession session, Long id);

	// find session by clazz Id & week
	OnlineSessionDTO findSessionByClazzNWeek(Long clazzId, int week);

	// find session by id
	OnlineSessionDTO getOnlineSession(Long id);

}