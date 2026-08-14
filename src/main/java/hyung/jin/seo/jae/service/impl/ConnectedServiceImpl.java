package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import hyung.jin.seo.jae.model.TestSchedule;
import hyung.jin.seo.jae.model.TestAnswerItem;
import hyung.jin.seo.jae.repository.ExtraworkProgressRepository;
import hyung.jin.seo.jae.repository.ExtraworkRepository;
import hyung.jin.seo.jae.repository.HomeworkProgressRepository;
import hyung.jin.seo.jae.repository.HomeworkRepository;
import hyung.jin.seo.jae.repository.HomeworkScheduleRepository;
import hyung.jin.seo.jae.repository.PracticeAnswerRepository;
import hyung.jin.seo.jae.repository.PracticeRepository;
import hyung.jin.seo.jae.repository.PracticeScheduleRepository;
import hyung.jin.seo.jae.repository.StudentPracticeRepository;
import hyung.jin.seo.jae.repository.StudentTestRepository;
import hyung.jin.seo.jae.repository.TestAnswerRepository;
import hyung.jin.seo.jae.repository.TestRepository;
import hyung.jin.seo.jae.repository.TestScheduleRepository;
import hyung.jin.seo.jae.service.ConnectedService;

@Service
public class ConnectedServiceImpl implements ConnectedService {
	
	@Autowired
	private HomeworkRepository homeworkRepository;

	@Autowired
	private ExtraworkRepository extraworkRepository;

	@Autowired
	private PracticeRepository practiceRepository;

	@Autowired
	private PracticeAnswerRepository practiceAnswerRepository;

	@Autowired
	private StudentPracticeRepository studentPracticeRepository;

	@Autowired
	private TestRepository testRepository;

	@Autowired
	private TestAnswerRepository testAnswerRepository;

	@Autowired
	private StudentTestRepository studentTestRepository;

	@Autowired
	private PracticeScheduleRepository practiceScheduleRepository;

	@Autowired
	private TestScheduleRepository testScheduleRepository;

	@Autowired
	private HomeworkScheduleRepository homeworkScheduleRepository;

	@Autowired
	private HomeworkProgressRepository homeworkProgressRepository;

	@Autowired
	private ExtraworkProgressRepository extraworkProgressRepository;

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
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
	public List<Practice> allPractices() {
		List<Practice> dtos = new ArrayList<>();
		try{
			dtos = practiceRepository.findAll();
		}catch(Exception e){
			System.out.println("No Practice found");
		}
		return dtos;
	}

	@Override
	public List<Test> allTests() {
		List<Test> dtos = new ArrayList<>();
		try{
			dtos = testRepository.findAll();
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return dtos;
	}

	@Override
	public List<HomeworkSchedule> allHomeworkSchedules() {
		List<HomeworkSchedule> dtos = new ArrayList<>();
		try{
			dtos = homeworkScheduleRepository.findAll();
		}catch(Exception e){
			System.out.println("No Homework Schedule found");
		}
		return dtos;
	}

	@Override
	public List<PracticeSchedule> allPracticeSchedules() {
		List<PracticeSchedule> dtos = new ArrayList<>();
		try{
			dtos = practiceScheduleRepository.findAll();
		}catch(Exception e){
			System.out.println("No Practice Schedule found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public Homework getHomework(Long id) {
		Optional<Homework> work = homeworkRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Override
	@Transactional(readOnly = true)
	public Extrawork getExtrawork(Long id) {
		Optional<Extrawork> work = extraworkRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Override
	@Transactional(readOnly = true)
	public Practice getPractice(Long id) {
		Optional<Practice> work = practiceRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Override
	@Transactional(readOnly = true)
	public PracticeDTO getPracticeInfo(Long id) {
		PracticeDTO dto = practiceRepository.getPracticeById(id);
		return dto;
	}

	@Override
	public PracticeAnswer getPracticeAnswer(Long id) {
		Optional<PracticeAnswer> answer = practiceAnswerRepository.findById(id);
		if(!answer.isPresent()) return null;
		return answer.get();
	}

	@Override
	public StudentPractice getStudentPractice(Long id) {
		Optional<StudentPractice> sp = studentPracticeRepository.findById(id);
		if(!sp.isPresent()) return null;
		return sp.get();
	}

	@Override
	public Test getTest(Long id) {
		Optional<Test> work = testRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Override
	public TestDTO getTestInfo(Long id) {
		TestDTO dto = testRepository.getTestById(id);
		return dto;
	}

	@Override
	public String getTestTypeName(Long id) {
		String name = "";
		try{
			name = testRepository.getTestTypeName(id);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return name;	
	}

	@Override
	public int getTestGroup(Long id) {
		int testGroup = 0;
		try{
			testGroup = testRepository.getTestGroup(id);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return testGroup;	
	}

	@Override
	public int getTestVolume(Long id) {
		int testVolume = 0;
		try{
			testVolume = testRepository.getTestVolume(id);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return testVolume;	
	}

	@Override
	public String getTestGrade(Long id) {
		String testGrade = "";
		try{
			testGrade = testRepository.getTestGrade(id);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return testGrade;	
	}

	@Override
	public TestAnswer getTestAnswer(Long id) {
		Optional<TestAnswer> test = testAnswerRepository.findById(id);
		if(!test.isPresent()) return null;
		return test.get();
	}

	@Override
	public StudentTest getStudentTest(Long id) {
		Optional<StudentTest> test = studentTestRepository.findById(id);
		if(!test.isPresent()) return null;
		return test.get();
	}

	@Override
	public HomeworkSchedule getHomeworkSchedule(Long id) {
		Optional<HomeworkSchedule> schedule = homeworkScheduleRepository.findById(id);
		if(!schedule.isPresent()) return null;
		return schedule.get();
	}

	@Override
	public HomeworkProgress getHomeworkProgress(Long id) {
		Optional<HomeworkProgress> progress = homeworkProgressRepository.findById(id);
		if(!progress.isPresent()) return null;
		return progress.get();
	}

	@Override
	public ExtraworkProgress getExtraworkProgress(Long id) {
		Optional<ExtraworkProgress> progress = extraworkProgressRepository.findById(id);
		if(!progress.isPresent()) return null;
		return progress.get();
	}

	@Override
	public PracticeSchedule getPracticeSchedule(Long id) {
		Optional<PracticeSchedule> test = practiceScheduleRepository.findById(id);
		if(!test.isPresent()) return null;
		return test.get();
	}

	@SuppressAjWarnings("null")
	@Override
	@Transactional
	public StudentPractice addStudentPractice(StudentPractice crs) {
		StudentPractice sp = studentPracticeRepository.save(crs);
		return sp;
	}

	@SuppressAjWarnings("null")
	@Override
	@Transactional
	public StudentTest addStudentTest(StudentTest crs) {
		// set isonline = true
		crs.setOnline(true);
		StudentTest test = studentTestRepository.save(crs);
		return test;
	} 

	@SuppressAjWarnings("null")
	@Override
	@Transactional
	public HomeworkProgress addHomeworkProgress(HomeworkProgress progress) {
		HomeworkProgress home = homeworkProgressRepository.save(progress);
		return home;
	}

	@SuppressAjWarnings("null")
	@Override
	@Transactional
	public ExtraworkProgress addExtraworkProgress(ExtraworkProgress progress) {
		ExtraworkProgress extra = extraworkProgressRepository.save(progress);
		return extra;
	}

	@Override
	@Transactional
	public HomeworkProgress updateHomeworkProgressPercentage(Long id, int percentage) {
		HomeworkProgress existing = homeworkProgressRepository.findById(id).get();
		// update percentage
		existing.setPercentage(percentage);
		// update registerDate
		existing.setRegisterDate(LocalDate.now());
		// update the existing record
		HomeworkProgress updated = homeworkProgressRepository.save(existing);
		return updated;
	}

	@Override
	@Transactional
	public ExtraworkProgress updateExtraworkProgressPercentage(Long id, int percentage) {
		ExtraworkProgress existing = extraworkProgressRepository.findById(id).get();
		// update percentage
		existing.setPercentage(percentage);
		// update registerDate
		existing.setRegisterDate(LocalDate.now());
		// update the existing record
		ExtraworkProgress updated = extraworkProgressRepository.save(existing);
		return updated;
	}

	@Override
	@Transactional
	public void deleteStudentPractice(Long studentId, Long practiceId) {
		try{
			studentPracticeRepository.deleteByStudentIdAndPracticeId(studentId, practiceId);
		}catch(Exception e){
			System.out.println("No StudentPractice found");
		}
	}

	@Override
	public HomeworkDTO getHomeworkInfo(long subject, int week) {
		HomeworkDTO dto = null;
		try{
			dto = homeworkRepository.findHomework(subject, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dto;
	}

	@Override
	public ExtraworkDTO getExtraworkInfo(int subject, int year, int week) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getExtraworkkInfo'");
	}

	@Override
	public PracticeDTO getPracticeInfo(int type, String grade, int volume) {
		PracticeDTO dto = null;
		try{
			dto = practiceRepository.findPractice(type, grade, volume);
		}catch(Exception e){
			System.out.println("No Practice found");
		}
		return dto;
	}

	@Override
	public TestDTO getTestInfo(int type, String grade, int volume) {
		TestDTO dto = null;
		try{
			dto = testRepository.findTest(type, grade, volume);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return dto;
	}

	@Override
	public List<HomeworkDTO> listHomework(long subject, String grade, int week) {
		List<HomeworkDTO> dtos = new ArrayList<>();
		try{
			dtos = homeworkRepository.filterHomeworkBySubjectNGradeNWeek(subject, grade, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return dtos;
	}

	@Override
	public long getHomeworkIdByWeek(long subject, String grade, int week){
		long id = 0L;
		try{
			id = homeworkRepository.findHomeworkIdBySubjectNGradeNWeek(subject, grade, week);
		}catch(Exception e){
			System.out.println("No Homework found");
		}
		return id;
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
	public List<PracticeDTO> listPractice(int type, String grade, int volume) {
		List<PracticeDTO> dtos = new ArrayList<>();
		try{
			dtos = practiceRepository.filterPracticeByTypeNGradeNVolume(type, grade, volume);
		}catch(Exception e){
			System.out.println("No Practice found");
		}
		return dtos;
	}

	@Override
	public List<TestDTO> listTest(int type, String grade, int volume) {
		List<TestDTO> dtos = new ArrayList<>();
		try{
			dtos = testRepository.filterTestByTypeNGradeNVolume(type, grade, volume);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return dtos;
	}

	@Override
	public List<HomeworkScheduleDTO> listHomeworkSchedule(LocalDateTime from, LocalDateTime to) {
		List<HomeworkScheduleDTO> dtos = new ArrayList<>();
		try {
			String fromStr = from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String toStr = to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			dtos = homeworkScheduleRepository.filterHomeworkScheduleByYear(fromStr, toStr);
		} catch (Exception e) {
			System.out.println("No Homework Schedule found - Exception: " + e.getMessage());
		}
		return dtos;
	}

	// @Override
	// public List<PracticeScheduleDTO> listPracticeSchedule(int year, int week) {
	// 	List<PracticeScheduleDTO> dtos = new ArrayList<>();
	// 	try{
	// 		dtos = practiceScheduleRepository.filterPracticeScheduleByYearNWeek(year, week);
	// 	}catch(Exception e){
	// 		System.out.println("No Practice Schedule found");
	// 	}
	// 	return dtos;
	// }

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

	@Override
	public List<SimpleBasketDTO> loadPractice(int type, int grade) {
		List<Object[]> objects = new ArrayList<>();
		try{
			objects = practiceRepository.summaryPractice(type, grade);
		}catch(Exception e){
			System.out.println("No Practice found");
		}
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		for(Object[] object : objects){
			SimpleBasketDTO dto = new SimpleBasketDTO(object);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<SimpleBasketDTO> loadTest(int type, int grade) {
		List<Object[]> objects = new ArrayList<>();
		try{
			objects = testRepository.summaryTest(type, grade);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		for(Object[] object : objects){
			SimpleBasketDTO dto = new SimpleBasketDTO(object);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<Integer> getAnswersByPractice(Long practiceId) {
		Optional<PracticeAnswer> answer = practiceAnswerRepository.findByPracticeId(practiceId);
		if(answer.isPresent()){
			return answer.get().getAnswers();
		}else{
			return null;
		}
	}

	@Override
	public List<Integer> getStudentPracticeAnswer(Long studentId, Long practionId) {
		Optional<StudentPractice> sp = studentPracticeRepository.findTopByStudentIdAndPracticeIdOrderByRegisterDateDesc(studentId, practionId);
		if(sp.isPresent()){
			return sp.get().getAnswers();
		}else{
			return null;
		}
	}

	@Override
	public List<TestAnswerItem> getAnswersByTest(Long testId) {
		Optional<TestAnswer> answer = testAnswerRepository.findByTestId(testId);
		if(answer.isPresent()){
			return answer.get().getAnswers();
		}else{
			return null;
		}
	}

	@Override
	public List<Integer> getStudentTestAnswer(Long studentId, Long testId, String from, String to) {
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);				
		Optional<StudentTest> answer = studentTestRepository.findByStudentIdAndTestIdWithYear(studentId, testId, fromDate, toDate);
		if(answer.isPresent()){
			return answer.get().getAnswers();
		}else{
			return null;
		}
	}

	@Override
	public int getPracticeAnswerCount(Long practiceId) {
		Optional<PracticeAnswer> answer = practiceAnswerRepository.findByPracticeId(practiceId);
		if(answer.isPresent()){
			List<Integer> answers =  answer.get().getAnswers();
			if((answers != null) && (answers.size()>0)) return answers.get(0);
		}
		return 0;
	}

	@Override
	public int getPracticeAnswerCountPerQuestion(Long practiceId) {
		int count = 0;
		try{
			count = practiceAnswerRepository.getAnswerCountByPractice(practiceId);
		}catch(Exception e){
			System.out.println("No PracticeAnswer found");
		}
		return count;
	}

	@Override
	public int getTestAnswerCount(Long testId) {
		Optional<TestAnswer> answer = testAnswerRepository.findByTestId(testId);
		if(answer.isPresent()){
			List<TestAnswerItem> answers =  answer.get().getAnswers();
			if((answers != null) && (answers.size()>0)) return answers.size();
		}
		return 0;
	}

	@Override
	public int getTestAnswerCountPerQuestion(Long testId) {
		int count = 0;
		try{
			count = testAnswerRepository.getAnswerCountByTest(testId);
		}catch(Exception e){
			System.out.println("No TestAnswer found");
		}
		return count;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isStudentPracticeExist(Long studentId, Long practiceId) {
		Optional<StudentPractice> sp = studentPracticeRepository.findTopByStudentIdAndPracticeIdOrderByRegisterDateDesc(studentId, practiceId);
		if(sp.isPresent()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isStudentTestExist(Long studentId, Long testId, String from, String to) {
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);				
		Optional<StudentTest> st = studentTestRepository.findByStudentIdAndTestIdWithYear(studentId, testId, fromDate, toDate);
		if(st.isPresent()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public PracticeAnswerDTO findPracticeAnswerByPractice(Long id) {
		PracticeAnswerDTO dto = null;
		try{
			PracticeAnswer answer = practiceAnswerRepository.findPracticeAnswerByPractice(id);
			if(answer!=null){
				dto = new PracticeAnswerDTO(answer);
			}
		}catch(Exception e){
			System.out.println("No PracticeAnswer found");
		}
		return dto;
	}

	@Override
	public StudentPracticeDTO findStudentPracticeByStudentNPractice(Long studentId, Long practiceId) {
		StudentPracticeDTO dto = null;
		try{
			dto = studentPracticeRepository.findStudentPractice(studentId, practiceId);
		}catch(Exception e){
			System.out.println("No StudentPractice found");
		}
		return dto;	
	}

	@Override
	public TestAnswerDTO findTestAnswerByTest(Long id) {
		TestAnswerDTO dto = null;
		try{
			TestAnswer answer = testAnswerRepository.findTestAnswerByTest(id);
			if(answer!=null){
				dto = new TestAnswerDTO(answer);
			}
		}catch(Exception e){
			System.out.println("No TestAnswer found");
		}
		return dto;
	}

	@Override
	public StudentTestDTO findStudentTestByStudentNTest(Long studentId, Long testId, String from, String to) {
		StudentTestDTO dto = null;
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);			
		try{
			dto =  studentTestRepository.findStudentTest(studentId, testId, fromDate, toDate);
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return dto;
	}

	@Override
	public StudentTestDTO getStudentTest(Long studentId, Long testTypeId, String grade, int volume, String from, String to) {
		StudentTestDTO dto = null;
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);			
		try{
			dto =  studentTestRepository.findStudentTestResult(studentId, testTypeId, grade, volume, fromDate, toDate);
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return dto;
	}

	@Override
	public String getRegDateforStudentTest(Long studentId, Long test, String from, String to) {
		String regString = "";
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);
		try{
			LocalDate regDate = studentTestRepository.getRegisterDateByStudentIdAndTestId(studentId, test, fromDate, toDate);
			regString = regDate.toString();
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return regString;
	}

	@Override
	public HomeworkScheduleDTO getHomeworkScheduleBySubjectAndGrade(String subject, String grade, LocalDateTime now) {
		String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		List<HomeworkScheduleDTO> result = homeworkScheduleRepository.getHomeworkScheduleBySubjectAndGrade(subject, grade, nowStr, PageRequest.of(0, 1));
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public HomeworkProgress getHomeworkProgressByStudentNHomework(Long studentId, Long homeworkId) {
		HomeworkProgress progress = null;
		try{
			progress =  homeworkProgressRepository.findHomeworkProgressByStudentAndHomework(studentId, homeworkId);
		}catch(Exception e){
			System.out.println("No Homework Progress found");
		}
		return progress;
	}

	@Override
	public ExtraworkProgress getExtraworkProgressByStudentNHomework(Long studentId, Long extraworkId) {
		ExtraworkProgress progress = null;
		try{
			progress =  extraworkProgressRepository.findExtraworkProgressByStudentAndHomework(studentId, extraworkId);
		}catch(Exception e){
			System.out.println("No Extrawork Progress found");
		}
		return progress;
	}

	@Override
	public int getHomeworkProgressPercentage(Long studentId, Long homeworkId) {
		int percentage = 0;
		try{
			percentage = homeworkProgressRepository.getPercentageByStudentAndHomework(studentId, homeworkId);
		}catch(Exception e){
			System.out.println("No Homework Progress found");
		}
		return percentage;
	}

	@Override
	public int getExtraworkProgressPercentage(Long studentId, Long extraworkId) {
		int percentage = 0;
		try{
			percentage = extraworkProgressRepository.getPercentageByStudentAndHomework(studentId, extraworkId);
		}catch(Exception e){
			System.out.println("No Extrawork Progress found");
		}
		return percentage;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PracticeScheduleDTO> checkPracticeSchedule(String practiceGroup, String grade, LocalDateTime now) {
		List<PracticeScheduleDTO> dtos = new ArrayList<>();
		try {
			String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			dtos = practiceScheduleRepository.getPracticeScheduleByGroupAndGrade(practiceGroup, grade, nowStr);
		} catch (Exception e) {
			System.out.println("No Practice Schedule found - Exception: " + e.getMessage());
		}
		return dtos;
	}

	@Override
	public List<TestScheduleDTO> checkTestSchedule(String testGroup, String grade, LocalDateTime now) {
		List<TestScheduleDTO> dtos = new ArrayList<>();
		try {
			String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			if (testGroup.contains(",")) {
				String[] testGroups = testGroup.split(",");
				for (String singleTestGroup : testGroups) {
					List<TestScheduleDTO> singleGroupDtos = testScheduleRepository.getTestScheduleByGroupAndGrade(singleTestGroup.trim(), grade, nowStr);
					dtos.addAll(singleGroupDtos);
				}
			} else {
				dtos = testScheduleRepository.getTestScheduleByGroupAndGrade(testGroup, grade, nowStr);
			}
		} catch (Exception e) {
			System.out.println("No Test Schedule found - Exception: " + e.getMessage());
		}
		return dtos;
	}

	@Override
	public List<TestScheduleDTO> checkTestSchedule4Explanation(String testGroup, String grade, LocalDateTime now) {
		List<TestScheduleDTO> dtos = new ArrayList<>();
		try {
			// Convert LocalDateTime to MySQL DATETIME format string for query
			String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			// Use native query to avoid JPQL LocalDateTime binding issues
			dtos = testScheduleRepository.getTestSchedule4Explanation(testGroup, grade, nowStr);
		} catch (Exception e) {
			System.out.println("No Test Schedule found - Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return dtos;
	}

	@Override
	public TestScheduleDTO getMostRecentTestSchedule(String testGroup, String grade, String week) {
		TestScheduleDTO dto = new TestScheduleDTO();
		try{
			dto = testScheduleRepository.getTestScheduleByGroupNGradeNWeekTop1(testGroup, grade, week);
		}catch(Exception e){
			System.out.println("No Test Schedule found");
		}
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PracticeDTO> getPracticeInfoByGroup(int practiceGroup, String grade, int week) {
		List<PracticeDTO> dtos = new ArrayList<>();
		try{
			dtos = practiceRepository.getPracticeByGroupNGradeNWeek(practiceGroup, grade, week);
		}catch(Exception e){
			System.out.println("No Practice found");
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TestDTO> getTestInfoByGroup(int testGroup, String grade, int week) {
		List<TestDTO> dtos = new ArrayList<>();
		try{
			dtos = testRepository.getTestByGroupNGradeNWeek(testGroup, grade, week);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return dtos;
	}

	@Override
	public double getAverageScoreByTest(Long testId, String from, String to) {
		double value = 0;
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);
		try{
			value = studentTestRepository.getAverageScoreByTestId(testId, fromDate, toDate);
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return value;
	}

	@Override
	public double getHighestScoreByTest(Long testId, String from, String to) {
		double value = 0;
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);
		try{
			value = studentTestRepository.getHighestScoreByTestId(testId, fromDate, toDate);
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return value;
	}

	@Override
	public double getLowestScoreByTest(Long testId, String from, String to) {
		double value = 0;
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);
		try{
			value = studentTestRepository.getLowestScoreByTestId(testId, fromDate, toDate);
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return value;
	}

	@Override
	public String getScoreCategory(double studentScore, Long testId, String from, String to) {
		List<Double> scores = new ArrayList<>();
		// convert String to LocalDate
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);
		try{
			scores = studentTestRepository.getAllScoreByTestId(testId, fromDate, toDate);	
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		if (scores.isEmpty()) {
			return "No";
		}
		int totalStudents = scores.size();
		
		int top10Index = (int) Math.ceil(totalStudents * 0.10) - 1;
		int top21Index = (int) Math.ceil(totalStudents * 0.21) - 1;
		int top39Index = (int) Math.ceil(totalStudents * 0.39) - 1;
		int top60Index = (int) Math.ceil(totalStudents * 0.60) - 1;
		int top79Index = (int) Math.ceil(totalStudents * 0.79) - 1;
	
		double top10Score = scores.get(top10Index);
		double top21Score = scores.get(top21Index);
		double top39Score = scores.get(top39Index);
		double top60Score = scores.get(top60Index);
		double top79Score = scores.get(top79Index);
	
		if (studentScore >= top10Score) {
			return "Top";
		} else if (studentScore >= top21Score) {
			return "Above";
		} else if (studentScore >= top39Score) {
			return "Higher";
		} else if (studentScore >= top60Score) {
			return "Middle";
		} else if (studentScore >= top79Score) {
			return "Lower";
		} else {
			return "Lowest";
		}
	}

	@Override
	public List<TestDTO> getTestInfoByType(Long testId, String from, String to) {
		List<TestDTO> dtos = new ArrayList<>();
		LocalDate fromDate = LocalDate.parse(from, DATE_FORMATTER);
		LocalDate toDate = LocalDate.parse(to, DATE_FORMATTER);
		try{
			dtos = testRepository.getTestByType(testId, fromDate, toDate);
		}catch(Exception e){
			System.out.println("No Test found");
		}
		return dtos;
	}

	@Override
	public List<StudentTestDTO> getLatestStudentTest(Long studentId) {
		List<StudentTestDTO> dtos = new ArrayList<>();
		try{
			dtos = studentTestRepository.getStudentTestsByStudentIdWithMostRecentRegisterDate(studentId);
		}catch(Exception e){
			System.out.println("No StudentTest found");
		}
		return dtos;
	}

}
