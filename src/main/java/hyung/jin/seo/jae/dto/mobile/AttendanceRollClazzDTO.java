package hyung.jin.seo.jae.dto.mobile;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttendanceRollClazzDTO implements Serializable {

	private String id;

	private String name;

	private String description; // Course.description

	private String day;

	private String grade; // Course.grade

	private String number; // how many enroled

	public AttendanceRollClazzDTO(long id, String day, String name, String grade, String description, int number) {
		this.id = Long.toString(id);
		this.day = day;
		this.name = name;
		this.grade = grade;
		this.description = description;
		this.number = Integer.toString(number);
	}

}
