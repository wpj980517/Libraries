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
	
	//���붩��
	public void addOrder(Order order)
	{
		try {
			String sql="insert into orders values(?,?,?,?,?,?)";
		
			//��Ҫ��utils������ת����sql������
			Timestamp timestamp=new Timestamp(order.getOrdertime().getTime());
			//��Ӳ���
			Object[] params={order.getOid(),timestamp,order.getTotal(),order.getState()
				,order.getUser().getUid(),order.getAddress()};
			//����
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("����ʧ��");
		}
		
	}
	
	//���붩����Ŀ
	public void addOrderItem(List<OrderItem> items) {
		try {
			String sql="insert into orderitem values(?,?,?,?,?)";
		
			//������Ҫ�������items����ȫ�����뵽���ݿ���
			Object[][] params=new Object[items.size()][];//�ȴ�����ά���飬���һάΪlist�ĳ��ȣ��ڶ�άΪÿ��list�������ݵĳ���
			//��items���뵽params��
			for(int i=0;i<items.size();i++)
			{
				OrderItem oi=items.get(i);
				//����
				params[i]=new Object[]{oi.getIid(),oi.getCount(),oi.getSubtotal()
					,oi.getOrder().getOid(),oi.getBook().getBid()};
			}
			//������������
			qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("�����Ŀʧ��");
		}
	
	}
	
/*-------------------------------------------------------------------------*/	

	//��ȡ���ж���
	public List<Order> findById(String uid) {
		//��ȡ���ж���
		String sql="select * from orders where uid=?";
		try {
			//������û����ж���
			List<Order> orderlist=qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			for(Order order:orderlist)
			{
				//���orderItem
				addItem(order);
			}
			return orderlist;
			
		} catch (SQLException e) {
			throw new RuntimeException("��ѯuidʧ��");
		}
		
		
	}
	private void addItem(Order order) throws SQLException {
		//��ȡ��order�ж�Ӧ��orderItem
		String sql="select * from orderitem o inner join book b on o.bid=b.bid where oid=?";	
		//ʹ��List<Map>�����в�ѯ ��book�Լ�orderitem�����ݶ��ó����ֱ����book��orderitem�����У�Ȼ�����߹���
		List<Map<String,Object>> maplist=qr.query(sql, new MapListHandler(), order.getOid());
		//����list�������ŵ�List<orderItem>��
		List<OrderItem> orderlist=toOrderItemList(maplist);
		order.setOrderlist(orderlist);
	}

	private List<OrderItem> toOrderItemList(List<Map<String, Object>> maplist) {
		//����book���󣬴���orderitem���󣬱���list��book����ŵ�orderitem������
		//Ȼ������뵽List<OrderItem>�����У�Ȼ�󷵻�list
		List<OrderItem> list=new ArrayList<OrderItem>();
		for(Map<String,Object> map:maplist) {
			//���÷�����װ��orderItem��
			OrderItem orderitem=toOrderItem(map);
			list.add(orderitem);//��ӵ�list��
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
			//��Ҫ��ɾ��orderitem����Ķ���
			String sql="delete from orderitem where oid=?";
			qr.update(sql, oid);
			//ɾ��orders�Ķ�Ӧ����
			String sql2="delete from orders where oid=?";
			qr.update(sql2, oid);
			
			
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		
		
		
	}
/*-------------------------------------------------------------------------*/	
	public Order load(String oid) {
		//����һ������
		//�����findById�������񣬿��Ի�ȡ������޸�
		
		try {
			String sql="select * from orders where oid=?";
			//������û����ж���
			Order order=qr.query(sql, new BeanHandler<Order>(Order.class), oid);		
				//���orderItem
				addItem(order);

			return order;
			
		} catch (SQLException e) {
			throw new RuntimeException("��ѯuidʧ��");
		}
		
		
	}
/*-------------------------------------------------------------------------*/
	public void updateState(String oid,int state) {
		
		 try {
			 String sql="update orders set state=? where oid=?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException("�޸Ķ���״̬ʧ��");
		}
	}
	
	//���ն�������״̬
	public int getState(String oid)
	{
		try {
			String sql="select state from orders where oid=?";
			return (Integer)qr.query(sql, new ScalarHandler(), oid);
		} catch (SQLException e) {
			throw new RuntimeException("��ȡ״̬ʧ��");
		}
	}

	//���������û������ж���
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
			throw new RuntimeException("��ѯʧ��");
		}
	}

	
	
}
