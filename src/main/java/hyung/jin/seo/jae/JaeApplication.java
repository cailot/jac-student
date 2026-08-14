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
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class JaeApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private CycleService cycleService;

	@Value("${app.version:unknown}")
	private String appVersion;

	// @Autowired
	// private EmailService emailService;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Australia/Melbourne"));
		SpringApplication.run(JaeApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logStartupSummary() {
		System.out.println("=======> Application Current time: " + java.time.LocalDateTime.now());
		System.out.println("=======> Application Version: " + appVersion);
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
		// emailService.test("jh05052008@gmail.com");
	}

}
