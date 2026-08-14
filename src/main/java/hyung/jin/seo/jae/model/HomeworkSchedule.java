package hyung.jin.seo.jae.model;

import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="HomeworkSchedule")
public class HomeworkSchedule {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fromDatetime;

    @Column(nullable = false)
    private LocalDateTime toDatetime;

    @Column(length = 30, nullable = true)
    private String grade;

    @Column(length = 30, nullable = true)
    private String subject;
    
    @Column(length = 2, nullable = true)
    private Integer subjectDisplay;

    @Column(length = 2, nullable = true)
    private Integer answerDisplay;

    @Column(length = 50, nullable = true)
    private String info;

    @Column
    private boolean active;

    @CreationTimestamp
    private LocalDate registerDate;
    
}
