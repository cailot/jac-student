package hyung.jin.seo.jae.service.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.dto.StudentAccount;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.StudentAccountService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;
import hyung.jin.seo.jae.repository.EnrolmentRepository;
import hyung.jin.seo.jae.repository.StudentRepository;

@Service
public class StudentAccountServiceImpl implements StudentAccountService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private EnrolmentRepository enrolmentRepository;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	private List<CycleDTO> cycles;

	// when student login, the below method will be executed to save StudentAccount as UserDetails
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		StudentAccount account = null;
		try{
			Object[] result = studentRepository.checkStudentAccount(Long.parseLong(username));
			if(result!=null && result.length > 0){
				Object[] obj = (Object[])result[0];
				account = new StudentAccount(obj);
				// check enrolment is valid if student is active
				int currentYear = getYear();
				int currentWeek = getWeek();
				// check if student is enrolled in any class for current
				List<Long> ids = enrolmentRepository.checkEnrolmentTime(Long.parseLong(username), currentYear, currentWeek);
				if(ids==null || ids.size() == 0){
					// No enrolment
					account.setEnabled(JaeConstants.INACTIVE);
					throw new DisabledException("User enrolment is not valid");
				}
			}
		}catch(Exception e){
			throw new UsernameNotFoundException("User : " + username + " was not found in the database");
		}
		return account;
	}

	@Override
	@Transactional
	public void updatePassword(Long id, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		try{
			studentRepository.updatePassword(id, encodedPassword);
		}catch(Exception e){
			System.out.println("No student found");
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


}
