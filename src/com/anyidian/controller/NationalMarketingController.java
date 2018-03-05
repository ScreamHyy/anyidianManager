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

import com.anyidian.dao.NationalMarketingDao;
import com.anyidian.model.Marketing;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class NationalMarketingController {
	private Marketing marketing;
	private List<Marketing> mList;
	private String json;

	@Autowired
	NationalMarketingDao nationalMarketingDao;

	public NationalMarketingDao getNationalMarketingDao() {
		return nationalMarketingDao;
	}

	public void setNationalMarketingDao(
			NationalMarketingDao nationalMarketingDao) {
		this.nationalMarketingDao = nationalMarketingDao;
	}

	/**
	 *
	 * 查询所有得营销数据
	 * 
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @return mList
	 * @throws Exception
	 * 
	 **/
	@RequestMapping(value = "/nationalMarketingListData")
	public void nationalMarketingList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));

		String marketingId = request.getParameter("marketing");
		String houseType = request.getParameter("houseType");
		String houseNum = request.getParameter("houseNum");
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String toName = request.getParameter("toName");
		String toMobile = request.getParameter("toMobile");
		
		
		Marketing marketing = new Marketing();
		if(StringUtil.isNotEmpty(marketingId)){
			marketing.setMarketingId(marketingId);
		}
		if(StringUtil.isNotEmpty(houseType)){
			marketing.setHouseType(houseType);
		}
		if(StringUtil.isNotEmpty(houseNum)){
			marketing.setHouseNum(houseNum);
		}
		if(StringUtil.isNotEmpty(name)){
			marketing.setName(name);
		}
		if(StringUtil.isNotEmpty(mobile)){
			marketing.setMobile(mobile);
		}
		if(StringUtil.isNotEmpty(toName)){
			marketing.setToName(toName);
		}
		if(StringUtil.isNotEmpty(toMobile)){
			marketing.setToMobile(toMobile);
		}

		mList =new ArrayList<Marketing>();
		mList = nationalMarketingDao.queryData(marketing,pageBean);
		json = new Gson().toJson(mList);
		
		JSONObject result = new JSONObject();
		int total = mList.size();
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
		
	}
	
	/**
	 * 
	 * 修改全民营销数据
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * 
	 **/
	
	@RequestMapping(value="/saveNationalMarketingData")
	public void saveNationalMarketingData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		
			request.setCharacterEncoding("UTF-8");
			
			String marketingId = request.getParameter("marketingId");
			String houseType = request.getParameter("houseType");
			String houseNum = request.getParameter("houseNum");
			String name = request.getParameter("name");
			String mobile = request.getParameter("mobile");
			String toName = request.getParameter("toName");
			String toMobile = request.getParameter("toMobile");
			
			marketing = new Marketing(marketingId, houseType, houseNum, name, mobile, toName, toMobile);
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(marketingId)){
				boolean success = nationalMarketingDao.modifyData(marketing);
				if(success){
					result.put("success", "修改成功");
				}else{
					result.put("errorMsg", "服务器内部错误");
				}
			}else{
				boolean success = nationalMarketingDao.addData(marketing);
				if(success){
					result.put("success", "添加成功");
				}else{
					result.put("errorMsg", "服务器内部错误");
				}
			}
			ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/deleteNationalMarketing")
	public void deleteNationalMarktingData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean success = nationalMarketingDao.deleteNationalMarketing(delIds);
		if(success){
			result.put("success", "删除成功");
		}else{
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}

}
