package hyung.jin.seo.jae.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestResultHistoryDTO {
    
	private int testNo;

	private int studentScore;

	private int average;

	// private int questionCount;

}
