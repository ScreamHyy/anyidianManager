package com.anyidian.dao;

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
import com.anyidian.model.Complaints;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class ComplaintsDao extends JdbcDaoSupport {
	
	private String sql;
	
	/**
	 * 查询投诉列表
	 * @param complaints
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<Complaints> queryData(Complaints complaints, PageBean pageBean) throws ParseException {
		sql = "select * from complaints_list";
		if(StringUtil.isNotEmpty(complaints.getCommunity())) {
			if(!complaints.getCommunity().equals("全部")){
				sql = sql+" "+"and community like '%"+complaints.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(complaints.getComplaintType())) {
			if(!complaints.getComplaintType().equals("全部")){
				sql = sql+" "+"and complaintType like '%"+complaints.getComplaintType()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(complaints.getState())) {
			sql = sql+" "+"and state like '%"+complaints.getState()+"%'";
		}
		
		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<Complaints> mList = new ArrayList<Complaints>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Complaints>() {

			@Override
			public Complaints mapRow(ResultSet rs, int num) throws SQLException {
				String listId = rs.getString("listId");
				String state = rs.getString("state");
				String community = rs.getString("community");
				String name = rs.getString("name");
				String mobile = rs.getString("mobile");
				String address = rs.getString("address");
				String date = rs.getString("date");
				String complaintType = rs.getString("complaintType");
				String introduce = rs.getString("introduce");
				String image = rs.getString("image");
				String evaluate = rs.getString("evaluate");
				int attitudeScore = rs.getInt("attitudeScore");
				int timeScore = rs.getInt("timeScore");
				Complaints complaints = new Complaints(listId, state, community, name, mobile, address, date, complaintType, introduce, image, evaluate, attitudeScore, timeScore);
				return complaints;
			}
		});
		return mList;
	}
	
	public int dataCount(Complaints complaints) throws Exception {
		sql = "select count(*) as total from complaints_list";
		if(StringUtil.isNotEmpty(complaints.getCommunity())) {
			if(!complaints.getCommunity().equals("全部")){
				sql = sql+" "+"and community like '%"+complaints.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(complaints.getComplaintType())) {
			if(!complaints.getComplaintType().equals("全部")){
				sql = sql+" "+"and complaintType like '%"+complaints.getComplaintType()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(complaints.getState())) {
			sql = sql+" "+"and state like '%"+complaints.getState()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 派遣处理
	 * @param listId
	 * @return
	 */
	public boolean dealComplaints(String listId) {
		Date day = new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String sendTime = df.format(day);
		
		sql = "update complaints_list set state=?,sendTime=? where listId=?";  
		int resultInt = getJdbcTemplate().update(sql, new Object[]{"处理中", sendTime, listId}); 
		if (resultInt > 0) {
			return true;
		}
		return false;
	}
	
}
