package itcast.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
	private Map<String,CartItem> map=new LinkedHashMap<String,CartItem>();
	
	//���ָ����Ŀ
	public void add(CartItem item) {
		//��Ϊ������Ҫ�ϲ���Ŀ ���������Ҫ���жϾ���Ŀ�ڲ��� �ھ���ȡ����Ŀ������� 
		//���ھ��������Ŀ
		if(map.containsKey(item.getBook().getBid())) {
			
			//�������
			CartItem item2=map.get(item.getBook().getBid());
			item2.setCount(item.getCount()+item2.getCount());//��������
			map.put(item.getBook().getBid(), item2);	
		}else {
			//���������
			map.put(item.getBook().getBid(), item);	
		}
	}
	
	//���
	public void clear() {
		map.clear();
	}
	
	//ɾ��ָ��
	public void delet(String bid) {
		map.remove(bid);
	}
	
	//��ȡ������Ŀ
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	//��ȡ��Ǯ��
	public double getTotals() {
		BigDecimal totals=new BigDecimal("0");
		for(CartItem item:map.values()) {
			totals=totals.add(new BigDecimal(item.getTotal()+""));

		}
		return totals.doubleValue();
	}
	
	
}
