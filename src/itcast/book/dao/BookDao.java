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
			//在查找书籍这里需要提出掉已经被删除的书籍
			String sql="select * from book where del=false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException("查询图书错误");
		}
		
		
	}

	public List<Book> findById(String cid) {
		
		try {
			String sql="select * from book where cid=? and del=false";
			return  qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException("分类查询失败");
		}
	}

	public Book findEachBook(String bid) {
		
		try {
			String sql="select * from book where bid=? and del=false";
			Map<String,Object> map=qr.query(sql, new MapHandler(), bid);
			//封装成book以及category两个对象
			Book book =CommonUtils.toBean(map, Book.class);
			Category c=CommonUtils.toBean(map, Category.class);
			//将category传入给book
			book.setCategory(c);
			return book;
			
		} catch (SQLException e) {
			throw new RuntimeException("查询书籍失败");
		}
	}

	public void add(Book book) {
		//添加图书
		try {
		String sql="insert into book values(?,?,?,?,?,?,?)";
		Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),false};
		
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("添加图书失败");
		}
		

	}

	public void del(String bid) {
		String sql="update book set del=true where bid=?";
		try {
			qr.update(sql, bid);
		} catch (SQLException e) {
			throw new RuntimeException("删除失败");
		}
	}

	public void mod(Book book) {
		String sql="update book set  bid=?,bname=?,price=?,author=?,image=?,cid=? where bid=?";
		Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),book.getBid()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("修改失败");
		}
	}
} 
