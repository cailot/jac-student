package hyung.jin.seo.jae.service.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.dto.StudentAccount;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.model.TestSchedule;
import hyung.jin.seo.jae.model.User;
import hyung.jin.seo.jae.repository.AssessmentAnswerRepository;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.EnrolmentService;
import hyung.jin.seo.jae.service.InvoiceService;
import hyung.jin.seo.jae.service.LoginActivityService;
import hyung.jin.seo.jae.service.PaymentService;
import hyung.jin.seo.jae.service.StudentAccountService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;
import hyung.jin.seo.jae.repository.EnrolmentRepository;
import hyung.jin.seo.jae.repository.MaterialRepository;
import hyung.jin.seo.jae.repository.StudentRepository;
import hyung.jin.seo.jae.repository.TestScheduleRepository;
import hyung.jin.seo.jae.repository.UserRepository;

@Service
public class StudentAccountServiceImpl implements StudentAccountService {

    private final AssessmentAnswerRepository assessmentAnswerRepository;

	private static final Logger logger = LoggerFactory.getLogger(StudentAccountServiceImpl.class);

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EnrolmentRepository enrolmentRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private TestScheduleRepository testScheduleRepository;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private LoginActivityService loginActivityService;

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private HttpSession session;

	@Value("${mock.explanation.enabled:false}")
	private boolean mockExplanationEnabled;

	@Value("${mock.vsse.code:${mock.vsss.code:${mock.vss.code:0}}}")
	private long mockVsssCode;

	@Value("${mock.njac.code:0}")
	private long mockNjacCode;

	@Value("${test-day-override:}")
	private String recordedTestDayOverride;

	private List<CycleDTO> cycles;

    public StudentAccountServiceImpl(AssessmentAnswerRepository assessmentAnswerRepository) {
        this.assessmentAnswerRepository = assessmentAnswerRepository;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			logger.debug("Attempting login for username: {}", username);
			boolean isStudent = username.length() == 8;
			
			if (isStudent) { // 1. normal case for student
				logger.debug("Processing student login...");
				long studentId = Long.parseLong(username);
				Object[] result = studentRepository.checkStudentAccount(studentId);
				
				if (result != null && result.length > 0) {
					logger.debug("Found student account in database");
					Object[] obj = (Object[]) result[0];
					StudentAccount account = new StudentAccount(obj);					
					// Check enrolment is valid if student is active
					LocalDate effectiveDate = resolveEffectiveDate();
					int currentYear = getYear(effectiveDate);
					int currentWeek = getWeek(effectiveDate);
					logger.debug("Checking enrollment for Date: {}, Year: {}, Week: {}", effectiveDate, currentYear, currentWeek);


					////////////////////////////////
					// mock test explanation check
					////////////////////////////////
					boolean allowMockLogin = logMockExplanationIfMatched(studentId);
					
				// 2. Check if student is enrolled in any class for current week
				List<Object[]> enrols = enrolmentRepository.checkEnrolmentTime(studentId, currentYear, currentWeek);
				if (currentWeek == JaeConstants.FIRST_WEEK) { // if first week, check last week of last year
					int previousYear = currentYear - 1;
					int lastWeek = cycleService.lastAcademicWeek(previousYear);
					List<Object[]> fallbackEnrols = enrolmentRepository.checkEnrolmentTime(studentId, previousYear, lastWeek);
					if (fallbackEnrols != null && !fallbackEnrols.isEmpty()) {
						enrols = fallbackEnrols;
					}
					logger.debug("Week 1 fallback enrollment check: studentId={}, previousYear={}, lastWeek={}, found={}",
							studentId, previousYear, lastWeek, fallbackEnrols != null && !fallbackEnrols.isEmpty());
				}
				if (enrols == null || enrols.isEmpty()) {
					if (!allowMockLogin) {
						// No enrolment
						logger.debug("No valid enrollment found for student");
						account.setEnabled(JaeConstants.INACTIVE);
						throw new DisabledException("Enrolment is not valid");
					}
					logger.warn("Mock login window applied for studentId: {}", studentId);
				}

				if (enrols != null && !enrols.isEmpty()) {
					logger.debug("Valid enrollment found for student");
					boolean hasPaid = false;
					for(Object[] enrol : enrols){
						String enrolId = enrol[0].toString();
						String enrolGrade = enrol[1].toString();
						// 3. Check payment on enrolment
						if(paymentService.hasPaymentForEnrolment(Long.parseLong(enrolId))){
							hasPaid = true;
							break;	
						}
						// Set the grade in the account authorities
						account.setGrade(enrolGrade);
						logger.debug("Enrolled Id - {} and Grade - {}", enrolId, enrolGrade);
					}
					// if not paid yet, block log-in
					if(!hasPaid){
						logger.debug("No valid payment found for student enrolments");
						logger.error("PAYMENT DEBUG: Throwing DisabledException with message: Payment not completed");
						
						/////////////////////////////////////////////////////////////////////////
						/// 
						///  Temporary enabled for migration
						/// 
						/// ////////////////////////////////////////////////////////////////////
						account.setEnabled(JaeConstants.INACTIVE);
						throw new DisabledException("Payment not completed");
					}
				}

				// Mock login always takes TT8 grade regardless of existing enrolments
				if (allowMockLogin) {
					account.setGrade(JaeConstants.TT8_CODE);
					logger.warn("Mock login grade overridden to TT8 for studentId: {}", studentId);
				}

				String fromWhere = (String) session.getAttribute("referer");
				// keep login entry if user is connected from login page
				if(fromWhere!=null && fromWhere.contains(JaeConstants.CONNECTED_FROM)) {
					logger.debug("Saving login activity for connected student");
					// add login activity
					loginActivityService.saveLoginActivity(Long.parseLong(username));
					// mock test explanation check
					// logMockExplanationIfMatched(Long.parseLong(username));
				}
				logger.debug("Student login successful");
				return account;
				} else {
					logger.debug("Student not found in database");
					throw new UsernameNotFoundException("Student not found");
				}
			} else { // admin case
				logger.debug("Processing admin login...");
				Object[] result = userRepository.checkUserAccount(username);
				if (result != null && result.length > 0) {
					logger.debug("Admin login successful");
					Object[] obj = (Object[]) result[0];
					User account = new User(obj);                
					return account;
				} else {
					logger.debug("Admin not found in database");
					throw new UsernameNotFoundException("Admin not found");
				}
			}
		} catch (DisabledException e) {
			// Re-throw DisabledException as-is so the custom failure handler can detect it
			logger.error("Login disabled for username: {}. Error: {}", username, e.getMessage());
			throw e;
		} catch (UsernameNotFoundException e) {
			// Re-throw UsernameNotFoundException as-is
			logger.error("Login failed for username: {}. Error: {}", username, e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Login failed for username: {}. Error: {}", username, e.getMessage());
			throw new UsernameNotFoundException("User: " + username + " was not found in the database", e);
		}
	}

	@Override
	@Transactional
	public void updatePassword(Long id, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		try{
			studentRepository.updatePassword(id, encodedPassword);
			logger.info("Password updated successfully for student ID: {}", id);
		}catch(Exception e){
			logger.error("Failed to update password for student ID: {}. Error: {}", id, e.getMessage());
		}	
	}

	@Override
	public Student getStudent(Long id) {
		Student std = null;
		try{
			std = studentRepository.findById(id).get();
		}catch(Exception e){
			System.out.println("No student found");
		}
		// studentRepository.findById(id).get();	
		return std;
	}

	// get current year
	private int getYear(){
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		int year = 0;
		for(CycleDTO dto: cycles) {
			String startDate = dto.getStartDate();
			String endDate = dto.getEndDate();
			try {
				if(JaeUtils.checkIfTodayBelongTo(startDate, endDate)) {
					year =  Integer.parseInt(dto.getYear());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return year;
	}

	private int getYear(LocalDate date) {
		if (date == null) {
			return getYear();
		}
		String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return cycleService.academicYear(formattedDate);
	}

	// get current week
	public int getWeek(){
		LocalDate today = LocalDate.now();
		int currentYear = today.getYear();
		int academicYear = getYear();
		int weeks = 0;
		String academicDate = "";
		String vacationStartDate = "";
		String vacationEndDate = "";
		// bring academic start date
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(academicYear))){
				academicDate = dto.getStartDate();
				vacationStartDate = dto.getVacationStartDate();
				vacationEndDate = dto.getVacationEndDate();
				break;
			}
		}
		LocalDate academicStart = LocalDate.parse(academicDate);
		LocalDate vacationStart = LocalDate.parse(vacationStartDate);
		LocalDate vacationEnd = LocalDate.parse(vacationEndDate);
		
		if(currentYear==academicYear) { // from June to December
			// compare today's date with vacation start date
			if(today.isBefore(vacationStart)) { // simply calculate weeks
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, today);
			}else { // set weeks as xmas week
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, vacationStart) - 1;
			}
		}else { // from January to June
			// simply calculate since last year starting date - 3 weeks (xmas holidays)
			// compare today's date with vacation end date
			if(today.isBefore(vacationEnd)) { // until vacation start date
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, vacationStart) - 1;
			}else{
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, today) - 3; // 3 weeks for xmas holidays
			}		
		}
		return (weeks+1); // calculation must start from 1 not 0
	}

	private int getWeek(LocalDate date) {
		if (date == null) {
			return getWeek();
		}
		String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return cycleService.academicWeeks(formattedDate);
	}

	private LocalDate resolveEffectiveDate() {
		if (recordedTestDayOverride == null || recordedTestDayOverride.trim().isEmpty()) {
			return LocalDate.now();
		}
		String override = recordedTestDayOverride.trim();
		try {
			return LocalDate.parse(override);
		} catch (Exception ex) {
			logger.warn("Invalid test-day-override='{}' for login week/year. Falling back to system date.", override);
			return LocalDate.now();
		}
	}

	// Returns the current academic CycleDTO, or null if not found.
	private CycleDTO getCurrentCycle() {
		if (cycles == null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		for (CycleDTO dto : cycles) {
			try {
				if (JaeUtils.checkIfTodayBelongTo(dto.getStartDate(), dto.getEndDate())) {
					return dto;
				}
			} catch (ParseException e) {
				logger.warn("Failed to parse cycle dates: {}", e.getMessage());
			}
		}
		return null;
	}

	// Returns true only when mock-book match exists and today is within temporary login window.
	// Queries Material directly via invoice ID pattern (studentId * 1000 + seq) to also detect
	// book-only invoices that have no Enrolment record (e.g. VSSE purchased without a class).
	private boolean logMockExplanationIfMatched(long studentId) {
		if (!mockExplanationEnabled) {
			return false;
		}
		try {
			CycleDTO currentCycle = getCurrentCycle();
			if (currentCycle == null) {
				logger.warn("No current academic cycle found, skipping mock check for studentId: {}", studentId);
				return false;
			}
			long minInvoiceId = studentId * 1000L;
			long maxInvoiceId = (studentId + 1L) * 1000L;
			String cycleStart = currentCycle.getStartDate();
			String cycleEnd = currentCycle.getEndDate();

			List<Long> bookIds = materialRepository.findBookIdsByStudentInvoice(minInvoiceId, maxInvoiceId, cycleStart, cycleEnd);
			
			boolean hasMockCourse = bookIds.stream()
					.filter(id -> id != null)
					.anyMatch(id -> id == mockVsssCode || id == mockNjacCode);

			if (hasMockCourse) {
				logger.warn("Mock explanation required for studentId: {}", studentId);
				return isWithinMockLoginWindow();
			} else {
				logger.warn("Mock explanation no need for studentId: {}", studentId);
			}
		} catch (Exception e) {
			logger.warn("Mock explanation check failed for studentId: {}", studentId, e);
		}
		return false;
	}

	// Temporary login grace period for mock-book students (inclusive dates).
	private boolean isWithinMockLoginWindow() {
		TestSchedule mockSchedule = testScheduleRepository.findLatestActiveExplanationWindowByGradeAndWeek(JaeConstants.TT8_CODE, JaeConstants.MOCK_TEST_NO);
		if (mockSchedule == null || mockSchedule.getExplanationFromDatetime() == null || mockSchedule.getExplanationToDatetime() == null) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		//System.out.println(mockSchedule);
		return !now.isBefore(mockSchedule.getExplanationFromDatetime()) && !now.isAfter(mockSchedule.getExplanationToDatetime());
	}


}
