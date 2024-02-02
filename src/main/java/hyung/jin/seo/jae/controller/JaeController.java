package hyung.jin.seo.jae.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JaeController {


	@GetMapping("/studentAdmin")
	public String adminJob(HttpSession session) {
		return "studentAdminPage";
	}

	@GetMapping("/studentList")
	public String studentList(HttpSession session) {
		return "studentListPage";
	}

	@GetMapping("/studentInvoice")
	public String studentInvoice(HttpSession session) {
		return "studentInvoicePage";
	}

	@GetMapping("/studentAttendance")
	public String studentAttendance(HttpSession session) {
		return "studentAttendancePage";
	}

	@GetMapping("/courseList")
	public String courseList(HttpSession session) {
		return "courseListPage";
	}
	
	@GetMapping("/classList")
	public String classList(HttpSession session) {
		return "classListPage";
	}

	@GetMapping("/cycle")
	public String academicCycle(HttpSession session) {
		return "cyclePage";
	}

	@GetMapping("/branch")
	public String branchList(HttpSession session) {
		return "branchPage";
	}

	@GetMapping("/teacherList")
	public String teacher(HttpSession session) {
		return "teacherListPage";
	}

	@GetMapping("/setting")
	public String setting(HttpSession session) {
		return "settingPage";
	}

	@GetMapping("/receipt")
	public String openReceipt(HttpSession session) {
		return "receiptPage";
	}

	@GetMapping("/invoice")
	public String openInvoice(HttpSession session) {
		return "invoicePage";
	}

}
