package hyung.jin.seo.jae.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class JaeLoginFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        // Capture the Referer header
        String referer = httpRequest.getHeader("Referer");

        // Store it in the session
        if (referer != null) {
            session.setAttribute("referer", referer);
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
   
}
