package hyung.jin.seo.jae.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.AttendanceDTO;
import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.dto.EnrolmentDTO;
import hyung.jin.seo.jae.dto.MaterialDTO;
import hyung.jin.seo.jae.dto.OutstandingDTO;
import hyung.jin.seo.jae.model.Attendance;
import hyung.jin.seo.jae.model.Book;
import hyung.jin.seo.jae.model.Clazz;
import hyung.jin.seo.jae.model.Enrolment;
import hyung.jin.seo.jae.model.Invoice;
import hyung.jin.seo.jae.model.Material;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.AttendanceService;
import hyung.jin.seo.jae.service.BookService;
import hyung.jin.seo.jae.service.ClazzService;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.EnrolmentService;
import hyung.jin.seo.jae.service.InvoiceService;
import hyung.jin.seo.jae.service.MaterialService;
import hyung.jin.seo.jae.service.OutstandingService;
import hyung.jin.seo.jae.service.StudentService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("enrolment")
public class JaeEnrolmentController {

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private ClazzService clazzService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private OutstandingService outstandingService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private BookService bookService;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private CycleService cycleService;

	@GetMapping("/search/student/{id}")
	@ResponseBody
	public List searchEnrolmentByStudent(@PathVariable Long id) {
		List dtos = new ArrayList(); 
		// get lastest invoice id
 		List<Long> invoiceIds = enrolmentService.findInvoiceIdByStudent(id);

		boolean isInvoiceAbsent = ((invoiceIds==null) || (invoiceIds.size()==0));
		if(isInvoiceAbsent) return dtos; // return empty list if no invoice

		// check current academic year and week
		int currentYear = cycleService.academicYear();
		int currentWeek = cycleService.academicWeeks();
		
		for(Long invoiceId : invoiceIds){
			boolean isStillActive = false;
			boolean isInvoicePaid = invoiceService.isPaidInvoice(invoiceId);

			// 1. get enrolments by invoice id
			List<EnrolmentDTO> enrols = enrolmentService.findEnrolmentByInvoiceAndStudent(invoiceId, id);
			for(EnrolmentDTO enrol : enrols){
				// 2. check if enrolment is active or not
				boolean isActive = currentYear >= Integer.parseInt(enrol.getYear()) && currentWeek <= enrol.getEndWeek();








				// if full paid, set extra as paid
				if(isInvoicePaid){
					enrol.setExtra(JaeConstants.FULL_PAID);
				}
















				if(isActive){
					isStillActive = true;
					dtos.add(enrol);
				}else{ // if not active, check if fully paid or not
					// 2-1. if not fully paid, set Overdue to extra
					if(!isInvoicePaid){
						enrol.setExtra(JaeConstants.OVERDUE);
						dtos.add(enrol);
					}
				}
			}
			if((isStillActive) || (!isInvoicePaid)){
				// 3. get materials by invoice id and add to list dtos
				List<MaterialDTO> materials = materialService.findMaterialByInvoice(invoiceId);
				for(MaterialDTO material : materials){
					dtos.add(material);
				}
				// 4. get outstandings by invoice id and add to list dtos
				List<OutstandingDTO> stands = outstandingService.getOutstandingtByInvoice(invoiceId);
				for(OutstandingDTO stand : stands){
					dtos.add(stand);
				}
			}
		}

		// 4. return dtos mixed by enrolments and outstandings
		return dtos;
	}

	// count records number in database
	@GetMapping("/count")
	@ResponseBody
	public long count() {
		long count = enrolmentService.checkCount();
		return count;
	}

	// bring all enrolments in database
	@GetMapping("/list")
	@ResponseBody
	public List<EnrolmentDTO> listEnrolments() {
 		List<EnrolmentDTO> dtos = enrolmentService.allEnrolments();
		return dtos;
	}
		
	// register new student
	@PostMapping("/register")
	@ResponseBody
	public EnrolmentDTO registerEnrolment(@RequestBody EnrolmentDTO formData) {
		System.out.println(formData);
		// 1. create bare Enrolment
		Enrolment enrolment = formData.convertToEnrolment();
		// 2. get Clazz
		Clazz clazz = clazzService.getClazz(Long.parseLong(formData.getClazzId()));
		// 3. get Student
		Student student = studentService.getStudent(Long.parseLong(formData.getStudentId()));
		// 4. assign Clazz & Student
		enrolment.setClazz(clazz);
		enrolment.setStudent(student);
		// 5. save Class
		EnrolmentDTO dto = enrolmentService.addEnrolment(enrolment);
		// 6. return dto;
		return dto;
	}

	// search clazz by student Id
	@GetMapping("/getClazz/student/{id}")
	@ResponseBody
	List<ClazzDTO> searchClazzByStudent(@PathVariable Long id) {
		List<Long> clazzIds = enrolmentService.findClazzIdByStudentId(id);
		List<ClazzDTO> dtos = new ArrayList<ClazzDTO>();
		for (Long clazzId : clazzIds) {
			Clazz clazz = clazzService.getClazz(clazzId);
			ClazzDTO dto = new ClazzDTO(clazz);
			dtos.add(dto);
		}
		return dtos;
	}

	/////////////////////////////////////////////////////////
	// Enrolment
	////////////////////////////////////////////////////////
	// associate elearnings with student
	@PostMapping("/associateElearning/{studentId}")
	@ResponseBody
	public ResponseEntity<String> associateElearning(@PathVariable Long studentId, @RequestBody Long[] elearningIds) {
		// // 1. get student
		// Student std = studentService.getStudent(studentId);
		// // 2. empty elearning list
		// Set<Elearning> elearningSet = std.getElearnings();
		// elearningSet.clear();
		// // 3. associate elearnings
		// for(Long elearningId : elearningIds) {
		// 	Elearning elearning = elearningService.getElearning(elearningId);
		// 	elearningSet.add(elearning);
		// }
		// // 4. update student
		// studentService.updateStudent(std, studentId);
		// 5. return success
		return ResponseEntity.ok("eLearning Success");
	}


	// @PostMapping("/associateBook/{studentId}")
	// @ResponseBody
	// public List<MaterialDTO> associateBook(@PathVariable Long studentId, @RequestBody Long[] bookIds) {
	// 	List<MaterialDTO> dtos = new ArrayList<>();
	// 	// 1. get Invoice
	// 	Invoice invo = invoiceService.getInvoiceByStudentId(studentId);
	// 	// if no invoice or no book, return empty list
	// 	if((invo==null) || (bookIds==null)) return dtos;
	// 	// 2. bring all registered Book - bookId & invoiceId
	// 	List<Long> registeredIds = materialService.findBookIdByInvoiceId(invo.getId());
	// 	for(Long bookId : bookIds){
	// 		// 2-1. check whether registeredIds contains bookId or not
	// 		if(registeredIds.contains(bookId)){ // if book is already registered, then do nothging but remove from registeredIds
	// 			registeredIds.remove(bookId);
	// 			continue;
	// 		}else{ // there is no registered book, which requires adding new book
	// 			// 2-2. get Book
	// 			Book book = bookService.getBook(bookId);
	// 			// 2-3. update invoice amount
	// 			invo.setAmount(invo.getAmount() + book.getPrice());
	// 			// 2-4. create Material
	// 			Material material = new Material();
	// 			material.setBook(book);
	// 			material.setInvoice(invo);
	// 			// 2-4. save Material - Invoice will be automatically updated
	// 			material = materialService.addMaterial(material);	
	// 		}
	// 	}	
	// 	// 3. if bookId is in the list but no passed then delete - delete book
	// 	for(Long deleteId : registeredIds){
	// 		// 3-2. update Invoice
	// 		double price = bookService.getPrice(deleteId);
	// 		invo.setAmount(invo.getAmount() - price);
	// 		// 3-3. remove Material by bookId & invoiceId - Invoice will be automatically upated
	// 		materialService.deleteMaterial(invo.getId(), deleteId);
	// 	}
	// 	// 4. add MaterialDTO to return list
	// 	Set<Material> materials = invo.getMaterials();
	// 	for (Material material : materials) {
	// 		dtos.add(new MaterialDTO(material));
	// 	}
	// 	return dtos;
	// }

	@PostMapping("/associateBook/{studentId}")
	@ResponseBody
	public List<MaterialDTO> associateBook(@PathVariable Long studentId, @RequestBody MaterialDTO[] formData) {
		List<MaterialDTO> dtos = new ArrayList<>();
		// 1. check if formData is empty or not
		// 1-1. if empty, it means delete existing Materials then return empty list
		if((formData==null)||(formData.length==0)){
			// 1-1-1. get Invoice
			Invoice invo = invoiceService.getLastInvoiceByStudentId(studentId);
			// 1-1-2. get all registered Materials
			List<MaterialDTO> materials = materialService.findMaterialByInvoice(invo.getId());
			for(MaterialDTO material : materials){
				// 1-1-3. delete Materials
				double price = material.getPrice();
				invo.setAmount(invo.getAmount() - price);
				materialService.deleteMaterial(Long.parseLong(material.getId()));
			}
			// 1-1-4. return empty list
			return dtos;
		}

		// 1-2. if not, add or update(skip) Materials
		// 1-2-1. get Invoice
		//Invoice invo = invoiceService.getLastActiveInvoiceByStudentId(studentId);
		Invoice invo = invoiceService.getLastInvoiceByStudentId(studentId);
		// 1-2-2. bring all registered Book - bookId & invoiceId
		List<Long> registeredIds = materialService.findBookIdByInvoiceId(invo.getId());
		for(MaterialDTO material : formData){
			// 1-2-3. check if formData has Id or not. if there is no id it means new Material
			if(StringUtils.isBlank(material.getId())){
				// 1-2-3-1. get Book
				Book book = bookService.getBook(Long.parseLong(material.getBookId()));
				// 1-2-3-2. update invoice amount
				invo.setAmount(invo.getAmount() + book.getPrice());
				// 1-2-3-3. create Material
				Material newMaterial = new Material();
				newMaterial.setBook(book);
				newMaterial.setInvoice(invo);
				// 1-2-3-4. save Material - Invoice will be automatically updated
				newMaterial = materialService.addMaterial(newMaterial);
			}
			// 1-2-4. if there is id, it means existing Material so no need to update, simply remove from registeredIds
			else{
				registeredIds.remove(Long.parseLong(material.getBookId()));
			}	
		}
		// 1-3. if bookId is in the list but no passed then delete - delete book
		for(Long deleteId : registeredIds){
			// 3-2. update Invoice
			double price = bookService.getPrice(deleteId);
			invo.setAmount(invo.getAmount() - price);
			// 3-3. remove Material by bookId & invoiceId - Invoice will be automatically upated
			materialService.deleteMaterialByInvoiceAndBook(invo.getId(), deleteId);
		}
		// 1-4. add MaterialDTO to return list
		Set<Material> materials = invo.getMaterials();
		for (Material material : materials) {
			dtos.add(new MaterialDTO(material));
		}
		return dtos;
	}

	@PostMapping("/associateOutstanding/{studentId}")
	@ResponseBody
	public List<OutstandingDTO> associateOutstanding(@PathVariable Long studentId) {
		List<OutstandingDTO> dtos = new ArrayList<>();
		// 1. get Invoice
		Invoice invo = invoiceService.getLastActiveInvoiceByStudentId(studentId);
		// 2. check if invoice has owing amount
		boolean isValidInvoice = (invo !=null) && (invo.getAmount() > invo.getPaidAmount());
		// 3. if invoice is already paid or null, return empty list
		if(!isValidInvoice) return dtos;
		// 4. bring all related Outstandings
		dtos = outstandingService.getOutstandingtByInvoice(invo.getId());
		// 5. return OutstandingDTO list
		return dtos;
	}



	@PostMapping("/associateClazz/{studentId}")
	@ResponseBody
	public List<EnrolmentDTO> associateClazz(@PathVariable Long studentId, @RequestBody EnrolmentDTO[] formData) {
		
		List<EnrolmentDTO> dtos = new ArrayList<>();
		// 1. check Invoice available
		boolean isInvoiceExist = false;
		// 2. get latest Invoice by studentId
		Invoice invo = invoiceService.getLastActiveInvoiceByStudentId(studentId);
		// 3. check whether Invoice is already created and still available
		if((invo!=null) && (invo.getAmount() > invo.getPaidAmount())){
			isInvoiceExist = true;
		}
		// 4. get Student to associate to Enrolment later
		Student student = studentService.getStudent(studentId);
		// 5. if no Invoice or Invoice is already paid, create new Invoice; otherwise use existing Invoice
		Invoice invoice = (isInvoiceExist) ? invo : invoiceService.addInvoice(new Invoice());
		// 6. get registered enrolments by invoice id
		List<Long> registeredIds = enrolmentService.findEnrolmentIdByInvoiceId(invoice.getId());

		for(EnrolmentDTO data : formData){
			// if Invoice is already created and still available, use it
			if(isInvoiceExist){
				// check class already belong to Invoice
				// Invoice already created but additional Enrolment (ADD)
				if(StringUtils.isEmpty(data.getId())){
					// 1. create new Enrolment
					Clazz clazz = clazzService.getClazz(Long.parseLong(data.getClazzId()));
					// 2. update Invoice amount
					// check discount is % or amount value
					String discount =StringUtils.defaultString(data.getDiscount(), "0");
					double discountAmount = 0;
					if(discount.contains("%")){
						discountAmount = (((data.getEndWeek()-data.getStartWeek()+1)-data.getCredit()) * data.getPrice()) * (Double.parseDouble(discount.replace("%", ""))/100);
					}else{
						discountAmount = Double.parseDouble(discount);
					}
					double enrolmentPrice = ((((data.getEndWeek()-data.getStartWeek()+1)-data.getCredit()) * data.getPrice()) - discountAmount);
					int credit = data.getCredit();
					// double discount = data.getDiscount();
					invoice.setAmount(invoice.getAmount() + enrolmentPrice);
					invoice.setCredit(invoice.getCredit() + credit);
					invoice.setDiscount(invoice.getDiscount() + discountAmount);
					// invoiceService.updateInvoice(invoice, invoice.getId());
					// 3. associate new Enrolment with Clazz,Student,Invoice
					Enrolment enrolment = new Enrolment();
					int startWeek = data.getStartWeek();
					int endWeek = data.getEndWeek();
					enrolment.setStartWeek(startWeek);
					enrolment.setEndWeek(endWeek);
					enrolment.setCredit(data.getCredit());
					enrolment.setDiscount(data.getDiscount());
					enrolment.setClazz(clazz);
					enrolment.setStudent(student);
					enrolment.setInvoice(invoice);

					// 4. save new Enrolment - Invoice will be automatically updated
					enrolmentService.addEnrolment(enrolment);
					data.setExtra(JaeConstants.NEW_ENROLMENT);
					data.setId(enrolment.getId()+"");
					data.setInvoiceId(invoice.getId()+"");

					// 5. put into List<EnrolmentDTO>
					dtos.add(data);

					// 6. if onlline class, skip attendance; otherwise create attendance	
					if(!data.isOnline()){
						///////////////// Attendance ////////////////////////
						int academicYear = clazzService.getAcademicYear(clazz.getId());
						String clazzDay = clazzService.getDay(clazz.getId());
						for(int i = startWeek; i <= endWeek; i++){
							Attendance attendance = new Attendance();
							attendance.setWeek(i+"");
							attendance.setStudent(student);
							attendance.setClazz(clazz);
							attendance.setDay(clazzDay);
							attendance.setStatus(JaeConstants.ATTEND_OTHER);
							
							LocalDate attendDate = cycleService.getDateByWeekAndDay(academicYear, i, clazzDay);
							attendance.setAttendDate(attendDate);
							
							attendanceService.addAttendance(attendance);
						}
						//////////////////////////////////////////////////////////
					}
				}else{ // Invoice already created and registered Enrolment, update Enrolment (UPDATE)
					// 1. get existing Enrolment
					Enrolment existing = enrolmentService.getEnrolment(Long.parseLong(data.getId()));
					// 2. update invoice amount (extract existing amount, credit, discount)
					int existStart = existing.getStartWeek();
					int existEnd = existing.getEndWeek();
					int existCredit = existing.getCredit();
					// check discount is % or amount value
					String existDiscount =StringUtils.defaultString(existing.getDiscount(), "0");
					double existDCAmount = 0;
					if(existDiscount.contains("%")){
						existDCAmount = (((existEnd-existStart+1)-existCredit) * data.getPrice()) * (Double.parseDouble(existDiscount.replace("%", ""))/100);
					}else{
						existDCAmount = Double.parseDouble(existDiscount);
					}
					double existTotal = ((((existEnd-existStart+1)-existCredit) * data.getPrice()) - existDCAmount);
					// extract existing values <MINUS>
					invoice.setCredit(invoice.getCredit() - existCredit);
					invoice.setDiscount(invoice.getDiscount() - existDCAmount);
					invoice.setAmount(invoice.getAmount() - existTotal);
					// update with new values <PLUS>
					int startWeek = data.getStartWeek();
					int endWeek = data.getEndWeek();
					int credit = data.getCredit();
					// check discount is % or amount value
					String discount =StringUtils.defaultString(data.getDiscount(), "0");
					double discountAmount = 0;
					if(discount.contains("%")){
						discountAmount = (((endWeek-startWeek+1)-credit) * data.getPrice()) * (Double.parseDouble(discount.replace("%", ""))/100);
					}else{
						discountAmount = Double.parseDouble(discount);
					}
					double enrolmentPrice = ((((endWeek-startWeek+1)-credit) * data.getPrice()) - discountAmount);
					invoice.setCredit(invoice.getCredit() + credit);
					invoice.setDiscount(invoice.getDiscount() + discountAmount);
					invoice.setAmount(invoice.getAmount() + enrolmentPrice);
					// 3. update Enrolment - Invoice will be automatically updated
					existing.setStartWeek(startWeek);
					existing.setEndWeek(endWeek);
					existing.setCredit(credit);
					existing.setDiscount(discount);
					enrolmentService.updateEnrolment(existing, existing.getId());
					// 4. put into List<EnrolmentDTO>
					dtos.add(data);
					// 5. remove enrolmentId from enrolmentIds
					registeredIds.remove(existing.getId());

					// 6. if onlline class, skip attendance; otherwise update attendance
					if(!data.isOnline()){
						///////////////// Attendance ////////////////////////
						Clazz clazz = clazzService.getClazz(Long.parseLong(data.getClazzId()));
						int academicYear = clazzService.getAcademicYear(clazz.getId());
						String clazzDay = clazzService.getDay(clazz.getId());
						List<AttendanceDTO> attendances = attendanceService.findAttendanceByStudentAndClazz(studentId, clazz.getId());
						int minValue = Integer.parseInt(attendances.get(0).getWeek());
						int maxValue = Integer.parseInt(attendances.get(attendances.size()-1).getWeek());
						LocalDate today = LocalDate.now();		
						for(AttendanceDTO attendance : attendances){
							// check attendDate is later than today
							LocalDate attendDate = LocalDate.parse(attendance.getAttendDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							if(attendDate.isAfter(today)){
								// update attendance
								int week = Integer.parseInt(attendance.getWeek());
								// if week is before startWeek or after endWeek, delete attendance
								if((week < startWeek) || (week > endWeek)){
									long attendId = Long.parseLong(attendance.getId());
									attendanceService.deleteAttendance(attendId);
								}							
							}
						}
						// add new attendance if not exist
						for(int i = startWeek; i <= endWeek; i++){
							// check week does not belong to min/max value, then add attendance
							if((i < minValue) || (i > maxValue)){
								Attendance attendance = new Attendance();
								attendance.setWeek(i+"");
								attendance.setStudent(student);
								attendance.setClazz(clazz);
								attendance.setDay(clazzDay);
								attendance.setStatus(JaeConstants.ATTEND_OTHER);
								LocalDate attendDate = cycleService.getDateByWeekAndDay(academicYear, i, clazzDay);
								attendance.setAttendDate(attendDate);
								attendanceService.addAttendance(attendance);
							}
						}
						//////////////////////////////////////////////////////////
					}

				}

			}else{ // if no Invoice or Invoice is already paid, create new Invoice (ADD)
				
				///////// check if enrolment is fully paid from previous invoice ///////////
				boolean isAlreadyFullPaid = StringUtils.equalsIgnoreCase(data.getExtra(), JaeConstants.FULL_PAID);
				// if so, no need to create new enrolment, skip it
				if(isAlreadyFullPaid) continue;
				/////////////

				// 1. create new Enrolment
				Clazz clazz = clazzService.getClazz(Long.parseLong(data.getClazzId()));
				// 2. update Invoice amount
				// check discount is % or amount value
				String discount =StringUtils.defaultString(data.getDiscount(), "0");
				double discountAmount = 0;
				if(discount.contains("%")){
					discountAmount = (((data.getEndWeek()-data.getStartWeek()+1)-data.getCredit()) * data.getPrice()) * (Double.parseDouble(discount.replace("%", ""))/100);
				}else{
					discountAmount = Double.parseDouble(discount);
				}
				double enrolmentPrice = ((((data.getEndWeek()-data.getStartWeek()+1)-data.getCredit()) * data.getPrice()) - discountAmount);
				int credit = data.getCredit();
				// double discount = data.getDiscount();
				invoice.setAmount(invoice.getAmount() + enrolmentPrice);
				invoice.setCredit(invoice.getCredit() + credit);
				invoice.setDiscount(invoice.getDiscount() + discountAmount);
				// invoiceService.updateInvoice(invoice, invoice.getId());
				// 3. associate new Enrolment with Clazz,Student,Invoice
				Enrolment enrolment = new Enrolment();
				int startWeek = data.getStartWeek();
				int endWeek = data.getEndWeek();
				enrolment.setStartWeek(startWeek);
				enrolment.setEndWeek(endWeek);
				enrolment.setCredit(data.getCredit());
				enrolment.setDiscount(data.getDiscount());
				enrolment.setClazz(clazz);
				enrolment.setStudent(student);
				enrolment.setInvoice(invoice);
				// 4. save new Enrolment - Invoice will be automatically updated
				EnrolmentDTO added = enrolmentService.addEnrolment(enrolment);
				data.setExtra(JaeConstants.NEW_ENROLMENT);
				data.setId(added.getId());
				data.setInvoiceId(invoice.getId()+"");

				// 4.  put into List<EnrolmentDTO>
				dtos.add(data);
				
				// 5. if onlline class, skip attendance; otherwise create attendance
				if(!data.isOnline()){	
					///////////////// Attendance ////////////////////////
					int academicYear = clazzService.getAcademicYear(clazz.getId());
					String clazzDay = clazzService.getDay(clazz.getId());
					for(int i = startWeek; i <= endWeek; i++){
						Attendance attendance = new Attendance();
						attendance.setWeek(i+"");
						attendance.setStudent(student);
						attendance.setClazz(clazz);
						attendance.setDay(clazzDay);
						attendance.setStatus(JaeConstants.ATTEND_OTHER);
						LocalDate attendDate = cycleService.getDateByWeekAndDay(academicYear, i, clazzDay);
						attendance.setAttendDate(attendDate);
						attendanceService.addAttendance(attendance);
					}
					//////////////////////////////////////////////////////////
				}
			}
		}// end of loop
		
		// 6. archive enrolments not in formData (DELETE)
		for(Long enrolmentId : registeredIds) {
			// update Invoice amount
			Enrolment enrolment = enrolmentService.getEnrolment(enrolmentId);
			int start = enrolment.getStartWeek();	
			int end = enrolment.getEndWeek();
			int credit = enrolment.getCredit();
			double price = clazzService.getPrice(enrolment.getClazz().getId());
			// check discount is % or amount value
			String discount =StringUtils.defaultString(enrolment.getDiscount(), "0");
			double discountAmount = 0;
			if(discount.contains("%")){
				discountAmount = (((end-start+1)-credit) * price) * (Double.parseDouble(discount.replace("%", ""))/100);
			}else{
				discountAmount = Double.parseDouble(discount);
			}
			double enrolmentPrice = ((((end-start+1)-credit) * price) - discountAmount);
			invoice.setCredit(invoice.getCredit() - credit);
			invoice.setDiscount(invoice.getDiscount() - discountAmount);
			invoice.setAmount(invoice.getAmount() - enrolmentPrice);
			// invoiceService.updateInvoice(invoice, invoice.getId());
			// archive Enrolment - Invoice will be automatically updated
			enrolmentService.archiveEnrolment(enrolmentId);

			// remove enrolmentId from registeredIds
			// registeredIds.remove(enrolmentId);

			///////////////// Attendance ////////////////////////
			long clazzId = enrolment.getClazz().getId();
			List<AttendanceDTO> attandances = attendanceService.findAttendanceByStudentAndClazz(studentId, clazzId);
			for(AttendanceDTO attendance : attandances){
				// check attendDate is later than today
				LocalDate attendDate = LocalDate.parse(attendance.getAttendDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				LocalDate today = LocalDate.now();
				if(attendDate.isAfter(today)){
					// delete attendance
					long attendId = Long.parseLong(attendance.getId());
					attendanceService.deleteAttendance(attendId);
				}
			}
			//////////////////////////////////////////////////////////
		}

		// 7. if only one left and it is online free course, then remove (DELETE)
		List<Long> finalIds = enrolmentService.findEnrolmentIdByInvoiceId(invoice.getId());
		if(finalIds.size()==1){
			Long lastEnrolmentId = finalIds.get(0);
			EnrolmentDTO lastEnrol = enrolmentService.getActiveEnrolment(lastEnrolmentId);
			boolean isFreeOnline = lastEnrol.isOnline() && lastEnrol.getDiscount().equalsIgnoreCase(JaeConstants.DISCOUNT_FREE);
			
			if(isFreeOnline){
				// archive Enrolment - Invoice will be automatically updated
				enrolmentService.archiveEnrolment(lastEnrolmentId);
				// remove EnrolmentDTO from dtos
				for(EnrolmentDTO dto : dtos){
					if(dto.getId().equalsIgnoreCase(lastEnrolmentId+"")){
						dtos.remove(dto);
						break;
					}
				}
			}
		}

		return dtos;
	}


}
