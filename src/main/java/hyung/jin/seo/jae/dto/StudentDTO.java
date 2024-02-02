package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.utils.JaeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@Getter
@Setter
// @ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable{
    
    private String id;
    
    private String firstName;
    
    private String lastName;

	private String password;
    
    private String grade;
    
    private String contactNo1;
    
    private String contactNo2;
    
    private String email1;

	private String email2;

	private String relation1;

	private String relation2;

    private String address;
    
    private String state;
    
    private String branch;
    
    private String memo;

	private String gender;
    
    private String registerDate;
    
    // private String enrolmentDate;
    
    private String endDate;

	private int startWeek;

	private int endWeek;

	private int active;
    
    public Student convertToStudent() {
    	Student std = new Student();
    	if(StringUtils.isNotBlank(id)) std.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(firstName)) std.setFirstName(this.firstName);
    	if(StringUtils.isNotBlank(lastName)) std.setLastName(this.lastName);
    	if(StringUtils.isNotBlank(grade)) std.setGrade(this.grade);
    	if(StringUtils.isNotBlank(contactNo1)) std.setContactNo1(this.contactNo1);
    	if(StringUtils.isNotBlank(contactNo2)) std.setContactNo2(this.contactNo2);
    	if(StringUtils.isNotBlank(email1)) std.setEmail1(this.email1);
    	if(StringUtils.isNotBlank(email2)) std.setEmail2(this.email2);
    	if(StringUtils.isNotBlank(relation1)) std.setRelation1(this.relation1);
    	if(StringUtils.isNotBlank(relation2)) std.setRelation2(this.relation2);
		if(StringUtils.isNotBlank(address)) std.setAddress(this.address);
    	if(StringUtils.isNotBlank(state)) std.setState(this.state);
    	if(StringUtils.isNotBlank(branch)) std.setBranch(this.branch);
    	if(StringUtils.isNotBlank(memo)) std.setMemo(this.memo);
		if(StringUtils.isNotBlank(gender)) std.setGender(this.gender);
		if(StringUtils.isNotBlank(password)) std.setPassword(password);
		std.setActive(active);
    	if(StringUtils.isNotBlank(registerDate)) std.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	if(StringUtils.isNotBlank(endDate)) std.setEndDate(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	return std;
    }

	public Student convertToOnlyStudent() {
    	Student std = new Student();
    	if(StringUtils.isNotBlank(id)) std.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(firstName)) std.setFirstName(this.firstName);
    	if(StringUtils.isNotBlank(lastName)) std.setLastName(this.lastName);
    	if(StringUtils.isNotBlank(grade)) std.setGrade(this.grade);
    	if(StringUtils.isNotBlank(contactNo1)) std.setContactNo1(this.contactNo1);
    	if(StringUtils.isNotBlank(contactNo2)) std.setContactNo2(this.contactNo2);
    	if(StringUtils.isNotBlank(email1)) std.setEmail1(this.email1);
		if(StringUtils.isNotBlank(email2)) std.setEmail2(this.email2);
    	if(StringUtils.isNotBlank(relation1)) std.setRelation1(this.relation1);
		if(StringUtils.isNotBlank(relation2)) std.setRelation2(this.relation2);
    	if(StringUtils.isNotBlank(address)) std.setAddress(this.address);
    	if(StringUtils.isNotBlank(state)) std.setState(this.state);
    	if(StringUtils.isNotBlank(branch)) std.setBranch(this.branch);
    	if(StringUtils.isNotBlank(memo)) std.setMemo(this.memo);
		if(StringUtils.isNotBlank(gender)) std.setGender(this.gender);
		if(StringUtils.isNotBlank(password)) std.setPassword(this.password);
		std.setActive(this.active);
    	if(StringUtils.isNotBlank(registerDate)) std.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	if(StringUtils.isNotBlank(endDate)) std.setEndDate(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	return std;
    }


    public StudentDTO(Student std) {
    	this.id = (std.getId()!=null) ? std.getId().toString() : "";
        this.firstName = (std.getFirstName()!=null) ? std.getFirstName() : "";
        this.lastName = (std.getLastName()!=null) ? std.getLastName() : "";
		this.password = (std.getPassword()!=null) ? std.getPassword() : "";
        this.grade = (std.getGrade()!=null) ? std.getGrade() : "";
        this.contactNo1 = (std.getContactNo1()!=null) ? std.getContactNo1() : "";
        this.contactNo2 = (std.getContactNo2()!=null) ? std.getContactNo2() : "";
        this.email1 = (std.getEmail1()!=null) ? std.getEmail1() : "";
		this.email2 = (std.getEmail2()!=null) ? std.getEmail2() : "";
		this.relation1 = (std.getRelation1()!=null) ? std.getRelation1() : "";
		this.relation2 = (std.getRelation2()!=null) ? std.getRelation2() : "";
        this.address = (std.getAddress()!=null) ? std.getAddress() : "";
        this.state = (std.getState()!=null) ? std.getState() : "";
        this.branch = (std.getBranch()!=null) ? std.getBranch() : "";
        this.memo = (std.getMemo()!=null) ? std.getMemo() : "";
		this.gender = (std.getGender()!=null) ? std.getGender() : "";
		this.active = (std.getActive()==JaeConstants.ACTIVE) ? JaeConstants.ACTIVE : JaeConstants.INACTIVE;
        this.registerDate = (std.getRegisterDate()!=null) ? std.getRegisterDate().toString() : "";
        this.endDate = (std.getEndDate()!=null) ? std.getEndDate().toString() : ""; 
    }
    
	public StudentDTO(long id, String firstName, String lastName, String grade, String gender, String contactNo1, String contactNo2, String email1, String email2, String state, String branch, LocalDate registerDate, LocalDate endDate, String password, int active) {
    	this.id = String.valueOf(id);
        this.firstName = (firstName !=null ) ? firstName : "";
        this.lastName = (lastName !=null ) ? lastName : "";
        this.grade = (grade!=null) ? grade : "";
		this.gender = (gender!=null) ? gender : "";
        this.contactNo1 = (contactNo1 !=null ) ? contactNo1 : "";
        this.contactNo2 = (contactNo2 !=null) ? contactNo2 : "";
        this.email1 = (email1!=null) ? email1 : "";
		this.email2 = (email2!=null) ? email2 : "";
		this.state = (state!=null) ? state : "";
        this.branch = (branch!=null) ? branch : "";
        this.registerDate = (registerDate!=null) ? registerDate.toString() : "";
		this.endDate = (endDate!=null) ? endDate.toString() : "";
		this.password = (password!=null) ? password : "";
		this.active = active;
    }

	public StudentDTO(long id, String firstName, String lastName, String grade, String gender, String contactNo1, String contactNo2, String email1, String email2, String state, String branch, LocalDate registerDate, LocalDate endDate, String password, int active, int startWeek, int endWeek) {
    	this.id = String.valueOf(id);
        this.firstName = (firstName !=null ) ? firstName : "";
        this.lastName = (lastName !=null ) ? lastName : "";
        this.grade = (grade!=null) ? grade : "";
		this.gender = (gender!=null) ? gender : "";
        this.contactNo1 = (contactNo1 !=null ) ? contactNo1 : "";
        this.contactNo2 = (contactNo2 !=null) ? contactNo2 : "";
        this.email1 = (email1!=null) ? email1 : "";
		this.email2 = (email2!=null) ? email2 : "";
		this.state = (state!=null) ? state : "";
        this.branch = (branch!=null) ? branch : "";
        this.registerDate = (registerDate!=null) ? registerDate.toString() : "";
		this.endDate = (endDate!=null) ? endDate.toString() : "";
		this.password = (password!=null) ? password : "";
		this.active = active;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
    }
}
