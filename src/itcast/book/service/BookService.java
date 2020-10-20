package itcast.book.service;

import java.util.List;

import itcast.book.dao.BookDao;
import itcast.book.domain.Book;

public class BookService {
	private BookDao bookdao =new BookDao();
	
	public  List<Book> findAll(){
		return bookdao.findAll();
	}

	public List<Book> findById(String cid) {
		
		return bookdao.findById(cid);
	}

	public Book findEachBook(String bid) {
		
		return bookdao.findEachBook(bid);
	}

	public  void add(Book book) {
		 bookdao.add(book);
	}

	public void del(String bid) {
		bookdao.del(bid);
	}

	public void mod(Book book) {
		bookdao.mod(book);
	}
}
