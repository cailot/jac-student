package hyung.jin.seo.jae.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentAccount implements UserDetails{
        
    private String username;
    
    private String password;
    
    private String firstName;
    
    private String lastName;

	private int enabled;

	private List<GrantedAuthority> authorities;

	public StudentAccount(Long id, String password, String firstName, String lastName, int active){
		this.username = Long.toString(id);
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enabled = active;
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(new SimpleGrantedAuthority("Student"));
		this.authorities = auths;		
	}

	public StudentAccount(Object[] obj){
		this.username = obj[0].toString();
		this.password = StringUtils.defaultString((String)obj[1], "");
		this.firstName = StringUtils.defaultString((String)obj[2], "");
		this.lastName = StringUtils.defaultString((String)obj[3], "");
		this.enabled = (obj[4]!=null) ? (Integer)obj[4] : 0;
		String grade = StringUtils.defaultString((String)obj[5], "0");
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(new SimpleGrantedAuthority(grade));
		this.authorities = auths;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;	
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;	
	}

	public void setGrade(String grade) {
		// Clear existing authorities
		this.authorities = new ArrayList<>();
		// Add the grade as an authority
		this.authorities.add(new SimpleGrantedAuthority(grade));
	}

	@Override
	public boolean isEnabled() {
		return enabled == 1;
	}

}
