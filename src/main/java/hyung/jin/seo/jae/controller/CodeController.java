package hyung.jin.seo.jae.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.model.Branch;
import hyung.jin.seo.jae.model.State;
import hyung.jin.seo.jae.service.CodeService;
import hyung.jin.seo.jae.utils.JaeConstants;

@Controller
@RequestMapping("code")
public class CodeController {

	@Autowired
	private CodeService codeService;
	
	// list state
	@GetMapping("/state")
	@ResponseBody
	List<SimpleBasketDTO> listState() {
		List<SimpleBasketDTO> dtos = codeService.loadState();
		return dtos;
	}

	// list branch
	@GetMapping("/branch")
	@ResponseBody
	List<SimpleBasketDTO> listBranch() {
		List<SimpleBasketDTO> dtos = codeService.loadBranch();
		return dtos;
	}

	// list grade
	@GetMapping("/grade")
	@ResponseBody
	List<SimpleBasketDTO> listGrade() {
		List<SimpleBasketDTO> dtos = codeService.loadGrade();
		return dtos;
	}

	@GetMapping("/subject")
	@ResponseBody
	List<SimpleBasketDTO> listSubject() {
		List<SimpleBasketDTO> dtos = codeService.loadSubject();
		return dtos;
	}

	@GetMapping("/practiceType")
	@ResponseBody
	List<SimpleBasketDTO> listPracticeType() {
		List<SimpleBasketDTO> dtos = codeService.loadPracticeType();
		return dtos;
	}

	@GetMapping("/testType")
	@ResponseBody
	List<SimpleBasketDTO> listTestType() {
		List<SimpleBasketDTO> dtos = codeService.loadTestType();
		return dtos;
	}

	// list branch by state
	@GetMapping("/listBranch")
	String filterBranch(@RequestParam(value = "listState", required = true) String state, Model model) {
		List<BranchDTO> dtos = new ArrayList<>();
		// search by state
		if (StringUtils.isNotBlank(state) && (!StringUtils.equals(state, JaeConstants.ALL))) {
			dtos = codeService.searchBranchByState(state);
		} else { // search all
			dtos = codeService.allBranches();
		}
		model.addAttribute(JaeConstants.BRANCH_LIST, dtos);
		return "branchPage";
	}

	// register new cycle
	@PostMapping("/registerBranch")
	@ResponseBody
	public ResponseEntity<String> registerBranch(@RequestBody BranchDTO formData) {
		try {
			// 1. create Cycle
			Branch branch = formData.convertToBranch(formData);
			// 2. get State
			State state = codeService.getState(Long.parseLong(formData.getStateId()));
			if(state!=null){
				state.addBranch(branch);
				// 3. update state
				codeService.updateState(state, state.getId());
			}
			// 3. return success;
			return ResponseEntity.ok("\"Branch register success\"");
		} catch (Exception e) {
			String message = "Error registering Branch: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// get branch by Id
	@GetMapping("/getBranch/{id}")
	@ResponseBody
	public BranchDTO getBranch(@PathVariable("id") Long id) {
		BranchDTO dto = new BranchDTO();
		try{
			dto = codeService.getBranch(id);
		}catch(Exception e){
			System.out.println("No branch found");
		}
		return dto;
	}

	// update existing branch
	@PutMapping("/updateBranch")
	@ResponseBody
	public ResponseEntity<String> updateBranch(@RequestBody BranchDTO formData) {
		try {
			// 1. create Branch
			Branch branch = formData.convertToBranch(formData);
			// 2. update Branch
			codeService.updateBranch(branch, branch.getId());
			// 3. return flag
			return ResponseEntity.ok("\"Branch update success\"");
		} catch (Exception e) {
			String message = "Error updating branch: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	// remove branch by Id
	@PutMapping("/deleteBranch/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteBranch(@PathVariable("id") Long id) {
		try{
			codeService.deleteBranch(id);
			return ResponseEntity.ok("\"Branch delete success\"");		
		}catch(Exception e){
			String message = "Error deleting branch: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}


}