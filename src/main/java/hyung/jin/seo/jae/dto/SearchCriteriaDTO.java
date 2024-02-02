package hyung.jin.seo.jae.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchCriteriaDTO {
    
	private String id;

	private String state;

	private String branch;

	private String grade;

	private String clazzId;

	private String clazzName;
	
	private String fromDate;

	private String toDate;

	private String extra;

}
