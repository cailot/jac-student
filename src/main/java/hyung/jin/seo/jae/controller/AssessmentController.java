package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hyung.jin.seo.jae.dto.AssessmentAnswerDTO;
import hyung.jin.seo.jae.dto.AssessmentDTO;
import hyung.jin.seo.jae.model.Assessment;
import hyung.jin.seo.jae.model.AssessmentAnswer;
import hyung.jin.seo.jae.model.AssessmentAnswerItem;
import hyung.jin.seo.jae.model.Grade;
import hyung.jin.seo.jae.model.Subject;
import hyung.jin.seo.jae.service.AssessmentService;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("assessment")
public class AssessmentController {

	@Autowired
	private CodeService codeService;

	@Autowired
	private AssessmentService assessmentService;


	// register test
	@PostMapping("/addAssessment")
	@ResponseBody
	public AssessmentDTO registerAssessment(@RequestBody AssessmentDTO formData) {
		// 1. create barebone
		Assessment work = formData.convertToAssessment();
		// 2. set active to true as default
		work.setActive(true);
		// 3. set Grade & Subject
		Grade grade = codeService.getGrade(Long.parseLong(formData.getGrade()));
		Subject subject = codeService.getSubject(formData.getSubject());
		// 4. associate Grade & Subject
		work.setGrade(grade);
		work.setSubject(subject);
		// 5. register Assessment
		Assessment added = assessmentService.addAssessment(work);
		// 6. return dto
		AssessmentDTO dto = new AssessmentDTO(added);
		return dto;
	}


	@GetMapping("/listAssessment")
	public String listAssessment(
			@RequestParam(value = "listGrade", required = false, defaultValue = "0") String grade,
			@RequestParam(value = "listSubject", required = false, defaultValue = "0") Long subject,
			Model model) {
		List<AssessmentDTO> dtos = new ArrayList();
		dtos = assessmentService.listAssessment(grade, subject);		
		model.addAttribute(JaeConstants.ASSESSMENT_LIST, dtos);
		return "assessListPage";
	}

	// get assessment
	@GetMapping("/getAssessment/{id}")
	@ResponseBody
	public AssessmentDTO getAssessment(@PathVariable Long id) {
		Assessment work = assessmentService.getAssessment(id);
		AssessmentDTO dto = new AssessmentDTO(work);
		return dto;
	}

	// update existing practice
	@PutMapping("/updateAssessment")
	@ResponseBody
	public ResponseEntity<String> updateAssessment(@RequestBody AssessmentDTO formData) {
		try{
			// 1. create barebone Homework
			Assessment work = formData.convertToAssessment();
			// 2. update Assessment
			work = assessmentService.updateAssessment(work, Long.parseLong(formData.getId()));
			// 3.return flag
			return ResponseEntity.ok("\"Assessment updated\"");
		}catch(Exception e){
			String message = "Error updating Assessment : " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@PostMapping(value = "/saveAssessAnswer")
	@ResponseBody
    public ResponseEntity<String> saveAssessAnswerSheet(@RequestBody Map<String, Object> payload) {
        // Extract practiceId and answers from the payload
		String answerId = payload.get("answerId").toString();
		String assessId = payload.get("assessId").toString();
		List<AssessmentAnswerItem> items = new ArrayList<>();
		// convert the answers list from the payload to a List<Map<String, Object>>
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> answerList = mapper.convertValue(payload.get("answers"), new TypeReference<List<Map<String, Object>>>() {});

		// Now iterate over answerList to access question, answer, and topic for each entry
		for (Map<String, Object> answer : answerList) {
			String question = StringUtils.defaultString(answer.get("question").toString(), "0");
			String selectedAnswer = StringUtils.defaultString(answer.get("answer").toString(), "0");
			String topic = StringUtils.defaultString(answer.get("topic").toString());
			// create TestAnswerItem and put it into items
			AssessmentAnswerItem item = new AssessmentAnswerItem(Integer.parseInt(question), Integer.parseInt(selectedAnswer), topic);
			items.add(item);
		}

		// if answerId has some value, update TestAnswer; otherwise register.
		if(StringUtils.isBlank(answerId)){
			// ADD
			// 1. create bare bone
			AssessmentAnswer ta = new AssessmentAnswer();
			// 2. populate AssessmentAnswer
			ta.setAnswers(items);
			// 3. get Assessment
			Assessment assess = assessmentService.getAssessment(Long.parseLong(assessId));
			// 4. associate Assessment
			ta.setAssessment(assess);
			// 5. register TestAnswer
			assessmentService.addAssessmentAnswer(ta);
		}else{
			// UPDATE
			// 1. get TestAnswer
			AssessmentAnswer ta =  assessmentService.findAssessmentAnswer(Long.parseLong(answerId));
			// 2. populate AssessmentAnswer
			ta.setAnswers(items);
			// 3. update AssessmentAnswer
			assessmentService.updateAssessmentAnswer(ta, Long.parseLong(answerId));
		}
		return ResponseEntity.ok("\"Success\"");
    }


	// delete assessment by Id
	@DeleteMapping(value = "/delete/{id}")
	@ResponseBody
    public ResponseEntity<String> removeBook(@PathVariable Long id) {
       	assessmentService.deleteAssessment(id);
		return ResponseEntity.ok("\"Assessment deleted successfully\"");
    }

	// check if AssessmentAnswer exists or not
	@GetMapping("/checkAssessAnswer/{assessId}")
	@ResponseBody
	public AssessmentAnswerDTO findTestAnswer(@PathVariable Long assessId) {
		AssessmentAnswerDTO answer = assessmentService.getAssessmentAnswer(assessId);
		return answer;
	}

	// get Assessment
	@GetMapping("/getAssessInfo/{grade}/{subject}")
	@ResponseBody
	public AssessmentDTO getAssessInfo(@PathVariable String grade, @PathVariable long subject) {
		AssessmentDTO dto = assessmentService.getAssessmentInfo(grade, subject);
		return dto;
		// document URL	
		// String url = "https://jacstorage.blob.core.windows.net/extra-materials/MathsTopic/P3/document/2D_Shapes.pdf";
		// return url;
	}

	@PostMapping(value = "/markAssessment")
	@ResponseBody
    public ResponseEntity<String> markTest(@RequestBody Map<String, Object> payload) {
        // Extract practiceId and answers from the payload
		String studentId = StringUtils.defaultString(payload.get("studentId").toString(), "0");
		String assessId = StringUtils.defaultString(payload.get("assessId").toString(), "0");
		List<Map<String, Object>> mapAns = (List<Map<String, Object>>) payload.get("answers");
		// convert the Map of answers to List
		List<Integer> answers = convertAssessmentAnswers(mapAns);
	
	
	/*
		// compare answers with answer sheet
		List<TestAnswerItem> corrects = connectedService.getAnswersByTest(Long.parseLong(testId));
		double score = JaeUtils.calculateTestScore(answers, corrects);
		// 1. create barebone
		StudentTest st = new StudentTest();
		st.setScore(score);
		// 2. set Student & Test
		Student student = studentService.getStudent(Long.parseLong(studentId));
		Test test = connectedService.getTest(Long.parseLong(testId));
		// 3. associate Student & Test
		st.setStudent(student);
		st.setTest(test);
		// 4. set answers
		st.setAnswers(answers);
		// 5. register StudentTest
		connectedService.addStudentTest(st);
	*/
		// 6. return flag
		return ResponseEntity.ok("\"StudentTest registered\"");
    }



	// helper method converting test answers Map to List
	private List<Integer> convertAssessmentAnswers(List<Map<String, Object>> answers) {
		// Sort the answers based on the "question" key
		answers.sort(Comparator.comparingInt(answer -> Integer.parseInt(answer.get("question").toString())));
		List<Integer> answerList = new ArrayList<>();
		for (Map<String, Object> answer : answers) {
			int selectedOption = Integer.parseInt(answer.get("answer").toString());
			answerList.add(selectedOption);
		}
		return answerList;
	}
}
