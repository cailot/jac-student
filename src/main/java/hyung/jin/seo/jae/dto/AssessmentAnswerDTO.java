package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import hyung.jin.seo.jae.model.AssessmentAnswer;
import hyung.jin.seo.jae.model.AssessmentAnswerItem;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentAnswerDTO implements Serializable {

	private String id;

	private long assessmentId;

	private List<AssessmentAnswerItem> answers;

	public AssessmentAnswerDTO(AssessmentAnswer work){
		this.id = String.valueOf(work.getId());
		this.assessmentId = work.getAssessment().getId();
		this.answers = work.getAnswers();
	}

}
