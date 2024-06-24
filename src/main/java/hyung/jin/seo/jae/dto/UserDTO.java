package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import hyung.jin.seo.jae.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO implements Serializable{
    
    // private String id;
    
    private String username;

    private String firstName;
    
    private String lastName;

    private String password;
        
    private String phone;
    
    private String email;

    private String role;
    
    private String state;
    
    private String branch;
    
    private String registerDate;
    
    private int enabled;
  
	public UserDTO(User user) {
    	// this.id = (user.getId()!=null) ? user.getId().toString() : "";
        this.firstName = (user.getFirstName()!=null) ? user.getFirstName() : "";
        this.lastName = (user.getLastName()!=null) ? user.getLastName() : "";
        this.username = (user.getUsername()!=null) ? user.getUsername() : "";
        this.password = (user.getPassword()!=null) ? user.getPassword() : "";
        this.phone = (user.getPhone()!=null) ? user.getPhone() : "";
        this.email = (user.getEmail()!=null) ? user.getEmail() : "";
        this.role = (user.getRole()!=null) ? user.getRole() : "";
        this.state = (user.getState()!=null) ? user.getState() : "";
        this.branch = (user.getBranch()!=null) ? user.getBranch() : "";
        this.registerDate = (user.getRegisterDate()!=null) ? user.getRegisterDate().toString() : "";
        this.enabled = user.getEnabled();
    }

    public UserDTO(String username, String firstName, String lastName, int enabled, String phone, String email, String role, String state, String branch){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.state = state;
        this.branch = branch;
    }
    
    public User convertToUser() {
    	User user = new User();
    	// if(StringUtils.isNotBlank(id)) user.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(firstName)) user.setFirstName(this.firstName);
    	if(StringUtils.isNotBlank(lastName)) user.setLastName(this.lastName);
    	if(StringUtils.isNotBlank(username)) user.setUsername(this.username);
    	if(StringUtils.isNotBlank(phone)) user.setPhone(this.phone);
    	if(StringUtils.isNotBlank(email)) user.setEmail(this.email);
    	if(StringUtils.isNotBlank(password)) user.setPassword(this.password);
    	if(StringUtils.isNotBlank(state)) user.setState(this.state);
    	if(StringUtils.isNotBlank(branch)) user.setBranch(this.branch);
    	if(StringUtils.isNotBlank(role)) user.setRole(this.role);
    	user.setEnabled(this.enabled);
    	return user;
    }
}
