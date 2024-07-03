package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import hyung.jin.seo.jae.model.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseDTO implements Serializable, Cloneable{
    
	private String id;
    
    private String name;
    
    private String description;
    
    private String registerDate;
    
    private String grade;

	private int year; // Cycle.year

	private double price;

	private boolean online;

	private String cycleId;

	private List<SubjectDTO> subjects = new ArrayList<>();

	public void addSubject(SubjectDTO subject){
		subjects.add(subject);
	}

	public Course convertToCourse() {
    	Course course = new Course();
    	if(StringUtils.isNotBlank(id)) course.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(name)) course.setName(this.name);
    	if(StringUtils.isNotBlank(description)) course.setDescription(this.description);
    	if(StringUtils.isNotBlank(registerDate)) course.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	if(StringUtils.isNotBlank(grade)) course.setGrade(this.grade);
		course.setOnline(this.online);
		course.setPrice(this.price);
    	return course;
    }

	public CourseDTO(Course course) {
    	this.id = (course.getId()!=null) ? course.getId().toString() : "";
    	this.name = (course.getName()!=null) ? course.getName() : "";
    	this.description = (course.getDescription()!=null) ? course.getDescription() : "";
    	this.registerDate = (course.getRegisterDate()!=null) ? course.getRegisterDate().toString() : "";
    	this.grade = (course.getGrade()!=null) ? course.getGrade() : "";
		this.online = course.isOnline();
		this.price = course.getPrice();
    }
    
	public CourseDTO(long id, String name, String description, String grade, boolean online, double price, long cycleId, int year){
		this.id = Long.toString(id);
		this.name = name;
		this.description = description;
		this.grade = grade;
		this.online = online;
		this.price = price;		
		this.cycleId = Long.toString(cycleId);
		this.year = year;
	}


	// for new academic year Object
	@Override
	public CourseDTO clone() {
		try{
			return (CourseDTO) super.clone();
		}catch(CloneNotSupportedException e){
			// Handle clone not supported exception
			return new CourseDTO();
		}
	}
}
