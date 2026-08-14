package hyung.jin.seo.jae.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.ExtraworkProgressDTO;
import hyung.jin.seo.jae.model.ExtraworkProgress;

public interface ExtraworkProgressRepository extends JpaRepository<ExtraworkProgress, Long> {

	@SuppressWarnings("null")
	Optional<ExtraworkProgress> findById(Long id);

        // bring ExtraworkProgressDTO by student & extrawork
	@Query("SELECT new hyung.jin.seo.jae.dto.ExtraworkProgressDTO(h.id, h.registerDate, h.percentage, h.student.id, h.extrawork.id) FROM ExtraworkProgress h WHERE (h.student.id = ?1) AND (h.extrawork.id = ?2) ")
	ExtraworkProgressDTO getExtraworkProgress(Long studentId, Long extraworkId);

        // bring ExtraworkProgress by student & extrawork
        @Query("SELECT h FROM ExtraworkProgress h WHERE h.student.id = :studentId AND h.extrawork.id = :extraworkId")
        ExtraworkProgress findExtraworkProgressByStudentAndHomework(@Param("studentId") Long studentId, @Param("extraworkId") Long extraworkId);

        // get percentage of HomeworkProgress by student & extrawork
        @Query("SELECT h.percentage FROM ExtraworkProgress h WHERE h.student.id = :studentId AND h.extrawork.id = :extraworkId")
        int getPercentageByStudentAndHomework(@Param("studentId") Long studentId, @Param("extraworkId") Long extraworkId);
        
}
