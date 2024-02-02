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
public class SimpleBasketDTO {
    
	private String name;

	private String value;

	public SimpleBasketDTO(Object[] obj){
		this.name = (obj[0]!=null) ? String.valueOf(obj[0]) : "";
		this.value = (obj[1]!=null) ? String.valueOf(obj[1]) : "";
	}

}
