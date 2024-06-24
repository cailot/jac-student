package hyung.jin.seo.jae.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import hyung.jin.seo.jae.dto.UserDTO;
import hyung.jin.seo.jae.model.User;

public interface UserRepository extends JpaRepository<User, String>{  
	
	@SuppressWarnings("null")
	List<User> findAll();

	User findByUsername(String username);
	
	@Query(value = "UPDATE User u SET u.password = ?2 WHERE u.id = ?1 AND ACTIVE = 0", nativeQuery = true)
    void updatePassword(Long id, String password);    

    @Query(value = "SELECT u.username, u.password, u.enabled, u.firstName, u.lastName, u.role, u.state, u.branch, u.email, u.phone FROM User u WHERE u.username =?1 AND u.enabled = 0", nativeQuery = true)   
	Object[] checkUserAccount(String username);

	@Query("SELECT new hyung.jin.seo.jae.dto.UserDTO(u.username, u.firstName, u.lastName, u.enabled, u.phone, u.email, u.role, u.state, u.branch) FROM User u WHERE (?1 = '0' OR u.role = ?1) AND (?2 = '0' OR u.state = ?2) AND (?3 = '0' OR u.branch = ?3)")
	List<UserDTO> listUsers(String role, String state, String branch);

	@Modifying
	@Query(value = "UPDATE User u SET u.password = ?2 WHERE u.username = ?1 AND u.enabled = 0", nativeQuery = true)
	void updatePassword(String username, String password);    

	@Query(value = "DELETE FROM User WHERE username = ?1", nativeQuery = true)
	void deleteByUsername(String username);    

}

