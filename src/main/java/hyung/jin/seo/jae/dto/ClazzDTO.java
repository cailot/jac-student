package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hyung.jin.seo.jae.model.Clazz;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClazzDTO implements Serializable{
    
	private String id;

	private String state;

	private String branch;
    
    //private String fee;

	private String name;
   
   	private String description; // Course.description

	private String day;

	private String startDate;

	private boolean active;

	private double price;

   	private String courseId;

	@JsonIgnore
	private String cycleId;

	private String grade; // Course.grade

	private boolean online; // Course.online

	private String year; // Cycle.year

	public ClazzDTO(long id, String state, String branch, double price, String day, String name, LocalDate startDate, boolean active, long courseId, long cycleId, String grade, boolean online, String description, int year) {
		this.id = Long.toString(id);
		this.state = state;
		this.branch = branch;
		this.price = price;
		this.day = day;
		this.name = name;
		this.startDate = startDate.toString();
		this.active = active;
		this.courseId = Long.toString(courseId);
		this.cycleId = Long.toString(cycleId);
		this.grade = grade;
		this.description = description;	
		this.year = Integer.toString(year);
	}

	public ClazzDTO(Clazz clazz){
		this.id = Long.toString(clazz.getId());
		this.state = clazz.getState();
		this.branch = clazz.getBranch();
		this.day = clazz.getDay();
		this.name = clazz.getName();
		this.startDate = clazz.getStartDate().toString();
		this.active = clazz.isActive();
		this.courseId = Long.toString(clazz.getCourse().getId());
		this.cycleId = Long.toString(clazz.getCycle().getId());
		this.grade = clazz.getCourse().getGrade();
		this.description = clazz.getCourse().getDescription();
		this.year = Integer.toString(clazz.getCycle().getYear());
		this.price = clazz.getPrice();
	}


	public Clazz convertToOnlyClass() {
    	Clazz clazz = new Clazz();
		if(StringUtils.isNotBlank(id)) clazz.setId(Long.parseLong(this.id));
		if(StringUtils.isNotBlank(state)) clazz.setState(this.state);
    	if(StringUtils.isNotBlank(branch)) clazz.setBranch(this.branch);
		if(StringUtils.isNotBlank(name)) clazz.setName(this.name);
		if(StringUtils.isNotBlank(startDate)) clazz.setStartDate(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		if(StringUtils.isNotBlank(day)) clazz.setDay(this.day);
		clazz.setActive(this.active);
		clazz.setPrice(this.price);
    	return clazz;
    }

}
