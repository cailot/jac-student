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
import javax.persistence.ForeignKey;
import javax.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="AssessmentAnswer")
public class AssessmentAnswer {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @ElementCollection
    @CollectionTable(name = "AssessmentAnswerCollection",
    joinColumns = @JoinColumn(name="AssessmentAnswer_id", foreignKey = @ForeignKey(name="FK_AssessmentAnswerCollection_AssessmentAnswer"))) // Set the custom table name
    private List<AssessmentAnswerItem> answers = new ArrayList<>();
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentId", foreignKey = @ForeignKey(name = "FK_AssessmentAnswer_Assessment"))
    private Assessment assessment;
    
}
