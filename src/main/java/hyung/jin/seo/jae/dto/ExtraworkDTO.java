package hyung.jin.seo.jae.dto;

import java.io.Serializable;

import hyung.jin.seo.jae.model.Extrawork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExtraworkDTO implements Serializable {

	private String id;

	private String videoPath;

	private String pdfPath;

	private String name;

	private boolean active;

	private String grade;

	private String registerDate;
	
	public ExtraworkDTO(long id, String videoPath, String pdfPath, String name, boolean active, String grade, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.videoPath = videoPath;
		this.pdfPath = pdfPath;
		this.name = name;
		this.active = active;
		this.grade = grade;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public Extrawork convertToExtrawork() {
    	Extrawork work = new Extrawork();
		work.setVideoPath(this.videoPath);
		work.setPdfPath(this.pdfPath);
		work.setName(this.name);
		work.setActive(this.active);
		return work;
	}

	public ExtraworkDTO(Extrawork work){
		this.id = String.valueOf(work.getId());
		this.videoPath = work.getVideoPath();
		this.pdfPath = work.getPdfPath();
		this.name = work.getName();
		this.active = work.isActive();
		this.grade = work.getGrade().getCode();
		this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
