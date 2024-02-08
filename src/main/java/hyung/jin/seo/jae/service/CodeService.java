package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.GradeDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.dto.StateDTO;
import hyung.jin.seo.jae.model.Branch;
import hyung.jin.seo.jae.model.Grade;
import hyung.jin.seo.jae.model.State;

public interface CodeService {

	// list all state
	List<StateDTO> allStates();

	// list for initial state value
	List<SimpleBasketDTO> loadState();

	// get State
	State getState(Long id);
	
	// update State
	State updateState(State newState, Long id);

	// list all branch
	List<BranchDTO> allBranches();

	// list branch by state
	List<BranchDTO> searchBranchByState(String state);

	// list for initial branch value
	List<SimpleBasketDTO> loadBranch();

	// get branch
	BranchDTO getBranch(Long id);

	// add branch
	Branch addBranch(Branch branch);

	// update branch
	Branch updateBranch(Branch newBranch, Long id);

	// delete branch
	void deleteBranch(Long id);

	// list all grade
	List<GradeDTO> allGrades();

	// list for initial grade value
	List<SimpleBasketDTO> loadGrade();

	// get grade
	Grade getGrade(Long id);

	// add grade
	Grade addGrade(Grade grade);

	// update grade
	Grade updateGrade(Grade newGrade, Long id);

	// delete grade
	void deleteGrade(Long id);

}
