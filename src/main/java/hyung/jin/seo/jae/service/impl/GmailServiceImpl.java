package hyung.jin.seo.jae.service.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.service.EmailService;

@Service
@ConditionalOnProperty(name = "email.provider", havingValue = "gmail")
public class GmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderAddress;

    public GmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmailWithPdf(String from, String to, String cc, String bcc, String subject, String body,
            String fileName, byte[] pdfBytes) {
        String[] tos = StringUtils.isBlank(to) ? new String[] {} : new String[] { to };
        String[] ccs = StringUtils.isBlank(cc) ? null : new String[] { cc };
        String[] bccs = StringUtils.isBlank(bcc) ? null : new String[] { bcc };
        sendEmailWithPdf(from, tos, ccs, bccs, subject, body, fileName, pdfBytes);
    }

    @Override
    public void sendEmailWithPdf(String from, String[] to, String[] cc, String[] bcc, String subject, String body,
            String fileName, byte[] pdfBytes) {
        boolean hasAttachment = pdfBytes != null && pdfBytes.length > 0;
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, hasAttachment, "UTF-8");
            helper.setFrom(senderAddress, StringUtils.defaultIfBlank(from, senderAddress));
            if (to != null && to.length > 0) {
                helper.setTo(to);
            }
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
            }
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
            }
            helper.setSubject(subject);
            helper.setText(body, true);

            if (pdfBytes != null && pdfBytes.length > 0) {
                String safeFileName = StringUtils.defaultIfBlank(fileName, "attachment.pdf");
                helper.addAttachment(safeFileName, new ByteArrayResource(pdfBytes), "application/pdf");
            }

            mailSender.send(message);
            logger.info("Gmail sent. to={}, subject={}", to != null ? to.length : 0, subject);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Gmail send failed. to={}, subject={}", to != null ? to.length : 0, subject, e);
        }
    }
}
