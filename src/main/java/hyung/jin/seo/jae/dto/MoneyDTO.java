package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MoneyDTO implements Serializable{
    
	public String id;

	public String registerDate;

	public double amount;

	public String info;

}
