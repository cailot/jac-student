package hyung.jin.seo.jae.dto;

import java.io.Serializable;

import hyung.jin.seo.jae.model.HomeworkSchedule;
import hyung.jin.seo.jae.utils.JaeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkScheduleDTO implements Serializable {

	private String id;

	private String from;

	private String to;

	private String info;

	private int subjectDisplay;

	private int answerDisplay;

	private boolean active;

	private String[] grade;

	private String[] subject;

	private String registerDate;

	// private int year;

	
	public HomeworkScheduleDTO(long id, LocalDateTime fromTime, LocalDateTime toTime, String grade, String subject, int subjectDisplay, int answerDisplay, String info, boolean active, LocalDate registerDate){
		this.id = String.valueOf(id);
		this.from = fromTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));;
		this.to = toTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = JaeUtils.splitString(grade);
		this.subject = JaeUtils.splitString(subject);
		this.subjectDisplay = subjectDisplay;
		this.answerDisplay = answerDisplay;
		this.info = info;
		this.active = active;
		this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public HomeworkSchedule convertToHomeworkSchedule() {
    	HomeworkSchedule schedule = new HomeworkSchedule();
		if(StringUtils.isNotBlank(id)) schedule.setId(Long.parseLong(this.id));
		schedule.setFromDatetime(LocalDateTime.parse(this.from, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		schedule.setToDatetime(LocalDateTime.parse(this.to, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		schedule.setActive(this.active);
		schedule.setInfo(this.info);
		schedule.setSubjectDisplay(this.subjectDisplay);
		schedule.setAnswerDisplay(this.answerDisplay);
		schedule.setGrade(JaeUtils.joinString(this.grade));
		schedule.setSubject(JaeUtils.joinString(this.subject));
		return schedule;
	}

	public HomeworkScheduleDTO(HomeworkSchedule schedule){
		this.id = String.valueOf(schedule.getId());
		this.from = schedule.getFromDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.to = schedule.getToDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
		this.grade = JaeUtils.splitString(schedule.getGrade());
		this.subject = JaeUtils.splitString(schedule.getSubject());
		this.subjectDisplay = schedule.getSubjectDisplay();
		this.answerDisplay = schedule.getAnswerDisplay();
		this.info = schedule.getInfo();
		this.active = schedule.isActive();
		this.registerDate = schedule.getRegisterDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}
