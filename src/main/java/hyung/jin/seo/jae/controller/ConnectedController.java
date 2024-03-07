package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.model.Extrawork;
import hyung.jin.seo.jae.model.Grade;
import hyung.jin.seo.jae.model.Homework;
import hyung.jin.seo.jae.model.Subject;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.ConnectedService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("connected")
public class ConnectedController {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectedController.class);

	@Autowired
	private ConnectedService connectedService;

	@Autowired
	private CodeService codeService;
	
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


	// search homework by subject, year & week
	@GetMapping("/homework/{subject}/{year}/{week}")
	@ResponseBody
	public HomeworkDTO searchHomework(@PathVariable int subject, @PathVariable int year, @PathVariable int week) {
		HomeworkDTO dto = connectedService.getHomeworkInfo(subject, year, week);
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
	@GetMapping("/filterExtrawork/{grade}")
	@ResponseBody
	public List<ExtraworkDTO> listExtraworks(@PathVariable String grade) {
		List<ExtraworkDTO> dtos = new ArrayList();
		String filteredGrade = StringUtils.defaultString(grade, JaeConstants.ALL);
		dtos = connectedService.listExtrawork(filteredGrade);		
		return dtos;
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
	
}
