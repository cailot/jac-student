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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Payment")
public class Payment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
     
    @Column(length = 10, nullable = false)
    private String method;
    
    @Column(length = 100)
    private String info;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private double amount;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private double total;
    
    @CreationTimestamp
    private LocalDate registerDate;
	
}
