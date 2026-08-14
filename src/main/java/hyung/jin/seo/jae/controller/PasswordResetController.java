package hyung.jin.seo.jae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.service.EmailService;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.repository.StudentRepository;
import hyung.jin.seo.jae.utils.JaeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.core.io.ClassPathResource;

@Controller
@RequestMapping("/password-reset")
public class PasswordResetController {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CodeService codeService;

    @Autowired
    private StudentRepository studentRepository;
    
    // admin email no longer used; notifications go to student's branch
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/request")
    @ResponseBody
    @Transactional
    public Map<String, Object> requestPasswordReset(@RequestParam("email") String email,
                                                   @RequestParam("studentId") String studentId,
                                                   @RequestParam("fullName") String fullName) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. 입력값 검증
            if (email == null || email.trim().isEmpty() || 
                studentId == null || studentId.trim().isEmpty() || 
                fullName == null || fullName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "All fields are required.");
                return response;
            }
            
            // 2. Student ID를 Long으로 변환
            Long studentIdLong;
            try {
                studentIdLong = Long.parseLong(studentId.trim());
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "Invalid Student ID format.");
                return response;
            }
            
            // 3. 학생 정보 검증
            Student student = studentRepository.findActiveStudentById(studentIdLong);
            if (student == null) {
                response.put("success", false);
                response.put("message", "Student ID not found or student is inactive.");
                return response;
            }
            
            // 4. 이메일 검증 (email1 또는 email2와 일치하는지 확인)
            String studentEmail1 = student.getEmail1();
            String studentEmail2 = student.getEmail2();
            
            if ((studentEmail1 == null || !studentEmail1.trim().equalsIgnoreCase(email.trim())) &&
                (studentEmail2 == null || !studentEmail2.trim().equalsIgnoreCase(email.trim()))) {
                response.put("success", false);
                response.put("message", "Email address does not match the student record.");
                return response;
            }
            
            // 5. 모든 검증 통과 - 비밀번호 재설정 및 이메일 전송
            String newPassword = generateSecurePassword();
            
            // Encode with bcrypt and prefix so DelegatingPasswordEncoder can verify
            String encoded = passwordEncoder.encode(newPassword);
            // If encoder doesn't add {id} prefix automatically, add it
            if (!encoded.startsWith("{") ) {
                encoded = "{bcrypt}" + encoded;
            }
            studentRepository.updateStudentPassword(studentIdLong, encoded);
            
            // 소속 브랜치로 알림 이메일 전송
            String branchCode = student.getBranch();
            String branchEmail = codeService.getBranchEmail(branchCode);
            String adminSubject = "Password Reset Completed - " + fullName;
            String adminEmailBody = createAdminNotificationTemplate(fullName, studentId, email, student, newPassword);
            if(branchEmail != null && !branchEmail.trim().isEmpty()){
                emailService.sendEmailWithPdf(
                    "JAC Study System",
                    branchEmail,
                    null,
                    null,
                    adminSubject,
                    adminEmailBody,
                    null,
                    null
                );
            }
            
            // 학생에게 새 비밀번호 이메일 전송
            String studentSubject = "Your Password Has Been Reset - JAC Study System";
            String studentEmailBody = createStudentPasswordTemplate(fullName, studentId, newPassword);
            
            emailService.sendEmailWithPdf(
                "JAC Study System",
                email,
                null,
                null,
                studentSubject,
                studentEmailBody,
                null,
                null
            );
            
            response.put("success", true);
            response.put("message", "Your password has been reset successfully. Please check your email for the new password.");
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred while processing your request.");
            return response;
        }
    }
    
    // 안전한 비밀번호 생성
    private String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        // 고정 8자
        int length = 8;
        
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
    
    // 관리자 알림 이메일 템플릿
    private String createAdminNotificationTemplate(String fullName, String studentId, String email, Student student, String newPassword) {
        StringBuilder template = new StringBuilder();
        String gradeName = JaeUtils.getGradeName(student.getGrade() != null ? student.getGrade() : "");
        String branchName = JaeUtils.getBranchName(student.getBranch() != null ? student.getBranch() : "");
        template.append("<html>")
               .append("<head>")
               .append("<style>")
               .append("body { font-family: Arial, sans-serif; font-size: 14px; line-height: 1.6; color: #333; }")
               .append(".header { background-color: #28a745; color: white; padding: 20px; text-align: center; }")
               .append(".content { padding: 20px; background-color: #f9f9f9; }")
               .append(".info-box { background-color: white; border: 1px solid #ddd; border-radius: 5px; padding: 15px; margin: 15px 0; }")
               .append(".info-row { margin: 10px 0; }")
               .append(".label { font-weight: bold; color: #28a745; }")
               .append(".password-box { background-color: #d4edda; border: 1px solid #c3e6cb; border-radius: 5px; padding: 15px; margin: 15px 0; }")
               .append(".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }")
               .append("</style>")
               .append("</head>")
               .append("<body>")
               .append("<div class='header'>")
               .append("<h2>Password Reset Completed</h2>")
               .append("<p>JAC Study System</p>")
               .append("</div>")
               .append("<div class='content'>")
               .append("<p>This is only for your information. A student's password has been automatically reset by the system.</p>")
               .append("<div class='info-box'>")
               .append("<div class='info-row'><span class='label'>Student Name:</span> ").append(fullName).append("</div>")
               .append("<div class='info-row'><span class='label'>Student ID:</span> ").append(studentId).append("</div>")
               .append("<div class='info-row'><span class='label'>Email Address:</span> ").append(email).append("</div>")
               .append("<div class='info-row'><span class='label'>Grade:</span> ").append(gradeName).append("</div>")
               .append("<div class='info-row'><span class='label'>Branch:</span> ").append(branchName).append("</div>")
               .append("<div class='info-row'><span class='label'>Reset Date:</span> ").append(new java.util.Date()).append("</div>")
               .append("</div>")
               .append("<div class='password-box'>")
               .append("<p><strong>New Password Generated:</strong></p>")
               .append("<p style='font-family: monospace; font-size: 16px; background: white; padding: 10px; border-radius: 3px;'>").append(newPassword).append("</p>")
               .append("</div>")
               .append("<p><strong>Action Taken:</strong></p>")
               .append("<ul>")
               .append("<li>Student information verified</li>")
               .append("<li>New password generated automatically</li>")
               .append("<li>Password updated in system</li>")
               .append("<li>New password sent to student</li>")
               .append("</ul>")
               .append("<p><strong>Note:</strong> This is an automated notification. No action required from administrator.</p>")
               .append("</div>")
               .append("<div class='footer'>")
               .append("<p>© 2015 - ").append(java.time.Year.now().getValue()).append(" James An College. All rights reserved.</p>")
               .append("</div>")
               .append("</body>")
               .append("</html>");
        
        return template.toString();
    }
    
    // 학생 비밀번호 이메일 템플릿
    // 이미지를 Base64로 변환
    private String getBase64Image(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource("static/" + imagePath);
            byte[] imageBytes = Files.readAllBytes(Paths.get(resource.getURI()));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String createStudentPasswordTemplate(String fullName, String studentId, String newPassword) {
        StringBuilder template = new StringBuilder();
        template.append("<html>")
               .append("<head>")
               .append("<style>")
               .append("body { font-family: Arial, sans-serif; font-size: 14px; line-height: 1.6; color: #333; }")
               .append(".header { background-color: #2d398e; color: white; padding: 20px; text-align: center; }")
               .append(".content { padding: 20px; background-color: #f9f9f9; }")
               .append(".password-box { background-color: #e3f2fd; border: 1px solid #2196f3; border-radius: 5px; padding: 20px; margin: 20px 0; text-align: center; }")
               .append(".new-password { font-family: monospace; font-size: 18px; background: white; padding: 15px; border-radius: 5px; border: 2px dashed #2196f3; margin: 15px 0; }")
               .append(".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }")
               .append("</style>")
               .append("</head>")
               .append("<body>")
               .append("<div class='header'>")
               .append("<h2>Password Reset Complete</h2>")
               .append("<p>JAC Study System</p>")
               .append("</div>")
               .append("<div class='content'>")
               .append("<p>Dear <strong>").append(fullName).append("</strong>,</p>")
               .append("<p>Your password has been successfully reset. Here are your new login credentials:</p>")
               .append("<div class='password-box'>")
               .append("<p><strong>Student ID:</strong> ").append(studentId).append("</p>")
               .append("<p><strong>New Password:</strong></p>")
               .append("<div class='new-password'>").append(newPassword).append("</div>")
               .append("</div>")
               .append("<p><strong>Important:</strong></p>")
               .append("<ul>")
               .append("<li>Please save this password in a secure location</li>")
               .append("<li>You can now log in to the JAC Study System</li>")
               .append("<li>For security reasons, please change your password after first login</li>")
               .append("</ul>")
               .append("<div style='background-color: #fff3cd; border: 1px solid #ffeeba; border-radius: 5px; padding: 20px; margin: 20px 0;'>")
               .append("<h6 style='color: #856404; margin-bottom: 15px;'><i class='bi bi-shield-lock'></i> Password Reset Guide</h6>")
               .append("<p style='margin-bottom: 15px;'><strong>Please follow these steps to change your password after login:</strong></p>")
               .append("<div style='text-align: center; margin-bottom: 15px;'>")
               .append("<img src='data:image/png;base64,").append(getBase64Image("assets/image/reset-guide.png")).append("' alt='Password Reset Guide' style='max-width: 100%; border: 1px solid #ddd; border-radius: 5px;'>")
               .append("</div>")
               .append("<ol style='margin-bottom: 0;'>")
               .append("<li>Click your name on the top banner</li>")
               .append("<li>This will pop up the Password Reset dialogue</li>")
               .append("<li>Input your new password in the \"New Password\" field</li>")
               .append("<li>Confirm your password in the \"Confirm Password\" field</li>")
               .append("<li>Click the \"Update Password\" button</li>")
               .append("</ol>")
               .append("</div>")
               .append("<p>If you did not request this password reset, please contact the administrator immediately.</p>")
               .append("<p>Best regards,<br>JAC Study System Team</p>")
               .append("</div>")
               .append("<div class='footer'>")
               .append("<p>© 2015 - ").append(java.time.Year.now().getValue()).append(" James An College. All rights reserved.</p>")
               .append("</div>")
               .append("</body>")
               .append("</html>");
        
        return template.toString();
    }
    
    private String createPasswordResetEmailTemplate(String fullName, String studentId, String email, Student student) {
        StringBuilder template = new StringBuilder();
        template.append("<html>")
               .append("<head>")
               .append("<style>")
               .append("body { font-family: Arial, sans-serif; font-size: 14px; line-height: 1.6; color: #333; }")
               .append(".header { background-color: #2d398e; color: white; padding: 20px; text-align: center; }")
               .append(".content { padding: 20px; background-color: #f9f9f9; }")
               .append(".info-box { background-color: white; border: 1px solid #ddd; border-radius: 5px; padding: 15px; margin: 15px 0; }")
               .append(".info-row { margin: 10px 0; }")
               .append(".label { font-weight: bold; color: #2d398e; }")
               .append(".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }")
               .append("</style>")
               .append("</head>")
               .append("<body>")
               .append("<div class='header'>")
               .append("<h2>Password Reset Request</h2>")
               .append("<p>JAC System</p>")
               .append("</div>")
               .append("<div class='content'>")
               .append("<p>A student has requested a password reset for their account. <strong>All information has been verified against the database.</strong></p>")
               .append("<div class='info-box'>")
               .append("<div class='info-row'><span class='label'>Student Name:</span> ").append(fullName).append("</div>")
               .append("<div class='info-row'><span class='label'>Student ID:</span> ").append(studentId).append("</div>")
               .append("<div class='info-row'><span class='label'>Email Address:</span> ").append(email).append("</div>")
               .append("<div class='info-row'><span class='label'>Grade:</span> ").append(student.getGrade()).append("</div>")
               .append("<div class='info-row'><span class='label'>Branch:</span> ").append(student.getBranch()).append("</div>")
               .append("<div class='info-row'><span class='label'>Request Date:</span> ").append(new java.util.Date()).append("</div>")
               .append("</div>")
               .append("<p><strong>Verification Status:</strong> VERIFIED</p>")
               .append("<ul>")
               .append("<li> Student ID exists and is active</li>")
               .append("<li> Email address matches student record</li>")
               .append("</ul>")
               .append("<p><strong>Action Required:</strong></p>")
               .append("<ol>")
               .append("<li>Reset the student's password in the system</li>")
               .append("<li>Send the new password to the student via email or SMS</li>")
               .append("</ol>")
               .append("<p><strong>Note:</strong> This is an automated message. Please do not reply to this email.</p>")
               .append("</div>")
               .append("<div class='footer'>")
               .append("<p>© 2015 - ").append(java.time.Year.now().getValue()).append(" James An College. All rights reserved.</p>")
               .append("</div>")
               .append("</body>")
               .append("</html>");
        
        return template.toString();
    }
}
