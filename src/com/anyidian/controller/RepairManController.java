package com.anyidian.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anyidian.dao.RepairManDao;
import com.anyidian.model.Bill;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairList;
import com.anyidian.model.RepairMan;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class RepairManController {
	
	private String community;
	private String json;
	private List<RepairMan> mList;
	
	@Autowired
	private RepairManDao repairManDao;
	
	public RepairManDao getRepairManDao() {
		return repairManDao;
	}

	public void setRepairManDao(RepairManDao repairManDao) {
		this.repairManDao = repairManDao;
	}

	/**
	 * 查询维修人员
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/repairManListData")
	public void RepairManList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); 
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		
		String community = request.getParameter("community");
		RepairMan repairMan = new RepairMan();
		if(StringUtil.isNotEmpty(community)) {
			repairMan.setCommunity(community);
		}
		mList = new ArrayList<RepairMan>();
		mList = repairManDao.queryData(repairMan, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = repairManDao.dataCount(repairMan);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 保存维修人员
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/saveRepairManData")
	public void saveRepairManData (HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String repairmanId = request.getParameter("repairmanId");
		String community = request.getParameter("community");
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");

		RepairMan repairMan = new RepairMan(repairmanId, name, mobile, community);
		JSONObject result = new JSONObject();
		if(StringUtil.isNotEmpty(repairmanId)) {
			boolean successs = repairManDao.modifyData(repairMan);
			if(successs){
				result.put("success", "修改成功!");
			} else {
				result.put("errorMsg", "修改失败!");
			}
		} else {
			boolean successs = repairManDao.addData(repairMan);
			if(successs){
				result.put("success", "添加成功!");
			} else {
				result.put("errorMsg", "添加失败!");
			}
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/deleteRepairManData")
	public void deleteRepairManData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean successs = repairManDao.deleteData(delIds);
		if(successs){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/loadRepairman")
	public void loadRepairman(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		community = request.getParameter("community");
		community = new String(community.getBytes("ISO-8859-1"), "UTF-8");
		
		mList = new ArrayList<RepairMan>();
		mList = repairManDao.queryRepairMan(community);
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}

}
