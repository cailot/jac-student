package hyung.jin.seo.jae.model;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ForeignKey;
import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Branch")
public class Branch {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 2, nullable = false)
    private String code;
    
    @Column(length = 25, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 50, nullable = false)
    private String email;

    // @Column(length = 70)
    // private String password;

    @Column(length = 100, nullable = true)
    private String address;

    @Column(length = 15, nullable = true)
    private String abn;

    @Column(length = 50, nullable = true)
    private String bank;

    @Column(length = 10, nullable = true)
    private String bsb;

    @Column(length = 15, nullable = true)
    private String accountNumber;

    @Column(length = 50, nullable = true)
    private String accountName;

    @Column(length = 1000, nullable = true)
    private String info;

    @CreationTimestamp
    private LocalDate registerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateId", foreignKey = @ForeignKey(name = "FK_Branch_State"))
    private State state;

 }
