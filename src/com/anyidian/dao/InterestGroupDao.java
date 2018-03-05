package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Interest;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class InterestGroupDao extends JdbcDaoSupport {
	String sql;
	List<Interest> mList;

	public List<Interest> queryData(Interest interest, PageBean pageBean) {
		sql = "select * from interest";
		if (StringUtil.isNotEmpty(interest.getCommunity())) {
			sql += " and community like '%" + interest.getCommunity()
					+ "%'";
		}
		sql += " order by id desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}
		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<Interest>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Interest>() {

			@Override
			public Interest mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				Interest interest = new Interest();
				interest.setInterestId(rs.getString("interestId"));
				interest.setCommunity(rs.getString("community"));
				interest.setInterestType(rs.getString("interestType"));

				return interest;
			}

		});
		// TODO Auto-generated method stub
		return mList;
	}

	public boolean modifyData(Interest interest) {
		// TODO Auto-generated method stub
		sql = "update interest set community=?,interestType=? where interestId=?";
		int result = getJdbcTemplate().update(sql,
				new Object[] { interest.getCommunity(), interest.getInterestType(),interest.getInterestId() });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean addData(Interest interest) {
		// TODO Auto-generated method stub
		Long timeStamp = System.currentTimeMillis();
		interest.setInterestId(String.valueOf(timeStamp));
		sql = "insert into interest(interestId,community,interestType) values(?,?,?)";
		int result = getJdbcTemplate().update(
				sql,
				new Object[] { interest.getInterestId(),
						interest.getCommunity(), interest.getInterestType() });
		
		if(result > 0){
			return true;
		}
		return false;
	}

	public boolean deleteData(String delIds) {
		// TODO Auto-generated method stub
		sql = "delete from interest where interestId in(" + delIds + ")";
		int result = getJdbcTemplate().update(sql, new Object[]{});
		if(result > 0){
			return true;
		}
		return false;
	}

	public List<Interest> queryInterestType() {
		// TODO Auto-generated method stub
		sql = "select * from interest";
		mList = new ArrayList<Interest>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Interest>(){

			@Override
			public Interest mapRow(ResultSet rs, int num)
					throws SQLException {
				// TODO Auto-generated method stub
				Interest interest = new Interest();
				interest.setInterestId(rs.getString("interestId"));
				interest.setCommunity(rs.getString("community"));
				interest.setInterestType(rs.getString("interestType"));
				return interest;
			}
			
		});
		return mList;
	}

}
