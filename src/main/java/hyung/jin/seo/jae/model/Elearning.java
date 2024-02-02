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

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Elearning")
public class Elearning {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 10, nullable = false)
    private String grade;
    
    @Column(length = 200, nullable = false)
    private String name;
    
    @CreationTimestamp
    private LocalDate registerDate;
    
}
