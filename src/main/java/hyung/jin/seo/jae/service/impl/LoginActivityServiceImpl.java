package hyung.jin.seo.jae.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.LoginActivityDTO;
import hyung.jin.seo.jae.model.LoginActivity;
import hyung.jin.seo.jae.repository.LoginActivityRepository;
import hyung.jin.seo.jae.service.LoginActivityService;

@Service
public class LoginActivityServiceImpl implements LoginActivityService {
	
	@Autowired
	private LoginActivityRepository loginActivityRepository;

	@Override
	@Transactional
	public void saveLoginActivity(Long studentId) {
		LoginActivity loginActivity = new LoginActivity();
		loginActivity.setStudentId(studentId);
		loginActivityRepository.save(loginActivity);
	}

	@Override
	public List<LoginActivityDTO> listStudentLogin(String branch, String grade, String from, String to) {
		List<LoginActivityDTO> dtos = new ArrayList<>();
		try{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate fromDate = LocalDate.parse(from, formatter);
			LocalDate toDate = LocalDate.parse(to, formatter);
			dtos = loginActivityRepository.findStudentLoginCounts(branch, grade, fromDate, toDate);	
		}catch(Exception e){
			System.out.println("No student found");
		}
		return dtos;
	}

		
}
