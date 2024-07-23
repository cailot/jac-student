package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.GuestStudentAssessmentDTO;
import hyung.jin.seo.jae.model.GuestStudentAssessment;

public interface GuestStudentAssessmentRepository extends JpaRepository<GuestStudentAssessment, Long> {


	@SuppressWarnings("null")
	List<GuestStudentAssessment> findAll();
	
	@SuppressWarnings("null")
	Optional<GuestStudentAssessment> findById(Long id);

        @Query("SELECT new hyung.jin.seo.jae.dto.GuestStudentAssessmentDTO(" +
                "st.id, " +
                "st.registerDate, " +
                "st.score, " +
                "st.guestStudent.id, " +
                "st.assessment.id, " +
                "st.answers) " +
                "FROM GuestStudentAssessment st " +
                "WHERE st.guestStudent.id = :studentId AND st.assessment.id = :assessId")
        GuestStudentAssessmentDTO findStudentTest(@Param("studentId") Long studentId, @Param("assessId") Long assessId);	// // bring latest EnrolmentDTO by student id, called from retrieveEnrolment() in
	// // courseInfo.jsp
	// check whether there is a record in GuestStudentAssessment table by studentId and assessId
	Optional<GuestStudentAssessment> findByGuestStudentAndAssessment(Long studentId, Long assessId);
	
	// delete existing record in GuestStudentAssessment table by studentId and assessId
        @Modifying
	void deleteByGuestStudentAndAssessment(Long studentId, Long assessId);

        @Query("SELECT new hyung.jin.seo.jae.dto.GuestStudentAssessmentDTO(st.id, st.registerDate, st.score, st.guestStudent.id, st.assessment.id, st.assessment.subject.name) FROM GuestStudentAssessment st WHERE st.guestStudent.id = :studentId")
        List<GuestStudentAssessmentDTO> findStudentTestResult(@Param("studentId") Long studentId);	// // bring latest EnrolmentDTO by student id, called from retrieveEnrolment() in


        // @Query("SELECT new hyung.jin.seo.jae.dto.GuestStudentAssessmentDTO(st.id, st.registerDate, st.score, st.guestStudent.id, st.assessment.id) FROM GuestStudentAssessment st WHERE st.guestStudent.id = :studentId")
        // List<GuestStudentAssessmentDTO> findStudentTestResult(@Param("studentId") Long studentId);	// // bring latest EnrolmentDTO by student id, called from retrieveEnrolment() in


        @Query("SELECT st.assessment.subject.abbr FROM GuestStudentAssessment st WHERE st.guestStudent.id = :studentId")
        List<String> findSujbectByGuestStudent(@Param("studentId") Long studentId);	// // bring latest EnrolmentDTO by student id, called from retrieveEnrolment() in

}
