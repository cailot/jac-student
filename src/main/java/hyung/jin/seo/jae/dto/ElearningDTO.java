package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hyung.jin.seo.jae.model.Elearning;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ElearningDTO implements Serializable {

	private String id;

	private String grade;

	private String name;

	@JsonIgnore
	private String registerDate;

	public ElearningDTO(Elearning crs) {
		this.id = (crs.getId() != null) ? crs.getId().toString() : "";
		this.grade = (crs.getGrade() != null) ? crs.getGrade() : "";
		this.name = (crs.getName() != null) ? crs.getName() : "";
		//this.registerDate = (crs.getRegisterDate() != null) ? crs.getRegisterDate().toString() : "";
	}

	public Elearning convertToElearning() {
		Elearning crs = new Elearning();
		if (StringUtils.isNotBlank(id))
			crs.setId(Long.parseLong(this.id));
		if (StringUtils.isNotBlank(grade))
			crs.setGrade(this.grade);
		if (StringUtils.isNotBlank(name))
			crs.setName(this.name);
		if (StringUtils.isNotBlank(registerDate))
			crs.setRegisterDate(LocalDate.parse(registerDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		return crs;
	}

}
