package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import hyung.jin.seo.jae.model.Homework;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDTO implements Serializable {

	private String id;

	private String videoPath;

	private String pdfPath;

	private String info;

	// private int type;

	// private long duration;

	private int week;

	private int year;

	private boolean active;

	private String grade;

	private String subject;

	private String registerDate;

	
	public HomeworkDTO(long id, String videoPath, String pdfPath, int week, int year, String info, boolean active, long grade, long subject, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.videoPath = videoPath;
		this.pdfPath = pdfPath;
		this.week = week;
		this.year = year;
		this.info = info;
		this.active = active;
		this.grade = String.valueOf(grade);
		this.subject = String.valueOf(subject);
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	// public HomeworkDTO(long id, String videoPath, String pdfPath, int week, int year, String info, boolean active, long grade, long subject, LocalDate registerDate){
	// 	this.id = String.valueOf(id);
	// 	this.type = type;
	// 	this.path = path;
	// 	this.duration = duration;
	// 	this.week = week;
	// 	this.year = year;
	// 	this.info = info;
	// 	this.active = active;
	// 	this.grade = String.valueOf(grade);
	// 	this.subject = String.valueOf(subject);
	// 	this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	// }

	public Homework convertToHomework() {
    	Homework work = new Homework();
		work.setVideoPath(this.videoPath);
		work.setPdfPath(this.pdfPath);
		work.setWeek(this.week);
		work.setYear(this.year);
		work.setInfo(this.info);
		work.setActive(this.active);
		return work;
	}

	public HomeworkDTO(Homework work){
		this.id = String.valueOf(work.getId());
		this.videoPath = work.getVideoPath();
		this.pdfPath = work.getPdfPath();
		this.week = work.getWeek();
		this.year = work.getYear();
		this.info = work.getInfo();
		this.active = work.isActive();
		this.grade = String.valueOf(work.getGrade().getId());
		this.subject = String.valueOf(work.getSubject().getId());
		this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
