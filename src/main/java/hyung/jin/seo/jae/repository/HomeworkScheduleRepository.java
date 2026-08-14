package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.HomeworkScheduleDTO;
import hyung.jin.seo.jae.model.HomeworkSchedule;

public interface HomeworkScheduleRepository extends JpaRepository<HomeworkSchedule, Long>{  
	
	@SuppressWarnings("null")
	List<HomeworkSchedule> findAll();
	
	@SuppressWarnings("null")
	Optional<HomeworkSchedule> findById(Long id);
	
	@Query("SELECT new hyung.jin.seo.jae.dto.HomeworkScheduleDTO(h.id, h.fromDatetime, h.toDatetime, h.grade, h.subject, h.subjectDisplay, h.answerDisplay, h.info, h.active, h.registerDate) " +
	"FROM HomeworkSchedule h WHERE h.fromDatetime BETWEEN FUNCTION('STR_TO_DATE', :fromStr, '%Y-%m-%d %H:%i:%s') AND FUNCTION('STR_TO_DATE', :toStr, '%Y-%m-%d %H:%i:%s') " +
	"ORDER BY h.id")
	List<HomeworkScheduleDTO> filterHomeworkScheduleByYear(@Param("fromStr") String fromStr, @Param("toStr") String toStr);

	@Query("SELECT new hyung.jin.seo.jae.dto.HomeworkScheduleDTO(h.id, h.fromDatetime, h.toDatetime, h.grade, h.subject, h.subjectDisplay, h.answerDisplay, h.info, h.active, h.registerDate) " +
	"FROM HomeworkSchedule h WHERE " +
	"(h.subject = '0' OR h.subject LIKE CONCAT('%,', :subject, ',%') OR h.subject LIKE CONCAT(:subject, ',%') OR h.subject LIKE CONCAT('%,', :subject) OR h.subject = :subject) " +
	"AND (h.grade = '0' OR h.grade LIKE CONCAT('%,', :grade, ',%') OR h.grade LIKE CONCAT(:grade, ',%') OR h.grade LIKE CONCAT('%,', :grade) OR h.grade = :grade) " +
	"AND (FUNCTION('STR_TO_DATE', :nowStr, '%Y-%m-%d %H:%i:%s') BETWEEN h.fromDatetime AND h.toDatetime) AND (h.active = true)")
	List<HomeworkScheduleDTO> getHomeworkScheduleBySubjectAndGrade(@Param("subject") String subject, @Param("grade") String grade, @Param("nowStr") String nowStr, Pageable pageable);
}
