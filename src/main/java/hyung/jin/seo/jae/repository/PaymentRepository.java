package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyung.jin.seo.jae.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{  
	
	@Query(value = "SELECT p.id, p.amount, p.total, p.method, p.info, p.registerDate, p.invoiceId FROM Payment p WHERE p.invoiceId = ?1 ORDER BY p.id DESC", nativeQuery = true)
	List<Object[]> findByInvoiceId(Long invoiceId);

	@Query(value = "SELECT p.invoiceId FROM Payment p WHERE p.id = ?1", nativeQuery = true)
	Long findInvoiceIdById(Long id);
}
