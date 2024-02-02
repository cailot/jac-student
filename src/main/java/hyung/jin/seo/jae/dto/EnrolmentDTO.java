package hyung.jin.seo.jae.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.Enrolment;
import hyung.jin.seo.jae.utils.JaeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EnrolmentDTO extends MoneyDTO{
    
	// private String id;

	// private String registerDate;

	private boolean cancelled;

	private String cancellationReason;

	private int startWeek;

	private int endWeek;

	private int credit;

	private String discount;

	// private double amount;

	private double paid;

	private String paymentDate;

	private String studentId;

	private String clazzId;

	private String invoiceId;

	private String name;

	private double price;

	private String grade;

	private String year;

	private String day;

	private String extra;

	private boolean online;

	// private String info;

	public EnrolmentDTO(Enrolment enrol){
		this.id = String.valueOf(enrol.getId());
		this.registerDate = enrol.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.cancelled = enrol.isCancelled();
		this.cancellationReason = enrol.getCancellationReason();
		this.startWeek = enrol.getStartWeek();
		this.endWeek = enrol.getEndWeek();
		this.credit = enrol.getCredit();
		this.discount = enrol.getDiscount();
		this.studentId = (enrol.getStudent()!=null) ? String.valueOf(enrol.getStudent().getId()) : "";
		this.clazzId = (enrol.getClazz()!=null) ? String.valueOf(enrol.getClazz().getId()) : "";
		this.invoiceId = (enrol.getInvoice()!=null) ? String.valueOf(enrol.getInvoice().getId()) : "";
		this.info = (enrol.getInfo()!=null) ? enrol.getInfo() : "";
	}

	public Enrolment convertToEnrolment() {
    	Enrolment enrolement = new Enrolment();
		if(StringUtils.isNotBlank(id)) enrolement.setId(Long.parseLong(id));
    	if(StringUtils.isNotBlank(registerDate)) enrolement.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		enrolement.setCancelled(cancelled);
		enrolement.setStartWeek(startWeek);
		enrolement.setEndWeek(endWeek);
		enrolement.setCredit(credit);
		enrolement.setDiscount(discount);
		if(StringUtils.isNotBlank(paymentDate)) enrolement.setRegisterDate(LocalDate.parse(paymentDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		if(StringUtils.isNotBlank(cancellationReason)) enrolement.setCancellationReason(cancellationReason);
		enrolement.setInfo(info);		
    	return enrolement;
    }

	public EnrolmentDTO(long id, LocalDate registerDate, boolean cancelled, String cancellationReason, int startWeek, int endWeek, String info, int credit, String discount, long invoiceId, double amount, double paid, LocalDate payDate, long studentId, long clazzId, String name, double price, boolean online, int year, String grade, String day){
		this.id = String.valueOf(id);
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.cancelled = cancelled;
		this.cancellationReason = cancellationReason;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.info = info;
		this.credit = credit;
		this.amount	= amount;
		this.paid = paid;
		this.discount = discount;
		this.invoiceId = String.valueOf(invoiceId);
		this.paymentDate = (payDate != null) ? payDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.studentId = String.valueOf(studentId);
		this.clazzId = String.valueOf(clazzId);
		this.name = name;
		this.price = price;
		this.online = online;
		this.year = String.valueOf(year);
		this.grade = grade;
		this.day = day;
	}

	public EnrolmentDTO(long id, LocalDate registerDate, boolean cancelled, String cancellationReason, int startWeek, int endWeek, String info, long studentId, long clazzId, String name, double price, boolean online, int year, String grade, String day){
		this.id = String.valueOf(id);
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.cancelled = cancelled;
		this.cancellationReason = cancellationReason;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.info = info;
		// this.credit = credit;
		// this.amount	= amount;
		// this.paid = paid;
		// this.discount = discount;
		this.studentId = String.valueOf(studentId);
		this.clazzId = String.valueOf(clazzId);
		this.name = name;
		this.price = price;
		this.online = online;
		this.year = String.valueOf(year);
		this.grade = grade;
		this.day = day;
	}



	public EnrolmentDTO(Object[] obj){
		this.id = (obj[0]!=null) ? String.valueOf(obj[0]) : null;
		this.registerDate = (obj[1]!=null) ? String.valueOf(obj[1]) : null;
		this.cancelled = (obj[2]!=null) ? (boolean)obj[2] : false;
		this.cancellationReason = (obj[3]!=null) ? (String)obj[3] : null;
		this.startWeek = (obj[4]!=null) ? (int)obj[4] : 0;
		this.endWeek = (obj[5]!=null) ? (int)obj[5] : 0;
		this.info = (obj[6]!=null) ? String.valueOf(obj[6]) : "";				
		this.invoiceId = (obj[7]!=null) ? String.valueOf(obj[7]) : "0";
		this.credit = (obj[8]!=null) ? Integer.parseInt(String.valueOf(obj[8])) : 0;
		this.discount = (obj[9]!=null) ? String.valueOf(obj[9]) : "0";
		this.amount = (obj[10]!=null) ? Double.parseDouble(String.valueOf(obj[10])) : 0;
		this.paid = (obj[11]!=null) ? Double.parseDouble(String.valueOf(obj[11])) : 0;
		this.paymentDate = (obj[12] != null) ? JaeUtils.dateFormat.format(obj[12]) : null;
		this.studentId = (obj[13]!=null) ? String.valueOf(obj[13]) : "0";
		this.clazzId = (obj[14]!=null) ? String.valueOf(obj[14]) : "0";
		this.name = (obj[15]!=null) ? (String)obj[15] : null;
		this.price = (obj[16]!=null) ? Double.parseDouble(String.valueOf(obj[16])) : 0;
		this.online = (obj[17]!=null) ? (boolean)obj[17] : false;
		this.year = (obj[18]!=null) ? String.valueOf(obj[18]) : null;
		this.grade = (obj[19]!=null) ? (String)obj[19] : null;
		this.day = (obj[20]!=null) ? (String)obj[20] : null;
	}
}
