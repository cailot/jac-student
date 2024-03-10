package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.dto.PracticeAnswerDTO;
import hyung.jin.seo.jae.dto.PracticeDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.model.Practice;
import hyung.jin.seo.jae.model.PracticeAnswer;

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
	ExtraworkDTO getExtraworkInfo(int subject, int year, int week);

	// list Extrawork by grade
	List<ExtraworkDTO> listExtrawork(String grade);
	
	// summary of Extrawork by grade
	List<SimpleBasketDTO> loadExtrawork(String grade);

	// list all Practices
	List<Practice> allPractices();

	// retrieve Practice by Id
	Practice getPractice(Long id);
	
	// register Practice
	Practice addPractice(Practice crs);
	
	// update Practice info by Id
	Practice updatePractice(Practice newWork, Long id);
	
	// delete Practice
	void deletePractice(Long id);

	// get Practice by type, grade & volume
	PracticeDTO getPracticeInfo(int type, String grade, int volume);

	// list Practice by type, grade & volume
	List<PracticeDTO> listPractice(int type, String grade, int volume);

	// summary of Practice by practiceType & grade
	List<SimpleBasketDTO> loadPractice(int type, int grade);

	// retrieve PracticeAnswer by Id
	PracticeAnswer getPracticeAnswer(Long id);

	// retrieve PracticeAnswer by Practice
	PracticeAnswerDTO findPracticeAnswerByPractice(Long id);

	// register PracticeAnswer
	PracticeAnswer addPracticeAnswer(PracticeAnswer crs);
	
	// update PracticeAnswer info by Id
	PracticeAnswer updatePracticeAnswer(PracticeAnswer newWork, Long id);
	

	
}
