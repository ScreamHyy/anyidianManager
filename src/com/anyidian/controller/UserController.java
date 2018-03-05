package com.anyidian.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.anyidian.dao.UserDao;
import com.anyidian.model.Constans;
import com.anyidian.model.PageBean;
import com.anyidian.model.User;
import com.anyidian.model.IntegralRecorde;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class UserController {

	private String json;
	@Autowired
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(value="/userListData")
	public void UserListData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String community = request.getParameter("community");
		String mobile = request.getParameter("mobile");
		String name = request.getParameter("name");

		User user = new User();
		if(StringUtil.isNotEmpty(community)){
			user.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(mobile)){
			user.setMobile(mobile);;
		}
		if(StringUtil.isNotEmpty(name)){
			user.setName(name);;
		}

		List<User> mList = new ArrayList<User>();
		mList = userDao.queryData(user, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = userDao.dataCount(user);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	@RequestMapping(value="/updateBackground")
	public void updateBackground(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String mobileStr = request.getParameter("mobile");
		JSONObject result = new JSONObject();

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		String imagePath = request.getSession ().getServletContext ().getRealPath ("/");  

		String fileName = "";
		String stroreName = "";
		String bgUrl = "";
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 由CommonsMultipartFile继承而来,拥有上面的方法.
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					fileName = file.getOriginalFilename();
					if (StringUtil.isNotEmpty(fileName)) {
						stroreName = System.currentTimeMillis() + ""
								+ fileName.substring(fileName.lastIndexOf("."));
						File savePathFile = new File(imagePath);

						String flagPath = savePathFile +  "\\resources\\images\\" + stroreName;
						File localFile = new File(flagPath);
						file.transferTo(localFile);
						bgUrl = Constans.MY_IMAGEURL + stroreName;
					}
					if(StringUtil.isEmpty(bgUrl)){
						result.put("errorMsg", "请选择一张图片!");
					} else {
						boolean success = userDao.updateBackground(mobileStr, bgUrl);
						if(success){
							result.put("success", "修改成功!");
						} else {
							result.put("errorMsg", "修改失败!");
						}
					}
				}
			}
		}
		ResponseUtil.write(response, result);
	}

	/**
	 * 查询积分兑换记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/integralRecordeData")
	public void IntegralRecordeData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String user = request.getParameter("user");

		IntegralRecorde integralRecorde  = new IntegralRecorde();
		if(StringUtil.isNotEmpty(user)){
			integralRecorde.setUser(user);
		}

		List<IntegralRecorde> mList = new ArrayList<IntegralRecorde>();
		mList = userDao.queryIntegralRecorde(integralRecorde, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = userDao.IntegralRecordeCount(integralRecorde);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	/**
	 * 添加积分兑换记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/saveIntegralRecordeData")
	public void SaveIntegralRecordeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		JSONObject result = new JSONObject();
		String user = request.getParameter("user");
		String title = request.getParameter("title");
		String integralFlag = request.getParameter("integral");
		int oldIntegral = userDao.queryIntegral(user);	//获取原本积分
		int integral = 0;
		int nowIntegral = 0;
		boolean flag = integralFlag.startsWith("-");
		if(flag) {
			String flagNum = integralFlag.substring(1, integralFlag.length());	//去掉-
			integral = Integer.parseInt(flagNum);	//获得积分（为负）
			nowIntegral = oldIntegral - integral;
		} else {
			integral = Integer.parseInt(integralFlag);
			nowIntegral = oldIntegral + integral;
		}
		IntegralRecorde integralRecorde = new IntegralRecorde(user, title, integralFlag);

		boolean success = userDao.insertIntegralRecorde(integralRecorde, nowIntegral);
		if(success){
			result.put("success", "添加成功，户主（" + user + "）获得积分" + integralFlag);
		} else {
			result.put("errorMsg", "添加失败!");
		}
		ResponseUtil.write(response, result);
	}

}
