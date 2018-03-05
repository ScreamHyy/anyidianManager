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

import com.anyidian.dao.NewHouseDao;
import com.anyidian.model.Constans;
import com.anyidian.model.NewHouse;
import com.anyidian.model.NewStatus;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class NewHouseController {
	private NewHouse newHouse;
	private String json;
	private List<NewHouse> mList;
	
	@Autowired
	private NewHouseDao newHouseDao;
	
	public NewHouseDao getNewHouseDao() {
		return newHouseDao;
	}

	public void setNewHouseDao(NewHouseDao newHouseDao) {
		this.newHouseDao = newHouseDao;
	}

	@RequestMapping(value="newHouseListData")
	public void queryNewHouseData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		String houseId = request.getParameter("houseId");
		String title = request.getParameter("title");
		
		newHouse = new NewHouse();
		if(StringUtil.isNotEmpty(houseId)){
			newHouse.setHouseId(houseId);
		}
		if(StringUtil.isNotEmpty(title)){
			newHouse.setTitle(title);
		}
		
		mList = new ArrayList<NewHouse>();
		mList = newHouseDao.queryData(newHouse,pageBean);
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
	@RequestMapping(value="/deleteNewHouse")
	public void deleteNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession().getServletContext()
				.getRealPath("/");

		boolean deleteInfo = newHouseDao.deleteData(delIds, imagePath);
		if (deleteInfo) {
			result.put("success", "删除成功");
		} else {
			result.put("errorMsg", "服务器内部错误");
		}
		ResponseUtil.write(response, result);
	}

	/**
	 *
	 * 添加修改或新增最新资讯的标签页
	 * 
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 **/

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/addNewHouseInfo")
	public ModelAndView addNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String houseId = request.getParameter("houseId");
		Map model = new HashMap();
		if (StringUtil.isNotEmpty(houseId)) {
			NewHouse newhouse = newHouseDao.queryNewHouseById(houseId);
			model.put("newhouse", newhouse);
		}
		ModelAndView mav = new ModelAndView("addNewHouse",model);
		return mav;
	}

	/**
	 * 
	 * 保存最新资讯
	 * 
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 **/
	@RequestMapping(value = "/saveNewHouse")
	public void saveNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		String houseId = request.getParameter("houseId");
		String introduce = request.getParameter("introduce");
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		String oldImage = request.getParameter("image");
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

					if (StringUtil.isNotEmpty(houseId)) {
						NewHouse house;
						if (StringUtil.isEmpty(imageUrl)) {
							house = new NewHouse(houseId, oldImage,
									title, introduce, detail);
						} else {
							house = new NewHouse(houseId, imageUrl,
									title, introduce, detail);
						}
						boolean successs = newHouseDao.updateData(house);
						if (successs) {
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						NewHouse house = new NewHouse(houseId, imageUrl, title, introduce, detail);
						boolean successs = newHouseDao.addData(house);
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

}
