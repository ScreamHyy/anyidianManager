package com.anyidian.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anyidian.dao.ComplaintsDao;
import com.anyidian.model.Complaints;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairList;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class ComplaintsController {

	private String json;

	@Autowired
	private ComplaintsDao complaintsDao;

	public ComplaintsDao getComplaintsDao() {
		return complaintsDao;
	}

	public void setComplaintsDao(ComplaintsDao complaintsDao) {
		this.complaintsDao = complaintsDao;
	}

	@RequestMapping(value="/complaintsListData")
	public void complaintsList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String community = request.getParameter("community");
		String type = request.getParameter("type");
		String state = request.getParameter("state");
		
		Complaints complaints = new Complaints();
		if(StringUtil.isNotEmpty(community)){
			complaints.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(type)){
			complaints.setComplaintType(type);
		}
		if(StringUtil.isNotEmpty(state)){
			complaints.setState(state);
		}

		List<Complaints> mList = new ArrayList<Complaints>();
		mList = complaintsDao.queryData(complaints, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = complaintsDao.dataCount(complaints);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	@RequestMapping(value="/dealComplaints")
	public void dealComplaints(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String listId = request.getParameter("listId");
		
		JSONObject result = new JSONObject();
		boolean successs = complaintsDao.dealComplaints(listId);
		if(successs){
			result.put("success", "操作成功!");
		} else {
			result.put("errorMsg", "操作失败!");
		}
		ResponseUtil.write(response, result);
	}

}
