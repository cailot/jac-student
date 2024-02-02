package hyung.jin.seo.jae.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.Attendance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttendanceDTO {

	private String id;

	private String attendDate;

	private String status;

	private String week;

	private String info;

	private String studentId;

	private String studentName;

	private String clazzId;

	private String clazzDay;

	private String clazzGrade;

	private String clazzName;

	public AttendanceDTO(Attendance attend) {
		this.id = String.valueOf(attend.getId());
		this.attendDate = attend.getAttendDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.status = StringUtils.defaultString(attend.getStatus(), "");
		this.week = StringUtils.defaultString(attend.getWeek(), "");
		this.info = StringUtils.defaultString(attend.getInfo(), "");
		this.studentId = String.valueOf(attend.getStudent().getId());
		this.studentName = attend.getStudent().getFirstName() + " " + attend.getStudent().getLastName();
		this.clazzId = String.valueOf(attend.getClazz().getId());
		this.clazzDay = StringUtils.defaultString(attend.getClazz().getDay(), "");
		this.clazzGrade = StringUtils.defaultString(attend.getClazz().getCourse().getGrade(), "");
		this.clazzName = StringUtils.defaultString(attend.getClazz().getName(), "");
	}

	public Attendance convertToAttendance() {
		Attendance attendance = new Attendance();
		if (StringUtils.isNotBlank(id))
			attendance.setId(Long.parseLong(id));
		if (StringUtils.isNotBlank(attendDate))
			attendance.setAttendDate(LocalDate.parse(attendDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		attendance.setStatus(status);
		attendance.setWeek(week);
		attendance.setInfo(info);
		return attendance;
	}

	public AttendanceDTO(long id, LocalDate attendDate, String status, String week, String info, long studentId,
			String studentFirstName, String studentLastName, long clazzId, String clazzDay, String clazzGrade,
			String clazzName) {
		this.id = String.valueOf(id);
		this.attendDate = attendDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.status = status;
		this.week = week;
		this.info = info;
		this.studentId = String.valueOf(studentId);
		this.studentName = studentFirstName + " " + studentLastName;
		this.clazzId = String.valueOf(clazzId);
		this.clazzDay = clazzDay;
		this.clazzGrade = clazzGrade;
		this.clazzName = clazzName;
	}

}
