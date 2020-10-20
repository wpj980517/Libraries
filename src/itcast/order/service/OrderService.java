package itcast.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import itcast.order.dao.OrderDao;
import itcast.order.domain.Order;

public class OrderService {
	private OrderDao orderdao=new OrderDao();
	
	public void add(Order order) {
		//使用事务来对order进行添加 因为需要把order和orderitem都添加上，如果一个添加不成功就回滚事务，保护数据库
		try {
			JdbcUtils.beginTransaction();//开启事务
				orderdao.addOrder(order);
				orderdao.addOrderItem(order.getOrderlist());
			JdbcUtils.commitTransaction();
			
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException("出错了 但事务已经回滚 保护了数据库");
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
		//先获取状态看是否数据库中被修改，如果没被修改则抛异常
		int state=orderdao.getState(oid);
		if(state!=3)throw new OrderException("您无权限确认");
		
		orderdao.updateState(oid, 4);
		
		
	}

	public void pay(String oid) {
		//修改订单状态
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
