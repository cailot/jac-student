
package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.LoginActivityDTO;

public interface LoginActivityService {
	
	// save login entry
	void saveLoginActivity(Long studentId);

	// list students login count in studyList.jsp
	List<LoginActivityDTO> listStudentLogin(String branch, String grade, String from, String to);

}
