package hyung.jin.seo.jae.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HomeworkProgressDTO{
    
	private Long id;

	private String registerDate;

	private int percentage;

	private Long studentId;

	private Long homeworkId;

	public HomeworkProgressDTO(Long id, LocalDate registerDate, int percentage, Long studentId, Long homeworkId) {
        this.id = id;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.percentage = percentage;
        this.studentId = studentId;
        this.homeworkId = homeworkId;
    }
}
