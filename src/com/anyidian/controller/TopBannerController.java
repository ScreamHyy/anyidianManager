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

import com.anyidian.dao.TopBannerDao;
import com.anyidian.model.Constans;
import com.anyidian.model.NewStatus;
import com.anyidian.model.TopBanner;
import com.anyidian.model.PageBean;
import com.anyidian.model.TopBanner;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class TopBannerController {
	private TopBanner topBanner;
	private String json;
	private List<TopBanner> mList;
	@Autowired
	TopBannerDao topBannerDao;
	

	public TopBannerDao getTopBannerDao() {
		return topBannerDao;
	}


	public void setTopBannerDao(TopBannerDao topBannerDao) {
		this.topBannerDao = topBannerDao;
	}


	public TopBannerController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/topBannerListData")
	public void topBannerListData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		String community = request.getParameter("community");
		String bannerType = request.getParameter("bannerType");
		String title = request.getParameter("title");
		
		topBanner = new TopBanner();
		if(StringUtil.isNotEmpty(community)){
			topBanner.setCommunity(community);
		}
		if(StringUtil.isNotEmpty(bannerType)){
			topBanner.setBannerType(bannerType);
		}
		if(StringUtil.isNotEmpty(title)){
			topBanner.setTitle(title);
		}
		
		mList = new ArrayList<TopBanner>();
		mList = topBannerDao.queryData(topBanner,pageBean);
		
		json = new Gson().toJson(mList);
		
		JSONObject result = new JSONObject();
		int total = mList.size();
		result.put("total", total);
		result.put("rows",json);
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/bannerTypeItem")
	public void bannerTypeItem(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		List<TopBanner> mList = new ArrayList<TopBanner>();
		List<TopBanner> topBannerList = new ArrayList<TopBanner>();
		topBannerList = topBannerDao.queryTopBanner();
		TopBanner TopBanner = new TopBanner();
		TopBanner.setBannerType("全部");
		mList.add(TopBanner);

		for(int i=0; i<topBannerList.size(); i++){
			TopBanner bean = new TopBanner();
			bean.setBannerType(topBannerList.get(i).getBannerType());
			mList.add(bean);
		}
		json = new Gson().toJson(mList);
		ResponseUtil.write(response, json);
	}
	
	@RequestMapping(value="/addTopBannerInfo")
	public ModelAndView addTopBannerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String bannerId = request.getParameter("bannerId");
		Map model = new HashMap();
		if(StringUtil.isNotEmpty(bannerId)){
			TopBanner topbanner = topBannerDao.querDataById(bannerId);
			model.put("topbanner", topbanner);
		}
		
		ModelAndView mav = new ModelAndView("addTopBanner",model);
		
		return mav;
	}
	
	@RequestMapping(value="/saveTopBanner")
	public void saveTopBanner(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String bannerId = request.getParameter("bannerId");
		String community = request.getParameter("community");
		String bannerType = request.getParameter("bannerType");
		String oldImage = request.getParameter("image");
		String title = request.getParameter("title");
		String introduce = request.getParameter("introduce");
		
		JSONObject result = new JSONObject();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		String imagePath = request.getSession().getServletContext()
				.getRealPath("/");

		String fileName = "";
		String stroreName = "";
		String imageUrl = "";
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 由CommonsMultipartFile继承而来,拥有上面的方法.
				MultipartFile file = multiRequest.getFile(iter.next());
				/*
				 * if(file.getSize() > 20*1024*1024){
				 * System.out.println("overMaxSize"); }
				 */
				if (file != null) {
					fileName = file.getOriginalFilename();
					if (StringUtil.isNotEmpty(fileName)) {
						stroreName = System.currentTimeMillis() + ""
								+ fileName.substring(fileName.lastIndexOf("."));
						File savePathFile = new File(imagePath);

						String flagPath = savePathFile
								+ "\\resources\\images\\" + stroreName;
						File localFile = new File(flagPath);
						file.transferTo(localFile);
						imageUrl = Constans.MY_IMAGEURL + stroreName;
					}

					if (StringUtil.isNotEmpty(bannerId)) {
						TopBanner topbanner;
						if (StringUtil.isEmpty(imageUrl)) {
							topbanner = new TopBanner(bannerId, community, bannerType, oldImage, title, introduce);
						} else {
							topbanner = new TopBanner(bannerId, community, bannerType, imageUrl, title, introduce);
						}
						boolean successs = topBannerDao.updateData(topbanner);
						if (successs) {
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						TopBanner topbanner = new TopBanner(bannerId, community, bannerType, imageUrl, title, introduce);
						boolean successs = topBannerDao.addData(topbanner);
						if (successs) {
							result.put("success", "添加成功!");
						} else {
							result.put("errorMsg", "添加失败!");
						}
					}
					/*
					 * 接收到的文件转移到给定的目标文件。
					 * 这可以移动文件系统中的文件,复制文件系统中的文件或内存内容保存到目标文件。如果目标文件已经存在,它将被删除。
					 * 如果文件系统中的文件被移动,不能再次调用该操作。因此,调用这个方法只有一次能够处理任何存储机制。
					 */
				}
			}
		}
		ResponseUtil.write(response, result);
	}
	
	@RequestMapping(value="/deleteTopBanner")
	public void deleteTopBanner(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession().getServletContext()
				.getRealPath("/");

		boolean deleteInfo = topBannerDao.deleteData(delIds, imagePath);
		if (deleteInfo) {
			result.put("success", "删除成功");
		} else {
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}

}
