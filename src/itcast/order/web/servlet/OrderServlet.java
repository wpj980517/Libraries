package itcast.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import itcast.cart.domain.Cart;
import itcast.cart.domain.CartItem;
import itcast.order.domain.Order;
import itcast.order.domain.OrderItem;
import itcast.order.service.OrderException;
import itcast.order.service.OrderService;
import itcast.user.domain.User;

public class OrderServlet extends BaseServlet{
	private OrderService orderservice=new OrderService();

	//将购买东西添加到订单里面
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//将东西添加到订单，然后添加到数据库中
		
		//通过车来获取订单信息
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		//创建order
		Order order=new Order();
		
		//添加属性
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(1);//1表示还没付款状态
		order.setTotal(cart.getTotals());
		User user=(User) request.getSession().getAttribute("session_user");
		order.setUser(user);
		
		//添加orderitems进order中 需要与cartitem对应上
		List<OrderItem> list=new ArrayList<OrderItem>();
		for(CartItem item:cart.getCartItems())
		{
			OrderItem oi=new OrderItem();
			oi.setIid(CommonUtils.uuid());
			oi.setBook(item.getBook());
			oi.setCount(item.getCount());
			oi.setSubtotal(item.getTotal());
			oi.setOrder(order);
			list.add(oi);
		}
		//添加到order上
		order.setOrderlist(list);
		//清空购物车
		cart.clear();
		//调用service方法
		orderservice.add(order);
		
		//添加到request域中
		request.setAttribute("order", order);
		//返回
		return "f:/jsps/order/desc.jsp";

	}
	//获取出用户的所有订单
	public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//先获取user
		User user=(User) request.getSession().getAttribute("session_user");
		//调用service方法 得到list<order>
		List<Order> orderlist =orderservice.myOrders(user.getUid());
		//封装到request域中
		request.setAttribute("orderalllist", orderlist);
		//转发出去
		return "f:/jsps/order/list.jsp";
	}
	//删除订单
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取删除的订单的订单号
		String oid =request.getParameter("oid");
		//调用方法删除订单
		orderservice.deleteOrder(oid);
		
		//转发回myOrders里面
		return "f:/order?method=myOrders";
	
	}
	//加载订单
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取出oid
		String oid =request.getParameter("oid");
		//调用service方法
		Order order=orderservice.load(oid);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	
	//确认收货
	public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//先提取oid
		String oid=request.getParameter("oid");
		//调用方法
		try {
			orderservice.confirm(oid);
			request.setAttribute("msg", "确认成功");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/order/msg.jsp";
		
	}
	
	//支付
	public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Properties prop=new Properties();
		//导入properties文件
		InputStream input =this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		prop.load(input);
		
		//准备参数
		String p0_Cmd="Buy";
		String p1_MerId=prop.getProperty("p1_MerId");
		String p2_Order=request.getParameter("oid");
		String p3_Amt="0.01";
		String p4_Cur="CNY";
		String p5_Pid="";
		String p6_Pcat="";
		String p7_Pdesc="";
		String p8_Url=prop.getProperty("p8_Url");
		String pd_FrpId=request.getParameter("pd_FrpId");
		String p9_SAF="";
		String pa_MP="";
		String pr_NeedResponse="1";
		
		

		
		//计算出hmc
		String keyValue=prop.getProperty("keyValue");
		String hmc=PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF,
				pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		//将其连接到一起
		StringBuilder url=new StringBuilder(prop.getProperty("url"));
		url.append("p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmc=").append(hmc);
		System.out.println(url);
		//重定向到网址
		//response.sendRedirect(url.toString());
		
		
		//这里就不需要重定向了，因为易宝网关已经不能够使用了，这里直接转到确认支付的servlet来更改状态就好了
		
		//但这里需要传入一些参数进去，尽可能模拟出支付的功能
		request.setAttribute("oid", p2_Order);
		
		return "f:/order?method=back";
		
	}
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//先获取订单号
		String oid=(String) request.getAttribute("oid");
		//获取订单号修改状态
		orderservice.pay(oid);
		//转发回jsp中
		request.setAttribute("msg", "支付成功，等待发货");
		return "f:/jsps/order/msg.jsp";	
		
	}
	
	
}
