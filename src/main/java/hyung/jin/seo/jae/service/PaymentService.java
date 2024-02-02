package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.PaymentDTO;
import hyung.jin.seo.jae.model.Payment;

public interface PaymentService {
	
	// list all Payment
	List<PaymentDTO> allPayments();
	
	// get total number of payment
 	long checkCount();

	// get Payment by Id
    PaymentDTO getPayment(Long id);

	// get Payment by invoice Id
	List<PaymentDTO> getPaymentByInvoice(Long invoiceId);

	// add Payment
	Payment addPayment(Payment payment);

	// update Payment
	PaymentDTO updatePayment(Payment payment, Long id);

	// find Payment by Id
	Payment findPaymentById(Long id);

	// get Invoice Id by Payment
	Long getInvoiceIdByPayment(Long id);
}
