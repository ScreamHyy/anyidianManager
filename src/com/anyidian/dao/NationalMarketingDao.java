package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Marketing;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class NationalMarketingDao extends JdbcDaoSupport {
	private String sql;
	private List<Marketing> mList;

	/**
	 * 
	 * Dao层得查询方法
	 * 
	 * @author ScreamHyy
	 * @param marketing
	 * @return mList
	 * 
	 **/
	@SuppressWarnings("unchecked")
	public List<Marketing> queryData(Marketing marketing, PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from marketing";
		if (StringUtil.isNotEmpty(marketing.getMarketingId())) {
			sql += " and marketingId like '%" + marketing.getMarketingId()
					+ "%'";
		}
		if (StringUtil.isNotEmpty(marketing.getHouseType())) {
			sql += " and houseType like '%" + marketing.getHouseType() + "%'";
		}
		if (StringUtil.isNotEmpty(marketing.getHouseNum())) {
			sql += " and houseNum like '%" + marketing.getHouseNum() + "%'";
		}
		if (StringUtil.isNotEmpty(marketing.getName())) {
			sql += " and name like '%" + marketing.getName() + "%'";
		}
		if (StringUtil.isNotEmpty(marketing.getMobile())) {
			sql += " and mobile like '%" + marketing.getMobile() + "%'";
		}
		if (StringUtil.isNotEmpty(marketing.getToName())) {
			sql += " and toName like '%" + marketing.getToName() + "%'";
		}
		if (StringUtil.isNotEmpty(marketing.getToMobile())) {
			sql += " ane toMobile like '%" + marketing.getToMobile() + "%'";
		}

		sql += " order by id desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}

		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<Marketing>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Marketing>() {

			@Override
			public Marketing mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				Marketing marketing = new Marketing();
				marketing.setMarketingId(rs.getString("marketingId"));
				marketing.setHouseType(rs.getString("houseType"));
				marketing.setHouseNum(rs.getString("houseNum"));
				marketing.setName(rs.getString("name"));
				marketing.setMobile(rs.getString("mobile"));
				marketing.setToName(rs.getString("toName"));
				marketing.setToMobile(rs.getString("toMobile"));
				return marketing;
			}
		});

		return mList;
	}

	/**
	 * 
	 * Dao层修改方法
	 * 
	 * @author ScreamHyy
	 * @param marketing
	 * @return boolean
	 * 
	 **/

	public boolean modifyData(Marketing marketing) {
		// TODO Auto-generated method stub
		sql = "update marketing set houseType=?,houseNum=?,name=?,mobile=?,toName=?,toMobile=? where marketingId=?";
		int result = getJdbcTemplate().update(
				sql,
				new Object[] { marketing.getHouseType(),
						marketing.getHouseNum(), marketing.getName(),
						marketing.getMobile(), marketing.getToName(),
						marketing.getToMobile(), marketing.getMarketingId() });

		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Dao层添加方法
	 * 
	 * @author ScreamHyy
	 * @param marketing
	 * @return boolean
	 * 
	 **/

	public boolean addData(Marketing marketing) {
		// TODO Auto-generated method stub
		Long timeStamp = System.currentTimeMillis();
		marketing.setMarketingId(String.valueOf(timeStamp));
		sql = "insert into marketing(marketingId,houseType,houseNum,name,mobile,toName,toMobile) values(?,?,?,?,?,?,?)";
		int result = getJdbcTemplate().update(
				sql,
				new Object[] { marketing.getMarketingId(),
						marketing.getHouseType(), marketing.getHouseNum(),
						marketing.getName(), marketing.getMobile(),
						marketing.getToName(), marketing.getToMobile() });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteNationalMarketing(String delIds) {
		// TODO Auto-generated method stub
		sql = "delete from marketing where marketingId in("+delIds+")";
		int result = getJdbcTemplate().update(sql, new Object[]{});
		if (result > 0) {
			return true;
		}
		return false;
	}

}
