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
import com.anyidian.dao.BusinessManageDao;
import com.anyidian.model.Community;
import com.anyidian.model.Constans;
import com.anyidian.model.Business;
import com.anyidian.model.Business;
import com.anyidian.model.LifeTypeItem;
import com.anyidian.model.PageBean;
import com.anyidian.model.Shop;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class BusinessManageController extends HttpServlet {

	private String json;

	@Autowired
	private BusinessManageDao businessManageDao;
	@Autowired
	private CommunityDao communityDao;

	public BusinessManageDao getBusinessManageDao() {
		return businessManageDao;
	}

	public void setBusinessManageDao(BusinessManageDao businessManageDao) {
		this.businessManageDao = businessManageDao;
	}

	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}
	
	/**
	 * 商家详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="shopData")
	public void shopData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String shopName = request.getParameter("shopName");
		String community = request.getParameter("community");
		String businessName = request.getParameter("businessName");
		Shop shop = new Shop();
		if(StringUtil.isNotEmpty(shopName)){
			shop.setShopName(shopName);
		}
		if(StringUtil.isNotEmpty(community)){
			shop.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(businessName)){
			if(!businessName.equals("全部")) {
				String businessId = businessManageDao.queryBusinessId(businessName);
				shop.setBusinessId(businessId);
			}
		}

		List<Shop> mList = new ArrayList<Shop>();
		mList = businessManageDao.queryShopData(shop, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = businessManageDao.dataShopCount(shop);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value="/addShop")
	public ModelAndView addShop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); 
		String shopId = request.getParameter("shopId");
		Map model = new HashMap();
		List<Community> communityList = new ArrayList<Community>();
		List<Business> businessList = new ArrayList<Business>();

		communityList = communityDao.queryCommunity();
		businessList = businessManageDao.queryBusinessItem();
		model.put("communityList", communityList);
		model.put("businessList", businessList);

		if(StringUtil.isNotEmpty(shopId)) {
			Shop shop = businessManageDao.queryById(shopId);
			model.put("shop", shop);
		} 
		ModelAndView mav = new ModelAndView("addShop", model);
		return mav;
	}

	/**
	 * 保存商家信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/saveShop")
	public void saveShop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String shopId = request.getParameter("shopId");
		String community = request.getParameter("community");
		String businessId = request.getParameter("businessId");
		String oldImage = request.getParameter("image");
		String shopName = request.getParameter("shopName");
		String tel = request.getParameter("tel");
		String address = request.getParameter("address");
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

					if(StringUtil.isNotEmpty(shopId)) {
						Shop shop;
						if(StringUtil.isEmpty(iconUrl)) {
							shop = new Shop(shopId, community, businessId, oldImage, shopName, tel, address);
						} else {
							shop = new Shop(shopId, community, businessId, iconUrl, shopName, tel, address);
						}
						boolean success = businessManageDao.updateShopData(shop);
						if(success){
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						Shop shop = new Shop(shopId, community, businessId, iconUrl, shopName, tel, address);
						boolean success = businessManageDao.addShopData(shop);
						if(success){
							result.put("success", "添加成功!");
						} else {
							result.put("errorMsg", "添加失败!");
						}
					}
				}
			}
		}
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 删除商家
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteShop")
	public void deleteShop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession ().getServletContext ().getRealPath ("/");

		boolean success = businessManageDao.deleteData(delIds,imagePath);
		if(success){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/businessTypeData")
	public void businessTypeData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		List<Business> mList = new ArrayList<Business>();
		mList = businessManageDao.queryBusiness(pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = businessManageDao.dataBusinessCount();
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
	@RequestMapping(value="/saveBusinessTypeData")
	public void saveBusinessTypeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String businessId = request.getParameter("businessId");
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

					if(StringUtil.isNotEmpty(businessId)) {
						Business item = new Business();
						if(StringUtil.isEmpty(iconUrl)) {
							item.setBusinessId(businessId);
							item.setIcon(oldIcon);
							item.setName(name);
						} else {
							item.setBusinessId(businessId);
							item.setIcon(iconUrl);
							item.setName(name);
						}
						boolean success = businessManageDao.updateItemData(item);
						if(success){
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						if(StringUtil.isEmpty(iconUrl)) {
							result.put("errorMsg", "请选择分类图标!");
						} else {
							Business item = new Business(iconUrl, name);
							boolean success = businessManageDao.addItemData(item);
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
	@RequestMapping(value="/deleteBusinessType")
	public void deleteBusinessType (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		boolean successs = businessManageDao.deleteBusinessType(delIds);
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
	@RequestMapping(value="/businessTypeItem")
	public void businessTypeItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		List<Business> mList = new ArrayList<Business>();
		mList = businessManageDao.queryBusinessItem();
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}
	
	/**
	 * 查询商家类型条目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/businessTypeItemTwo")
	public void businessTypeItemTwo(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");

		List<Business> mList = new ArrayList<Business>();
		List<Business> itemList = new ArrayList<Business>();
		itemList = businessManageDao.queryBusinessItem();
		Business businessItem = new Business();
		businessItem.setName("全部");
		mList.add(businessItem);

		for(int i=0; i<itemList.size(); i++){
			Business bean = new Business();
			bean.setName(itemList.get(i).getName());
			mList.add(bean);
		}
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}

}
