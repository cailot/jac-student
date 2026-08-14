package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import hyung.jin.seo.jae.model.PracticeSchedule;
import hyung.jin.seo.jae.utils.JaeUtils;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PracticeScheduleDTO implements Serializable {

	private String id;

	private String from;

	private String to;

	private String[] grade;

	private String[] practiceGroup;

	private String[] week;

	private String info;

	private boolean active;

	private String registerDate;

	// private List<PracticeDTO> practices = new ArrayList();

	// public void addPractice(PracticeDTO practice){
	// 	practices.add(practice);
	// }

	public PracticeSchedule convertToPracticeSchedule() {
    	PracticeSchedule schedule = new PracticeSchedule();
		if(StringUtils.isNotBlank(id)) schedule.setId(Long.parseLong(this.id));
		schedule.setFromDatetime(LocalDateTime.parse(this.from, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		schedule.setToDatetime(LocalDateTime.parse(this.to, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		schedule.setActive(this.active);
		schedule.setInfo(this.info);
		schedule.setGrade(JaeUtils.joinString(this.grade));
		schedule.setPracticeGroup(JaeUtils.joinString(this.practiceGroup));
		schedule.setWeek(JaeUtils.joinString(this.week));
		return schedule;
	}

	public PracticeScheduleDTO(PracticeSchedule schedule){
		this.id = String.valueOf(schedule.getId());
		this.from = schedule.getFromDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.to = schedule.getToDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = JaeUtils.splitString(schedule.getGrade());
		this.practiceGroup = JaeUtils.splitString(schedule.getPracticeGroup());
		this.week = JaeUtils.splitString(schedule.getWeek());
		this.info = schedule.getInfo();
		this.active = schedule.isActive();
		this.registerDate = schedule.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		// this.id = String.valueOf(work.getId());
		// this.year = work.getYear();
		// this.week = work.getWeek();
		// this.info = work.getInfo();
		// this.active = work.isActive();
		// this.registerDate = work.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	// public PracticeScheduleDTO(long id, int year, int week, String info, boolean active, LocalDate registerDate){
	public PracticeScheduleDTO(long id, LocalDateTime fromTime, LocalDateTime toTime, String grade, String group, String week, String info, boolean active, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.from = fromTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));;
		this.to = toTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = JaeUtils.splitString(grade);
		this.practiceGroup = JaeUtils.splitString(group);
		this.week = JaeUtils.splitString(week);
		this.info = info;
		this.active = active;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
