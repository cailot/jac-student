package hyung.jin.seo.jae.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import hyung.jin.seo.jae.dto.AssessmentAnswerDTO;
import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.EnrolmentDTO;
import hyung.jin.seo.jae.dto.GuestStudentAssessmentDTO;
import hyung.jin.seo.jae.dto.InvoiceDTO;
import hyung.jin.seo.jae.dto.MaterialDTO;
import hyung.jin.seo.jae.dto.MoneyDTO;
import hyung.jin.seo.jae.dto.OutstandingDTO;
import hyung.jin.seo.jae.dto.StudentDTO;
import hyung.jin.seo.jae.model.GuestStudent;
import hyung.jin.seo.jae.service.PdfService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Service
public class PdfServiceImpl implements PdfService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public byte[] generateAssessmentPdf(Map<String, Object> data) {
		try {
			// // Set the content type and attachment header.
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter pdfWriter = new PdfWriter(baos);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdfDocument);
			Paragraph onespace = new Paragraph("\n");
			float wholeWidth = pdfDocument.getDefaultPageSize().getWidth(); // whole width
			float wholeHeight = pdfDocument.getDefaultPageSize().getHeight(); // whole height

			// prepare ingredients
			GuestStudent student = (GuestStudent) data.get(JaeConstants.STUDENT_INFO);
			List<GuestStudentAssessmentDTO> gsas = (List<GuestStudentAssessmentDTO>) data.get(JaeConstants.STUDENT_ANSWER);
			List<AssessmentAnswerDTO> aas = (List<AssessmentAnswerDTO>) data.get(JaeConstants.CORRECT_ANSWER);
			
			// 1. button section
			Image buttons = imageLogo();
			float x = wholeWidth/2 - 90;
			float y = wholeHeight/2 + 380;
			buttons.setFixedPosition(x, y);
			document.add(buttons);
			document.add(onespace);
			document.add(onespace);

			// 2. title section
			// Table title = getGreetingTable(wholeWidth, student);
			// document.add(title);
			// document.add(onespace);

			// 3. header section
			// Table header = getHeaderTable(wholeWidth, data);
			// document.add(header);

			// // 4. detail section
			// Object[] details = getInvoiceDetailTable(wholeWidth, data);
			// Table detail = (Table) details[0];
			// double finalTotal = (double) details[1];
			// document.add(detail);
			// document.add(onespace);

			// // 5. paid section
			// Table paid = getInvoicePaidTable(wholeWidth, data, finalTotal);
			// document.add(paid);
			// document.add(onespace);
			// document.add(onespace);

			// // 6. note section
			// Table note = getBranchNoteTable(wholeWidth, data);
			// document.add(note);
			// document.close();

			byte[] pdfData = baos.toByteArray();
			return pdfData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void generateTestPdf(Map<String, Object> data) {
		try {
			// pdf directory - resources/pdf
			String projectRootPath = new File("").getAbsolutePath();
			String pdfDirectoryPath = projectRootPath + "/src/main/resources/pdf/";
			File pdfDirectory = new File(pdfDirectoryPath);
			if (!pdfDirectory.exists()) {
				pdfDirectory.mkdirs();
			}
			String fullPath = pdfDirectoryPath + "test.pdf";

			PdfWriter pdfWriter = new PdfWriter(fullPath);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdfDocument);
			Paragraph onespace = new Paragraph("\n");
			float wholeWidth = pdfDocument.getDefaultPageSize().getWidth(); // whole width
			float wholeHeight = pdfDocument.getDefaultPageSize().getHeight(); // whole height
			
			// prepare ingredients
			GuestStudent student = (GuestStudent) data.get(JaeConstants.STUDENT_INFO);
			List<GuestStudentAssessmentDTO> gsas = (List<GuestStudentAssessmentDTO>) data.get(JaeConstants.STUDENT_ANSWER);
			List<AssessmentAnswerDTO> aas = (List<AssessmentAnswerDTO>) data.get(JaeConstants.CORRECT_ANSWER);
			
			// 1. button section
			Image buttons = imageLogo();
			float x = 0;//wholeWidth/2 - 90;
			float y = 0;//wholeHeight/2 + 380;
			buttons.setFixedPosition(x, y);
			document.add(buttons);
			document.add(onespace);
			document.add(onespace);

			// 3. title section
			// Table title = getReceiptTitleTable(wholeWidth, data);
			// document.add(title);
			// document.add(onespace);

			// // 4. header section
			// Table header = getHeaderTable(wholeWidth, data);
			// document.add(header);

			// // 5. detail section
			// Object[] details = getReceiptDetailTable(wholeWidth, data);
			// Table detail = (Table) details[0];
			// double finalTotal = (double) details[1];
			// double paidTotal = (double) details[2];
			// document.add(detail);
			// document.add(onespace);

			// // 6. paid section
			// Table paid = getReceiptPaidTable(wholeWidth, finalTotal, paidTotal);
			// document.add(paid);
			// document.add(onespace);
			// document.add(onespace);

			// // 7. note section
			// Table note = getBranchNoteTable(wholeWidth, data);
			// document.add(note);
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}






	private Table getGreetingTable(float wholeWidth, GuestStudent data) throws MalformedURLException, URISyntaxException, IOException {
		// float one = wholeWidth*2/3;
		// float two = wholeWidth/3;
		float columnWith[] = {wholeWidth};
		//BranchDTO branch = data.get(JaeConstants.INVOICE_BRANCH) != null ? (BranchDTO) data.get(JaeConstants.INVOICE_BRANCH) : new BranchDTO();
		Table table = new Table(columnWith);
		table.addCell(new Cell().add("Dear....").setBorder(Border.NO_BORDER).setBold().setFontSize(20f).setVerticalAlignment(VerticalAlignment.MIDDLE));
		return table;
	}

	private Table getHeaderTable(float wholeWidth, Map<String, Object> data) {
		Table header = new Table(new float[]{wholeWidth/2, wholeWidth/2});
		Table nested1 = new Table(new float[]{(wholeWidth/2)/4, (wholeWidth/2)*3/4});
		MoneyDTO money = data.get(JaeConstants.PAYMENT_HEADER) != null ? (MoneyDTO) data.get(JaeConstants.PAYMENT_HEADER) : new MoneyDTO();
		InvoiceDTO invoice = data.get(JaeConstants.INVOICE_INFO) != null ? (InvoiceDTO) data.get(JaeConstants.INVOICE_INFO) : new InvoiceDTO();
		StudentDTO student = data.get(JaeConstants.STUDENT_INFO) != null ? (StudentDTO) data.get(JaeConstants.STUDENT_INFO) : new StudentDTO();
		nested1.addCell(boldCell("Date :").setBorder(Border.NO_BORDER));
		nested1.addCell(boldCell(JaeUtils.getToday()).setBorder(Border.NO_BORDER));
		nested1.addCell(boldCell("Name :").setBorder(Border.NO_BORDER));
		nested1.addCell(boldCell(student.getFirstName() + " " + student.getLastName()).setBorder(Border.NO_BORDER));
		nested1.addCell(boldCell("Student ID :").setBorder(Border.NO_BORDER));
		nested1.addCell(boldCell(student.getId()).setBorder(Border.NO_BORDER));
		header.addCell(new Cell().add(nested1).setBorder(Border.NO_BORDER));
		Table nested2 = new Table(new float[]{(wholeWidth/2)/4, (wholeWidth/2)*3/4});
		nested2.addCell(boldCell("Due Date :").setBorder(Border.NO_BORDER));
		nested2.addCell(boldCell(money.getRegisterDate()).setBorder(Border.NO_BORDER));
		nested2.addCell(boldCell("Grade :").setBorder(Border.NO_BORDER));
		nested2.addCell(boldCell(JaeUtils.getGradeName(money.getInfo())).setBorder(Border.NO_BORDER));
		nested2.addCell(boldCell("Invoice No :").setBorder(Border.NO_BORDER));
		nested2.addCell(boldCell(invoice.getId()).setBorder(Border.NO_BORDER));
		header.addCell(new Cell().add(nested2).setBorder(Border.NO_BORDER));
		return header;
	}
	
	private Object[] getInvoiceDetailTable(float wholeWidth, Map<String, Object> data) {
		List<EnrolmentDTO> enrolments = data.get(JaeConstants.PAYMENT_ENROLMENTS) != null ? (List<EnrolmentDTO>) data.get(JaeConstants.PAYMENT_ENROLMENTS) : new ArrayList<>();
		List<MaterialDTO> materials = data.get(JaeConstants.PAYMENT_MATERIALS) != null ? (List<MaterialDTO>) data.get(JaeConstants.PAYMENT_MATERIALS) : new ArrayList<>();
		List<OutstandingDTO> outstandings = data.get(JaeConstants.PAYMENT_OUTSTANDINGS) != null ? (List<OutstandingDTO>) data.get(JaeConstants.PAYMENT_OUTSTANDINGS) : new ArrayList<>();
		InvoiceDTO invoice = data.get(JaeConstants.INVOICE_INFO) != null ? (InvoiceDTO) data.get(JaeConstants.INVOICE_INFO) : new InvoiceDTO();
		Table detail = new Table(new float[]{(wholeWidth)*3/12, (wholeWidth)*3/12, (wholeWidth)/12, (wholeWidth)*2/12, (wholeWidth)/12, (wholeWidth)*2/12});
		detail.setBorder(new SolidBorder(1)); // Set the border to a solid line with a width of 2
		detail.setMarginTop(5);

		detail.addCell(new Cell(2, 0).add(tableHeaderCell("Title")).setVerticalAlignment(VerticalAlignment.MIDDLE));
		detail.addCell(new Cell(2, 0).add(tableHeaderCell("Period/Date")).setVerticalAlignment(VerticalAlignment.MIDDLE));
		detail.addCell(new Cell(0, 2).add(tableHeaderCell("Fee (Incl.GST)")).setVerticalAlignment(VerticalAlignment.MIDDLE));
		detail.addCell(new Cell(2, 0).add(tableHeaderCell("Discount")).setVerticalAlignment(VerticalAlignment.MIDDLE));
		detail.addCell(new Cell(2, 0).add(tableHeaderCell("Subtotal\n(Incl.GST)")).setVerticalAlignment(VerticalAlignment.MIDDLE));
		detail.addCell(new Cell(0, 0).add(tableHeaderCell("Weeks\n(Qty)")).setVerticalAlignment(VerticalAlignment.MIDDLE));
		detail.addCell(new Cell(0, 0).add(tableHeaderCell("Weekly Fee\n(Unit price)")).setVerticalAlignment(VerticalAlignment.MIDDLE));

		// data display
		double finalTotal = 0;
		// enrolements
		for(EnrolmentDTO enrolment : enrolments){
			detail.addCell(new Cell().add(boldCell("Class [" + JaeUtils.getGradeName(enrolment.getGrade()) + "] " + enrolment.getName())).setTextAlignment(TextAlignment.CENTER));
			detail.addCell(new Cell().add(boldCell(StringUtils.defaultString(enrolment.getExtra()))).setTextAlignment(TextAlignment.CENTER));
			detail.addCell(new Cell().add(boldCell(enrolment.getEndWeek()-enrolment.getStartWeek()+1+"")).setTextAlignment(TextAlignment.RIGHT));
			detail.addCell(new Cell().add(boldCell(String.format("%.2f", enrolment.getPrice()))).setTextAlignment(TextAlignment.RIGHT));
			String disStr = StringUtils.defaultString(enrolment.getDiscount(), "0");
			double discount = 0;
			if(StringUtils.contains(disStr, '%')){
				discount = Double.parseDouble(StringUtils.substringBefore(disStr, "%"));
				discount = ((enrolment.getEndWeek()-enrolment.getStartWeek()+1)-enrolment.getCredit())*enrolment.getPrice()*discount/100;
			}else{
				discount = Double.parseDouble(disStr);
			}
			detail.addCell(new Cell().add(boldCell(enrolment.getDiscount())).setTextAlignment(TextAlignment.RIGHT));
			double total = (enrolment.getEndWeek()-enrolment.getStartWeek()+1-enrolment.getCredit())*enrolment.getPrice() - discount;
			detail.addCell(new Cell().add(boldCell(String.format("%.2f", total))).setTextAlignment(TextAlignment.RIGHT));
			finalTotal += total;
		}
		// materials
		for(MaterialDTO material : materials){
			detail.addCell(new Cell().add(boldCell("Book " + material.getName())).setTextAlignment(TextAlignment.CENTER));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.CENTER));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT));
			detail.addCell(new Cell().add(boldCell(String.format("%.2f", material.getPrice()))).setTextAlignment(TextAlignment.RIGHT));	
			finalTotal += material.getPrice();
		}
		// outstandings
		for(OutstandingDTO outstanding : outstandings){
			detail.addCell(new Cell().add(boldCell("Payment")).setTextAlignment(TextAlignment.CENTER));
			detail.addCell(new Cell().add(boldCell(outstanding.getRegisterDate()+"")).setTextAlignment(TextAlignment.CENTER));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT));
			detail.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT));
			double paid = outstanding.getPaid();
			String paidAmount = (paid > 0.00) ? " - " + String.format("%.2f", paid) : "0.00";
			detail.addCell(new Cell().add(boldCell(paidAmount)).setTextAlignment(TextAlignment.RIGHT));
		}
		// note if invoice
		detail.addCell(new Cell(0, 6).add(boldCell("* Other Information : " + StringUtils.defaultString(invoice.getInfo()))).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(10));
		// return table & finalTotal
		return new Object[]{detail, finalTotal};
	}

	private Table getInvoicePaidTable(float wholeWidth, Map<String, Object> data, double finalTotal) {
		InvoiceDTO invoice = data.get(JaeConstants.INVOICE_INFO) != null ? (InvoiceDTO) data.get(JaeConstants.INVOICE_INFO) : new InvoiceDTO();
		Table paid = new Table(new float[]{wholeWidth/4, wholeWidth/4, wholeWidth/4, wholeWidth/4}).setBorder(Border.NO_BORDER);
		paid.addCell(new Cell().setBorder(Border.NO_BORDER));
		paid.addCell(new Cell().setBorder(Border.NO_BORDER));
		paid.addCell(new Cell().setBorder(Border.NO_BORDER));
		Table paidDetail = new Table(new float[]{(wholeWidth/4)*3/7, (wholeWidth/4)/7, (wholeWidth/4)*3/7}).setBorder(Border.NO_BORDER);
		paidDetail.addCell(paidCell("FINAL TOTAL").setBorder(Border.NO_BORDER));
		paidDetail.addCell(dollarCell().setBorder(Border.NO_BORDER));
		paidDetail.addCell(paidCell(String.format("%.2f", finalTotal)).setBorder(Border.NO_BORDER));
		paidDetail.addCell(paidCell("FEE PAID").setBorder(Border.NO_BORDER));
		paidDetail.addCell(dollarCell().setBorder(Border.NO_BORDER));
		double paidAmount = invoice.getPaidAmount();
		String paidAmtStr = (paidAmount > 0.00) ? String.format(" - %.2f", paidAmount) : "0.00";
		paidDetail.addCell(paidNoBoldCell(paidAmtStr).setBorder(Border.NO_BORDER));
		paidDetail.addCell(paidCell("BALANCE").setBorder(Border.NO_BORDER));
		paidDetail.addCell(dollarCell().setBorder(Border.NO_BORDER));
		String remaining = "";
		if(finalTotal - paidAmount <= 0){
			remaining = "PAID IN FULL";
		}else{
			remaining = String.format("%.2f", (finalTotal - paidAmount));
		}
		paidDetail.addCell(paidNoBoldCell(remaining).setBorder(Border.NO_BORDER));
		paid.addCell(new Cell().add(paidDetail).setBorder(Border.NO_BORDER));
		return paid;
	}

	private Table getBranchNoteTable(float wholeWidth, Map<String, Object> data) {
		BranchDTO branch = data.get(JaeConstants.INVOICE_BRANCH) != null ? (BranchDTO) data.get(JaeConstants.INVOICE_BRANCH) : new BranchDTO();
		Table note = new Table(new float[]{wholeWidth}).setBorder(new SolidBorder(0.5f));
		note.addCell(boldCell("Note :").setItalic().setFontSize(8f).setBold().setBorder(Border.NO_BORDER).setPaddingLeft(5));
		//String content = "1. Total payment dute in 14 days<br>2. Please make payment to the following account<br>3. Please use the invoice number as the reference when making payment<br>4. If you have any questions about this invoice, please contact Jin Seo on 03 9361 0051";
		String content = branch.getInfo();
		String[] contents = StringUtils.splitByWholeSeparator(content, "<br/>");
		if(contents != null){
			for(String msg : contents){
				note.addCell(new Cell().add(msg).setFontSize(8f).setBorder(Border.NO_BORDER).setPaddingLeft(10));
			}
		}
		return note;
	}

	private Cell boldTitleCell(String contents) {
		return new Cell().add(contents).setBold().setFontSize(7f);
	}

	private Cell boldCell(String contents) {
		return new Cell().add(contents).setBold().setFontSize(7.5f);
	}

	private Cell tableHeaderCell(String contents) {
		return new Cell().add(contents).setFontSize(7.5f).setTextAlignment(TextAlignment.CENTER);
	}

	private Cell dollarCell() {
		return new Cell().add("$").setFontSize(7f).setTextAlignment(TextAlignment.LEFT).setFontColor(Color.GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
	}

	private Cell paidCell(String contents) {
		return new Cell().add(contents).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER).setBold();
	}

	private Cell paidNoBoldCell(String contents) {
		return new Cell().add(contents).setFontSize(7f).setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
	}

	private Image imageLogo() throws URISyntaxException, MalformedURLException, IOException {
		Resource resource = resourceLoader.getResource("classpath:static/assets/image/logo_title.jpg");
		ImageData imageData = ImageDataFactory.create(resource.getFile().getAbsolutePath());
		Image img = new Image(imageData);
		img.setAutoScale(true);
		return img;
	}

}
