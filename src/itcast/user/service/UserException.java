package itcast.user.service;
//创建一个异常 用于把异常信息抛回给servlet
public class UserException extends Exception{

	public UserException() {
		super();
		
	}

	public UserException(String message) {
		super(message);
		
	}

}
