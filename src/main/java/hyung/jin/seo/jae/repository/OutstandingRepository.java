package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.dto.OutstandingDTO;
import hyung.jin.seo.jae.model.Outstanding;

public interface OutstandingRepository extends JpaRepository<Outstanding, Long>{  
	
	@Query(value = "SELECT new hyung.jin.seo.jae.dto.OutstandingDTO(o.id, o.paid, o.remaining, o.amount, o.registerDate, o.paymentId, o.invoice.id, o.info) FROM Outstanding o WHERE o.invoice.id = ?1")
	List<OutstandingDTO> findByInvoiceId(Long invoiceId);

}
