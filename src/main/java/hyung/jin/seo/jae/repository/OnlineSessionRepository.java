package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.OnlineSession;

public interface OnlineSessionRepository extends JpaRepository<OnlineSession, Long> {

	// bring OnlineSessionDTO by clazz id
	@Query("SELECT new hyung.jin.seo.jae.dto.OnlineSessionDTO(o.id, o.active, o.week, o.address, o.clazz.course.grade, o.day, o.startTime, o.endTime, o.clazz.cycle.year, o.registerDate, o.clazz.id) FROM OnlineSession o WHERE o.clazz.id = ?1")
	List<OnlineSessionDTO> findOnlineSessionByClazzId(long clazzId);

	// bring active OnlineSessionDTO by clazz id
	@Query("SELECT new hyung.jin.seo.jae.dto.OnlineSessionDTO(o.id, o.active, o.week, o.address, o.clazz.course.grade, o.day, o.startTime, o.endTime, o.clazz.cycle.year, o.registerDate, o.clazz.id) FROM OnlineSession o WHERE o.clazz.id = ?1 AND o.active=true")
	List<OnlineSessionDTO> findActiveOnlineSessionByClazzId(long clazzId); 

	// bring inactive OnlineSessionDTO by clazz id
	@Query("SELECT new hyung.jin.seo.jae.dto.OnlineSessionDTO(o.id, o.active, o.week, o.address, o.clazz.course.grade, o.day, o.startTime, o.endTime, o.clazz.cycle.year, o.registerDate, o.clazz.id) FROM OnlineSession o WHERE o.clazz.id = ?1 AND o.active=false")
	List<OnlineSessionDTO> findInactiveOnlineSessionByClazzId(long clazzId);

	// filter OnlineSessionDTO by grade
	@Query("SELECT new hyung.jin.seo.jae.dto.OnlineSessionDTO(o.id, o.active, o.week, o.address, o.clazz.course.grade, o.day, o.startTime, o.endTime, o.clazz.cycle.year, o.registerDate, o.clazz.id) FROM OnlineSession o WHERE (?1 = 'All' OR o.clazz.course.grade = ?1)")
	List<OnlineSessionDTO> filterOnlineSessionByGrade(String grade);

	// filter OnlineSessionDTO by year
	@Query("SELECT new hyung.jin.seo.jae.dto.OnlineSessionDTO(o.id, o.active, o.week, o.address, o.clazz.course.grade, o.day, o.startTime, o.endTime, o.clazz.cycle.year, o.registerDate, o.clazz.id) FROM OnlineSession o WHERE o.clazz.cycle.year = ?1")
	List<OnlineSessionDTO> filterOnlineSessionByYear(int year);

	// filter OnlineSessionDTO by grade & year
	@Query("SELECT new hyung.jin.seo.jae.dto.OnlineSessionDTO(o.id, o.active, o.week, o.address, o.clazz.course.grade, o.day, o.startTime, o.endTime, o.clazz.cycle.year, o.registerDate, o.clazz.id) FROM OnlineSession o WHERE (?1 = 'All' OR o.clazz.course.grade = ?1) AND (o.clazz.cycle.year = ?2)")
	List<OnlineSessionDTO> filterOnlineSessionByGradeNYear(String grade, int year);
	
    // get Online by clazz Id and week
    @Query("SELECT o FROM OnlineSession o WHERE o.clazz.id = ?1 AND o.week = ?2 AND o.active = true")
	OnlineSessionDTO getOnlineSessionByClazzNWeek(@Param("clazzId") long clazzId, @Param("week") int week);

}
