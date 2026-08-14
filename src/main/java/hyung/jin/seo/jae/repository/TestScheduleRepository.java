package hyung.jin.seo.jae.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.TestScheduleDTO;
import hyung.jin.seo.jae.model.TestSchedule;

public interface TestScheduleRepository extends JpaRepository<TestSchedule, Long>{  
	
	List<TestSchedule> findAll();

	// filter TestScheduleDTO by time & testGroup
	@Query("SELECT new hyung.jin.seo.jae.dto.TestScheduleDTO(t.id, t.fromDatetime, t.toDatetime, t.grade, t.testGroup, t.week, t.info, t.active, t.registerDate, t.resultDate, t.explanationFromDatetime, t.explanationToDatetime) " +
	"FROM TestSchedule t " +
	"WHERE t.fromDatetime BETWEEN FUNCTION('STR_TO_DATE', :fromStr, '%Y-%m-%d %H:%i:%s') AND FUNCTION('STR_TO_DATE', :toStr, '%Y-%m-%d %H:%i:%s') " +
	"AND (:testGroup = '0' OR t.testGroup = :testGroup OR t.testGroup LIKE CONCAT('%,', :testGroup, ',%') OR t.testGroup LIKE CONCAT(:testGroup, ',%') OR t.testGroup LIKE CONCAT('%,', :testGroup))")
	List<TestScheduleDTO> filterTestScheduleByTimeNGroup(@Param("fromStr") String fromStr, @Param("toStr") String toStr, @Param("testGroup") String testGroup);

	@Query("SELECT new hyung.jin.seo.jae.dto.TestScheduleDTO(t.id, t.fromDatetime, t.toDatetime, t.grade, t.testGroup, t.week, t.info, t.active, t.registerDate, t.explanationFromDatetime, t.explanationToDatetime) " +
	"FROM TestSchedule t WHERE " +
	"(t.testGroup = '0' OR t.testGroup LIKE CONCAT('%,', :testGroup, ',%') OR t.testGroup LIKE CONCAT(:testGroup, ',%') OR t.testGroup LIKE CONCAT('%,', :testGroup) OR t.testGroup = :testGroup) " +
	"AND (t.grade = '0' OR t.grade LIKE CONCAT('%,', :grade, ',%') OR t.grade LIKE CONCAT(:grade, ',%') OR t.grade LIKE CONCAT('%,', :grade) OR t.grade = :grade) " +
	"AND (FUNCTION('STR_TO_DATE', :nowStr, '%Y-%m-%d %H:%i:%s') BETWEEN t.fromDatetime AND t.toDatetime) AND (t.active = true)")
	List<TestScheduleDTO> getTestScheduleByGroupAndGrade(@Param("testGroup") String testGroup, @Param("grade") String grade, @Param("nowStr") String nowStr);

	@Query("SELECT new hyung.jin.seo.jae.dto.TestScheduleDTO(t.id, t.fromDatetime, t.toDatetime, t.grade, t.testGroup, t.week, t.info, t.active, t.registerDate, t.explanationFromDatetime, t.explanationToDatetime) " +
	"FROM TestSchedule t WHERE " +
	"t.testGroup = :testGroup " +
	"AND t.grade = :grade " +
	"AND (FUNCTION('STR_TO_DATE', :nowStr, '%Y-%m-%d %H:%i:%s') BETWEEN t.explanationFromDatetime AND t.explanationToDatetime) AND (t.active = true)")
	List<TestScheduleDTO> getTestSchedule4Explanation(@Param("testGroup") String testGroup, @Param("grade") String grade, @Param("nowStr") String nowStr);

	// @Query("SELECT new hyung.jin.seo.jae.dto.TestScheduleDTO(t.id, t.fromDatetime, t.toDatetime, t.grade, t.testGroup, t.week, t.info, t.active, t.registerDate, t.explanationFromDatetime, t.explanationToDatetime) " +
	// "FROM TestSchedule t WHERE " +
	// "(t.testGroup = '0' OR t.testGroup LIKE CONCAT('%,', :testGroup, ',%') OR t.testGroup LIKE CONCAT(:testGroup, ',%') OR t.testGroup LIKE CONCAT('%,', :testGroup) OR t.testGroup = :testGroup) " +
	// "AND (t.grade = '0' OR t.grade LIKE CONCAT('%,', :grade, ',%') OR t.grade LIKE CONCAT(:grade, ',%') OR t.grade LIKE CONCAT('%,', :grade) OR t.grade = :grade) " +
	// "AND (:now BETWEEN t.explanationFromDatetime AND t.explanationToDatetime) AND (t.active = true)")
	// List<TestScheduleDTO> getTestSchedule4Explanation(@Param("testGroup") String testGroup, @Param("grade") String grade, @Param("now") LocalDateTime now);

	// bring TestScheduleDTO by grade, week & testGroup - recent top 1 only 
	@Query("SELECT new hyung.jin.seo.jae.dto.TestScheduleDTO(t.id, t.fromDatetime, t.toDatetime, t.grade, t.testGroup, t.week, t.info, t.active, t.registerDate, t.explanationFromDatetime, t.explanationToDatetime) " +
	"FROM TestSchedule t WHERE " +
	"(t.testGroup = '0' OR t.testGroup LIKE CONCAT('%,', :testGroup, ',%') OR t.testGroup LIKE CONCAT(:testGroup, ',%') OR t.testGroup LIKE CONCAT('%,', :testGroup) OR t.testGroup = :testGroup) " +
	"AND (t.grade = '0' OR t.grade LIKE CONCAT('%,', :grade, ',%') OR t.grade LIKE CONCAT(:grade, ',%') OR t.grade LIKE CONCAT('%,', :grade) OR t.grade = :grade) " +
	"AND (t.week = '0' OR t.week LIKE CONCAT('%,', :week, ',%') OR t.week LIKE CONCAT(:week, ',%') OR t.week LIKE CONCAT('%,', :week) OR t.week = :week) " +
	"AND (t.active = true)")
	List<TestScheduleDTO> getTestScheduleByGroupNGradeNWeek(@Param("testGroup") String testGroup, @Param("grade") String grade, @Param("week") String week);

	// Bring TestScheduleDTO by grade, week & testGroup - recent top 1 only
	@Query("SELECT new hyung.jin.seo.jae.dto.TestScheduleDTO(t.id, t.fromDatetime, t.toDatetime, t.grade, t.testGroup, t.week, t.info, t.active, t.registerDate, t.explanationFromDatetime, t.explanationToDatetime) " +
	"FROM TestSchedule t WHERE (t.testGroup = '0' OR t.testGroup LIKE CONCAT('%,', :testGroup, ',%') OR t.testGroup LIKE CONCAT(:testGroup, ',%') OR t.testGroup LIKE CONCAT('%,', :testGroup) OR t.testGroup = :testGroup) " +
	"AND (t.grade = :grade) " +
	"AND (t.week = '0' OR t.week LIKE CONCAT('%,', :week, ',%') OR t.week LIKE CONCAT(:week, ',%') OR t.week LIKE CONCAT('%,', :week) OR t.week = :week) " +
	"AND (t.active = true) " +
	"ORDER BY t.registerDate DESC")
	TestScheduleDTO getTestScheduleByGroupNGradeNWeekTop1(@Param("testGroup") String testGroup, @Param("grade") String grade, @Param("week") String week);

	// Bring latest active explanation window by grade and testNo(week)
	@Query(value = "SELECT * FROM TestSchedule t " +
			"WHERE (t.grade = '0' OR t.grade LIKE CONCAT('%,', :grade, ',%') OR t.grade LIKE CONCAT(:grade, ',%') OR t.grade LIKE CONCAT('%,', :grade) OR t.grade = :grade) " +
			"AND (t.week = '0' OR t.week LIKE CONCAT('%,', :week, ',%') OR t.week LIKE CONCAT(:week, ',%') OR t.week LIKE CONCAT('%,', :week) OR t.week = :week) " +
			"AND t.active = 1 " +
			"AND t.explanationFromDatetime IS NOT NULL " +
			"AND t.explanationToDatetime IS NOT NULL " +
			"ORDER BY t.registerDate DESC, t.id DESC " +
			"LIMIT 1", nativeQuery = true)
	TestSchedule findLatestActiveExplanationWindowByGradeAndWeek(@Param("grade") String grade, @Param("week") String week);

}

