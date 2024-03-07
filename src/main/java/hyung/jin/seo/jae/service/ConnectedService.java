package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.Homework;

public interface ConnectedService {
	// list all Homeworks
	List<Homework> allHomeworks();
	
	// retrieve Homework by Id
	Homework getHomework(Long id);
	
	// register Homework
	Homework addHomework(Homework crs);
    
    // update Homework info by Id
 	Homework updateHomework(Homework newWork, Long id);
	
	// delete Homework
	void deleteHomework(Long id);

	// get Homework by subject, year & week
	HomeworkDTO getHomeworkInfo(int subject, int year, int week);

	// list Homework by subject, grade, year & week
	List<HomeworkDTO> listHomework(int subject, String grade, int year, int week);

	// list all Extraworks
	List<Extrawork> allExtraworks();

	// retrieve Extrawork by Id
	Extrawork getExtrawork(Long id);
	
	// register Extrawork
	Extrawork addExtrawork(Extrawork crs);
	
	// update Extrawork info by Id
	Extrawork updateExtrawork(Extrawork newWork, Long id);
	
	// delete Extrawork
	void deleteExtrawork(Long id);

	// get Extrawork by subject, year & week
	ExtraworkDTO getExtraworkkInfo(int subject, int year, int week);

	// list Extrawork by grade
	List<ExtraworkDTO> listExtrawork(String grade);
	
	// summary of Extrawork by grade
	List<SimpleBasketDTO> loadExtrawork(String grade);
}
