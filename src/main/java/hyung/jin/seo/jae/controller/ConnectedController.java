package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.dto.PracticeAnswerDTO;
import hyung.jin.seo.jae.dto.PracticeDTO;
import hyung.jin.seo.jae.dto.PracticeScheduleDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.dto.StudentTestDTO;
import hyung.jin.seo.jae.dto.TestAnswerDTO;
import hyung.jin.seo.jae.dto.TestDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.Grade;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.model.Practice;
import hyung.jin.seo.jae.model.PracticeType;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.model.StudentPractice;
import hyung.jin.seo.jae.model.StudentTest;
import hyung.jin.seo.jae.model.Subject;
import hyung.jin.seo.jae.model.Test;
import hyung.jin.seo.jae.model.TestAnswerItem;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.ConnectedService;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Controller
@RequestMapping("connected")
public class ConnectedController {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectedController.class);

	@Autowired
	private ConnectedService connectedService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private StudentService studentService;
	
	// register homework
	@PostMapping("/addHomework")
	@ResponseBody
	public HomeworkDTO registerHomework(@RequestBody HomeworkDTO formData) {
		// 1. create barebone
		Homework work = formData.convertToHomework();
		// 2. set active to true as default
		work.setActive(true);
		// 3. set Subject
		Subject subject = codeService.getSubject(Long.parseLong(formData.getSubject()));
		// 4. set Grade
		Grade grade = codeService.getGrade(Long.parseLong(formData.getGrade()));
		// 5. associate Subject & Grade
		work.setSubject(subject);
		work.setGrade(grade);
		// 6. register Homework
		Homework added = connectedService.addHomework(work);
		// 7. return dto
		HomeworkDTO dto = new HomeworkDTO(added);
		return dto;
	}

	// register extrawork
	@PostMapping("/addExtrawork")
	@ResponseBody
	public ExtraworkDTO registerExtrawork(@RequestBody ExtraworkDTO formData) {
		// 1. create barebone
		Extrawork work = formData.convertToExtrawork();
		// 2. set active to true as default
		work.setActive(true);
		// 3. set Grade
		Grade grade = codeService.getGrade(Long.parseLong(formData.getGrade()));
		// 4. associate Grade
		work.setGrade(grade);
		// 5. register Extrawork
		Extrawork added = connectedService.addExtrawork(work);
		// 6. return dto
		ExtraworkDTO dto = new ExtraworkDTO(added);
		return dto;
	}

	// register practice
	@PostMapping("/addPractice")
	@ResponseBody
	public PracticeDTO registerPractice(@RequestBody PracticeDTO formData) {
		// 1. create barebone
		Practice work = formData.convertToPractice();
		// 2. set active to true as default
		work.setActive(true);
		// 3. set Grade & PracticeType
		Grade grade = codeService.getGrade(Long.parseLong(formData.getGrade()));
		PracticeType type = codeService.getPracticeType(formData.getPracticeType());
		// 4. associate Grade & PracticeType
		work.setGrade(grade);
		work.setPracticeType(type);
		// 5. register Practice
		Practice added = connectedService.addPractice(work);
		// 6. return dto
		PracticeDTO dto = new PracticeDTO(added);
		return dto;
	}

	@PostMapping(value = "/addStudentPractice")
	@ResponseBody
    public ResponseEntity<String> registerStudentPractice(@RequestBody Map<String, Object> payload) {
        // Extract practiceId and answers from the payload
		String studentId = StringUtils.defaultString(payload.get("studentId").toString(), "0");
		String practiceId = StringUtils.defaultString(payload.get("practiceId").toString(), "0");
		List<Map<String, Object>> mapAns = (List<Map<String, Object>>) payload.get("answers");
		// convert the Map of answers to List
		List<Integer> answers = convertPracticeAnswers(mapAns);
		// compare answers with answer sheet
		List<Integer> corrects = connectedService.getAnswersByPractice(Long.parseLong(practiceId));
		double score = JaeUtils.calculatePracticeScore(answers, corrects);
		// 1. create barebone
		StudentPractice sp = new StudentPractice();
		sp.setScore(score);
		// 2. set Student & Practice
		Student student = studentService.getStudent(Long.parseLong(studentId));
		Practice practice = connectedService.getPractice(Long.parseLong(practiceId));
		// 3. associate Student & Practice
		sp.setStudent(student);
		sp.setPractice(practice);
		// 4. set answers
		sp.setAnswers(answers);
		// 5. register StudentPractice
		connectedService.addStudentPractice(sp);
		// 6. return flag
		return ResponseEntity.ok("\"StudentPractice registered\"");
    }

	@PostMapping(value = "/addStudentTest")
	@ResponseBody
    public ResponseEntity<String> registerStudentTest(@RequestBody Map<String, Object> payload) {
        // Extract practiceId and answers from the payload
		String studentId = StringUtils.defaultString(payload.get("studentId").toString(), "0");
		String testId = StringUtils.defaultString(payload.get("testId").toString(), "0");
		List<Map<String, Object>> mapAns = (List<Map<String, Object>>) payload.get("answers");
		// convert the Map of answers to List
		List<Integer> answers = convertTestAnswers(mapAns);
		// compare answers with answer sheet
		List<TestAnswerItem> corrects = connectedService.getAnswersByTest(Long.parseLong(testId));
		double score = JaeUtils.calculateTestScore(answers, corrects);
		// 1. create barebone
		StudentTest st = new StudentTest();
		st.setScore(score);
		// 2. set Student & Test
		Student student = studentService.getStudent(Long.parseLong(studentId));
		Test test = connectedService.getTest(Long.parseLong(testId));
		// 3. associate Student & Test
		st.setStudent(student);
		st.setTest(test);
		// 4. set answers
		st.setAnswers(answers);
		// 5. register StudentTest
		connectedService.addStudentTest(st);
		// 6. return flag
		return ResponseEntity.ok("\"StudentTest registered\"");
    }

	// delete StudentPractice
	@DeleteMapping("/deleteStudentPractice/{studentId}/{practiceId}")
	@ResponseBody
	public ResponseEntity<String> deleteStudentAnswer(@PathVariable Long studentId, @PathVariable Long practiceId){
		try{
			connectedService.deleteStudentPractice(studentId, practiceId);
			return ResponseEntity.ok("\"StudentPractice deleted\"");
		}catch(Exception e){
			String message = "Error deleting StudentPractice : " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// update existing homework
	@PutMapping("/updateHomework")
	@ResponseBody
	public ResponseEntity<String> updateHomework(@RequestBody HomeworkDTO formData) {
		try{
			// 1. create barebone Homework
			Homework work = formData.convertToHomework();
			// 2. update Homework
			work = connectedService.updateHomework(work, Long.parseLong(formData.getId()));
			// 3.return flag
			return ResponseEntity.ok("\"Homework updated\"");
		}catch(Exception e){
			String message = "Error updating Homework : " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// update existing extrawork
	@PutMapping("/updateExtrawork")
	@ResponseBody
	public ResponseEntity<String> updateExtrawork(@RequestBody ExtraworkDTO formData) {
		try{
			// 1. create barebone Homework
			Extrawork work = formData.convertToExtrawork();
			// 2. update Homework
			work = connectedService.updateExtrawork(work, Long.parseLong(formData.getId()));
			// 3.return flag
			return ResponseEntity.ok("\"Extrawork updated\"");
		}catch(Exception e){
			String message = "Error updating Extrawork : " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// update existing practice
	@PutMapping("/updatePractice")
	@ResponseBody
	public ResponseEntity<String> updatePractice(@RequestBody PracticeDTO formData) {
		try{
			// 1. create barebone Homework
			Practice work = formData.convertToPractice();
			// 2. update Homework
			work = connectedService.updatePractice(work, Long.parseLong(formData.getId()));
			// 3.return flag
			return ResponseEntity.ok("\"Practice updated\"");
		}catch(Exception e){
			String message = "Error updating Practice : " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}
	
	// get homework
	@GetMapping("/getHomework/{id}")
	@ResponseBody
	public HomeworkDTO getHomework(@PathVariable Long id) {
		Homework work = connectedService.getHomework(id);
		HomeworkDTO dto = new HomeworkDTO(work);
		return dto;
	}

	// get extrawork
	@GetMapping("/getExtrawork/{id}")
	@ResponseBody
	public ExtraworkDTO getExtrawork(@PathVariable Long id) {
		Extrawork work = connectedService.getExtrawork(id);
		ExtraworkDTO dto = new ExtraworkDTO(work);
		return dto;
	}

	// get practice
	@GetMapping("/getPractice/{id}")
	@ResponseBody
	public PracticeDTO getPractice(@PathVariable Long id) {
		Practice work = connectedService.getPractice(id);
		PracticeDTO dto = new PracticeDTO(work);
		// get question count
		int count = connectedService.getPracticeAnswerCount(id);
		dto.setQuestionCount(count);
		return dto;
	}

	// get test
	@GetMapping("/getTest/{id}")
	@ResponseBody
	public TestDTO getTest(@PathVariable Long id) {
		Test work = connectedService.getTest(id);
		TestDTO dto = new TestDTO(work);
		// get question count
		int count = connectedService.getTestAnswerCount(id);
		dto.setQuestionCount(count);
		return dto;
	}

	// search homework by subject, year & week
	@GetMapping("/homework/{subject}/{year}/{week}")
	@ResponseBody
	public HomeworkDTO searchHomework(@PathVariable int subject, @PathVariable int year, @PathVariable int week) {
		HomeworkDTO dto = connectedService.getHomeworkInfo(subject, year, week);
		return dto;
	}

	// // search practice by id
	@GetMapping("/practiceAnswer/{studentId}/{practiceId}")
	@ResponseBody
	public PracticeAnswerDTO searchPracticeAnswer(@PathVariable String studentId, @PathVariable String practiceId) {
		String filteredStudentId = StringUtils.defaultString(studentId, "0");
		String filteredPracticeId = StringUtils.defaultString(practiceId, "0");
		PracticeAnswerDTO dto = connectedService.findPracticeAnswerByPractice(Long.parseLong(filteredPracticeId));
		// get student's answer....
		List<Integer> answers = connectedService.getStudentPracticeAnswer(Long.parseLong(filteredStudentId), Long.parseLong(filteredPracticeId));
		dto.setStudents(answers);
		return dto;
	}
	
	@GetMapping("/testAnswer/{studentId}/{testId}")
	@ResponseBody
	public TestAnswerDTO searchTestAnswer(@PathVariable String studentId, @PathVariable String testId) {
		String filteredStudentId = StringUtils.defaultString(studentId, "0");
		String filteredTestId = StringUtils.defaultString(testId, "0");
		TestAnswerDTO dto = connectedService.findTestAnswerByTest(Long.parseLong(filteredTestId));
		// get student's answer....
		List<Integer> answers = connectedService.getStudentTestAnswer(Long.parseLong(filteredStudentId), Long.parseLong(filteredTestId));
		dto.setStudents(answers);
		return dto;
	}

	// bring homework in database
	@GetMapping("/filterHomework")
	public String listHomeworks(
			@RequestParam(value = "listSubject", required = false) String subject,
			@RequestParam(value = "listGrade", required = false) String grade,
			@RequestParam(value = "listYear", required = false) String year,
			@RequestParam(value = "listWeek", required = false) String week, 
			Model model) {
		List<HomeworkDTO> dtos = new ArrayList();
		String filteredSubject = StringUtils.defaultString(subject, "0");
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		String filteredYear = StringUtils.defaultString(year, "0");
		String filteredWeek = StringUtils.defaultString(week, "0");
		dtos = connectedService.listHomework(Integer.parseInt(filteredSubject), filteredGrade, Integer.parseInt(filteredYear), Integer.parseInt(filteredWeek));		
		model.addAttribute(JaeConstants.HOMEWORK_LIST, dtos);
		return "homeworkListPage";
	}

	// bring extrawork in database
	@GetMapping("/filterExtrawork")
	public String listExtraworks(
			@RequestParam(value = "listGrade", required = false) String grade,
			Model model) {
		List<ExtraworkDTO> dtos = new ArrayList();
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		dtos = connectedService.listExtrawork(filteredGrade);		
		model.addAttribute(JaeConstants.EXTRAWORK_LIST, dtos);
		return "extraworkListPage";
	}

	@GetMapping("/filterPractice")
	public String listPractices(
			@RequestParam(value = "listPracticeType", required = false) String practiceType,
			@RequestParam(value = "listGrade", required = false) String grade,
			@RequestParam(value = "listVolume", required = false) String volume,
			Model model) {
		List<PracticeDTO> dtos = new ArrayList();
		String filteredType = StringUtils.defaultString(practiceType, "0");
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		String filteredVolume = StringUtils.defaultString(volume, "0");
		dtos = connectedService.listPractice(Integer.parseInt(filteredType), filteredGrade, Integer.parseInt(filteredVolume));		
		model.addAttribute(JaeConstants.PRACTICE_LIST, dtos);
		return "practiceListPage";
	}

	// bring summary of extrawork
	@GetMapping("/summaryExtrawork/{grade}")
	@ResponseBody
	public List<SimpleBasketDTO> summaryExtraworks(@PathVariable String grade) {
		List<SimpleBasketDTO> dtos = new ArrayList();
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		dtos = connectedService.loadExtrawork(filteredGrade);	
		return dtos;
	}

	@GetMapping("/summaryPractice/{studentId}/{practiceType}/{grade}")
	@ResponseBody
	public List<SimpleBasketDTO> summaryPractices(@PathVariable String studentId, @PathVariable String practiceType, @PathVariable String grade) {
		List<SimpleBasketDTO> dtos = new ArrayList();
		String filteredStudentId = StringUtils.defaultString(studentId, "0");
		String filteredPracticeType = StringUtils.defaultString(practiceType, "0");
		String filteredGrade = StringUtils.defaultString(grade, "0");
		dtos = connectedService.loadPractice(Integer.parseInt(filteredPracticeType), Integer.parseInt(filteredGrade));
		// check whether the volume is finished or not
		for(SimpleBasketDTO dto : dtos){
			// get practiceId
			String practiceId = StringUtils.defaultString(dto.getValue(), "0");
			boolean done = connectedService.isStudentPracticeExist(Long.parseLong(filteredStudentId), Long.parseLong(practiceId));
			if(done){
				String name = dto.getName();
				dto.setName(name + JaeConstants.PRACTICE_COMPLETE);
			}
		}
		return dtos;
	}

	@GetMapping("/summaryTest/{studentId}/{testType}/{grade}")
	@ResponseBody
	public List<SimpleBasketDTO> summaryTests(@PathVariable String studentId, @PathVariable String testType, @PathVariable String grade) {
		List<SimpleBasketDTO> dtos = new ArrayList();
		String filteredStudentId = StringUtils.defaultString(studentId, "0");
		String filteredTestType = StringUtils.defaultString(testType, "0");
		String filteredGrade = StringUtils.defaultString(grade, "0");
		dtos = connectedService.loadTest(Integer.parseInt(filteredTestType), Integer.parseInt(filteredGrade));
		// check whether the volume is finished or not
		for(SimpleBasketDTO dto : dtos){
			// get testId
			String testId = StringUtils.defaultString(dto.getValue(), "0");
			boolean done = connectedService.isStudentTestExist(Long.parseLong(filteredStudentId), Long.parseLong(testId));
			if(done){
				String name = dto.getName();
				dto.setName(name + JaeConstants.PRACTICE_COMPLETE);
			}
		}
		return dtos;
	}

	@GetMapping("/summaryTestResult/{studentId}/{testType}/{grade}/{volume}")
	@ResponseBody
	public List<StudentTestDTO> summaryTestResults(@PathVariable String studentId, @PathVariable String testType, @PathVariable String grade, @PathVariable String volume) {
//////////////////////////////////////////////////////////////////////////////////////////////
		List<StudentTestDTO> dtos = new ArrayList<>();
		String filteredStudentId = StringUtils.defaultString(studentId, "0");
		String filteredGrade = StringUtils.defaultString(grade, "0");
		String filteredVolume = StringUtils.defaultString(volume, "0");
		String[] types = StringUtils.split(StringUtils.defaultString(testType),",");
		// Loop through each test type in the array
		for (String type : types) {
			// can I simply use SimpleBasketDTO for testId & testTypeName ??...
			StudentTestDTO dto = connectedService.getStudentTest(Long.parseLong(filteredStudentId), Long.parseLong(type), filteredGrade, Integer.parseInt(filteredVolume));
			if(dto!=null) dtos.add(dto);
		}		
		return dtos;
	}

	@GetMapping("/studentTestResult/{studentTestId}")
	@ResponseBody
	public String getReportAddress(@PathVariable String studentTestId) {
//////////////////////////////////////////////////////////////////////////////////////////////
		String address = "http://assessment.jamesancollegevic.com/result/65fbc8a50eab1f75597adb65.pdf";
		String filteredStudentTestId = StringUtils.defaultString(studentTestId, "0");
		return address;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	@PostMapping(value = "/submitAnswers")
	@ResponseBody
    public ResponseEntity<String> submitAnswers(@RequestBody Map<String, Object> payload) {
         // Extract practiceId and answers from the payload
		 String studentId = payload.get("studentId").toString();
		 String practiceId = payload.get("practiceId").toString();
		 List<Map<String, Object>> answers = (List<Map<String, Object>>) payload.get("answers");
 
		 // Process the answers
		 // Each answer is a map with two keys: "question" and "answer"
 
		 for (Map<String, Object> answer : answers) {
			 Integer question = (Integer) answer.get("question");
			 Integer selectedOption = (Integer) answer.get("answer");
			
			 System.out.println(question + " - " + selectedOption);
			 // Process each answer
			 // ...
		 } 
        return ResponseEntity.ok("\"Success\"");
    }


	// get practice schedule
	@GetMapping("/getPracticeSchedule/{year}/{week}/{grade}")
	@ResponseBody
	public List<PracticeDTO> getPracticeSchedule(@PathVariable int year, @PathVariable int week, @PathVariable int grade) {
		List<PracticeDTO> dtos = new ArrayList<>();
		// 1. get PracticeScheduleDTO by year & week
		List<PracticeScheduleDTO> schedules = connectedService.listPracticeSchedule(year, week);
		// 2. get PracticeDTO from PracticeScheduleDTO
		for(PracticeScheduleDTO schedule : schedules){
			// 3. get PracticeDTO
			List<PracticeDTO> practices = schedule.getPractices();
			for(PracticeDTO practice : practices){
				// 4. get and compare grade
				int practiceGrade = Integer.parseInt(StringUtils.defaultString(practice.getGrade(), "0"));
				if(grade == practiceGrade){
					// 5. add to list
					dtos.add(practice);
				}
			}
		}
		// 6. return dtos
		return dtos;
	}

	// helper method converting practice answers Map to List
	private List<Integer> convertPracticeAnswers(List<Map<String, Object>> answers) {
		// Sort the answers based on the "question" key
		answers.sort(Comparator.comparingInt(answer -> Integer.parseInt(answer.get("question").toString())));

		List<Integer> answerList = new ArrayList<>();
		// 1st element represents total answer count
		answerList.add(0, answers.size());
		for (Map<String, Object> answer : answers) {
			int questionNum = Integer.parseInt(answer.get("question").toString());
			int selectedOption = Integer.parseInt(answer.get("answer").toString());
			answerList.add(questionNum, selectedOption);
		}
		return answerList;
	}

	// helper method converting test answers Map to List
	private List<Integer> convertTestAnswers(List<Map<String, Object>> answers) {
		// Sort the answers based on the "question" key
		answers.sort(Comparator.comparingInt(answer -> Integer.parseInt(answer.get("question").toString())));
		List<Integer> answerList = new ArrayList<>();
		for (Map<String, Object> answer : answers) {
//			int questionNum = Integer.parseInt(answer.get("question").toString());
			int selectedOption = Integer.parseInt(answer.get("answer").toString());
			answerList.add(selectedOption);
		}
		return answerList;
	}
}
