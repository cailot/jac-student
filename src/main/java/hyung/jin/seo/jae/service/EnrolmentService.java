package hyung.jin.seo.jae.service;

import java.util.List;

import hyung.jin.seo.jae.dto.EnrolmentDTO;
import hyung.jin.seo.jae.model.Enrolment;

public interface EnrolmentService {
	// list all enrolments
	List<EnrolmentDTO> allEnrolments();

	// list enrolments by student Id
	List<EnrolmentDTO> findEnrolmentByStudent(Long studentId);

	// list enrolments by invoice Id
	List<EnrolmentDTO> findEnrolmentByInvoice(Long invoiceId);

	// list enrolments by clazz Id
	List<EnrolmentDTO> findEnrolmentByClazz(Long claszzId);

	// list enrolments by clazz Id and student Id
	List<EnrolmentDTO> findEnrolmentByClazzAndStudent(Long clazzId, Long studentId);

	// list enrolments by clazz Id and invoice Id
	List<EnrolmentDTO> findEnrolmentByClazzAndInvoice(Long clazzId, Long invoiceId);

	// list enrolments by active invoice Id and student Id
	List<EnrolmentDTO> findEnrolmentByInvoiceAndStudent(Long invoiceId, Long studentId);

	// list enrolments by invoice Id and student Id
	List<EnrolmentDTO> findAllEnrolmentByInvoiceAndStudent(Long invoiceId, Long studentId);

	// return total count
	long checkCount();

	// add enrolment
	EnrolmentDTO addEnrolment(Enrolment enrolment);

	// deactivate enrolment
	void archiveEnrolment(Long id);

	// update enrolment
	Enrolment updateEnrolment(Enrolment enrolment, Long id);

	// find clazz Id by student Id
	List<Long> findClazzIdByStudentId(Long studentId);

	// find student Id by clazz Id
	List<Long> findStudentIdByClazzId(Long clazzId);

	// find enrolment Id by student Id
	List<Long> findEnrolmentIdByStudentId(Long studentId);

	// find enrolment Id by invoice Id
	List<Long> findEnrolmentIdByInvoiceId(Long invoiceId);

	// find enrolment by id
	Enrolment getEnrolment(Long id);

	// find active enrolment by id
	EnrolmentDTO getActiveEnrolment(Long id);

	// find latest invoice id by student id
	Long findLatestInvoiceIdByStudent(Long studentId);

	// find all invoice id by student id
	List<Long> findInvoiceIdByStudent(Long studentId);

	// get student number by clazz id & week
	Integer getStudentNumberByClazz(Long clazzId, int week);

}