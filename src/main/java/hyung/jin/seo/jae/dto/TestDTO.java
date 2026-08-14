package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import hyung.jin.seo.jae.model.Test;
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
public class TestDTO implements Serializable {

	private String id;

	private String pdfPath;

	private int volume;

	private int answerCount;

	private int questionCount;

	private boolean active;

	private String info;

	private String grade;

	private long testType;

	private String registerDate;

	private String name;

	private boolean processed;

	private double average;

	public TestDTO(long id, String pdfPath, int volume, boolean active, boolean processed, double average, String info, String grade, long testType, String name, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.pdfPath = pdfPath;
		this.volume = volume;
		this.active = active;
		this.processed = processed;
		this.average = average;
		this.info = info;
		this.grade = grade;
		this.testType = testType;
		this.name = name;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	public TestDTO(long id, String pdfPath, int volume, boolean active, String info, String grade, long testType, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.pdfPath = pdfPath;
		this.volume = volume;
		// this.questionCount = questionCount;
		this.active = active;
		this.info = info;
		this.grade = grade;
		this.testType = testType;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public TestDTO(long id, String pdfPath, int volume, boolean active, String info, String grade, long testType, LocalDate registerDate, String name){
		this.id = String.valueOf(id);
		this.pdfPath = pdfPath;
		this.volume = volume;
		// this.questionCount = questionCount;
		this.active = active;
		this.info = info;
		this.grade = grade;
		this.testType = testType;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.name = name;
	}

	public TestDTO(long id, int volume, double average, long testType){
		this.id = String.valueOf(id);
		this.volume = volume;
		this.average = average;
		this.testType = testType;
	}

	public Test convertToTest() {
    	Test work = new Test();
		work.setPdfPath(this.pdfPath);
		work.setVolume(this.volume);
		work.setActive(this.active);
		work.setProcessed(this.processed);
		work.setAverage(this.average);
		work.setInfo(this.info);
		return work;
	}

	public TestDTO(Test work){
		this.id = String.valueOf(work.getId());
		this.pdfPath = work.getPdfPath();
		this.volume = work.getVolume();
		this.active = work.getActive();
		this.processed = work.getProcessed();
		this.average = work.getAverage();
		this.info = work.getInfo();
		this.grade = work.getGrade().getCode();
		this.testType = work.getTestType().getId();
		this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
