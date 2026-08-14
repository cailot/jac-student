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
public class TeacherPasswordUpdateRequestDTO implements Serializable {

	private String teacherId;

	private String currentPassword;

	private String newPassword;
}
