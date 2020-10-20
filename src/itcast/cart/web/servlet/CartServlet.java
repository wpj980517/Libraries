package itcast.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import itcast.book.domain.Book;
import itcast.book.service.BookService;
import itcast.cart.domain.Cart;
import itcast.cart.domain.CartItem;

public class CartServlet extends BaseServlet {

	
	//添加书籍到购物车
	public String addcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		//先从session中得到车
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		
		//判断是否为空
		if(cart==null) {
			response.getWriter().print("请先登录");
			return null;
		}
		
		
		//得到书籍id
		String bid=request.getParameter("bid");

		//得到书的对象
		Book book=new BookService().findEachBook(bid);
		//得到数量
		int count=Integer.parseInt(request.getParameter("count"));
		//封装到cartitem里面
		CartItem item=new CartItem();
		item.setBook(book);
		item.setCount(count);
		//封装到cart里面
		cart.add(item);
		
		//转发出去
		return "r:/jsps/cart/list.jsp";
		
	}
	//清空车
	public String clearcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "r:/jsps/cart/list.jsp";
	}
	//删除指定项
	public String deletcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		String bid=request.getParameter("bid");
		cart.delet(bid);
		return "r:/jsps/cart/list.jsp";
	
	}	
		
}
