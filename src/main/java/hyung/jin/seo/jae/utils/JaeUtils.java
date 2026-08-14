package hyung.jin.seo.jae.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import hyung.jin.seo.jae.model.AssessmentAnswerItem;
import hyung.jin.seo.jae.model.TestAnswerItem;

public class JaeUtils {

	// public static Map<String, String> ACADEMIC_START_DAY;
	
	// static {
	// 	 ACADEMIC_START_DAY = new HashMap<String, String>();
	// 	 ACADEMIC_START_DAY.put("2019", "17/06");
	// 	 ACADEMIC_START_DAY.put("2020", "15/06");
	// 	 ACADEMIC_START_DAY.put("2021", "14/06");
	// 	 ACADEMIC_START_DAY.put("2022", "13/06");
	// 	 ACADEMIC_START_DAY.put("2023", "12/06");
	// 	 ACADEMIC_START_DAY.put("2024", "10/06");
	// 	 ACADEMIC_START_DAY.put("2025", "09/06");
	// 	 ACADEMIC_START_DAY.put("2026", "08/06");
	// 	 ACADEMIC_START_DAY.put("2027", "07/06");
	// 	 ACADEMIC_START_DAY.put("2028", "05/06");
	// 	 ACADEMIC_START_DAY.put("2029", "04/06");
	// 	 ACADEMIC_START_DAY.put("2030", "03/06");
	// 	 ACADEMIC_START_DAY.put("2031", "02/06");
	// }
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd");

	
	// return academic year.
	// for example, today is 17/04/2023 while academic year in 2023 is 11/06/2023 then it will return '2022'
	// however, if today is 17/09/2023, it will return '2023' as academic calendar date (11/06/2023) already passed.
	// public static int academicYear() {
	// 	Calendar today = Calendar.getInstance();
	// 	int currentYear = today.get(Calendar.YEAR);
	// 	int academicYear = currentYear;
		
	// 	String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(currentYear));
	// 	String dateString = academicDate + "/" + currentYear;
	// 	//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	// 	try {
	// 		Date specifiedDate = dateFormat.parse(dateString);
	// 		Calendar speicifedAcademic = Calendar.getInstance();
	// 		speicifedAcademic.setTime(specifiedDate);
			
	// 		if(today.before(speicifedAcademic)) { // return 'currentYear - 1'
	// 			academicYear = currentYear-1;
	// 		}
	// 	} catch (ParseException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
	// 	return academicYear;
	// }
	
	// return acadamicYear based on passed date
	// public static int academicYear(String date) throws ParseException {
	// 	Date ds = dateFormat.parse(date); // ex> 20/04/2023
	// 	Calendar specific = Calendar.getInstance();
	// 	specific.setTime(ds);
		
	// 	int specificYear = specific.get(Calendar.YEAR);
	// 	int academicYear = specificYear;
		
	// 	String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(specificYear));
	// 	String dateString = academicDate + "/" + specificYear;
	// 	//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	// 	Date specifiedDate = dateFormat.parse(dateString);
	// 	Calendar speicifedAcademic = Calendar.getInstance();
	// 	speicifedAcademic.setTime(specifiedDate);
		
	// 	if(specific.before(speicifedAcademic)) { // return 'currentYear - 1'
	// 		academicYear = specificYear-1;
	// 	}
	// 	return academicYear;
	// }
	

	// // return last date of academic year
	// public static LocalDate lastAcademicDate(String year) {
	// 	int tempYear = Integer.parseInt(year)+1;
	// 	String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(tempYear));
	// 	String[] dates = StringUtils.split(academicDate, '/');
	// 	LocalDate nextAcademicStart = LocalDate.of(tempYear, Integer.parseInt(dates[1]), Integer.parseInt(dates[0])); 
	// 	LocalDate lastAcademicDate = nextAcademicStart.minusDays(1);
	// 	return lastAcademicDate;
	// }
		
	
	// return weeks number based on academic year
	// public static int academicWeeks() throws ParseException {
	// 	LocalDate today = LocalDate.now();
	// 	int currentYear = today.getYear();
	// 	int academicYear = academicYear();
	// 	int weeks = 0;
	// 	if(currentYear==academicYear) { // from June to December
	// 		// bring academic start date
	// 		String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(currentYear));
	// 		String academicString = academicDate + "/" + currentYear;
	// 		Date interim = dateFormat.parse(academicString);
	// 		LocalDate academicStart = interim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	// 		// set X-mas
	// 		LocalDate xmas = LocalDate.of(currentYear, 12, 25);
	// 		// compare today's date with Xmas
	// 		if(today.isBefore(xmas)) { // simply calculate weeks
	// 			weeks = (int) ChronoUnit.WEEKS.between(academicStart, today);
	// 		}else { // set weeks as xmas week
	// 			weeks = (int) ChronoUnit.WEEKS.between(academicStart, xmas);
	// 		}
	// 	}else { // from January to June
	// 		// simply calculate since last year starting date - 3 weeks (xmas holidays)
	// 		// bring academic start date
	// 		String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(academicYear));
	// 		String academicString = academicDate + "/" + Integer.toString(academicYear);
	// 		Date interim = dateFormat.parse(academicString);
	// 		LocalDate academicStart = interim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	// 		weeks = ((int) ChronoUnit.WEEKS.between(academicStart, today)) - 3;
			
	// 	}
	// 	return (weeks+1); // calculation must start from 1 not 0
	// }
	
	
	// // return weeks number based on academic year
	// public static int academicWeeks(String date) throws ParseException {
	// 	// if not formatted date passed, return 0
	// 	if(!isValidDateFormat(date)) return 0;
	// 	String[] ds = date.split("/"); // ex> 20/04/2023
	// 	LocalDate specific = LocalDate.of(Integer.parseInt(ds[2]), Integer.parseInt(ds[1]), Integer.parseInt(ds[0]));
	// 	int currentYear = Integer.parseInt(ds[2]);
	// 	int academicYear = academicYear(date);
	// 	int weeks = 0;
	// 	if(currentYear==academicYear) { // from June to December
	// 		// bring academic start date
	// 		String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(currentYear));
	// 		String academicString = academicDate + "/" + currentYear;
	// 		Date interim = dateFormat.parse(academicString);
	// 		LocalDate academicStart = interim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	// 		// set X-mas
	// 		LocalDate xmas = LocalDate.of(currentYear, 12, 25);
	// 		// compare today's date with Xmas
	// 		if(specific.isBefore(xmas)) { // simply calculate weeks
	// 			weeks = (int) ChronoUnit.WEEKS.between(academicStart, specific);
	// 		}else { // set weeks as xmas week
	// 			weeks = (int) ChronoUnit.WEEKS.between(academicStart, xmas);
	// 		}
	// 	}else { // from January to June
	// 		// simply calculate since last year starting date - 3 weeks (xmas holidays)
	// 		// bring academic start date
	// 		String academicDate = (String) ACADEMIC_START_DAY.get(Integer.toString(academicYear));
	// 		String academicString = academicDate + "/" + Integer.toString(academicYear);
	// 		Date interim = dateFormat.parse(academicString);
	// 		LocalDate academicStart = interim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	// 		weeks = ((int) ChronoUnit.WEEKS.between(academicStart, specific)) - 3;
			
	// 	}
	// 	return (weeks+1); // calculation must start from 1 not 0
	// }
	
	// convert date format from yyyy-MM-dd to dd/MM/yyyy, for example 2023-04-22 to 22/04/2023
	public static String convertToddMMyyyyFormat(String date) throws ParseException {
		String formatted = "";
		if(StringUtils.isNotBlank(date)) {
			Date display = displayFormat.parse(date);
			formatted = dateFormat.format(display);
		}
		return formatted;
	}
	
	// convert date format from dd/MM/yyyy to yyyy-MM-dd, for example 22/04/2023 to 2023-04-22
	public static String convertToyyyyMMddFormat(String date) throws ParseException {
		String formatted = "";
		if(StringUtils.isNotBlank(date)) {
			Date display = dateFormat.parse(date);
			formatted = displayFormat.format(display);
		}
		return formatted;
	}
	
	// check if string date is formatted 'dd/MM/yyyy'
	public static boolean isValidDateFormat(String date) {
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date);
			return true;
		}catch(ParseException e) {
			return false;
		}
	}

	// return date as dd/MM/yyyy
	public static String getToday(){
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		return date;
	}

	// return date info for Student Memo
	public static String getTodayForMemo(){
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		return " [" + date + "]";
	}
	
	// check wether Today is between from and to
    public static boolean checkIfTodayBelongTo(String from, String to) throws ParseException {
        // Check if from or to is null or empty
        if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            return false;
        }
        
        // an extra check to remove time part from date string if exists
		from = StringUtils.substringBefore(from, " ");
		to = StringUtils.substringBefore(to, " ");
		
		// Additional check after substring operation
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            return false;
        }
		
		// Use a new SimpleDateFormat per parse - SimpleDateFormat is not thread-safe; shared static causes NumberFormatException under concurrency
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		fmt.setLenient(false);
		Date fromDate;
		Date toDate;
		
		try {
			fromDate = fmt.parse(from);
		} catch (ParseException e) {
			System.err.println("Error parsing 'from' date: '" + from + "' - " + e.getMessage());
			return false;
		}
		
		try {
			toDate = fmt.parse(to);
		} catch (ParseException e) {
			System.err.println("Error parsing 'to' date: '" + to + "' - " + e.getMessage());
			return false;
		}
		// Truncate the time component from today
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date today = calendar.getTime();
      	return (today.compareTo(fromDate) >= 0 && today.compareTo(toDate) <= 0); 
    }

	// check wether date is between from and to.
	// date format must be 'dd/MM/yyyy'
	public static boolean checkIfTodayBelongTo(String date, String from, String to) throws ParseException {
		// Check if any parameter is null or empty
		if (StringUtils.isBlank(date) || StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			return false;
		}
		// Use new SimpleDateFormat instances - static shared formatters are not thread-safe
		SimpleDateFormat isoFmt = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dmyFmt = new SimpleDateFormat("dd/MM/yyyy");
		isoFmt.setLenient(false);
		dmyFmt.setLenient(false);
		Date fromDate = isoFmt.parse(from);
		Date toDate = isoFmt.parse(to);
		Date specificDate = dmyFmt.parse(date);
		// Truncate the time component from today
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(specificDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date checkDate = calendar.getTime();
		return (checkDate.compareTo(fromDate) >= 0 && checkDate.compareTo(toDate) <= 0);
	}

	// check wether 1st date is earlier than 2nd date.
	public static boolean isEarlier(String date1String, String date2String) throws ParseException {
        Date date1 = dateFormat.parse(date1String);
        Date date2 = dateFormat.parse(date2String);
        int comparisonResult = date1.compareTo(date2);

        return (comparisonResult < 0);
    }

	// clear all info in session
	public static void clearSession(HttpSession session){
		Enumeration<String> names = session.getAttributeNames();
		while(names.hasMoreElements()){
			String name = names.nextElement();
			session.removeAttribute(name);
		}
	}

	// calculate practice score by comparing student answers and answer sheet
	public static double calculatePracticeScore(List<Integer> studentAnswers, List<Integer> answerSheet) {
        // Check if both lists have the same size
        if ((studentAnswers==null) || (answerSheet==null) || (studentAnswers.size() != answerSheet.size())) {
            return 0;
        }
        int totalQuestions = answerSheet.get(0); // Assuming the first element is the total count

        // Iterate through the lists and compare corresponding elements
        int correctAnswers = 0;
        for (int i = 1; i <= totalQuestions; i++) {
            int studentAnswer = studentAnswers.get(i);
            int correctAnswer = answerSheet.get(i);

            if (studentAnswer == correctAnswer) {
                correctAnswers++;
            }
        }
        // Calculate the final score as a percentage
        double score = ((double) correctAnswers / totalQuestions) * 100;
		double rounded = Math.round(score * 100.0) / 100.0;
        return rounded;
    }

	// calculate test score by comparing student answers and answer sheet
	public static double calculateTestScore(List<Integer> studentAnswers, List<TestAnswerItem> answerSheet) {
		// Check if both lists have the same size
        if ((studentAnswers==null) || (answerSheet==null) || (studentAnswers.size() != answerSheet.size())) {
            return 0;
        }
        int totalQuestions = answerSheet.size();

        // Iterate through the lists and compare corresponding elements
        int correctAnswers = 0;
        for (int i = 0; i < totalQuestions; i++) {
            int studentAnswer = studentAnswers.get(i);
            int correctAnswer = answerSheet.get(i).getAnswer();

            if (studentAnswer == correctAnswer) {
                correctAnswers++;
            }
        }
        // Calculate the final score as a percentage
        double score = ((double) correctAnswers / totalQuestions) * 100;
		double rounded = Math.round(score * 100.0) / 100.0;
        return rounded;
	}

	// count test score by comparing student answers and answer sheet
	public static int countTestScore(List<Integer> studentAnswers, List<TestAnswerItem> answerSheet) {
		// Check if both lists have the same size
        if ((studentAnswers==null) || (answerSheet==null) || (studentAnswers.size() != answerSheet.size())) {
            return 0;
        }
        int totalQuestions = answerSheet.size();
        // Iterate through the lists and compare corresponding elements
        int correctAnswers = 0;
        for (int i = 0; i < totalQuestions; i++) {
            int studentAnswer = studentAnswers.get(i);
            int correctAnswer = answerSheet.get(i).getAnswer();

            if (studentAnswer == correctAnswer) {
                correctAnswers++;
            }
        }
        // Return the count of correct answers
        return correctAnswers;
	}

	// calculate test score by comparing student answers and answer sheet
	public static double calculateAssessmentScore(List<Integer> studentAnswers, List<AssessmentAnswerItem> answerSheet) {
		// Check if both lists have the same size
		if ((studentAnswers==null) || (answerSheet==null) || (studentAnswers.size() != answerSheet.size())) {
			return 0;
		}
		int totalQuestions = answerSheet.size();

		// Iterate through the lists and compare corresponding elements
		int correctAnswers = 0;
		for (int i = 0; i < totalQuestions; i++) {
			int studentAnswer = studentAnswers.get(i);
			int correctAnswer = answerSheet.get(i).getAnswer();

			if (studentAnswer == correctAnswer) {
				correctAnswers++;
			}
		}
		// Calculate the final score as a percentage
		double score = ((double) correctAnswers / totalQuestions) * 100;
		double rounded = Math.round(score * 100.0) / 100.0;
		return rounded;
	}
	
	// return grade name
	public static String getGradeName(String value) {
		String gradeText = "";
		switch(value) {
			case "1": gradeText = "P2"; break;
			case "2": gradeText = "P3"; break;
			case "3": gradeText = "P4"; break;
			case "4": gradeText = "P5"; break;
			case "5": gradeText = "P6"; break;
			case "6": gradeText = "S7"; break;
			case "7": gradeText = "S8"; break;
			case "8": gradeText = "S9"; break;
			case "9": gradeText = "S10"; break;
			case "10": gradeText = "S10E"; break;
			case "11": gradeText = "TT6"; break;
			case "12": gradeText = "TT8"; break;
			case "13": gradeText = "TT8E"; break;
			case "14": gradeText = "SRW4"; break;
			case "15": gradeText = "SRW5"; break;
			case "16": gradeText = "SRW6"; break;
			case "17": gradeText = "SRW7"; break;
			case "18": gradeText = "SRW8"; break;
			case "19": gradeText = "JMSS"; break;
			case "20": gradeText = "VCE"; 
		}
		return gradeText;
	}

	/**
	 * {@code Course.grade} codes 14–18 correspond to SRW4–SRW8 in {@code jac.grade}.
	 * When multiple enrolments overlap the same week, non-SRW grades should take precedence for online session resolution.
	 */
	public static boolean isSrwCourseGradeCode(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		switch (value.trim()) {
			case "14":
			case "15":
			case "16":
			case "17":
			case "18":
			case "20": // VCE
				return true;
			default:
				return false;
		}
	}

	// return grade year name
	public static String getGradeYearName(String value) {
		String gradeText = "";
		switch(value) {
			case "1": gradeText = "Year 2"; break;
			case "2": gradeText = "Year 3"; break;
			case "3": gradeText = "Year 4"; break;
			case "4": gradeText = "Year 5"; break;
			case "5": gradeText = "Year 6"; break;
			case "6": gradeText = "Year 7"; break;
			case "7": gradeText = "Year 8"; break;
			case "8": gradeText = "Year 9"; break;
			case "9": gradeText = "Year 10"; break;
			case "10": gradeText = "Year 1"; 
		}
		return gradeText;
	}

	// return branch name
	public static String getBranchName(String value) {
		String branchText = "";
		switch(value) {
			case "12": branchText = "Box Hill"; break;
			case "13": branchText = "Braybrook"; break;
			case "14": branchText = "Chadstone"; break;
			case "15": branchText = "Cranbourne"; break;
			case "16": branchText = "Epping"; break;
			case "17": branchText = "Glen Waverley"; break;
			case "18": branchText = "Narre Warren"; break;
			case "19": branchText = "Mitcham"; break;
			case "20": branchText = "Preston"; break;
			case "21": branchText = "Richmond"; break;
			case "22": branchText = "Springvale"; break;
			case "23": branchText = "St.Albans"; break;
			case "24": branchText = "Werribee"; break;
			case "25": branchText = "Balwyn"; break;
			case "26": branchText = "Rowville"; break;
			case "27": branchText = "Caroline Springs"; break;
			case "28": branchText = "Bayswater"; break;
			case "29": branchText = "Point Cook"; break;
			case "30": branchText = "Craigieburn"; break;
			case "31": branchText = "Mernda"; break;
			case "32": branchText = "Melton"; break;
			case "33": branchText = "Glenroy"; break;
			case "34": branchText = "Pakenham"; break;
			case "35": branchText = "Tarneit"; break;
			case "36": branchText = "Clyde"; break;
			default: branchText = "Testing"; 
		}
		return branchText;
	}

	// return answer
	public static String getAnswer(int value) {
		String answer = "";
		switch(value) {
			case 1: answer = "A"; break;
			case 2: answer = "B"; break;
			case 3: answer = "C"; break;
			case 4: answer = "D"; break;
			case 5: answer = "E"; break;
		}
		return answer;
	}

	// return difference between startDateTime and endDateTime
	public static long calculateDurationInMinutes(LocalDateTime startDateTime, LocalDateTime endDateTime){
		if (startDateTime != null && endDateTime != null) {
            Duration duration = Duration.between(startDateTime, endDateTime);
            return duration.toMinutes();
        } else {
            return 0; // or handle the null case as needed
        }
	}
	
	// return difference between startTime and endTime
	public static long calculateDurationInMinutes(String startTime, String endTime) {
        if (startTime != null && endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(startTime, formatter);
            LocalTime end = LocalTime.parse(endTime, formatter);
            Duration duration = Duration.between(start, end);
            return duration.toMinutes();
        } else {
            return 0; // or handle the null case as needed
        }
    }

	// return String array by delimiting string with comma
	public static String[] splitString(String value) {
		String[] parts = value.split(",");
		return parts;
	}

	// return String by joining string array with comma
	public static String joinString(String[] values) {
		if(values == null || values.length == 0)
		{ 
			return "";
		}else if(values.length == 1) {
			return values[0];
		}else{
			String joined = String.join(",", values);
			return joined;
		}
	}

	// return test group name
	public static String getTestGroup(int value) {
		String answer = "";
		switch(value) {
			case 1: answer = "Mega Test"; break;
			case 2: answer = "Revision Test"; break;
			case 3: answer = "Edu Test"; break;
			case 4: answer = "Acer Test"; break;
			case 5: answer = "Mock Test"; break;
		}
		return answer;
	}

	// format answer
	public static String formatAnswer(int num){
		String answer = "";
		switch(num){
			case 1:
				answer = "A";
				break;
			case 2:
				answer = "B";
				break;
			case 3:
				answer = "C";
				break;
			case 4:
				answer = "D";
				break;
			case 5:
				answer = "E";
				break;
			default:
				answer = "";
		}
		return answer;
	}
	
}
