package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import hyung.jin.seo.jae.model.PracticeAnswer;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PracticeAnswerDTO implements Serializable {

	private String id;

	private String videoPath;

	private String pdfPath;

	private long practiceId;

	private List<Integer> answers;

	public PracticeAnswerDTO(PracticeAnswer work){
		this.id = String.valueOf(work.getId());
		this.videoPath = work.getVideoPath();
		this.pdfPath = work.getPdfPath();
		this.practiceId = work.getPractice().getId();
		this.answers = work.getAnswers();
	}

}
