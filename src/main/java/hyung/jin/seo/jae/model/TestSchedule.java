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
@Table(name="TestSchedule")
public class TestSchedule {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fromDatetime;

    @Column(nullable = false)
    private LocalDateTime toDatetime;

    @Column(length = 30, nullable = false)
    private String grade;

    @Column(length = 30, nullable = false)
    private String testGroup;
   
    @Column(length = 30, nullable = false)
    private String week;

    @Column(length = 50, nullable = true)
    private String info;

    @Column
    private boolean active;

    @Column(nullable = true)
    private LocalDateTime explanationFromDatetime;

    @Column(nullable = true)
    private LocalDateTime explanationToDatetime;
    
    @CreationTimestamp
    private LocalDate registerDate;

    private LocalDate resultDate;

}