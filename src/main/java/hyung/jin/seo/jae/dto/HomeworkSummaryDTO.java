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
public class HomeworkSummaryDTO extends WorkSummaryDTO {
    
	// private long id;

	private int week;

	private int percentage;

}
