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
public class StudentPracticeDTO{
    
	private Long id;

	private String registerDate;

	private double score;

	private Long studentId;

	private Long practiceId;

	private List<Integer> answers;

	public StudentPracticeDTO(Long id, LocalDate registerDate, double score, Long studentId, Long practiceId, Collection<Integer> answers) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.score = score;
        this.studentId = studentId;
        this.practiceId = practiceId;
        this.answers = new ArrayList<>(answers);
    }
}
