package hyung.jin.seo.jae.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.Subject;
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
public class SubjectDTO implements Serializable{
    
	private String id;

	private String abbr;

	private String name;

	private String description;

	public SubjectDTO(Subject subject){
		this.id = (subject.getId()!=null) ? subject.getId().toString() : "";
		this.abbr = StringUtils.defaultString(subject.getAbbr());
		this.name = StringUtils.defaultString(subject.getName());
		this.description = StringUtils.defaultString(subject.getDescription());
	}

}