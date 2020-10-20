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

	//����������ӵ���������
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��������ӵ�������Ȼ����ӵ����ݿ���
		
		//ͨ��������ȡ������Ϣ
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		//����order
		Order order=new Order();
		
		//�������
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(1);//1��ʾ��û����״̬
		order.setTotal(cart.getTotals());
		User user=(User) request.getSession().getAttribute("session_user");
		order.setUser(user);
		
		//���orderitems��order�� ��Ҫ��cartitem��Ӧ��
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
		//��ӵ�order��
		order.setOrderlist(list);
		//��չ��ﳵ
		cart.clear();
		//����service����
		orderservice.add(order);
		
		//��ӵ�request����
		request.setAttribute("order", order);
		//����
		return "f:/jsps/order/desc.jsp";

	}
	//��ȡ���û������ж���
	public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//�Ȼ�ȡuser
		User user=(User) request.getSession().getAttribute("session_user");
		//����service���� �õ�list<order>
		List<Order> orderlist =orderservice.myOrders(user.getUid());
		//��װ��request����
		request.setAttribute("orderalllist", orderlist);
		//ת����ȥ
		return "f:/jsps/order/list.jsp";
	}
	//ɾ������
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡɾ���Ķ����Ķ�����
		String oid =request.getParameter("oid");
		//���÷���ɾ������
		orderservice.deleteOrder(oid);
		
		//ת����myOrders����
		return "f:/order?method=myOrders";
	
	}
	//���ض���
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ��oid
		String oid =request.getParameter("oid");
		//����service����
		Order order=orderservice.load(oid);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	
	//ȷ���ջ�
	public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//����ȡoid
		String oid=request.getParameter("oid");
		//���÷���
		try {
			orderservice.confirm(oid);
			request.setAttribute("msg", "ȷ�ϳɹ�");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/order/msg.jsp";
		
	}
	
	//֧��
	public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Properties prop=new Properties();
		//����properties�ļ�
		InputStream input =this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		prop.load(input);
		
		//׼������
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
		
		

		
		//�����hmc
		String keyValue=prop.getProperty("keyValue");
		String hmc=PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF,
				pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		//�������ӵ�һ��
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
		//�ض�����ַ
		//response.sendRedirect(url.toString());
		
		
		//����Ͳ���Ҫ�ض����ˣ���Ϊ�ױ������Ѿ����ܹ�ʹ���ˣ�����ֱ��ת��ȷ��֧����servlet������״̬�ͺ���
		
		//��������Ҫ����һЩ������ȥ��������ģ���֧���Ĺ���
		request.setAttribute("oid", p2_Order);
		
		return "f:/order?method=back";
		
	}
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//�Ȼ�ȡ������
		String oid=(String) request.getAttribute("oid");
		//��ȡ�������޸�״̬
		orderservice.pay(oid);
		//ת����jsp��
		request.setAttribute("msg", "֧���ɹ����ȴ�����");
		return "f:/jsps/order/msg.jsp";	
		
	}
	
	
}
