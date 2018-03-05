package com.anyidian.dao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.NewStatus;
import com.anyidian.model.PageBean;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class NewStatusDao extends JdbcDaoSupport {
	private String sql;
	private List<NewStatus> mList;

	/**
	 * 
	 * 查询
	 * 
	 * @author ScreamHyy
	 * @param newStatus
	 * @param pageBean
	 * @return mList
	 * 
	 * **/

	public List<NewStatus> queryData(NewStatus newStatus, PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from new_status";
		if (StringUtil.isNotEmpty(newStatus.getStatusType())) {
			sql += " and statusType like '%" + newStatus.getStatusType() + "%'";
		}
		if (StringUtil.isNotEmpty(newStatus.getDate())) {
			sql += " and date like '%" + newStatus.getDate() + "%'";
		}
		if (StringUtil.isNotEmpty(newStatus.getTitle())) {
			sql += " and title like '%" + newStatus.getTitle() + "%'";
		}

		sql += " order by id desc";

		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}

		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<NewStatus>();
		mList = getJdbcTemplate().query(sql, new RowMapper<NewStatus>() {

			@Override
			public NewStatus mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				NewStatus newstatus = new NewStatus();
				newstatus.setStatusId(rs.getString("statusId"));
				newstatus.setStatusType(rs.getString("statusType"));
				newstatus.setImage(rs.getString("image"));
				newstatus.setTitle(rs.getString("title"));
				newstatus.setDate(rs.getString("date"));
				newstatus.setDetail(rs.getString("detail"));
				return newstatus;
			}

		});

		return mList;
	}

	/**
	 * 
	 * 删除最新资讯
	 * 
	 * @author ScreamHyy
	 * @param delIds
	 * @param imagePath
	 * @return
	 * 
	 * **/

	public boolean deleteData(String delIds, String imagePath) {
		// TODO Auto-generated method stub
		File savePathFile = new File(imagePath);
		String[] delIdArray = delIds.split(",");
		for (int i = 0; i < delIdArray.length; i++) {
			String flagId = delIdArray[i];
			String sql = "select image from new_status where statusId=?";
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
				sql = "delete from new_status where statusId in(" + delIds
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

	/**
	 * 
	 * 根据资讯id来查询
	 *
	 * @author ScreamHyy
	 * @param statusId
	 * @return newstatus
	 * 
	 **/

	@SuppressWarnings("unchecked")
	public NewStatus queryNewStatusById(String statusId) {
		// TODO Auto-generated method stub
		sql = "select * from new_status where statusId=" + statusId;
		return getJdbcTemplate().queryForObject(sql, new RowMapper<NewStatus>() {

			@Override
			public NewStatus mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				NewStatus newstatus = new NewStatus();
				newstatus.setStatusId(rs.getString("statusId"));
				newstatus.setStatusType(rs.getString("statusType"));
				newstatus.setImage(rs.getString("image"));
				newstatus.setTitle(rs.getString("title"));
				newstatus.setDate(rs.getString("date"));
				newstatus.setDetail(rs.getString("detail"));
				return newstatus;
			}

		});
	}

	/**
	 * 
	 * 更改资讯
	 * 
	 * @author ScreamHyy
	 * @param newstatus
	 * @return
	 *
	 **/

	public boolean updateData(NewStatus newstatus) {
		// TODO Auto-generated method stub
		sql = "update new_status set image=?,title=?,detail=? where statusId=?";
		int resultInt = getJdbcTemplate().update(
				sql,
				new Object[] { newstatus.getImage(), newstatus.getTitle(),
						newstatus.getDetail(), newstatus.getStatusId() });
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	public boolean addData(NewStatus newstatus,String statusType) {
		// TODO Auto-generated method stub
		Date day = new Date();
		String date = DateUtil.dateToString(day, "yyyy-MM-dd");
		Long timeStamp = System.currentTimeMillis();
		newstatus.setDate(date);
		newstatus.setStatusId(String.valueOf(timeStamp));
		newstatus.setStatusType(statusType);
		sql = "insert into new_status(statusId,statusType,image,title,date,detail) values(?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(
				sql,
				new Object[] { newstatus.getStatusId(),
						newstatus.getStatusType(), newstatus.getImage(),
						newstatus.getTitle(), newstatus.getDate(),
						newstatus.getDetail() });
		if(resultInt > 0){
			return true;
		}
		return false;
	}
}
