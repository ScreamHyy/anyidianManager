package com.anyidian.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
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

import com.anyidian.dao.HelperDao;
import com.anyidian.model.Constans;
import com.anyidian.model.Helper;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class HelperController {

	private Helper helper;
	private List<Helper> mList;
	private String json;

	@Autowired
	private HelperDao helperDao;

	public HelperDao getHelperDao() {
		return helperDao;
	}

	public void setHelperDao(HelperDao helperDao) {
		this.helperDao = helperDao;
	}

	/**
	 * 查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/helperListData")
	public void HelperList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String helperType = request.getParameter("helperType");

		helper = new Helper();
		if(StringUtil.isNotEmpty(helperType)){
			helper.setHelperType(helperType);
		}

		mList = new ArrayList<Helper>();
		mList = helperDao.queryData(helper, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = helperDao.dataCount(helper);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	/**
	 * 保存
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/saveHelperData")
	public void saveData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String helperId = request.getParameter("helperId");
		String icon = request.getParameter("icon");
		String helperType = request.getParameter("helperType");
		String website = request.getParameter("website");
		JSONObject result = new JSONObject();

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		String imagePath = request.getSession ().getServletContext ().getRealPath ("/");  

		String fileName = "";
		String stroreName = "";
		String iconUrl = "";
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
						iconUrl = Constans.MY_IMAGEURL + stroreName;
					}

					if(StringUtil.isNotEmpty(helperId)) {
						Helper helper = new Helper();
						if(StringUtil.isEmpty(iconUrl)) {
							helper.setHelperId(helperId);
							helper.setHelperType(helperType);
							helper.setIcon(icon);
							helper.setWebsite(website);
						} else {
							helper.setHelperId(helperId);
							helper.setHelperType(helperType);
							helper.setIcon(iconUrl);
							helper.setWebsite(website);
						}
						boolean success = helperDao.updateData(helper);
						if(success){
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						if(StringUtil.isEmpty(iconUrl)) {
							result.put("errorMsg", "请选择分类图标!");
						}
						else {
							helper.setHelperType(helperType);
							helper.setIcon(iconUrl);
							helper.setWebsite(website);
							boolean success = helperDao.addData(helper);
							if(success){
								result.put("success", "添加成功!");
							} else {
								result.put("errorMsg", "添加失败!");
							}
						}
					}
					/*
			                    接收到的文件转移到给定的目标文件。
			   这可以移动文件系统中的文件,复制文件系统中的文件或内存内容保存到目标文件。如果目标文件已经存在,它将被删除。
			   如果文件系统中的文件被移动,不能再次调用该操作。因此,调用这个方法只有一次能够处理任何存储机制。
					 */
				}
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
	@RequestMapping(value="/deleteHelperData")
	public void deleteData (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean successs = helperDao.deleteData(delIds);
		if(successs){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

}
