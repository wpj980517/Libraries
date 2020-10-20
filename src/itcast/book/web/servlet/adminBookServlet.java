package itcast.book.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import itcast.book.domain.Book;
import itcast.book.service.BookService;
import itcast.category.domain.Category;
import itcast.category.service.CategoryService;

public class adminBookServlet extends BaseServlet {
	private BookService bookservice=new BookService();
	private CategoryService categoryservice =new CategoryService();
	
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("booklist",bookservice.findAll());
		return "f:/adminjsps/admin/book/list.jsp";
		
	}
	public String eachBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid=request.getParameter("bid");
		Book book=bookservice.findEachBook(bid);
		request.setAttribute("book", book);
		//这里还需要提取目录出来
		List<Category>list =categoryservice.findAll();
		request.setAttribute("categorylist", list);
		
		return "f:/adminjsps/admin/book/desc.jsp";
	}

	//添加图书
	public String addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> list=categoryservice.findAll();
		request.setAttribute("categorylist", list);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	//删除图书
	public String delBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid =request.getParameter("bid");
		bookservice.del(bid);
		
		return "f:/adminbook?method=findAll";
	}
	public String modBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Book book =CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category c=CommonUtils.toBean(request.getParameterMap(), Category.class);
		book.setCategory(c);
		bookservice.mod(book);
		
		
		return "f:/adminbook?method=findAll";
	}
}
