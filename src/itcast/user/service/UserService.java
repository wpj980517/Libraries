package itcast.user.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import itcast.user.dao.UserDao;
import itcast.user.domain.User;

public class UserService {
	private UserDao userdao =new UserDao();
	
	//注册功能
	public void regist(User form) throws UserException 
	{
		//校验用户名
		User user=userdao.findByUsername(form.getUsername());
		if(user!=null) throw new UserException("用户名已被注册");
		//校验邮件
		user=userdao.findByEmail(form.getEmail());
		if(user!=null) throw new UserException("邮箱已被注册");
		//无错误则添加到数据库
		userdao.add(form);
	}
	
	//发邮件功能
	public void sendEmail(User form) throws IOException {
		/*发送邮件*/
		//先提取出properties配置文件
		Properties prop=new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("/email_template.properties"));
		//提取内容
		String host=prop.getProperty("host");
		String uname=prop.getProperty("uname");
		String pwd=prop.getProperty("pwd");
		String from=prop.getProperty("from");
		String to=form.getEmail();
		String subject =new String(prop.getProperty("subject").getBytes("iso-8859-1"),"utf-8")  ;
		String content=new String(prop.getProperty("content").getBytes("iso-8859-1"),"utf-8");
		
		//将内容中的占位码换成内容
		content=MessageFormat.format(content,form.getCode());
		
		Session session=MailUtils.createSession(host, uname, pwd);//创建session
		Mail mail=new Mail(from,to,subject,content);//创建邮件对象
		try {
			MailUtils.send(session, mail);//发送邮件
		} catch (MessagingException e1) {
			throw new RuntimeException("发送失败");
		}
	}
	
	//激活功能
	public void active(String code) throws UserException {
		
		User user=userdao.findByCode(code);
		
		//检验用户
		if(user==null)throw new UserException("无此用户，激活码或错误");
		if(user.isState())throw new UserException("已激活，无需继续激活");
		//更改状态
		userdao.updateState(user.getUid(), true);	
	}
	
	//登录功能
	public User login(User form) throws UserException {
		User user=userdao.findByUsername(form.getUsername());
		//检验form
		if(form.getUsername().trim().isEmpty()||form.getPassword().trim().isEmpty())
			throw new UserException("账户或密码不能为空");
		//判断是否存在
		if(user==null)throw new UserException("用户不存在");
		//判断密码是否正确
		if(!user.getPassword().equals(form.getPassword()))throw new UserException("密码错误");
		//判断是否激活
		if(!user.isState())throw new UserException("用户未激活");
		//返回user
		return user;
		
	}
	
	
}
