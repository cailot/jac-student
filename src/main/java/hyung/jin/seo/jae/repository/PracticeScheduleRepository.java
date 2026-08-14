package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.PracticeScheduleDTO;
import hyung.jin.seo.jae.model.PracticeSchedule;

public interface PracticeScheduleRepository extends JpaRepository<PracticeSchedule, Long>{  
	
	List<PracticeSchedule> findAll();

	@Query("SELECT new hyung.jin.seo.jae.dto.PracticeScheduleDTO(p.id, p.fromDatetime, p.toDatetime, p.grade, p.practiceGroup, p.week, p.info, p.active, p.registerDate) " +
	"FROM PracticeSchedule p WHERE " +
	"(p.practiceGroup = '0' OR p.practiceGroup LIKE CONCAT('%,', :practiceGroup, ',%') OR p.practiceGroup LIKE CONCAT(:practiceGroup, ',%') OR p.practiceGroup LIKE CONCAT('%,', :practiceGroup) OR p.practiceGroup = :practiceGroup) " +
	"AND (p.grade = '0' OR p.grade LIKE CONCAT('%,', :grade, ',%') OR p.grade LIKE CONCAT(:grade, ',%') OR p.grade LIKE CONCAT('%,', :grade) OR p.grade = :grade) " +
	"AND (FUNCTION('STR_TO_DATE', :nowStr, '%Y-%m-%d %H:%i:%s') BETWEEN p.fromDatetime AND p.toDatetime) AND (p.active = true)")
	List<PracticeScheduleDTO> getPracticeScheduleByGroupAndGrade(@Param("practiceGroup") String practiceGroup, @Param("grade") String grade, @Param("nowStr") String nowStr);
}

