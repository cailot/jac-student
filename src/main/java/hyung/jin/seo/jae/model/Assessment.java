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
import javax.persistence.ForeignKey;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Assessment")
public class Assessment {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 300, nullable = false)
    private String pdfPath;

    @Column
    private boolean active;
                       
    @CreationTimestamp
    private LocalDate registerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", foreignKey = @ForeignKey(name = "FK_Assessment_Subject"))
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeId", foreignKey = @ForeignKey(name = "FK_Assessment_Grade"))
    private Grade grade;
}
