package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.model.Clazz;

public interface ClazzRepository extends JpaRepository<Clazz, Long> {

	// list all class for grade
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE c.course.grade = ?1")
	List<ClazzDTO> findClassForGrade(String grade);

	// list all class for year
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE c.course.cycle.year = ?1")
	List<ClazzDTO> findClassForYear(int year);

	// list all class for grade & year
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE c.course.grade = ?1 AND c.course.cycle.year = ?2")
	List<ClazzDTO> findClassForGradeNCycle(String grade, int year);

	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE c.course.id = ?1 AND c.course.cycle.year = ?2")
	List<ClazzDTO> findClassForCourseIdNCycle(Long id, int year);

	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE c.course.id = ?1 AND c.course.cycle.year = ?2 AND c.state = ?3 AND c.branch = ?4")
	List<ClazzDTO> findClassForCourseIdNCycleNStateNBranch(Long id, int year, String state, String branch);

	// list all class for state, branch, grade
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3)")
	List<ClazzDTO> findClassForStateNBranchNGrade(String state, String branch, String grade);

	// list onsite class for state, branch, grade
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND c.course.online = 0")
	List<ClazzDTO> findOnsiteClassForStateNBranchNGrade(String state, String branch, String grade);

	// list online class for state, branch, grade
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND c.course.online = 1")
	List<ClazzDTO> findOnlineClassForStateNBranchNGrade(String state, String branch, String grade);

	// list onsite class for state, branch, grade
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND (c.course.online = false)")
	List<ClazzDTO> findOnSiteClassForStateNBranchNGrade(String state, String branch, String grade);

	// list onsite class for state, branch, grade, year on Teacher page
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND (?4 = 0 OR c.course.cycle.year = ?4) AND (c.course.online = false)")
	List<ClazzDTO> findOnSiteClassForStateNBranchNGradeNYear(String state, String branch, String grade, int year);

	// list class Ids for state, branch, grade
	@Query("SELECT c.id FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3)")
	List<Long> findClassIdsForStateNBranchNGrade(String state, String branch, String grade);

	// list all class for state, branch, grade, year
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND c.course.cycle.year = ?4")
	List<ClazzDTO> findClassForStateNBranchNGradeNYear(String state, String branch, String grade, int year);

	// list all class for state, branch, grade, year
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND c.course.cycle.year = ?4 AND c.course.online = 0")
	List<ClazzDTO> findOnsiteClassForStateNBranchNGradeNYear(String state, String branch, String grade, int year);

	// list all class for state, branch, grade, year
	@Query("SELECT new hyung.jin.seo.jae.dto.ClazzDTO(c.id, c.state, c.branch, c.course.price, c.day, c.name, c.startDate, c.active, c.course.id, c.course.grade, c.course.online, c.course.description, c.course.cycle.year) FROM Clazz c WHERE (?1 = '0' OR c.state = ?1) AND (?2 = '0' OR c.branch = ?2) AND (?3 = '0' OR c.course.grade = ?3) AND c.course.cycle.year = ?4 AND c.course.online = 1")
	List<ClazzDTO> findOnlineClassForStateNBranchNGradeNYear(String state, String branch, String grade, int year);

	// get price by class id
	//@Query(value = "SELECT cos.price FROM Course cos where cos.id = (SELECT c.courseId FROM Class c WHERE c.id = :clazzId)", nativeQuery = true)
	// @Query(value = "SELECT c.course.price FROM Class c where c.id = :clazzId", nativeQuery = true)
	// double getPrice(Long clazzId);

	@Query("SELECT c.course.price FROM Clazz c WHERE c.id = ?1")
	double getPrice(Long clazzId);

	// get academic year by class id
	@Query(value = "SELECT c.course.cycle.year FROM Clazz c where c.id = ?1")
	int getYear(Long clazzId);

	// get grade by class id
	@Query(value = "SELECT c.course.grade FROM Clazz c where c.id = ?1")
	String getGrade(Long clazzId);

	// get day by class id
	@Query(value = "SELECT c.day FROM Clazz c where c.id = ?1")
	String getDay(Long clazzId);

	// get name by class id
	@Query(value = "SELECT c.name FROM Clazz c where c.id = ?1")
	String getName(Long clazzId);

	// get class id by grade and year
	@Query(value = "SELECT c.id FROM Clazz c where c.course.grade = ?1 AND c.course.cycle.year = ?2 AND c.course.online = true")
	Long getOnlineClazzId(String grade, int year);

	// get class by grade and year
	@Query(value = "SELECT c FROM Clazz c where c.course.grade = :grade AND c.course.cycle.year = :year AND c.course.online = true")
	Optional<Clazz> getClazz4OnlineSession(@Param("grade") String grade, @Param("year") int year);
	
}