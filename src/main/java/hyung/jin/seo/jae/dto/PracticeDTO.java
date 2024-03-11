package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import hyung.jin.seo.jae.model.Practice;
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
public class PracticeDTO implements Serializable {

	private String id;

	private String pdfPath;

	private int volume;

	private int questionCount;

	private boolean active;

	private String info;

	private String grade;

	private long practiceType;

	private String registerDate;

	
	public PracticeDTO(long id, String pdfPath, int volume, boolean active, String info, String grade, long practiceType, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.pdfPath = pdfPath;
		this.volume = volume;
		// this.questionCount = questionCount;
		this.active = active;
		this.info = info;
		this.grade = grade;
		this.practiceType = practiceType;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public Practice convertToPractice() {
    	Practice work = new Practice();
		work.setPdfPath(this.pdfPath);
		work.setVolume(this.volume);
		// work.setQuestionCount(this.questionCount);
		work.setActive(this.active);
		work.setInfo(this.info);
		return work;
	}

	public PracticeDTO(Practice work){
		this.id = String.valueOf(work.getId());
		this.pdfPath = work.getPdfPath();
		this.volume = work.getVolume();
		// this.questionCount = work.getQuestionCount();
		this.active = work.isActive();
		this.info = work.getInfo();
		this.grade = work.getGrade().getCode();
		this.practiceType = work.getPracticeType().getId();
		this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
