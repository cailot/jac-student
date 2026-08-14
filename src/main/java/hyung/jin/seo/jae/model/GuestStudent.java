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
@Table(name="GuestStudent")
public class GuestStudent {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 100, nullable = false)
    private String firstName;
    
    @Column(length = 100, nullable = false)
    private String lastName;
    
    @Column(length = 10, nullable = false)
    private String grade;
    
    @Column(length = 50, nullable = true)
    private String contactNo;
    
    @Column(length = 100, nullable = true)
    private String email;
    
    @Column(length = 10, nullable = true)
    private String state;
    
    @Column(length = 10, nullable = true)
    private String branch;
            
    @CreationTimestamp
    private LocalDate registerDate;

    // @OneToMany(mappedBy = "student", cascade = {
    //  	CascadeType.PERSIST,
	// 	CascadeType.MERGE,
	// 	CascadeType.REFRESH,
	// 	CascadeType.DETACH
	// })
    // private Set<Enrolment> enrolments = new HashSet<>();

}
