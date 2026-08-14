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
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OnlineActivity")
public class OnlineActivity {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @ManyToOne
	@JoinColumn(name = "studentId", foreignKey = @ForeignKey(name = "FK_OnlineActivity_Student"))
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = "onlineSessionId", foreignKey = @ForeignKey(name = "FK_OnlineActivity_OnlineSession"))
	private OnlineSession onlineSession;
    
    @Column
    private int status;

    @CreationTimestamp
    private LocalDate registerDate;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    @Column(name = "onlineFileName", length = 255)
    private String onlineFileNm;
 }
