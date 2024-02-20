package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.ClazzDTO;
import hyung.jin.seo.jae.model.Clazz;
import hyung.jin.seo.jae.repository.ClazzRepository;
import hyung.jin.seo.jae.service.ClazzService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Service
public class ClazzServiceImpl implements ClazzService {

	@Autowired
	private ClazzRepository clazzRepository;

	@Override
	public long checkCount() {
		long count = clazzRepository.count();
		return count;
	}

	@Override
	public List<ClazzDTO> allClazz() {
		List<Clazz> crs = new ArrayList<>();
		try {
			crs = clazzRepository.findAll();
		} catch (Exception e) {
			System.out.println("No class found");
		}
		// clazzRepository.findAll();
		List<ClazzDTO> dtos = new ArrayList<>();
		for (Clazz claz : crs) {
			ClazzDTO dto = new ClazzDTO(claz);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> findClazzForGradeNCycle(String grade, int year) {
		// 1. get classes
		List<ClazzDTO> dtos = new ArrayList<>();
		try {
			dtos = clazzRepository.findClassForGradeNCycle(grade, year);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> findClazzForCourseIdNCycle(Long id, int year) {
		List<ClazzDTO> dtos = new ArrayList<>();
		try {
			dtos = clazzRepository.findClassForCourseIdNCycle(id, year);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		// clazzRepository.findClassForCourseIdNCycle(id, year);
		return dtos;
	}

	@Override
	public ClazzDTO addClazz(Clazz clazz) {
		Clazz cla = clazzRepository.save(clazz);
		ClazzDTO dto = new ClazzDTO(cla);
		return dto;
	}

	@Override
	public Clazz getClazz(Long id) {
		Clazz clazz = null;
		try {
			clazz = clazzRepository.findById(id).get();
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return clazz;
	}

	@Override
	public ClazzDTO updateClazz(Clazz clazz) {
		// search by getId
		Clazz existing = clazzRepository.findById(clazz.getId())
				.orElseThrow(() -> new EntityNotFoundException("Clazz Not Found"));
		// Update info
		// state
		String newState = clazz.getState();
		existing.setState(newState);
		// branch
		String newBranch = clazz.getBranch();
		existing.setBranch(newBranch);
		// start date
		LocalDate newStartDate = clazz.getStartDate();
		existing.setStartDate(newStartDate);
		// name
		String newName = clazz.getName();
		existing.setName(newName);
		// day
		String newDay = clazz.getDay();
		existing.setDay(newDay);
		// price
		double newPrice = clazz.getPrice();
		existing.setPrice(newPrice);
		// active
		boolean newActive = clazz.isActive();
		existing.setActive(newActive);
		// update Course & Cycle
		existing.setCourse(clazz.getCourse());
		existing.setCycle(clazz.getCycle());
		// update the existing record
		Clazz updated = clazzRepository.save(existing);
		ClazzDTO dto = new ClazzDTO(updated);
		return dto;
	}

	@Override
	public List<ClazzDTO> listClazz(String state, String branch, String grade, String year) {
		List<ClazzDTO> dtos = null;
		if (StringUtils.isNotBlank(year) && (!StringUtils.equals(year, JaeConstants.ALL))) {
			dtos = clazzRepository.findClassForStateNBranchNGradeNYear(state, branch, grade, Integer.parseInt(year));
		} else {
			dtos = clazzRepository.findClassForStateNBranchNGrade(state, branch, grade);
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> filterClazz(String state, String branch, String grade) {
		List<ClazzDTO> dtos = new ArrayList<>();
		try {
			dtos = clazzRepository.findClassForStateNBranchNGrade(state, branch, grade);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return dtos;
	}

	@Override
	public double getPrice(Long id) {
		double price = 0;
		try {
			price = clazzRepository.getPrice(id);
		} catch (Exception e) {
			System.out.println("No price found");
		}
		return price;
	}

	@Override
	public int getAcademicYear(Long id) {
		int year = 0;
		try {
			year = clazzRepository.getYear(id);
		} catch (Exception e) {
			System.out.println("No academic year found");
		}
		return year;
	}

	@Override
	public String getDay(Long id) {
		String day = "";
		try {
			day = clazzRepository.getDay(id);
		} catch (Exception e) {
			System.out.println("No day found");
		}
		return day;
	}

	@Override
	public String getName(Long id) {
		String name = "";
		try {
			name = clazzRepository.getName(id);
		} catch (Exception e) {
			System.out.println("No name found");
		}
		return name;
	}

	@Override
	public List<Long> filterClazzIds(String state, String branch, String grade) {
		List<Long> ids = new ArrayList<>();
		try {
			ids = clazzRepository.findClassIdsForStateNBranchNGrade(state, branch, grade);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return ids;
	}

	@Override
	public Long getOnlineId(String grade, int year) {
		Long id = 0L;
		try {
			id = clazzRepository.getOnlineClazzId(grade, year);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return id;
	}

	@Override
	public String getGrade(Long id) {
		String grade = "";
		try {
			grade = clazzRepository.getGrade(id);
		} catch (Exception e) {
			System.out.println("No grade found");
		}
		return grade;
	}

	@Override
	public List<ClazzDTO> filterOnSiteClazz(String state, String branch, String grade) {
		List<ClazzDTO> dtos = new ArrayList<>();
		try {
			dtos = clazzRepository.findOnSiteClassForStateNBranchNGrade(state, branch, grade);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> findClazzForCourseIdNCycleNStateNBranch(Long id, int year, String state, String branch) {
		List<ClazzDTO> dtos = new ArrayList<>();
		try {
			dtos = clazzRepository.findClassForCourseIdNCycleNStateNBranch(id, year, state, branch);
		} catch (Exception e) {
			System.out.println("No class found");
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> filterOnSiteClazz(String state, String branch, String grade, String year) {
		List<ClazzDTO> dtos = new ArrayList<>();
		if (StringUtils.isNotBlank(year) && (!StringUtils.equals(year, JaeConstants.ALL))) {
			dtos = clazzRepository.findOnSiteClassForStateNBranchNGradeNYear(state, branch, grade, Integer.parseInt(year));
		} else {
			dtos = clazzRepository.findOnSiteClassForStateNBranchNGrade(state, branch, grade);
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> listOnsiteClazz(String state, String branch, String grade, String year) {
		List<ClazzDTO> dtos = null;
		if (StringUtils.isNotBlank(year) && (!StringUtils.equals(year, JaeConstants.ALL))) {
			dtos = clazzRepository.findOnsiteClassForStateNBranchNGradeNYear(state, branch, grade, Integer.parseInt(year));
		} else {
			dtos = clazzRepository.findOnsiteClassForStateNBranchNGrade(state, branch, grade);
		}
		return dtos;
	}

	@Override
	public List<ClazzDTO> listOnlineClazz(String state, String branch, String grade, String year) {
		List<ClazzDTO> dtos = null;
		if (StringUtils.isNotBlank(year) && (!StringUtils.equals(year, JaeConstants.ALL))) {
			dtos = clazzRepository.findOnlineClassForStateNBranchNGradeNYear(state, branch, grade, Integer.parseInt(year));
		} else {
			dtos = clazzRepository.findOnlineClassForStateNBranchNGrade(state, branch, grade);
		}
		return dtos;
	}

	@Override
	public Clazz getOnlineByGradeNYear(String grade, int year) {
		Optional<Clazz> option = clazzRepository.getClazz4OnlineSession(grade, year);
		Clazz clazz = option.orElse(null);
		return clazz;
	}

}