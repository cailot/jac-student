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
public class TeacherLoginResponseDTO implements Serializable {

	private boolean success;
	private String message;
	private Long teacherId;
	private String email;
	private String firstName;
	private String lastName;

	public static TeacherLoginResponseDTO ok(Long teacherId, String email, String firstName, String lastName) {
		TeacherLoginResponseDTO dto = new TeacherLoginResponseDTO();
		dto.setSuccess(true);
		dto.setMessage("Login Success");
		dto.setTeacherId(teacherId);
		dto.setEmail(email);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		return dto;
	}

	public static TeacherLoginResponseDTO fail(String message) {
		TeacherLoginResponseDTO dto = new TeacherLoginResponseDTO();
		dto.setSuccess(false);
		dto.setMessage(message);
		return dto;
	}
}
