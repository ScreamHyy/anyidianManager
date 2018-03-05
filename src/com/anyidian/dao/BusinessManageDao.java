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
import com.anyidian.model.Business;
import com.anyidian.model.LifeTypeItem;
import com.anyidian.model.PageBean;
import com.anyidian.model.Shop;
import com.anyidian.util.StringUtil;

public class BusinessManageDao extends JdbcDaoSupport {

	private String sql;
	
	/**
	 * 查询
	 * @param shop
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<Shop> queryShopData(Shop shop, PageBean pageBean) throws ParseException {
		sql = "select * from shop";
		if(StringUtil.isNotEmpty(shop.getShopName())) {
			sql = sql+" and shopName like '%"+shop.getShopName()+"%'";
		}
		if(StringUtil.isNotEmpty(shop.getCommunity())) {
			if(!shop.getCommunity().equals("全部")) {
				sql = sql+" and community like '%"+shop.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(shop.getBusinessId())) {
			sql = sql+" and businessId like '%"+shop.getBusinessId()+"%'";
		}

		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<Shop> mList = new ArrayList<Shop>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Shop>() {

			@Override
			public Shop mapRow(ResultSet rs, int num) throws SQLException {
				String shopId = rs.getString("shopId");
				String community = rs.getString("community");
				String businessId = rs.getString("businessId");
				String image = rs.getString("image");
				String shopName = rs.getString("shopName");
				String tel = rs.getString("tel");
				String address = rs.getString("address");
				String businessIcon = queryIcon(businessId);
				String businessName = queryName(businessId);
				Shop shop = new Shop(shopId, community, businessId, businessIcon, businessName, image, shopName, tel, address);
				return shop;
			}
		});
		return mList;
	}
	
	/**
	 * 查询商家类型条目
	 * @return
	 */
	public List<Business> queryBusinessItem() {
		sql = "select * from business";
		List<Business> mList = new ArrayList<Business>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Business>(){
			@Override
			public Business mapRow(ResultSet rs, int num)
					throws SQLException {
				String businessId = rs.getString("businessId");
				String icon = rs.getString("icon");
				String name = rs.getString("name");
				Business item = new Business(businessId, icon, name);
				return item;
			}
		});
		return mList;
	}
	
	public int dataShopCount(Shop shop) throws Exception {
		sql = "select count(*) as total from shop";
		if(StringUtil.isNotEmpty(shop.getShopName())) {
			sql = sql+" and shopName like '%"+shop.getShopName()+"%'";
		}
		if(StringUtil.isNotEmpty(shop.getBusinessId())) {
			sql = sql+" and businessId like '%"+shop.getBusinessId()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 通过id查询商家类型图标
	 * @param businessId
	 * @return
	 */
	public String queryIcon(String businessId) {
		sql = "select icon from business where businessId=?";
		String icon = getJdbcTemplate().queryForObject(sql, new Object[] {businessId}, String.class);
		return icon;
	}
	
	/**
	 * 通过businessId查询上商家类型
	 * @param businessId
	 * @return
	 */
	public String queryName(String businessId) {
		sql = "select name from business where businessId=?";
		String name = getJdbcTemplate().queryForObject(sql, new Object[] {businessId}, String.class);
		return name;
	}

	/**
	 * 通过businessName查询上商家
	 * @param businessName
	 * @return
	 */
	public String queryBusinessId(String businessName) {
		sql = "select businessId from business where name=?";
		String icon = getJdbcTemplate().queryForObject(sql, new Object[] {businessName}, String.class);
		return icon;
	}
	
	/**
	 * 更改
	 * @param lifeCircle
	 * @return
	 */
	public boolean updateShopData(Shop shop) {
		sql = "update shop set community=?,businessId=?,image=?,shopName=?,tel=?,address=? where shopId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{shop.getCommunity(), 
				shop.getBusinessId(), shop.getImage(), shop.getShopName(), shop.getTel(), shop.getAddress(), shop.getShopId()});
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
	public boolean addShopData(Shop shop) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String shopId = String.valueOf(timeStamp);
		sql = "insert into shop(shopId,community,businessId,image,tel,shopName,address) values(?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{shopId, shop.getClass(),
				shop.getBusinessId(), shop.getImage(), shop.getShopName(), shop.getTel(), shop.getAddress()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param shopId
	 * @param imagePath
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean deleteData(String shopId, String imagePath) {
		File savePathFile = new File(imagePath);
		String[] noticeIdArray = shopId.split(",");
		for (int i = 0; i < noticeIdArray.length; i++) {
			String flagId = noticeIdArray[i];
			sql = "select image from shop where shopId=?"; 
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
				sql = "delete from shop where shopId in("+shopId+")";
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
	public Shop queryById(String shopId) {
		sql = "select * from shop where shopId='"+shopId+"'";
		return getJdbcTemplate().queryForObject(sql, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				Shop shop = new Shop();
				shop.setShopId(rs.getString("shopId"));
				shop.setCommunity(rs.getString("community"));
				shop.setBusinessId(rs.getString("businessId"));
				shop.setImage(rs.getString("image"));
				shop.setShopName(rs.getString("shopName"));
				shop.setTel(rs.getString("tel"));
				shop.setAddress(rs.getString("address"));
				return shop;
			}
		});
	}

	/**
	 * 查询商家类型条目
	 * @return
	 */
	public List<Business> queryBusiness(PageBean pageBean) {
		sql = "select * from business order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<Business> mList = new ArrayList<Business>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Business>(){
			@Override
			public Business mapRow(ResultSet rs, int num)
					throws SQLException {
				String businessId = rs.getString("businessId");
				String icon = rs.getString("icon");
				String name = rs.getString("name");
				Business item = new Business(businessId, icon, name);
				return item;
			}
		});
		return mList;
	}
	
	public int dataBusinessCount() throws Exception {
		sql = "select count(*) as total from business";
		int total = getJdbcTemplate().queryForInt(sql);
		return total;
	}

	/**
	 * 添加商家类型
	 * @param business
	 * @return
	 */
	public boolean addItemData(Business business) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String businessId = String.valueOf(timeStamp);
		sql = "insert into business(businessId,icon,name) values(?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{businessId, business.getIcon(), business.getName()});
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
	public boolean updateItemData(Business business) {
		sql = "update business set icon=?,name=? where businessId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{business.getIcon(), business.getName(), business.getBusinessId()});
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
	public boolean deleteBusinessType(String delIds) {
		sql = "delete from business where businessId in("+delIds+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {  
			return true;  
		}
		return false;
	}

}
