package hyung.jin.seo.jae.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.LoginActivityDTO;
import hyung.jin.seo.jae.model.LoginActivity;

public interface LoginActivityRepository extends JpaRepository<LoginActivity, Long>{  
    
    @Query("SELECT new hyung.jin.seo.jae.dto.LoginActivityDTO(la.studentId, s.firstName, s.lastName, s.grade, s.contactNo1, s.email1, s.registerDate, " +
       "SUM(CASE WHEN la.registerDate BETWEEN :startDate AND :endDate THEN 1 ELSE 0 END), " +
       "COUNT(la.id)) " +
       "FROM LoginActivity la " +
       "JOIN Student s ON la.studentId = s.id " +
       "WHERE (:branch = '0' OR s.branch = :branch)" + 
       "AND (:grade = '0' OR s.grade = :grade)" + 
       "AND s.active = 1 " +
       "GROUP BY la.studentId")
    List<LoginActivityDTO> findStudentLoginCounts(@Param("branch") String branch, @Param("grade") String grade, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
}
