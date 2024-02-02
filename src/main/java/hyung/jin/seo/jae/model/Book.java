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
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Book")
public class Book {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    
    @Column(length = 30, nullable = false)
    private String name;
    
    @Column(length = 10, nullable = false)
    private String grade;
    
    @Column(columnDefinition = "DECIMAL(10,2)")
    private double price;
    
    @CreationTimestamp
    private LocalDate registerDate;

    @OneToMany(mappedBy = "book", cascade = {
	    CascadeType.PERSIST,
	    CascadeType.MERGE,
	    CascadeType.REFRESH,
	    CascadeType.DETACH
   	})
    private Set<Material> materials = new HashSet<>();

    public void addMaterial(Material material){
    	materials.add(material);
    }


	
}
