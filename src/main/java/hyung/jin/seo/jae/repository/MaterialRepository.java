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

	@Modifying
    @Query("DELETE FROM Material m WHERE m.invoice.id = :invoiceId AND m.book.id = :bookId")
    void deleteMaterialByInvoiceIdAndBookId(@Param("invoiceId") Long invoiceId, @Param("bookId") Long bookId);

	// return Material ids by invoice id
	@Query("SELECT m.id FROM Material m WHERE m.invoice.id = ?1")
	List<Long> findMaterialIdByInvoiceId(long invoiceId);

	// return Book ids by invoice id
	@Query("SELECT m.book.id FROM Material m WHERE m.invoice.id = ?1")
	List<Long> findBookIdByInvoiceId(long invoiceId);

	// return distinct book ids for a student's invoices within the given academic cycle date range.
	// invoice IDs follow the convention: studentId(8 digits) * 1000 + sequence(3 digits).
	// minInvoiceId/maxInvoiceId are pre-computed in Java: studentId * 1000 and (studentId+1) * 1000.
	// cycleStartDate/cycleEndDate must be yyyy-MM-dd strings (from CycleDTO.getStartDate/getEndDate).
	@Query(value = "SELECT DISTINCT m.bookId FROM Material m " +
			"JOIN Invoice i ON i.id = m.invoiceId " +
			"WHERE i.id >= :minInvoiceId " +
			"AND i.id < :maxInvoiceId " +
			"AND i.registerDate >= :cycleStartDate " +
			"AND i.registerDate <= :cycleEndDate", nativeQuery = true)
	List<Long> findBookIdsByStudentInvoice(
			@Param("minInvoiceId") long minInvoiceId,
			@Param("maxInvoiceId") long maxInvoiceId,
			@Param("cycleStartDate") String cycleStartDate,
			@Param("cycleEndDate") String cycleEndDate);

	long count();
}
