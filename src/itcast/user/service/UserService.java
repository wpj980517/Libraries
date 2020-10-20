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
	
	//ע�Ṧ��
	public void regist(User form) throws UserException 
	{
		//У���û���
		User user=userdao.findByUsername(form.getUsername());
		if(user!=null) throw new UserException("�û����ѱ�ע��");
		//У���ʼ�
		user=userdao.findByEmail(form.getEmail());
		if(user!=null) throw new UserException("�����ѱ�ע��");
		//�޴�������ӵ����ݿ�
		userdao.add(form);
	}
	
	//���ʼ�����
	public void sendEmail(User form) throws IOException {
		/*�����ʼ�*/
		//����ȡ��properties�����ļ�
		Properties prop=new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("/email_template.properties"));
		//��ȡ����
		String host=prop.getProperty("host");
		String uname=prop.getProperty("uname");
		String pwd=prop.getProperty("pwd");
		String from=prop.getProperty("from");
		String to=form.getEmail();
		String subject =new String(prop.getProperty("subject").getBytes("iso-8859-1"),"utf-8")  ;
		String content=new String(prop.getProperty("content").getBytes("iso-8859-1"),"utf-8");
		
		//�������е�ռλ�뻻������
		content=MessageFormat.format(content,form.getCode());
		
		Session session=MailUtils.createSession(host, uname, pwd);//����session
		Mail mail=new Mail(from,to,subject,content);//�����ʼ�����
		try {
			MailUtils.send(session, mail);//�����ʼ�
		} catch (MessagingException e1) {
			throw new RuntimeException("����ʧ��");
		}
	}
	
	//�����
	public void active(String code) throws UserException {
		
		User user=userdao.findByCode(code);
		
		//�����û�
		if(user==null)throw new UserException("�޴��û�������������");
		if(user.isState())throw new UserException("�Ѽ�������������");
		//����״̬
		userdao.updateState(user.getUid(), true);	
	}
	
	//��¼����
	public User login(User form) throws UserException {
		User user=userdao.findByUsername(form.getUsername());
		//����form
		if(form.getUsername().trim().isEmpty()||form.getPassword().trim().isEmpty())
			throw new UserException("�˻������벻��Ϊ��");
		//�ж��Ƿ����
		if(user==null)throw new UserException("�û�������");
		//�ж������Ƿ���ȷ
		if(!user.getPassword().equals(form.getPassword()))throw new UserException("�������");
		//�ж��Ƿ񼤻�
		if(!user.isState())throw new UserException("�û�δ����");
		//����user
		return user;
		
	}
	
	
}
