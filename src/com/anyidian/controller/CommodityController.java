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
import java.util.Random;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anyidian.dao.CommunityDao;
import com.anyidian.dao.ImagesDao;
import com.anyidian.dao.CommodityDao;
import com.anyidian.model.Commodity;
import com.anyidian.model.Community;
import com.anyidian.model.Constans;
import com.anyidian.model.PageBean;
import com.anyidian.model.Shop;
import com.anyidian.util.DateUtil;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class CommodityController extends HttpServlet {

	private String json;
	private Gson gson;

	@Autowired
	private CommodityDao commodityDao;
	@Autowired
	private ImagesDao imagesDao;

	public CommodityDao getCommodityDao() {
		return commodityDao;
	}

	public void setCommodityDao(CommodityDao commodityDao) {
		this.commodityDao = commodityDao;
	}

	public ImagesDao getImagesDao() {
		return imagesDao;
	}

	public void setImagesDao(ImagesDao imagesDao) {
		this.imagesDao = imagesDao;
	}

	/**
	 * 积分商店详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="commodityData")
	public void commodityData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String shop = request.getParameter("shop");
		String state = request.getParameter("state");
		String name = request.getParameter("name");
		Commodity commodity = new Commodity();
		if(StringUtil.isNotEmpty(shop)) {
			commodity.setShop(shop);
		}
		if(StringUtil.isNotEmpty(state)) {
			commodity.setState(state);
		}
		if(StringUtil.isNotEmpty(name)) {
			commodity.setName(name);
		}

		List<Commodity> mList = new ArrayList<Commodity>();
		mList = commodityDao.queryCommodityData(commodity, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = commodityDao.dataCommodityCount(commodity);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
		
		/*Map model = new HashMap();
		List<String> commodityItem = new ArrayList<String>();
		commodityItem = commodityDao.queryShopItemTwo();
		model.put("commodityItem", commodityItem);
		ModelAndView mav = new ModelAndView("commodity", model);
		return mav;*/
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value="/addCommodity")
	public ModelAndView addCommodity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); 
		String commodityId = request.getParameter("commodityId");
		Map model = new HashMap();
		List<Community> communityList = new ArrayList<Community>();
		//communityList = communityDao.queryCommunity();
		//model.put("communityList", communityList);

		if(StringUtil.isNotEmpty(commodityId)) {
			Commodity commodity = commodityDao.queryById(commodityId);
			model.put("commodity", commodity);
		} 
		ModelAndView mav = new ModelAndView("addCommodity", model);
		return mav;
	}

	/**
	 * 保存商品
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/saveCommodity")
	public void saveCommodity(@RequestParam("imageUrl") MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String commodityId = request.getParameter("commodityId");
		String name = request.getParameter("name");
		String supplier = request.getParameter("supplier");
		String imageUrl = null;
		String shop = request.getParameter("shop");
		String nowPrice = request.getParameter("nowPrice");
		//int integral = Integer.parseInt(integralFlag);
		String oldPrice = request.getParameter("oldPrice");
		//double marketPrice = Double.parseDouble(marketPriceFlag);
		String supplyPrice = request.getParameter("supplyPrice");
		String fee = request.getParameter("fee");
		String introduce = request.getParameter("introduce");
		String stockNum = request.getParameter("stockNum");
		String state = request.getParameter("state");
		JSONObject result = new JSONObject();
		List<String> imagesList = new ArrayList<String>();
		String imagePath = request.getSession().getServletContext().getRealPath("/");

		if(StringUtil.isEmpty(commodityId)) {
			Long timeStamp = System.currentTimeMillis() / 1000;
			commodityId = String.valueOf(timeStamp);
			//Commodity commodity = new Commodity(commodityId, name, supplier, shop, nowPrice, oldPrice, supplyPrice, fee, introduce, stockNum, state);
			Commodity commodity = new Commodity();
			boolean success = commodityDao.addCommodityData(commodity);
			if(success){
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					// 保存文件
					imageUrl = imagesDao.saveFile(request, file, imagesList, commodityId);
					if(StringUtil.isNotEmpty(imageUrl)) {
						boolean flag = imagesDao.insertImages(commodityId, imageUrl);
						if(!flag) {
							result.put("errorMsg", "图片添加失败!");
							return;
						}
						if(i == files.length - 1) {
							if(flag) {
								result.put("success", "添加成功!");
							}
						}
					}
				}
			} else {
				result.put("errorMsg", "添加失败!");
			}
		} else {
			//Commodity commodity = new Commodity(commodityId, name, supplier, shop, nowPrice, oldPrice, supplyPrice, fee, introduce, stockNum, state);
			Commodity commodity = new Commodity();
			boolean success = commodityDao.updateCommodityData(commodity);
			List<String> imagesUrlList = new ArrayList<String>();
			if(success){
				if(files != null || files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						MultipartFile file = files[i];
						// 保存文件
						imageUrl = imagesDao.saveFile(request, file, imagesList, commodityId);
						if(StringUtil.isNotEmpty(imageUrl)) {
							imagesUrlList.add(imageUrl);
						}
						if(i == files.length - 1) {
							if (imagesUrlList != null && imagesUrlList.size() > 0) {
								boolean success2 = commodityDao.updateImages(commodityId, imagePath, imagesUrlList);
								if(success2) {
									result.put("success", "修改成功!");
								} else {
									result.put("errorMsg", "图片修改失败!");
								}
							}
						}
					} 
				}
			} else {
				result.put("errorMsg", "修改失败!");
			}
		}

		ResponseUtil.write(response, result);
	}

	/**
	 * 删除商品
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteCommodity")
	public void deleteCommodity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession ().getServletContext ().getRealPath ("/");

		boolean success = commodityDao.deleteData(delIds, imagePath);
		if(success){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}
	
	/**
	 * 查询商家
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/commodityItem")
	public void commodityItem(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Shop> shopList = new ArrayList<Shop>();
		shopList = commodityDao.queryShopItem();

		json = new Gson().toJson(shopList);
		ResponseUtil.write(response, json);
	}

}
