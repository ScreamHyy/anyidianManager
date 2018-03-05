package com.anyidian.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;

import com.anyidian.dao.ManagerDao;
import com.anyidian.dao.UserDao;
import com.anyidian.model.Manager;
import com.anyidian.model.Menu;
import com.anyidian.model.MenuTree;
import com.anyidian.model.PageBean;
import com.anyidian.model.Role;
import com.anyidian.model.TopBanner;
import com.anyidian.util.DBManager;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class ManagerController {
	private Manager manager;
	private List<Manager> mList;
	private String json;
	
	DBManager dbManager = new DBManager();
	@Autowired
	ManagerDao managerDao = new ManagerDao();

	public ManagerDao getManagerDao() {
		return managerDao;
	}

	public void setManagerDao(ManagerDao managerDao) {
		this.managerDao = managerDao;
	}

	private Connection con;
	private int flagNums;

	@RequestMapping(value = "/updatePwd")
	public void updatePwd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		request.setCharacterEncoding("utf-8");
		String newpwd = request.getParameter("newpwd");
		String oldpwd = request.getParameter("oldpwd");
		String username = (String) session.getAttribute("username");

		try {
			con = dbManager.getCon();
			JSONObject result = new JSONObject();
			flagNums = managerDao.updatePwd(con, username, oldpwd, newpwd);
			if (flagNums > 0) {
				result.put("success", "true");
			} else {
				result.put("errorMsg", "密码错误");
			}
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbManager.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@RequestMapping(value = "/managerList")
	public void managerList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		mList = new ArrayList<Manager>();
		mList = managerDao.managerList(pageBean);
		
		json = new Gson().toJson(mList);
		JSONObject result = new JSONObject();
		int total = mList.size();
		result.put("rows", json);
		result.put("total", total);
		
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/authorization")
	public void authorization(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("listId");
		int rid = Integer.parseInt(request.getSession().getAttribute("rid").toString());
		manager = new Manager();
		if(StringUtil.isNotEmpty(id)){
			manager.setId(Integer.parseInt(id));
		}
		boolean success = managerDao.updateState(manager);
		JSONObject result = new JSONObject();
		if (success) {
			result.put("success", "操作成功!");
		} else {
			result.put("errorMsg", "服务器内部错误");
		}
		
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/addManager")
	public ModelAndView addTopBannerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		Map model = new HashMap();		
		ModelAndView mav = new ModelAndView("addManager",model);
		
		return mav;
	}
	
	@RequestMapping(value="/addRole")
	public ModelAndView addRoleInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		Map model = new HashMap();		
		ModelAndView mav = new ModelAndView("addRole",model);
		
		return mav;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value="/getAllMenu")
	public void getAllMenu(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		int rid = Integer.parseInt(request.getSession().getAttribute("rid").toString());
		List<Menu> mList = new ArrayList<Menu>();
		mList = managerDao.getAllMenu();
		List<MenuTree> tList = new ArrayList<MenuTree>();
		tList = managerDao.menuToTree(mList);
		json = new Gson().toJson(tList);
		System.out.println(json);
		ResponseUtil.write(response, json);
	}
	
	@RequestMapping(value="/saveManager")
	public void saveManager(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("uname");
		String password = request.getParameter("password");
		String rname = request.getParameter("rname");
		String mobile = request.getParameter("mobile");
		
		JSONObject result = new JSONObject();
		Role role = new Role();
		manager = new Manager();
		if(StringUtil.isNotEmpty(username)){
			manager.setUsername(username);
		}
		if(StringUtil.isNotEmpty(password)){
			manager.setPassword(password);
		}
		if(StringUtil.isNotEmpty(mobile)){
			manager.setMobile(mobile);
		}
		if(StringUtil.isNotEmpty(rname)){
			role.setRname(rname);
		}
		boolean success = managerDao.addManager(manager) && managerDao.addRole(role);
		if (success) {
			result.put("success", "添加成功!");
		} else {
			result.put("errorMsg", "添加失败!");
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/addUserRole")
	public void addRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String str = request.getParameter("str");
		for(int i = 0; i< 5;i++){
			int y = i+1;
			str+=","+y;
		}
		String a[] = str.split(",");
		
		boolean success = false;
		for(int i = 0; i<a.length;i++){
			success = managerDao.addRoleAndUser(a[i]);
		}
		JSONObject result = new JSONObject();
		if(success){
			result.put("success", "添加成功！");
		}else{
			result.put("errorMsg", "添加失败！");
		}
		ResponseUtil.write(response,result);
	}
	/**
	 *
	 * 删除
	 *
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 **/
	@RequestMapping(value="/deleteManager")
	public void deleteNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();

		boolean deleteInfo = managerDao.deleteData(delIds);
		if (deleteInfo) {
			result.put("success", "删除成功");
		} else {
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}
}
