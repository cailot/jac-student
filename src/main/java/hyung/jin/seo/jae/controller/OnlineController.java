package hyung.jin.seo.jae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.service.EnrolmentService;
import hyung.jin.seo.jae.service.OnlineSessionService;

@Controller
@RequestMapping("elearning")
public class OnlineController {

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private OnlineSessionService onlineSessionService;

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
		// if week is first week of academic year, check student's register date is more than a month.
		// if yes, then get previous grade's last week content.
		

		// 1. get clazzId via Enrolment with parameters - studentId, year, week, online
		Long clazzId = enrolmentService.findClazzId4OnlineSession(id, year, week);
		// 2. get OnlineSession by clazzId, set (week-1)
		OnlineSessionDTO dto = onlineSessionService.findSessionByClazzNWeek(clazzId, set);
		// 4. return OnlineSessionDTO
		return dto;
	}
	
}
