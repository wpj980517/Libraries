package itcast.order.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import itcast.order.domain.Order;
import itcast.order.service.OrderService;

public class adminOrderServlet extends BaseServlet {
	private OrderService orderservice =new OrderService();
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> list= orderservice.findAll();

		request.setAttribute("orderlist", list);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	public String send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid=request.getParameter("oid");
		orderservice.send(oid);
		
		return "f:/adminorder?method=findAll";
	}
	
}
