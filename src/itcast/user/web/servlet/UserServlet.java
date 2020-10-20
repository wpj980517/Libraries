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
	
	//激活功能
	public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获取激活码
		String code=request.getParameter("code");
		try {
			//激活
			userservice.active(code);
			request.setAttribute("msg", "激活成功");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.toString());
			return "f:/jsps/msg.jsp";
		}
	
	}
	
	
	
	//注册功能
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获取传过来的表单数据并补全
		User form =CommonUtils.toBean(request.getParameterMap(), User.class);
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//校验错误信息
		Map<String,String> errors=new HashMap<String ,String>();
		
		String username =form.getUsername();		
		if(username==null||username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
			
		}else if(username.length()<3 || username.length()>10)
		{
			errors.put("username", "用户名在3~10之间");
			
		}
		
		String password= form.getPassword();
		if(password==null||password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
			
		}else if(password.length()<3 ||password.length()>10)
		{
			errors.put("password", "密码在3~10之间");
			
		}
		
		String email= form.getEmail();
		if(email==null||email.trim().isEmpty()) {
			errors.put("email", "邮箱不能为空");
			
		}else if(!email.matches("\\w+@\\w+\\.\\w+"))//使用正则表达式来检验邮箱格式
		{
			errors.put("email", "邮箱格式错误");

		}
		if(errors.size()>0) {
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		//发送邮件
		userservice.sendEmail(form);
	
		//无错误则添加
		try {
			userservice.regist(form);
			request.setAttribute("msg", "恭喜你注册成功，请马上激活");
			return "f:/jsps/msg.jsp";
			
		} catch (UserException e) {
			//如果用户名或者邮箱已经存在 转发到msg上
			request.setAttribute("msg", e);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}

	}
	//登录功能
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数
		User form =CommonUtils.toBean(request.getParameterMap(), User.class);
		
		if(form.getUsername().trim().isEmpty()||form.getPassword().trim().isEmpty())
		{
			request.setAttribute("msg","用户名或密码不能为空");
			return "f:/jsps/user/login.jsp";
		}

		//调用方法
		try {
			User user=userservice.login(form);
			//保存到session并且重定向
			request.getSession().setAttribute("session_user", user);
			//给客户一辆购物车
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.toString());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
		
	
	}	
	//退出功能
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//把session销毁
		request.getSession().invalidate();
		return "r:/index.jsp";
	
	}
}
