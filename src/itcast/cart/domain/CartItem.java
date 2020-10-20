package itcast.cart.domain;

import java.math.BigDecimal;

import itcast.book.domain.Book;

public class CartItem {
	//设置商品条目
	private Book book;//商品
	private int count;//数量
	
	//提供小计提取方法
	public double getTotal() {
		//这里可能会出现计算钱的时候不精确 使用BigDecimal来将其精确化
		BigDecimal d1=new BigDecimal(book.getPrice()+"");
		BigDecimal d2=new BigDecimal(count+"");
		return d1.multiply(d2).doubleValue();
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "CartItem [book=" + book + ", count=" + count + "]";
	}

	
	
}
