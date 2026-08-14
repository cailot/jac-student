package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginActivityDTO implements Serializable{
    
	private String id;
    
    private String studentId;
    
    private String registerDate;

	private long count;

	private long total;

	private String firstName;

	private String lastName;

	private String grade;

	private String contactNo1;

	private String email1;

	private String startDate;

    public LoginActivityDTO(long id,  String studentId, LocalDate registerDate, long count, long total) {
        this.id = Long.toString(id);
		this.studentId = studentId;
		this.registerDate = (registerDate!=null) ? registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.count = count;
		this.total = total;
	}

	public LoginActivityDTO(long studentId, long count) {
  		this.studentId = Long.toString(studentId);
		this.count = count;
	}

	public LoginActivityDTO(long studentId, long count, long total) {
		this.studentId = Long.toString(studentId);
	  	this.count = count;
		this.total = total;
	}

	public LoginActivityDTO(long studentId, String firstName, String lastName, String grade, String contact, String email, LocalDate startDate, long count, long total) {
		this.studentId = Long.toString(studentId);
		this.firstName = firstName;
		this.lastName = lastName;
		this.grade = grade;
		this.contactNo1 = contact;
		this.email1 = email;
		this.startDate = (startDate!=null) ? startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
	  	this.count = count;
		this.total = total;
	}
}
