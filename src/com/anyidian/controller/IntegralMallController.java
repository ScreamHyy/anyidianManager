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
import com.anyidian.dao.IntegralMallDao;
import com.anyidian.model.Community;
import com.anyidian.model.Constans;
import com.anyidian.model.IntegralMall;
import com.anyidian.model.PageBean;
import com.anyidian.util.DateUtil;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class IntegralMallController extends HttpServlet {

	private String json;
	private Gson gson;

	@Autowired
	private IntegralMallDao integralMallDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private ImagesDao imagesDao;

	public IntegralMallDao getIntegralMallDao() {
		return integralMallDao;
	}

	public void setIntegralMallDao(IntegralMallDao integralMallDao) {
		this.integralMallDao = integralMallDao;
	}

	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
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
	@RequestMapping(value="integralMallData")
	public void integralMallData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");  
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));

		String mall = request.getParameter("mall");
		String community = request.getParameter("community");
		IntegralMall integralMall = new IntegralMall();
		if(StringUtil.isNotEmpty(mall)) {
			integralMall.setMall(mall);
		}
		if(StringUtil.isNotEmpty(community)) {
			integralMall.setCommunity(community);
		}

		List<IntegralMall> mList = new ArrayList<IntegralMall>();
		mList = integralMallDao.queryIntegralMallData(integralMall, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = integralMallDao.dataIntegralMallCount(integralMall);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value="/addIntegralMall")
	public ModelAndView addIntegralMall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8"); 
		String mallId = request.getParameter("mallId");
		Map model = new HashMap();
		List<Community> communityList = new ArrayList<Community>();

		communityList = communityDao.queryCommunity();
		model.put("communityList", communityList);

		if(StringUtil.isNotEmpty(mallId)) {
			IntegralMall integralMall = integralMallDao.queryById(mallId);
			model.put("integralMall", integralMall);
		} 
		ModelAndView mav = new ModelAndView("addIntegralMall", model);
		return mav;
	}

	/**
	 * 保存积分商店信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/saveIntegralMall")
	public void saveIntegralMall(@RequestParam("imageUrl") MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");  
		String mallId = request.getParameter("mallId");
		String community = request.getParameter("community");
		String title = request.getParameter("title");
		//String imageUrl = request.getParameter("imageUrl");
		String imageUrl = null;
		String mall = request.getParameter("mall");
		String integralFlag = request.getParameter("integral");
		int integral = Integer.parseInt(integralFlag);
		String marketPriceFlag = request.getParameter("marketPrice");
		double marketPrice = Double.parseDouble(marketPriceFlag);
		String introduce = request.getParameter("introduce");
		JSONObject result = new JSONObject();
		List<String> imagesList = new ArrayList<String>();
		String imagePath = request.getSession().getServletContext().getRealPath("/");

		if(StringUtil.isEmpty(mallId)) {
			Long timeStamp = System.currentTimeMillis() / 1000;
			mallId = String.valueOf(timeStamp);
			IntegralMall integralMall = new IntegralMall(mallId, community, title, mall, integral, marketPrice, introduce);
			boolean success = integralMallDao.addIntegralMallData(integralMall);
			if(success){
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					// 保存文件
					imageUrl = imagesDao.saveFile(request, file, imagesList, mallId);
					if(StringUtil.isNotEmpty(imageUrl)) {
						boolean flag = imagesDao.insertImages(mallId, imageUrl);
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
			IntegralMall integralMall = new IntegralMall(mallId, community, title, mall, integral, marketPrice, introduce);
			boolean success = integralMallDao.updateIntegralMallData(integralMall);
			List<String> imagesUrlList = new ArrayList<String>();
			if(success){
				if(files != null || files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						MultipartFile file = files[i];
						// 保存文件
						imageUrl = imagesDao.saveFile(request, file, imagesList, mallId);
						if(StringUtil.isNotEmpty(imageUrl)) {
							imagesUrlList.add(imageUrl);
						}
						if(i == files.length - 1) {
							if (imagesUrlList != null && imagesUrlList.size() > 0) {
								boolean success2 = integralMallDao.updateImages(mallId, imagePath, imagesUrlList);
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
	 * 删除积分商店
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteIntegralMall")
	public void deleteIntegralMall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession ().getServletContext ().getRealPath ("/");

		boolean success = integralMallDao.deleteData(delIds, imagePath);
		if(success){
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

}
