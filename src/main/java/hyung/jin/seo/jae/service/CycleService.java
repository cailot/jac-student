package hyung.jin.seo.jae.service;

import java.time.LocalDate;
import java.util.List;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.model.Cycle;

public interface CycleService {
	
	// list all Cycles
	List<CycleDTO> allCycles();
	
	// get total number of cycle
 	long checkCount();

	// get current academic year
	int academicYear();

	// get academic year by date
	int academicYear(String date);

	// get current academic week
	int academicWeeks();

	// get academic week by date
	int academicWeeks(String date);

	// check if todate belongs to holiday
	boolean isBelongToHoliday();

	// check if date belongs to holiday
	boolean isBelongToHoliday(String date);

	// get academic start Sunday
	String academicStartSunday(int year, int week);

	// get academic end Saturday
	String academicEndSaturday(int year, int week);

	// get date by week and day
	LocalDate getDateByWeekAndDay(int year, int week, String day);

	// get Cycle by Id
    Cycle getCycle(Long cycleId);

	// update Cycle
	Cycle updateCycle(Cycle cycle);

	// get Id by date
	Long findIdByDate(String date);

	// get Cycle by date
	Cycle findCycleByDate(String date);

	// add Cycle
	Cycle addCycle(Cycle cycle);

	// list all cycles by year
	List<CycleDTO> listCycles(int year);
}
