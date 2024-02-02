package hyung.jin.seo.jae.service.impl;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.TemporalAdjusters.next;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.model.Cycle;
import hyung.jin.seo.jae.repository.CycleRepository;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Service
public class CycleServiceImpl implements CycleService {
	

	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
	private List<CycleDTO> cycles;

	@Autowired
	private CycleRepository cycleRepository;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	
	@Override
	public long checkCount() {
		long count = cycleRepository.count();
		return count;
	}

	@Override
	public List<CycleDTO> allCycles() {
		List<Cycle> cycles = new ArrayList<>();
		try{
			cycles = cycleRepository.findAll();
		}catch(Exception e){
			System.out.println("No cycle found");
		}
		// cycleRepository.findAll();
		List<CycleDTO> dtos = new ArrayList<>();
		for(Cycle cycle: cycles){
			CycleDTO dto = new CycleDTO(cycle);
			dtos.add(dto);
		}
		return dtos;
	}	



	// return academic year.
	// for example, today is 17/04/2023 while academic year in 2023 is 11/06/2023 then it will return '2022'
	// however, if today is 17/09/2023, it will return '2023' as academic calendar date (11/06/2023) already passed.
	@Override
	public int academicYear() {
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		int year = 0;
		for(CycleDTO dto: cycles) {
			String startDate = dto.getStartDate();
			String endDate = dto.getEndDate();
			try {
				if(JaeUtils.checkIfTodayBelongTo(startDate, endDate)) {
					year =  Integer.parseInt(dto.getYear());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return year;
	}

	@Override
	public int academicYear(String date) {
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		int year = 0;
		for(CycleDTO dto: cycles) {
			String startDate = dto.getStartDate();
			String endDate = dto.getEndDate();
			try {
				if(JaeUtils.checkIfTodayBelongTo(date, startDate, endDate)) {
					year =  Integer.parseInt(dto.getYear());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return year;
	}

	// return weeks number based on academic year
	@Override
	public int academicWeeks(){
		LocalDate today = LocalDate.now();
		int currentYear = today.getYear();
		int academicYear = academicYear();
		int weeks = 0;
		String academicDate = "";
		String vacationStartDate = "";
		String vacationEndDate = "";
		// bring academic start date
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(academicYear))){
				academicDate = dto.getStartDate();
				vacationStartDate = dto.getVacationStartDate();
				vacationEndDate = dto.getVacationEndDate();
				break;
			}
		}
		LocalDate academicStart = LocalDate.parse(academicDate);
		LocalDate vacationStart = LocalDate.parse(vacationStartDate);
		LocalDate vacationEnd = LocalDate.parse(vacationEndDate);
		
		if(currentYear==academicYear) { // from June to December
			// compare today's date with vacation start date
			if(today.isBefore(vacationStart)) { // simply calculate weeks
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, today);
			}else { // set weeks as xmas week
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, vacationStart) - 1;
			}
		}else { // from January to June
			// simply calculate since last year starting date - 3 weeks (xmas holidays)
			// compare today's date with vacation end date
			if(today.isBefore(vacationEnd)) { // until vacation start date
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, vacationStart) - 1;
			}else{
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, today) - 3; // 3 weeks for xmas holidays
			}		
		}
		return (weeks+1); // calculation must start from 1 not 0
	}


	// return weeks number based on academic year
	@Override
	public int academicWeeks(String date){
		// if not formatted date passed, return 0
		if(!JaeUtils.isValidDateFormat(date)) return 0;
		LocalDate specificDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		int currentYear = specificDate.getYear();
		int academicYear = academicYear(date);
		int weeks = 0;
		String academicDate = "";
		String vacationStartDate = "";
		String vacationEndDate = "";

		// bring academic start date & end date
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(academicYear))){
				academicDate = dto.getStartDate();
				vacationStartDate = dto.getVacationStartDate();
				vacationEndDate = dto.getVacationEndDate();
				break;
			}
		}
		// convert to LocalDate
		LocalDate academicStart = LocalDate.parse(academicDate);
		LocalDate vacationStart = LocalDate.parse(vacationStartDate);
		LocalDate vacationEnd = LocalDate.parse(vacationEndDate);
			
		if(currentYear==academicYear) { // from June to December
			// compare today's date with vacation start date
			if(specificDate.isBefore(vacationStart)) { // simply calculate weeks
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, specificDate);
			}else { // set weeks as xmas week
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, vacationStart) - 1;
			}
		}else { // from January to June
			// simply calculate since last year starting date - 3 weeks (xmas holidays)
			// compare today's date with vacation end date
			if(specificDate.isBefore(vacationEnd)) { // until vacation start date
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, vacationStart) - 1;
			}else{
				weeks = (int) ChronoUnit.WEEKS.between(academicStart, specificDate) - 3; // 3 weeks for xmas holidays
			}		
		}
		return (weeks+1); // calculation must start from 1 not 0
	}

	@Override
	public boolean isBelongToHoliday(){
		boolean isBelongToHoliday = false;
		LocalDate today = LocalDate.now();
		// get academic year
		// int currentYear = today.getYear();
		int academicYear = academicYear();
		String vacationStartDate = "";
		String vacationEndDate = "";
		// get vacation start date and end date
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(academicYear))){
				vacationStartDate = dto.getVacationStartDate();
				vacationEndDate = dto.getVacationEndDate();
				break;
			}
		}
		LocalDate vacationStart = LocalDate.parse(vacationStartDate);
		LocalDate vacationEnd = LocalDate.parse(vacationEndDate);
		if ((today.isAfter(vacationStart) || today.isEqual(vacationStart)) 
    		&& (today.isBefore(vacationEnd) || today.isEqual(vacationEnd))) {
				isBelongToHoliday = true;
		}
		return isBelongToHoliday;
	}

	@Override
	public boolean isBelongToHoliday(String date){
		boolean isBelongToHoliday = false;
		// if not formatted date passed, return false
		if(!JaeUtils.isValidDateFormat(date)) return isBelongToHoliday;
		LocalDate specificDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// get academic year
		int academicYear = academicYear(date);
		String vacationStartDate = "";
		String vacationEndDate = "";
		// get vacation start date and end date
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(academicYear))){
				vacationStartDate = dto.getVacationStartDate();
				vacationEndDate = dto.getVacationEndDate();
				break;
			}
		}
		LocalDate vacationStart = LocalDate.parse(vacationStartDate);
		LocalDate vacationEnd = LocalDate.parse(vacationEndDate);
		if ((specificDate.isAfter(vacationStart) || specificDate.isEqual(vacationStart)) 
    		&& (specificDate.isBefore(vacationEnd) || specificDate.isEqual(vacationEnd))) {
				isBelongToHoliday = true;
		}
		return isBelongToHoliday;
	}

	@Override
	public Cycle getCycle(Long cycleId) {
		Optional<Cycle> cycle = cycleRepository.findById(cycleId);
		if(cycle.isPresent()) {
			return cycle.get();
		}else{
			return null;
		}
	}

	@Override
	public Long findIdByDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Parse the string to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
		Long id = cycleRepository.findIdByDate(localDate);
		return id;
	}

	@Override
	public Cycle findCycleByDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// Parse the string to LocalDate
		LocalDate localDate = LocalDate.parse(date, formatter);
		Cycle cycle = cycleRepository.findCycleByDate(localDate);
		return cycle;
	}









	@Override
	public String academicStartSunday(int year, int week) {
		String startDate = getStartDate(year);
		String vacationStartDate = getVacationStartDate(year);
		int vacationStartWeek = 0;
		try {
			vacationStartWeek = academicWeeks(JaeUtils.convertToddMMyyyyFormat(vacationStartDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(week <= vacationStartWeek){ // 1. before vacation starts
			LocalDate academicYearStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate weekStartDay = academicYearStartDate.plusWeeks(week - 1);		
			String formattedWeekStartDay = weekStartDay.format(dateFormatter);
			return formattedWeekStartDay;
		}else{ // 2. afterf vactation ends
			String vacationEndDate = getVacationEndDate(year);
			int delta = week - vacationStartWeek;
			LocalDate academicYearVactoionEndDate = LocalDate.parse(vacationEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			// get next sunday of vacation end date
			LocalDate resumeStartDay = academicYearVactoionEndDate.with(next(DayOfWeek.SUNDAY));		
			LocalDate weekStartDay = resumeStartDay.plusWeeks(delta - 1);				
			String formattedWeekStartDay = weekStartDay.format(dateFormatter);
			return formattedWeekStartDay;
		}
	}






	@Override
	public String academicEndSaturday(int year, int week) {
		String startDate = getStartDate(year);
		String vacationStartDate = getVacationStartDate(year);
		int vacationStartWeek = 0;
		try {
			vacationStartWeek = academicWeeks(JaeUtils.convertToddMMyyyyFormat(vacationStartDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(week <= vacationStartWeek){ // 1. before vacation starts
			LocalDate academicYearStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate weekStartDay = academicYearStartDate.plusWeeks(week - 1);
			LocalDate weekEndDay = weekStartDay.plusDays(6);		
			String formattedWeekStartDay = weekEndDay.format(dateFormatter);
			return formattedWeekStartDay;
		}else{ // 2. afterf vactation ends
			String vacationEndDate = getVacationEndDate(year);
			int delta = week - vacationStartWeek;
			LocalDate academicYearVactoionEndDate = LocalDate.parse(vacationEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			// get next sunday of vacation end date
			LocalDate resumeStartDay = academicYearVactoionEndDate.with(next(DayOfWeek.SATURDAY));		
			LocalDate weekStartDay = resumeStartDay.plusWeeks(delta - 1);				
			String formattedWeekStartDay = weekStartDay.format(dateFormatter);
			return formattedWeekStartDay;
		}
	}











	// get start date of academic year
	private String getStartDate(int year){
		String startDate = "";
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(year))){
				startDate = dto.getStartDate();
				break;
			}
		}
		return startDate;
	}

	// get end date of academic year
	private String getEndDate(int year){
		String endDate = "";
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(year))){
				endDate = dto.getEndDate();
				break;
			}
		}
		return endDate;
	}

	// get vacation start date of academic year
	private String getVacationStartDate(int year){
		String startDate = "";
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(year))){
				startDate = dto.getVacationStartDate();
				break;
			}
		}
		return startDate;
	}

	// get vacation end date of academic year
	private String getVacationEndDate(int year){
		String endDate = "";
		if(cycles==null) {
			cycles = (List<CycleDTO>) applicationContext.getBean(JaeConstants.ACADEMIC_CYCLES);
		}
		for(CycleDTO dto : cycles){
			if(dto.getYear().equals(Integer.toString(year))){
				endDate = dto.getVacationEndDate();
				break;
			}
		}
		return endDate;
	}

	@Override
	public LocalDate getDateByWeekAndDay(int year, int week, String day) {
		String start = academicStartSunday(year, week);
		switch(day){
			// case "Sunday":
			// 	return start;
			case "Monday":
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1);
			case "Tuesday":
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(2);
			case "Wednesday":
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(3);
			case "Thursday":
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(4);
			case "Friday":
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(5);
			case "Saturday":
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(6);
			default: // Sunday or All
				return LocalDate.parse(start, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
	}

	@Override
	public Cycle addCycle(Cycle cycle) {
		Cycle add = cycleRepository.save(cycle);
		return add;
	}

	@Override
	public List<CycleDTO> listCycles(int year) {
		List<CycleDTO> dtos = new ArrayList<>();
		try{
			dtos = cycleRepository.findCycleForYear(year);
		}catch(Exception e){
			System.out.println("No cycle found");
		}
		return dtos;	
	}

	@Override
	public Cycle updateCycle(Cycle cycle) {
		// search by getId
		Cycle existing = cycleRepository.findById(cycle.getId())
				.orElseThrow(() -> new EntityNotFoundException("Clazz Not Found"));
		// Update info
		// year
		int newYear = cycle.getYear();
		existing.setYear(newYear);
		// description
		String newDescription = cycle.getDescription();
		existing.setDescription(newDescription);
		// start date
		LocalDate newStartDate = cycle.getStartDate();
		existing.setStartDate(newStartDate);
		// end date
		LocalDate newEndDate = cycle.getEndDate();
		existing.setEndDate(newEndDate);
		// vacation start date
		LocalDate newVacationStartDate = cycle.getVacationStartDate();
		existing.setVacationStartDate(newVacationStartDate);
		// vacation end date
		LocalDate newVacationEndDate = cycle.getVacationEndDate();
		existing.setVacationEndDate(newVacationEndDate);
		// update the existing record
		Cycle updated = cycleRepository.save(existing);
		return updated;		
	}
}
