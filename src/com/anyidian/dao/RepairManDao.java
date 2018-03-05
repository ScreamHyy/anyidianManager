package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anyidian.model.PageBean;
import com.anyidian.model.RepairMan;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;

public class RepairManDao extends JdbcDaoSupport {

	private String repairmanId;
	private String name;
	private String mobile;
	private String community;
	private String sql;
	private List<RepairMan> mList;

	public List<RepairMan> queryData(RepairMan repairMan, PageBean pageBean) throws ParseException {
		sql = "select * from repair_man";
		if(StringUtil.isNotEmpty(repairMan.getCommunity())) {
			if(!repairMan.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+repairMan.getCommunity()+"%'";
			}
		}
		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		mList = new ArrayList<RepairMan>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<RepairMan>() {

			@Override
			public RepairMan mapRow(ResultSet rs, int num) throws SQLException {
				repairmanId = rs.getString("repairmanId");
				community = rs.getString("community");
				name = rs.getString("name");
				mobile = rs.getString("mobile");
				RepairMan repairMan = new RepairMan(repairmanId, name, mobile, community);
				return repairMan;
			}
		});
		return mList;
	}

	public int dataCount(RepairMan repairMan) throws Exception {
		sql = "select count(*) as total from repair_man";
		if(StringUtil.isNotEmpty(repairMan.getCommunity())) {
			if(!repairMan.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+repairMan.getCommunity()+"%'";
			}
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	public List<RepairMan> queryRepairMan(final String community){
		sql = "select * from repair_man where community='"+community+"' order by id desc";
		mList = new ArrayList<RepairMan>();
		mList = getJdbcTemplate().query(sql, new RowMapper<RepairMan>() {

			@Override
			public RepairMan mapRow(ResultSet rs, int num)
					throws SQLException {
				repairmanId = rs.getString("repairmanId");
				name = rs.getString("name");
				mobile = rs.getString("mobile");
				RepairMan repairMan = new RepairMan(repairmanId, name, mobile, community);
				return repairMan;
			}

		});
		return mList;
	}

	/**
	 * 更改
	 * @param repairMan
	 * @return
	 */
	public boolean modifyData(RepairMan repairMan) {
		sql = "update repair_man set community=?,name=?,mobile=? where repairmanId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{repairMan.getCommunity(), repairMan.getName(), repairMan.getMobile(), 
				repairMan.getRepairmanId()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 保存
	 * @param repairMan
	 * @return
	 */
	public boolean addData(RepairMan repairMan) {
		Long timeStamp = System.currentTimeMillis() / 1000;
		String repairmanId = String.valueOf(timeStamp);
		sql = "insert into repair_man(repairmanId,community,name,mobile) values(?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{repairmanId, repairMan.getCommunity(), repairMan.getName(), repairMan.getMobile()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public boolean deleteData (String repairmanId) {
		sql = "delete from repair_man where repairmanId in("+repairmanId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

}
