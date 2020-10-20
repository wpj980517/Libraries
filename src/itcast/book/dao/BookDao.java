package itcast.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import itcast.book.domain.Book;
import itcast.category.domain.Category;

public class BookDao {
	private QueryRunner qr=new TxQueryRunner();
	
	public List<Book> findAll(){
		try {
			//�ڲ����鼮������Ҫ������Ѿ���ɾ�����鼮
			String sql="select * from book where del=false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException("��ѯͼ�����");
		}
		
		
	}

	public List<Book> findById(String cid) {
		
		try {
			String sql="select * from book where cid=? and del=false";
			return  qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException("�����ѯʧ��");
		}
	}

	public Book findEachBook(String bid) {
		
		try {
			String sql="select * from book where bid=? and del=false";
			Map<String,Object> map=qr.query(sql, new MapHandler(), bid);
			//��װ��book�Լ�category��������
			Book book =CommonUtils.toBean(map, Book.class);
			Category c=CommonUtils.toBean(map, Category.class);
			//��category�����book
			book.setCategory(c);
			return book;
			
		} catch (SQLException e) {
			throw new RuntimeException("��ѯ�鼮ʧ��");
		}
	}

	public void add(Book book) {
		//���ͼ��
		try {
		String sql="insert into book values(?,?,?,?,?,?,?)";
		Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),false};
		
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("���ͼ��ʧ��");
		}
		

	}

	public void del(String bid) {
		String sql="update book set del=true where bid=?";
		try {
			qr.update(sql, bid);
		} catch (SQLException e) {
			throw new RuntimeException("ɾ��ʧ��");
		}
	}

	public void mod(Book book) {
		String sql="update book set  bid=?,bname=?,price=?,author=?,image=?,cid=? where bid=?";
		Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),book.getBid()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("�޸�ʧ��");
		}
	}
} 
