package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.repository.ExtraworkRepository;
import hyung.jin.seo.jae.repository.HomeworkRepository;
import hyung.jin.seo.jae.service.ConnectedService;

@Service
public class ConnectedServiceImpl implements ConnectedService {
	
	@Autowired
	private HomeworkRepository homeworkRepository;

	@Autowired
	private ExtraworkRepository extraworkRepository;

	
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
        String newVideoPath = StringUtils.defaultString(newWork.getVideoPath());
        if(StringUtils.isNotBlank(newVideoPath)){
        	existing.setVideoPath(newVideoPath);
        }
		String newPdfPath = StringUtils.defaultString(newWork.getPdfPath());
        if(StringUtils.isNotBlank(newPdfPath)){
        	existing.setPdfPath(newPdfPath);
        }
        String newInfo = StringUtils.defaultString(newWork.getInfo());
        if(StringUtils.isNotBlank(newInfo)){
        	existing.setInfo(newInfo);
        }
		int newWeek = newWork.getWeek();
		existing.setWeek(newWeek);
		int newYear = newWork.getYear();
		existing.setYear(newYear);
		boolean newActive = newWork.isActive();
		existing.setActive(newActive);
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
	public HomeworkDTO getHomeworkInfo(int subject, int year, int week) {
		HomeworkDTO dto = null;
		try{
			dto = homeworkRepository.findHomework(subject, year, week);
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

	@Override
	public List<Extrawork> allExtraworks() {
		List<Extrawork> dtos = new ArrayList<>();
		try{
			dtos = extraworkRepository.findAll();
		}catch(Exception e){
			System.out.println("No Extrawork found");
		}
		return dtos;
	}

	@Override
	public Extrawork getExtrawork(Long id) {
		Optional<Extrawork> work = extraworkRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Override
	@Transactional
	public Extrawork addExtrawork(Extrawork work) {
		Extrawork extra = extraworkRepository.save(work);
		return extra;
	}

	@Override
	@Transactional
	public Extrawork updateExtrawork(Extrawork newWork, Long id) {
		// search by getId
		Extrawork existing = extraworkRepository.findById(id).get();
        // Update info
        String newVideoPath = StringUtils.defaultString(newWork.getVideoPath());
        if(StringUtils.isNotBlank(newVideoPath)){
        	existing.setVideoPath(newVideoPath);
        }
		String newPdfPath = StringUtils.defaultString(newWork.getPdfPath());
        if(StringUtils.isNotBlank(newPdfPath)){
        	existing.setPdfPath(newPdfPath);
        }
        String newName = StringUtils.defaultString(newWork.getName());
        if(StringUtils.isNotBlank(newName)){
        	existing.setName(newName);
        }
		boolean newActive = newWork.isActive();
		existing.setActive(newActive);
        // update the existing record
        Extrawork updated = extraworkRepository.save(existing);
        return updated;
	}

	@Override
	@Transactional
	public void deleteExtrawork(Long id) {
		try{
		    extraworkRepository.deleteById(id);
        }catch(org.springframework.dao.EmptyResultDataAccessException e){
            System.out.println("Nothing to delete");
        }
	}

	@Override
	public ExtraworkDTO getExtraworkkInfo(int subject, int year, int week) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getExtraworkkInfo'");
	}

	@Override
	public List<ExtraworkDTO> listExtrawork(String grade) {
		List<ExtraworkDTO> dtos = new ArrayList<>();
		try{
			dtos = extraworkRepository.filterExtraworkByGrade(grade);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dtos;
	}

	@Override
	public List<SimpleBasketDTO> loadExtrawork(String grade) {
		List<Object[]> objects = new ArrayList<>();
		try{
			objects = extraworkRepository.summaryExtrawork(grade);
		}catch(Exception e){
			System.out.println("No Extrawork found");
		}
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		for(Object[] object : objects){
			SimpleBasketDTO dto = new SimpleBasketDTO(object);
			dtos.add(dto);
		}
		return dtos;
	}
	

}
