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
public class GuestStudentAssessmentDTO{
    
	private Long id;

	private String registerDate;

	private double score;

	private Long guestStudentId;

	private Long assessmentId;

	private List<Integer> answers;

	private String subject;

	public GuestStudentAssessmentDTO(Long id, LocalDate registerDate, double score, Long guestStudentId, Long assessmentId, String subject) {
		this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.score = score;
		this.guestStudentId = guestStudentId;
		this.assessmentId = assessmentId;
		this.subject = subject;
	}

	public GuestStudentAssessmentDTO(Long id, LocalDate registerDate, double score, Long guestStudentId, Long assessmentId) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.score = score;
        this.guestStudentId = guestStudentId;
        this.assessmentId = assessmentId;
    }

	public GuestStudentAssessmentDTO(Long id, LocalDate registerDate, double score, Long guestStudentId, Long assessmentId, Collection<Integer> answers) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.score = score;
        this.guestStudentId = guestStudentId;
        this.assessmentId = assessmentId;
        this.answers = new ArrayList<>(answers);
    }
}
