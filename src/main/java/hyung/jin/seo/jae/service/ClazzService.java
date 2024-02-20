package hyung.jin.seo.jae.service;

import java.util.List;
import java.util.Optional;

import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.model.Clazz;

public interface ClazzService {

	// bring clazz by id
	Clazz getClazz(Long id);

	// list all class
	List<ClazzDTO> allClazz();

	// bring class list base on the condition
	List<ClazzDTO> listClazz(String state, String branch, String grade, String year);

	// bring onsite class list base on the condition
	List<ClazzDTO> listOnsiteClazz(String state, String branch, String grade, String year);

	// bring online class list base on the condition
	List<ClazzDTO> listOnlineClazz(String state, String branch, String grade, String year);

	// bring class list for dropdown list
	List<ClazzDTO> filterClazz(String state, String branch, String grade);

	// bring on-site class list for dropdown list
	List<ClazzDTO> filterOnSiteClazz(String state, String branch, String grade);

	// bring on-site class list for dropdown list in Teacher page
	List<ClazzDTO> filterOnSiteClazz(String state, String branch, String grade, String year);

	// bring class Ids
	List<Long> filterClazzIds(String state, String branch, String grade);

	// list all class for grade & year
	List<ClazzDTO> findClazzForGradeNCycle(String grade, int year);

	// list all class for courseId & year
	List<ClazzDTO> findClazzForCourseIdNCycle(Long id, int year);

	// list class for courseId & year & state & branch
	List<ClazzDTO> findClazzForCourseIdNCycleNStateNBranch(Long id, int year, String state, String branch);

	// return total count
	long checkCount();

	// add class
	ClazzDTO addClazz(Clazz clazz);

	// update class
	ClazzDTO updateClazz(Clazz clazz);

	// get class price
	double getPrice(Long id);

	// get academic year
	int getAcademicYear(Long id);

	// get grade
	String getGrade(Long id);

	// get day
	String getDay(Long id);

	// get name
	String getName(Long id);

	// get class id by grade and year
	Long getOnlineId(String grade, int year);

	// get online class by grade and year
	Clazz getOnlineByGradeNYear(String grade, int year);
}