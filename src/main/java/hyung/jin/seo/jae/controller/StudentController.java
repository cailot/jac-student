package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

import hyung.jin.seo.jae.dto.StudentDTO;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("student")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	// register new student
	@PostMapping("/register")
	@ResponseBody
	public StudentDTO registerStudent(@RequestBody StudentDTO formData) {
		// 1. create Student without elearning
		Student std = formData.convertToOnlyStudent();
		std = studentService.addStudent(std);
		StudentDTO dto = new StudentDTO(std);
		return dto;
	}

	// search student with keyword - ID, firstName & lastName
	@GetMapping("/search")
	@ResponseBody
	List<StudentDTO> searchStudents(@RequestParam("keyword") String keyword) {
		List<Student> students = studentService.searchStudents(keyword);
		List<StudentDTO> dtos = new ArrayList<StudentDTO>();
		for (Student std : students) {
			StudentDTO dto = new StudentDTO(std);
			dtos.add(dto);
		}
		return dtos;
	}
	
	// search student by ID
	@GetMapping("/get/{id}")
	@ResponseBody
	StudentDTO getStudents(@PathVariable Long id) {
		Student std = studentService.getStudent(id);
		if(std==null) return new StudentDTO(); // return empty if not found
		StudentDTO dto = new StudentDTO(std);
		return dto;
	}
	
	// update existing student
	@PutMapping("/update")
	@ResponseBody
	public StudentDTO updateStudent(@RequestBody StudentDTO formData) {
		Student std = formData.convertToStudent();
		// 1. update Student
		std = studentService.updateStudent(std, std.getId());
		// 2. convert Student to StudentDTO
		StudentDTO dto = new StudentDTO(std);
		return dto;
	}
	
	// de-activate student by Id
	@PutMapping("/inactivate/{id}")
	@ResponseBody
	public void inactivateStudent(@PathVariable Long id) {
		studentService.deactivateStudent(id);
	}
	

	// de-activate student by Id
	@PutMapping("/activate/{id}")
	@ResponseBody
	public StudentDTO activateStudent(@PathVariable Long id) {
		Student std = studentService.activateStudent(id);
		StudentDTO dto = new StudentDTO(std);
		return dto;
	}

	// update student password
	@PutMapping("/updatePassword/{id}/{pwd}")
	@ResponseBody
	public void updatePassword(@PathVariable Long id, @PathVariable String pwd) {
		Long stdId = id;
		String newPwd = pwd;
		Student std = new Student();
		std.setId(stdId);
		std.setPassword(newPwd);
		studentService.updatePassword(std);
		// return result;
	}
	

	// search student list with state, branch, grade, start date or active
	@GetMapping("/list")
	public String listStudents(@RequestParam(value="listState", required=false) String state, @RequestParam(value="listBranch", required=false) String branch, @RequestParam(value="listGrade", required=false) String grade, @RequestParam(value="listYear", required=false) String year, @RequestParam(value="listActive", required=false) String active, Model model) {
		List<StudentDTO> dtos = studentService.listStudents(state, branch, grade, year, active);
		model.addAttribute(JaeConstants.STUDENT_LIST, dtos);
		return "studentListPage";
	}

	// list student list with state, branch, grade
	@GetMapping("/upgrade")
	public String gradeStudents(@RequestParam(value="listState", required=false) String state, @RequestParam(value="listBranch", required=false) String branch, @RequestParam(value="listCurrentGrade", required=false) String grade, Model model) {
		List<StudentDTO> dtos = studentService.showGradeStudents(state, branch, grade);
		if(dtos==null || dtos.isEmpty()){
			model.addAttribute(JaeConstants.UPGRADE_LIST, null);
		}else{
			model.addAttribute(JaeConstants.UPGRADE_LIST, dtos);			
		}
		return "studentGradePage";
	}

	@PostMapping("/updateGrade/{listTo}")
	public String updateGrade(@PathVariable("listTo") String listTo, @RequestBody List<Long> ids, Model model) {
		String grade = listTo;
		studentService.batchUpdateGrade(ids, grade);
		return "studentGradePage";
	}
	

}
