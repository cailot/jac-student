package hyung.jin.seo.jae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.StudentDTO;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.StudentAccountService;

@Controller
@RequestMapping("online")
public class StudentAccountController {

	@Autowired
	private StudentAccountService studentAccountService;
		
	// search student by ID
	@GetMapping("/get/{id}")
	@ResponseBody
	StudentDTO getStudents(@PathVariable Long id) {
		Student std = studentAccountService.getStudent(id);
		if(std==null) return new StudentDTO(); // return empty if not found
		StudentDTO dto = new StudentDTO(std);
		return dto;
	}
	
	// update student password
	@PutMapping("/updatePassword/{id}/{pwd}")
	@ResponseBody
	public void updatePassword(@PathVariable Long id, @PathVariable String pwd) {
		studentAccountService.updatePassword(id, pwd);
	}
	
}
