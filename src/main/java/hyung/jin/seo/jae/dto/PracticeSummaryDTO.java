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
public class PracticeSummaryDTO extends WorkSummaryDTO {
    
	// private long id;

	private String title;

	private int week;

}
