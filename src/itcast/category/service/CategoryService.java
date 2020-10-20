package itcast.category.service;

import java.util.List;

import itcast.book.domain.Book;
import itcast.category.dao.CategoryDao;
import itcast.category.domain.Category;


public class CategoryService {
	private CategoryDao categorydao=new CategoryDao();

	//����ȫ��
	public List<Category> findAll() {
		
		return categorydao.findAll();
	}

	
	//��ӷ���
	public void addCategory(String cname) throws categoryException {
		Category c=categorydao.findByName(cname);
		if(c!=null)throw new categoryException("�����Ѵ���");
		categorydao.add(cname);
	}


	public void deleteCategory(String cid) throws categoryException {
		//����ֻɾ��Ŀ¼��û��ͼ��ķ���
		List<Book> list=categorydao.getBooks(cid);
		if(!list.isEmpty()) throw new categoryException("��Ŀ¼�����鼮����ɾ��");
		categorydao.deleteCategory(cid);
	}


	public Category findById(String cname) {
		
		return categorydao.findByName(cname);
	}


	public void edit(String cname,String cid) throws categoryException {
		Category category =categorydao.findByName(cname);
		if(category!=null)throw new categoryException("���û����Ѵ���");
		categorydao.edit(cname,cid);
	}
	
	

	
	
}
