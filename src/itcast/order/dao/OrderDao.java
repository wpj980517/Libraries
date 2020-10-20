package itcast.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import itcast.book.domain.Book;
import itcast.category.domain.Category;
import itcast.order.domain.Order;
import itcast.order.domain.OrderItem;

public class OrderDao {
	
	private QueryRunner qr=new TxQueryRunner();
	
	//插入订单
	public void addOrder(Order order)
	{
		try {
			String sql="insert into orders values(?,?,?,?,?,?)";
		
			//需要将utils的日期转换成sql的日期
			Timestamp timestamp=new Timestamp(order.getOrdertime().getTime());
			//添加参数
			Object[] params={order.getOid(),timestamp,order.getTotal(),order.getState()
				,order.getUser().getUid(),order.getAddress()};
			//插入
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("插入失败");
		}
		
	}
	
	//插入订单条目
	public void addOrderItem(List<OrderItem> items) {
		try {
			String sql="insert into orderitem values(?,?,?,?,?)";
		
			//我们需要批处理对items进行全部插入到数据库中
			Object[][] params=new Object[items.size()][];//先创建二维数组，其第一维为list的长度，第二维为每个list里面内容的长度
			//将items插入到params中
			for(int i=0;i<items.size();i++)
			{
				OrderItem oi=items.get(i);
				//插入
				params[i]=new Object[]{oi.getIid(),oi.getCount(),oi.getSubtotal()
					,oi.getOrder().getOid(),oi.getBook().getBid()};
			}
			//调用批处理方法
			qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("添加条目失败");
		}
	
	}
	
/*-------------------------------------------------------------------------*/	

	//获取所有订单
	public List<Order> findById(String uid) {
		//获取所有订单
		String sql="select * from orders where uid=?";
		try {
			//查出该用户所有订单
			List<Order> orderlist=qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			for(Order order:orderlist)
			{
				//添加orderItem
				addItem(order);
			}
			return orderlist;
			
		} catch (SQLException e) {
			throw new RuntimeException("查询uid失败");
		}
		
		
	}
	private void addItem(Order order) throws SQLException {
		//提取出order中对应的orderItem
		String sql="select * from orderitem o inner join book b on o.bid=b.bid where oid=?";	
		//使用List<Map>来进行查询 将book以及orderitem的内容都拿出来分别放入book和orderitem对象中，然后将两者关联
		List<Map<String,Object>> maplist=qr.query(sql, new MapListHandler(), order.getOid());
		//遍历list并将其存放到List<orderItem>中
		List<OrderItem> orderlist=toOrderItemList(maplist);
		order.setOrderlist(orderlist);
	}

	private List<OrderItem> toOrderItemList(List<Map<String, Object>> maplist) {
		//创建book对象，创建orderitem对象，遍历list将book对象放到orderitem对象中
		//然后将其放入到List<OrderItem>对象当中，然后返回list
		List<OrderItem> list=new ArrayList<OrderItem>();
		for(Map<String,Object> map:maplist) {
			//调用方法封装到orderItem中
			OrderItem orderitem=toOrderItem(map);
			list.add(orderitem);//添加到list中
		}
		return list;
		
	}

	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderitem=CommonUtils.toBean(map, OrderItem.class);
		Category c=CommonUtils.toBean(map, Category.class);
		Book book =CommonUtils.toBean(map, Book.class);
		book.setCategory(c);
		orderitem.setBook(book);
				
		return orderitem;
	}
	
	
/*-------------------------------------------------------------------------*/	

	public void deleteOrder(String oid) {
		try {
			//需要先删除orderitem里面的东西
			String sql="delete from orderitem where oid=?";
			qr.update(sql, oid);
			//删除orders的对应东西
			String sql2="delete from orders where oid=?";
			qr.update(sql2, oid);
			
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		
		
	}
/*-------------------------------------------------------------------------*/	
	public Order load(String oid) {
		//加载一个订单
		//这里和findById方法很像，可以获取其进行修改
		
		try {
			String sql="select * from orders where oid=?";
			//查出该用户所有订单
			Order order=qr.query(sql, new BeanHandler<Order>(Order.class), oid);		
				//添加orderItem
				addItem(order);

			return order;
			
		} catch (SQLException e) {
			throw new RuntimeException("查询uid失败");
		}
		
		
	}
/*-------------------------------------------------------------------------*/
	public void updateState(String oid,int state) {
		
		 try {
			 String sql="update orders set state=? where oid=?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException("修改订单状态失败");
		}
	}
	
	//按照订单查找状态
	public int getState(String oid)
	{
		try {
			String sql="select state from orders where oid=?";
			return (Integer)qr.query(sql, new ScalarHandler(), oid);
		} catch (SQLException e) {
			throw new RuntimeException("获取状态失败");
		}
	}

	//查找所有用户的所有订单
	public List<Order> finAll() {
		try {
			String sql="select * from orders";
			List<Order> orderlist=qr.query(sql, new BeanListHandler<Order>(Order.class));
			for(int i=0;i<orderlist.size();i++)
			{
				addItem(orderlist.get(i));	
			}
			
			return orderlist;
			
		} catch (SQLException e) {
			throw new RuntimeException("查询失败");
		}
	}

	
	
}
