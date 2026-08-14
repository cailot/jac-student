package hyung.jin.seo.jae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import hyung.jin.seo.jae.service.PropertiesService;

@Controller
@RequestMapping("config")
public class PropertiesController {

	@Autowired
	private PropertiesService propertiesService;

	// get Homework Normal value
	@GetMapping("/homeworkSubject")
	public int getHomeworkNormal(){
		return propertiesService.getSubjectCardCount();
    }

	// get Homework Short value
	@GetMapping("/homeworkShort")
	public int getHomeworkShort(){
		return propertiesService.getAnswerCardCount();
    }
	
}