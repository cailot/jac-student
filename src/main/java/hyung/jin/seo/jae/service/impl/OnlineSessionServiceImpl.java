package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.OnlineSession;
import hyung.jin.seo.jae.repository.OnlineSessionRepository;
import hyung.jin.seo.jae.service.OnlineSessionService;

@Service
public class OnlineSessionServiceImpl implements OnlineSessionService {
	

	@Autowired
	private OnlineSessionRepository onlineSessionRepository;

	@Override
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
	public long checkCount() {
		long count = onlineSessionRepository.count();
		return count;
	}

	@Override
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
    public OnlineSessionDTO findSessionByClazzNWeek(Long clazzId, int week) {
        OnlineSessionDTO dto = null;
		try{
			dto = onlineSessionRepository.getOnlineSessionByClazzNWeek(clazzId, week);
		}catch(Exception e){
			System.out.println("No OnlineSession found");
		}
		return dto;
    }

}
