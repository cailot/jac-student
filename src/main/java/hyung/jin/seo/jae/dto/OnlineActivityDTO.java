package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class OnlineActivityDTO implements Serializable{
    
	private String id;
    
    private String studentId;
    
	private String firstName;

	private String lastName;

	private String grade;

	private String contactNo;

	private String email;

	private String onlineSessionId;
    
	private String onlineName;

	private String registerDate;

	private int set;

	private int status;

	private String startDateTime;

	private String endDateTime;

	public OnlineActivityDTO(long id, long studentId, String firstName, String lastName, String grade, String contact, String email, long onlineSessionId, String onlineName, LocalDate registerDate, int set, int status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.id = Long.toString(id);
		this.studentId = Long.toString(studentId);
		this.firstName = firstName;
		this.lastName = lastName;
		this.grade = grade;
		this.contactNo = contact;
		this.email = email;
		this.onlineSessionId = Long.toString(onlineSessionId);
		this.onlineName = onlineName;
		this.registerDate = (registerDate!=null) ? registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
	  	this.set = set;
		this.status = status;
		this.startDateTime = (startDateTime!=null) ? startDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.endDateTime = (endDateTime!=null) ? endDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
	}
}
