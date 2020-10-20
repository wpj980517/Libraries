package testmod;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Properties;

import org.junit.Test;



public class test1 {
	@Test
	public void fun1() throws IOException {
		Properties prop=new Properties();
		prop.load(this.getClass().getResourceAsStream("/email_template.properties"));
		//��ȡ����
		String host=prop.getProperty("host");
		String uname=prop.getProperty("uname");
		String pwd=prop.getProperty("pwd");
		String from=prop.getProperty("from");
		String subject =prop.getProperty("subject");
		String content=prop.getProperty("content");
		System.out.println(new String(subject.getBytes("iso-8859-1"),"utf-8"));
	}
	@Test
	public void fun2() throws IOException {
		Properties prop=new Properties();
		prop.load(this.getClass().getResourceAsStream("/email_template.properties"));
		String content=prop.getProperty("content");
		//ʹ��ռλ���滻
		content=MessageFormat.format(content, "xxxxx");
		//��һ������Ϊģ�� ��������滻�ɵ����� �ɰ������ �м���ռλ��ģ����漸������
	}
	
	@Test
	public void fun3() {
		//����ʹ��BigInteger���100�Ľ׳�
		BigInteger sum=BigInteger.valueOf(1);//��װ��BigInteger
		for(int i=1;i<=100;i++) {//ѭ�����
			BigInteger b= BigInteger.valueOf(i);
			sum=sum.multiply(b);
		}
		System.out.println(sum);
	
		//����ʹ��BigDecimal����������ƴ��������	
		
		BigDecimal d1=new BigDecimal("2.0");//�����������ʹ��String������
		BigDecimal d2=new BigDecimal("1.0");
		//d1-d2
		BigDecimal d3=d1.subtract(d2);
		System.out.println(d3);
		
	}

}
