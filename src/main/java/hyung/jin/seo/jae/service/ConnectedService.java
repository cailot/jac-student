package hyung.jin.seo.jae.service;

import java.time.LocalDateTime;
import java.util.List;

import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.dto.HomeworkScheduleDTO;
import hyung.jin.seo.jae.dto.PracticeAnswerDTO;
import hyung.jin.seo.jae.dto.PracticeDTO;
import hyung.jin.seo.jae.dto.PracticeScheduleDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.dto.StudentPracticeDTO;
import hyung.jin.seo.jae.dto.StudentTestDTO;
import hyung.jin.seo.jae.dto.TestAnswerDTO;
import hyung.jin.seo.jae.dto.TestDTO;
import hyung.jin.seo.jae.dto.TestScheduleDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.ExtraworkProgress;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.model.HomeworkProgress;
import hyung.jin.seo.jae.model.HomeworkSchedule;
import hyung.jin.seo.jae.model.Practice;
import hyung.jin.seo.jae.model.PracticeAnswer;
import hyung.jin.seo.jae.model.PracticeSchedule;
import hyung.jin.seo.jae.model.StudentPractice;
import hyung.jin.seo.jae.model.StudentTest;
import hyung.jin.seo.jae.model.Test;
import hyung.jin.seo.jae.model.TestAnswer;
import hyung.jin.seo.jae.model.TestAnswerItem;

public interface ConnectedService {

	/////////////////////////////////////////////////////////
	//
	//	HOMEWORK
	//
	/////////////////////////////////////////////////////////
	// list all Homeworks
	List<Homework> allHomeworks();
	
	// retrieve Homework by Id
	Homework getHomework(Long id);
	
	// // register Homework
	// Homework addHomework(Homework crs);
    
    // // update Homework info by Id
 	// Homework updateHomework(Homework newWork, Long id);
	
	// // delete Homework
	// void deleteHomework(Long id);

	// get Homework by subject & week
	HomeworkDTO getHomeworkInfo(long subject, int week);

	// list Homework by subject, grade & week
	List<HomeworkDTO> listHomework(long subject, String grade, int week);

	// get Id by subject, grade & week
	long getHomeworkIdByWeek(long subject, String grade, int week);

	/////////////////////////////////////////////////////////
	//
	//	HOMEWORK SCHEDULE
	//
	/////////////////////////////////////////////////////////

	// list all Homework Schedules
	List<HomeworkSchedule> allHomeworkSchedules();

	// list Homework Schedule
	List<HomeworkScheduleDTO> listHomeworkSchedule(LocalDateTime from, LocalDateTime to);

	// retrieve Homework Schedule by Id
	HomeworkSchedule getHomeworkSchedule(Long id);
	
	// // register Homework Schedule
	// HomeworkSchedule addHomeworkSchedule(HomeworkSchedule schedule);
	
	// // update Homework Schedule info by Id
	// HomeworkSchedule updateHomeworkSchedule(HomeworkSchedule schedule, Long id);
	
	// // delete Homeowork Schedule
	// void deleteHomeworkSchedule(Long id);

	// get Homework Schedule by subject & grade & localDateTime
	HomeworkScheduleDTO getHomeworkScheduleBySubjectAndGrade(String subject, String grade, LocalDateTime now);

	/////////////////////////////////////////////////////////
	//
	//	HOMEWORK PROGRESS
	//
	/////////////////////////////////////////////////////////

	// retrieve Homework Progress by Id
	HomeworkProgress getHomeworkProgress(Long id);

	// retrieve Homework Progress by student & homework
	HomeworkProgress getHomeworkProgressByStudentNHomework(Long studentId, Long homeworkId);

	// get Percentage Homework Progress by student & homework
	int getHomeworkProgressPercentage(Long studentId, Long homeworkId);

	// register Homework Progress
	HomeworkProgress addHomeworkProgress(HomeworkProgress progress);
	
	// update Homework Progress info by Id
	HomeworkProgress updateHomeworkProgressPercentage(Long id, int percentage);

	/////////////////////////////////////////////////////////
	//
	//	EXTRAWORK
	//
	/////////////////////////////////////////////////////////
	// list all Extraworks
	List<Extrawork> allExtraworks();

	// retrieve Extrawork by Id
	Extrawork getExtrawork(Long id);
	
	// // register Extrawork
	// Extrawork addExtrawork(Extrawork crs);
	
	// // update Extrawork info by Id
	// Extrawork updateExtrawork(Extrawork newWork, Long id);
	
	// // delete Extrawork
	// void deleteExtrawork(Long id);

	// get Extrawork by subject, year & week
	ExtraworkDTO getExtraworkInfo(int subject, int year, int week);

	// list Extrawork by grade
	List<ExtraworkDTO> listExtrawork(String grade);
	
	// summary of Extrawork by grade
	List<SimpleBasketDTO> loadExtrawork(String grade);


	/////////////////////////////////////////////////////////
	//
	//	HOMEWORK PROGRESS
	//
	/////////////////////////////////////////////////////////

	// retrieve Extrawork Progress by Id
	ExtraworkProgress getExtraworkProgress(Long id);

	// retrieve Extrawork Progress by student & homework
	ExtraworkProgress getExtraworkProgressByStudentNHomework(Long studentId, Long extraworkId);

	// get Percentage Extrawork Progress by student & homework
	int getExtraworkProgressPercentage(Long studentId, Long extraworkId);

	// register Extrawork Progress
	ExtraworkProgress addExtraworkProgress(ExtraworkProgress progress);
	
	// update Extrawork Progress info by Id
	ExtraworkProgress updateExtraworkProgressPercentage(Long id, int percentage);

	/////////////////////////////////////////////////////////
	//
	//	PRACTICE
	//
	/////////////////////////////////////////////////////////

	// list all Practices
	List<Practice> allPractices();

	// retrieve Practice by Id
	Practice getPractice(Long id);

	// retrieve Practice by Id
	PracticeDTO getPracticeInfo(Long id);

	// get Practice by practice group, grade & week
	List<PracticeDTO> getPracticeInfoByGroup(int practiceGroup, String grade, int week);
	
	// // register Practice
	// Practice addPractice(Practice crs);
	
	// // update Practice info by Id
	// Practice updatePractice(Practice newWork, Long id);
	
	// // delete Practice
	// void deletePractice(Long id);

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

	// // register PracticeAnswer
	// PracticeAnswer addPracticeAnswer(PracticeAnswer crs);
	
	// // update PracticeAnswer info by Id
	// PracticeAnswer updatePracticeAnswer(PracticeAnswer newWork, Long id);

	// get Answer sheet by Practice
	List<Integer> getAnswersByPractice(Long practiceId);

	// get Student's answer by Student & Practice
	List<Integer> getStudentPracticeAnswer(Long studentId, Long  practionId);

	// get how many question answer sheet has
	int getPracticeAnswerCount(Long practiceId);

	// get how many answers per question
	int getPracticeAnswerCountPerQuestion(Long practiceId);

	// retrieve StudentPractice by Id
	StudentPractice getStudentPractice(Long id);

	// retrieve PracticeAnswer by Practice
	StudentPracticeDTO findStudentPracticeByStudentNPractice(Long studentId, Long practiceId);

	// // register PracticeAnswer
	StudentPractice addStudentPractice(StudentPractice crs);
	
	// // update PracticeAnswer info by Id
	// StudentPractice updateStudentPractice(StudentPractice newWork, Long id);

	// check if student has done the practice
	boolean isStudentPracticeExist(Long studentId, Long practiceId);

	// delete existing record to take test again
	void deleteStudentPractice(Long studentId, Long practiceId); 

	/////////////////////////////////////////////////////////
	//
	//	TEST
	//
	/////////////////////////////////////////////////////////

	// list all Test
	List<Test> allTests();

	// retrieve Test by Id
	Test getTest(Long id);

	// get Test by Id
	TestDTO getTestInfo(Long id);
	
	// get Test by type, grade & volume
	TestDTO getTestInfo(int type, String grade, int volume);

	// list Test by type, grade & volume
	List<TestDTO> listTest(int type, String grade, int volume);

	// summary of Test by practiceType & grade
	List<SimpleBasketDTO> loadTest(int type, int grade);

	// retrieve TestAnswer by Id
	TestAnswer getTestAnswer(Long id);

	// retrieve TestAnswer by Test
	TestAnswerDTO findTestAnswerByTest(Long id);

	// get Answer sheet by Test
	List<TestAnswerItem> getAnswersByTest(Long testId);

	// get Student's answer by Student & Test
	List<Integer> getStudentTestAnswer(Long studentId, Long  testId, String from, String to);	

	// get how many question answer sheet has
	int getTestAnswerCount(Long testId);
	
	// retrieve StudentTest by Id
	StudentTest getStudentTest(Long id);

	// retrieve TestAnswer by Test
	StudentTestDTO findStudentTestByStudentNTest(Long studentId, Long testId, String from, String to);

	// register TestAnswer
	StudentTest addStudentTest(StudentTest crs);
	
	// check if student has done the test
	boolean isStudentTestExist(Long studentId, Long testId, String from, String to);

	// retrieve StudentTest brief info
	StudentTestDTO getStudentTest(Long studentId, Long testTypeId, String grade, int volume, String from, String to);

	// get latest StudentTest by studentId
	List<StudentTestDTO> getLatestStudentTest(Long studentId);

	// get Test by test group, grade & week
	List<TestDTO> getTestInfoByGroup(int testGroup, String grade, int week);

	// get how many answers per question
	int getTestAnswerCountPerQuestion(Long testId);

	// get registerDate by studentId and testId
	String getRegDateforStudentTest(Long studentId, Long test, String from, String to);
	
	double getAverageScoreByTest(Long testId, String from, String to);

	double getHighestScoreByTest(Long testId, String from, String to);

	double getLowestScoreByTest(Long testId, String from, String to);

	String getScoreCategory(double studentScore, Long testId, String from, String to);

	// get Test by testType
	List<TestDTO> getTestInfoByType(Long testId, String from, String to);

	// get test type name by id
	String getTestTypeName(Long id);

	// get test group by id
	int getTestGroup(Long id);

	// get test grade by id
	String getTestGrade(Long id);

	// get test volume by id
	int getTestVolume(Long id);

	/////////////////////////////////////////////////////////
	//
	//	PRACTICE SCHEDULE
	//
	/////////////////////////////////////////////////////////

	// list all Practice Schedules
	List<PracticeSchedule> allPracticeSchedules();

	// list Practice Schedule by year & week
	// List<PracticeScheduleDTO> listPracticeSchedule(int year, int week);

	// retrieve Practice Schedule by Id
	PracticeSchedule getPracticeSchedule(Long id);
	
	// // register Practice Schedule
	// PracticeSchedule addPracticeSchedule(PracticeSchedule ps);
	
	// // update Practice Schedule info by Id
	// PracticeSchedule updatePracticeSchedule(PracticeSchedule newWork, Long id);
	
	// // delete Practice Schedule
	// void deletePracticeSchedule(Long id);

	// get Practice Schedule by practiceGroup & grade & localDateTime
	List<PracticeScheduleDTO> checkPracticeSchedule(String practiceGroup, String grade, LocalDateTime now);

	/////////////////////////////////////////////////////////
	//
	//	TEST SCHEDULE
	//
	/////////////////////////////////////////////////////////

	// get Test Schedule by testGroup & grade & localDateTime
	List<TestScheduleDTO> checkTestSchedule(String testGroup, String grade, LocalDateTime now);

	// get Test Schedule for explanation by testGroup & grade & localDateTime
	List<TestScheduleDTO> checkTestSchedule4Explanation(String testGroup, String grade, LocalDateTime now);

	// get most recent Test Schedule by testGroup & grade & week
	TestScheduleDTO getMostRecentTestSchedule(String testGroup, String grade, String week);
	
}
