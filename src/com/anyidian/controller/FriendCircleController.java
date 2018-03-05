package com.anyidian.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.anyidian.dao.FriendCircleDao;
import com.anyidian.model.Comment;
import com.anyidian.model.FriendCircle;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class FriendCircleController {

	private FriendCircle friendCircle;
	private String json;
	private List<FriendCircle> mList;

	@Autowired
	FriendCircleDao friendCircleDao;

	public FriendCircleDao getFriendCircleDao() {
		return friendCircleDao;
	}

	public void setFriendCircleDao(FriendCircleDao friendCircleDao) {
		this.friendCircleDao = friendCircleDao;
	}

	@RequestMapping(value = "/friendCircleListData")
	public void friendCircleListData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));

		String community = request.getParameter("community");
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String date = request.getParameter("date");
		String content = request.getParameter("content");
		String statusType = request.getParameter("statusType");

		friendCircle = new FriendCircle();
		if (StringUtil.isNotEmpty(community)) {
			friendCircle.setCommunity(community);
		}
		if (StringUtil.isNotEmpty(name)) {
			friendCircle.setName(name);
		}
		if (StringUtil.isNotEmpty(mobile)) {
			friendCircle.setMobile(mobile);
		}
		if (StringUtil.isNotEmpty(date)) {
			friendCircle.setDate(date);
		}
		if (StringUtil.isNotEmpty(content)) {
			friendCircle.setContent(content);
		}
		if (StringUtil.isNotEmpty(statusType)) {
			friendCircle.setStatusType(statusType);
		}
		mList = new ArrayList<FriendCircle>();
		mList = friendCircleDao.queryData(friendCircle, pageBean);

		json = new Gson().toJson(mList);
		JSONObject result = new JSONObject();
		int total = mList.size();
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);

	}
	
	@RequestMapping(value="/deleteFriendCircleData")
	public void deleteFriendCircleData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean success = friendCircleDao.deleteData(delIds);
		if(success){
			result.put("success", "删除成功");
		}else{
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/commentListData")
	public void queryCommentData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String statusId = request.getParameter("statusId");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Comment comment = new Comment();
		JSONObject result = new JSONObject();
		List<Comment> list = new ArrayList<Comment>();
		
		if(StringUtil.isNotEmpty(statusId)){
			comment.setStatusId(statusId);
		}
		list = friendCircleDao.queryComment(comment,pageBean);
		json = new Gson().toJson(list);
		int total = list.size();
		result.put("rows", json);
		result.put("total", total);
		
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/addRoundFriend")
	public ModelAndView addRoundFriend(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		@SuppressWarnings("rawtypes")
		Map model = new HashMap();
		@SuppressWarnings("unchecked")
		ModelAndView mv = new ModelAndView("roundFriend",model);
		return mv;
	}
	
	@RequestMapping(value = "/roundFriendListData")
	public void roundFriendListData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));

		String community = request.getParameter("community");
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String date = request.getParameter("date");
		String content = request.getParameter("content");
		String statusType = request.getParameter("statusType");

		friendCircle = new FriendCircle();
		if (StringUtil.isNotEmpty(community)) {
			friendCircle.setCommunity(community);
		}
		if (StringUtil.isNotEmpty(name)) {
			friendCircle.setName(name);
		}
		if (StringUtil.isNotEmpty(mobile)) {
			friendCircle.setMobile(mobile);
		}
		if (StringUtil.isNotEmpty(date)) {
			friendCircle.setDate(date);
		}
		if (StringUtil.isNotEmpty(content)) {
			friendCircle.setContent(content);
		}
		if (StringUtil.isNotEmpty(statusType)) {
			friendCircle.setStatusType(statusType);
		}
		mList = new ArrayList<FriendCircle>();
		mList = friendCircleDao.queryRoundFriendData(friendCircle, pageBean);

		json = new Gson().toJson(mList);
		JSONObject result = new JSONObject();
		int total = mList.size();
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);

	}
}
