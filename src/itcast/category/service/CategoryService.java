package itcast.category.service;

import java.util.List;

import itcast.book.domain.Book;
import itcast.category.dao.CategoryDao;
import itcast.category.domain.Category;


public class CategoryService {
	private CategoryDao categorydao=new CategoryDao();

	//查找全部
	public List<Category> findAll() {
		
		return categorydao.findAll();
	}

	
	//添加分类
	public void addCategory(String cname) throws categoryException {
		Category c=categorydao.findByName(cname);
		if(c!=null)throw new categoryException("该类已存在");
		categorydao.add(cname);
	}


	public void deleteCategory(String cid) throws categoryException {
		//这里只删除目录下没有图书的分类
		List<Book> list=categorydao.getBooks(cid);
		if(!list.isEmpty()) throw new categoryException("该目录下有书籍不能删除");
		categorydao.deleteCategory(cid);
	}


	public Category findById(String cname) {
		
		return categorydao.findByName(cname);
	}


	public void edit(String cname,String cid) throws categoryException {
		Category category =categorydao.findByName(cname);
		if(category!=null)throw new categoryException("该用户名已存在");
		categorydao.edit(cname,cid);
	}
	
	

	
	
}
