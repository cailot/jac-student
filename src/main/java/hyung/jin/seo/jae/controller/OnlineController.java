package hyung.jin.seo.jae.controller;

import java.time.LocalDate;
import java.util.List;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.EnrolmentService;
import hyung.jin.seo.jae.service.OnlineSessionService;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("elearning")
public class OnlineController {

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private OnlineSessionService onlineSessionService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private CodeService codeService;

	// get online course url
	@GetMapping("/getLive/{id}/{year}/{week}")
	@ResponseBody
	public OnlineSessionDTO getOnlineLive(@PathVariable("id") long id, @PathVariable("year") int year, @PathVariable("week") int week) {	
		// 1. get clazzId via Enrolment with parameters - studentId, year, week, online
		Long clazzId = enrolmentService.findClazzId4OnlineSession(id, year, week);
		// 2. get OnlineSession by clazzId, set (week-1)
		OnlineSessionDTO dto = onlineSessionService.findSessionByClazzNWeek(clazzId, week-1);
		// 4. return OnlineSessionDTO
		return dto;
	}

	// get online course url
	@GetMapping("/getRecord/{id}/{year}/{week}/{set}")
	@ResponseBody
	public OnlineSessionDTO getOnlineRecorded(@PathVariable("id") long id, @PathVariable("year") int year, @PathVariable("week") int week, @PathVariable("set") int set) {	
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		// if week is first week of academic year, check student's register date is more than a month.
		////////////////////////////////////////////////////////////////////////////////////////////////
		if(week == 1){
			Student std = studentService.getStudent(id);
			LocalDate regDate = std.getRegisterDate();
			// check if regDate is less than last month compared with today
			LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
			if (regDate.isBefore(oneMonthAgo)) {
				// regDate is more than one month ago which means existing student so show last week of previous grade unless grade = TT8
				// check grade is TT8 or not
				String grade = std.getGrade(); // TT8's code is 12
				// TT8 needs last week on TT8
				if (JaeConstants.TT8_CODE.equals(grade)) {
					// get last week of last year
					int lastWeek = cycleService.lastAcademicWeek(year-1);
					// get OnlineSession by grade, year, week, for example> 2024, 50th week
					OnlineSessionDTO dto = onlineSessionService.getOnlineSessionByGradeNSetNYear(JaeConstants.TT8_CODE, lastWeek, year-1);
					return dto;
				}else{
					// get last week of previous grade
					int lastWeek = cycleService.lastAcademicWeek(year-1);
					// get previous grade
					String previousGrade = codeService.getPreviousGrade(grade);
					// check if previous grade = 0, it means no need to show
					if(JaeConstants.NO_PREVIOUS_GRADE.equals(previousGrade)){
						return new OnlineSessionDTO(); // return empty OnlineSessionDTO to avoid NullPointerException
					}else{
						// get OnlineSession by previous grade, last year, last week, for example> 2024, 50th week
						OnlineSessionDTO dto = onlineSessionService.getOnlineSessionByGradeNSetNYear(previousGrade, lastWeek, year-1);
						return dto;
					}
				}

			} else {
				// regDate is within the last month, it means new student so no need to show 49th week of previous grade
				System.out.println("Registration date is within the last month.");
				return new OnlineSessionDTO(); // return empty OnlineSessionDTO to avoid NullPointerException
			}
		}
		// 1. get clazzId via Enrolment with parameters - studentId, year, week, online
		Long clazzId = enrolmentService.findClazzId4OnlineSession(id, year, week);
		// 2. get OnlineSession by clazzId, set (week-1)
		OnlineSessionDTO dto = onlineSessionService.findSessionByClazzNWeek(clazzId, set);
		// 4. return OnlineSessionDTO
		return dto;
	}
	
}
