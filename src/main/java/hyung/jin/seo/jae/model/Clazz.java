package hyung.jin.seo.jae.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.ForeignKey;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.CascadeType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Class")
public class Clazz{ // bridge table between Course & Cycle
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "courseId", foreignKey = @ForeignKey(name = "FK_Class_Course"))
	private Course course;

	@CreationTimestamp
    private LocalDate registerDate;

	@Column(length = 30)
    private String state;

	@Column(length = 50)
    private String branch;

	@Column(length = 100)
    private String name;

	@CreatedDate
    private LocalDate startDate;

	@Column(length = 10)
    private String day;

	@Column
	private boolean active;

	@OneToMany(mappedBy = "clazz", cascade = {
		CascadeType.PERSIST,
	   CascadeType.MERGE,
	   CascadeType.REFRESH,
	   CascadeType.DETACH
   	})
    private Set<Enrolment> enrolments = new HashSet<>();

	@OneToMany(mappedBy = "clazz", cascade = {
		CascadeType.PERSIST,
	   CascadeType.MERGE,
	   CascadeType.REFRESH,
	   CascadeType.DETACH
   	})
    private Set<Attendance> attendances = new HashSet<>();

}
