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
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column
	private boolean active;
    
    @CreationTimestamp
    private LocalDate registerDate;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "BookSubjectLink",
		joinColumns = { @JoinColumn(name = "bookId", foreignKey = @ForeignKey(name = "FK_BookSubjectLink_Book")) },
		inverseJoinColumns = { @JoinColumn(name = "subjectId", foreignKey = @ForeignKey(name = "FK_BookSubjectLink_Subject")) }
	)
	private List<Subject> subjects = new ArrayList<>();

    public void addSubject(Subject sub){
		subjects.add(sub);
	}

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
