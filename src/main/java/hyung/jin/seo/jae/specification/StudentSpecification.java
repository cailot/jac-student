package hyung.jin.seo.jae.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import hyung.jin.seo.jae.model.Student;

public interface StudentSpecification {
	
	// Id
	static Specification<Student> idEquals(String keyword){
		return (root, query, cb) -> cb.equal(root.get("id"), Integer.parseInt(keyword));
	}
		
	// first name
	static Specification<Student> firstNameContains(String keyword){
		return (root, query, cb) -> cb.like(root.get("firstName"), "%" + keyword + "%");
	}
	
	// last name
	static Specification<Student> lastNameContains(String keyword){
		return (root, query, cb) -> cb.like(root.get("lastName"), "%" + keyword + "%");
	}

	// name
	static Specification<Student> nameContains(String keyword) {
	    return (root, query, builder) -> {
	        String keywordLike = "%" + keyword + "%";
	        return builder.or(
	            builder.like(root.get("firstName"), keywordLike),
	            builder.like(root.get("lastName"), keywordLike)
	        );
	    };
	}
	
	// state
	static Specification<Student> stateEquals(String keyword){
		return (root, query, cb) -> cb.equal(root.get("state"), keyword);
	}
	
	// branch
	static Specification<Student> branchEquals(String keyword){
		return (root, query, cb) -> cb.equal(root.get("branch"), keyword);
	}
	
	// grade
	static Specification<Student> gradeEquals(String keyword){
		return (root, query, cb) -> cb.equal(root.get("grade"), keyword);
	}
	
	// year
	static Specification<Student> yearRange(String year){
		LocalDate startDate = LocalDate.of(Integer.parseInt(year), 1, 1);
		LocalDate endDate = LocalDate.of(Integer.parseInt(year), 12, 31);
		return new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Expression<LocalDate> dateField = root.get("registerDate");
				return criteriaBuilder.between(dateField, startDate, endDate);
			}	
		};
	}
	
	// start date equal
	static Specification<Student> startDateEquals(LocalDate date) {
		  return (root, query, cb) -> cb.equal(root.get("registerDate"), date);
	}

	// start date before
	static Specification<Student> startDateLessThanOrEqualTo(LocalDate date) {
	    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("registerDate"), date);
	}
	
	// start date after
	static Specification<Student> startDateAfter(LocalDate date) {
	    return (root, query, cb) -> cb.greaterThan(root.get("registerDate"), date);
	}

	// is not null 
	static Specification<Student> hasNotNullVaule(String column){
		return (root, query, cb) -> cb.isNotNull(root.get(column));
	}
	
	// is null
	static Specification<Student> hasNullVaule(String column){
		return (root, query, cb) -> cb.isNull(root.get(column));
	}
	
}
