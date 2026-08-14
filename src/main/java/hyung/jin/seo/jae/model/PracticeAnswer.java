package hyung.jin.seo.jae.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="PracticeAnswer")
public class PracticeAnswer {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(length = 300, nullable = true)
    private String videoPath;

    @Column(length = 300, nullable = true)
    private String pdfPath;

    @Column
    private int answerCount;

    @ElementCollection
    @CollectionTable(name = "PracticeAnswerCollection",
    joinColumns = @JoinColumn(name="PracticeAnswer_id", foreignKey = @ForeignKey(name="FK_PracticeAnswerCollection_PracticeAnswer"))) // Set the custom table name
    private List<Integer> answers = new ArrayList<>();
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practiceId", foreignKey = @ForeignKey(name = "FK_PracticeAnswer_Practice"))
    private Practice practice;

}
