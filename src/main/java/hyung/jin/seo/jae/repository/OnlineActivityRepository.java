package hyung.jin.seo.jae.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.OnlineActivityDTO;
import hyung.jin.seo.jae.model.OnlineActivity;

public interface OnlineActivityRepository extends JpaRepository<OnlineActivity, Long>{  
    
    // find by student id and onlinesession id
    OnlineActivity findByStudentIdAndOnlineSessionId(Long studentId, Long onlineSessionId);

    // find latest activity in current week by student and file name
    OnlineActivity findTopByStudentIdAndOnlineFileNmAndRegisterDateBetweenOrderByIdDesc(
            Long studentId, String onlineFileNm, LocalDate startDate, LocalDate endDate);

    @Query("SELECT new hyung.jin.seo.jae.dto.OnlineActivityDTO(" +
    "CASE WHEN o.id IS NULL THEN 0L ELSE o.id END, " +
    "s.id, " +
    "s.firstName, " +
    "s.lastName, " +
    "s.grade, " +
    "s.contactNo1, " +
    "s.email1, " +
    "CASE WHEN os.id IS NULL THEN 0L ELSE os.id END, " +
    "CASE WHEN os.title IS NULL THEN '' ELSE os.title END, " +
    "CASE WHEN o.registerDate IS NULL THEN NULL ELSE o.registerDate END, " +
    "CASE WHEN os.week IS NULL THEN 0 ELSE os.week END, " +
    "CASE WHEN o.status IS NULL THEN 0 ELSE o.status END, " +
    "CASE WHEN o.startDateTime IS NULL THEN NULL ELSE o.startDateTime END, " +
    "CASE WHEN o.endDateTime IS NULL THEN NULL ELSE o.endDateTime END) " +
    "FROM Student s " +
    "LEFT JOIN OnlineActivity o ON o.student.id = s.id " +
    "LEFT JOIN OnlineSession os ON o.onlineSession.id = os.id " +
    "WHERE s.id = :studentId " +
    "AND os.id = :onlineSessionId")
    OnlineActivityDTO getStudentStatus(@Param("studentId") Long studentId, @Param("onlineSessionId") Long sessionId);
}