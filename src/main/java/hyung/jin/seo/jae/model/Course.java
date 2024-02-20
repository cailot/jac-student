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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Course")
public class Course{
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 100, nullable = false)
    private String name;
    
    @Column(length = 10, nullable = false)
    private String grade;
    
    @Column(length = 400, nullable = false)
    private String description;

	@Column
	private boolean online;
    
    @CreationTimestamp
    private LocalDate registerDate;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "Course_Subject",
		joinColumns = { @JoinColumn(name = "courseId")},
		inverseJoinColumns = { @JoinColumn(name = "subjectId")}
	)
	private Set<Subject> subjects = new LinkedHashSet<>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private Set<Clazz> classes = new LinkedHashSet<>();


}
