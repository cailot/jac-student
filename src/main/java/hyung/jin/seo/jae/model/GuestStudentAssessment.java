package hyung.jin.seo.jae.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="GuestStudentAssessment")
public class GuestStudentAssessment{ // bridge table between GuestStudent & Assessment
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
	@ManyToOne
	@JoinColumn(name = "guestStudentId")
	private GuestStudent guestStudent;
	
	@ManyToOne
	@JoinColumn(name = "assessmentId")
	private Assessment assessment;

	@CreationTimestamp
    private LocalDate registerDate;

	@Column
	private double score;

	// @ElementCollection(fetch=FetchType.LAZY)
    // @CollectionTable(name = "GuestStudentAssessmentAnswerCollection") // Set the custom table name
    // private List<Integer> answers = new ArrayList<>();
	@ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name = "GuestStudentAssessmentAnswerCollection", joinColumns = @JoinColumn(name = "guestStudentAssessment_id"))
    @Column(name = "answers")
    private List<Integer> answers = new ArrayList<>();
}
