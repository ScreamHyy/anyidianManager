package com.anyidian.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anyidian.dao.BillDao;
import com.anyidian.dao.CommunityDao;
import com.anyidian.model.Bill;
import com.anyidian.model.Community;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairList;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class BillController {

	private Bill bill;
	private List<Bill> mList;
	private String json;

	@Autowired
	private BillDao billDao;
	@Autowired
	private CommunityDao communityDao;

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}
	
	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	/**
	 * 查询缴费单
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/billListData")
	public void BillList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String mobile = request.getParameter("mobile");
		String room = request.getParameter("room");
		String community = request.getParameter("community");
		String billType = request.getParameter("billType");
		String state = request.getParameter("state");
		String bdate = request.getParameter("bdate");
		String edate = request.getParameter("edate");

		bill = new Bill();
		if(StringUtil.isNotEmpty(mobile)){
			bill.setMobile(mobile);
		}
		if(StringUtil.isNotEmpty(room)){
			bill.setRoom(room);
		}
		if(StringUtil.isNotEmpty(community)){
			bill.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(billType)){
			bill.setBillType(billType);
		}
		if(StringUtil.isNotEmpty(state)){
			bill.setState(state);
		}

		mList = new ArrayList<Bill>();
		mList = billDao.queryData(bill, pageBean, bdate, edate);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = billDao.dataCount(bill, bdate, edate);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	/**
	 * 保存缴费单数据
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/saveBillData")
	public void saveBillData (HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String community = request.getParameter("community");
		String billType = request.getParameter("billType");
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String room = request.getParameter("room");
		String date = request.getParameter("date");
		String price = request.getParameter("price");
		String billId = request.getParameter("billId");
		double priceFlag = Double.parseDouble(price);

		bill = new Bill(community, name, mobile, room, billType, date, priceFlag);
		JSONObject result = new JSONObject();
		if(StringUtil.isNotEmpty(billId)) {
			bill.setBillId(billId);
			boolean success = billDao.modifyData(bill);
			if(success){
				result.put("success", "修改成功!");
			} else {
				result.put("errorMsg", "修改失败!");
			}
		} else {
			boolean success = billDao.addData(bill);
			if(success){
				result.put("success", "添加成功!");
			} else {
				result.put("errorMsg", "添加失败!");
			}
		}
		ResponseUtil.write(response, result);
	}

	/**
	 * 删除数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteBillData")
	public void deleteBillData (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean success = billDao.deleteBill(delIds);
		if(success){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 查询小区条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/communityItemTwo")
	public void communityItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		List<Community> mList = new ArrayList<Community>();
		mList = communityDao.queryCommunity();
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}
	
	/**
	 * 缴费单统计
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/billListStatisticsData")
	public void BillListStatisticsData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String mobile = request.getParameter("mobile");
		String room = request.getParameter("room");
		String community = request.getParameter("community");
		String billType = request.getParameter("billType");
		String state = request.getParameter("state");
		String bdate = request.getParameter("bdate");
		String edate = request.getParameter("edate");

		bill = new Bill();
		if(StringUtil.isNotEmpty(mobile)){
			bill.setMobile(mobile);
		}
		if(StringUtil.isNotEmpty(room)){
			bill.setRoom(room);
		}
		if(StringUtil.isNotEmpty(community)){
			bill.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(billType)){
			bill.setBillType(billType);
		}
		if(StringUtil.isNotEmpty(state)){
			bill.setState(state);
		}

		mList = new ArrayList<Bill>();
		mList = billDao.queryStatisticsData(bill, pageBean, bdate, edate);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = billDao.dataStatisticsCount(bill, bdate, edate);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}
	
}
