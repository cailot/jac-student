package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import hyung.jin.seo.jae.dto.SubjectDTO;
import hyung.jin.seo.jae.model.Clazz;
import hyung.jin.seo.jae.model.Course;
import hyung.jin.seo.jae.model.Cycle;
import hyung.jin.seo.jae.model.Subject;
import hyung.jin.seo.jae.service.ClazzService;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.CourseService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("class")
public class ClazzController {

	@Autowired
	private ClazzService clazzService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private CodeService codeService;

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
	Long getOnlineId(@RequestParam("grade") String grade, @RequestParam("year") int year) {
		Long id = clazzService.getOnlineId(grade, year);
		return id;
	}

	// bring all classes in database
	@GetMapping("/listClass")
	public String listClasses(@RequestParam(value = "listState", required = false) String state,
			@RequestParam(value = "listBranch", required = false) String branch,
			@RequestParam(value = "listGrade", required = false) String grade,
			@RequestParam(value = "listYear", required = false) String year,
			@RequestParam(value = "listType", required = false) String type, Model model) {
		List<ClazzDTO> dtos = new ArrayList();
		String clazzType = StringUtils.defaultString(type);
		if(JaeConstants.ONSITE.equalsIgnoreCase(clazzType)){
			dtos = clazzService.listOnsiteClazz(state, branch, grade, year);
		}else if(JaeConstants.ONLINE.equalsIgnoreCase(clazzType)){
			dtos = clazzService.listOnlineClazz(state, branch, grade, year);
		}else{
			dtos = clazzService.listClazz(state, branch, grade, year);
		}
		model.addAttribute(JaeConstants.CLASS_LIST, dtos);
		return "classListPage";
	}
	
	// bring all classes in database
	@GetMapping("/listCycle")
	public String listCycle(@RequestParam(value = "listYear", required = true) String year, Model model) {
		List<CycleDTO> dtos = null;
		if(StringUtils.isNotEmpty(year) && !("0".equalsIgnoreCase(year))) {
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
		// associate subjects
		for(CourseDTO dto : dtos){
			Course course = courseService.getCourse(Long.parseLong(dto.getId()));
			List<Subject> subjects = course.getSubjects();
			for(Subject subject : subjects){
				SubjectDTO subDTO = new SubjectDTO(subject);
				dto.addSubject(subDTO);
			}
		}
		// if new academic year is going to start, display next year classes
		if (week > JaeConstants.ACADEMIC_START_COMMING_WEEKS) {
			List<CourseDTO> nexts = courseService.findByGradeNYear(grade, year+1);
			if(nexts.size() > 0){
				for(CourseDTO next : nexts){
					// update description
					next.setDescription(next.getDescription() + JaeConstants.ACADEMIC_NEXT_YEAR_COURSE_SUFFIX);
					// associate subjects
					Course nextCourse = courseService.getCourse(Long.parseLong(next.getId()));
					List<Subject> nextSubs = nextCourse.getSubjects();
					for(Subject nextSub : nextSubs){
						SubjectDTO nextSubDTO = new SubjectDTO(nextSub);
						next.addSubject(nextSubDTO);
					}
				}
				dtos.addAll(nexts);
			}
		}
		return dtos;
	}

	// bring onsite courses based on grade
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
		List<Subject> subjects = course.getSubjects();
		for(Subject subject : subjects){
			SubjectDTO subDTO = new SubjectDTO(subject);
			dto.addSubject(subDTO);
		}
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
			// 2. associate Subjects
			List<SubjectDTO> subjects = formData.getSubjects();
			for(SubjectDTO subject : subjects){
				Subject sub =  codeService.getSubject(Long.parseLong(subject.getId()));
				course.addSubject(sub);
			}
			// 3. associate Cycle
			Cycle cycle = cycleService.findCycleByYear(formData.getYear());
			course.setCycle(cycle);
			// 4. set active to true as default
			course.setActive(true);
			// 5. save Course
			courseService.addCourse(course);
			// 6. return success;
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
			// 4. assign Course & Cycle
			clazz.setCourse(course);
			// 5. add Class
			clazzService.addClazz(clazz);
			// 6. return success;
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
			// 3. assign Course & Cycle
			clazz.setCourse(course);
			// 4. save Class
			clazzService.updateClazz(clazz);
			// 5. return flag
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
			// 2. associate Subjects
			List<SubjectDTO> subjects = formData.getSubjects();
			for(SubjectDTO subject : subjects){
				Subject sub =  codeService.getSubject(Long.parseLong(subject.getId()));
				course.addSubject(sub);
			}
			// 3. save Class
			courseService.updateCourse(course, Long.parseLong(formData.getId()));
			// 4. return flag
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
			@RequestParam("grade") String grade, @RequestParam("year") int year) {
		// List<ClazzDTO> dtos = clazzService.findClazzForCourseIdNCycle(courseId,
		// year);
		List<ClazzDTO> dtos = clazzService.filterOnSiteClazz(state, branch, grade, year);
		return dtos;
	}

	// get online course url
	@GetMapping("/getOnlineAddress/{grade}/{week}")
	@ResponseBody
	public String getOnlineCourse(@PathVariable("grade") int grade, @PathVariable("week") int week) {	
		// 1. get URL from stored data by administrator
		String info = "https://us02web.zoom.us/rec/play/ma2pfFazOsXqFla1dreILhb5Xjffq-85oAksTr9TgxjNdPfHDRKQMz7hcxuJrbpUaE6ofpw0wQ0WCt4s.qQEHvpWXF4BWgnru?canPlayFromShare=true&from=share_recording_detail&startTime=1706506287000&componentName=rec-play&originRequestUrl=https%3A%2F%2Fus02web.zoom.us%2Frec%2Fshare%2FmnB4w4HZI80oTYn_UyQCkveSxmITcw0Xs-Myw9pN4DUx4Dv-HrOaosI4si2jeOmr.32ShDyR6f2WnTe3j%3FstartTime%3D1706506287000";
		info ="https://us02web.zoom.us/rec/share/pBvQsJ7smzy0kGHPyB8Hpp5Z1gwIsEl7EIeLDr-FvxX6CpFaC24FhU12j1Hc6wIF.t-Su391-4i26Htif?startTime=1707715876000";
		// System.out.println(grade + " : " +  week);
		// 3. return info
		return info;
	}

	// remove cycle by Id
	@PutMapping("/deleteCycle/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteCycle(@PathVariable("id") Long id) {
		try{
			cycleService.deleteCycle(id);
			return ResponseEntity.ok("\"Cycle delete success\"");		
		}catch(Exception e){
			String message = "Error deleting cycle: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// delete course by Id
	@DeleteMapping(value = "/deleteCourse/{courseId}")
	@ResponseBody
    public ResponseEntity<String> removeCourse(@PathVariable String courseId) {
        Long id = Long.parseLong(StringUtils.defaultString(courseId, "0"));
		courseService.deleteCourse(id);
		return ResponseEntity.ok("\"Course deleted successfully\"");
    }

	// delete class by Id
	@DeleteMapping(value = "/deleteClass/{classId}")
	@ResponseBody
	public ResponseEntity<String> removeClass(@PathVariable String classId) {
		Long id = Long.parseLong(StringUtils.defaultString(classId, "0"));
		clazzService.deleteClass(id);
		return ResponseEntity.ok("\"classId deleted successfully\"");
	}

}
