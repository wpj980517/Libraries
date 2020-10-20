package itcast.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import itcast.category.domain.Category;
import itcast.category.service.CategoryService;

public class CategoryServlet extends BaseServlet {
	private CategoryService categoryservice =new CategoryService();
	//查询所有分类

	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Category> list=categoryservice.findAll();
		request.setAttribute("categoryList", list);
		return "f:/jsps/left.jsp";
	}
	
}
