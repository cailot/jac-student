package hyung.jin.seo.jae.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 도메인별 리다이렉트를 처리하는 Controller
 * jacconnectedclass.com.au -> /connected/login
 * jacelearning.com.au -> /online/login
 */
@Controller
@RequestMapping("/")
public class DomainRedirectController {

    private static final Logger logger = LoggerFactory.getLogger(DomainRedirectController.class);

    /**
     * 루트 경로(/)로 접속 시 도메인에 따라 리다이렉트
     */
    @GetMapping("/")
    public String redirectByDomain(HttpServletRequest request) {
        String host = request.getHeader("Host");
        logger.info("Domain redirect requested. Host: {}", host);
        
        if (host != null) {
            // www 제거 및 포트 번호 제거
            String cleanHost = host.toLowerCase();
            if (cleanHost.startsWith("www.")) {
                cleanHost = cleanHost.substring(4);
            }
            if (cleanHost.contains(":")) {
                cleanHost = cleanHost.substring(0, cleanHost.indexOf(":"));
            }
            
            logger.info("Clean host: {}", cleanHost);
            
            // jacconnectedclass.com.au로 접속한 경우
            if ("jacconnectedclass.com.au".equals(cleanHost)) {
                logger.info("Redirecting to /connected/login for jacconnectedclass.com.au");
                return "redirect:/connected/login";
            }
            // jacconnectedclass.com으로 접속한 경우
            else if ("jacconnectedclass.com".equals(cleanHost)) {
                logger.info("Redirecting to /connected/login for jacconnectedclass.com");
                return "redirect:/connected/login";
            }
            // jacelearning.com.au로 접속한 경우
            else if ("jacelearning.com.au".equals(cleanHost)) {
                logger.info("Redirecting to /online/login for jacelearning.com.au");
                return "redirect:/online/login";
            }
            // jacelearning.com으로 접속한 경우
            else if ("jacelearning.com".equals(cleanHost)) {
                logger.info("Redirecting to /online/login for jacelearning.com");
                return "redirect:/online/login";
            }
        }
        
        // 기본 도메인(jac-study.azurewebsites.net) 또는 기타 도메인인 경우
        // 기본 페이지로 이동하거나 기존 로직 유지
        logger.info("Using default redirect to /connected/login");
        return "redirect:/connected/login"; // 기본값으로 connected/login으로 이동
    }
}
