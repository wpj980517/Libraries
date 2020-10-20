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




//管理员的servlet
public class adminCategoryServlet extends BaseServlet {
	private CategoryService categoryservice=new CategoryService();
	
	//转到登录页面
	public String toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "r:/adminjsps/login.jsp";
	}
	//查找所有分类
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> list=categoryservice.findAll();
		
		request.setAttribute("categorylist", list);
		
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//添加分类
	public String addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cname=request.getParameter("cname");
		try {
			categoryservice.addCategory(cname);
			request.setAttribute("msg", "添加成功");
			return "f:/adminjsps/admin/category/add.jsp";
			
		} catch (categoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/admin/category/add.jsp";
		}

	}
	//删除分类
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
	//修改分类两个方法，一个获取对象发给mod.jsp，然后再修改
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

