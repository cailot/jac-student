package hyung.jin.seo.jae.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.StudentAccount;
import hyung.jin.seo.jae.model.Student;
import hyung.jin.seo.jae.service.StudentAccountService;
import hyung.jin.seo.jae.repository.StudentRepository;

@Service
public class StudentAccountServiceImpl implements StudentAccountService {

	@Autowired
	private StudentRepository studentRepository;

	// @Autowired
	// private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		StudentAccount account = null;
		try{
			Object[] result = studentRepository.checkStudentAccount(Long.parseLong(username));
			if(result!=null && result.length > 0){
				Object[] obj = (Object[])result[0];
				account = new StudentAccount(obj);
			}
		}catch(Exception e){
			throw new UsernameNotFoundException("User : " + username + " was not found in the database");
		}
		return account;
	}

	@Override
	public void updatePassword(Long id, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		try{
			studentRepository.updatePassword(id, encodedPassword);
		}catch(Exception e){
			System.out.println("No student found");
		}	
		
	}

	@Override
	public Student getStudent(Long id) {
		Student std = null;
		try{
			std = studentRepository.findById(id).get();
		}catch(Exception e){
			System.out.println("No student found");
		}
		// studentRepository.findById(id).get();	
		return std;
	}


}
