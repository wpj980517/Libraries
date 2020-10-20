package itcast.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import itcast.book.domain.Book;
import itcast.book.service.BookService;
import itcast.category.domain.Category;
import itcast.category.service.CategoryService;

public class adminAddBookServlet extends HttpServlet {
	private BookService bookservice=new BookService();
	private CategoryService categoryservice=new CategoryService();
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//对于post请求，设置请求编码，防止乱码
		request.setCharacterEncoding("utf-8");
		//设置响应正文的编码和浏览器解码采用的编码
		response.setContentType("text/html;charset=utf-8");
		
		//获取表单内容，三步走
		DiskFileItemFactory factory=new DiskFileItemFactory(1024*1024,new File("D:/temp"));
		ServletFileUpload sfu=new ServletFileUpload(factory);
		//设置单个文件最大值
		sfu.setFileSizeMax(1024*1024);
		try {
			List<FileItem> list=sfu.parseRequest(request);
			
			//将除了图片以外的对象封装到map然后再封装到book中
			Map<String,String> map=new HashMap<String,String>();
			
			for(FileItem item:list) {
				if(item.isFormField())
				{
					//判断表单项是否为普通表单项，如果是就插入到map中
					map.put(item.getFieldName(), item.getString("utf-8"));
				}
			}
			Book book=CommonUtils.toBean(map, Book.class);
			//设置bid
			book.setBid(CommonUtils.uuid());
			//设置cid
			Category category=CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			

			
			
			//这一步是为了把别的地方得来的图片存放到指定目录下然后得以提取
			//获取得到存放图片的路径，以及图片名称
			String realpath=this.getServletContext().getRealPath("book_img");
			int index=list.get(1).getName().lastIndexOf("\\");
			String filename=CommonUtils.uuid()+"_"+list.get(1).getName().substring(index+1);
			
			//检测filename格式
			if(!filename.endsWith("jpg"))
			{
				request.setAttribute("msg", "上传格式错误");
				//在这里还要把分类的信息给传过去，不然看不见分类
				request.setAttribute("categorylist", categoryservice.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			/*
			//检验图片尺寸
			//先得到图片
			Image image=new ImageIcon(list.get(1).getName()).getImage();
			if(image.getWidth(null)>300||image.getHeight(null)>300)
			{
				//将图片宽和高限制为200
				request.setAttribute("msg", "图片太大");
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return ;
			}
			*/
			
	
			//将其封装成文件然后将FileItem中的文件传进去
			File file=new File(realpath, filename);
			System.out.println(file.getAbsolutePath());
			list.get(1).write(file);
			
			//把图片设置给image
			book.setImage("book_img/"+filename);
			
			//将图书添加进去
			bookservice.add(book);
			
			request.getRequestDispatcher("/adminbook?method=findAll").forward(request, response);

		} catch (Exception e) {
			//首先处理文件大小过大异常
			if(e instanceof FileUploadBase.FileSizeLimitExceededException)
			{
				request.setAttribute("msg", "传入文件过大");
				request.setAttribute("categorylist", categoryservice.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				
			}
		}
		
		
		
		
	}
}	
