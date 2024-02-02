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

	private double price;

	private int year;

	private boolean online;

	private List<String> subjects = new ArrayList<>();

	public void addSubject(String subject){
		subjects.add(subject);
	}


	public CourseDTO(Course course) {
    	this.id = (course.getId()!=null) ? course.getId().toString() : "";
    	this.name = (course.getName()!=null) ? course.getName() : "";
    	this.description = (course.getDescription()!=null) ? course.getDescription() : "";
    	this.registerDate = (course.getRegisterDate()!=null) ? course.getRegisterDate().toString() : "";
    	this.grade = (course.getGrade()!=null) ? course.getGrade() : "";
		this.price = (course.getPrice()!=0) ? course.getPrice() : 0;
		this.online = course.isOnline();
    }
    
    public Course convertToCourse() {
    	Course course = new Course();
    	if(StringUtils.isNotBlank(id)) course.setId(Long.parseLong(this.id));
    	if(StringUtils.isNotBlank(name)) course.setName(this.name);
    	if(StringUtils.isNotBlank(description)) course.setDescription(this.description);
    	if(StringUtils.isNotBlank(registerDate)) course.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	if(StringUtils.isNotBlank(grade)) course.setGrade(this.grade);
		if(price!=0) course.setPrice(this.price);
		course.setOnline(this.online);
    	return course;
    }

	public CourseDTO(long id, String name, String description, String grade, double price, boolean online){
		this.id = Long.toString(id);
		this.name = name;
		this.description = description;
		this.grade = grade;
		this.price = price;
		this.online = online;		
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
