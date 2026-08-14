package hyung.jin.seo.jae.dto;

import java.io.Serializable;
import hyung.jin.seo.jae.model.TestSchedule;
import hyung.jin.seo.jae.utils.JaeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestScheduleDTO implements Serializable {

	private String id;

	private String from;

	private String to;

	private String explanationFrom;

	private String explanationTo;

	//private String[] grade;
	private String grade;

	private String[] testGroup;

	private String[] week;

	private String info;

	private boolean active;

	private String registerDate;

	private String resultDate;


	public TestSchedule convertToTestSchedule() {
    	TestSchedule ts = new TestSchedule();
		if(StringUtils.isNotBlank(id)) ts.setId(Long.parseLong(this.id));
		ts.setFromDatetime(LocalDateTime.parse(this.from, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		ts.setToDatetime(LocalDateTime.parse(this.to, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		ts.setActive(this.active);
		ts.setInfo(this.info);
		ts.setGrade(this.grade);
		ts.setTestGroup(JaeUtils.joinString(this.testGroup));
		ts.setWeek(JaeUtils.joinString(this.week));
		ts.setResultDate(this.resultDate != null ? LocalDate.parse(this.resultDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null);
		ts.setExplanationFromDatetime(StringUtils.isNotBlank(explanationFrom) ? LocalDateTime.parse(this.explanationFrom, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
		ts.setExplanationToDatetime(StringUtils.isNotBlank(explanationTo) ? LocalDateTime.parse(this.explanationTo, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);	
		return ts;
	}

	public TestScheduleDTO(TestSchedule schedule){
		this.id = String.valueOf(schedule.getId());
		this.from = schedule.getFromDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.to = schedule.getToDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = schedule.getGrade();
		this.testGroup = JaeUtils.splitString(schedule.getTestGroup());
		this.week = JaeUtils.splitString(schedule.getWeek());
		this.info = schedule.getInfo();
		this.active = schedule.isActive();
		this.registerDate = schedule.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.resultDate = (schedule.getResultDate() != null) ? schedule.getResultDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.explanationFrom =   schedule.getExplanationFromDatetime()!= null ? schedule.getExplanationFromDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")) : null;
		this.explanationTo = schedule.getExplanationToDatetime() != null ? schedule.getExplanationToDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")) : null;
	}

	public TestScheduleDTO(long id, LocalDateTime fromTime, LocalDateTime toTime, String grade, String group, String week, String info, boolean active, LocalDate registerDate, LocalDateTime explanationFrom, LocalDateTime explanationTo){
		this.id = String.valueOf(id);
		this.from = fromTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));;
		this.to = toTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = grade;
		this.testGroup = JaeUtils.splitString(group);
		this.week = JaeUtils.splitString(week);
		this.info = info;
		this.active = active;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.explanationFrom = (explanationFrom != null) ? explanationFrom.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")) : null;
		this.explanationTo = (explanationTo != null) ? explanationTo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")) : null;
	}

	public TestScheduleDTO(long id, LocalDateTime fromTime, LocalDateTime toTime, String grade, String group, String week, String info, boolean active, LocalDate registerDate, LocalDate resultDate, LocalDateTime explanationFrom, LocalDateTime explanationTo){
		this.id = String.valueOf(id);
		this.from = fromTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));;
		this.to = toTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = grade;
		this.testGroup = JaeUtils.splitString(group);
		this.week = JaeUtils.splitString(week);
		this.info = info;
		this.active = active;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.resultDate = (resultDate != null) ? resultDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		this.explanationFrom = (explanationFrom != null) ? explanationFrom.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")) : null;
		this.explanationTo = (explanationTo != null) ? explanationTo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")) : null;
	}

}
