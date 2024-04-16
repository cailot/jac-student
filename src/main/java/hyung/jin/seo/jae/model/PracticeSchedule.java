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
@Table(name="PracticeSchedule")
public class PracticeSchedule {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 4, nullable = true)
    private Integer year;

    @Column(length = 2, nullable = true)
    private Integer week;

    @Column(length = 50, nullable = false)
    private String info;

    @Column
    private boolean active;
    
    @CreationTimestamp
    private LocalDate registerDate;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "PracticeScheculeLink",
		joinColumns = { @JoinColumn(name = "practiceScheduleId")},
		inverseJoinColumns = { @JoinColumn(name = "practiceId")}
	)
	private Set<Practice> practices = new LinkedHashSet();

    public void addPractice(Practice practice){
        practices.add(practice);
    }

 }
