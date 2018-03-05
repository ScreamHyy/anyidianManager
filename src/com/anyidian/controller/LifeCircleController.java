package com.anyidian.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anyidian.dao.CommunityDao;
import com.anyidian.dao.LifeCircleDao;
import com.anyidian.model.Community;
import com.anyidian.model.Constans;
import com.anyidian.model.LifeCircle;
import com.anyidian.model.LifeTypeItem;
import com.anyidian.model.Notice;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class LifeCircleController extends HttpServlet {

	private String json;

	@Autowired
	private LifeCircleDao lifeCircleDao;
	@Autowired
	private CommunityDao communityDao;

	public LifeCircleDao getLifeCircleDao() {
		return lifeCircleDao;
	}

	public void setLifeCircleDao(LifeCircleDao lifeCircleDao) {
		this.lifeCircleDao = lifeCircleDao;
	}

	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	/**
	 * 本地生活圈
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/lifeCircleData")
	public void lifeCircleData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String name = request.getParameter("name");
		String typeName = request.getParameter("typeName");
		LifeCircle lifeCircle = new LifeCircle();
		if(StringUtil.isNotEmpty(name)){
			lifeCircle.setName(name);
		}
		if(StringUtil.isNotEmpty(typeName)){
			if(!typeName.equals("全部")) {
				String itemId = lifeCircleDao.queryItemId(typeName);
				lifeCircle.setTypeId(itemId);
			}
		}

		List<LifeCircle> mList = new ArrayList<LifeCircle>();
		mList = lifeCircleDao.queryData(lifeCircle, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = lifeCircleDao.dataCount(lifeCircle);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value="/addLifeCircle")
	public ModelAndView addLifeCircle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); 
		String lifeId = request.getParameter("lifeId");
		Map model = new HashMap();
		List<Community> communityList = new ArrayList<Community>();
		List<LifeTypeItem> lifeTypeList = new ArrayList<LifeTypeItem>();

		communityList = communityDao.queryCommunity();
		lifeTypeList = lifeCircleDao.queryLifeTypeItemTwo();
		model.put("communityList", communityList);
		model.put("lifeTypeList", lifeTypeList);

		if(StringUtil.isNotEmpty(lifeId)) {
			LifeCircle lifeCircle = lifeCircleDao.queryById(lifeId);
			model.put("lifeCircle", lifeCircle);
		} 
		ModelAndView mav = new ModelAndView("addLifeCircle", model);
		return mav;
	}

	/**
	 * 保存商家信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/saveLifeCircle")
	public void saveLifeCircle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String lifeId = request.getParameter("lifeId");
		String oldImage = request.getParameter("image");
		String typeId = request.getParameter("typeId");
		String name = request.getParameter("name");
		String tel = request.getParameter("tel");
		String address = request.getParameter("address");
		String detail = request.getParameter("detail");
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

					if(StringUtil.isNotEmpty(lifeId)) {
						LifeCircle lifeCircle;
						if(StringUtil.isEmpty(iconUrl)) {
							lifeCircle = new LifeCircle(lifeId, oldImage, typeId, name, tel, address, detail);
						} else {
							lifeCircle = new LifeCircle(lifeId, iconUrl, typeId, name, tel, address, detail);
						}
						boolean success = lifeCircleDao.updateData(lifeCircle);
						if(success){
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						LifeCircle lifeCircle = new LifeCircle(lifeId, iconUrl, typeId, name, tel, address, detail);
						boolean success = lifeCircleDao.addData(lifeCircle);
						if(success){
							result.put("success", "添加成功!");
						} else {
							result.put("errorMsg", "添加失败!");
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
	 * 删除
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteLifeCircle")
	public void deleteLifeCircle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession ().getServletContext ().getRealPath ("/");

		boolean success = lifeCircleDao.deleteData(delIds,imagePath);
		if(success){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

	@RequestMapping(value="/lifeTypeData")
	public void lifeTypeData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		List<LifeTypeItem> mList = new ArrayList<LifeTypeItem>();
		mList = lifeCircleDao.queryLifeTypeItem(pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = lifeCircleDao.dataItemCount();
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	/**
	 * 保存商家类型
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/saveLifeTypeData")
	public void saveLifeTypeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String itemId = request.getParameter("itemId");
		String name = request.getParameter("name");
		String oldIcon = request.getParameter("icon");
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

					if(StringUtil.isNotEmpty(itemId)) {
						LifeTypeItem item = new LifeTypeItem();
						if(StringUtil.isEmpty(iconUrl)) {
							item.setItemId(itemId);
							item.setIcon(oldIcon);
							item.setName(name);
						} else {
							item.setItemId(itemId);
							item.setIcon(iconUrl);
							item.setName(name);
						}
						boolean success = lifeCircleDao.updateItemData(item);
						if(success){
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						if(StringUtil.isEmpty(iconUrl)) {
							result.put("errorMsg", "请选择分类图标!");
						} else {
							LifeTypeItem item = new LifeTypeItem(iconUrl, name);
							boolean success = lifeCircleDao.addItemData(item);
							if(success) {
								result.put("success", "添加成功!");
							} else {
								result.put("errorMsg", "添加失败!");
							}
						}
					}
				}
			}
		}
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 删除商家类型
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteLifeType")
	public void deleteLifeType (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean successs = lifeCircleDao.deleteItemData(delIds);
		if(successs){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

	/**
	 * 查询商家类型条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/lifeTypeItem")
	public void lifeTypeItem(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");

		List<LifeTypeItem> mList = new ArrayList<LifeTypeItem>();
		List<LifeTypeItem> itemList = new ArrayList<LifeTypeItem>();
		itemList = lifeCircleDao.queryLifeTypeItemTwo();
		LifeTypeItem lifeTypeItem = new LifeTypeItem();
		lifeTypeItem.setName("全部");
		mList.add(lifeTypeItem);

		for(int i=0; i<itemList.size(); i++){
			LifeTypeItem bean = new LifeTypeItem();
			bean.setName(itemList.get(i).getName());
			mList.add(bean);
		}
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}

	/**
	 * 查询商家类型条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/lifeTypeItemTwo")
	public void lifeTypeItemTwo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		List<LifeTypeItem> mList = new ArrayList<LifeTypeItem>();
		mList = lifeCircleDao.queryLifeTypeItemTwo();
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}
	
}
