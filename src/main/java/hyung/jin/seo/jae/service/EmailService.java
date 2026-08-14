package hyung.jin.seo.jae.service;

public interface EmailService {

	// send report email
	// void emailReport(String to, String body, byte[] data) throws MessagingException;

	// send report email with attachment
	// void emailReport(String from, String to, String body, byte[] data) throws MessagingException;

	// send combined email with attachment to multiple recipients with bcc
	void sendEmailWithPdf(String from, String to, String cc, String bcc, String subject, String body, String fileName, byte[] pdfBytes);

	// send combined email with attachment to multiple recipients with bcc
	void sendEmailWithPdf(String from, String[] to, String[] cc, String[] bcc, String subject, String body, String fileName, byte[] pdfBytes);

}
