package hyung.jin.seo.jae.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Qualifier;
import hyung.jin.seo.jae.service.StudentAccountService;
import hyung.jin.seo.jae.security.LegacyAwarePasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class JaeStudentSecurity {

        @Bean
        public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
                StrictHttpFirewall firewall = new StrictHttpFirewall();
                firewall.setAllowSemicolon(true); // 세미콜론 허용
                firewall.setAllowUrlEncodedSlash(true); // URL 인코딩된 슬래시 허용
                firewall.setAllowBackSlash(true); // 백슬래시 허용
                firewall.setAllowUrlEncodedDoubleSlash(true); // URL 인코딩된 더블 슬래시 허용
                return firewall;
        }

        @Configuration
        @Order(1)
        public static class OnlineSecurityConfig extends WebSecurityConfigurerAdapter {
                @Autowired
                private JaeLoginFilter jaeLoginFilter;

                @Autowired
                private StudentAccountService studentAccountService;

                @Autowired
                private PasswordEncoder passwordEncoder;

                @Override
                protected void configure(HttpSecurity http) throws Exception {
                        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
                        // disable CSRF protection
                        http.csrf().disable();
                        http
                                .antMatcher("/online/**")
                                .authorizeRequests(requests -> requests
                                .antMatchers("/online/lesson").authenticated() // Secure /online/lesson
                                .antMatchers("/online/login").permitAll()
                                .antMatchers("/online/urlLoginEncrypted").permitAll()) // Allow encrypted URL-based login
                                .formLogin(login -> login
                                        .loginPage("/online/login") // login page link
                                        .loginProcessingUrl("/online/processLogin")
                                        .defaultSuccessUrl("/online/lesson") // redirect link after login
                                        .failureHandler(JaeStudentSecurity.customAuthenticationFailureHandler())
                                        .permitAll())
                                .logout(logout -> logout
                                        .logoutUrl("/online/logout") // specify logout URL
                                        .logoutSuccessUrl("/online/login") // redirect url after logout
                                        .invalidateHttpSession(true) // make session unavailable
                                        .permitAll())
                                .sessionManagement(session -> session
                                        .maximumSessions(2)
                                        .maxSessionsPreventsLogin(false)
                                        .and()
                                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                        .invalidSessionUrl("/online/login?expired=true")
                                        .sessionFixation().migrateSession());
                        // Add the custom filter before the UsernamePasswordAuthenticationFilter                
                        http.addFilterBefore(jaeLoginFilter, UsernamePasswordAuthenticationFilter.class);
                }

                @Override
                protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                        auth.userDetailsService(studentAccountService).passwordEncoder(passwordEncoder);
                }
        }

        @Configuration
        @Order(2)
        public static class ConnectedSecurityConfig extends WebSecurityConfigurerAdapter {
                @Autowired
                private JaeLoginFilter jaeLoginFilter;

                @Autowired
                private StudentAccountService studentAccountService;

                @Autowired
                private PasswordEncoder passwordEncoder;

                @Override
                protected void configure(HttpSecurity http) throws Exception {
                        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
                        // disable CSRF protection
                        http.csrf().disable();
                        http
                                .antMatcher("/connected/**")
                                .authorizeRequests(requests -> requests
                                .antMatchers("/connected/login").permitAll()
                                .antMatchers("/connected/urlLoginEncrypted").permitAll() // Allow encrypted URL-based login
                                .antMatchers("/connected/**").authenticated()) // Secure all /connected/* paths
                                .formLogin(login -> login
                                        .loginPage("/connected/login") // login page link
                                        .loginProcessingUrl("/connected/processLogin")
                                        .defaultSuccessUrl("/connected/lesson")// redirect link after login
                                        .failureHandler(JaeStudentSecurity.customAuthenticationFailureHandler())
                                        .permitAll())
                                .logout(logout -> logout
                                        .logoutUrl("/connected/logout") // specify logout URL
                                        .logoutSuccessUrl("/connected/login")// redirect url after logout
                                        .invalidateHttpSession(true)// make session unavailable
                                        .permitAll())
                                .sessionManagement(session -> session
                                        .maximumSessions(2)
                                        .maxSessionsPreventsLogin(false)
                                        .and()
                                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                        .invalidSessionUrl("/connected/login?expired=true")
                                        .sessionFixation().migrateSession());
                        // Add the custom filter before the UsernamePasswordAuthenticationFilter
                        http.addFilterBefore(jaeLoginFilter, UsernamePasswordAuthenticationFilter.class);
                        
                }

                @Override
                protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                        auth.userDetailsService(studentAccountService).passwordEncoder(passwordEncoder);
                }
        }

        @Configuration
        @Order(3)
        public static class AssessSecurityConfig extends WebSecurityConfigurerAdapter {
                @Override
                protected void configure(HttpSecurity http) throws Exception {

                        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
                        // disable CSRF protection
                        http.csrf().disable();
                        http
                                .antMatcher("/assessment/**")
                                .authorizeRequests(requests -> requests
                                .antMatchers("/assessment/test").permitAll());     
                }
        }
        
        @Bean
        public static AuthenticationFailureHandler customAuthenticationFailureHandler() {
                return new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
                                
                                String errorParam;
                                String message = exception.getMessage();
                                
                                // Check if it's an InternalAuthenticationServiceException wrapping a DisabledException
                                if (exception instanceof InternalAuthenticationServiceException) {
                                        Throwable cause = exception.getCause();
                                        if (cause instanceof DisabledException) {
                                                message = cause.getMessage();
                                        }
                                }
                                
                                if (exception instanceof DisabledException || 
                                    (exception instanceof InternalAuthenticationServiceException && 
                                     exception.getCause() instanceof DisabledException)) {
                                        
                                        if (message != null && message.contains("Payment not completed")) {
                                                errorParam = "payment";
                                        } else if (message != null && message.contains("Enrolment is not valid")) {
                                                errorParam = "enrolment";
                                        } else {
                                                errorParam = "disabled";
                                        }
                                } else {
                                        errorParam = "credentials";
                                }
                                
                                String loginUrl = request.getRequestURI().replace("/processLogin", "/login");
                                String redirectUrl = loginUrl + "?error=" + errorParam;
                                
                                response.sendRedirect(redirectUrl);
                        }
                };
        }
        
        @Bean
        public PasswordEncoder getPasswordEncoder(){
                return new LegacyAwarePasswordEncoder();
        }

}