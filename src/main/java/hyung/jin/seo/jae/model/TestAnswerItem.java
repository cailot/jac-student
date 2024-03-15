package hyung.jin.seo.jae.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
@Embeddable
public class TestAnswerItem {
    
	private int question;
	private int answer;
	private String topic;

	public TestAnswerItem(int question, int answer, String topic){
		this.question = question;
		this.answer = answer;
		this.topic = topic;
	}
}
