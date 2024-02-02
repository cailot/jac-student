package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.Cycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class CycleDTO implements Serializable{

    private String id;
    
    private String year;
    
    private String startDate;

    private String endDate;
    
    private String vacationStartDate;

    private String vacationEndDate;

    private String description;
	
    public CycleDTO(long id, int year, LocalDate startDate, LocalDate endDate, LocalDate vacationStartDate, LocalDate vacationEndDate, String description) {
        this.id = Long.toString(id);
        this.year = Integer.toString(year);
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
        this.vacationStartDate = vacationStartDate.toString();
        this.vacationEndDate = vacationEndDate.toString();
        this.description = description;
    }
    
	public CycleDTO(Cycle cycle) {
        this.id = (cycle.getId()!=null) ? cycle.getId().toString() : "";            
    	this.year = (cycle.getYear()!=null) ? cycle.getYear().toString() : "";
    	this.startDate = (cycle.getStartDate()!=null) ? cycle.getStartDate().toString() : "";
    	this.endDate = (cycle.getEndDate()!=null) ? cycle.getEndDate().toString() : "";
    	this.vacationStartDate = (cycle.getVacationStartDate()!=null) ? cycle.getVacationStartDate().toString() : "";
    	this.vacationEndDate = (cycle.getVacationEndDate()!=null) ? cycle.getVacationEndDate().toString() : "";
        this.description = (cycle.getDescription()!=null) ? cycle.getDescription() : "";
    }
    public Cycle convertToCycle() {
    	Cycle cycle = new Cycle();
        if(StringUtils.isNotBlank(id)) cycle.setId(Long.parseLong(this.id));
		if(StringUtils.isNotBlank(year)) cycle.setYear(Integer.parseInt(this.year));
        if(StringUtils.isNotBlank(startDate)) cycle.setStartDate(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		if(StringUtils.isNotBlank(endDate)) cycle.setEndDate(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		if(StringUtils.isNotBlank(vacationStartDate)) cycle.setVacationStartDate(LocalDate.parse(vacationStartDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));	
		if(StringUtils.isNotBlank(vacationEndDate)) cycle.setVacationEndDate(LocalDate.parse(vacationEndDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if(StringUtils.isNotBlank(description)) cycle.setDescription(description);	
		return cycle;
    }
}
