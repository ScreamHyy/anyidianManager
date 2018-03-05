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

import com.anyidian.dao.CommunityDao;
import com.anyidian.model.Committee;
import com.anyidian.model.Community;
import com.anyidian.model.LifeTypeItem;
import com.anyidian.model.Notice;
import com.anyidian.model.PageBean;
import com.anyidian.model.RepairMan;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class CommunityController {

	private String json;

	@Autowired
	private CommunityDao communityDao;

	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	/**
	 * 查询小区
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/communityListData")
	public void CommunityList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		List<Community> mList = new ArrayList<Community>();
		mList = communityDao.queryData(pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = communityDao.dataCount();
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	/**
	 * 查询小区条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/communityItem")
	public void communityItem(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");

		List<Community> mList = new ArrayList<Community>();
		List<Community> communityList = new ArrayList<Community>();
		communityList = communityDao.queryCommunity();
		Community community = new Community();
		community.setCommunityName("全部");
		mList.add(community);

		for(int i=0; i<communityList.size(); i++){
			Community bean = new Community();
			bean.setCommunityName(communityList.get(i).getCommunityName());
			mList.add(bean);
		}
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}

	/**
	 * 保存小区信息
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/saveCommunityData")
	public void saveCommunityData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String communityId = request.getParameter("communityId");
		String communityName = request.getParameter("communityName");
		String committee = request.getParameter("committee");
		String city = request.getParameter("city");
		String propertyName = request.getParameter("propertyName");
		String mobile = request.getParameter("mobile");
		String address = request.getParameter("address");
		String landArea = request.getParameter("landArea");
		String buildArea = request.getParameter("buildArea");

		Community community = new Community(communityId, communityName, committee, city, propertyName, mobile, address, landArea, buildArea);
		JSONObject result = new JSONObject();
		if(StringUtil.isNotEmpty(communityId)) {
			boolean successs = communityDao.modifyData(community);
			if(successs){
				result.put("success", "修改成功!");
			} else {
				result.put("errorMsg", "修改失败!");
			}
		} else {
			boolean successs = communityDao.addData(community);
			if(successs){
				result.put("success", "添加成功!");
			} else {
				result.put("errorMsg", "添加失败!");
			}
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/deleteCommunityData")
	public void deleteCommunityData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean successs = communityDao.deleteData(delIds);
		if(successs){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

	/**
	 * 查询居委会条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/committeeItem")
	public void committeeItem(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");

		List<Committee> mList = new ArrayList<Committee>();
		List<Committee> committeeList = new ArrayList<Committee>();
		committeeList = communityDao.queryCommittee();
		Committee committee = new Committee();
		committee.setCommitteeName("全部");
		mList.add(committee);

		for(int i=0; i<committeeList.size(); i++){
			Committee bean = new Committee();
			bean.setCommitteeName(committeeList.get(i).getCommitteeName());
			mList.add(bean);
		}
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}
	
}
