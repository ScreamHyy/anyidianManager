package com.anyidian.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.NewHouse;
import com.anyidian.model.NewStatus;
import com.anyidian.model.PageBean;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class NewHouseDao extends JdbcDaoSupport {
	private NewHouse newHouse;
	private String sql;
	private List<NewHouse> mList;

	public List<NewHouse> queryData(NewHouse newHouse, PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from new_house";
		if (StringUtil.isNotEmpty(newHouse.getHouseId())) {
			sql += " and houseId like '%" + newHouse.getHouseId() + "%'";
		}
		if (StringUtil.isNotEmpty(newHouse.getTitle())) {
			sql += " and title like '%" + newHouse.getTitle() + "%'";
		}
		sql += " order by id desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}
		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<NewHouse>();
		mList = getJdbcTemplate().query(sql, new RowMapper<NewHouse>() {

			@Override
			public NewHouse mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				NewHouse newhouse = new NewHouse();
				newhouse.setHouseId(rs.getString("houseId"));
				newhouse.setImage(rs.getString("image"));
				newhouse.setTitle(rs.getString("title"));
				newhouse.setIntroduce(rs.getString("introduce"));
				newhouse.setDetail(rs.getString("detail"));
				return newhouse;
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
			String sql = "select image from new_house where houseId=?";
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
				sql = "delete from new_house where houseId in(" + delIds + ")";
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
	public NewHouse queryNewHouseById(String houseId) {
		// TODO Auto-generated method stub
		sql = "select * from new_house where houseId=" + houseId;
		return getJdbcTemplate().queryForObject(sql, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				NewHouse newhouse = new NewHouse();
				newhouse.setHouseId(rs.getString("houseId"));
				newhouse.setImage(rs.getString("image"));
				newhouse.setTitle(rs.getString("title"));
				newhouse.setIntroduce(rs.getString("introduce"));
				newhouse.setDetail(rs.getString("detail"));
				return newhouse;
			}

		});
	}

	public boolean updateData(NewHouse house) {
		// TODO Auto-generated method stub
		sql = "update new_house set image=?,title=?,introduce=?,detail=? where houseId=?";
		int resultInt = getJdbcTemplate().update(
				sql,
				new Object[] { house.getImage(), house.getTitle(),
						house.getIntroduce(), house.getDetail(),house.getHouseId() });
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	public boolean addData(NewHouse house) {
		// TODO Auto-generated method stub
		Long timeStamp = System.currentTimeMillis();
		house.setHouseId(String.valueOf(timeStamp));
		sql = "insert into new_house(houseId,image,title,introduce,detail) values(?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(
				sql,
				new Object[] { house.getHouseId(), house.getImage(),
						house.getTitle(), house.getIntroduce(),
						house.getDetail() });
		if(resultInt > 0){
			return true;
		}
		return false;
	}

}
