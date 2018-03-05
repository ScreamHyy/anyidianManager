package com.anyidian.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anyidian.model.Manager;
import com.anyidian.model.Menu;
import com.anyidian.model.MenuTree;
import com.anyidian.model.PageBean;
import com.anyidian.model.Role;
import com.anyidian.util.StringUtil;

public class ManagerDao extends JdbcDaoSupport {

	PreparedStatement pstmt;
	private String sql;

	/**
	 * 登录验证
	 * 
	 * @param con
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Manager login(Connection con, Manager user) throws Exception {
		Manager resultUser = null;
		sql = "select * from manager where username=? and password=?";
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2, user.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			resultUser = new Manager();
			resultUser.setUsername(rs.getString("username"));
			resultUser.setPassword(rs.getString("password"));
			resultUser.setState(rs.getShort("state"));
		}
		return resultUser;
	}

	/**
	 * 更改密码
	 * 
	 * @param con
	 * @param username
	 * @param password
	 * @param newpwd
	 * @return
	 * @throws SQLException
	 */
	public int updatePwd(Connection con, String username, String oldpwd,
			String newpwd) throws SQLException {
		sql = "select * from manager where username=? and password=?";
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, username);
		pstmt.setString(2, oldpwd);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			sql = "update manager set password=? where username=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newpwd);
			pstmt.setString(2, username);
			System.out.println(pstmt.executeUpdate());
			return pstmt.executeUpdate();
		}
		return 0;
	}

	// 查询该登陆用户是否具有后台管理权限
	public int getRid(Connection con, Manager user) throws SQLException {
		// TODO Auto-generated method stub
		Role role = new Role();
		sql = "select r.id from manager u INNER JOIN role r on u.id=r.id where u.username=? and u.`password`=?";
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2, user.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			role.setRid(rs.getInt("id"));
			return role.getRid();
		}
		return 0;
	}

	// 查询父菜单
	public List<Menu> getParent(Connection con, int rid) throws SQLException {
		List<Menu> lm = new ArrayList<>();
		sql = "select m.* from menu m inner join users_role u on m.mid=u.mid where u.rid=? and m.mparent=0";
		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, rid);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Menu m = new Menu();
			m.setMid(rs.getShort("mid"));
			m.setText(rs.getString("mname"));
			m.setMiconCls(rs.getString("miconCls"));
			m.setMparent(rs.getInt("mparent"));
			m.setMurl(rs.getString("murl"));
			lm.add(m);
		}
		return lm;
	}

	public List<Menu> getKid(Connection con, String mid, int rid)
			throws SQLException {
		List<Menu> lm = new ArrayList<>();
		ResultSet rs = null;
		sql = "select m.* from menu m inner join users_role u on m.mid=u.mid where mparent="
				+ mid + " and u.rid=" + rid;
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			Menu m = new Menu();
			m.setMid(rs.getShort("mid"));
			m.setText(rs.getString("mname"));
			m.setMiconCls(rs.getString("miconCls"));
			m.setMparent(rs.getInt("mparent"));
			m.setMurl(rs.getString("murl"));
			lm.add(m);
		}
		return lm;
	}

	public List<Manager> managerList(PageBean pageBean) {
		// TODO Auto-generated method stub
		sql = "select * from manager";
		List<Manager> mList = new ArrayList<Manager>();
		mList = getJdbcTemplate().query(sql, new RowMapper<Manager>() {

			@Override
			public Manager mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				Manager manager = new Manager();
				manager.setId(rs.getShort("id"));
				manager.setUsername(rs.getString("username"));
				manager.setMobile(rs.getString("mobile"));
				manager.setCommittee(rs.getString("committee"));
				manager.setCommunity(rs.getString("community"));
				manager.setState(rs.getShort("state"));
				return manager;
			}

		});
		return mList;
	}

	public boolean updateState(Manager manager) {
		// TODO Auto-generated method stub
		sql = "update manager set state=1 where id=?";
		int result = getJdbcTemplate().update(sql,
				new Object[] { manager.getId() });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public List<Menu> getAllMenu() {
		// TODO Auto-generated method stub
		sql = "select * from menu where mparent=0";
		List<Menu> menus = new ArrayList<Menu>();
		menus = getJdbcTemplate().query(sql, new RowMapper<Menu>() {

			@Override
			public Menu mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				Menu menu = new Menu();
				menu.setMid(rs.getShort("mid"));
				menu.setText(rs.getString("mname"));
				menu.setMiconCls(rs.getString("miconCls"));
				menu.setMurl(rs.getString("murl"));
				menu.setMparent(rs.getShort("mparent"));
				return menu;
			}

		});
		return menus;
	}

	public List<Menu> getChildMenu(String pid) {
		// TODO Auto-generated method stub
		sql = "select * from menu";
		if (StringUtil.isNotEmpty(pid)) {
			sql += " where mparent=" + pid;
		}
		List<Menu> menus = new ArrayList<Menu>();
		menus = getJdbcTemplate().query(sql, new RowMapper<Menu>() {

			@Override
			public Menu mapRow(ResultSet rs, int num) throws SQLException {
				// TODO Auto-generated method stub
				Menu menu = new Menu();
				menu.setMid(rs.getShort("mid"));
				menu.setText(rs.getString("mname"));
				menu.setMiconCls(rs.getString("miconCls"));
				menu.setMurl(rs.getString("murl"));
				menu.setMparent(rs.getShort("mparent"));
				return menu;
			}

		});
		return menus;
	}

	public List<MenuTree> menuToTree(List<Menu> mList) {
		// TODO Auto-generated method stub
		List<MenuTree> tList = new ArrayList<MenuTree>();
		for (int i = 0; i < mList.size(); i++) {
			MenuTree tree = new MenuTree();
			tree.setId(String.valueOf(mList.get(i).getMid()));
			tree.setPid(String.valueOf(mList.get(i).getMparent()));
			tree.setState("open");
			tree.setText(mList.get(i).gettext());
			tree.setUrl(mList.get(i).getMurl());
			if (mList.get(i).getMparent() == 0) {
				List<Menu> list = getChildMenu(String.valueOf(mList.get(i)
						.getMid()));
				tree.setChildren(childToTree(list));
			}
			tList.add(tree);
		}
		return tList;
	}

	List<MenuTree> childToTree(List<Menu> mList) {
		List<MenuTree> tList = new ArrayList<MenuTree>();
		for (int i = 0; i < mList.size(); i++) {
			MenuTree tree = new MenuTree();
			tree.setId(String.valueOf(mList.get(i).getMid()));
			tree.setPid(String.valueOf(mList.get(i).getMparent()));
			tree.setState("open");
			tree.setText(mList.get(i).gettext());
			tree.setUrl(mList.get(i).getMurl());
			tList.add(tree);
		}
		return tList;
	}

	public boolean addManager(Manager manager) {
		// TODO Auto-generated method stub
		manager.setCommittee("");
		manager.setCommunity("");
		manager.setState(0);
		sql = "INSERT INTO manager(username,`password`,mobile,committee,community,state) VALUES(?,?,?,?,?,?)";
		int result = getJdbcTemplate().update(
				sql,
				new Object[] { manager.getUsername(), manager.getPassword(),
						manager.getMobile(), manager.getCommittee(),
						manager.getCommunity(), manager.getState() });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean addRole(Role role) {
		// TODO Auto-generated method stub
		role.setUid(getUid());
		sql = "insert into role(rname,uid) values(?,?)";
		int result = getJdbcTemplate().update(sql,
				new Object[] { role.getRname(), role.getUid() });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public int getUid() {
		sql = "SELECT id from manager ORDER BY id DESC LIMIT 1";
		int id = getJdbcTemplate().queryForInt(sql);
		return id;
	}

	public int getRid() {
		sql = "select id from role order by id desc limit 1";
		int id = getJdbcTemplate().queryForInt(sql);
		return id;
	}

	public boolean addRoleAndUser(String mid) {
		// TODO Auto-generated method stub
		int rid = getRid();
		int parseid = Integer.parseInt(mid);
		int result =  getJdbcTemplate().update("insert into users_role(rid,mid) values(?,?)", new Object[] { rid, parseid });
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteData(String delIds) {
		// TODO Auto-generated method stub
		sql = "delete from manager where id in(" + delIds + ")";
		int result = getJdbcTemplate().update(sql, new Object[] {});
		if (result > 0) {
			return true;
		}
		return false;
	}
}
