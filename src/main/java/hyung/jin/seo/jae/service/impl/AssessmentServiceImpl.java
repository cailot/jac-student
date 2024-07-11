package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.AssessmentAnswerDTO;
import hyung.jin.seo.jae.dto.AssessmentDTO;
import hyung.jin.seo.jae.model.Assessment;
import hyung.jin.seo.jae.model.AssessmentAnswer;
import hyung.jin.seo.jae.repository.AssessmentAnswerRepository;
import hyung.jin.seo.jae.repository.AssessmentRepository;
import hyung.jin.seo.jae.service.AssessmentService;

@Service
public class AssessmentServiceImpl implements AssessmentService {
	
	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private AssessmentAnswerRepository assessmentAnswerRepository;

	@Override
	public List<Assessment> allAssessments() {
		List<Assessment> dtos = new ArrayList<>();
		try{
			dtos = assessmentRepository.findAll();
		}catch(Exception e){
			System.out.println("No Assessment found");
		}
		return dtos;
	}

	@Override
	public Assessment getAssessment(Long id) {
		Optional<Assessment> work = assessmentRepository.findById(id);
		if(!work.isPresent()) return null;
		return work.get();
	}

	@Transactional
	@Override
	public Assessment addAssessment(Assessment work) {
		Assessment assessment = assessmentRepository.save(work);
		return assessment;
	}

	@Transactional
	@Override
	public Assessment updateAssessment(Assessment newWork, Long id) {
		Assessment existing = assessmentRepository.findById(id).get();
        // Update info
        String newPdfPath = StringUtils.defaultString(newWork.getPdfPath());
        existing.setPdfPath(newPdfPath);
        boolean newActive = newWork.isActive();
		existing.setActive(newActive);
        Assessment updated = assessmentRepository.save(existing);
		return updated;
	}

	@Transactional
	@Override
	public void deleteAssessment(Long id) {
		try{
			// 1. get associated TestAnswer
			AssessmentAnswer aa = assessmentAnswerRepository.findAssessmentAnswerByAssessment(id);
			if(aa!=null){
				// 2. empty TestAnswerCollection
				aa.setAnswers(new ArrayList<>());
				assessmentAnswerRepository.save(aa);
				// 3. delete associated assessmentAnswer
				assessmentAnswerRepository.deleteAssessmentAnswerByAssessment(id);
			}
			// 4. delete assessment
			assessmentRepository.deleteById(id);
		}catch(Exception e){
			System.out.println("Nothing to delete");
		}
	}

	@Override
	public AssessmentDTO getAssessmentInfo(String grade, long subjectId) {
		AssessmentDTO dto = null;
		try{
			dto = assessmentRepository.findAssessment(grade, subjectId);
		}catch(Exception e){
			System.out.println("No Assessment found");
		}
		return dto;
	}

	@Override
	public List<AssessmentDTO> listAssessment(String grade) {
		List<AssessmentDTO> dtos = new ArrayList<>();
		try{
			dtos = assessmentRepository.findAssessmentByGrade(grade);
		}catch(Exception e){
			System.out.println("No Assessment found");
		}
		return dtos;
	}

	@Override
	public List<AssessmentDTO> listAssessment(long subjectId) {
		List<AssessmentDTO> dtos = new ArrayList<>();
		try{
			dtos = assessmentRepository.findAssessmentBySubject(subjectId);
		}catch(Exception e){
			System.out.println("No Assessment found");
		}
		return dtos;
	}

	@Override
	public List<AssessmentDTO> listAssessment(String grade, long subjectId) {
		List<AssessmentDTO> dtos = new ArrayList<>();
		try{
			dtos = assessmentRepository.findAssessmentByGradeNSubject(grade, subjectId);
		}catch(Exception e){
			System.out.println("No Assessment found");
		}
		return dtos;
	}

	@Override
	public AssessmentAnswer findAssessmentAnswer(Long assessmentId) {
		AssessmentAnswer answer = null;
		try{
			answer = assessmentAnswerRepository.findAssessmentAnswerByAssessment(assessmentId);
		}catch(Exception e){
			System.out.println("No AssessmentAnswer found");
		}
		return answer;
	}

	@Override
	public AssessmentAnswerDTO getAssessmentAnswer(Long assessmentId) {
		AssessmentAnswerDTO dto = null;
		try{
			AssessmentAnswer answer = assessmentAnswerRepository.findAssessmentAnswerByAssessment(assessmentId);
			if(answer!=null){
				dto = new AssessmentAnswerDTO(answer);
			}
		}catch(Exception e){
			System.out.println("No AssessmentAnswer found");
		}
		return dto;
	}

	@Transactional
	@Override
	public AssessmentAnswer addAssessmentAnswer(AssessmentAnswer answer) {
		AssessmentAnswer aa = assessmentAnswerRepository.save(answer);
		return aa;
	}

	@Transactional
	@Override
	public AssessmentAnswer updateAssessmentAnswer(AssessmentAnswer newWork, Long id) {
		// get by id
		AssessmentAnswer existing = assessmentAnswerRepository.findById(id).get();
		// update info
		List newAns = newWork.getAnswers();
		existing.setAnswers(newAns);
		// update the existing record
		AssessmentAnswer updated = assessmentAnswerRepository.save(existing);
		// return updated record
		return updated;
	}

	@Override
	public List<Integer> getStudentAssessmentAnswer(Long studentId, Long assessId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getStudentAssessmentAnswer'");
	}



}
