package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import hyung.jin.seo.jae.model.PracticeSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PracticeScheduleDTO implements Serializable {

	private String id;

	private int year;

	private int week;

	private String info;

	private boolean active;

	private String registerDate;

	// private List<String> practices = new ArrayList<>();

	// public void addPractice(String practice){
	// 	practices.add(practice);
	// }

	private List<PracticeDTO> practices = new ArrayList();

	public void addPractice(PracticeDTO practice){
		practices.add(practice);
	}

	public PracticeSchedule convertToPracticeSchedule() {
    	PracticeSchedule ps = new PracticeSchedule();
		ps.setYear(this.year);
		ps.setWeek(this.week);
		ps.setInfo(this.info);
		ps.setActive(this.active);
		return ps;
	}

	public PracticeScheduleDTO(PracticeSchedule work){
		this.id = String.valueOf(work.getId());
		this.year = work.getYear();
		this.week = work.getWeek();
		this.info = work.getInfo();
		this.active = work.isActive();
		this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public PracticeScheduleDTO(long id, int year, int week, String info, boolean active, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.year = year;
		this.week = week;
		this.info = info;
		this.active = active;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
