package com.anyidian.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anyidian.dao.InterestGroupDao;
import com.anyidian.model.Interest;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class InterestGroupController {
	private Interest interest;
	private String json;
	private List<Interest> mList;
	@Autowired
	InterestGroupDao interestGroupDao;

	public InterestGroupDao getInterestGroupDao() {
		return interestGroupDao;
	}

	public void setInterestGroupDao(InterestGroupDao interestGroupDao) {
		this.interestGroupDao = interestGroupDao;
	}

	@RequestMapping(value = "/interestGroupListData")
	public void interestGroupListData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));

		String community = request.getParameter("community");

		interest = new Interest();
		if (StringUtil.isNotEmpty(community)) {
			interest.setCommunity(community);
		}

		mList = new ArrayList<Interest>();
		mList = interestGroupDao.queryData(interest, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = mList.size();
		result.put("total", total);
		result.put("rows", json);
		ResponseUtil.write(response, result);

	}

	@RequestMapping(value = "/saveInterestGroupData")
	public void saveInterestGroupData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String interestId = request.getParameter("interestId");
		String community = request.getParameter("community");
		String interestType = request.getParameter("interestType");

		interest = new Interest();
		if (StringUtil.isNotEmpty(community)) {
			interest.setCommunity(community);
		}
		if (StringUtil.isNotEmpty(interestType)) {
			interest.setInterestType(interestType);
		}
		JSONObject result = new JSONObject();
		if (StringUtil.isNotEmpty(interestId)) {
			interest.setInterestId(interestId);
			boolean success = interestGroupDao.modifyData(interest);
			if(success){
				result.put("success", "修改成功");
			}else{
				result.put("errorMsg", "服务器内部错误");
			}
		} else {
			boolean success = interestGroupDao.addData(interest);
			if(success){
				result.put("success", "添加成功");
			}else{
				result.put("errorMsg", "服务器内部错误");
			}
		}
		
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/deleteInterestGroupData")
	public void deleteInterestGroupData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean success = interestGroupDao.deleteData(delIds);
		if(success){
			result.put("success", "删除成功");
		}else{
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/interestItem")
	public void interestItem(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		mList = new ArrayList<Interest>();
		List<Interest> interestList = new ArrayList<Interest>();
		interestList = interestGroupDao.queryInterestType();
		
		Interest interestItem = new Interest();
		interestItem.setInterestType("全部");
		
		mList.add(interestItem);
		for(int i=0; i<interestList.size();i++){
			Interest bean = new Interest();
			bean.setInterestType(interestList.get(i).getInterestType());
			mList.add(bean);
		}
		
		
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);		
	}
	
}
