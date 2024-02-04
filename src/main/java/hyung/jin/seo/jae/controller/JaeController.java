package hyung.jin.seo.jae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JaeController {


	@GetMapping("/login")
	public String showLoginPage() {
		return "loginPage";
	}

	@GetMapping("/online")
	public String populateOnlineCourse() {
		return "onlinePage";
	}
}
