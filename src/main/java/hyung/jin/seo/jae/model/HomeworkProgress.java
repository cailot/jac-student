package hyung.jin.seo.jae.model;

import java.time.LocalDate;
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

import javax.persistence.Column;
import javax.persistence.ForeignKey;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="HomeworkProgress")
public class HomeworkProgress{ // bridge table between Student & Practice
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "studentId", foreignKey = @ForeignKey(name = "FK_HomeworkProgress_Student"))
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "homeworkId", foreignKey = @ForeignKey(name = "FK_HomeworkProgress_Homework"))
	private Homework homework;
	
	@CreationTimestamp
    private LocalDate registerDate;

	@Column
	private int percentage;

}
