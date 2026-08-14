package hyung.jin.seo.jae.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;


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
@Table(name="Cycle")
public class Cycle {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
	@Column(length = 4, nullable = false)
    private Integer year;
	
	@Column(length = 200, nullable = true)
    private String description;
    
    @CreatedDate
    private LocalDate startDate;
    
    @CreatedDate
    private LocalDate endDate;

    @CreatedDate
    private LocalDate vacationStartDate;
    
    @CreatedDate
    private LocalDate vacationEndDate;
    
    @CreationTimestamp
    private LocalDate registerDate;
    
    // @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL)
	// private Set<Clazz> classes = new LinkedHashSet<>();

}
