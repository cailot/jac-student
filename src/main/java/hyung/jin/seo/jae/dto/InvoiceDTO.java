package hyung.jin.seo.jae.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.Invoice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InvoiceDTO extends MoneyDTO{
    
	// private String id;

	// private String registerDate;

	private String paymentDate;

	private int credit;

	private double discount;

	// private double amount;

	private double paidAmount;

	private String enrolmentId;

	// private String info;


	public InvoiceDTO(Invoice invoice){
		this.id = String.valueOf(invoice.getId());
		this.registerDate = invoice.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		if(invoice.getPaymentDate() != null) {
			this.paymentDate = invoice.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
		this.credit = invoice.getCredit();
		this.discount = invoice.getDiscount();
		this.amount = invoice.getAmount();
		this.paidAmount = invoice.getPaidAmount();
		this.info = invoice.getInfo();
	}

	public Invoice convertToOnlyInvoice() {
    	Invoice invoice = new Invoice();
		if(StringUtils.isNotBlank(id)) invoice.setId(Long.parseLong(id));
    	if(StringUtils.isNotBlank(registerDate)) invoice.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		if(StringUtils.isNotBlank(paymentDate)) invoice.setPaymentDate(LocalDate.parse(paymentDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		invoice.setCredit(credit);
		invoice.setDiscount(discount);
		invoice.setAmount(amount);
		invoice.setPaidAmount(paidAmount);
		invoice.setInfo(info);
		return invoice;
    }

	public InvoiceDTO(long id, int credit, double discount, double paidAmount, double amount, LocalDate registerDate, LocalDate payCompleteDate, String info){
		this.id = String.valueOf(id);
		this.credit = credit;
		this.discount = discount;
		this.paidAmount = paidAmount;
		this.amount = amount;
		this.registerDate = (registerDate!=null) ? registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.paymentDate = (payCompleteDate!=null) ? payCompleteDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.info = info;
	}

}
