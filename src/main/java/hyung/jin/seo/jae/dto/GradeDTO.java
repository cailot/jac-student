package hyung.jin.seo.jae.dto;

import java.io.Serializable;

import hyung.jin.seo.jae.model.Grade;
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
public class GradeDTO implements Serializable{
    
	private String id;

	private String code;

	private String name;

	public GradeDTO(Grade grade){
		this.id = (grade.getId()!=null) ? grade.getId().toString() : "";
		this.code = (grade.getCode()!=null) ? grade.getCode() : "";
		this.name = (grade.getName()!=null) ? grade.getName() : "";
	}

}
