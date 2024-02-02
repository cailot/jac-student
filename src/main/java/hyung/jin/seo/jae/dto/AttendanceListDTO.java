package hyung.jin.seo.jae.dto;

import java.util.ArrayList;
import java.util.List;

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
public class AttendanceListDTO {
    
	private List<String> attendDate = new ArrayList<>();

	private List<String> status = new ArrayList<>();

	private List<Integer> week = new ArrayList<>();

	private String studentId;

	private String studentName;

	private String clazzId;

	private String clazzDay;

	private String clazzGrade;

	private String clazzName;

}



