package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.AssessmentAnswerDTO;
import hyung.jin.seo.jae.dto.AssessmentDTO;
import hyung.jin.seo.jae.model.Assessment;
import hyung.jin.seo.jae.model.AssessmentAnswer;
import hyung.jin.seo.jae.model.GuestStudent;

public interface AssessmentService {

	// list all Assessment
	List<Assessment> allAssessments();

	// retrieve Assessment by Id
	Assessment getAssessment(Long id);
	
	// register Assessment
	Assessment addAssessment(Assessment crs);
	
	// update Assessment info by Id
	Assessment updateAssessment(Assessment newWork, Long id);
	
	// delete Assessment
	void deleteAssessment(Long id);

	// get Assessment by grade & subject
	AssessmentDTO getAssessmentInfo(String grade, long subjectId);

	// list Assessment by grade
	List<AssessmentDTO> listAssessment(String grade);

	// list Assessment by subject
	List<AssessmentDTO> listAssessment(long subjectId);

	// list Assessment by subject
	List<AssessmentDTO> listAssessment(String grade, long subjectId);

	// get AssessmentAnswer by assessment
	AssessmentAnswerDTO getAssessmentAnswer(Long assessmentId);

	// get AssessmentAnswer by assessment
	AssessmentAnswer findAssessmentAnswer(Long assessmentId);


	// register AssessmentAnswer
	AssessmentAnswer addAssessmentAnswer(AssessmentAnswer answer);

	// update AssessmentAnswer info by Id
	AssessmentAnswer updateAssessmentAnswer(AssessmentAnswer newWork, Long id);

	// get Answer sheet by Assessment
	// List<AssessmentAnswerItem> getAnswersByAssessment(Long assessId);

	// get Student's answer by Student & Assessment
	List<Integer> getStudentAssessmentAnswer(Long studentId, Long  assessId);
	
	// retrieve StudentTest by Id
	// GuestStudentAssessment getStudentAssessment(Long id);

	// retrieve AssessmentAnswer by Assessment
	// StudentTestDTO findStudentTestByStudentNTest(Long studentId, Long testId);

	// register TestAnswer
	// StudentTest addStudentTest(StudentTest crs);
	
	// update TestAnswer info by Id
	// StudentTest updateStudentTest(StudentTest newWork, Long id);

	// check if student has done the test
	// boolean isStudentTestExist(Long studentId, Long testId);

	// delete existing record to take test again
	// void deleteStudentTest(Long studentId, Long testId); 	

	// register GuestStudent
	GuestStudent addGuestStudent(GuestStudent gs);


}
