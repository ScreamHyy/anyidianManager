package com.anyidian.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Helper;
import com.anyidian.model.PageBean;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class HelperDao extends JdbcDaoSupport {

	private String sql;
	private List<Helper> mList;
	
	/**
	 * 查询缴费单
	 * @param Helper
	 * @param pageBean
	 * @param bdate
	 * @param edate
	 * @return
	 * @throws ParseException
	 */
	public List<Helper> queryData(Helper Helper, PageBean pageBean) throws ParseException {
		sql = "select * from helper";
		if(StringUtil.isNotEmpty(Helper.getHelperType())) {
			sql = sql+" and helperType like '%"+Helper.getHelperType()+"%'";
		}
		
		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		mList = new ArrayList<Helper>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Helper>() {

			@Override
			public Helper mapRow(ResultSet rs, int num) throws SQLException {
				String helperId = rs.getString("helperId");
				String icon = rs.getString("icon");
				String helperType = rs.getString("helperType");
				String website = rs.getString("website");
				Helper item = new Helper(helperId, icon, helperType, website);
				return item;
			}
		});
		return mList;
	}
	
	public int dataCount(Helper helper) throws Exception {
		sql = "select count(*) as total from helper";
		if(StringUtil.isNotEmpty(helper.getHelperType())) {
			sql = sql+" and helperType like '%"+helper.getHelperType()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 修改数据
	 * @param Helper
	 * @return
	 */
	public boolean updateData(Helper Helper) {
		sql = "update helper set icon=?,helperType=?,website=? where helperId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{Helper.getIcon(), Helper.getHelperType(), Helper.getWebsite(), Helper.getHelperId()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 添加数据
	 * @param Helper
	 * @return
	 */
	public boolean addData(Helper Helper) {
		Long timeStamp = System.currentTimeMillis() / 1000;
		String helperId = String.valueOf(timeStamp);
		sql = "insert into helper(helperId,icon,helperType,website) values(?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{helperId, Helper.getIcon(), Helper.getHelperType(), Helper.getWebsite()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param Helper
	 * @return
	 */
	public boolean deleteData(String helperId) {
		sql = "delete from helper where helperId in("+helperId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

}
