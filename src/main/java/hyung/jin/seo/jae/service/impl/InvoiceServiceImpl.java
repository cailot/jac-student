package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyung.jin.seo.jae.dto.InvoiceDTO;
import hyung.jin.seo.jae.model.Invoice;
import hyung.jin.seo.jae.repository.InvoiceRepository;
import hyung.jin.seo.jae.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	private InvoiceRepository invoiceRepository;
   
	@Override
	public long checkCount() {
		long count = invoiceRepository.count();
		return count;
	}

	@Override
	public List<InvoiceDTO> allInvoices() {
		List<Invoice> invoices = new ArrayList<>();
		try{
			invoices = invoiceRepository.findAll();
		}catch(Exception e){
			System.out.println("No invoice found");
		}
		// invoiceRepository.findAll();
		List<InvoiceDTO> dtos = new ArrayList<>();
		for(Invoice invoice: invoices){
			InvoiceDTO dto = new InvoiceDTO(invoice);
			dtos.add(dto);
		}
		return dtos;
	}

	// add Invoice
	@Override
	@Transactional
	public Invoice addInvoice(Invoice invoice) {
		Invoice invo = invoiceRepository.save(invoice);
		//InvoiceDTO dto = new InvoiceDTO(invo);
		return invo;
	}

	// find Invoice by id
	// @Override
	// public InvoiceDTO findInvoiceById(Long id) {
	// 	Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Enrolment not found"));
	// 	InvoiceDTO dto = new InvoiceDTO(invoice);
	// 	return dto;
	// }

	@Override
	@Transactional
	public InvoiceDTO updateInvoice(Invoice invoice, Long id) {
		// search by getId
		Invoice existing = invoiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Enrolment not found"));
		// Update info
		// credit
		if(invoice.getCredit()!=existing.getCredit()){
			existing.setCredit(invoice.getCredit());
		}
		// discount
		if(invoice.getDiscount()!=existing.getDiscount()){
			existing.setDiscount(invoice.getDiscount());
		}
		// paidAmount
		if(invoice.getPaidAmount()!=existing.getPaidAmount()){
			existing.setPaidAmount(invoice.getPaidAmount());
		}
		// amount
		if(invoice.getAmount()!=existing.getAmount()){
			existing.setAmount(invoice.getAmount());
		}
		// update the existing record
		Invoice updated = invoiceRepository.save(existing);
		InvoiceDTO dto = new InvoiceDTO(updated);
		return dto;
	}

	@Override
	public InvoiceDTO getInvoiceDTOByStudentId(Long studentId) {
		InvoiceDTO dto = null;
		try{
			dto = invoiceRepository.findInvoiceDTOByStudentId(studentId);
		}catch(Exception e){
			System.out.println("No invoice found");
		}
		// return invoiceRepository.findInvoiceDTOByStudentId(studentId);
		return dto;
	}

	@Override
	public Long getInvoiceIdByStudentId(Long studentId) {
		Long id = null;
		try{
			id = invoiceRepository.findLatestInvoiceIdByStudentId(studentId);
		}catch(Exception e){
			System.out.println("No invoice found");
		}
		return id;
		// return invoiceRepository.findLatestInvoiceIdByStudentId(studentId);
	}

	@Override
	public Invoice getInvoice(Long id) {
		return invoiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Enrolment not found"));
	}

	@Override
	public Invoice getLastActiveInvoiceByStudentId(Long studentId) {
		return invoiceRepository.findInvoiceByStudentId(studentId);
	}

	@Override
	public double getInvoiceOwingAmount(Long id) {
		double amount = 0;
		try{
			amount = invoiceRepository.getInvoiceOwingAmount(id);
		}catch(Exception e){
			System.out.println("No invoice found");
		}
		return amount;
	}

	@Override
	public double getInvoiceTotalAmount(Long id) {
		double amount = 0;
		try{
			amount = invoiceRepository.getInvoiceTotalAmount(id);
		}catch(Exception e){
			System.out.println("No invoice found");
		}
		return amount;
	}

	@Override
	public Invoice getLastInvoiceByStudentId(Long studentId) {
		return invoiceRepository.findLastInvoiceByStudentId(studentId);
	}

	@Override
	public boolean isPaidInvoice(Long id) {
		double balance =0;
		try{
			balance = invoiceRepository.isPaidInvoice(id);
		}catch(Exception e){
			System.out.println("No invoice found");
		}
		return (balance <= 0) ? true : false;
	}
}
