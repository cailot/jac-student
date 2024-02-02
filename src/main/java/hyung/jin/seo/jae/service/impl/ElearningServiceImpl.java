package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.model.Elearning;
import hyung.jin.seo.jae.repository.ElearningRepository;
import hyung.jin.seo.jae.service.ElearningService;

@Service
public class ElearningServiceImpl implements ElearningService {
	
	@Autowired
	private ElearningRepository elearningRepository;

	@Override
	public long checkCount() {
		long count = elearningRepository.count();
		return count;
	}

	@Override
	public List<Elearning> allElearnings() {
		List<Elearning> courses = new ArrayList<>();
		try{
			courses = elearningRepository.findAll();
		}catch(Exception e){
			System.out.println("No elearning found");
		}
		// elearningRepository.findAll();
		return courses;
	}
	
	@Override
	public Elearning getElearning(Long id) {
		Optional<Elearning> crs = elearningRepository.findById(id);
		if(!crs.isPresent()) return null;
		return crs.get();
	}

	@Override
	public Elearning addElearning(Elearning crs) {
		Elearning course = elearningRepository.save(crs);
		return course;
	}

	@Override
	public Elearning updateElearning(Elearning newCourse, Long id) {
		// search by getId
		Elearning existing = elearningRepository.findById(id).get();
        // Update info
        String newName = StringUtils.defaultString(newCourse.getName());
        if(StringUtils.isNotBlank(newName)){
        	existing.setName(newName);
        }
        String newGrade = StringUtils.defaultString(newCourse.getGrade());
        if(StringUtils.isNotBlank(newGrade)){
        	existing.setGrade(newGrade);
        }
        LocalDate newRegisterDate = newCourse.getRegisterDate();
        if(newRegisterDate!=null){
        	existing.setRegisterDate(newRegisterDate);
        }
        // update the existing record
        Elearning updated = elearningRepository.save(existing);
        return updated;
	}

	
	@Override
	public void deleteElearning(Long id) {
		try{
		    elearningRepository.deleteById(id);
        }catch(org.springframework.dao.EmptyResultDataAccessException e){
            System.out.println("Nothing to delete");
        }
	}

	@Override
	public List<Elearning> gradeElearnings(String grade) {
		List<Elearning> courses = new ArrayList<>();
		try{
			courses = elearningRepository.findAllByGrade(grade);
		}catch(Exception e){
			System.out.println("No elearning found");
		}
		// elearningRepository.findAllByGrade(grade);
		return courses;
	}

	@Override
	public List<Elearning> studentElearnings(Long id) {
		List<Elearning> courses = new ArrayList<>();
		try{
			courses = elearningRepository.findByStudentId(id);
		}catch(Exception e){
			System.out.println("No elearning found");
		}
		// elearningRepository.findByStudentId(id);
		return courses;
	}


}
