package itcast.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
	private Map<String,CartItem> map=new LinkedHashMap<String,CartItem>();
	
	//添加指定条目
	public void add(CartItem item) {
		//因为我们需要合并条目 因此这里需要先判断旧条目在不在 在就提取旧条目添加数量 
		//不在就添加新条目
		if(map.containsKey(item.getBook().getBid())) {
			
			//如果存在
			CartItem item2=map.get(item.getBook().getBid());
			item2.setCount(item.getCount()+item2.getCount());//更改数量
			map.put(item.getBook().getBid(), item2);	
		}else {
			//如果不存在
			map.put(item.getBook().getBid(), item);	
		}
	}
	
	//清空
	public void clear() {
		map.clear();
	}
	
	//删除指定
	public void delet(String bid) {
		map.remove(bid);
	}
	
	//获取所有条目
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	//获取总钱数
	public double getTotals() {
		BigDecimal totals=new BigDecimal("0");
		for(CartItem item:map.values()) {
			totals=totals.add(new BigDecimal(item.getTotal()+""));

		}
		return totals.doubleValue();
	}
	
	
}
