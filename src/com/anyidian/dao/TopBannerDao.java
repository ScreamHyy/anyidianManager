package com.anyidian.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.PageBean;
import com.anyidian.model.TopBanner;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class TopBannerDao extends JdbcDaoSupport {
	private String sql;
	private List<TopBanner> mList;

	public TopBannerDao() {
		// TODO Auto-generated constructor stub
	}

	public List<TopBanner> queryData(TopBanner topBanner, PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from top_banner";
		if (StringUtil.isNotEmpty(topBanner.getCommunity())) {
			if (!topBanner.getCommunity().equals("全部")) {
				sql += " and community like '%" + topBanner.getCommunity()
						+ "%'";
			}
		}
		if (StringUtil.isNotEmpty(topBanner.getBannerType())) {
			if (!topBanner.getBannerType().equals("全部")) {
				sql += " and bannerType like '%" + topBanner.getBannerType()
						+ "%'";
			}
		}

		if (StringUtil.isNotEmpty(topBanner.getTitle())) {
			sql += " and title like '%" + topBanner.getTitle() + "%'";
		}
		sql += " order by id desc";
		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<TopBanner>();
		mList = getJdbcTemplate().query(sql, new RowMapper<TopBanner>() {

			@Override
			public TopBanner mapRow(ResultSet rs, int num) throws SQLException {
				TopBanner topbanner = new TopBanner();
				topbanner.setBannerId(rs.getString("bannerId"));
				topbanner.setCommunity(rs.getString("community"));
				topbanner.setBannerType(rs.getString("bannerType"));
				topbanner.setImage(rs.getString("image"));
				topbanner.setTitle(rs.getString("title"));
				topbanner.setIntroduce(rs.getString("introduce"));
				// TODO Auto-generated method stub
				return topbanner;
			}

		});
		return mList;
	}

	public List<TopBanner> queryTopBanner() {
		// TODO Auto-generated method stub
		sql = "select * from top_banner group by bannerType";
		mList = new ArrayList<TopBanner>();
		mList = getJdbcTemplate().query(sql, new RowMapper<TopBanner>() {

			@Override
			public TopBanner mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				TopBanner topbanner = new TopBanner();
				topbanner.setBannerId(rs.getString("bannerId"));
				topbanner.setCommunity(rs.getString("community"));
				topbanner.setBannerType(rs.getString("bannerType"));
				topbanner.setImage(rs.getString("image"));
				topbanner.setTitle(rs.getString("title"));
				topbanner.setIntroduce(rs.getString("introduce"));
				// TODO Auto-generated method stub
				return topbanner;
			}

		});
		return mList;
	}

	public TopBanner querDataById(String bannerId) {
		// TODO Auto-generated method stub
		sql = "select * from top_banner where bannerId=" + bannerId;

		return getJdbcTemplate().queryForObject(sql,
				new RowMapper<TopBanner>() {

					@Override
					public TopBanner mapRow(ResultSet rs, int num)
							throws SQLException {
						TopBanner topbanner = new TopBanner();
						topbanner.setBannerId(rs.getString("bannerId"));
						topbanner.setCommunity(rs.getString("community"));
						topbanner.setBannerType(rs.getString("bannerType"));
						topbanner.setImage(rs.getString("image"));
						topbanner.setTitle(rs.getString("title"));
						topbanner.setIntroduce(rs.getString("introduce"));
						// TODO Auto-generated method stub
						return topbanner;
					}

				});
	}

	public boolean updateData(TopBanner topbanner) {
		// TODO Auto-generated method stub
		sql = "update top_banner set community=?,bannerType=?,image=?,title=?,introduce=? where bannerId=?";
		int resultInt = getJdbcTemplate().update(
				sql,
				new Object[] { topbanner.getCommunity(),
						topbanner.getBannerType(), topbanner.getImage(),
						topbanner.getTitle(), topbanner.getIntroduce(),
						topbanner.getBannerId() });
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	public boolean addData(TopBanner topbanner) {
		// TODO Auto-generated method stub
		Long timeStamp = System.currentTimeMillis();
		topbanner.setBannerId(String.valueOf(timeStamp));
		sql = "insert into top_banner(bannerId,community,bannerType,image,title,introduce) values(?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(
				sql,
				new Object[] { topbanner.getBannerId(),
						topbanner.getCommunity(), topbanner.getBannerType(),
						topbanner.getImage(), topbanner.getTitle(),
						topbanner.getIntroduce() });
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteData(String delIds, String imagePath) {
		// TODO Auto-generated method stub
		File savePathFile = new File(imagePath);
		String[] delIdArray = delIds.split(",");
		for (int i = 0; i < delIdArray.length; i++) {
			String flagId = delIdArray[i];
			String sql = "select image from top_banner where bannerId=?";
			String image = getJdbcTemplate().queryForObject(sql,
					new Object[] { flagId }, String.class);
			if (StringUtil.isNotEmpty(image)) {
				String[] imageName = image.split("/");
				String name = imageName[imageName.length - 1];
				String flagPath = savePathFile + "\\resources\\images\\" + name;
				File localFile = new File(flagPath);
				// 判断文件是否存在
				boolean flag = false;
				flag = localFile.exists();
				if (flag) {
					if (localFile.isFile()) {
						boolean flag1 = false;
						flag1 = localFile.delete();
						if (flag1) {
							System.out.println("成功删除图片" + localFile.getName());
						}
					}
				}
			}

			if (i == delIdArray.length - 1) {
				sql = "delete from top_banner where bannerId in(" + delIds
						+ ")";
				System.out.println(delIds);
				int resultInt = getJdbcTemplate().update(sql, new Object[] {});
				if (resultInt > 0) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

}
