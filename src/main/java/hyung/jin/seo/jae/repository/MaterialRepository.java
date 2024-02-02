package hyung.jin.seo.jae.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyung.jin.seo.jae.dto.MaterialDTO;
import hyung.jin.seo.jae.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long>{  
	
	List<Material> findAll();
	
	@Query("SELECT new hyung.jin.seo.jae.dto.MaterialDTO(m.id, m.registerDate, m.paymentDate, m.info, m.book.id, m.book.name, m.book.price, m.invoice.id) FROM Material m WHERE m.invoice.id = ?1") 
	List<MaterialDTO> findMaterialByInvoiceId(Long invoiceId);

	@Query("SELECT new hyung.jin.seo.jae.dto.MaterialDTO(m.id, m.registerDate, m.paymentDate, m.info, m.book.id, m.book.name, m.book.price, m.invoice.id) FROM Material m WHERE m.invoice.id = ?1 AND m.book.id = ?2") 
	MaterialDTO findMaterialByInvoiceIdAndBookId(Long invoiceId, Long bookId);

	@Modifying
    @Query("DELETE FROM Material m WHERE m.id = :id")
    void deleteMaterial(@Param("id") Long id);

    @Query("DELETE FROM Material m WHERE m.invoice.id = :invoiceId AND m.book.id = :bookId")
    void deleteMaterialByInvoiceIdAndBookId(@Param("invoiceId") Long invoiceId, @Param("bookId") Long bookId);

	// return Material ids by invoice id
	@Query("SELECT m.id FROM Material m WHERE m.invoice.id = ?1")
	List<Long> findMaterialIdByInvoiceId(long invoiceId);

	// return Book ids by invoice id
	@Query("SELECT m.book.id FROM Material m WHERE m.invoice.id = ?1")
	List<Long> findBookIdByInvoiceId(long invoiceId);

	long count();
}
