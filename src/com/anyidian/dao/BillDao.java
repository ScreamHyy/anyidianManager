package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Bill;
import com.anyidian.model.Helper;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairList;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class BillDao extends JdbcDaoSupport {
	private String billId;
	private String community;
	private String name;
	private String mobile;
	private String room;
	private String billType;
	private String date;
	private double price;
	private String state;

	private String sql;
	private List<Bill> mList;

	/**
	 * 查询缴费单
	 * @param bill
	 * @param pageBean
	 * @param bdate
	 * @param edate
	 * @return
	 * @throws ParseException
	 */
	public List<Bill> queryData(Bill bill, PageBean pageBean, String bdate, String edate) throws ParseException {
		sql = "select * from bill";
		if(StringUtil.isNotEmpty(bill.getMobile())) {
			sql = sql+" and mobile like '%"+bill.getMobile()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getRoom())) {
			sql = sql+" and room like '%"+bill.getRoom()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getCommunity())) {
			if(!bill.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+bill.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(bill.getBillType())) {
			sql = sql+" and billType like '%"+bill.getBillType()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getState())) {
			sql = sql+" and state like '%"+bill.getState()+"%'";
		}
		if(StringUtil.isNotEmpty(bdate) && StringUtil.isNotEmpty(edate)) {
			sql = sql+" and date between '"+bdate+"' and '"+edate+"'";
		}
		
		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		mList = new ArrayList<Bill>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Bill>() {

			@Override
			public Bill mapRow(ResultSet rs, int num) throws SQLException {
				billId = rs.getString("billId");
				community = rs.getString("community");
				name = rs.getString("name");
				mobile = rs.getString("mobile");
				room = rs.getString("room");
				billType = rs.getString("billType");
				date = rs.getString("date");
				price = rs.getDouble("price");
				state = rs.getString("state");
				Bill bill = new Bill(billId, community, name, mobile, room, billType, date, price, state);
				return bill;
			}
		});
		return mList;
	}
	
	public int dataCount(Bill bill, String bdate, String edate) throws Exception {
		sql = "select count(*) as total from bill";
		if(StringUtil.isNotEmpty(bill.getMobile())) {
			sql = sql+" and mobile like '%"+bill.getMobile()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getRoom())) {
			sql = sql+" and room like '%"+bill.getRoom()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getCommunity())) {
			if(!bill.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+bill.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(bill.getBillType())) {
			sql = sql+" and billType like '%"+bill.getBillType()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getState())) {
			sql = sql+" and state like '%"+bill.getState()+"%'";
		}
		if(StringUtil.isNotEmpty(bdate) && StringUtil.isNotEmpty(edate)) {
			sql = sql+" and date between '"+bdate+"' and '"+edate+"'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 修改数据
	 * @param bill
	 * @return
	 */
	public boolean modifyData(Bill bill) {
		sql = "update bill set community=?,name=?,mobile=?,room=?,billType=?,date=?,price=? where billId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{bill.getCommunity(), bill.getName(), bill.getMobile(), bill.getRoom(), 
				bill.getBillType(), bill.getDate(), bill.getPrice(), bill.getBillId()}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 添加数据
	 * @param bill
	 * @return
	 */
	public boolean addData(Bill bill) {
		Long timeStamp = System.currentTimeMillis() / 1000;
		String billId = String.valueOf(timeStamp);
		sql = "insert into bill(billId,community,name,mobile,room,billType,date,price,state,selected) values(?,?,?,?,?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{billId, bill.getCommunity(), bill.getName(), bill.getMobile(), bill.getRoom(), 
				bill.getBillType(), bill.getDate(), bill.getPrice(), "未缴费", "false"}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param bill
	 * @return
	 */
	public boolean deleteBill(String billId) {
		sql = "delete from bill where billId in("+billId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

	/**
	 * 查询缴费单统计数据
	 * @param bill
	 * @param pageBean
	 * @param bdate
	 * @param edate
	 * @return
	 */
	public List<Bill> queryStatisticsData(Bill bill, PageBean pageBean,
			String bdate, String edate) {
		sql = "select community,name,mobile,room,billType,round(sum(price),2) as price,group_concat(date separator '，') as date,state from bill";
		if(StringUtil.isNotEmpty(bill.getMobile())) {
			sql = sql+" and mobile like '%"+bill.getMobile()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getRoom())) {
			sql = sql+" and room like '%"+bill.getRoom()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getCommunity())) {
			if(!bill.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+bill.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(bill.getBillType())) {
			sql = sql+" and billType like '%"+bill.getBillType()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getState())) {
			sql = sql+" and state like '%"+bill.getState()+"%'";
		}
		if(StringUtil.isNotEmpty(bdate) && StringUtil.isNotEmpty(edate)) {
			sql = sql+" and date between '"+bdate+"' and '"+edate+"'";
		}
		
		sql = sql+" group by name";
		//sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		mList = new ArrayList<Bill>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Bill>() {

			@Override
			public Bill mapRow(ResultSet rs, int num) throws SQLException {
				community = rs.getString("community");
				name = rs.getString("name");
				mobile = rs.getString("mobile");
				room = rs.getString("room");
				billType = rs.getString("billType");
				date = rs.getString("date");
				price = rs.getDouble("price");
				state = rs.getString("state");
				Bill bill = new Bill(billId, community, name, mobile, room, billType, date, price, state);
				return bill;
			}
		});
		return mList;
	}

	public int dataStatisticsCount(Bill bill, String bdate, String edate) {
		sql = "select count(name) as total from bill";
		if(StringUtil.isNotEmpty(bill.getMobile())) {
			sql = sql+" and mobile like '%"+bill.getMobile()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getRoom())) {
			sql = sql+" and room like '%"+bill.getRoom()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getCommunity())) {
			if(!bill.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+bill.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(bill.getBillType())) {
			sql = sql+" and billType like '%"+bill.getBillType()+"%'";
		}
		if(StringUtil.isNotEmpty(bill.getState())) {
			sql = sql+" and state like '%"+bill.getState()+"%'";
		}
		if(StringUtil.isNotEmpty(bdate) && StringUtil.isNotEmpty(edate)) {
			sql = sql+" and date between '"+bdate+"' and '"+edate+"'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

}
