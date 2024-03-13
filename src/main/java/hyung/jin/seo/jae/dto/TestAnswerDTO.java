package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import hyung.jin.seo.jae.model.TestAnswer;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerDTO implements Serializable {

	private String id;

	private String videoPath;

	private String pdfPath;

	private long testId;

	private List<Integer> answers;

	public TestAnswerDTO(TestAnswer work){
		this.id = String.valueOf(work.getId());
		this.videoPath = work.getVideoPath();
		this.pdfPath = work.getPdfPath();
		this.testId = work.getTest().getId();
		this.answers = work.getAnswers();
	}

}
