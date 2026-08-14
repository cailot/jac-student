package hyung.jin.seo.jae.model;

import java.time.LocalDate;

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
import javax.persistence.Table;
import javax.persistence.ForeignKey;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Attendance")
public class Attendance{ // bridge table between Student & Class
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
	@ManyToOne
	@JoinColumn(name = "studentId", foreignKey = @ForeignKey(name = "FK_Attendance_Student"))
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = "clazzId", foreignKey = @ForeignKey(name = "FK_Attendance_Clazz"))
	private Clazz clazz;

	@Column
    private LocalDate attendDate;

	@Column(length = 5)
    private String status;

	@Column(length = 10)
    private String day;

	@Column(length = 7)
    private String week;

	@Column(length = 100)
    private String info;

}
