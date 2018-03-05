package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Committee;
import com.anyidian.model.Community;
import com.anyidian.model.Helper;
import com.anyidian.model.LifeTypeItem;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class CommunityDao extends JdbcDaoSupport {

	private String sql;
	
	/**
	 * 查询小区详情信息
	 * @param pageBean
	 * @return
	 */
	public List<Community> queryData(PageBean pageBean) {
		sql = "select * from community";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<Community> mList = new ArrayList<Community>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Community>(){

			@Override
			public Community mapRow(ResultSet rs, int num)
					throws SQLException {
				String communityId = rs.getString("communityId");
				String communityName = rs.getString("communityName");
				String committee = rs.getString("committee");
				String city = rs.getString("city");
				String propertyName = rs.getString("propertyName");
				String mobile = rs.getString("mobile");
				String address = rs.getString("address");
				String landArea = rs.getString("landArea");
				String buildArea = rs.getString("buildArea");
				Community community = new Community(communityId, communityName, committee, city, propertyName, mobile, address, landArea, buildArea);
				return community;
			}
		});
		return mList;
	}
	
	public int dataCount() throws Exception {
		sql = "select count(*) as total from community";
		int total = getJdbcTemplate().queryForInt(sql);
		return total;
	}

	/**
	 * 查询小区
	 * @return
	 */
	public List<Community> queryCommunity() {
		sql = "select * from community";
		List<Community> mList = new ArrayList<Community>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Community>(){

			@Override
			public Community mapRow(ResultSet rs, int num)
					throws SQLException {
				String communityId = rs.getString("communityId");
				String communityName = rs.getString("communityName");
				String committee = rs.getString("committee");
				String city = rs.getString("city");
				String propertyName = rs.getString("propertyName");
				String mobile = rs.getString("mobile");
				String address = rs.getString("address");
				String landArea = rs.getString("landArea");
				String buildArea = rs.getString("buildArea");
				Community community = new Community(communityId, communityName, committee, city, propertyName, mobile, address, landArea, buildArea);
				return community;
			}
		});
		return mList;
	}
	
	/**
	 * 修改数据
	 * @param communityName
	 * @return
	 */
	public boolean modifyData(Community community) {
		sql = "update community set communityName=?,committee=?,city=?,propertyName=?,mobile=?,address=?,landArea=?,buildArea=? where communityId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{community.getCommunityName(), community.getCommittee(), community.getCity(), 
				community.getPropertyName(), community.getMobile(), community.getAddress(), community.getLandArea(), community.getBuildArea(), community.getCommunityId()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 添加数据
	 * @param communityName
	 * @return
	 */
	public boolean addData(Community community) {
		Long timeStamp = System.currentTimeMillis() / 1000;
		String communityId = String.valueOf(timeStamp);
		sql = "insert into community(communityId,communityName,committee,city,propertyName,mobile,address,landArea,buildArea) values(?,?,?,?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{communityId, community.getCommunityName(), community.getCommittee(), community.getCity(), 
				community.getPropertyName(), community.getMobile(), community.getAddress(), community.getLandArea(), community.getBuildArea()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}
	
	/**
	 * 删除小区
	 * @param delIds
	 * @return
	 */
	public boolean deleteData(String delIds) {
		sql = "delete from community where communityId in("+delIds+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}
	
	/**
	 * 查询小区居委会
	 * @return
	 */
	public List<Committee> queryCommittee() {
		sql = "select * from committee";
		List<Committee> mList = new ArrayList<Committee>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Committee>(){

			@Override
			public Committee mapRow(ResultSet rs, int num)
					throws SQLException {
				String committeeId = rs.getString("committeeId");
				String committeeName = rs.getString("committeeName");
				String header = rs.getString("header");
				String mobile = rs.getString("mobile");
				String address = rs.getString("address");
				Committee committee = new Committee(committeeId, committeeName, header, mobile, address);
				return committee;
			}
		});
		return mList;
	}
	
}
