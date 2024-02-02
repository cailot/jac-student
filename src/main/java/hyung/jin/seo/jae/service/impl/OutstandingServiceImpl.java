package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.OutstandingDTO;
import hyung.jin.seo.jae.model.Outstanding;
import hyung.jin.seo.jae.repository.OutstandingRepository;
import hyung.jin.seo.jae.service.OutstandingService;

@Service
public class OutstandingServiceImpl implements OutstandingService {
	
	@Autowired
	private OutstandingRepository outstandingRepository;
   
	@Override
	public long checkCount() {
		long count = outstandingRepository.count();
		return count;
	}

	@Override
	public Outstanding getOutstanding(Long id) {
		Outstanding stand = outstandingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Outstanding not found"));
		return stand;
	}

	@Override
	public List<OutstandingDTO> getOutstandingtByInvoice(Long invoiceId) {
		List<OutstandingDTO> dtos = new ArrayList<>();
		try{
			dtos = outstandingRepository.findByInvoiceId(invoiceId);
		}catch(Exception e){
			System.out.println("No outstanding found");
		}
		return dtos;
	}

	@Override
	@Transactional
	public Outstanding addOutstanding(Outstanding stand) {
		return outstandingRepository.save(stand);
	}

	@Override
	public List<OutstandingDTO> allOutstandings() {
		List<Outstanding> stands = new ArrayList<>();
		try{
			stands = outstandingRepository.findAll();
		}catch(Exception e){
			System.out.println("No outstanding found");
		}
		// outstandingRepository.findAll();
		List<OutstandingDTO> dtos = new ArrayList<>();
		for(Outstanding stand: stands){
			OutstandingDTO dto = new OutstandingDTO(stand);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional
	public Outstanding updateOutstanding(Outstanding stand, Long id) {
		// search by getId
		Outstanding existing = outstandingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Outstanding not found"));
		// Update info
		// paid
		if(stand.getPaid()!=existing.getPaid()){
			existing.setPaid(stand.getPaid());
		}
		// remaining
		if(stand.getRemaining()!=existing.getRemaining()){
			existing.setRemaining(stand.getRemaining());
		}
		// amount
		if(stand.getAmount()!=existing.getAmount()){
			existing.setAmount(stand.getAmount());
		}
		// info
		if(!StringUtils.equalsIgnoreCase(StringUtils.defaultString(stand.getInfo()), StringUtils.defaultString(existing.getInfo()))){
			existing.setInfo(StringUtils.defaultString(stand.getInfo()));
		}
		// update the existing record
		Outstanding updated = outstandingRepository.save(existing);
		return updated;
	}

}
