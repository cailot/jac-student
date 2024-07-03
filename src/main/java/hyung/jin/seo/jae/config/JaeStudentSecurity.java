package hyung.jin.seo.jae.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class JaeStudentSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
    
        @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
                        "/assets/css/**",
                        "/assets/js/**",
                        "/assets/fonts/**",
                        "/assets/images/**",
                        "/js/**",
                        "/fonts/**",
                        "/css/**",
                        "/image/**"
                        ); // excluding folders list
	}

        @Configuration
        @Order(1)
        public static class OnlineSecurityConfig extends WebSecurityConfigurerAdapter {
                @Override
                protected void configure(HttpSecurity http) throws Exception {
                        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
                        // disable CSRF protection
                        http.csrf().disable();
                        http
                                .antMatcher("/online/**")
                                .authorizeRequests(requests -> requests
                                .antMatchers("/online/lesson").authenticated() // Secure /online/lesson
                                .antMatchers("/online/login").permitAll())
                                .formLogin(login -> login
                                        .loginPage("/online/login") // login page link
                                        .loginProcessingUrl("/online/processLogin")
                                        .defaultSuccessUrl("/online/lesson") // redirect link after login
                                        .permitAll())
                                .logout(logout -> logout
                                        .logoutUrl("/online/logout") // specify logout URL
                                        .logoutSuccessUrl("/online/login") // redirect url after logout
                                        .invalidateHttpSession(true) // make session unavailable
                                        .permitAll());
                }
        }

        @Configuration
        @Order(2)
        public static class ConnectedSecurityConfig extends WebSecurityConfigurerAdapter {
                @Override
                protected void configure(HttpSecurity http) throws Exception {
                        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
                        // disable CSRF protection
                        http.csrf().disable();
                        http
                                .antMatcher("/connected/**")
                                .authorizeRequests(requests -> requests
                                // .antMatchers("/connected/", "/connected/login").permitAll())
                                // .antMatchers("/connected/lesson").authenticated() // Secure /online/lesson
                                .antMatchers("/connected/**").authenticated() // Secure all /connected/* paths
                                .antMatchers("/connected/login").permitAll())
                                .formLogin(login -> login
                                        .loginPage("/connected/login") // login page link
                                        .loginProcessingUrl("/connected/processLogin")
                                        .defaultSuccessUrl("/connected/lesson")// redirect link after login
                                        .permitAll())
                                .logout(logout -> logout
                                        .logoutUrl("/connected/logout") // specify logout URL
                                        .logoutSuccessUrl("/connected/login")// redirect url after logout
                                        .invalidateHttpSession(true)// make session unavailable
                                        .permitAll());
                        
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
        public PasswordEncoder getPasswordEncoder(){
                return new BCryptPasswordEncoder();
        }

}