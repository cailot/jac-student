package hyung.jin.seo.jae.dto;

import java.io.Serializable;

import hyung.jin.seo.jae.model.State;
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
public class StateDTO implements Serializable{
    
	private String id;

	private String code;

	private String name;

	public StateDTO(State state){
		this.id = (state.getId()!=null) ? state.getId().toString() : "";
		this.code = (state.getCode()!=null) ? state.getCode() : "";
		// this.acronym = (state.getAcronym()!=null) ? state.getAcronym() : "";
		this.name = (state.getName()!=null) ? state.getName() : "";
	}

}
