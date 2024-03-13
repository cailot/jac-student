package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.StudentTestDTO;
import hyung.jin.seo.jae.model.StudentTest;

public interface StudentTestRepository extends JpaRepository<StudentTest, Long> {


	@SuppressWarnings("null")
	List<StudentTest> findAll();
	
	@SuppressWarnings("null")
	Optional<StudentTest> findById(Long id);

        @Query("SELECT new hyung.jin.seo.jae.dto.StudentTestDTO(" +
                "st.id, " +
                "st.registerDate, " +
                "st.score, " +
                "st.student.id, " +
                "st.test.id, " +
                "st.answers) " +
                "FROM StudentTest st " +
                "WHERE st.student.id = :studentId AND st.test.id = :testId")
        StudentTestDTO findStudentTest(
                @Param("studentId") Long studentId,
                @Param("testId") Long testId
        );	// // bring latest EnrolmentDTO by student id, called from retrieveEnrolment() in
	// // courseInfo.jsp
	// check whether there is a record in StudentTest table by studentId and testId
	Optional<StudentTest> findByStudentIdAndTestId(Long studentId, Long testId);
	
	// delete existing record in StudentTest table by studentId and testId
	void deleteByStudentIdAndTestId(Long studentId, Long testId);

}
