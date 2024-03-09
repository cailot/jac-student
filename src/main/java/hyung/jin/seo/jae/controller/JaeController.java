package hyung.jin.seo.jae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JaeController {


	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//	Online Course
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/online/login")
	public String showOnlineLogin() {
		return "onlineLogin";
	}

	@GetMapping("/online/logout")
	public String redirectOnlineLogin() {
		return "redirect:/online/login";
	}

	@GetMapping("/online/lesson")
	public String populateOnlineCourse() {
		return "onlinePage";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//	Connected Class
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/connected/login")
	public String showConnectedLogin() {
		return "connectedLogin";
	}

	@GetMapping("/connected/logout")
	public String redirectConnectedLogin() {
		return "redirect:/connected/login";
	}

	@GetMapping("/connected/lesson")
	public String populateConnectedClass() {
		return "connectedPage";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Homework Menu
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// english homework
	@GetMapping("/connected/engHomework")
	public String dispayEngHomework() {
		return "engHomeworkPage";
	}

	// math homework
	@GetMapping("/connected/mathHomework")
	public String dispayMathHomework() {
		return "mathHomeworkPage";
	}

	// writing homework
	@GetMapping("/connected/writeHomework")
	public String dispayWritingHomework() {
		return "writeHomeworkPage";
	}

	// homework answer
	@GetMapping("/connected/shortAnswer")
	public String dispayAnswerHomework() {
		return "shortAnswerPage";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Extra Material Menu
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// extra materials
	@GetMapping("/connected/extraMaterial")
	public String dispayExtraMaterial() {
		return "extraMaterialPage";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Practice Menu
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// mega english
	@GetMapping("/connected/megaEng")
	public String dispayMegaEngPractice() {
		return "megaEngPage";
	}

	// mega math
	@GetMapping("/connected/megaMath")
	public String dispayMegaMathPractice() {
		return "megaMathPage";
	}

	// mega GA
	@GetMapping("/connected/megaGA")
	public String dispayMegaGAPractice() {
		return "megaGAPage";
	}

	// naplan math
	@GetMapping("/connected/naplanMath")
	public String dispayNaplanMathPractice() {
		return "naplanMathPage";
	}

	// naplan LC
	@GetMapping("/connected/naplanLC")
	public String dispayNaplanLCPractice() {
		return "naplanLCPage";
	}

	// naplan Reading
	@GetMapping("/connected/naplanRead")
	public String dispayNaplanReadPractice() {
		return "naplanReadPage";
	}


}
