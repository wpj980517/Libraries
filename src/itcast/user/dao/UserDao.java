package itcast.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;
import itcast.user.domain.User;

public class UserDao {
	private QueryRunner qr=new TxQueryRunner();
	
	//按照人名查询
	public User findByUsername(String username) {
		try {
			String sql="select * from tb_user where username=?";
			return qr.query(sql, new BeanHandler<User>(User.class), username);
		} catch (SQLException e) {
			throw new RuntimeException("查询人名失败");
		}
	}
	//查询邮件地址
	public User findByEmail(String email) {
		try {
			String sql="select * from tb_user where username=?";
			return qr.query(sql, new BeanHandler<User>(User.class), email);
		} catch (SQLException e) {
			throw new RuntimeException("查询邮件失败");
		}
	}
	//添加到数据库
	public void add(User user) {
		try {
			String sql="insert into tb_user values(?,?,?,?,?,?)";
			Object[] params= {user.getUid(),user.getUsername(),user.getPassword(),user.getEmail(),user.getCode(),user.isState()};
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("添加错误");
		}
	}
	//按激活码查询
	public User findByCode(String code) {
		try {
			String sql="select * from tb_user where code=?";
			return qr.query(sql, new BeanHandler<User>(User.class), code);
		} catch (SQLException e) {
			throw new RuntimeException("按激活码查询用户失败");
		}
	}
	//更新激活状态
	public void updateState(String uid,boolean state) {
		try {
			String sql="update tb_user set state=? where uid=?";
			Object[] params= {state,uid};
			 qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("激活失败");
		}
	}
}
