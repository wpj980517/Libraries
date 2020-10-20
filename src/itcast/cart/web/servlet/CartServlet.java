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

	
	//����鼮�����ﳵ
	public String addcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		//�ȴ�session�еõ���
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		
		//�ж��Ƿ�Ϊ��
		if(cart==null) {
			response.getWriter().print("���ȵ�¼");
			return null;
		}
		
		
		//�õ��鼮id
		String bid=request.getParameter("bid");

		//�õ���Ķ���
		Book book=new BookService().findEachBook(bid);
		//�õ�����
		int count=Integer.parseInt(request.getParameter("count"));
		//��װ��cartitem����
		CartItem item=new CartItem();
		item.setBook(book);
		item.setCount(count);
		//��װ��cart����
		cart.add(item);
		
		//ת����ȥ
		return "r:/jsps/cart/list.jsp";
		
	}
	//��ճ�
	public String clearcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "r:/jsps/cart/list.jsp";
	}
	//ɾ��ָ����
	public String deletcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		String bid=request.getParameter("bid");
		cart.delet(bid);
		return "r:/jsps/cart/list.jsp";
	
	}	
		
}
