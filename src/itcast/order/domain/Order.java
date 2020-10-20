package itcast.order.domain;

import java.util.Date;
import java.util.List;

import itcast.user.domain.User;

public class Order {
	private String oid;
	private Date ordertime;
	private double total;
	private int state;//����״̬ 1δ���� 2�Ѹ���δ���� 3�ѷ���δȷ���տ� 4��ȷ���տ�
	private User user;//�����û�
	private String address;
	private List<OrderItem> orderlist;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<OrderItem> getOrderlist() {
		return orderlist;
	}
	public void setOrderlist(List<OrderItem> orderlist) {
		this.orderlist = orderlist;
	}
	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", state=" + state + ", user="
				+ user + ", address=" + address + ", orderlist=" + orderlist + "]";
	}
	
	
}
