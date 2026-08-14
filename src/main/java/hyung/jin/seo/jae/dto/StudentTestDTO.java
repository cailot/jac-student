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

	private boolean isOnline;

	private List<Integer> answers = new ArrayList<>();

	private String studentName;

	private String testName;

	private String fileName;

	// additional info
	private int volume;

	private Long testTypeId;

	private String gradeCode;

	private String testTypeName;

	public StudentTestDTO(Long id, LocalDate registerDate, double score, Long studentId, Long testId, boolean isOnline, Collection<Integer> answers) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.score = score;
        this.studentId = studentId;
        this.testId = testId;
		this.isOnline = isOnline;
        this.answers = new ArrayList<>(answers);
    }

	public StudentTestDTO(Long id, Long studentId, Long testId, int volume, String gradeCode, Long testTypeId, String testTypeName, boolean isOnline) {
        this.id = id;
		this.studentId = studentId;
        this.testId = testId;
		this.volume = volume;
		this.gradeCode = gradeCode;
		this.testTypeId = testTypeId;
		this.testTypeName = testTypeName;
		this.isOnline = isOnline;
    }

	public StudentTestDTO(Long id, LocalDate registerDate, double score, Long studentId, Long testId, boolean isOnline) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.score = score;
        this.studentId = studentId;
        this.testId = testId;
		this.isOnline = isOnline;
    }

	public void addAnswer(int answer) {
		this.answers.add(answer);
	}
}
