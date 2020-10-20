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
		//����post��������������룬��ֹ����
		request.setCharacterEncoding("utf-8");
		//������Ӧ���ĵı���������������õı���
		response.setContentType("text/html;charset=utf-8");
		
		//��ȡ�����ݣ�������
		DiskFileItemFactory factory=new DiskFileItemFactory(1024*1024,new File("D:/temp"));
		ServletFileUpload sfu=new ServletFileUpload(factory);
		//���õ����ļ����ֵ
		sfu.setFileSizeMax(1024*1024);
		try {
			List<FileItem> list=sfu.parseRequest(request);
			
			//������ͼƬ����Ķ����װ��mapȻ���ٷ�װ��book��
			Map<String,String> map=new HashMap<String,String>();
			
			for(FileItem item:list) {
				if(item.isFormField())
				{
					//�жϱ����Ƿ�Ϊ��ͨ�������ǾͲ��뵽map��
					map.put(item.getFieldName(), item.getString("utf-8"));
				}
			}
			Book book=CommonUtils.toBean(map, Book.class);
			//����bid
			book.setBid(CommonUtils.uuid());
			//����cid
			Category category=CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			

			
			
			//��һ����Ϊ�˰ѱ�ĵط�������ͼƬ��ŵ�ָ��Ŀ¼��Ȼ�������ȡ
			//��ȡ�õ����ͼƬ��·�����Լ�ͼƬ����
			String realpath=this.getServletContext().getRealPath("book_img");
			int index=list.get(1).getName().lastIndexOf("\\");
			String filename=CommonUtils.uuid()+"_"+list.get(1).getName().substring(index+1);
			
			//���filename��ʽ
			if(!filename.endsWith("jpg"))
			{
				request.setAttribute("msg", "�ϴ���ʽ����");
				//�����ﻹҪ�ѷ������Ϣ������ȥ����Ȼ����������
				request.setAttribute("categorylist", categoryservice.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			/*
			//����ͼƬ�ߴ�
			//�ȵõ�ͼƬ
			Image image=new ImageIcon(list.get(1).getName()).getImage();
			if(image.getWidth(null)>300||image.getHeight(null)>300)
			{
				//��ͼƬ��͸�����Ϊ200
				request.setAttribute("msg", "ͼƬ̫��");
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return ;
			}
			*/
			
	
			//�����װ���ļ�Ȼ��FileItem�е��ļ�����ȥ
			File file=new File(realpath, filename);
			System.out.println(file.getAbsolutePath());
			list.get(1).write(file);
			
			//��ͼƬ���ø�image
			book.setImage("book_img/"+filename);
			
			//��ͼ����ӽ�ȥ
			bookservice.add(book);
			
			request.getRequestDispatcher("/adminbook?method=findAll").forward(request, response);

		} catch (Exception e) {
			//���ȴ����ļ���С�����쳣
			if(e instanceof FileUploadBase.FileSizeLimitExceededException)
			{
				request.setAttribute("msg", "�����ļ�����");
				request.setAttribute("categorylist", categoryservice.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				
			}
		}
		
		
		
		
	}
}	
