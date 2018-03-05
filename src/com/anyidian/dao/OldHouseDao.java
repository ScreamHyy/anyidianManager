package com.anyidian.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.OldHouse;
import com.anyidian.model.OldHouse;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class OldHouseDao extends JdbcDaoSupport {
	private OldHouse oldHouse;
	private String sql;
	private List<OldHouse> mList;

	public List<OldHouse> queryData(OldHouse oldHouse, PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from old_house";
		if (StringUtil.isNotEmpty(oldHouse.getHouseId())) {
			sql += " and houseId like '%" + oldHouse.getHouseId() + "%'";
		}
		if (StringUtil.isNotEmpty(oldHouse.getHouseType())) {
			sql += " and houseType like '%" + oldHouse.getHouseType() + "%'";
		}
		if (StringUtil.isNotEmpty(oldHouse.getWhere())) {
			sql += " and `where` like '%" + oldHouse.getWhere() + "%'";
		}
		if (StringUtil.isNotEmpty(oldHouse.getArea())) {
			sql += " and area like '%" + oldHouse.getArea() + "%'";
		}
		sql += " order by id desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}
		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<OldHouse>();
		mList = getJdbcTemplate().query(sql, new RowMapper<OldHouse>() {

			@Override
			public OldHouse mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				OldHouse oldHouse = new OldHouse();
				oldHouse.setHouseId(rs.getString("houseId"));
				oldHouse.setHouseType(rs.getString("houseType"));
				oldHouse.setWhere(rs.getString("where"));
				oldHouse.setImage(rs.getString("image"));
				oldHouse.setTitle(rs.getString("title"));
				oldHouse.setMobile(rs.getString("mobile"));
				oldHouse.setArea(rs.getString("area"));
				oldHouse.setPrice(rs.getString("price"));
				oldHouse.setIntroduce(rs.getString("introduce"));
				return oldHouse;
			}

		});
		return mList;
	}

	public boolean deleteData(String delIds, String imagePath) {
		// TODO Auto-generated method stub
		File savePathFile = new File(imagePath);
		String[] delIdArray = delIds.split(",");
		for (int i = 0; i < delIdArray.length; i++) {
			String flagId = delIdArray[i];
			String sql = "select image from old_house where houseId=?";
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
				sql = "delete from old_house where houseId in(" + delIds + ")";
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public OldHouse queryOldHouseById(String houseId) {
		// TODO Auto-generated method stub
		sql = "select * from old_house where houseId=" + houseId;
		return getJdbcTemplate().queryForObject(sql, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				OldHouse oldHouse = new OldHouse();
				oldHouse.setHouseId(rs.getString("houseId"));
				oldHouse.setHouseType(rs.getString("houseType"));
				oldHouse.setWhere(rs.getString("where"));
				oldHouse.setImage(rs.getString("image"));
				oldHouse.setTitle(rs.getString("title"));
				oldHouse.setMobile(rs.getString("mobile"));
				oldHouse.setArea(rs.getString("area"));
				oldHouse.setPrice(rs.getString("price"));
				oldHouse.setIntroduce(rs.getString("introduce"));
				return oldHouse;
			}

		});
	}


}