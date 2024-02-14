package hyung.jin.seo.jae.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import hyung.jin.seo.jae.model.Student;

public interface StudentAccountService extends UserDetailsService {

    // internally invoked when user logs in
    //UserDetails loadUserByUsername

    // get Student info
    Student getStudent(Long id);

    // update password
    void updatePassword(Long id, String password);
}
