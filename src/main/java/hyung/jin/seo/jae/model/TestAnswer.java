package hyung.jin.seo.jae.model;

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
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.ForeignKey;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TestAnswer")
public class TestAnswer {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(length = 300, nullable = true)
    private String videoPath;

    @Column(length = 300, nullable = true)
    private String pdfPath;

    @ElementCollection
    @CollectionTable(name = "TestAnswerCollection", 
    joinColumns = @JoinColumn(name="TestAnswer_id", foreignKey = @ForeignKey(name="FK_TestAnswerCollection_TestAnswer"))) // Set the custom table name
    private List<TestAnswerItem> answers = new ArrayList<>();
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testId", foreignKey = @ForeignKey(name = "FK_TestAnswer_Test"))
    private Test test;
    
}
