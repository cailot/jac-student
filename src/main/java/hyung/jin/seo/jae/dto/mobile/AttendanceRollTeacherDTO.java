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
public class AttendanceRollTeacherDTO implements Serializable {

	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String password;

	private String address; // Course.grade

	private String vit; // how many enroled

	public AttendanceRollTeacherDTO(long id, String firstName, String lastName, String email, String phone, String password, String address, String vit) {
		this.id = Long.toString(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.address = address;
		this.vit = vit;
	}

}
