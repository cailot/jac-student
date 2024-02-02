package hyung.jin.seo.jae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import hyung.jin.seo.jae.dto.CycleDTO;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.utils.JaeConstants;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class JaeApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private CycleService cycleService;

	public static void main(String[] args) {
		SpringApplication.run(JaeApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JaeApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		// register cycles to applicationContext
		List<CycleDTO> cycles = cycleService.allCycles();
		applicationContext.getBeanFactory().registerSingleton(JaeConstants.ACADEMIC_CYCLES, cycles);

		// cycleService.getDateByWeekAndDay(2023, 27, "Tuesday");
		// String date = cycleService.academicEndSaturday(2023, 27);
		// System.out.println("********************** " + date);
		// date = "24/12/2023";
		// System.out.println("********************** " + cycleService.academicWeeks(date));
		// System.out.println("********************** " + cycleService.isBelongToHoliday(date));
		// boolean year = JaeUtils.checkIfTodayBelongTo("2022-06-13", "2023-06-11");
		// System.out.println("After ****** " + year);
		// year = JaeUtils.checkIfTodayBelongTo("13/06/2022", "2022-06-13",
		// "2023-06-11");
		// System.out.println("After ****** " + year);
		// year = JaeUtils.checkIfTodayBelongTo("12/05/2022", "2022-06-11",
		// "2023-06-12");
		// System.out.println("After ****** " + year);
		// int year = cycleService.academicYear();
		// System.out.println("********************** " + year);
		// year = cycleService.academicYear("19/08/2024");
		// System.out.println("********************** " + year);
		// int week = cycleService.academicWeeks();
		// System.out.println("********************** " + week);
		// week = cycleService.academicWeeks("19/07/2023");
		// System.out.println("********************** " + week);

		// cycleService.academicStartSunday(2023, 33);
		// String d1 = "15/07/2023";
		// String d2 = "16/07/2023";
		// String d3 = "17/07/2023";

		// System.out.println(JaeUtils.isEarlier(d2, d3));
		// System.out.println(JaeUtils.isEarlier(d1, d2));
		// System.out.println(JaeUtils.isEarlier(d1, d3));

		// System.out.println(JaeUtils.isEarlier(d3, d1));

	}

}