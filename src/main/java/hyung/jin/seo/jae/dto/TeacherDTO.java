package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import hyung.jin.seo.jae.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherDTO implements Serializable{
    
    private String id;
    
    private String firstName;
    
    private String lastName;
    
    private String title;
    
    private String phone;
    
    private String email;
    
    private String address;
    
    private String state;
    
    private String branch;
    
    private String memo;
    
    private String bank;
    
    private String bsb;
    
    private String accountNumber;
    
    private String superannuation;

    private String vitNumber;
    
    private String superMember;
    
    private String tfn;
    
    private String startDate;
    
    private String endDate;
  
	public TeacherDTO(Teacher teacher) {
    	this.id = (teacher.getId()!=null) ? teacher.getId().toString() : "";
        this.firstName = (teacher.getFirstName()!=null) ? teacher.getFirstName() : "";
        this.lastName = (teacher.getLastName()!=null) ? teacher.getLastName() : "";
        this.title = (teacher.getTitle()!=null) ? teacher.getTitle() : "";
        this.phone = (teacher.getPhone()!=null) ? teacher.getPhone() : "";
        this.email = (teacher.getEmail()!=null) ? teacher.getEmail() : "";
        this.address = (teacher.getAddress()!=null) ? teacher.getAddress() : "";
        this.state = (teacher.getState()!=null) ? teacher.getState() : "";
        this.branch = (teacher.getBranch()!=null) ? teacher.getBranch() : "";
        this.memo = (teacher.getMemo()!=null) ? teacher.getMemo() : "";
        this.bank = (teacher.getBank()!=null) ? teacher.getBank() : "";
        this.bsb = (teacher.getBsb()!=null) ? teacher.getBsb() : "";
        this.accountNumber = (teacher.getAccountNumber()!=null) ? teacher.getAccountNumber().toString() : "";
        this.superannuation = (teacher.getSuperannuation()!=null) ? teacher.getSuperannuation() : "";
        this.vitNumber = (teacher.getVitNumber()!=null) ? teacher.getVitNumber() : "";
        this.superMember = (teacher.getSuperMember()!=null) ? teacher.getSuperMember() : "";
        this.tfn = (teacher.getTfn()!=null) ? teacher.getTfn().toString() : "";
        this.startDate = (teacher.getStartDate()!=null) ? teacher.getStartDate().toString() : "";
        this.endDate = (teacher.getEndDate()!=null) ? teacher.getEndDate().toString() : ""; 
    }
    
    public Teacher convertToTeacher() {
    	Teacher teacher = new Teacher();
    	if(StringUtils.isNotBlank(id)) teacher.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(firstName)) teacher.setFirstName(this.firstName);
    	if(StringUtils.isNotBlank(lastName)) teacher.setLastName(this.lastName);
    	if(StringUtils.isNotBlank(title)) teacher.setTitle(this.title);
    	if(StringUtils.isNotBlank(phone)) teacher.setPhone(this.phone);
    	if(StringUtils.isNotBlank(email)) teacher.setEmail(this.email);
    	if(StringUtils.isNotBlank(address)) teacher.setAddress(this.address);
    	if(StringUtils.isNotBlank(state)) teacher.setState(this.state);
    	if(StringUtils.isNotBlank(branch)) teacher.setBranch(this.branch);
    	if(StringUtils.isNotBlank(memo)) teacher.setMemo(this.memo);
    	if(StringUtils.isNotBlank(bank)) teacher.setBank(this.bank);
    	if(StringUtils.isNotBlank(bsb)) teacher.setBsb(this.bsb);
    	if(StringUtils.isNotBlank(accountNumber)) teacher.setAccountNumber(Long.parseLong(this.accountNumber));
    	if(StringUtils.isNotBlank(superannuation)) teacher.setSuperannuation(this.superannuation);
        if(StringUtils.isNotBlank(vitNumber)) teacher.setVitNumber(this.vitNumber);        
    	if(StringUtils.isNotBlank(superMember)) teacher.setSuperMember(this.superMember);
    	if(StringUtils.isNotBlank(tfn)) teacher.setTfn(Long.parseLong(this.tfn));
    	if(StringUtils.isNotBlank(startDate)) teacher.setStartDate(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	if(StringUtils.isNotBlank(endDate)) teacher.setEndDate(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	return teacher;
    }
}
