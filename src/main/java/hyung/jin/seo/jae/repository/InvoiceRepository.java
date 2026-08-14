package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.dto.InvoiceDTO;
import hyung.jin.seo.jae.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{  
	
	// bring latest InvoiceDTO by student id
    // @Query("SELECT new hyung.jin.seo.jae.dto.InvoiceDTO(i.id, i.credit, i.discount, i.paidAmount, i.amount, i.registerDate, i.paymentDate, i.info) FROM Invoice i WHERE i.id = (SELECT MAX(en.invoice.id) FROM Enrolment en WHERE en.student.id = ?1 AND en.old = false)")
    @Query("SELECT new hyung.jin.seo.jae.dto.InvoiceDTO(i.id, i.credit, i.discount, i.paidAmount, i.amount, i.registerDate, i.paymentDate, i.info) FROM Invoice i WHERE i.id = (SELECT MAX(en.invoice.id) FROM Enrolment en WHERE en.student.id = ?1)")
	InvoiceDTO findInvoiceDTOByStudentId(long studentId);

	// bring latest active Invoice by student id
	@Query("SELECT i FROM Invoice i WHERE i.id = (SELECT MAX(en.invoice.id) FROM Enrolment en WHERE en.student.id = ?1 AND en.old = false)")
	Invoice findInvoiceByStudentId(long studentId);


	// bring latest Invoice by student id
	@Query("SELECT i FROM Invoice i WHERE i.id = (SELECT MAX(en.invoice.id) FROM Enrolment en WHERE en.student.id = ?1)")
	Invoice findLastInvoiceByStudentId(long studentId);

	// return invoice id by student id
	@Query("SELECT e.invoice.id FROM Enrolment e WHERE e.student.id = ?1 and e.old = false order by e.registerDate desc")
	List<Long> findInvoiceIdByStudentId(long studentId);

	// return latest invoice id by student id
	@Query("SELECT MAX(e.invoice.id) FROM Enrolment e WHERE e.student.id = ?1 and e.old = false order by e.registerDate desc")
	Long findLatestInvoiceIdByStudentId(long studentId);

	// return invoice amount by id
	@Query("SELECT (i.amount - i.paidAmount) FROM Invoice i WHERE i.id = ?1")
	double getInvoiceOwingAmount(long id);

	// return invoice total amount by id
	@Query("SELECT i.amount FROM Invoice i WHERE i.id = ?1")
	double getInvoiceTotalAmount(long id);

	// return invoice balance by id
	@Query("SELECT (i.amount - i.paidAmount) FROM Invoice i WHERE i.id = ?1")
	double isPaidInvoice(long id);

}
