package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.StudentPracticeDTO;
import hyung.jin.seo.jae.model.StudentPractice;

public interface StudentPracticeRepository extends JpaRepository<StudentPractice, Long> {


	@SuppressWarnings("null")
	List<StudentPractice> findAll();
	
	@SuppressWarnings("null")
	Optional<StudentPractice> findById(Long id);

    @Query("SELECT new hyung.jin.seo.jae.dto.StudentPracticeDTO(" +
            "sp.id, " +
            "sp.registerDate, " +
            "sp.score, " +
            "sp.student.id, " +
            "sp.practice.id, " +
            "sp.answers) " +
            "FROM StudentPractice sp " +
            "WHERE sp.student.id = :studentId AND sp.practice.id = :practiceId")
    StudentPracticeDTO findStudentPractice(
            @Param("studentId") Long studentId,
            @Param("practiceId") Long practiceId
    );	// // bring latest EnrolmentDTO by student id, called from retrieveEnrolment() in
	// // courseInfo.jsp
	// check whether there is a record in StudentPractice table by studentId and practiceId
	Optional<StudentPractice> findByStudentIdAndPracticeId(Long studentId, Long practiceId);
	
	// delete existing record in StudentPracice table by studentId and practiceId
	void deleteByStudentIdAndPracticeId(Long studentId, Long practiceId);

}
