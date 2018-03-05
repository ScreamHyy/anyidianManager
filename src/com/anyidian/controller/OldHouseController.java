package com.anyidian.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.anyidian.dao.OldHouseDao;
import com.anyidian.model.Constans;
import com.anyidian.model.OldHouse;
import com.anyidian.model.NewStatus;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class OldHouseController {
	private OldHouse oldHouse;
	private String json;
	private List<OldHouse> mList;
	
	@Autowired
	private OldHouseDao oldHouseDao;
	
	public OldHouseDao getOldHouseDao() {
		return oldHouseDao;
	}

	public void setOldHouseDao(OldHouseDao oldHouseDao) {
		this.oldHouseDao = oldHouseDao;
	}

	@RequestMapping(value="/oldHouseListData")
	public void queryOldHouseData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		String houseId = request.getParameter("houseId");
		String houseType = request.getParameter("houseType");
		String where = request.getParameter("where");
		String area = request.getParameter("area");
		
		oldHouse = new OldHouse();
		if(StringUtil.isNotEmpty(houseId)){
			oldHouse.setHouseId(houseId);
		}
		if(StringUtil.isNotEmpty(houseType)){
			oldHouse.setTitle(houseType);
		}
		if(StringUtil.isNotEmpty(where)){
			oldHouse.setWhere(where);
		}
		if(StringUtil.isNotEmpty(area)){
			oldHouse.setArea(area);
		}
		
		mList = new ArrayList<OldHouse>();
		mList = oldHouseDao.queryData(oldHouse,pageBean);
		JSONObject result = new JSONObject();
		json = new Gson().toJson(mList);
		int total = mList.size();
		result.put("rows", json);
		result.put("total", total);
		
		ResponseUtil.write(response, result);
		
	}
	/**
	 *
	 * 删除
	 *
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 **/
	@RequestMapping(value="/deleteOldHouse")
	public void deleteNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession().getServletContext()
				.getRealPath("/");

		boolean deleteInfo = oldHouseDao.deleteData(delIds, imagePath);
		if (deleteInfo) {
			result.put("success", "删除成功");
		} else {
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}

}
