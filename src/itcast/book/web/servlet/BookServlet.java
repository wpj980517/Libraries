package itcast.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import itcast.book.service.BookService;

public class BookServlet extends BaseServlet{
	private BookService bookservice =new BookService();

	//查询所有书籍
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("BookList", bookservice.findAll());
		return "f:/jsps/book/list.jsp";
	}
	//查询对应分类的书籍
	public String findById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		
		request.setAttribute("BookList", bookservice.findById(cid));
		
		return "f:/jsps/book/list.jsp";
	}
	public String findEachBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid=request.getParameter("bid");
		request.setAttribute("Book", bookservice.findEachBook(bid));
		
		return "f:/jsps/book/desc.jsp";
	}
}
