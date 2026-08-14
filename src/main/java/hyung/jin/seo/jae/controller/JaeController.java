package hyung.jin.seo.jae.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hyung.jin.seo.jae.service.LoginActivityService;
import hyung.jin.seo.jae.service.StudentAccountService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;

@Controller
public class JaeController {

	@Autowired
	private LoginActivityService loginActivityService;

	@Autowired
	private StudentAccountService studentAccountService;

	@Value("${mock.explanation.enabled:false}")
	private boolean mockExplanationEnabled;

	// URL-based authentication for online system (with encrypted password from database)
	@GetMapping("/online/urlLoginEncrypted")
	public String authenticateOnlineByEncryptedUrl(@RequestParam("id") String id, 
	                                              @RequestParam("encPassword") String encryptedPassword,
	                                              HttpServletRequest request, 
	                                              HttpServletResponse response) {
		try {
			// Load user details directly
			UserDetails userDetails = studentAccountService.loadUserByUsername(id);
			
			// Check if the encrypted password matches
			if (userDetails != null && userDetails.getPassword().equals(encryptedPassword)) {
				// Create authenticated token directly
				UsernamePasswordAuthenticationToken authToken = 
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				// Set additional details
				authToken.setDetails(new WebAuthenticationDetails(request));
				
				// Set the authentication in the security context
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
				// Save login activity
				loginActivityService.saveLoginActivity(Long.parseLong(id));
				
				// Redirect to online lesson page
				return "redirect:/online/lesson";
			} else {
				// Authentication failed
				return "redirect:/online/login?error=credentials";
			}
			
		} catch (DisabledException e) {
			// Handle payment and enrolment specific errors
			String message = e.getMessage();
			if (message != null && message.contains("Payment not completed")) {
				return "redirect:/online/login?error=payment";
			} else if (message != null && message.contains("Enrolment is not valid")) {
				return "redirect:/online/login?error=enrolment";
			} else {
				return "redirect:/online/login?error=disabled";
			}
		} catch (UsernameNotFoundException e) {
			// Student not found
			return "redirect:/online/login?error=credentials";
		} catch (Exception e) {
			// Any other authentication failure
			return "redirect:/online/login?error=credentials";
		}
	}

	// URL-based authentication for connected system (with encrypted password from database)
	// @GetMapping("/connected/urlLoginEncrypted")
	// public String authenticateConnectedByEncryptedUrl(@RequestParam("id") String id, 
	//                                                   @RequestParam("encPassword") String encryptedPassword,
	//                                                   HttpServletRequest request, 
	//                                                   HttpServletResponse response) {
	// 	try {
	// 		// Load user details directly
	// 		UserDetails userDetails = studentAccountService.loadUserByUsername(id);
			
	// 		// Check if the encrypted password matches
	// 		if (userDetails != null && userDetails.getPassword().equals(encryptedPassword)) {
	// 			// Create authenticated token directly
	// 			UsernamePasswordAuthenticationToken authToken = 
	// 				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
	// 			// Set additional details
	// 			authToken.setDetails(new WebAuthenticationDetails(request));
				
	// 			// Set the authentication in the security context
	// 			SecurityContextHolder.getContext().setAuthentication(authToken);
				
	// 			// Save login activity
	// 			loginActivityService.saveLoginActivity(Long.parseLong(id));
				
	// 			// Redirect to connected lesson page
	// 			return "redirect:/connected/lesson";
	// 		} else {
	// 			// Authentication failed
	// 			return "redirect:/connected/login?error=true";
	// 		}
			
	// 	} catch (Exception e) {
	// 		// Authentication failed - redirect to login page with error
	// 		return "redirect:/connected/login?error=true";
	// 	}
	// }

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
	public String populateConnectedClass(Model model) {
		// Landing only: tilePage.jsp skips background.jpg (other tiles keep photo background).
		model.addAttribute("connectedClassLanding", Boolean.TRUE);
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
	// mega practice
	@GetMapping("/connected/practice/mega")
	public String dispayMegaPractice() {
		return "megaPracticePage";
	}

	// revision practice
	@GetMapping("/connected/practice/revision")
	public String dispayRevisionPractice() {
		return "revisionPracticePage";
	}

	// naplan practice
	@GetMapping("/connected/practice/naplan")
	public String dispayNaplanPractice() {
		return "naplanPracticePage";
	}

	// edu practice
	@GetMapping("/connected/practice/edu")
	public String dispayEduPractice() {
		return "eduPracticePage";
	}

	// acer practice
	@GetMapping("/connected/practice/acer")
	public String dispayAcerPractice() {
		return "acerPracticePage";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Test Menu
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// mega
	@GetMapping("/connected/test/mega")
	public String dispayMegaTest() {
		return "megaTestPage";
	}

	// mega explanation
	@GetMapping("/connected/test/megaExplanation")
	public String dispayMegaExplanation() {
		return "megaExplanationPage";
	}

	@GetMapping("/connected/test/revision")
	public String dispayRevisionTest() {
		return "revisionTestPage";
	}

	// revision explanation
	@GetMapping("/connected/test/revisionExplanation")
	public String dispayRevisionExplanation() {
		return "revisionExplanationPage";
	}

	@GetMapping("/connected/test/acer")
	public String dispayAcerTest() {
		return "acerTestPage";
	}

	@GetMapping("/connected/test/edu")
	public String dispayEduTest() {
		return "eduTestPage";
	}

	@GetMapping("/connected/test/ttExplanation")
	public String dispayTtExplanation() {
		return "ttExplanationPage";
	}

	@GetMapping("/connected/test/mockTestExplanation")
	public String displayMockTestExplanation() {
		if (!mockExplanationEnabled) {
			return "redirect:/connected/test/ttExplanation";
		}
		return "mockTestExplanationPage";
	}

	@GetMapping("/connected/test/mock")
	public String dispayMockTest() {
		return "mockTestPage";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Test Result Menu
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// mega vol 1 result
	@GetMapping("/connected/result/megaVol1")
	public String dispayMegaVol1Result() {
		return "megaVol1ResultPage";
	}
	
	// mega vol 2 result
	@GetMapping("/connected/result/megaVol2")
	public String dispayMegaVol2Result() {
		return "megaVol2ResultPage";
	}
	
	// mega vol 3 result
	@GetMapping("/connected/result/megaVol3")
	public String dispayMegaVol3Result() {
		return "megaVol3ResultPage";
	}

	// mega vol 4 result
	@GetMapping("/connected/result/megaVol4")
	public String dispayMegaVol4Result() {
		return "megaVol4ResultPage";
	}

	// mega vol 5 result
	@GetMapping("/connected/result/megaVol5")
	public String dispayMegaVol5Result() {
		return "megaVol5ResultPage";
	}

	// revision vol 1 result
	@GetMapping("/connected/result/revisionVol1")
	public String dispayRevisionVol1Result() {
		return "revisionVol1ResultPage";
	}
	
	// revision vol 2 result
	@GetMapping("/connected/result/revisionVol2")
	public String dispayRevisionVol2Result() {
		return "revisionVol2ResultPage";
	}
	
	// revision vol 3 result
	@GetMapping("/connected/result/revisionVol3")
	public String dispayRevisionVol3Result() {
		return "revisionVol3ResultPage";
	}

	// revision vol 4 result
	@GetMapping("/connected/result/revisionVol4")
	public String dispayRevisionVol4Result() {
		return "revisionVol4ResultPage";
	}

	// revision vol 5 result
	@GetMapping("/connected/result/revisionVol5")
	public String dispayRevisionVol5Result() {
		return "revisionVol5ResultPage";
	}

	// math result
	@GetMapping("/connected/result/mathResult")
	public String dispayMathResult() {
		return "mathResultPage";
	}

	// english result
	@GetMapping("/connected/result/engResult")
	public String dispayEnglishResult() {
		return "engResultPage";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Assessment
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/online_assessment")
	public String assessmentWelcome() {
		return "assessmentWelcomePage";
	}
	
	@GetMapping("/assessment/apply")
	public String assessmentApply() {
		return "assessmentApplyPage";
	}

	@GetMapping("/assessment/list")
	public String assessmentList() {
		return "assessmentListPage";
	}

	@GetMapping("/assessment/math")
	public String assessmentMath() {
		return "assessmentMathPage";
	}

	@GetMapping("/assessment/english")
	public String assessmentEnglish() {
		return "assessmentEngishPage";
	}

	@GetMapping("/assessment/ga")
	public String assessmentGA() {
		return "assessmentGAPage";
	}
}
