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
		//提取内容
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
		//使用占位符替换
		content=MessageFormat.format(content, "xxxxx");
		//第一个参数为模板 后面的是替换成的内容 可包含多个 有几个占位符模板后面几个参数
	}
	
	@Test
	public void fun3() {
		//测试使用BigInteger完成100的阶乘
		BigInteger sum=BigInteger.valueOf(1);//封装成BigInteger
		for(int i=1;i<=100;i++) {//循环相乘
			BigInteger b= BigInteger.valueOf(i);
			sum=sum.multiply(b);
		}
		System.out.println(sum);
	
		//测试使用BigDecimal来处理二进制带来的误差	
		
		BigDecimal d1=new BigDecimal("2.0");//创建对象必须使用String构造器
		BigDecimal d2=new BigDecimal("1.0");
		//d1-d2
		BigDecimal d3=d1.subtract(d2);
		System.out.println(d3);
		
	}

}
