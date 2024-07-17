package hyung.jin.seo.jae.service;

public interface EmailService {

	// send simple email
	void sendEmail(String from, String to, String subject, String body);

	// send simple email with attachment
	void sendEmailWithAttachment(String from, String to, String subject, String body, String fileName, byte[] pdfBytes);
}
