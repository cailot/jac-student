package hyung.jin.seo.jae.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long>{  
	
	List<Branch> findAll();

	@Query("SELECT new hyung.jin.seo.jae.dto.BranchDTO(b.id, b.code, b.name, b.phone, b.email, b.address, b.abn, b.bank, b.bsb, b.accountNumber, b.accountName, b.info, b.state.id) FROM Branch b ORDER BY b.code ASC")
	List<BranchDTO> getAllBranches();

	@Query(value = "SELECT b.name, b.code FROM Branch b", nativeQuery = true)   
	List<Object[]> loadBranch();
	
	@Query(value = "SELECT b.email FROM Branch b WHERE b.code = ?1", nativeQuery = true)
	Optional<String> getEmail(String code);

	@Query("SELECT new hyung.jin.seo.jae.dto.BranchDTO(b.id, b.code, b.name, b.phone, b.email, b.address, b.abn, b.bank, b.bsb, b.accountNumber, b.accountName, b.info, b.state.id) FROM Branch b WHERE b.state.id = ?1 ORDER BY b.code ASC")
	List<BranchDTO> searchBranchByState(long state);

	@Query("SELECT new hyung.jin.seo.jae.dto.BranchDTO(b.id, b.code, b.name, b.phone, b.email, b.address, b.abn, b.bank, b.bsb, b.accountNumber, b.accountName, b.info, b.state.id) FROM Branch b WHERE b.id = ?1")
	BranchDTO findBranch(long id);

	@Query("SELECT new hyung.jin.seo.jae.dto.BranchDTO(b.id, b.code, b.name, b.phone, b.email, b.address, b.abn, b.bank, b.bsb, b.accountNumber, b.accountName, b.info, b.state.id) FROM Branch b WHERE b.code = ?1")
	BranchDTO findBranchByCode(String code);

	@Query("SELECT new hyung.jin.seo.jae.dto.BranchDTO(b.id, b.code, b.name, b.phone, b.email, b.address, b.abn, b.bank, b.bsb, b.accountNumber, b.accountName, b.info, b.state.id) FROM Branch b WHERE b.state.id = ?1 AND b.code = ?2")
	BranchDTO findBranchByStateNCode(Long state, String code);

	@Query("SELECT new hyung.jin.seo.jae.dto.SimpleBasketDTO(b.email, b.info) FROM Branch b WHERE b.code =?1")   
	SimpleBasketDTO getBranchInfo(String code);
	
}
