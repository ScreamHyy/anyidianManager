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
import com.anyidian.model.LifeCircle;
import com.anyidian.model.LifeTypeItem;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class LifeCircleDao extends JdbcDaoSupport {
	private String lifeId;
	private String image;
	private String typeId;
	private String name;
	private String tel;
	private String address;
	private String detail;

	private String sql;

	/**
	 * 查询
	 * @param lifeCircle
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<LifeCircle> queryData(LifeCircle lifeCircle, PageBean pageBean) throws ParseException {
		sql = "select * from life_circle";
		if(StringUtil.isNotEmpty(lifeCircle.getName())) {
			sql = sql+" and name like '%"+lifeCircle.getName()+"%'";
		}
		if(StringUtil.isNotEmpty(lifeCircle.getTypeId())) {
			sql = sql+" and typeId like '%"+lifeCircle.getTypeId()+"%'";
		}

		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<LifeCircle> mList = new ArrayList<LifeCircle>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<LifeCircle>() {

			@Override
			public LifeCircle mapRow(ResultSet rs, int num) throws SQLException {
				lifeId = rs.getString("lifeId");
				name = rs.getString("name");
				typeId = rs.getString("typeId");
				tel = rs.getString("tel");
				address = rs.getString("address");
				detail = rs.getString("detail");
				image = rs.getString("image");
				String icon = queryIcon(typeId);
				String lifeType = queryType(typeId);
				LifeCircle lifeCircle = new LifeCircle(lifeId, icon, image, typeId, lifeType, name, tel, address, detail);
				return lifeCircle;
			}
		});
		return mList;
	}
	
	public int dataCount(LifeCircle lifeCircle) throws Exception {
		sql = "select count(*) as total from life_circle";
		if(StringUtil.isNotEmpty(lifeCircle.getName())) {
			sql = sql+" and name like '%"+lifeCircle.getName()+"%'";
		}
		if(StringUtil.isNotEmpty(lifeCircle.getTypeId())) {
			sql = sql+" and typeId like '%"+lifeCircle.getTypeId()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 通过id查询商家类型图标
	 * @param itemId
	 * @return
	 */
	public String queryIcon(String itemId) {
		sql = "select icon from life_circle_item where itemId=?";
		String icon = getJdbcTemplate().queryForObject(sql, new Object[] {itemId}, String.class);
		return icon;
	}
	
	/**
	 * 通过itemId查询上商家类型
	 * @param itemId
	 * @return
	 */
	public String queryType(String itemId) {
		sql = "select name from life_circle_item where itemId=?";
		String name = getJdbcTemplate().queryForObject(sql, new Object[] {itemId}, String.class);
		return name;
	}

	/**
	 * 通过typeName查询上商家
	 * @param typeName
	 * @return
	 */
	public String queryItemId(String typeName) {
		sql = "select itemId from life_circle_item where name=?";
		String itemId = getJdbcTemplate().queryForObject(sql, new Object[] {typeName}, String.class);
		return itemId;
	}
	
	/**
	 * 更改
	 * @param lifeCircle
	 * @return
	 */
	public boolean updateData(LifeCircle lifeCircle) {
		sql = "update life_circle set image=?,typeId=?,name=?,tel=?,address=?,detail=? where lifeId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{lifeCircle.getImage(), lifeCircle.getTypeId(), lifeCircle.getName(), 
				lifeCircle.getTel(), lifeCircle.getAddress(), lifeCircle.getDetail(), lifeCircle.getLifeId()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加
	 * @param lifeCircle
	 * @return
	 */
	public boolean addData(LifeCircle lifeCircle) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String lifeId = String.valueOf(timeStamp);
		sql = "insert into life_circle(lifeId,image,typeId,name,tel,address,detail) values(?,?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{lifeId, lifeCircle.getImage(), lifeCircle.getTypeId(), lifeCircle.getName(), 
				lifeCircle.getTel(), lifeCircle.getAddress(), lifeCircle.getDetail()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param lifeId
	 * @param imagePath
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean deleteData(String lifeId, String imagePath) {
		File savePathFile = new File(imagePath);
		String[] noticeIdArray = lifeId.split(",");
		for (int i = 0; i < noticeIdArray.length; i++) {
			String flagId = noticeIdArray[i];
			sql = "select image from life_circle where lifeId=?"; 
			String image = getJdbcTemplate().queryForObject(sql, new Object[] {flagId}, String.class);
			if(StringUtil.isNotEmpty(image)) {
				String[] iamgeName = image.split("/");
				String name = iamgeName[iamgeName.length - 1];
				String flagPath = savePathFile +  "\\resources\\images\\" + name;
				File localFile = new File(flagPath);
				// 判断文件是否存在
				boolean falg = false;
				falg = localFile.exists();
				if (falg) {
					if (localFile.isFile()) {
						boolean flag = false;
						flag = localFile.delete();
						if (flag) {
							System.out.println("成功删除图片：" + localFile.getName());
						}
					}
				}
			}
			if(i == noticeIdArray.length - 1) {
				sql = "delete from life_circle where lifeId in("+lifeId+")";
				int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
				if (resultInt > 0) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LifeCircle queryById(String lifeId) {
		sql = "select * from life_circle where lifeId='"+lifeId+"'";
		return getJdbcTemplate().queryForObject(sql, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				LifeCircle lifeCircle = new LifeCircle();
				lifeCircle.setLifeId(rs.getString("lifeId"));
				lifeCircle.setImage(rs.getString("image"));
				lifeCircle.setTypeId(rs.getString("typeId"));
				lifeCircle.setName(rs.getString("name"));
				lifeCircle.setTel(rs.getString("tel"));
				lifeCircle.setAddress(rs.getString("address"));
				lifeCircle.setDetail(rs.getString("detail"));
				return lifeCircle;
			}
		});
	}
	
	/**
	 * 查询商家类型条目详情
	 * @return
	 */
	public List<LifeTypeItem> queryLifeTypeItem(PageBean pageBean) {
		sql = "select * from life_circle_item";
		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<LifeTypeItem> mList = new ArrayList<LifeTypeItem>();
		mList = getJdbcTemplate().query(sql, new RowMapper<LifeTypeItem>(){
			@Override
			public LifeTypeItem mapRow(ResultSet rs, int num)
					throws SQLException {
				String itemId = rs.getString("itemId");
				String icon = rs.getString("icon");
				String name = rs.getString("name");
				LifeTypeItem item = new LifeTypeItem(itemId, icon, name);
				return item;
			}
		});
		return mList;
	}
	
	/**
	 * 查询商家类型条目
	 * @return
	 */
	public List<LifeTypeItem> queryLifeTypeItemTwo() {
		sql = "select * from life_circle_item";
		List<LifeTypeItem> mList = new ArrayList<LifeTypeItem>();
		mList = getJdbcTemplate().query(sql, new RowMapper<LifeTypeItem>(){
			@Override
			public LifeTypeItem mapRow(ResultSet rs, int num)
					throws SQLException {
				String itemId = rs.getString("itemId");
				String icon = rs.getString("icon");
				String name = rs.getString("name");
				LifeTypeItem item = new LifeTypeItem(itemId, icon, name);
				return item;
			}
		});
		return mList;
	}
	
	public int dataItemCount() throws Exception {
		sql = "select count(*) as total from life_circle_item";
		int total = getJdbcTemplate().queryForInt(sql);
		return total;
	}

	/**
	 * 添加商家类型
	 * @param lifeTypeItem
	 * @return
	 */
	public boolean addItemData(LifeTypeItem lifeTypeItem) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String itemId = String.valueOf(timeStamp);
		sql = "insert into life_circle_item(itemId,icon,name) values(?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{itemId, lifeTypeItem.getIcon(), lifeTypeItem.getName()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 更改商家类型
	 * @param lifeType
	 * @return
	 */
	public boolean updateItemData(LifeTypeItem lifeTypeItem) {
		sql = "update life_circle_item set icon=?,name=? where itemId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{lifeTypeItem.getIcon(), lifeTypeItem.getName(), lifeTypeItem.getItemId()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除商家类型
	 * @param delIds
	 * @return
	 */
	public boolean deleteItemData(String delIds) {
		sql = "delete from life_circle_item where itemId in("+delIds+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}
	
}
