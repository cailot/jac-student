package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;
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

import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.dto.CourseDTO;
import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.model.Clazz;
import hyung.jin.seo.jae.model.Course;
import hyung.jin.seo.jae.model.Cycle;
import hyung.jin.seo.jae.service.ClazzService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.CourseService;
import hyung.jin.seo.jae.utils.JaeConstants;
import io.micrometer.core.instrument.util.StringUtils;

@Controller
@RequestMapping("class")
public class JaeClazzController {

	@Autowired
	private ClazzService clazzService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private CourseService courseService;

	// search classes by grade & year
	@GetMapping("/search")
	@ResponseBody
	List<ClazzDTO> searchClasses(@RequestParam("grade") String grade) {
		int year = cycleService.academicYear();
		List<ClazzDTO> dtos = clazzService.findClazzForGradeNCycle(grade, year);
		return dtos;
	}

	// check current academic year and week
	@GetMapping("/academy")
	@ResponseBody
	String[] getAcademicInfo() {
		int year = cycleService.academicYear();
		int week = cycleService.academicWeeks();
		return new String[] { String.valueOf(year), String.valueOf(week) };
	}

	// count records number in database
	@GetMapping("/count")
	@ResponseBody
	long coutEtc() {
		long count = clazzService.checkCount();
		return count;
	}

	// get online course Id
	@GetMapping("/id")
	@ResponseBody
	Long getId(@RequestParam("grade") String grade, @RequestParam("year") int year) {
		Long id = clazzService.getOnlineId(grade, year);
		return id;
	}

	// bring all courses in database
	@GetMapping("/listClass")
	public String listClasses(@RequestParam(value = "listState", required = false) String state,
			@RequestParam(value = "listBranch", required = false) String branch,
			@RequestParam(value = "listGrade", required = false) String grade,
			@RequestParam(value = "listYear", required = false) String year,
			@RequestParam(value = "listActive", required = false) String active, Model model) {
		// System.out.println(state + "\t" + branch + "\t" + grade + "\t" + year + "\t" + active + "\t");
		List<ClazzDTO> dtos = clazzService.listClazz(state, branch, grade, year, active);
		model.addAttribute(JaeConstants.CLASS_LIST, dtos);
		return "classListPage";
	}

	// bring all classes in database
	@GetMapping("/listCycle")
	public String listCycle(@RequestParam(value = "listYear", required = true) String year, Model model) {
		List<CycleDTO> dtos = null;
		if(StringUtils.isNotEmpty(year) && !(JaeConstants.ALL.equalsIgnoreCase(year))) {
			int yearParam = Integer.parseInt(year);
			dtos = cycleService.listCycles(yearParam);
		}else{
			// if condition is 'All', bring all
			dtos = cycleService.allCycles();
		}
		model.addAttribute(JaeConstants.CYCLE_LIST, dtos);
		return "cyclePage";
	}

	// bring all classes in database
	@GetMapping("/listCourse")
	public String listCourses(@RequestParam(value = "listGrade", required = false) String grade, Model model) {
		List<CourseDTO> dtos = null;
		// if grade has some value
		if ((StringUtils.isNotBlank(grade)) && !(JaeConstants.ALL.equalsIgnoreCase(grade))) {
			dtos = courseService.findByGrade(grade);
		} else { // if grade has no value, simply bring all
			dtos = courseService.allCourses();
		}
		model.addAttribute(JaeConstants.COURSE_LIST, dtos);
		return "courseListPage";
	}

	// bring all courses based on grade
	@GetMapping("/coursesByGrade")
	@ResponseBody
	public List<CourseDTO> getCoursesByGrade(@RequestParam(value = "grade", required = true) String grade) {
		int year = cycleService.academicYear();
		int week = cycleService.academicWeeks();
		List<CourseDTO> dtos = courseService.findByGrade(grade);
		// set year
		for (CourseDTO dto : dtos) {
			dto.setYear(year);
		}
		// if new academic year is going to start, display next year classes
		if (week > JaeConstants.ACADEMIC_START_COMMING_WEEKS) {
			List<CourseDTO> nexts = new ArrayList<>();
			// display next year courses by increasing price
			for (CourseDTO dto : dtos) {
				CourseDTO next = dto.clone();
				next.setYear(year + 1);
				next.setPrice(next.getPrice() + JaeConstants.ACADEMIC_NEXT_YEAR_COURSE_PRICE_INCREASE);
				next.setDescription(next.getDescription() + JaeConstants.ACADEMIC_NEXT_YEAR_COURSE_SUFFIX);
				nexts.add(next);
			}
			// add next year courses to the end of the list
			if (nexts.size() > 0) {
				dtos.addAll(nexts);
			}
		}
		return dtos;
	}

	// bring all courses based on grade
	@GetMapping("/listCoursesByGrade")
	@ResponseBody
	public List<CourseDTO> listCoursesByGrade(@RequestParam(value = "grade", required = true) String grade) {
		int year = cycleService.academicYear();
		// int week = cycleService.academicWeeks();
		List<CourseDTO> dtos = courseService.findByGrade(grade);
		// set year
		for (CourseDTO dto : dtos) {
			dto.setYear(year);
		}
		return dtos;
	}

	// search classes by id & year & state & branch
	@GetMapping("/classesByCourse")
	@ResponseBody
	List<ClazzDTO> getClassesByGrade(@RequestParam("courseId") Long courseId, @RequestParam("year") int year,
			@RequestParam("state") String state, @RequestParam("branch") String branch) {
		// List<ClazzDTO> dtos = clazzService.findClazzForCourseIdNCycle(courseId,
		// year);
		List<ClazzDTO> dtos = clazzService.findClazzForCourseIdNCycleNStateNBranch(courseId, year, state, branch);
		return dtos;
	}

	// get class by Id
	@GetMapping("/get/class/{id}")
	@ResponseBody
	public ClazzDTO getClass(@PathVariable("id") Long id) {
		Clazz clazz = clazzService.getClazz(id);
		ClazzDTO dto = new ClazzDTO(clazz);
		return dto;
	}

	// get course by Id
	@GetMapping("/get/course/{id}")
	@ResponseBody
	public CourseDTO getCourse(@PathVariable("id") Long id) {
		Course course = courseService.getCourse(id);
		CourseDTO dto = new CourseDTO(course);
		return dto;
	}

	// get cycle by Id
	@GetMapping("/get/cycle/{id}")
	@ResponseBody
	public CycleDTO getCycle(@PathVariable("id") Long id) {
		Cycle cycle = cycleService.getCycle(id);
		CycleDTO dto = new CycleDTO(cycle);
		return dto;
	}

	// register new course
	@PostMapping("/registerCourse")
	@ResponseBody
	public ResponseEntity<String> registerCourse(@RequestBody CourseDTO formData) {
		// System.out.println(formData);
		try {
			// 1. create Course
			Course course = formData.convertToCourse();
			// 2. save Class
			courseService.addCourse(course);
			// 3. return success;
			return ResponseEntity.ok("\"Course register success\"");
		} catch (Exception e) {
			String message = "Error registering Course: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// register new class
	@PostMapping("/registerClass")
	@ResponseBody
	public ResponseEntity<String> registerClass(@RequestBody ClazzDTO formData) {
		try {
			// 1. create bare Class
			Clazz clazz = formData.convertToOnlyClass();
			// 2. set active to true as default
			clazz.setActive(true);
			// 3. get Course
			Course course = courseService.getCourse(Long.parseLong(formData.getCourseId()));
			// 4. get Cycle
			Cycle cycle = cycleService.findCycleByDate(formData.getStartDate());
			// 5. assign Course & Cycle
			clazz.setCourse(course);
			clazz.setCycle(cycle);
			// 6. add Class
			clazzService.addClazz(clazz);
			// 3. return success;
			return ResponseEntity.ok("\"Class register success\"");
		} catch (Exception e) {
			String message = "Error registering Class: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// register new cycle
	@PostMapping("/registerCycle")
	@ResponseBody
	public ResponseEntity<String> registerCycle(@RequestBody CycleDTO formData) {
		try {
			// 1. create Cycle
			Cycle cycle = formData.convertToCycle();
			// 2. add Class
			cycleService.addCycle(cycle);
			// 3. return success;
			return ResponseEntity.ok("\"Class register success\"");
		} catch (Exception e) {
			String message = "Error registering Class: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// update existing class
	@PutMapping("/update/class")
	@ResponseBody
	public ResponseEntity<String> updateClazz(@RequestBody ClazzDTO formData) {
		try {
			// 1. create bare Class
			Clazz clazz = formData.convertToOnlyClass();
			// 2. get Course
			Course course = courseService.getCourse(Long.parseLong(formData.getCourseId()));
			// 3. get Cycle
			Cycle cycle = cycleService.findCycleByDate(formData.getStartDate());
			// 4. assign Course & Cycle
			clazz.setCourse(course);
			clazz.setCycle(cycle);
			// 5. save Class
			clazzService.updateClazz(clazz);
			// 6. return flag
			return ResponseEntity.ok("\"Class update success\"");
		} catch (Exception e) {
			String message = "Error updating class: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// update existing course
	@PutMapping("/update/course")
	@ResponseBody
	public ResponseEntity<String> updateCourse(@RequestBody CourseDTO formData) {
		try {
			// 1. create Course
			Course course = formData.convertToCourse();
			// 2. save Class
			courseService.updateCourse(course);
			// 3. return flag
			return ResponseEntity.ok("\"Course update success\"");
		} catch (Exception e) {
			String message = "Error updating course: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// update existing cycle
	@PutMapping("/update/cycle")
	@ResponseBody
	public ResponseEntity<String> updateCycle(@RequestBody CycleDTO formData) {
		try {
			// 1. create Cycle
			Cycle cycle = formData.convertToCycle();
			// 2. uppdate Cycle
			cycleService.updateCycle(cycle);
			// 3. return flag
			return ResponseEntity.ok("\"Course update success\"");
		} catch (Exception e) {
			String message = "Error updating course: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// bring all courses for dropdown list
	@GetMapping("/filterClass")
	@ResponseBody
	public List<ClazzDTO> filterClasses(@RequestParam(value = "listState", required = false) String state,
			@RequestParam(value = "listBranch", required = false) String branch,
			@RequestParam(value = "listGrade", required = false) String grade) {
		// System.out.println(state + "\t" + branch + "\t" + grade);
		List<ClazzDTO> dtos = clazzService.filterClazz(state, branch, grade);
		return dtos;
	}

	// list classes for teacher association
	@GetMapping("/classes4Teacher")
	@ResponseBody
	List<ClazzDTO> getClassesForTeacher(@RequestParam("state") String state, @RequestParam("branch") String branch,
			@RequestParam("grade") String grade, @RequestParam("year") String year) {
		// List<ClazzDTO> dtos = clazzService.findClazzForCourseIdNCycle(courseId,
		// year);
		List<ClazzDTO> dtos = clazzService.filterOnSiteClazz(state, branch, grade, year);
		return dtos;
	}

}
