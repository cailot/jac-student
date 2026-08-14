package hyung.jin.seo.jae.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.EnrolmentDTO;
import hyung.jin.seo.jae.dto.InvoiceDTO;
import hyung.jin.seo.jae.dto.MaterialDTO;
import hyung.jin.seo.jae.dto.MoneyDTO;
import hyung.jin.seo.jae.dto.PaymentDTO;
import hyung.jin.seo.jae.dto.StudentDTO;
import hyung.jin.seo.jae.model.Enrolment;
import hyung.jin.seo.jae.model.Invoice;
import hyung.jin.seo.jae.model.Material;
import hyung.jin.seo.jae.model.Payment;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.EmailService;
import hyung.jin.seo.jae.service.EnrolmentService;
import hyung.jin.seo.jae.service.InvoiceService;
import hyung.jin.seo.jae.service.MaterialService;
import hyung.jin.seo.jae.service.PaymentService;
import hyung.jin.seo.jae.service.PdfService;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Controller
@RequestMapping("invoice")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private CodeService	codeService;

	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private EmailService emailService;

	// count records number in database
	@GetMapping("/count")
	@ResponseBody
	long count() {
		long count = invoiceService.checkCount();
		return count;
	}

	// get invoice amount
	@GetMapping("/amount/{id}")
	@ResponseBody
	public double getInvoiceAmount(@PathVariable("id") Long id) {	
		double amount = invoiceService.getInvoiceOwingAmount(id);
		return (amount<0) ? 0 : amount;
	}

	// bring all invoices in database
	@GetMapping("/list")
	@ResponseBody
	public List<InvoiceDTO> listInvoices() {
 		List<InvoiceDTO> dtos = invoiceService.allInvoices();
		return dtos;
	}
		
	// register new invoice
	@GetMapping("/getInfo/{studentId}")
	@ResponseBody
	public String retrieveInvoiceInfo(@PathVariable("studentId") Long studentId) {	
		// 1. get latest invoice by student id
		Invoice invoice = invoiceService.getLastActiveInvoiceByStudentId(studentId);
		// 2. get invoice info
		String info = StringUtils.defaultString(invoice.getInfo());    
		// 3. return info
		return info;
	}

	// payment history
	@GetMapping("/history")
	public String getPayments(@RequestParam("studentKeyword") String studentId, HttpSession session) {
		// 1. flush session from previous payment
		JaeUtils.clearSession(session);
		
		// 2. get student and save it into session
		long stdId = Long.parseLong(studentId);
		Student student = studentService.getStudent(stdId);
		session.setAttribute(JaeConstants.STUDENT_INFO, student);

		// 3. get invoice id by student id
		List<Long> invoiceIds = enrolmentService.findInvoiceIdByStudent(stdId);
		if(invoiceIds.size() == 0) return "studentInvoicePage";

		// 4. get InvoiceDTOs and save it into session
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>();
		for(Long invoiceId : invoiceIds){
			Invoice invoice = invoiceService.getInvoice(invoiceId);
			invoiceDTOs.add(new InvoiceDTO(invoice));
		}
		session.setAttribute(JaeConstants.PAYMENT_INVOICES, invoiceDTOs);

		// 5. get payment list and save it into session
		List<PaymentDTO> paymentDTOs = new ArrayList<PaymentDTO>();
		for(Long invoiceId : invoiceIds){
			List<PaymentDTO> payments = paymentService.getPaymentByInvoice(invoiceId);
			// get Enrolment lilst
			List<EnrolmentDTO> enrolments = enrolmentService.findAllEnrolmentByInvoiceAndStudent(invoiceId, stdId);
			for(EnrolmentDTO enrol : enrolments){
				// 9-1. set period of enrolment to extra field
				String start = cycleService.academicStartMonday(enrol.getYear(), enrol.getStartWeek());
				String end = cycleService.academicEndSunday(enrol.getYear(), enrol.getEndWeek());
				enrol.setExtra(start + " ~ " + end);
			}
			// get Material list - no need to get material list
			// List<MaterialDTO> materials = materialService.findMaterialByInvoice(invoiceId);
			for(PaymentDTO payment : payments){
				payment.setEnrols(enrolments);
				// payment.setBooks(materials);
			}
			paymentDTOs.addAll(payments);
		}
		session.setAttribute(JaeConstants.PAYMENT_PAYMENTS, paymentDTOs);

		// 6. return redirect page
		return "studentInvoicePage";
	}

}