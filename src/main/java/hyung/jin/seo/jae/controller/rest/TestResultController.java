package hyung.jin.seo.jae.controller.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.dto.StudentDTO;
import hyung.jin.seo.jae.dto.StudentTestDTO;
import hyung.jin.seo.jae.dto.TestResultHistoryDTO;
import hyung.jin.seo.jae.dto.TestScheduleDTO;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.model.TestAnswerItem;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.ConnectedService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.PdfService;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@RestController
@RequestMapping("result")
public class TestResultController {

	@Autowired
	private PdfService pdfService;

	@Autowired
	private ConnectedService connectedService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private StudentService studentService;

	@GetMapping("/download-pdf/{id}")
	public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
		System.out.println("Download PDF with ID: " + id);

		// Correctly instantiate HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
		ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename("TestResult.pdf").build();
 		headers.setContentDisposition(contentDisposition);

		// get latest test result by student id
		List<StudentTestDTO> studentTests = connectedService.getLatestStudentTest(id);

		if (studentTests == null || studentTests.isEmpty()) {
			byte[] pdfData = pdfService.generateEmptyTestResult(id);
			return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
		}
		// prepare pdf data
		Map<String, Object> data = preparePdfData(id, studentTests);
		// generate pdf data
		byte[] pdfData = pdfService.generateTestResult(data);
		// Log the PDF size (important for debugging)
		System.out.println("Generated PDF size: " + (pdfData != null ? pdfData.length : "null"));

		if (pdfData == null || pdfData.length == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

        return ResponseEntity.ok().headers(headers).body(pdfData);
	}

	@GetMapping("/download-azure-pdf/{studentId}")
	public ResponseEntity<byte[]> downloadAzurePdf(@PathVariable String studentId) {
		System.out.println("Download Azure PDF for student: " + studentId);
		
		try {
			// Extract branch code from studentId (2nd and 3rd digits)
			String branchCode = studentId.substring(1, 3);
			String fileName = studentId + ".pdf";
			
			// Construct Azure Blob Storage URL
			String baseUrl = "https://jacstorage.blob.core.windows.net/work/test/" + branchCode + "/";
			String fullUrl = baseUrl + fileName;
			
			// Download file from Azure Blob Storage
			URL url = new URL(fullUrl);
			InputStream inputStream = url.openStream();
			byte[] fileData = inputStream.readAllBytes();
			inputStream.close();
			
			// Set headers for download
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
				.filename("TestResult_" + studentId + ".pdf").build();
			headers.setContentDisposition(contentDisposition);
			
			return ResponseEntity.ok().headers(headers).body(fileData);
			
		} catch (IOException e) {
			System.err.println("Error downloading file from Azure Blob Storage: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// prepare pdf data
	private Map<String, Object> preparePdfData(Long studentId, List<StudentTestDTO> studentTests) {
		// Set Map to store ingredients
		Map<String, Object> data = new HashMap<>();
		// add student info
		Student std = studentService.getStudent(studentId);
		data.put(JaeConstants.STUDENT_INFO, new StudentDTO(std));
		// add test result info
		data.put(JaeConstants.TEST_RESULT_INFO, studentTests);
		// add test group info
		long tempTestId = ((StudentTestDTO)(studentTests.get(0))).getTestId();
		int testGroup = connectedService.getTestGroup(tempTestId);
		String testGrade = connectedService.getTestGrade(tempTestId);
		String testGroupName = JaeUtils.getTestGroup(testGroup);
		data.put(JaeConstants.TEST_GROUP_INFO, testGroupName);
		// add branch info
		BranchDTO branch = codeService.getBranch(std.getBranch());
		data.put(JaeConstants.BRANCH_INFO, branch);
		// add test volume info
		int volume = connectedService.getTestVolume(tempTestId);
		String volumeName = "";
		if(testGroup == 1 || testGroup == 2) { // if test group is 1 or 2, then add volume info
			if(volume == 1) {
				volumeName = "Volume 1";
			}else if(volume == 2) {
				volumeName = "Volume 2";
			}else if(volume == 3) {
				volumeName = "Volume 3";
			}else if(volume == 4) {
				volumeName = "Volume 4";
			}else if(volume == 5) {
				volumeName = "Volume 5";
			}
		}else{
			if(volume == 36) {
				volumeName = "SIM 1";
			}else if(volume == 37) {
				volumeName = "SIM 2";
			}else if(volume == 38) {
				volumeName = "SIM 3";
			}else if(volume == 39) {
				volumeName = "SIM 4";
			}else if(volume == 40) {
				volumeName = "SIM 5";
			}else {
				volumeName = volume + " week";
			}
		}
		data.put(JaeConstants.VOLUME_INFO, volumeName);

		int currentYear = cycleService.academicYear();
		CycleDTO cycle = cycleService.listCycles(currentYear);
		String startCycle = cycle.getStartDate();
		String endCycle = cycle.getEndDate();

		// set test title info
		List<String> testTitles = new ArrayList<>();
		// set test answer total count info
		List<Integer> testAnswerTotalCount = new ArrayList<>();
		// set student answer correct count info
		List<Integer> studentAnswerCorrectCount = new ArrayList<>();
		// set student score
		List<Double> studentScores = new ArrayList<>();
		// set average score
		List<Double> averageScores = new ArrayList<>();
		// set highest score
		List<Double> highestScores = new ArrayList<>();
		// set lowest score
		List<Double> lowestScores = new ArrayList<>();
		// set student answer
		List<List<Integer>> studentAnswers = new ArrayList<>();
		// set test answer
		List<List<TestAnswerItem>> testAnswers = new ArrayList<>();
		// set result history
		List<List<TestResultHistoryDTO>> histories = new ArrayList<>();
		for(StudentTestDTO studentTest : studentTests) {		
			// add test title info
			long testId = studentTest.getTestId();
			String testTypeName = connectedService.getTestTypeName(testId);
			testTitles.add(testTypeName);
			// get test answer total count info
			int testAnswerCount = connectedService.getTestAnswerCount(testId);
			testAnswerTotalCount.add(testAnswerCount);
			// get student answer correct count info
			List<Integer> answers = connectedService.getStudentTestAnswer(studentId, testId, cycle.getStartDate(), cycle.getEndDate());	
			List<TestAnswerItem> testAnswerItems = connectedService.getAnswersByTest(testId);
			int correctCount = JaeUtils.countTestScore(answers, testAnswerItems);
			studentAnswerCorrectCount.add(correctCount);
			studentAnswers.add(answers);
			testAnswers.add(testAnswerItems);
			// get average, hightest, lowest score
			double studentScore = studentTest.getScore();
			double averageScore = connectedService.getAverageScoreByTest(studentTest.getTestId(), startCycle, endCycle);
			double highestScore = connectedService.getHighestScoreByTest(studentTest.getTestId(), startCycle, endCycle);
			double lowestScore = connectedService.getLowestScoreByTest(studentTest.getTestId(), startCycle, endCycle);
			studentScores.add(studentScore);
			averageScores.add(averageScore);
			highestScores.add(highestScore);
			lowestScores.add(lowestScore);

			// get test result history
			List<TestResultHistoryDTO> history = null;// new ArrayList<>();
			int weekCount = 0;	
			if(testGroup == 1 || testGroup == 2) { // Mega, Revision
				weekCount = 5;
			}else if(testGroup == 3){ // Edu - max 32
				weekCount = 32;
			}else{ // Acer - max 40
				weekCount = 40;
			}
			history = new ArrayList<>(weekCount);
			// get test result history
			for(int i=1; i<=weekCount; i++){
				TestScheduleDTO testSchedule = connectedService.getMostRecentTestSchedule(testGroup+"", testGrade, i+"");
				TestResultHistoryDTO testHistory = new TestResultHistoryDTO();
				if(testSchedule == null) {
					history.add(testHistory);
					continue;
				}
				String from = testSchedule.getFrom(); // ex> 01/04/2025, 17:38
				String dateOnlyFrom = from.substring(0, 10); // Extracts the first 10 characters ex> 01/04/2025
				try {
					from = JaeUtils.convertToyyyyMMddFormat(dateOnlyFrom);
				} catch (ParseException e) {
					from = "2100-01-01";
					e.printStackTrace();
				} // ex> 2025-04-01
				String to = testSchedule.getTo();
				String dateOnlyTo = to.substring(0, 10);
				try {
					to = JaeUtils.convertToyyyyMMddFormat(dateOnlyTo);
				} catch (ParseException e) {
					to = "2100-01-01";
					e.printStackTrace();
				}
				// Subtract 3 days from the 'from' date as buffer for Onsite date while no need for to (same as the last day of Online)
				LocalDate fromDate = LocalDate.parse(from);
				fromDate = fromDate.minusDays(3);
				from = fromDate.toString(); // Convert back to String

				double average = connectedService.getAverageScoreByTest(studentTest.getTestId(), from, to);
				testHistory.setTestNo(i);
				testHistory.setAverage((int)(average));
				// student score
				StudentTestDTO studentTestHistory = connectedService.findStudentTestByStudentNTest(studentId, testId, from, to);
				testHistory.setStudentScore(studentTestHistory==null ? 0 : (int)(studentTestHistory.getScore()));
				history.add(testHistory);			
			}
			histories.add(history);
		}
		// add test title info
		data.put(JaeConstants.TEST_TITLE_INFO, testTitles);
		// add test answer total count info
		data.put(JaeConstants.TEST_ANSWER_TOTAL_COUNT, testAnswerTotalCount);
		// add student answer correct count info
		data.put(JaeConstants.STUDENT_ANSWER_CORRECT_COUNT, studentAnswerCorrectCount);
		// add student score
		data.put(JaeConstants.STUDENT_SCORE, studentScores);
		// add average score
		data.put(JaeConstants.TEST_AVERAGE_SCORE, averageScores);
		// add highest score
		data.put(JaeConstants.TEST_HIGHEST_SCORE, highestScores);
		// add lowest score
		data.put(JaeConstants.TEST_LOWEST_SCORE, lowestScores);
		// add student answer
		data.put(JaeConstants.STUDENT_ANSWERS, studentAnswers);
		// add test answers
		data.put(JaeConstants.TEST_ANSWERS, testAnswers);
		// add test result history
		data.put(JaeConstants.TEST_RESULT_HISTORY, histories);
		return data;
	}

}
