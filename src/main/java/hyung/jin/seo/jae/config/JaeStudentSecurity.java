package hyung.jin.seo.jae.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class JaeStudentSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
    
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails jin = User.builder()
            .username("jin")
            .password("{noop}123")
            .authorities(Collections.emptyList())
            .build();
        return new InMemoryUserDetailsManager(jin);    
    }

    // @Override
	// protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// 	auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	// }
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/assets/css/**","/assets/js/**","/assets/fonts/**","/assets/images/**"); // excluding folders list
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

        http.headers(headers -> headers.frameOptions().sameOrigin());// allow iframe to embed PDF in body
        
        http
                .authorizeRequests(requests -> requests
                        // .antMatchers("/dashboard").hasAnyRole(allRoles)
                        // .antMatchers("/summary").hasAnyRole(allRoles)
                        // .antMatchers("/detail").hasAnyRole(allRoles)
                        // //.antMatchers("/detail").hasAnyRole(noViewerRoles)
                        // .antMatchers("/audit").hasAnyRole(allRoles)
                        // .antMatchers("/user").hasAnyRole(allRoles)
                        // .antMatchers("/document").hasAnyRole(allRoles)
                        .antMatchers("/", "static/**", "/login").permitAll())
                .formLogin(login -> login
                        .loginPage("/login") // login page link
                        .loginProcessingUrl("/processLogin")
                        .defaultSuccessUrl("/online")// redirect link after login
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")// redirect url after logout
                        .invalidateHttpSession(true)// make session unavailable
                        .permitAll());
		
	}

}
