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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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

   @Column(columnDefinition = "DECIMAL(10,2)")
	private double price;
    
    @Column(length = 400, nullable = false)
    private String description;

	@Column
    private boolean active = true;

	@Column
	private boolean online;
    
    @CreationTimestamp
    private LocalDate registerDate;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "CourseSubjectLink",
		joinColumns = { @JoinColumn(name = "courseId", foreignKey = @ForeignKey(name = "FK_CourseSubjectLink_Course")) },
		inverseJoinColumns = { @JoinColumn(name = "subjectId", foreignKey = @ForeignKey(name = "FK_CourseSubjectLink_Subject")) }
	)
	private List<Subject> subjects = new ArrayList<>();

	public void addSubject(Subject sub){
		subjects.add(sub);
	}

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private Set<Clazz> classes = new LinkedHashSet<>();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cycleId", foreignKey = @ForeignKey(name = "FK_Course_Cycle"))
	private Cycle cycle;
}
