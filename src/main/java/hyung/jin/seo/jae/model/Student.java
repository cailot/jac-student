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
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Student")
public class Student {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 100, nullable = false)
    private String firstName;
    
    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 70, nullable = true) // exsting data has no password
    private String password;
    
    @Column(length = 1, nullable = false)
    private int active;
    
    @Column(length = 10, nullable = false)
    private String grade;
    
    @Column(length = 50, nullable = true)
    private String contactNo1;
    
    @Column(length = 50, nullable = true)
    private String contactNo2;
    
    @Column(length = 100, nullable = true)
    private String email1;
    
    @Column(length = 100, nullable = true)
    private String email2;

    @Column(length = 10, nullable = true)
    private String relation1;

    @Column(length = 10, nullable = true)
    private String relation2;
    
    @Column(length = 200, nullable = true)
    private String address;
    
    @Column(length = 30, nullable = true)
    private String state;
    
    @Column(length = 50, nullable = true)
    private String branch;
    
    @Column(length = 1000, nullable = true)
    private String memo;
    
    @Column(length = 7, nullable = true)
    private String gender;
        
    @CreationTimestamp
    private LocalDate registerDate;
    
    @CreatedDate
    private LocalDate endDate;

    @OneToMany(mappedBy = "student", cascade = {
     	CascadeType.PERSIST,
		CascadeType.MERGE,
		CascadeType.REFRESH,
		CascadeType.DETACH
	})
    private Set<Enrolment> enrolments = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = {
     	CascadeType.PERSIST,
		CascadeType.MERGE,
		CascadeType.REFRESH,
		CascadeType.DETACH
	})
    private Set<Attendance> attendances = new HashSet<>();

}
