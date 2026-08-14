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

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OnlineSession")
public class OnlineSession {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column
    private boolean active;
    
    // @Column(length = 10, nullable = false)
    // private String grade; // it comes from clazz.course.grade
    
    @Column(length = 500, nullable = true)
    private String address;
    
    @Column(length = 10, nullable = true)
    private String day;
    
    @Column(length = 50, nullable = true)
    private String title;

    @Column(length = 20, nullable = true)
    private String startTime;

    @Column(length = 20, nullable = true)
    private String endTime;

    // @Column(length = 10, nullable = true)
    // private int year; // it comes from clazz.cycle.year
    
    @Column(length = 5, nullable = true)
    private int week;
        
    @CreationTimestamp
    private LocalDate registerDate;

    @ManyToOne
    @JoinColumn(name = "clazzId", foreignKey = @ForeignKey(name = "FK_OnlineSession_Clazz"))
    private Clazz clazz;

}
