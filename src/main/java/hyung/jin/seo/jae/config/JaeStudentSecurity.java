package hyung.jin.seo.jae.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
                //auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance()); 
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/assets/css/**","/assets/js/**","/assets/fonts/**","/assets/images/**"); // excluding folders list
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
		// disable CSRF protection
		http.csrf().disable();
        http
                .authorizeRequests(requests -> requests
                       .antMatchers("/", "/online/static/**", "/online/login").permitAll())
                .formLogin(login -> login
                        .loginPage("/online/login") // login page link
                        .loginProcessingUrl("/online/processLogin")
                        .defaultSuccessUrl("/online/lesson")// redirect link after login
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/online/login")// redirect url after logout
                        .invalidateHttpSession(true)// make session unavailable
                        .permitAll());
		
	}

        @Bean
        public PasswordEncoder getPasswordEncoder(){
                return new BCryptPasswordEncoder();
        }

}
