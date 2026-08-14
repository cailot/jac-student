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
@Table(name="Test")
public class Test{
    
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
    private Boolean active;

    @Column
    private Boolean processed;

    @Column
    private double average;
    
    @CreationTimestamp
    private LocalDate registerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeId", foreignKey = @ForeignKey(name = "FK_Test_Grade"))
    private Grade grade;	
     
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testTypeId", foreignKey = @ForeignKey(name = "FK_Test_TestType"))
    private TestType testType;

}
