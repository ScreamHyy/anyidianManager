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

import com.anyidian.model.Notice;
import com.anyidian.model.PageBean;
import com.anyidian.util.StringUtil;

public class NoticeDao extends JdbcDaoSupport {
	private String noticeId;
	private String community;
	private String noticeType;
	private String committee;
	private String date;
	private String title;
	private String detail;
	private String image;
	private String publisher;

	private String sql;

	/**
	 * 查询小区通知
	 * @param notice
	 * @param pageBean
	 * @return
	 * @throws ParseException
	 */
	public List<Notice> queryData(Notice notice, PageBean pageBean) throws ParseException {
		sql = "select * from notice";
		if(StringUtil.isNotEmpty(notice.getNoticeType())) {
			sql = sql+" and noticeType like '%"+notice.getNoticeType()+"%'";
		}
		if(StringUtil.isNotEmpty(notice.getCommunity())) {
			if(!notice.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+notice.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(notice.getCommittee())) {
			if(!notice.getCommittee().equals("全部")){
				sql = sql+" and committee like '%"+notice.getCommittee()+"%'";
			}
		}

		sql = sql+" order by id desc";
		if(pageBean != null){
			sql = sql+" limit"+" "+pageBean.getStart()+","+pageBean.getRows();
		}
		List<Notice> mList = new ArrayList<Notice>();
		mList = getJdbcTemplate().query(sql.replaceFirst("and", "where"), new RowMapper<Notice>() {

			@Override
			public Notice mapRow(ResultSet rs, int num) throws SQLException {
				noticeId = rs.getString("noticeId");
				community = rs.getString("community");
				noticeType = rs.getString("noticeType");
				committee = rs.getString("committee");
				date = rs.getString("date");
				title = rs.getString("title");
				detail = rs.getString("detail");
				image = rs.getString("image");
				publisher = rs.getString("publisher");
				Notice notice = new Notice(noticeId, community, noticeType, committee, date, title, detail, image, publisher);
				return notice;
			}
		});
		return mList;
	}
	
	public int dataCount(Notice notice) throws Exception {
		sql = "select count(*) as total from notice";
		if(StringUtil.isNotEmpty(notice.getNoticeType())) {
			sql = sql+" and noticeType like '%"+notice.getNoticeType()+"%'";
		}
		if(StringUtil.isNotEmpty(notice.getCommunity())) {
			if(!notice.getCommunity().equals("全部")){
				sql = sql+" and community like '%"+notice.getCommunity()+"%'";
			}
		}
		if(StringUtil.isNotEmpty(notice.getCommittee())) {
			if(!notice.getCommittee().equals("全部")){
				sql = sql+" and committee like '%"+notice.getCommittee()+"%'";
			}
		}
		int total = getJdbcTemplate().queryForInt(sql.replaceFirst("and", "where"));
		return total;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Notice queryById(String noticeId) {
		sql = "select * from notice where noticeId='"+noticeId+"'";
		return getJdbcTemplate().queryForObject(sql, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				Notice notice = new Notice();
				notice.setNoticeId(rs.getString("noticeId"));
				notice.setCommunity(rs.getString("community"));
				notice.setNoticeType(rs.getString("noticeType"));
				notice.setCommittee(rs.getString("committee"));
				notice.setDate(rs.getString("date"));
				notice.setTitle(rs.getString("title"));
				notice.setDetail(rs.getString("detail"));
				notice.setImage(rs.getString("image"));
				notice.setPublisher(rs.getString("publisher"));
				return notice;
			}
		});
	}

	/**
	 * 更改通知
	 * @param notice
	 * @return
	 */
	public boolean updateData(Notice notice) {
		sql = "update notice set community=?,committee=?,title=?,detail=?,image=?,publisher=? where noticeId=?";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{notice.getCommunity(), notice.getCommittee(), notice.getTitle(), 
				notice.getDetail(), notice.getImage(), notice.getPublisher(), notice.getNoticeId()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加通知
	 * @param notice
	 * @return
	 */
	public boolean addData(Notice notice) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String nowTime = df.format(day);
		Long timeStamp = System.currentTimeMillis() / 1000;
		String noticeId = String.valueOf(timeStamp);
		sql = "insert into notice(noticeId,community,noticeType,committee,date,title,detail,image,publisher) values(?,?,?,?,?,?,?,?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{noticeId, notice.getCommunity(), notice.getNoticeType(), notice.getCommittee(), nowTime, 
				notice.getTitle(), notice.getDetail(), notice.getImage(), notice.getPublisher()});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除数据
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean deleteData(String noticeId, String imagePath) {
		File savePathFile = new File(imagePath);
		String[] noticeIdArray = noticeId.split(",");
		for (int i = 0; i < noticeIdArray.length; i++) {
			String flagId = noticeIdArray[i];
			sql = "select image from notice where noticeId=?"; 
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
				sql = "delete from notice where noticeId in("+noticeId+")";
				int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
				if (resultInt > 0) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

}
