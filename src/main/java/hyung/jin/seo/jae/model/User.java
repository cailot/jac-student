package hyung.jin.seo.jae.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hyung.jin.seo.jae.utils.JaeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="User")
public class User implements UserDetails {

	// @Id
    // @GeneratedValue(generator = "usernameGenerator", strategy = GenerationType.IDENTITY)
    // @GenericGenerator(name = "usernameGenerator", strategy = "hyung.jin.seo.jae.utils.UsernameGenerator")
    // private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private String username;
    
    
    @Column(length = 100, nullable = false)
    private String password;

    @Column
    private int enabled;

    @Column(length = 100, nullable = true)
    private String firstName;
    
    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String role;
    
    @CreationTimestamp
    private LocalDate registerDate;

    @Column(length = 100, nullable = true)
    private String email;

    @Column(length = 100, nullable = true)
    private String phone;

    @Column(length = 2, nullable = true)
    private String state;
    
    @Column(length = 2, nullable = true)
    private String branch;

    @Transient
    private List<GrantedAuthority> authorities;

	public User(Object[] columns) {
        // this.id = (columns[0] != null) ? ((columns[0] instanceof BigInteger) ? ((BigInteger) columns[0]).longValue() : (Long) columns[0]) : 0L;
		this.username = StringUtils.defaultString((String) columns[0], "");
		this.password = StringUtils.defaultString((String) columns[1], "");
		this.enabled = (columns[3] != null) ? ((Integer) columns[2]) : 0;
		this.firstName = StringUtils.defaultString((String) columns[3], "");
		this.lastName = StringUtils.defaultString((String) columns[4], "");
		
        this.role = StringUtils.defaultString((String) columns[5], "");
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        if(StringUtils.defaultString(role).equalsIgnoreCase(JaeConstants.ROLE_ADMIN)){ // admin
            auths.add(new SimpleGrantedAuthority("Administrator"));
        }else{ // staff
            auths.add(new SimpleGrantedAuthority("Staff"));
        }
        this.authorities = auths;

        this.state = StringUtils.defaultString((String) columns[6], "0");
		this.branch = StringUtils.defaultString((String) columns[7], "0");
		this.email = StringUtils.defaultString((String) columns[8], "");
        this.phone = StringUtils.defaultString((String) columns[9], "");
	}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return this.authorities;
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


    @Override
    public boolean isEnabled() {
        return true;
    }
	
}