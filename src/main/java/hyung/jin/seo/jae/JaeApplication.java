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
	}

}
