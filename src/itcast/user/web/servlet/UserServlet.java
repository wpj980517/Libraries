package itcast.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;
import itcast.cart.domain.Cart;
import itcast.user.domain.User;
import itcast.user.service.UserException;
import itcast.user.service.UserService;

public class UserServlet  extends BaseServlet{
	private UserService userservice =new UserService();
	
	//�����
	public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��ȡ������
		String code=request.getParameter("code");
		try {
			//����
			userservice.active(code);
			request.setAttribute("msg", "����ɹ�");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.toString());
			return "f:/jsps/msg.jsp";
		}
	
	}
	
	
	
	//ע�Ṧ��
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��ȡ�������ı����ݲ���ȫ
		User form =CommonUtils.toBean(request.getParameterMap(), User.class);
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//У�������Ϣ
		Map<String,String> errors=new HashMap<String ,String>();
		
		String username =form.getUsername();		
		if(username==null||username.trim().isEmpty()) {
			errors.put("username", "�û�������Ϊ��");
			
		}else if(username.length()<3 || username.length()>10)
		{
			errors.put("username", "�û�����3~10֮��");
			
		}
		
		String password= form.getPassword();
		if(password==null||password.trim().isEmpty()) {
			errors.put("password", "���벻��Ϊ��");
			
		}else if(password.length()<3 ||password.length()>10)
		{
			errors.put("password", "������3~10֮��");
			
		}
		
		String email= form.getEmail();
		if(email==null||email.trim().isEmpty()) {
			errors.put("email", "���䲻��Ϊ��");
			
		}else if(!email.matches("\\w+@\\w+\\.\\w+"))//ʹ��������ʽ�����������ʽ
		{
			errors.put("email", "�����ʽ����");

		}
		if(errors.size()>0) {
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		//�����ʼ�
		userservice.sendEmail(form);
	
		//�޴��������
		try {
			userservice.regist(form);
			request.setAttribute("msg", "��ϲ��ע��ɹ��������ϼ���");
			return "f:/jsps/msg.jsp";
			
		} catch (UserException e) {
			//����û������������Ѿ����� ת����msg��
			request.setAttribute("msg", e);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}

	}
	//��¼����
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ����
		User form =CommonUtils.toBean(request.getParameterMap(), User.class);
		
		if(form.getUsername().trim().isEmpty()||form.getPassword().trim().isEmpty())
		{
			request.setAttribute("msg","�û��������벻��Ϊ��");
			return "f:/jsps/user/login.jsp";
		}

		//���÷���
		try {
			User user=userservice.login(form);
			//���浽session�����ض���
			request.getSession().setAttribute("session_user", user);
			//���ͻ�һ�����ﳵ
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.toString());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
		
	
	}	
	//�˳�����
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��session����
		request.getSession().invalidate();
		return "r:/index.jsp";
	
	}
}
