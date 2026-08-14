package hyung.jin.seo.jae.service.impl;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import hyung.jin.seo.jae.service.EmailService;
import io.micrometer.core.instrument.util.StringUtils;

@Service
@ConditionalOnProperty(name = "email.provider", havingValue = "sendgrid", matchIfMissing = true)
public class SendGridServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(SendGridServiceImpl.class);
	
	
	@Value("${spring.sender.assessment}")
    private String assessmentName;

	@Value("${spring.sender.invoice}")
    private String invoiceName;

    @Value("${email.api.key}")
	private String emailKey;

	@Value("${email.sender.address}")
	private String senderAddress;

	// Compose Mail for SendGrid
	private Mail composeMail(String from, String[] tos, String[] ccs, String[] bccs, String subject, String body ){
		Mail mail = new Mail();
		// set From
		Email fromEmail = new Email();
		fromEmail.setName(from);
		fromEmail.setEmail(senderAddress);
		mail.setFrom(fromEmail);
		mail.setSubject(subject);
		// Content content = new Content("text/plain", body);
		Content content = new Content("text/html", body);
		mail.addContent(content);
		Personalization p = new Personalization();
		// set To
		for(String to : tos){
			p.addTo(new Email(to));
		}
		// set CC
		if(ccs != null && ccs.length > 0){
			for(String cc : ccs){
				p.addCc(new Email(cc));
			}
		}
		// set BCC
		if(bccs != null && bccs.length > 0){
			for(String bcc : bccs){
				p.addBcc(new Email(bcc));
			}
		}
		mail.addPersonalization(p);
		return mail;
	}

    	// Compose Mail for SendGrid
	private Mail composeMail(String from, String to, String cc, String bcc, String subject, String body ){
		Mail mail = new Mail();
		// set From
		Email fromEmail = new Email();
		fromEmail.setName(from);
		fromEmail.setEmail(senderAddress);
		mail.setFrom(fromEmail);
		mail.setSubject(subject);
		// Content content = new Content("text/plain", body);
		Content content = new Content("text/html", body);
		mail.addContent(content);
		Personalization p = new Personalization();
		// set To
		p.addTo(new Email(to));
		// set CC
		if(StringUtils.isNotBlank(cc)){
			p.addCc(new Email(cc));
		}
		// set BCC
		if(StringUtils.isNotBlank(bcc)){
			p.addBcc(new Email(bcc));
		}
		mail.addPersonalization(p);
		return mail;
	}


    @Override
	public void sendEmailWithPdf(String from, String to, String cc, String bcc, String subject,
			String body, String fileName, byte[] pdfBytes) {
		// set contents
		Mail mail = composeMail(from, to, cc, bcc, subject, body);		
		// Add attachment if present
		if(pdfBytes != null && pdfBytes.length > 0) {
			try {
				Attachments attachments = new Attachments();
				String base64Content = Base64.getEncoder().encodeToString(pdfBytes);
				attachments.setContent(base64Content);
				attachments.setFilename(fileName);
				attachments.setType("application/pdf");
				attachments.setDisposition("attachment");
				mail.addAttachments(attachments);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// Send email using SendGrid
		SendGrid sendGrid = new SendGrid(emailKey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sendGrid.api(request);
			logger.info("SendGrid sent. to={}, subject={}", to != null ? 1 : 0, subject);
		} catch(IOException e) {
			logger.error("SendGrid send failed.", e);
		}
	}

    @Override
	public void sendEmailWithPdf(String from, String[] tos, String[] ccs, String[] bccs, String subject,
			String body, String fileName, byte[] pdfBytes) {
		// set contents
		Mail mail = composeMail(from, tos, ccs, bccs, subject, body);		
		// Add attachment if present
		if(pdfBytes != null && pdfBytes.length > 0) {
			try {
				Attachments attachments = new Attachments();
				String base64Content = Base64.getEncoder().encodeToString(pdfBytes);
				attachments.setContent(base64Content);
				attachments.setFilename(fileName);
				attachments.setType("application/pdf");
				attachments.setDisposition("attachment");
				mail.addAttachments(attachments);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// Send email using SendGrid
		SendGrid sendGrid = new SendGrid(emailKey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sendGrid.api(request);
			logger.info("SendGrid sent. to={}, subject={}", tos != null ? tos.length : 0, subject);
		} catch(IOException e) {
			logger.error("SendGrid send failed.", e);
		}
	}

}
