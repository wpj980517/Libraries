package itcast.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import itcast.book.dao.BookDao;
import itcast.book.domain.Book;
import itcast.category.domain.Category;

public class CategoryDao {
	private QueryRunner qr=new TxQueryRunner();

	public List<Category> findAll() {
		String sql="select * from category ";
		
		try {
			return qr.query(sql, new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	public Category findByName(String cname) {
		String sql="select * from category where cname=?";
		try {
			Category category=qr.query(sql, new BeanHandler<Category>(Category.class), cname);
			return category;
		} catch (SQLException e) {
			throw new RuntimeException("²éÑ¯´íÎó");
		}
		
	}
	public Category findById(String cid) {
		String sql="select * from category where cid=?";
		try {
			Category category=qr.query(sql, new BeanHandler<Category>(Category.class), cid);
			return category;
		} catch (SQLException e) {
			throw new RuntimeException("²éÑ¯´íÎó");
		}
	}


	public void add(String cname) {
		try {
		String sql="insert into category values(?,?)";
		List<Category> list=this.findAll();
		Object[] params= {list.size()+1,cname};
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("¸üÐÂÊ§°Ü");
		}
	}

	public void deleteCategory(String cid) {
		try {
			String sql="delete from category where cid=?";
			qr.update(sql, cid);
		} catch (SQLException e) {
			throw new RuntimeException("É¾³ý´íÎó");
		}
	}

	public List<Book> getBooks(String cid) {
		BookDao bookdao=new BookDao();
		List<Book> list=bookdao.findById(cid);
		return list;
		
	}

	public void edit(String cname,String cid) {
		try {
			String sql="update category set cname=? where cid=?";
			Object[] params = {cname,cid};
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("ÐÞ¸ÄÊ§°Ü");
		}
	}

	
	
	
}
