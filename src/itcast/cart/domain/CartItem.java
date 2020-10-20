package itcast.cart.domain;

import java.math.BigDecimal;

import itcast.book.domain.Book;

public class CartItem {
	//������Ʒ��Ŀ
	private Book book;//��Ʒ
	private int count;//����
	
	//�ṩС����ȡ����
	public double getTotal() {
		//������ܻ���ּ���Ǯ��ʱ�򲻾�ȷ ʹ��BigDecimal�����侫ȷ��
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
