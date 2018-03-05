package com.anyidian.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anyidian.dao.ManagerDao;
import com.anyidian.model.Manager;
import com.anyidian.model.Menu;
import com.anyidian.model.Role;
import com.anyidian.util.DBManager;
import com.anyidian.util.StringUtil;

@Controller
public class LoginController {

	private String username;
	private String password;
	private Manager user;
	private Role role;
	DBManager dbManager = new DBManager();
	ManagerDao userDao = new ManagerDao();

	@RequestMapping("/login")
	public void loginView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		username = request.getParameter("username");
		password = request.getParameter("password");
		request.setAttribute("username", username);
		request.setAttribute("password", password);

		if(StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
			request.setAttribute("error", "请输入用户名和密码！");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		user = new Manager(username, password);
		role = new Role();
		Connection con = null;
		try {
			con = dbManager.getCon();
			Manager currentUser = userDao.login(con, user);
			role.setRid(userDao.getRid(con, user));
			if(currentUser == null) {
				request.setAttribute("error", "用户名或密码错误！");
				//服务器跳转
				request.getRequestDispatcher("index.jsp").forward(request, response);
			} else {
				if(currentUser.getState() != 0){
					//获取session
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					session.setAttribute("rid", role.getRid());
					//客服端跳转
					response.sendRedirect("main");
				}else{
					request.setAttribute("error", "该用户没有授权!");
					request.getRequestDispatcher("index.jsp").forward(request, response);
				}
			}
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

	@RequestMapping("/main")
	public ModelAndView mainView(){
		return new ModelAndView("main");	//跳转页面名字
	}
	
	@RequestMapping("/menu")
	public void getAllMenu(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String mid = request.getParameter("mid");
		int rid = Integer.parseInt(request.getSession().getAttribute("rid").toString());
		List<Menu> lm = null;
		Connection con = null;
		try{
			con = dbManager.getCon();
			if(mid != null){
				lm = userDao.getKid(con, mid, rid);
			}else{
				lm = userDao.getParent(con, rid);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				dbManager.closeCon(con);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		JSONArray jsonArray = JSONArray.fromObject(lm);
		String str = jsonArray.toString();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(str);
		response.getWriter().flush();
		response.getWriter().close();
	}
}
