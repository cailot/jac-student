package hyung.jin.seo.jae.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.dto.ExtraworkDTO;
import hyung.jin.seo.jae.dto.ExtraworkProgressDTO;
import hyung.jin.seo.jae.dto.ExtraworkSummaryDTO;
import hyung.jin.seo.jae.dto.HomeworkDTO;
import hyung.jin.seo.jae.dto.HomeworkProgressDTO;
import hyung.jin.seo.jae.dto.HomeworkScheduleDTO;
import hyung.jin.seo.jae.dto.HomeworkSummaryDTO;
import hyung.jin.seo.jae.dto.PracticeAnswerDTO;
import hyung.jin.seo.jae.dto.PracticeDTO;
import hyung.jin.seo.jae.dto.PracticeScheduleDTO;
import hyung.jin.seo.jae.dto.PracticeSummaryDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.dto.StudentTestDTO;
import hyung.jin.seo.jae.dto.TestAnswerDTO;
import hyung.jin.seo.jae.dto.TestDTO;
import hyung.jin.seo.jae.dto.TestScheduleDTO;
import hyung.jin.seo.jae.dto.TestSummaryDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.ExtraworkProgress;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.model.HomeworkProgress;
import hyung.jin.seo.jae.model.Practice;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.model.StudentPractice;
import hyung.jin.seo.jae.model.StudentTest;
import hyung.jin.seo.jae.model.Test;
import hyung.jin.seo.jae.model.TestAnswerItem;
import hyung.jin.seo.jae.model.TestSchedule;
import hyung.jin.seo.jae.repository.MaterialRepository;
import hyung.jin.seo.jae.repository.TestScheduleRepository;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.ConnectedService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.OnlineSessionService;
import hyung.jin.seo.jae.service.PropertiesService;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Controller
@RequestMapping("connected")
public class ConnectedController {

	@Autowired
	private ConnectedService connectedService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	private CycleService cycleService;
	
	@Autowired
	private OnlineSessionService onlineSessionService;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private TestScheduleRepository testScheduleRepository;

	@Value("${mock.explanation.enabled:false}")
	private boolean mockExplanationEnabled;

	@Value("${mock.vsse.code:${mock.vsss.code:${mock.vss.code:0}}}")
	private long mockVsseCode;

	@Value("${mock.njac.code:0}")
	private long mockNjacCode;

	@Value("${test-day-override:}")
	private String recordedTestDayOverride;

	@GetMapping("/enrolGrade/{studentId}")
	@ResponseBody
	public String getEnrolGrade(@PathVariable Long studentId) {
		// During mock explanation period, mock login students have TT8 set as their Spring Security authority.
		// Return TT8 directly so the menu reflects the mock grade, not the DB enrolment grade.
		if (mockExplanationEnabled) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getAuthorities().stream()
					.anyMatch(a -> JaeConstants.TT8_CODE.equals(a.getAuthority()))) {
				return JaeConstants.TT8_CODE;
			}
		}
		return resolveConnectedEnrolGrade(studentId);
	}

	// Check session status
	@GetMapping("/sessionStatus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getSessionStatus(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			long maxInactiveInterval = session.getMaxInactiveInterval();
			long lastAccessedTime = session.getLastAccessedTime();
			long currentTime = System.currentTimeMillis();
			long remainingTime = (lastAccessedTime + (maxInactiveInterval * 1000)) - currentTime;
			
			response.put("valid", true);
			response.put("maxInactiveInterval", maxInactiveInterval);
			response.put("remainingTime", Math.max(0, remainingTime / 1000)); // in seconds
			response.put("lastAccessedTime", lastAccessedTime);
			response.put("currentTime", currentTime);
		} else {
			response.put("valid", false);
			response.put("message", "No active session");
		}
		
		return ResponseEntity.ok(response);
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
		// Prevent duplicate test submissions within the current cycle
		int currentYear = cycleService.academicYear();
		CycleDTO cycle = cycleService.listCycles(currentYear);
		// A transient DB failure can leave cycle null (e.g. connection pool outage). Fail fast
		// with a clear 503 instead of an NPE so the client error path can prompt a retry.
		if (cycle == null) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body("\"Submission temporarily unavailable. Please try again.\"");
		}
		boolean alreadySubmitted = connectedService.isStudentTestExist(
			Long.parseLong(studentId),
			Long.parseLong(testId),
			resolveTestTakenFromDate(cycle),
			cycle.getEndDate()
		);
		if (alreadySubmitted) {
			return ResponseEntity.ok("\"StudentTest already registered\"");
		}
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
		// 1. get PracticeDTO
		PracticeDTO dto = connectedService.getPracticeInfo(id);
		// 2. get answer count
		int answerCount = connectedService.getPracticeAnswerCountPerQuestion(id);
		dto.setAnswerCount(answerCount);
		// 3. get question count
		int questionCount = connectedService.getPracticeAnswerCount(id);
		dto.setQuestionCount(questionCount);
		// 4. return dto
		return dto;
	}

	// get test
	@GetMapping("/getTest/{id}")
	@ResponseBody
	public TestDTO getTest(@PathVariable Long id) {
		// 1. get TestDTO
		TestDTO dto = connectedService.getTestInfo(id);
		// 2. get answer count
		int answerCount = connectedService.getTestAnswerCountPerQuestion(id);
		dto.setAnswerCount(answerCount);
		// 3. get question count
		int questionCount = connectedService.getTestAnswerCount(id);
		dto.setQuestionCount(questionCount);
		// 4. return dto
		return dto;
	}

	// get test answer by test id
	@GetMapping("/getTestAnswer/{id}")
	@ResponseBody
	public TestAnswerDTO getTestAnswer(@PathVariable Long id) {
		// 1. get TestDTO
		TestAnswerDTO dto = connectedService.findTestAnswerByTest(id);
		// 4. return dto
		return dto;
	}

	// search homework by id
	@GetMapping("/homework/{homeworkId}")
	@ResponseBody
	public ResponseEntity<HomeworkDTO> searchHomework(@PathVariable long homeworkId) {
		if (homeworkId <= 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Homework work = connectedService.getHomework(homeworkId);
		if (work == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		HomeworkDTO dto = new HomeworkDTO(work);
		return ResponseEntity.ok(dto);
	}

	// // search practice by id
	@GetMapping("/practiceAnswer/{studentId}/{practiceId}")
	@ResponseBody
	public PracticeAnswerDTO searchPracticeAnswer(@PathVariable String studentId, @PathVariable String practiceId) {
		String filteredStudentId = StringUtils.defaultString(studentId, "0");
		String filteredPracticeId = StringUtils.defaultString(practiceId, "0");
		PracticeAnswerDTO dto = connectedService.findPracticeAnswerByPractice(Long.parseLong(filteredPracticeId));
		// get answer count
		int answerCount = connectedService.getPracticeAnswerCountPerQuestion(Long.parseLong(filteredPracticeId));
		dto.setAnswerCount(answerCount);
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
		int currentYear = cycleService.academicYear();
		CycleDTO cycle = cycleService.listCycles(currentYear);
		List<Integer> answers = connectedService.getStudentTestAnswer(Long.parseLong(filteredStudentId), Long.parseLong(filteredTestId), cycle.getStartDate(), cycle.getEndDate());
		dto.setStudents(answers);
		return dto;
	}

	// bring homework in database
	@GetMapping("/filterHomework")
	public String listHomeworks(
			@RequestParam(value = "listSubject", required = false) String subject,
			@RequestParam(value = "listGrade", required = false) String grade,
			@RequestParam(value = "listWeek", required = false) String week, 
			Model model) {
		List<HomeworkDTO> dtos = new ArrayList();
		String filteredSubject = StringUtils.defaultString(subject, "0");
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		String filteredWeek = StringUtils.defaultString(week, "0");
		dtos = connectedService.listHomework(Integer.parseInt(filteredSubject), filteredGrade, Integer.parseInt(filteredWeek));		
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
	@GetMapping("/summaryExtraworkAll/{grade}")
	@ResponseBody
	public List<SimpleBasketDTO> summaryExtraworkAll(@PathVariable String grade) {
		List<SimpleBasketDTO> dtos = new ArrayList();
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		dtos = connectedService.loadExtrawork(filteredGrade);	
		return dtos;
	}

	@GetMapping("/summaryExtrawork/{studentId}")
	@ResponseBody
	public List<ExtraworkSummaryDTO> summaryExtraworks(@PathVariable long studentId) {
		int academicYear = cycleService.academicYear();
		int academicWeek = cycleService.academicWeeks();
		String grade = onlineSessionService.getOnlineSessionGrade(studentId, academicYear, academicWeek);
		List<ExtraworkSummaryDTO> dtos = new ArrayList();
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		List<SimpleBasketDTO> baskets = connectedService.loadExtrawork(filteredGrade);
		for(SimpleBasketDTO basket : baskets){
			ExtraworkSummaryDTO dto = new ExtraworkSummaryDTO();
			dto.setId(Long.parseLong(basket.getValue()));
			dto.setTitle(basket.getName());
			int percentage = connectedService.getExtraworkProgressPercentage(studentId, dto.getId());
			dto.setPercentage(percentage);
			dtos.add(dto);
		}	
		return dtos;
	}

	@GetMapping("/summaryPractice/{practiceGroup}/{studentId}")
	@ResponseBody
	public List<PracticeSummaryDTO> summaryPractices(@PathVariable int practiceGroup, @PathVariable long studentId) {
		// 1. get current LocalDateTime & current week
		LocalDateTime now = LocalDateTime.now();
		int currentYear = cycleService.academicYear();
		int currentWeek = cycleService.academicWeeks();
		String grade = onlineSessionService.getOnlineSessionGrade(studentId, currentYear, currentWeek);

		// TT6/TT8 are a standalone track. At week 1, getOnlineSessionGrade resolves the previous
		// academic year's last week, which misses students newly enrolled into TT this year
		// (e.g. P6 last year -> TT6 now). Prefer the current TT enrolment grade so Practice still
		// appears when it matches the schedule. Only TT grades are affected; P/S resolution is unchanged.
		if (currentWeek == JaeConstants.FIRST_WEEK) {
			String currentGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(studentId));
			if (isTtGrade(currentGrade)) {
				grade = currentGrade;
			}
		}

		// 2. get PracticeScheduleDTO by current time, practiceType & grade
		List<PracticeScheduleDTO> schedules = connectedService.checkPracticeSchedule(practiceGroup+"", grade, now);
		// 3. check if schedule is empty
		if(schedules.isEmpty()){
			// 3-1. if empty, return empty list
			return new ArrayList<>();
		}else{
			// 3-2. if not empty, get practice list
			List<PracticeSummaryDTO> dtos = new ArrayList<>();
			// Use Set to track unique combinations of (practiceId, week) to prevent duplicates
			// This ensures the same practice doesn't appear multiple times while preserving
			// different practices that may have the same title
			Set<String> seenKeys = new HashSet<>();
			outter:for(PracticeScheduleDTO schedule : schedules){
				
				String[] groups = schedule.getPracticeGroup();
				String[] weeks = schedule.getWeek();
				
				inner:for(int i=0; i<groups.length; i++){
					System.out.println(groups[i] + " : " + weeks[i]);
					int group = Integer.parseInt(groups[i]);
					if(group != practiceGroup) continue inner;
					int week = Integer.parseInt(weeks[i]);

					List<PracticeDTO> practices = connectedService.getPracticeInfoByGroup(practiceGroup, grade, week);
				
					for(PracticeDTO practice : practices){
						// add to list
						long practiceId = Long.parseLong(practice.getId());
						
						// Create a unique key based on practiceId and week to prevent duplicates
						// This safely prevents the same practice from appearing multiple times
						// while ensuring different practices with the same title are not filtered out
						String uniqueKey = practiceId + "_" + week;
						
						// Only add if we haven't seen this combination before
						if(!seenKeys.contains(uniqueKey)){
							seenKeys.add(uniqueKey);
							PracticeSummaryDTO dto = new PracticeSummaryDTO();
							String title = practice.getTitle();
							boolean done = connectedService.isStudentPracticeExist(studentId, practiceId);
							if(done){
								title = title + JaeConstants.PRACTICE_COMPLETE;
							}
							dto.setId(practiceId);
							dto.setTitle(title);
							dto.setWeek(week);
							dtos.add(dto);
						}
					}
				}

			}
			// Sort by week (Set number) in ascending order
			dtos.sort(Comparator.comparingInt(PracticeSummaryDTO::getWeek));
			return dtos;
		}
	}

	@GetMapping("/summaryPractice/{studentId}/{practiceType}/{grade}")
	@ResponseBody
	public List<SimpleBasketDTO> summaryPracticesByType(@PathVariable long studentId, @PathVariable int practiceType, @PathVariable String grade) {
		// Get practice list by type and grade
		List<SimpleBasketDTO> baskets = connectedService.loadPractice(practiceType, Integer.parseInt(grade));
		
		// Apply deduplication logic to prevent duplicates
		// Use Set to track unique combinations of (name, value) to prevent duplicates
		Set<String> seenKeys = new HashSet<>();
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		
		for(SimpleBasketDTO basket : baskets){
			// Create a unique key based on name and value to prevent duplicates
			// Normalize name by removing DONE suffix if present
			String normalizedName = basket.getName().endsWith(JaeConstants.PRACTICE_COMPLETE) 
				? basket.getName().substring(0, basket.getName().length() - JaeConstants.PRACTICE_COMPLETE.length())
				: basket.getName();
			String uniqueKey = normalizedName + "_" + basket.getValue();
			
			// Only add if we haven't seen this combination before
			if(!seenKeys.contains(uniqueKey)){
				seenKeys.add(uniqueKey);
				dtos.add(basket);
			}
		}
		
		return dtos;
	}

	@GetMapping("/summaryTest/{testGroup}/{studentId}")
	@ResponseBody
	public List<TestSummaryDTO> summaryTests(@PathVariable String testGroup, @PathVariable long studentId) {
		// Parse testGroup string into array of integers
		String[] testGroupStrings = testGroup.split(",");
		int[] testGroupArray = new int[testGroupStrings.length];
		for(int i = 0; i < testGroupStrings.length; i++) {
			testGroupArray[i] = Integer.parseInt(testGroupStrings[i].trim());
		}
		
		// 1. get current LocalDateTime & current week
		LocalDateTime now = LocalDateTime.now();
		int academicYear = cycleService.academicYear();
		int academicWeek = cycleService.academicWeeks();
		String grade = onlineSessionService.getOnlineSessionGrade(studentId, academicYear, academicWeek);

		// TT6/TT8 are a standalone Class Test track (not part of the P/S progression).
		// At week 1, getOnlineSessionGrade resolves the previous academic year's last week,
		// which misses students newly enrolled into TT this year (e.g. P6 last year -> TT6 now).
		// Prefer the current TT enrolment grade so the Class Test still appears when it matches
		// the schedule. Only TT grades are affected; P/S grade resolution is unchanged.
		if (academicWeek == JaeConstants.FIRST_WEEK) {
			String currentGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(studentId));
			if (isTtGrade(currentGrade)) {
				grade = currentGrade;
			}
		}

		// 2. get TestScheduleDTO by current time, testGroup & grade
		List<TestScheduleDTO> schedules = connectedService.checkTestSchedule(testGroup, grade, now);
		// 3. check if schedule is empty
		if(schedules.isEmpty()){
			// 3-1. if empty, return empty list
			return new ArrayList<>();
		}else{
			// 3-2. if not empty, get test list
			List<TestSummaryDTO> dtos = new ArrayList<>();
			// Use Set to track unique combinations of (testId, week) to prevent duplicates
			// This ensures the same test doesn't appear multiple times while preserving
			// different tests that may have the same title
			Set<String> seenKeys = new HashSet<>();
			outter:for(TestScheduleDTO schedule : schedules){				
				String[] groups = schedule.getTestGroup();
				String[] weeks = schedule.getWeek();
				inner:for(int i=0; i<groups.length; i++){
					System.out.println(groups[i] + " : " + weeks[i]);
					int group = Integer.parseInt(groups[i]);
					// Check if this group is in our testGroupArray
					boolean groupFound = false;
					for(int testGroupItem : testGroupArray) {
						if(group == testGroupItem) {
							groupFound = true;
							break;
						}
					}
					if(!groupFound) continue inner;
					
					int week = Integer.parseInt(weeks[i]);
					List<TestDTO> tests = connectedService.getTestInfoByGroup(group, grade, week);
					for(TestDTO test : tests){
						// add to list
						long testId = Long.parseLong(test.getId());
						
						// Create a unique key based on testId and week to prevent duplicates
						// This safely prevents the same test from appearing multiple times
						// while ensuring different tests with the same title are not filtered out
						String uniqueKey = testId + "_" + week;
						
						// Only add if we haven't seen this combination before
						if(!seenKeys.contains(uniqueKey)){
							seenKeys.add(uniqueKey);
							TestSummaryDTO dto = new TestSummaryDTO();
							String title = test.getName();
							int currentYear = cycleService.academicYear();
							CycleDTO cycle = cycleService.listCycles(currentYear);
							boolean done = connectedService.isStudentTestExist(studentId, testId, resolveTestTakenFromDate(cycle), cycle.getEndDate());
							if(done){
								title = title + JaeConstants.PRACTICE_COMPLETE;
							}
							dto.setId(testId);
							dto.setTitle(title);
							dto.setWeek(week);
							dtos.add(dto);
						}
					}
				}
			}
			// Sort by week (Set number) in ascending order
			dtos.sort(Comparator.comparingInt(TestSummaryDTO::getWeek));
			return dtos;
		}
	}

	@GetMapping("/summaryMockTestExplanation/{studentId}")
	@ResponseBody
	public List<TestSummaryDTO> summaryMockTestExplanation(@PathVariable long studentId) {
		if (!mockExplanationEnabled) {
			return new ArrayList<>();
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getAuthorities().stream()
				.noneMatch(a -> JaeConstants.TT8_CODE.equals(a.getAuthority()))) {
			return new ArrayList<>();
		}
		// Use the same schedule lookup as login window check (grade + MOCK_TEST_NO week).
		// This avoids relying on testGroup exact-match since the stored testGroup value is unknown.
		TestSchedule mockSchedule = testScheduleRepository.findLatestActiveExplanationWindowByGradeAndWeek(
				JaeConstants.TT8_CODE, JaeConstants.MOCK_TEST_NO);
		if (mockSchedule == null
				|| mockSchedule.getExplanationFromDatetime() == null
				|| mockSchedule.getExplanationToDatetime() == null) {
			return new ArrayList<>();
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(mockSchedule.getExplanationFromDatetime())
				|| now.isAfter(mockSchedule.getExplanationToDatetime())) {
			return new ArrayList<>();
		}
		// Parse testGroup/week arrays stored in the schedule and retrieve matching tests.
		String[] groups = JaeUtils.splitString(mockSchedule.getTestGroup());
		String[] weeks = JaeUtils.splitString(mockSchedule.getWeek());
		int currentYear = cycleService.academicYear();
		CycleDTO cycle = cycleService.listCycles(currentYear);
		if (cycle == null) {
			return new ArrayList<>();
		}
		if (!hasMockMaterialRegistration(studentId, cycle)) {
			return new ArrayList<>();
		}
		List<TestSummaryDTO> dtos = new ArrayList<>();
		Set<String> seenKeys = new HashSet<>();
		for (int i = 0; i < groups.length; i++) {
			int group = Integer.parseInt(groups[i]);
			int week = Integer.parseInt(weeks[i]);
			List<TestDTO> tests = connectedService.getTestInfoByGroup(group, JaeConstants.TT8_CODE, week);
			for (TestDTO test : tests) {
				// Writing tests are not part of the mock explanation
				if (test.getName() != null && test.getName().toLowerCase().contains("writing")) {
					continue;
				}
				long testId = Long.parseLong(test.getId());
				String uniqueKey = testId + "_" + week;
				if (!seenKeys.contains(uniqueKey)) {
					seenKeys.add(uniqueKey);
					TestSummaryDTO dto = new TestSummaryDTO();
					dto.setId(testId);
					dto.setTitle(test.getName());
					dto.setWeek(week);
					dtos.add(dto);
				}
			}
		}
		dtos.sort(Comparator.comparingInt(TestSummaryDTO::getWeek));
		return dtos;
	}

	private boolean hasMockMaterialRegistration(long studentId, CycleDTO cycle) {
		try {
			long minInvoiceId = studentId * 1000L;
			long maxInvoiceId = (studentId + 1L) * 1000L;
			List<Long> bookIds = materialRepository.findBookIdsByStudentInvoice(
					minInvoiceId, maxInvoiceId, cycle.getStartDate(), cycle.getEndDate());
			return bookIds != null && bookIds.stream()
					.filter(id -> id != null)
					.anyMatch(id -> id == mockVsseCode || id == mockNjacCode);
		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping("/summaryTest4Explanation/{testGroup}/{studentId}")
	@ResponseBody
	public List<TestSummaryDTO> summaryTest4Explanation(@PathVariable int testGroup, @PathVariable long studentId) {
		final int mockScheduleWeek = Integer.parseInt(JaeConstants.MOCK_TEST_NO);
		// 1. get current LocalDateTime & current week
		LocalDateTime now = LocalDateTime.now();
		LocalDate effectiveDate = resolveConnectedEffectiveDate();
		int academicYear = getConnectedAcademicYear(effectiveDate);
		int academicWeek = getConnectedAcademicWeek(effectiveDate);
		// Weeks 1-3 keep the previous academic year's last-week grade (TT6/TT8 excluded),
		// so transitioning students still see last year's Test Explanation content.
		String explanationGrade = academicWeek <= JaeConstants.THIRD_WEEK
				? resolveTestExplanationGradeForWeekOne(studentId, academicYear)
				: resolveConnectedEnrolGrade(studentId);
		if (StringUtils.isBlank(explanationGrade)) {
			return new ArrayList<>();
		}

		// 2. get TestScheduleDTO by current time, testGroup & grade
		List<TestScheduleDTO> schedules = connectedService.checkTestSchedule4Explanation(testGroup+"", explanationGrade, now);
		// 3. check if schedule is empty
		if(schedules.isEmpty()){
			// 3-1. if empty, return empty list
			return new ArrayList<>();
		}else{
			// 3-2. if not empty, get test list
			List<TestSummaryDTO> dtos = new ArrayList<>();
			// Use Set to track unique combinations of (testId, week) to prevent duplicates
			// This ensures the same test doesn't appear multiple times while preserving
			// different tests that may have the same title
			Set<String> seenKeys = new HashSet<>();
			outter:for(TestScheduleDTO schedule : schedules){				
				String[] groups = schedule.getTestGroup();
				String[] weeks = schedule.getWeek();
				inner:for(int i=0; i<groups.length; i++){
					//System.out.println(groups[i] + " : " + weeks[i]);
					int group = Integer.parseInt(groups[i]);
					if(group != testGroup) continue inner;
					int week = Integer.parseInt(weeks[i]);
					// Test explanation should follow the enrolled grade context returned by
					// getOnlineSessionGrade for each academic week.
					String currentGrade = explanationGrade;
					List<TestDTO> tests = connectedService.getTestInfoByGroup(testGroup, currentGrade, week);
					for(TestDTO test : tests){
						long testId = Long.parseLong(test.getId());
						if (!mockExplanationEnabled) {
							if (week == mockScheduleWeek) {
								continue;
							}
							if (connectedService.getTestGroup(testId) == JaeConstants.MOCK_TEST) {
								continue;
							}
							String nm = test.getName();
							if (nm != null && nm.toLowerCase().contains("(mock)")) {
								continue;
							}
						}
						// add to list
						
						// Create a unique key based on testId and week to prevent duplicates
						// This safely prevents the same test from appearing multiple times
						// while ensuring different tests with the same title are not filtered out
						String uniqueKey = testId + "_" + week;
						
						// Only add if we haven't seen this combination before
						if(!seenKeys.contains(uniqueKey)){
							seenKeys.add(uniqueKey);
							TestSummaryDTO dto = new TestSummaryDTO();
							String title = test.getName();
							dto.setId(testId);
							dto.setTitle(title);
							dto.setWeek(week);
							dtos.add(dto);
						}
					}
				}
			}
			// Sort by week (Set number) in ascending order
			dtos.sort(Comparator.comparingInt(TestSummaryDTO::getWeek));
			return dtos;
		}
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
		// 1. get current year 
		int currentYear = cycleService.academicYear();
		CycleDTO cycle = cycleService.listCycles(currentYear);
		// 2. loop through each test type in the array
		for (String type : types) {
			// can I simply use SimpleBasketDTO for testId & testTypeName ??...
			StudentTestDTO dto = connectedService.getStudentTest(Long.parseLong(filteredStudentId), Long.parseLong(type), filteredGrade, Integer.parseInt(filteredVolume), cycle.getStartDate(), cycle.getEndDate());
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

	// get subject list
	@GetMapping("/subjectList/{subject}/{student}")
	@ResponseBody
	public List<HomeworkSummaryDTO> subjectList(@PathVariable String subject, @PathVariable long student) {
		int subjectCard = 0;
		// int answerCard = 0;
		
		// 1. get current LocalDateTime & current week
		LocalDateTime now = LocalDateTime.now();
		LocalDate effectiveDate = resolveConnectedEffectiveDate();
		int currentYear = getConnectedAcademicYear(effectiveDate);
		int currentWeek = getConnectedAcademicWeek(effectiveDate);
		String grade = resolveConnectedHomeworkGrade(student, currentYear, currentWeek);

		// 2. get weeks from properties or schedule by checking database
		HomeworkScheduleDTO schedule = connectedService.getHomeworkScheduleBySubjectAndGrade(subject, grade, now);
		if(schedule == null){
			// 2-1. get cards count from properties
			subjectCard = propertiesService.getSubjectCardCount();
			// answerCard = propertiesService.getAnswerCardCount();
		}else{
			// 2-2. get cards count from schedule
			subjectCard = schedule.getSubjectDisplay();
			// answerCard = schedule.getAnswerDisplay();
		}
		// 3. calculate and get Homework info (id & week)
		List<HomeworkSummaryDTO> dtos = new ArrayList<>();

		////////////////////////////////////////////////////////////////////////////////////////////////
		// if week is first week of academic year, check student's register date is more than a month.
		////////////////////////////////////////////////////////////////////////////////////////////////
		if(currentWeek == JaeConstants.FIRST_WEEK){

			Student std = studentService.getStudent(student);
			String latestEnrolmentGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(student));
			String registeredGrade = StringUtils.trimToEmpty(std.getGrade());
			String currentGrade = StringUtils.isNotBlank(latestEnrolmentGrade)
					? latestEnrolmentGrade
					: (StringUtils.isNotBlank(registeredGrade) ? registeredGrade : grade);
			String previousYearGrade = StringUtils.trimToEmpty(
					onlineSessionService.getOnlineSessionGrade(student, currentYear, currentWeek));
			boolean isNewRegistrationWithoutPreviousYearEnrolment =
					StringUtils.isNotBlank(currentGrade) && StringUtils.isBlank(previousYearGrade);
			LocalDate regDate = std.getRegisterDate();
			// check if regDate is less than last month compared with today
			LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
			boolean eligibleForWeekOneHomework = isNewRegistrationWithoutPreviousYearEnrolment
					|| regDate == null
					|| regDate.isBefore(oneMonthAgo);
			if (eligibleForWeekOneHomework) {
				// If student's register date is more than a month, week 1 shows last 2 sets
				// from previous-year grade (if available), then Set 1 by grade rule below.
				// get last week of last year
				int lastWeek = cycleService.lastAcademicWeek(cycleService.academicYear()-1);
				String previousGrade = codeService.getPreviousGrade(currentGrade);
				String set4849Grade;
				if (StringUtils.isNotBlank(previousYearGrade)) {
					// Continuous enrolment: use actual previous-year grade for Set48/49.
					set4849Grade = previousYearGrade;
				} else if (isNewRegistrationWithoutPreviousYearEnrolment) {
					// New enrolment without previous-year row: A3 rule (previous grade for Set48/49).
					// TT6/TT8 are standalone tracks (no grade-1 progression): keep current grade.
					if (isTtGrade(currentGrade)) {
						set4849Grade = currentGrade;
					} else {
						set4849Grade = StringUtils.isNotBlank(previousGrade) && !"0".equals(previousGrade)
								? previousGrade
								: currentGrade;
					}
				} else {
					set4849Grade = currentGrade;
				}

				// 2nd last week of current grade
				HomeworkSummaryDTO dto2 = new HomeworkSummaryDTO();
				long homeworkId2 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), set4849Grade, lastWeek-1);
				int percentage2 = connectedService.getHomeworkProgressPercentage(student, homeworkId2);
				dto2.setWeek(lastWeek-1);
				dto2.setId(homeworkId2);
				dto2.setPercentage(percentage2);
				dtos.add(dto2);
				
				// last week of current grade
				HomeworkSummaryDTO dto1 = new HomeworkSummaryDTO();
				long homeworkId1 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), set4849Grade, lastWeek);
				int percentage1 = connectedService.getHomeworkProgressPercentage(student, homeworkId1);
				dto1.setWeek(lastWeek);
				dto1.setId(homeworkId1);
				dto1.setPercentage(percentage1);
				dtos.add(dto1);

				// 1st week of current grade
				HomeworkSummaryDTO dto3 = new HomeworkSummaryDTO();
				boolean keepCurrentGradeForSetOne =
						"TT6".equalsIgnoreCase(set4849Grade)
						|| "TT8".equalsIgnoreCase(set4849Grade)
						|| "JMSS".equalsIgnoreCase(set4849Grade);
				String nextGrade = codeService.getNextGrade(set4849Grade);
				String setOneGrade = isNewRegistrationWithoutPreviousYearEnrolment
						? currentGrade
						: (keepCurrentGradeForSetOne
								? set4849Grade
								: (StringUtils.isNotBlank(nextGrade) && !"0".equals(nextGrade)
										? nextGrade
										: (StringUtils.isNotBlank(currentGrade) ? currentGrade : set4849Grade)));
				long homeworkId3 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), setOneGrade, 1);
				int percentage3 = connectedService.getHomeworkProgressPercentage(student, homeworkId3);
				dto3.setWeek(1);
				dto3.setId(homeworkId3);
				dto3.setPercentage(percentage3);
				dtos.add(dto3);

				return dtos;
			}

		}else if(currentWeek == JaeConstants.SECOND_WEEK){

			Student std = studentService.getStudent(student);
			LocalDate regDate = std.getRegisterDate();
			// check if regDate is less than last month compared with today
			LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
			if(regDate != null && regDate.isBefore(oneMonthAgo)){
				// if student's register date is more than a month, return 2 homework from previous grade
				String stdGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(student));
				if (StringUtils.isBlank(stdGrade)) {
					stdGrade = std.getGrade();
				}
				String previousGrade = codeService.getPreviousGrade(stdGrade);
				// get last week of last year
				int lastWeek = cycleService.lastAcademicWeek(cycleService.academicYear()-1);
				
				// last week of previous grade
				HomeworkSummaryDTO dto1 = new HomeworkSummaryDTO();
				long homeworkId1 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), previousGrade, lastWeek);
				int percentage1 = connectedService.getHomeworkProgressPercentage(student, homeworkId1);
				dto1.setWeek(lastWeek);
				dto1.setId(homeworkId1);
				dto1.setPercentage(percentage1);
				dtos.add(dto1);

				// 1st week of current grade
				HomeworkSummaryDTO dto2 = new HomeworkSummaryDTO();
				long homeworkId2 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), stdGrade, 1);
				int percentage2 = connectedService.getHomeworkProgressPercentage(student, homeworkId2);
				dto2.setWeek(1);
				dto2.setId(homeworkId2);
				dto2.setPercentage(percentage2);
				dtos.add(dto2);

				// 2nd week of current grade
				HomeworkSummaryDTO dto3 = new HomeworkSummaryDTO();
				long homeworkId3 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), stdGrade, 2);
				int percentage3 = connectedService.getHomeworkProgressPercentage(student, homeworkId3);
				dto3.setWeek(2);
				dto3.setId(homeworkId3);
				dto3.setPercentage(percentage3); 
				dtos.add(dto3);

				return dtos;
			}

			// New (recently registered) students with no previous-year enrolment used to fall
			// through this else-if and receive an empty list (only the locked next Set appeared
			// on the page). Mirror the week-1 new-registration rule so they still see the previous
			// grade's last week (Set 49) plus the current grade's Set 1 & Set 2.
			String latestEnrolmentGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(student));
			String registeredGrade = StringUtils.trimToEmpty(std.getGrade());
			String currentGrade = StringUtils.isNotBlank(latestEnrolmentGrade)
					? latestEnrolmentGrade
					: (StringUtils.isNotBlank(registeredGrade) ? registeredGrade : grade);
			String previousYearGrade = resolvePreviousYearLastWeekGrade(student, currentYear);
			boolean isNewRegistrationWithoutPreviousYearEnrolment =
					StringUtils.isNotBlank(currentGrade) && StringUtils.isBlank(previousYearGrade);
			if(isNewRegistrationWithoutPreviousYearEnrolment || regDate == null){
				// Set 49 grade follows the same rule as Homework week-1 Set 48/49 for new registrations.
				String previousGrade = codeService.getPreviousGrade(currentGrade);
				String set49Grade;
				if (isTtGrade(currentGrade)) {
					// TT6/TT8 are standalone tracks (no grade-1 progression): keep current grade.
					set49Grade = currentGrade;
				} else if (StringUtils.isNotBlank(previousGrade) && !"0".equals(previousGrade)) {
					set49Grade = previousGrade;
				} else {
					set49Grade = currentGrade;
				}
				int lastWeek = cycleService.lastAcademicWeek(cycleService.academicYear()-1);

				// last week of previous grade (Set 49)
				HomeworkSummaryDTO dtoPrev = new HomeworkSummaryDTO();
				long homeworkIdPrev = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), set49Grade, lastWeek);
				int percentagePrev = connectedService.getHomeworkProgressPercentage(student, homeworkIdPrev);
				dtoPrev.setWeek(lastWeek);
				dtoPrev.setId(homeworkIdPrev);
				dtoPrev.setPercentage(percentagePrev);
				dtos.add(dtoPrev);

				// 1st week of current grade
				HomeworkSummaryDTO dtoSet1 = new HomeworkSummaryDTO();
				long homeworkIdSet1 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), currentGrade, 1);
				int percentageSet1 = connectedService.getHomeworkProgressPercentage(student, homeworkIdSet1);
				dtoSet1.setWeek(1);
				dtoSet1.setId(homeworkIdSet1);
				dtoSet1.setPercentage(percentageSet1);
				dtos.add(dtoSet1);

				// 2nd week of current grade
				HomeworkSummaryDTO dtoSet2 = new HomeworkSummaryDTO();
				long homeworkIdSet2 = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), currentGrade, 2);
				int percentageSet2 = connectedService.getHomeworkProgressPercentage(student, homeworkIdSet2);
				dtoSet2.setWeek(2);
				dtoSet2.setId(homeworkIdSet2);
				dtoSet2.setPercentage(percentageSet2);
				dtos.add(dtoSet2);

				return dtos;
			}

		}else{
			// if week is not first/second week of academic year, return normal homeworks from current grade
			for(int i = (subjectCard-1) ; i >= 0; i--){
				HomeworkSummaryDTO dto = new HomeworkSummaryDTO();
				long homeworkId = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), grade, (currentWeek-i));
				int percentage = connectedService.getHomeworkProgressPercentage(student, homeworkId);
				dto.setWeek(currentWeek - i);
				dto.setId(homeworkId);
				dto.setPercentage(percentage);
				dtos.add(dto);
			}
		}
		
		// 4. return HomeworkDTO
		return dtos;
	}

	// get short answer list
	@GetMapping("/shortAnswerList/{subject}/{student}")
	@ResponseBody
	public List<HomeworkSummaryDTO> shortAnswerList(@PathVariable String subject, @PathVariable long student) {
		// int subjectCard = 0;
		int answerCard = 0;	
		// 1. get current LocalDateTime & current week
		LocalDateTime now = LocalDateTime.now();
		LocalDate effectiveDate = resolveConnectedEffectiveDate();
		int academicYear = getConnectedAcademicYear(effectiveDate);
		int academicWeek = getConnectedAcademicWeek(effectiveDate);
		int currentWeek = academicWeek-1; // -1 to get the previous week
		String grade = resolveConnectedHomeworkGrade(student, academicYear, academicWeek);
		// 2. get weeks from properties or schedule by checking database
		HomeworkScheduleDTO schedule = connectedService.getHomeworkScheduleBySubjectAndGrade(subject, grade, now);
		if(schedule == null){
			// 2-1. get cards count from properties
			// subjectCard = propertiesService.getSubjectCardCount();
			answerCard = propertiesService.getAnswerCardCount();
		}else{
			// 2-2. get cards count from schedule
			// subjectCard = schedule.getSubjectDisplay();
			answerCard = schedule.getAnswerDisplay();
		}
		// 3. calculate and get Homework info (id & week)
		List<HomeworkSummaryDTO> dtos = new ArrayList<>();
		////////////////////////////////////////////////////////////////////////////////////////////////
		// if week is first week of academic year, check student's register date is more than a month.
		////////////////////////////////////////////////////////////////////////////////////////////////
		if(academicWeek == JaeConstants.FIRST_WEEK){ // currentWeek is academicWeek-1
			// Week 1: Set 49 follows the same grade rule as Homework Set 48/49.
			String currentGrade = grade;
			String previousYearGrade = StringUtils.trimToEmpty(
					onlineSessionService.getOnlineSessionGrade(student, academicYear, academicWeek));
			boolean isNewRegistrationWithoutPreviousYearEnrolment =
					StringUtils.isNotBlank(currentGrade) && StringUtils.isBlank(previousYearGrade);
			String previousGrade = codeService.getPreviousGrade(currentGrade);
			String set49Grade;
			if (StringUtils.isNotBlank(previousYearGrade)) {
				// Continuous enrolment (e.g. P5 last year and P5 this year): use actual previous-year grade.
				set49Grade = previousYearGrade;
			} else if (isNewRegistrationWithoutPreviousYearEnrolment) {
				// New enrolment without previous-year row: use grade-1 (e.g. new S9 -> S8 Set 49).
				// TT6/TT8 are standalone tracks (no grade-1 progression): keep current grade.
				if (isTtGrade(currentGrade)) {
					set49Grade = currentGrade;
				} else {
					set49Grade = StringUtils.isNotBlank(previousGrade) && !"0".equals(previousGrade)
							? previousGrade
							: currentGrade;
				}
			} else {
				set49Grade = currentGrade;
			}
			int lastWeek = cycleService.lastAcademicWeek(cycleService.academicYear()-1);
			HomeworkSummaryDTO dto = new HomeworkSummaryDTO();
			long homeworkId = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), set49Grade, lastWeek);
			if (homeworkId <= 0) {
				homeworkId = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), currentGrade, lastWeek);
			}
			int percentage = connectedService.getHomeworkProgressPercentage(student, homeworkId);
			dto.setWeek(lastWeek);
			dto.setId(homeworkId);
			dto.setPercentage(percentage);
			dtos.add(dto);
			return dtos;

		}else if(academicWeek == JaeConstants.SECOND_WEEK){
			// Week 2: always expose Set 1 for the student's resolved grade. 
			HomeworkSummaryDTO dto = new HomeworkSummaryDTO();
			long homeworkId = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), grade, 1);
			int percentage = connectedService.getHomeworkProgressPercentage(student, homeworkId);
			dto.setWeek(1);
			dto.setId(homeworkId);
			dto.setPercentage(percentage);
			dtos.add(dto);
			return dtos;

		}else{
			// if week is not first/second week of academic year, return normal short answer from current grade
			for(int i = (answerCard-1) ; i >= 0; i--){
				HomeworkSummaryDTO dto = new HomeworkSummaryDTO();
				long homeworkId = connectedService.getHomeworkIdByWeek(Long.parseLong(subject), grade, (currentWeek-i));
				int percentage = connectedService.getHomeworkProgressPercentage(student, homeworkId);
				dto.setWeek(currentWeek - i);
				dto.setId(homeworkId);
				dto.setPercentage(percentage);
				dtos.add(dto);
			}
		}
		// 4. return HomeworkDTO
		return dtos;
	}

	@PostMapping("/updateHomeworkProgress")
    public ResponseEntity<String> updateHomeworkProgress(@RequestBody HomeworkProgressDTO progress) {
        try {
			Long homework = progress.getHomeworkId();
			Long student = progress.getStudentId();
			//check if record exists
			HomeworkProgress existing = connectedService.getHomeworkProgressByStudentNHomework(student, homework);
			if(existing == null){	// create new record
				HomeworkProgress add = new HomeworkProgress();
				Homework homwork = connectedService.getHomework(homework);
				Student stud = studentService.getStudent(student);
				add.setHomework(homwork);
				add.setStudent(stud);
				add.setPercentage(progress.getPercentage());
				connectedService.addHomeworkProgress(add);	
			}else{ // update existing record
				connectedService.updateHomeworkProgressPercentage(existing.getId(), progress.getPercentage());
			}
			return ResponseEntity.ok("Progress updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error updating progress: " + e.getMessage());
		}
	}

	@PostMapping("/updateExtraworkProgress")
    public ResponseEntity<String> updateExtraworkProgress(@RequestBody ExtraworkProgressDTO progress) {
        try {
			Long extrawork = progress.getExtraworkId();
			Long student = progress.getStudentId();
			//check if record exists
			ExtraworkProgress existing = connectedService.getExtraworkProgressByStudentNHomework(student, extrawork);
			if(existing == null){	// create new record
				ExtraworkProgress add = new ExtraworkProgress();
				Extrawork work = connectedService.getExtrawork(extrawork);
				Student stud = studentService.getStudent(student);
				add.setExtrawork(work);
				add.setStudent(stud);
				add.setPercentage(progress.getPercentage());
				connectedService.addExtraworkProgress(add);	
			}else{ // update existing record
				connectedService.updateExtraworkProgressPercentage(existing.getId(), progress.getPercentage());
			}
			return ResponseEntity.ok("Progress updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error updating progress: " + e.getMessage());
		}
	}
	
	@GetMapping("/studentTestDate/{studentId}/{testId}")
	@ResponseBody
	public String getReportAddress(@PathVariable long studentId, @PathVariable long testId) {
		// 1. get current year 
		int currentYear = cycleService.academicYear();
		CycleDTO cycle = cycleService.listCycles(currentYear);
		// 2. get test date (transition weeks widen the range to include previous-cycle onsite records)
		String timeString = connectedService.getRegDateforStudentTest(studentId, testId, resolveTestTakenFromDate(cycle), cycle.getEndDate());
		return timeString;
	}

	private String resolveConnectedEnrolGrade(long studentId) {
		LocalDate effectiveDate = resolveConnectedEffectiveDate();
		int academicYear = getConnectedAcademicYear(effectiveDate);
		int academicWeek = getConnectedAcademicWeek(effectiveDate);

		if (academicWeek == JaeConstants.FIRST_WEEK) {
			// Week 1: promoted students follow previous-year last-week grade (e.g. S7 Set49 track).
			String previousYearGrade = resolvePreviousYearLastWeekGrade(studentId, academicYear);
			if (StringUtils.isNotBlank(previousYearGrade)) {
				return previousYearGrade;
			}
			// New registration with no previous-year enrolment: use current grade for menu/track.
			return resolveCurrentEnrolGradeForExplanation(studentId);
		}

		// Week-priority: prefer the enrolment that covers the current academic week, and fall back to the
		// latest-registered enrolment only when the current week has no matching enrolment. This keeps the
		// menu/track grade aligned with the current week so a future-term enrolment registered early
		// (e.g. S7 weeks 11-20) does not override the current-week grade (e.g. TT6 weeks 1-10). This also
		// subsumes the previous year-end (last two weeks) special case, which already preferred the
		// week-aware grade. Week 1 is handled separately above.
		String weekGrade = StringUtils.trimToEmpty(
				onlineSessionService.getOnlineSessionGrade(studentId, academicYear, academicWeek));
		if (StringUtils.isNotBlank(weekGrade)) {
			return weekGrade;
		}
		return StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(studentId));
	}

	/**
	 * Week 1 only: grade enrolled at the previous academic year's last week (e.g. week 49).
	 * Used for Test Explanation and week-1 menu track (Mega P1-P6, Revision S7+).
	 */
	private String resolvePreviousYearLastWeekGrade(long studentId, int academicYear) {
		return StringUtils.trimToEmpty(
				onlineSessionService.getOnlineSessionGrade(studentId, academicYear, JaeConstants.FIRST_WEEK));
	}

	private String resolveTestExplanationGradeForWeekOne(long studentId, int academicYear) {
		// Week 1 must stay consistent with the menu track (resolveConnectedEnrolGrade),
		// which prefers the previous academic year's last-week grade. A student who moved
		// up (e.g. P5 -> TT6) keeps the previous-year grade here so the menu band and the
		// explanation content match (Mega P5), instead of jumping to the new TT6 enrolment.
		String previousYearLastWeekGrade = resolvePreviousYearLastWeekGrade(studentId, academicYear);
		if (StringUtils.isNotBlank(previousYearLastWeekGrade)) {
			return previousYearLastWeekGrade;
		}

		// No previous-year enrolment: genuine TT6/TT8 students keep their current grade.
		String currentGrade = resolveCurrentEnrolGradeForExplanation(studentId);
		if (isTtTestExplanationGrade(currentGrade)) {
			return currentGrade;
		}

		// New registration with no previous-year enrolment: no Test Explanation at week 1.
		if (isNewRegistrationWithoutPreviousYearEnrolment(studentId, academicYear)) {
			return "";
		}

		String previousGrade = codeService.getPreviousGrade(currentGrade);
		if (StringUtils.isNotBlank(previousGrade) && !"0".equals(previousGrade)) {
			return previousGrade;
		}
		return currentGrade;
	}

	private boolean isNewRegistrationWithoutPreviousYearEnrolment(long studentId, int academicYear) {
		String currentGrade = resolveCurrentEnrolGradeForExplanation(studentId);
		String previousYearGrade = resolvePreviousYearLastWeekGrade(studentId, academicYear);
		return StringUtils.isNotBlank(currentGrade) && StringUtils.isBlank(previousYearGrade);
	}

	private String resolveCurrentEnrolGradeForExplanation(long studentId) {
		String currentGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(studentId));
		if (StringUtils.isBlank(currentGrade)) {
			Student std = studentService.getStudent(studentId);
			currentGrade = (std != null) ? StringUtils.trimToEmpty(std.getGrade()) : "";
		}
		return currentGrade;
	}

	/**
	 * Resolves the student's current enrolment grade for homework/short-answer content.
	 * At week 1, {@link OnlineSessionService#getOnlineSessionGrade} intentionally looks up
	 * the previous academic year, so fall back to latest/student grade instead.
	 */
	private String resolveConnectedHomeworkGrade(long studentId, int academicYear, int academicWeek) {
		// Non-week-1: prefer the enrolment that covers the current academic week so a future-term enrolment
		// registered early does not override the current-week grade. Week 1 deliberately skips the
		// week-aware lookup because getOnlineSessionGrade then resolves to the previous academic year.
		if (academicWeek != JaeConstants.FIRST_WEEK) {
			String weekGrade = StringUtils.trimToEmpty(
					onlineSessionService.getOnlineSessionGrade(studentId, academicYear, academicWeek));
			if (StringUtils.isNotBlank(weekGrade)) {
				return weekGrade;
			}
		}
		String latestGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(studentId));
		if (StringUtils.isNotBlank(latestGrade)) {
			return latestGrade;
		}
		return resolveCurrentEnrolGradeForExplanation(studentId);
	}

	private boolean isTtTestExplanationGrade(String grade) {
		return isTtGrade(grade);
	}

	/**
	 * TT6/TT8 are standalone selective tracks, not part of the sequential P/S grade
	 * progression. getPreviousGrade(TT8) returns TT6, which is wrong for these students,
	 * so callers must keep the current grade instead of shifting down.
	 */
	private boolean isTtGrade(String grade) {
		if (StringUtils.isBlank(grade)) {
			return false;
		}
		return JaeConstants.TT6_CODE.equals(grade)
				|| JaeConstants.TT8_CODE.equals(grade)
				|| "TT6".equalsIgnoreCase(grade)
				|| "TT8".equalsIgnoreCase(grade);
	}

	/**
	 * Lower bound (from-date) for the "already taken" test check.
	 * At the new academic year's transition weeks (1-2), a carryover test can be taken
	 * onsite at the very end of the previous academic year and then re-opened online in the
	 * new year. Because the onsite record's registerDate falls in the previous cycle, the
	 * current-cycle-only range misses it and the online test is wrongly retakeable.
	 * During weeks 1-2 we widen the from-date back to the previous cycle's start so those
	 * onsite records are detected. Outside that window the current cycle start is used
	 * (unchanged behaviour).
	 */
	private String resolveTestTakenFromDate(CycleDTO currentCycle) {
		if (currentCycle == null) {
			return null;
		}
		int academicWeek = cycleService.academicWeeks();
		if (academicWeek <= JaeConstants.SECOND_WEEK) {
			CycleDTO previousCycle = cycleService.listCycles(cycleService.academicYear() - 1);
			if (previousCycle != null && StringUtils.isNotBlank(previousCycle.getStartDate())) {
				return previousCycle.getStartDate();
			}
		}
		return currentCycle.getStartDate();
	}

	private LocalDate resolveConnectedEffectiveDate() {
		if (StringUtils.isBlank(recordedTestDayOverride)) {
			return LocalDate.now();
		}
		String override = StringUtils.trim(recordedTestDayOverride);
		try {
			return LocalDate.parse(override);
		} catch (Exception ex) {
			return LocalDate.now();
		}
	}

	private int getConnectedAcademicYear(LocalDate date) {
		if (date == null) {
			return cycleService.academicYear();
		}
		String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		int year = cycleService.academicYear(formattedDate);
		return year > 0 ? year : cycleService.academicYear();
	}

	private int getConnectedAcademicWeek(LocalDate date) {
		if (date == null) {
			return cycleService.academicWeeks();
		}
		String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		int week = cycleService.academicWeeks(formattedDate);
		return week > 0 ? week : cycleService.academicWeeks();
	}


}
