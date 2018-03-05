package com.anyidian.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Commodity;
import com.anyidian.model.Community;
import com.anyidian.model.PageBean;
import com.anyidian.model.Shop;
import com.anyidian.util.StringUtil;

public class CommodityDao extends JdbcDaoSupport {

	private String sql;
	@Autowired
	private ImagesDao imagesDao;

	public ImagesDao getImagesDao() {
		return imagesDao;
	}

	public void setImagesDao(ImagesDao imagesDao) {
		this.imagesDao = imagesDao;
	}

	/**
	 * 查询
	 * @param commodity
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<Commodity> queryCommodityData(Commodity commodity, PageBean pageBean) throws ParseException {
		//sql = "select c.*,GROUP_CONCAT(i.image) as images from commodity c left join images i on c.commodityId=i.imageId";
		sql = "select c.*,GROUP_CONCAT(i.image) as images,count(e.commodityId) as commentNum from "
				+ "commodity c left join images i on c.commodityId=i.imageId left join commodity_evaluate e on c.commodityId=e.commodityId";
		if(StringUtil.isNotEmpty(commodity.getShop())) {
			if(!commodity.getShop().equals("全部")) {
				sql = sql+" and shop like '%"+commodity.getShop()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(commodity.getState())) {
			if(!commodity.getState().equals("全部")) {
				sql = sql+" and state like '%"+commodity.getState()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(commodity.getName())) {
			sql = sql+" and name like '%"+commodity.getName()+"%'";
		}

		sql = sql+"  group by c.commodityId order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<Commodity> mList = new ArrayList<Commodity>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Commodity>() {

			@Override
			public Commodity mapRow(ResultSet rs, int num) throws SQLException {
				String commodityId = rs.getString("commodityId");
				String name = rs.getString("name");
				String supplier = rs.getString("supplier");
				String shop = rs.getString("shop");
				double nowPrice = rs.getDouble("nowPrice");
				double oldPrice = rs.getDouble("oldPrice");
				double supplyPrice = rs.getDouble("supplyPrice");
				double fee = rs.getDouble("fee");
				String introduce = rs.getString("introduce");
				int stockNum = rs.getInt("stockNum");
				String state = rs.getString("state");
				String images = rs.getString("images");
				String commentNum = rs.getString("commentNum");
				Commodity commodity = new Commodity(commodityId, name, supplier, shop, 
						nowPrice, oldPrice, supplyPrice, fee, introduce, stockNum, state, images, commentNum);
				return commodity;
			}
		});
		return mList;
	}

	public int dataCommodityCount(Commodity commodity) throws Exception {
		sql = "select count(*) as total from commodity";
		if(StringUtil.isNotEmpty(commodity.getShop())) {
			if(!commodity.getShop().equals("全部")) {
				sql = sql+" and shop like '%"+commodity.getShop()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(commodity.getState())) {
			if(!commodity.getState().equals("全部")) {
				sql = sql+" and state like '%"+commodity.getState()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(commodity.getName())) {
			sql = sql+" and name like '%"+commodity.getName()+"%'";
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 更改
	 * @param commodity
	 * @return
	 */
	public boolean updateCommodityData(Commodity commodity) {
		sql = "update commodity set name=?,supplier=?,shop=?,"
				+ "nowPrice=?,oldPrice=?,supplyPrice=?,fee=?,introduce=?,stockNum=?,state=? where commodityId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{commodity.getName(), commodity.getSupplier(), commodity.getShop(), commodity.getNowPrice(), 
				commodity.getOldPrice(), commodity.getSupplyPrice(), commodity.getFee(), commodity.getIntroduce(), commodity.getStockNum(), commodity.getState(), commodity.getCommodityId()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加
	 * @param commodity
	 * @return
	 */
	public boolean addCommodityData(Commodity commodity) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String commodityId = String.valueOf(timeStamp);
		sql = "insert into commodity(commodityId,name,supplier,shop,nowPrice,oldPrice,supplyPrice,fee,introduce,stockNum,state)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{commodityId, commodity.getName(), commodity.getSupplier(), commodity.getShop(), commodity.getNowPrice(), 
				commodity.getOldPrice(), commodity.getSupplyPrice(), commodity.getFee(), commodity.getIntroduce(), commodity.getStockNum(), commodity.getState()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param commodityId
	 * @param imagePath
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean deleteData(String commodityId, String imagePath) {
		File savePathFile = new File(imagePath);
		String[] mallIdArray = commodityId.split(",");
		for (int i = 0; i < mallIdArray.length; i++) {
			String flagId = mallIdArray[i];
			sql = "select image from images where imageId='"+flagId+"'";
			List<String> imagesList = new ArrayList<String>();
			imagesList = getJdbcTemplate().query(sql, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int num) throws SQLException {
					String image = rs.getString("image");
					return image;
				}
			});
			if(imagesList.size() > 0) {
				for(int j = 0; j<imagesList.size(); j++) {
					String imageUrl = imagesList.get(j);
					String[] iamgeName = imageUrl.split("/");
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
							if (!flag) {
								return false;
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
			if(i == mallIdArray.length - 1) {
				boolean success = imagesDao.deleteImages(commodityId);
				if(success) {
					boolean success1 = deleteCommodity(commodityId);
					if(success1) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Commodity queryById(String commodityId) {
		sql = "select c.*,GROUP_CONCAT(i.image) as images from commodity c left join"
				+ " images i on c.commodityId=i.imageId where commodityId='"+commodityId+"'";
		return getJdbcTemplate().queryForObject(sql, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				String commodityId = rs.getString("commodityId");
				String name = rs.getString("name");
				String supplier = rs.getString("supplier");
				String shop = rs.getString("shop");
				double nowPrice = rs.getDouble("nowPrice");
				double oldPrice = rs.getDouble("oldPrice");
				double supplyPrice = rs.getDouble("supplyPrice");
				double fee = rs.getDouble("fee");
				String introduce = rs.getString("introduce");
				int stockNum = rs.getInt("stockNum");
				String state = rs.getString("state");
				String images = rs.getString("images");
				Commodity commodity = new Commodity(commodityId, name, supplier, shop, 
						nowPrice, oldPrice, supplyPrice, fee, introduce, stockNum, state, images);
				return commodity;
			}
		});
	}

	/**
	 * 删除图片
	 * @param commodityId
	 * @param imagesUrlList 
	 * @return
	 */
	public boolean updateImages(String commodityId, String imagePath, List<String> imagesUrlList) {
		File savePathFile = new File(imagePath);
		sql = "select image from images where imageId='"+commodityId+"'";
		List<String> imagesList = new ArrayList<String>();
		imagesList = getJdbcTemplate().query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int num) throws SQLException {
				String image = rs.getString("image");
				return image;
			}
		});

		if(imagesList.size() > 0) {
			for(int i = 0; i< imagesList.size(); i++) {
				String imageUrl = imagesList.get(i);
				String[] iamgeName = imageUrl.split("/");
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
						if (!flag) {
							return false;
						}
					} else {
						return false;
					}
				}
				if(i == imagesList.size() -1) {
					boolean success = imagesDao.deleteImages(commodityId);
					if(success) {
						for(int j = 0; j < imagesUrlList.size(); j++) {
							boolean success1 = imagesDao.insertImages(commodityId, imagesUrlList.get(j));
							if(!success1) {
								return false;
							}
						}
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 删除商城
	 * @param commodityId
	 * @return
	 */
	public boolean deleteCommodity(String commodityId) {
		sql = "delete from commodity where commodityId in("+commodityId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 	查询店铺条目
	 * @return
	 */
	public List<Shop> queryShopItem() {
		sql = "select shopName from shop";
		List<Shop> shopList = new ArrayList<Shop>();
		Shop shop = new Shop();
		shop.setShopName("全部");
		shopList.add(shop);

		List<Shop> itemList = new ArrayList<Shop>();
		itemList = getJdbcTemplate().query(sql, new RowMapper<Shop>(){

			@Override
			public Shop mapRow(ResultSet rs, int num)
					throws SQLException {
				Shop shop = new Shop();
				String shopName = rs.getString("shopName");
				shop.setShopName(shopName);
				return shop;
			}
		});
		shopList.addAll(itemList);

		sql = "select name from life_circle group by name";
		List<Shop> lifeCircleList = new ArrayList<Shop>();
		lifeCircleList = getJdbcTemplate().query(sql, new RowMapper<Shop>(){

			@Override
			public Shop mapRow(ResultSet rs, int num)
					throws SQLException {
				Shop shop = new Shop();
				String name = rs.getString("name");
				shop.setShopName(name);
				return shop;
			}
		});

		shopList.addAll(lifeCircleList);
		return shopList;
	}

}
