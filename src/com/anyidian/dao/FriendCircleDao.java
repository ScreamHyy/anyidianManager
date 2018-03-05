package com.anyidian.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Comment;
import com.anyidian.model.FriendCircle;
import com.anyidian.model.PageBean;
import com.anyidian.util.DateUtil;
import com.anyidian.util.StringUtil;

public class FriendCircleDao extends JdbcDaoSupport {
	private String sql;
	private List<FriendCircle> mList;

	public List<FriendCircle> queryData(FriendCircle friendCircle,
			PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select f.*,GROUP_CONCAT(i.image) AS myImages from friend_circle f left join images i on f.statusId=i.imageId";
		if (StringUtil.isNotEmpty(friendCircle.getCommunity())) {
			if (!friendCircle.getCommunity().equals("全部")) {
				sql += " and community like '%" + friendCircle.getCommunity()
						+ "%'";
			}
		}
		if (StringUtil.isNotEmpty(friendCircle.getName())) {
			sql += " and name like '%" + friendCircle.getName() + "%'";
		}
		if (StringUtil.isNotEmpty(friendCircle.getMobile())) {
			sql += " and mobile like '%" + friendCircle.getMobile() + "%'";
		}
		if (StringUtil.isNotEmpty(friendCircle.getDate())) {
			sql += " and date like '%" + friendCircle.getDate() + "%'";
		}
		if (StringUtil.isNotEmpty(friendCircle.getStatusType())) {
			if (!friendCircle.getStatusType().equals("全部")) {
				sql += " and statusType like '%" + friendCircle.getStatusType()
						+ "%'";
			}
		}
		sql += " group by f.statusId order by id desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}
		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<FriendCircle>();
		mList = getJdbcTemplate().query(sql, new RowMapper<FriendCircle>() {

			@Override
			public FriendCircle mapRow(ResultSet rs, int num)
					throws SQLException {
				// TODO Auto-generated method stub
				FriendCircle friendCircle = new FriendCircle();
				friendCircle.setStatusId(rs.getString("statusId"));
				friendCircle.setCommunity(rs.getString("community"));
				friendCircle.setName(rs.getString("name"));
				friendCircle.setMobile(rs.getString("mobile"));
				friendCircle.setDate(rs.getString("date"));
				friendCircle.setContent(rs.getString("content"));
				friendCircle.setStatusType(rs.getString("statusType"));
				friendCircle.setLikeNum(rs.getInt("likeNum"));
				friendCircle.setCommentNum(rs.getInt("commentNum"));
				friendCircle.setMyImages(rs.getString("myImages"));
				return friendCircle;
			}

		});
		return mList;
	}

	public boolean modifyData(FriendCircle friendCircle) {
		// TODO Auto-generated method stub
		sql = "update friend_circle set community=?,name=?,mobile=?,content=?,statusType=? where statusId=?";
		int result = getJdbcTemplate().update(
				sql,
				new Object[] { friendCircle.getCommunity(),
						friendCircle.getName(), friendCircle.getMobile(),
						friendCircle.getContent(),
						friendCircle.getStatusType(),
						friendCircle.getStatusId() });

		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean addData(FriendCircle friendCircle) {
		// TODO Auto-generated method stub
		sql = "insert into friend_circle(statusId,community,name,mobile,date,content,statusType,likeNum,commentNum) values(?,?,?,?,?,?,?,?,?)";
		Long timestamp = System.currentTimeMillis();
		String statusId = String.valueOf(timestamp);
		friendCircle.setStatusId(statusId);
		friendCircle.setLikeNum(0);
		friendCircle.setCommentNum(0);
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm");
		friendCircle.setDate(date);
		int result = getJdbcTemplate()
				.update(sql,
						new Object[] { friendCircle.getStatusId(),
								friendCircle.getCommunity(),
								friendCircle.getName(),
								friendCircle.getMobile(),
								friendCircle.getDate(),
								friendCircle.getContent(),
								friendCircle.getStatusType(),
								friendCircle.getLikeNum(),
								friendCircle.getCommentNum() });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteData(String delIds) {
		// TODO Auto-generated method stub
		sql = "delete from friend_circle where statusId in(" + delIds + ")";
		int result = getJdbcTemplate().update(sql, new Object[] {});
		if (result > 0) {
			return true;
		}
		return false;
	}

	public List<Comment> queryComment(Comment comment, PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from comment";
		if (StringUtil.isNotEmpty(comment.getStatusId())) {
			sql += " and statusId like '%" + comment.getStatusId() + "%'";
		}
		sql += " order by id desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}
		sql = sql.replaceFirst("and", "where");
		List<Comment> list = new ArrayList<Comment>();
		list = getJdbcTemplate().query(sql, new RowMapper<Comment>() {

			@Override
			public Comment mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				Comment comment = new Comment();
				comment.setStatusId(rs.getString("statusId"));
				comment.setName(rs.getString("name"));
				comment.setContent(rs.getString("content"));
				return comment;
			}

		});
		return list;
	}

	public List<FriendCircle> queryRoundFriendData(FriendCircle friendCircle,
			PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select f.*,GROUP_CONCAT(i.image) AS myImages from friend_circle f left join images i on f.statusId=i.imageId";
		if (StringUtil.isNotEmpty(friendCircle.getCommunity())) {
			if (!friendCircle.getCommunity().equals("全部")) {
				sql += " and community like '%" + friendCircle.getCommunity()
						+ "%'";
			}
		}
		if (StringUtil.isNotEmpty(friendCircle.getName())) {
			sql += " and name like '%" + friendCircle.getName() + "%'";
		}
		if (StringUtil.isNotEmpty(friendCircle.getMobile())) {
			sql += " and mobile like '%" + friendCircle.getMobile() + "%'";
		}
		if (StringUtil.isNotEmpty(friendCircle.getDate())) {
			sql += " and date like '%" + friendCircle.getDate() + "%'";
		}
		if (StringUtil.isNotEmpty(friendCircle.getStatusType())) {
			if (!friendCircle.getStatusType().equals("全部")) {
				sql += " and statusType like '%" + friendCircle.getStatusType()
						+ "%'";
			}
		}
		sql += " group by f.statusId order by likeNum desc";
		if (pageBean != null) {
			sql += " limit " + pageBean.getStart() + "," + pageBean.getRows();
		}
		sql = sql.replaceFirst("and", "where");
		mList = new ArrayList<FriendCircle>();
		mList = getJdbcTemplate().query(sql, new RowMapper<FriendCircle>() {

			@Override
			public FriendCircle mapRow(ResultSet rs, int num)
					throws SQLException {
				// TODO Auto-generated method stub
				FriendCircle friendCircle = new FriendCircle();
				friendCircle.setStatusId(rs.getString("statusId"));
				friendCircle.setCommunity(rs.getString("community"));
				friendCircle.setName(rs.getString("name"));
				friendCircle.setMobile(rs.getString("mobile"));
				friendCircle.setDate(rs.getString("date"));
				friendCircle.setContent(rs.getString("content"));
				friendCircle.setStatusType(rs.getString("statusType"));
				friendCircle.setLikeNum(rs.getInt("likeNum"));
				friendCircle.setCommentNum(rs.getInt("commentNum"));
				friendCircle.setMyImages(rs.getString("myImages"));
				return friendCircle;
			}

		});
		return mList;
	}
}
