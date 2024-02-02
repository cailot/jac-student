package hyung.jin.seo.jae.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hyung.jin.seo.jae.dto.BookDTO;
import hyung.jin.seo.jae.model.Book;
import hyung.jin.seo.jae.service.BookService;

@Controller
@RequestMapping("book")
public class JaeBookController {

	//private static final Logger LOG = LoggerFactory.getLogger(JaeBookController.class);

	@Autowired
	private BookService bookService;

	// search all course books
	@GetMapping("/list")
	@ResponseBody
	List<BookDTO> listBooks() {
		List<BookDTO> dtos = bookService.allBooks();
		return dtos;
	}	
	
	// search course books by grade
	@GetMapping("/listGrade")
	@ResponseBody
	List<BookDTO> listGradeBook(@RequestParam("grade") String grade) {
		// int year = JaeUtils.academicYear();
		List<BookDTO> dtos = bookService.booksByGrade(grade);	
		return dtos;
	}

	// get book by Id
	@GetMapping("/get/{id}")
	@ResponseBody
	Book getBook(@PathVariable("id") Long id) {
		Book book = bookService.getBook(id);	
		return book;
	}

	// count records number in database
	@GetMapping("/count")
	@ResponseBody
	long coutFees() {
		long count = bookService.checkCount();
		return count;
	}
}
