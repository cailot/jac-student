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
public class AttendanceRollStudentDTO implements Serializable {

	private String id;

	private String status;

	private String studentId;

	private String studentName;

	public AttendanceRollStudentDTO(long id, String status, long studentId, String studentName) {
		this.id = Long.toString(id);
		this.status = status;
		this.studentId = Long.toString(studentId);
		this.studentName = studentName;
	}

}
