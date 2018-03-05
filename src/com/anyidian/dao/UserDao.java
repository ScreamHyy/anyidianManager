package com.anyidian.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Bill;
import com.anyidian.model.Helper;
import com.anyidian.model.IntegralRecorde;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairList;
import com.anyidian.model.User;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class UserDao extends JdbcDaoSupport {
	private String background;
	private String name;
	private String mobile;
	private String avatar;
	private String idNumber;
	private String city;
	private String community;
	private String floor;
	private String unit;
	private String houseNumber;
	private int integral;
	private String volunteer;

	private String sql;

	/**
	 * 查询户主
	 * @param user
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<User> queryData(User user, PageBean pageBean) throws ParseException {
		sql = "select * from user";
		if(StringUtil.isNotEmpty(user.getCommunity())) {
			if(!user.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+user.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(user.getMobile())) {
			sql = sql+" and mobile like '%"+user.getMobile()+"%'";
		}
		if(StringUtil.isNotEmpty(user.getName())) {
			sql = sql+" and name like '%"+user.getName()+"%'";
		}

		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<User> mList = new ArrayList<User>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int num) throws SQLException {
				background = rs.getString("background");
				name = rs.getString("name");
				mobile = rs.getString("mobile");
				avatar = rs.getString("avatar");
				idNumber = rs.getString("idNumber");
				city = rs.getString("city");
				community = rs.getString("community");
				floor = rs.getString("floor");
				unit = rs.getString("unit");
				houseNumber = rs.getString("houseNumber");
				integral = rs.getInt("integral");
				volunteer = rs.getString("volunteer");
				User user = new User(background, name, mobile, avatar, idNumber, city, community, floor, unit, houseNumber, integral, volunteer);
				return user;
			}
		});
		return mList;
	}

	public int dataCount(User user) throws Exception {
		sql = "select count(*) as total from user";
		if(StringUtil.isNotEmpty(user.getCommunity())) {
			if(!user.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+user.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(user.getMobile())) {
			sql = sql+" and mobile like '%"+user.getMobile()+"%'";
		}
		if(StringUtil.isNotEmpty(user.getName())) {
			sql = sql+" and name like '%"+user.getName()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 更换户主背景图片
	 * @param mobileStr
	 * @return
	 */
	public boolean updateBackground(String mobileStr, String bgUrl) {
		String[] mobileArray = mobileStr.split(",");
		for (int i = 0; i < mobileArray.length; i++) {
			String mobile = mobileArray[i];
			sql = "update user set background=? where mobile=?";
			int resultInt = getJdbcTemplate().update(sql, new Object[]{bgUrl, mobile});
			if (!(resultInt > 0)) {
				return false;
			}
			if(i == mobileArray.length - 1) {
				if(resultInt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 查询积分兑换记录
	 * @param integralRecorde
	 * @param pageBean
	 * @return
	 */
	public List<IntegralRecorde> queryIntegralRecorde(IntegralRecorde integralRecorde, PageBean pageBean) {
		sql = "select * from integral_recorde";
		if(StringUtil.isNotEmpty(integralRecorde.getUser())) {
			sql = sql+" and mobile like '%"+integralRecorde.getUser()+"%'";
		}

		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<IntegralRecorde> mList = new ArrayList<IntegralRecorde>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<IntegralRecorde>() {

			@Override
			public IntegralRecorde mapRow(ResultSet rs, int num) throws SQLException {
				String user = rs.getString("user");
				String title = rs.getString("title");
				String date = rs.getString("date");
				String integral = rs.getString("integral");
				IntegralRecorde integralRecorde = new IntegralRecorde(user, title, date, integral);
				return integralRecorde;
			}
		});
		return mList;
	}

	public int IntegralRecordeCount(IntegralRecorde integralRecorde) {
		sql = "select count(*) as total from integral_recorde";
		if(StringUtil.isNotEmpty(integralRecorde.getUser())) {
			sql = sql+" and community like '%"+integralRecorde.getUser()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 添加积分兑换记录
	 * @param integralRecorde
	 * @param nowIntegral 
	 * @return
	 */
	public boolean insertIntegralRecorde(IntegralRecorde integralRecorde, int nowIntegral) {
		Date day = new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		String recordeDate = df.format(day);
		sql = "insert into integral_recorde(user,title,date,integral) values(?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{integralRecorde.getUser(), integralRecorde.getTitle(), recordeDate, integralRecorde.getIntegral()}); 
		if (resultInt > 0) {
			sql = "update user set integral=? where mobile=?";
			int flag = getJdbcTemplate().update(sql, new Object[]{nowIntegral, integralRecorde.getUser()}); 
			if (flag > 0) {
				return true;  
			}
			return false;  
		}
		return false;
	}
	
	/**
	 * 查询当前积分
	 * @param integralRecorde
	 * @return
	 */
	public int queryIntegral(String user) {
		sql = "select integral from user where mobile=?";
		integral = getJdbcTemplate().queryForInt(sql,  new Object[] {user});
		return integral;
	}

}
