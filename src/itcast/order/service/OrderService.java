package itcast.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import itcast.order.dao.OrderDao;
import itcast.order.domain.Order;

public class OrderService {
	private OrderDao orderdao=new OrderDao();
	
	public void add(Order order) {
		//ʹ����������order������� ��Ϊ��Ҫ��order��orderitem������ϣ����һ����Ӳ��ɹ��ͻع����񣬱������ݿ�
		try {
			JdbcUtils.beginTransaction();//��������
				orderdao.addOrder(order);
				orderdao.addOrderItem(order.getOrderlist());
			JdbcUtils.commitTransaction();
			
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException("������ �������Ѿ��ع� ���������ݿ�");
		}
		
		
	}

	public List<Order> myOrders(String uid) {
		
		return orderdao.findById(uid);
	}

	public void deleteOrder(String oid) {
		
		 orderdao.deleteOrder(oid);
	}

	public Order load(String oid) {
		
		return orderdao.load(oid);
	}
	
	public void confirm (String oid)throws OrderException
	{
		//�Ȼ�ȡ״̬���Ƿ����ݿ��б��޸ģ����û���޸������쳣
		int state=orderdao.getState(oid);
		if(state!=3)throw new OrderException("����Ȩ��ȷ��");
		
		orderdao.updateState(oid, 4);
		
		
	}

	public void pay(String oid) {
		//�޸Ķ���״̬
		int state=orderdao.getState(oid);
		if(state==1)
		{
			orderdao.updateState(oid, 2);
		}
		
	}

	public List<Order> findAll() {
		List<Order> list=orderdao.finAll();
		
		return list;
	}

	public void send(String oid) {
		int state=orderdao.getState(oid);
		if(state==2)
		{
			orderdao.updateState(oid, 3);
		}
	}
}
