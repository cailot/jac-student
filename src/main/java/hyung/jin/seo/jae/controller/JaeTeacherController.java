package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.dto.TeacherDTO;
import hyung.jin.seo.jae.model.Clazz;
import hyung.jin.seo.jae.model.Teacher;
import hyung.jin.seo.jae.service.ClazzService;
import hyung.jin.seo.jae.service.TeacherService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("teacher")
public class JaeTeacherController {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private ClazzService clazzService;

	// register new teacher
	@PostMapping("/register")
	@ResponseBody
	public TeacherDTO registerTeacher(@RequestBody TeacherDTO formData) {
		Teacher teacher = formData.convertToTeacher();
		teacher = teacherService.addTeacher(teacher);
		TeacherDTO dto = new TeacherDTO(teacher);
		return dto;
	}

	// // search teacher with keyword - ID, firstName & lastName
	// @GetMapping("/search")
	// @ResponseBody
	// List<TeacherDTO> searchTeachers(@RequestParam("keyword") String keyword) {
	// 	List<Teacher> teachers = teacherService.searchTeachers(keyword);
	// 	List<TeacherDTO> dtos = new ArrayList<TeacherDTO>();
	// 	for (Teacher teacher : teachers) {
	// 		TeacherDTO dto = new TeacherDTO(teacher);
	// 		dtos.add(dto);
	// 	}
	// 	return dtos;
	// }

	// search teacher by ID
	@GetMapping("/get/{id}")
	@ResponseBody
	TeacherDTO getTeachers(@PathVariable Long id) {
		Teacher teacher = teacherService.getTeacher(id);
		if (teacher == null)
			return new TeacherDTO(); // return empty if not found
		TeacherDTO dto = new TeacherDTO(teacher);
		return dto;
	}

	// update existing teacher
	@PutMapping("/update")
	@ResponseBody
	public TeacherDTO updateTeacher(@RequestBody TeacherDTO formData) {
		Teacher teacher = formData.convertToTeacher();
		// 1. update Teacher
		teacher = teacherService.updateTeacher(teacher, teacher.getId());
		// 2. convert Teacher to TeacherDTO
		TeacherDTO dto = new TeacherDTO(teacher);
		return dto;
	}

	// activate teacher by Id
	@PutMapping("/activate/{id}")
	@ResponseBody
	public void activateTeacher(@PathVariable Long id) {
		teacherService.reactivateTeacher(id);
	}

	// de-activate teacher by Id
	@PutMapping("/inactivate/{id}")
	@ResponseBody
	public void inactivateTeacher(@PathVariable Long id) {
		teacherService.dischargeTeacher(id);
	}

	// search student list with state, branch or active
	@GetMapping("/list")
	public String listTeachers(@RequestParam(value = "listState", required = false) String state,
			@RequestParam(value = "listBranch", required = false) String branch,
			Model model) {
		List<Teacher> teachers = teacherService.listTeachers(state, branch);
		List<TeacherDTO> dtos = new ArrayList<TeacherDTO>();
		for (Teacher teacher : teachers) {
			TeacherDTO dto = new TeacherDTO(teacher);
			dtos.add(dto);
		}
		model.addAttribute(JaeConstants.TEACHER_LIST, dtos);
		return "teacherListPage";
	}

	// retrieve clazz by Id
	@GetMapping("/getClazz/{teacherId}")
	@ResponseBody
	public List<ClazzDTO> getClazz(@PathVariable("teacherId") Long teacherId) {
		List<Long> clazzIds = new ArrayList<>();
		List<ClazzDTO> clazzes = new ArrayList<>();
		try {
			clazzIds = teacherService.getClazzIdByTeacher(teacherId);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		for (Long clazzId : clazzIds) {
			ClazzDTO dto = new ClazzDTO(clazzService.getClazz(clazzId));
			clazzes.add(dto);
		}
		return clazzes;
	}

	// add assoicated clazz by Id
	@PutMapping("/addClazz/{teacherId}/{clazzId}")
	@ResponseBody
	public ClazzDTO addClazz(@PathVariable("teacherId") Long teacherId,
			@PathVariable("clazzId") Long clazzId) {

		// 1. get teacher
		Teacher teacher = teacherService.getTeacher(teacherId);
		// 2. get associated clazz
		Set<Clazz> clazzs = teacher.getClazzs();
		Clazz addClazz = clazzService.getClazz(clazzId);
		// 3. add clazz
		clazzs.add(addClazz);
		// 4. update teacher's clazz
		teacherService.updateTeacher(teacher, teacherId);
		// 5. return success message
		return new ClazzDTO(addClazz);
	}

	// remove assoicated clazz by Id
	@PutMapping("/updateClazz/{teacherId}/{clazzId}")
	@ResponseBody
	public ResponseEntity<String> removeClazz(@PathVariable("teacherId") Long teacherId,
			@PathVariable("clazzId") Long clazzId) {

		// 1. get teacher
		Teacher teacher = teacherService.getTeacher(teacherId);
		// 2. get associated clazz
		Set<Clazz> clazzs = teacher.getClazzs();
		for (Clazz clazz : clazzs) {
			// 3. remove clazz
			if (clazz.getId() == clazzId) {
				clazzs.remove(clazz);
				break;
			}
		}
		// 4. update teacher's clazz
		teacherService.updateTeacher(teacher, teacherId);
		// 5. return success message
		return ResponseEntity.ok("success");
	}
}
