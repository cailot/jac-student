package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	List<Teacher> findAll();

	List<Teacher> findAllByEndDateIsNull();

	List<Teacher> findAllByEndDateIsNotNull();

	Optional<Teacher> findById(Long id);

	long count();

	// search by state
	@Query("SELECT t FROM Teacher t WHERE t.state = :state")
	List<Teacher> findByState(@Param("state") String state);

	// search by branch
	@Query("SELECT t FROM Teacher t WHERE t.branch = :branch")
	List<Teacher> findByBranch(@Param("branch") String branch);

	// search by state & branch
	@Query("SELECT t FROM Teacher t WHERE t.state = :state AND t.branch = :branch")
	List<Teacher> findByStateAndBranch(@Param("state") String state, @Param("branch") String branch);

	// get clazz id by taecher id
	@Query(value = "SELECT clazzId FROM Teacher_Class WHERE teacherId = ?1", nativeQuery = true)
	List<Long> findClazzIdByTeacherId(Long id);

}
