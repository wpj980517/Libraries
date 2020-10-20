package itcast.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import itcast.category.dao.CategoryDao;
import itcast.category.domain.Category;
import itcast.category.service.CategoryService;
import itcast.category.service.categoryException;




//����Ա��servlet
public class adminCategoryServlet extends BaseServlet {
	private CategoryService categoryservice=new CategoryService();
	
	//ת����¼ҳ��
	public String toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "r:/adminjsps/login.jsp";
	}
	//�������з���
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> list=categoryservice.findAll();
		
		request.setAttribute("categorylist", list);
		
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//��ӷ���
	public String addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cname=request.getParameter("cname");
		try {
			categoryservice.addCategory(cname);
			request.setAttribute("msg", "��ӳɹ�");
			return "f:/adminjsps/admin/category/add.jsp";
			
		} catch (categoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/admin/category/add.jsp";
		}

	}
	//ɾ������
	public String deleteCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		try {
			categoryservice.deleteCategory(cid);
			return "f:/admincategory?method=findAll";
		} catch (categoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/admincategory?method=findAll";
		}
	}
	//�޸ķ�������������һ����ȡ���󷢸�mod.jsp��Ȼ�����޸�
	public String findById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cname=request.getParameter("cname");
		Category category=categoryservice.findById(cname);
		request.setAttribute("category", category);
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cname=request.getParameter("cname");
		String cid=request.getParameter("cid");
		try {
			categoryservice.edit(cname,cid);
			return "f:/admincategory?method=findAll";
		} catch (categoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/admin/category/mod.jsp";
		}
	}
}

