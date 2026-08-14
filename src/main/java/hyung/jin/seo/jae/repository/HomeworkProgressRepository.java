package hyung.jin.seo.jae.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.HomeworkProgressDTO;
import hyung.jin.seo.jae.model.HomeworkProgress;

public interface HomeworkProgressRepository extends JpaRepository<HomeworkProgress, Long> {

	@SuppressWarnings("null")
	Optional<HomeworkProgress> findById(Long id);

        // bring HomeworkProgressDTO by student & homework
	@Query("SELECT new hyung.jin.seo.jae.dto.HomeworkProgressDTO(h.id, h.registerDate, h.percentage, h.student.id, h.homework.id) FROM HomeworkProgress h WHERE (h.student.id = ?1) AND (h.homework.id = ?2) ")
	HomeworkProgressDTO getHomeworkProgress(Long studentId, Long homeworkId);

        // bring HomeworkProgress by student & homework
        @Query("SELECT h FROM HomeworkProgress h WHERE h.student.id = :studentId AND h.homework.id = :homeworkId")
        HomeworkProgress findHomeworkProgressByStudentAndHomework(@Param("studentId") Long studentId, @Param("homeworkId") Long homeworkId);

        // get percentage of HomeworkProgress by student & homework
        @Query("SELECT h.percentage FROM HomeworkProgress h WHERE h.student.id = :studentId AND h.homework.id = :homeworkId")
        int getPercentageByStudentAndHomework(@Param("studentId") Long studentId, @Param("homeworkId") Long homeworkId);
        
}
