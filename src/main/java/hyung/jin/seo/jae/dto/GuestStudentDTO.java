package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.GuestStudent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestStudentDTO implements Serializable{
    
    private String id;
    
    private String firstName;
    
    private String lastName;

    private String grade;
    
    private String contactNo;
    
    private String email;

	private String state;
    
    private String branch;
        
    private String registerDate;
    
    public GuestStudent convertToGuestStudent() {
    	GuestStudent std = new GuestStudent();
    	if(StringUtils.isNotBlank(id)) std.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(firstName)) std.setFirstName(this.firstName);
    	if(StringUtils.isNotBlank(lastName)) std.setLastName(this.lastName);
    	if(StringUtils.isNotBlank(grade)) std.setGrade(this.grade);
    	if(StringUtils.isNotBlank(contactNo)) std.setContactNo(this.contactNo);
    	if(StringUtils.isNotBlank(email)) std.setEmail(this.email);
    	if(StringUtils.isNotBlank(state)) std.setState(this.state);
    	if(StringUtils.isNotBlank(branch)) std.setBranch(this.branch);
    	if(StringUtils.isNotBlank(registerDate)) std.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	return std;
    }

    public GuestStudentDTO(GuestStudent std) {
    	this.id = (std.getId()!=null) ? std.getId().toString() : "";
        this.firstName = (std.getFirstName()!=null) ? std.getFirstName() : "";
        this.lastName = (std.getLastName()!=null) ? std.getLastName() : "";
		this.grade = (std.getGrade()!=null) ? std.getGrade() : "";
        this.contactNo = (std.getContactNo()!=null) ? std.getContactNo() : "";
        this.email = (std.getEmail()!=null) ? std.getEmail() : "";
        this.state = (std.getState()!=null) ? std.getState() : "";
        this.branch = (std.getBranch()!=null) ? std.getBranch() : "";
        this.registerDate = (std.getRegisterDate()!=null) ? std.getRegisterDate().toString() : "";
    }
    
	public GuestStudentDTO(long id, String firstName, String lastName, String grade, String contactNo, String email, String state, String branch, LocalDate registerDate) {
    	this.id = String.valueOf(id);
        this.firstName = (firstName !=null ) ? firstName : "";
        this.lastName = (lastName !=null ) ? lastName : "";
        this.grade = (grade!=null) ? grade : "";
	    this.contactNo = (contactNo !=null ) ? contactNo : "";
		this.email = (email!=null) ? email : "";
		this.state = (state!=null) ? state : "";
        this.branch = (branch!=null) ? branch : "";
        this.registerDate = (registerDate!=null) ? registerDate.toString() : "";
	}
}
