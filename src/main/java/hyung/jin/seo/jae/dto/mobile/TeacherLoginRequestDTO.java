package hyung.jin.seo.jae.dto.mobile;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TeacherLoginRequestDTO implements Serializable {

	@JsonProperty("user_email")
	private String userEmail;

	@JsonProperty("user_password")
	private String userPassword;
}
