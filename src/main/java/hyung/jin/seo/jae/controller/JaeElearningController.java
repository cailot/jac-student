package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.ElearningDTO;
import hyung.jin.seo.jae.model.Elearning;
import hyung.jin.seo.jae.service.ElearningService;

@Controller
@RequestMapping("elearning")
public class JaeElearningController {

	private static final Logger LOG = LoggerFactory.getLogger(JaeElearningController.class);

	@Autowired
	private ElearningService elearningService;

	
	// register new student
	@PostMapping("/register")
	@ResponseBody
	public ElearningDTO registerStudent(@RequestBody ElearningDTO formData) {
		Elearning crs = formData.convertToElearning();
		crs = elearningService.addElearning(crs);
		ElearningDTO dto = new ElearningDTO(crs);
		return dto;
	}


	// search elearning with grade
	@GetMapping("/grade")
	@ResponseBody
	List<ElearningDTO> gradeCourses(@RequestParam("grade") String keyword) {
		List<Elearning> crss = elearningService.gradeElearnings(keyword);
		List<ElearningDTO> dtos = new ArrayList<ElearningDTO>();
		for (Elearning crs : crss) {
			ElearningDTO dto = new ElearningDTO(crs);
			dtos.add(dto);
		}
		return dtos;
	}

	
	// update existing course
	@PutMapping("/update")
	@ResponseBody
	public ElearningDTO updateStudent(@RequestBody ElearningDTO formData) {
		Elearning crs = formData.convertToElearning();
		crs = elearningService.updateElearning(crs, crs.getId());
		ElearningDTO dto = new ElearningDTO(crs);
		return dto;
	}
	
	
	// list all courses
	@GetMapping("/list")
	@ResponseBody
	List<ElearningDTO> allCourses() {
		List<Elearning> crss = elearningService.allElearnings();
		List<ElearningDTO> dtos = new ArrayList<ElearningDTO>();
		for(Elearning crs : crss) {
			ElearningDTO dto = new ElearningDTO(crs);	
			dtos.add(dto);
		}
        return dtos;
	}

	// search e-learning by student Id
	@GetMapping("/search/student/{id}")
	@ResponseBody
	List<ElearningDTO> searchElearningByStudent(@PathVariable Long id) {
		List<Elearning> crss = elearningService.studentElearnings(id);
		List<ElearningDTO> dtos = new ArrayList<ElearningDTO>();
		for(Elearning crs : crss) {
			ElearningDTO dto = new ElearningDTO(crs);	
			dtos.add(dto);
		}
        return dtos;
	}
	
}
