package hyung.jin.seo.jae.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentTestDTO{
    
	private Long id;

	private String registerDate;

	private double score;

	private Long studentId;

	private Long testId;

	private List<Integer> answers;

	// additional info
	private int volume;

	private Long testTypeId;

	private String gradeCode;

	private String testTypeName;

	public StudentTestDTO(Long id, Long studentId, Long testId, int volume, String gradeCode, Long testTypeId, String testTypeName) {
        this.id = id;
		this.studentId = studentId;
        this.testId = testId;
		this.volume = volume;
		this.gradeCode = gradeCode;
		this.testTypeId = testTypeId;
		this.testTypeName = testTypeName;
    }

	public StudentTestDTO(Long id, LocalDate registerDate, double score, Long studentId, Long testId, Collection<Integer> answers) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.score = score;
        this.studentId = studentId;
        this.testId = testId;
        this.answers = new ArrayList<>(answers);
    }
}
