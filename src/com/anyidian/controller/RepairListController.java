package com.anyidian.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anyidian.dao.RepairListDao;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairItem;
import com.anyidian.model.RepairList;
import com.anyidian.model.RepairMan;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class RepairListController {

	private RepairList repairList;
	private String json;
	
	@Autowired
	private RepairListDao repairListDao;

	public RepairListDao getRepairListDao() {
		return repairListDao;
	}

	public void setRepairListDao(RepairListDao repairListDao) {
		this.repairListDao = repairListDao;
	}

	/**
	 * 查询维修单信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/repairListData")
	public void RepairList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		
		String mobile = request.getParameter("mobile");
		String community = request.getParameter("community");
		String type = request.getParameter("type");
		String state = request.getParameter("state");
		
		repairList = new RepairList();
		if(StringUtil.isNotEmpty(mobile)){
			repairList.setMobile(mobile);;
		}
		if(StringUtil.isNotEmpty(community)){
			repairList.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(type)){
			repairList.setRepairType(type);
		}
		if(StringUtil.isNotEmpty(state)){
			repairList.setState(state);
		}

		List<RepairList> mList = new ArrayList<RepairList>();
		mList = repairListDao.queryData(repairList, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = repairListDao.dataCount(repairList);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	/**
	 * 添加维修人员/维修结果
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/addRepairWorkRun")
	public void addRepairWorkRun(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");  
		String listId = request.getParameter("repairId");
		String repairListId = request.getParameter("repairListId");
		String repairmanId = request.getParameter("repairmanId");
		String remarks = request.getParameter("remarks");
		String cost = request.getParameter("cost");
		String result = request.getParameter("result");
		JSONObject resultJson = new JSONObject();

		if(cost == null) {
			boolean successs = repairListDao.addRepairMan(listId, repairmanId, remarks);
			if(successs){
				resultJson.put("success", "操作成功!");
			} else {
				resultJson.put("errorMsg", "操作失败!");
			}
		} else {
			double costFlag = Double.parseDouble(cost);
			boolean successs = repairListDao.addRepairResult(repairListId, costFlag, result);
			if(successs){
				resultJson.put("success", "操作成功!");
			} else {
				resultJson.put("errorMsg", "操作失败!");
			}
		}
		ResponseUtil.write(response, resultJson);
	}
	
	/**
	 * 查询维修条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/repairItemTwoData")
	public void repairItemTwoData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<RepairItem> mList = new ArrayList<RepairItem>();
		RepairItem repairItem = new RepairItem();
		repairItem.setRepairType("全部");
		mList.add(repairItem);
		
		List<RepairItem> itemList = new ArrayList<RepairItem>();
		itemList = repairListDao.queryRepairItem();
		for(int i=0; i<itemList.size(); i++){
			RepairItem item = new RepairItem();
			item.setRepairType(itemList.get(i).getRepairType());
			mList.add(item);
		}
		
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}
	
	/**
	 * 查询维修条目信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/repairItemData")
	public void repairItemData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); 
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		
		List<RepairItem> mList = new ArrayList<RepairItem>();
		mList = repairListDao.queryRepairItemData(pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = repairListDao.RepairItemCount();
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 保存维修条目
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/saveRepairItemData")
	public void saveRepairItemData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String itemId = request.getParameter("itemId");
		String repairType = request.getParameter("repairType");
		String price = request.getParameter("price");
		String remark = request.getParameter("remark");

		RepairItem repairItem = new RepairItem(itemId, repairType, price, remark);
		JSONObject result = new JSONObject();
		if(StringUtil.isNotEmpty(itemId)) {
			boolean successs = repairListDao.modifyRepairItem(repairItem);
			if(successs){
				result.put("success", "修改成功!");
			} else {
				result.put("errorMsg", "修改失败!");
			}
		} else {
			boolean successs = repairListDao.addRepairItem(repairItem);
			if(successs){
				result.put("success", "添加成功!");
			} else {
				result.put("errorMsg", "添加失败!");
			}
		}
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 删除维修条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteRepairItemData")
	public void deleteRepairItemData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean successs = repairListDao.deleteRepairItem(delIds);
		if(successs){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

}
