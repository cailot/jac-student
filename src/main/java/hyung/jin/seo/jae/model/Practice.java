package hyung.jin.seo.jae.model;

import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Practice")
public class Practice {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(length = 300, nullable = true)
    private String pdfPath;

    @Column(length = 2, nullable = true)
    private Integer volume;

    @Column(length = 50, nullable = true)
    private String info;

    @Column
    private boolean active;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeId")
    private Grade grade;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practiceTypeId")
    private PracticeType practiceType;

    @CreationTimestamp
    private LocalDate registerDate;
    
}
