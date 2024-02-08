package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.BranchDTO;
import hyung.jin.seo.jae.dto.GradeDTO;
import hyung.jin.seo.jae.dto.SimpleBasketDTO;
import hyung.jin.seo.jae.dto.StateDTO;
import hyung.jin.seo.jae.model.Branch;
import hyung.jin.seo.jae.model.Grade;
import hyung.jin.seo.jae.model.State;
import hyung.jin.seo.jae.repository.BranchRepository;
import hyung.jin.seo.jae.repository.GradeRepository;
import hyung.jin.seo.jae.repository.StateRepository;
import hyung.jin.seo.jae.service.CodeService;

@Service
public class CodeServiceImpl implements CodeService {

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private GradeRepository gradeRepository;

	@Override
	public List<StateDTO> allStates() {
		List<StateDTO> dtos = new ArrayList<>();
		try{
			List<State> states = stateRepository.findAll();
			for(State state : states){
				StateDTO dto = new StateDTO(state);
				dtos.add(dto);
			}
		}catch(Exception e){
			System.out.println("No state found");
		}
		return dtos;
	}

	@Override
	public List<SimpleBasketDTO> loadState() {
		List<Object[]> objects = new ArrayList<>();
		try{
			objects = stateRepository.loadState();
		}catch(Exception e){
			System.out.println("No state found");
		}
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		for(Object[] object : objects){
			SimpleBasketDTO dto = new SimpleBasketDTO(object);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<BranchDTO> allBranches() {
		List<BranchDTO> dtos = new ArrayList<>();
		try{
			dtos = branchRepository.getAllBranches();
		}catch(Exception e){
			System.out.println("No branch found");
		}
		return dtos;
	}

	@Override
	public List<BranchDTO> searchBranchByState(String state) {
		List<BranchDTO> dtos = new ArrayList<>();
		try{
			dtos = branchRepository.searchBranchByState(Long.parseLong(state));
		}catch(Exception e){
			System.out.println("No branch found");
		}
		return dtos;
	}

	@Override
	public List<SimpleBasketDTO> loadBranch() {
		List<Object[]> objects = new ArrayList<>();
		try{
			objects = branchRepository.loadBranch();
		}catch(Exception e){
			System.out.println("No branch found");
		}
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		for(Object[] object : objects){
			SimpleBasketDTO dto = new SimpleBasketDTO(object);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public Branch addBranch(Branch branch) {
		Branch bran = branchRepository.save(branch);
		return bran;
	}

	@Override
	public Branch updateBranch(Branch newBranch, Long id) {
		Branch bran = branchRepository.findById(id).map(branch -> {
			branch.setCode(newBranch.getCode());
			branch.setName(newBranch.getName());
			branch.setPhone(newBranch.getPhone());
			branch.setEmail(newBranch.getEmail());
			branch.setAddress(newBranch.getAddress());
			branch.setAbn(newBranch.getAbn());
			branch.setBank(newBranch.getBank());
			branch.setBsb(newBranch.getBsb());
			branch.setAccountNumber(newBranch.getAccountNumber());
			branch.setAccountName(newBranch.getAccountName());
			branch.setInfo(newBranch.getInfo());
			return branchRepository.save(branch);
		}).orElseGet(() -> {
			newBranch.setId(id);
			return branchRepository.save(newBranch);
		});
		return bran;
	}

	@Override
	public void deleteBranch(Long id) {
		try {
			branchRepository.deleteById(id);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to delete");
		}
	}

	@Override
	public State getState(Long id) {
		State state = null;
		try {
			state = stateRepository.findById(id).get();
		} catch (Exception e) {
			System.out.println("No state found");
		}
		return state;
	}

	@Override
	public State updateState(State newState, Long id) {
		State state = stateRepository.findById(id).map(st -> {
			st.setCode(newState.getCode());
			st.setName(newState.getName());
			return stateRepository.save(st);
		}).orElseGet(() -> {
			newState.setId(id);
			return stateRepository.save(newState);
		});
		return state;
	}

	@Override
	public BranchDTO getBranch(Long id) {
		BranchDTO dto = null;
		try{
			dto = branchRepository.findBranch(id);
		}catch(Exception e){
			System.out.println("No branch found");
		}
		return dto;
	}

	@Override
	public List<GradeDTO> allGrades() {
		List<GradeDTO> dtos = new ArrayList<>();
		try{
			List<Grade> grades = gradeRepository.findAll();
			for(Grade grade : grades){
				GradeDTO dto = new GradeDTO(grade);
				dtos.add(dto);
			}
		}catch(Exception e){
			System.out.println("No state found");
		}
		return dtos;
	}

	@Override
	public List<SimpleBasketDTO> loadGrade() {
		List<Object[]> objects = new ArrayList<>();
		try{
			objects = gradeRepository.loadGrade();
		}catch(Exception e){
			System.out.println("No state found");
		}
		List<SimpleBasketDTO> dtos = new ArrayList<>();
		for(Object[] object : objects){
			SimpleBasketDTO dto = new SimpleBasketDTO(object);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public Grade getGrade(Long id) {
		Grade grade = null;
		try {
			grade = gradeRepository.findById(id).get();
		} catch (Exception e) {
			System.out.println("No grade found");
		}
		return grade;	
	}

	@Override
	public Grade addGrade(Grade grade) {
		Grade gr = gradeRepository.save(grade);
		return gr;
	}

	@Override
	public Grade updateGrade(Grade newGrade, Long id) {
		Grade grade = gradeRepository.findById(id).map(gr -> {
			gr.setCode(newGrade.getCode());
			gr.setName(newGrade.getName());
			return gradeRepository.save(gr);
		}).orElseGet(() -> {
			newGrade.setId(id);
			return gradeRepository.save(newGrade);
		});
		return grade;
	}

	@Override
	public void deleteGrade(Long id) {
		try {
			gradeRepository.deleteById(id);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Nothing to delete");
		}
	}

}
