package hyung.jin.seo.jae.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyung.jin.seo.jae.dto.BookDTO;
import hyung.jin.seo.jae.model.Book;
import hyung.jin.seo.jae.repository.BookRepository;
import hyung.jin.seo.jae.repository.SubjectRepository;
import hyung.jin.seo.jae.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;

	@Override
	public List<BookDTO> allBooks() {
		List<Book> books = new ArrayList<>();
		try{
			books = bookRepository.findAll();
		}catch(Exception e){
			System.out.println("No book found");
		}
		// bookRepository.findAll();
		List<BookDTO> dtos = new ArrayList<>();
		for(Book book: books){
			BookDTO dto = new BookDTO(book);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public Book getBook(Long id){
		Book book = null;
		try{
			book = bookRepository.findById(id).get();
		}catch(Exception e){
			System.out.println("No book found");
		}
		// bookRepository.findById(id).get();
		return book;
	}

	@Override
	public List<BookDTO> booksByGrade(String grade) {
		// 1. get books
		List<Book> books = bookRepository.findByGrade(grade);
		// 2. get subjects
		//List<String> subjects = subjectRepository.findSubjectNamesForGrade(grade);
		List<String> subjects = subjectRepository.findSubjectAbbrForGrade(grade);
		// 3. assign subjects to books
		List<BookDTO> dtos = new ArrayList<BookDTO>();
		for(Book book : books){
			BookDTO dto = new BookDTO(book);
			for(String subject : subjects){
				dto.addSubject(subject);
			}
			dtos.add(dto);
		}
		// 4. add postage for all years
		List<Book> postageBooks = bookRepository.findByGrade("all");
		for(Book postageBook : postageBooks){
			BookDTO dto = new BookDTO(postageBook);
			// put empty string to avoid JSON error
			List<String> temp = Collections.singletonList("");
			dto.setSubjects(temp);
			dtos.add(dto);
		}
		// 5. return DTOs
		return dtos;	
	}

	@Override
	public long checkCount() {
		long count = bookRepository.count();
		return count;
	}

	@Override
	public List<BookDTO> findBookByInvoiceId(Long id) {
		List<Book> books = new ArrayList<>();
		try{
			books = bookRepository.findBookByInvoiceId(id);
		}catch(Exception e){
			System.out.println("No book found");
		}
		// bookRepository.findBookByInvoiceId(id);
		List<BookDTO> dtos = new ArrayList<>();
		for(Book book: books){
			BookDTO dto = new BookDTO(book);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public double getPriceByMaterial(Long materialId) {
		double price = 0;
		try{
			price = bookRepository.getPriceByMaterialId(materialId);
		}catch(Exception e){
			System.out.println("No book found");
		}
		return price;
		// return bookRepository.getPriceByMaterialId(materialId);
	}

	@Override
	public double getPrice(Long bookId) {
		double price = 0;
		try{
			price = bookRepository.getPrice(bookId);
		}catch(Exception e){
			System.out.println("No book found");
		}
		return price;
		// return bookRepository.getPrice(bookId);
	}
	
}
