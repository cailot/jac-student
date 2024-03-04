package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.repository.HomeworkRepository;
import hyung.jin.seo.jae.service.ConnectedService;

@Service
public class ConnectedServiceImpl implements ConnectedService {
	
	@Autowired
	private HomeworkRepository homeworkRepository;

	
	@Override
	public List<Homework> allHomeworks() {
		List<Homework> dtos = new ArrayList<>();
		try{
			dtos = homeworkRepository.findAll();
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dtos;
	}
	
	@Override
	public Homework getHomework(Long id) {
		Optional<Homework> work = homeworkRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Override
	@Transactional
	public Homework addHomework(Homework work) {
		Homework home = homeworkRepository.save(work);
		return home;
	}

	@Override
	@Transactional
	public Homework updateHomework(Homework newWork, Long id) {
		// search by getId
		Homework existing = homeworkRepository.findById(id).get();
        // Update info
        String newPath = StringUtils.defaultString(newWork.getPath());
        if(StringUtils.isNotBlank(newPath)){
        	existing.setPath(newPath);
        }
        String newInfo = StringUtils.defaultString(newWork.getInfo());
        if(StringUtils.isNotBlank(newInfo)){
        	existing.setInfo(newInfo);
        }
		int newType = newWork.getType();		
		existing.setType(newType);
		long newDuration = newWork.getDuration();
		existing.setDuration(newDuration);
		int newWeek = newWork.getWeek();
		existing.setWeek(newWeek);
		int newYear = newWork.getYear();
		existing.setYear(newYear);
		boolean newActive = newWork.isActive();
		existing.setActive(newActive);
        // LocalDate newRegisterDate = newWork.getRegisterDate();
        // if(newRegisterDate!=null){
        // 	existing.setRegisterDate(newRegisterDate);
        // }
        // update the existing record
        Homework updated = homeworkRepository.save(existing);
        return updated;
	}

	
	@Override
	@Transactional
	public void deleteHomework(Long id) {
		try{
		    homeworkRepository.deleteById(id);
        }catch(org.springframework.dao.EmptyResultDataAccessException e){
            System.out.println("Nothing to delete");
        }
	}

	@Override
	public List<HomeworkDTO> getHomeworkInfo(int subject, int year, int week) {
		List<HomeworkDTO> dtos = new ArrayList<>();
		try{
			dtos = homeworkRepository.findHomework(subject, year, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dtos;
	}
	
	@Override
	public HomeworkDTO getVideoHomeworkInfo(int subject, int year, int week) {
		HomeworkDTO dto = null;
		try{
			dto = homeworkRepository.findVideoHomework(subject, year, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dto;
	}

	@Override
	public HomeworkDTO getPdfHomeworkInfo(int subject, int year, int week) {
		HomeworkDTO dto = null;
		try{
			dto = homeworkRepository.findPdfHomework(subject, year, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dto;
	}

	@Override
	public List<HomeworkDTO> listHomework(int subject, String grade, int year, int week) {
		List<HomeworkDTO> dtos = new ArrayList<>();
		try{
			dtos = homeworkRepository.filterHomeworkBySubjectNGradeNYearNWeek(subject, grade, year, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dtos;
	}
	

}
