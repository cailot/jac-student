package hyung.jin.seo.jae.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.model.Cycle;

public interface CycleRepository extends JpaRepository<Cycle, Long>{  
	
	List<Cycle> findAll();

	long count();

	@Query("SELECT c.id FROM Cycle c WHERE :date BETWEEN c.startDate AND c.endDate")
	Long findIdByDate(@Param("date") LocalDate date);

    @Query("SELECT c FROM Cycle c WHERE :date BETWEEN c.startDate AND c.endDate")
    Cycle findCycleByDate(@Param("date") LocalDate date);

	@Query("SELECT c FROM Cycle c WHERE c.year = :year")
	Cycle findCycleByYear(@Param("year") int year);

	// list all cycles for year
	@Query("SELECT new hyung.jin.seo.jae.dto.CycleDTO(c.id, c.year, c.startDate, c.endDate, c.vacationStartDate, c.vacationEndDate, c.description) FROM Cycle c WHERE c.year = ?1")
	List<CycleDTO> findCycleForYear(int year);

}
