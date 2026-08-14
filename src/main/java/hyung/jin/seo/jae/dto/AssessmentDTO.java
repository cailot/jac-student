package hyung.jin.seo.jae.dto;

import java.io.Serializable;

import hyung.jin.seo.jae.model.Assessment;
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
public class AssessmentDTO implements Serializable {

	private String id;

	private String pdfPath;

	private boolean active = true;

	private String grade;

	private long subject;

	private String registerDate;

	public AssessmentDTO(long id, String pdfPath, boolean active, String grade, long subject, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.pdfPath = pdfPath;
		this.active = active;
		this.grade = grade;
		this.subject = subject;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public Assessment convertToAssessment() {
    	Assessment work = new Assessment();
		work.setPdfPath(this.pdfPath);
		work.setActive(this.active);
		return work;
	}

	public AssessmentDTO(Assessment work){
		this.id = String.valueOf(work.getId());
		this.pdfPath = work.getPdfPath();
		this.grade = work.getGrade().getCode();
		this.subject = work.getSubject().getId();
		this.active = work.isActive();
		this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
