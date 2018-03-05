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

import com.anyidian.model.PageBean;
import com.anyidian.model.IntegralMall;
import com.anyidian.util.StringUtil;

public class IntegralMallDao extends JdbcDaoSupport {

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
	 * @param integralMall
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<IntegralMall> queryIntegralMallData(IntegralMall integralMall, PageBean pageBean) throws ParseException {
		sql = "select m.*,GROUP_CONCAT(i.image) as images from integral_mall m left join images i on m.mallId=i.imageId";
		if(StringUtil.isNotEmpty(integralMall.getMall())) {
			sql = sql+" and mall like '%"+integralMall.getMall()+"%'";
		}
		if(StringUtil.isNotEmpty(integralMall.getCommunity())) {
			if(!integralMall.getCommunity().equals("全部")) {
				sql = sql+" and community like '%"+integralMall.getCommunity()+"%'";
			}
		}

		sql = sql+"  group by m.mallId order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<IntegralMall> mList = new ArrayList<IntegralMall>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<IntegralMall>() {

			@Override
			public IntegralMall mapRow(ResultSet rs, int num) throws SQLException {
				String mallId = rs.getString("mallId");
				String community = rs.getString("community");
				String title = rs.getString("title");
				String mall = rs.getString("mall");
				int integral = rs.getInt("integral");
				double marketPrice = rs.getDouble("marketPrice");
				String introduce = rs.getString("introduce");
				String images = rs.getString("images");
				IntegralMall integralMall = new IntegralMall(mallId, community, title, mall, integral, marketPrice, introduce, images);
				return integralMall;
			}
		});
		return mList;
	}

	public int dataIntegralMallCount(IntegralMall integralMall) throws Exception {
		sql = "select count(*) as total from integral_mall";
		if(StringUtil.isNotEmpty(integralMall.getMall())) {
			sql = sql+" and mall like '%"+integralMall.getMall()+"%'";
		}
		if(StringUtil.isNotEmpty(integralMall.getCommunity())) {
			if(!integralMall.getCommunity().equals("全部")) {
				sql = sql+" and community like '%"+integralMall.getCommunity()+"%'";
			}
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	/**
	 * 更改
	 * @param integralMall
	 * @return
	 */
	public boolean updateIntegralMallData(IntegralMall integralMall) {
		sql = "update integral_mall set community=?,title=?,mall=?,integral=?,marketPrice=?,introduce=? where mallId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{integralMall.getCommunity(), integralMall.getTitle(), integralMall.getMall(), 
				integralMall.getIntegral(), integralMall.getMarketPrice(), integralMall.getIntroduce(), integralMall.getMallId()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加
	 * @param integralMall
	 * @return
	 */
	public boolean addIntegralMallData(IntegralMall integralMall) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String mallId = String.valueOf(timeStamp);
		sql = "insert into integral_mall(mallId,community,title,mall,integral,marketPrice,introduce) values(?,?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{mallId, integralMall.getCommunity(), integralMall.getTitle(), 
				integralMall.getMall(), integralMall.getIntegral(), integralMall.getMarketPrice(), integralMall.getIntroduce()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param mallId
	 * @param imagePath
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean deleteData(String mallId, String imagePath) {
		File savePathFile = new File(imagePath);
		String[] mallIdArray = mallId.split(",");
		for (int i = 0; i < mallIdArray.length; i++) {
			String flagId = mallIdArray[i];
			//sql = "select image from images where imageId=?"; 
			//String image = getJdbcTemplate().queryForObject(sql, new Object[] {flagId}, String.class);
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
				boolean success = imagesDao.deleteImages(mallId);
				if(success) {
					boolean success1 = deleteIntegralMall(mallId);
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
	public IntegralMall queryById(String mallId) {
		sql = "select m.*,GROUP_CONCAT(i.image) as images from integral_mall m left join images i on m.mallId=i.imageId where mallId='"+mallId+"'";
		return getJdbcTemplate().queryForObject(sql, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				IntegralMall integralMall = new IntegralMall();
				integralMall.setMallId(rs.getString("mallId"));
				integralMall.setCommunity(rs.getString("community"));
				integralMall.setTitle(rs.getString("title"));
				integralMall.setMall(rs.getString("mall"));
				integralMall.setIntegral(rs.getInt("integral"));
				integralMall.setMarketPrice(rs.getDouble("marketPrice"));
				integralMall.setIntroduce(rs.getString("introduce"));
				integralMall.setIntroduce(rs.getString("introduce"));
				integralMall.setImages(rs.getString("images"));
				return integralMall;
			}
		});
	}

	/**
	 * 删除图片
	 * @param mallId
	 * @param imagesUrlList 
	 * @return
	 */
	public boolean updateImages(String mallId, String imagePath, List<String> imagesUrlList) {
		File savePathFile = new File(imagePath);
		sql = "select image from images where imageId='"+mallId+"'";
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
							/*boolean success = imagesDao.deleteImages(mallId);
							if(success) {
								for(int j = 0; j < imagesUrlList.size(); j++) {
									boolean success1 = imagesDao.insertImages(mallId, imagesUrlList.get(j));
									if(!success1) {
										return false;
									}
								}
							}*/
							return false;
						}
					} else {
						return false;
					}
				}
				if(i == imagesList.size() -1) {
					boolean success = imagesDao.deleteImages(mallId);
					if(success) {
						for(int j = 0; j < imagesUrlList.size(); j++) {
							boolean success1 = imagesDao.insertImages(mallId, imagesUrlList.get(j));
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
	 * @param mallId
	 * @return
	 */
	public boolean deleteIntegralMall(String mallId) {
		sql = "delete from integral_mall where mallId in("+mallId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

}
