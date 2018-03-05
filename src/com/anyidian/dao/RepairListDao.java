package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.PageBean;
import com.anyidian.model.RepairItem;
import com.anyidian.model.RepairList;
import com.anyidian.util.StringUtil;

public class RepairListDao extends JdbcDaoSupport {

	private String sql;
	private String listId;
	private String state;
	private String mobile;
	private String name;
	private String address;
	private String date;
	private String repairType;
	private String introduce;
	private String image;
	private String community;
	private String repairmanName;
	private double cost;
	private String evaluate;

	private int qualityScore;
	private int attitudeScore;
	private int timeScore;
	private int priceScore;
	private List<RepairList> mList;

	/**
	 * 查询报修单信息
	 * @param data
	 * @param pageBean
	 * @return
	 */
	public List<RepairList> queryData(RepairList repairList, PageBean pageBean) {

		sql = "select * from repair_list";
		if(StringUtil.isNotEmpty(repairList.getCommunity())) {
			if(!repairList.getCommunity().equals("全部")){
				sql = sql+" "+"and community like '%"+repairList.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(repairList.getRepairType())) {
			if(!repairList.getRepairType().equals("全部")){
				sql = sql+" "+"and repairType like '%"+repairList.getRepairType()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(repairList.getState())) {
			sql = sql+" "+"and state like '%"+repairList.getState()+"%'";
		}
		if(StringUtil.isNotEmpty(repairList.getMobile())) {
			sql = sql+" "+"and mobile like '%"+repairList.getMobile()+"%'";
		}
		sql = sql+" "+"order by id desc";
		if(pageBean != null){
			sql = sql+" "+"limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		mList = new ArrayList<RepairList>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<RepairList>() {

			@Override
			public RepairList mapRow(ResultSet rs, int num) throws SQLException {
				listId = rs.getString("listId");
				state = rs.getString("state");
				mobile = rs.getString("mobile");
				name = rs.getString("name");
				address = rs.getString("address");
				date = rs.getString("date");
				repairType = rs.getString("repairType");
				introduce = rs.getString("introduce");
				image = rs.getString("image");
				community = rs.getString("community");
				repairmanName = rs.getString("repairmanName");
				cost = rs.getDouble("cost");
				evaluate = rs.getString("evaluate");
				qualityScore = rs.getInt("qualityScore");
				attitudeScore = rs.getInt("attitudeScore");
				timeScore = rs.getInt("timeScore");
				priceScore = rs.getInt("priceScore");
				RepairList repairList = new RepairList(listId, state, mobile, name, address, date, repairType, introduce, image, community, 
						repairmanName, cost, evaluate, qualityScore, attitudeScore, timeScore, priceScore);
				return repairList;
			}
		});
		return mList;
	}

	public int dataCount(RepairList repairList) throws Exception {
		sql = "select count(*) as total from repair_list";
		if(StringUtil.isNotEmpty(repairList.getCommunity())) {
			if(!repairList.getCommunity().equals("全部")){
				sql = sql+" "+"and community like '%"+repairList.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(repairList.getRepairType())) {
			if(!repairList.getRepairType().equals("全部")){
				sql = sql+" "+"and repairType like '%"+repairList.getRepairType()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(repairList.getState())) {
			sql = sql+" "+"and state like '%"+repairList.getState()+"%'";
		}
		if(StringUtil.isNotEmpty(repairList.getMobile())) {
			sql = sql+" "+"and mobile like '%"+repairList.getMobile()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 派遣维修人
	 * @param listId
	 * @param repairmanId
	 * @param remarks
	 * @return 
	 */
	@SuppressWarnings("unused")
	public boolean addRepairMan(String listId, String repairmanId, String remarks) {
		Date day = new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String sendTime = df.format(day);
		long timeStamp = System.currentTimeMillis() / 1000;
		String repairworkId =  String.valueOf(timeStamp);
		String name;
		sql = "insert into repair_work(repairworkId,repairId,repairmanId,remarks,sendTime) values(?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{repairworkId, listId, repairmanId, remarks, sendTime}); 
		if (resultInt > 0) {  
			sql = "select name from repair_man where repairmanId=?"; 
			name = getJdbcTemplate().queryForObject(sql,  new Object[] {repairmanId}, String.class);
			if(StringUtil.isNotEmpty(name)) {
				sql = "update repair_list set state=?,repairmanName=? where listId=?";  
				int flag = getJdbcTemplate().update(sql, new Object[]{"维修派单", name, listId}); 
				if (flag > 0) {
					return true;
				}
				return false;
			}
			return false;
		}  
		return false;  
	}

	/**
	 * 添加维修费用和结果
	 * @param listId
	 * @param cost
	 * @param result
	 * @return
	 */
	public boolean addRepairResult(String repairId, double cost, String result) {
		sql = "update repair_list set cost=?,state=? where listId=?"; 
		int resultInt = getJdbcTemplate().update(sql, new Object[]{cost, "待评价", repairId}); 
		if (resultInt > 0) {
			sql = "update repair_work set result=? where repairId=?";
			int flag = getJdbcTemplate().update(sql, new Object[]{result, repairId}); 
			if (flag > 0) {  
				return true;  
			}
			return false;
		}  
		return false; 
	}

	/**
	 * 查询维修条目
	 * @return
	 * @throws SQLException
	 */
	public List<RepairItem> queryRepairItem() throws SQLException {
		sql = "select * from repair_item";
		List<RepairItem> mList = new ArrayList<RepairItem>();

		mList = getJdbcTemplate().query(sql, new RowMapper<RepairItem>() {

			@Override
			public RepairItem mapRow(ResultSet rs, int num) throws SQLException {
				String itemId = rs.getString("itemId");
				String repairType = rs.getString("repairType");
				RepairItem repairItem = new RepairItem(itemId, repairType);
				return repairItem;
			}
		});
		return mList;
	}

	public List<RepairItem> queryRepairItemData(PageBean pageBean) {
		sql = "select * from repair_item order by id desc";
		if(pageBean != null){
			sql = sql+" "+"limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<RepairItem> mList = new ArrayList<RepairItem>();

		mList = getJdbcTemplate().query(sql, new RowMapper<RepairItem>() {

			@Override
			public RepairItem mapRow(ResultSet rs, int num) throws SQLException {
				String itemId = rs.getString("itemId");
				String repairType = rs.getString("repairType");
				String price = rs.getString("price");
				String remark = rs.getString("remark");
				RepairItem repairItem = new RepairItem(itemId, repairType, price, remark);
				return repairItem;
			}
		});
		return mList;
	}

	public int RepairItemCount() {
		sql = "select count(*) as total from repair_item";
		int total = getJdbcTemplate().queryForInt(sql);
		return total;
	}

	/**
	 * 更改维修条目
	 * @param repairItem
	 * @return
	 */
	public boolean modifyRepairItem(RepairItem repairItem) {
		sql = "update repair_item set repairType=?,price=?,remark=? where itemId=?"; 
		int resultInt = getJdbcTemplate().update(sql, new Object[]{repairItem.getRepairType(), repairItem.getPrice(), repairItem.getRemark(), repairItem.getItemId()}); 
		if (resultInt > 0) {
			return true;  
		}  
		return false; 
	}

	/**
	 * 添加维修条目
	 * @param repairItem
	 * @return
	 */
	public boolean addRepairItem(RepairItem repairItem) {
		Long timeStamp = System.currentTimeMillis() / 1000;
		String itemId = String.valueOf(timeStamp);
		sql = "insert into repair_item(itemId,repairType,price,remark) values(?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{itemId, repairItem.getRepairType(), repairItem.getPrice(), repairItem.getRemark()}); 
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
	public boolean deleteRepairItem(String itemId) {
		sql = "delete from repair_item where itemId in("+itemId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

}
