package com.anyidian.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.anyidian.dao.NewStatusDao;
import com.anyidian.model.Constans;
import com.anyidian.model.NewStatus;
import com.anyidian.model.Notice;
import com.anyidian.model.PageBean;
import com.anyidian.util.DateUtil;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class NewStatusController {
	private NewStatus newStatus;
	private List<NewStatus> mList;
	private String json;

	@Autowired
	private NewStatusDao newStatusDao;

	public NewStatusDao getNewStatusDao() {
		return newStatusDao;
	}

	public void setNewStatusDao(NewStatusDao newStatusDao) {
		this.newStatusDao = newStatusDao;
	}

	/**
	 * 
	 * 查询所有数据
	 * 
	 * @author ScreamHyy
	 * @param request
	 * @param response
	 * @throws Exception
	 * 
	 * 
	 * **/
	@RequestMapping(value = {"/newStatusListData", "/favouredPolicyListData",
			"/industryNewsListData", "/policiesRegulationsListData",
			"/biddingInfoListData", "/noticeBulletinListData",
			"/volunteerRecruitmentListData" })
	public void newStatusList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));

		String title = request.getParameter("title");
		String statusType = request.getParameter("statusType");
		String date = request.getParameter("date");

		newStatus = new NewStatus();
		if (StringUtil.isNotEmpty(statusType)) {
			newStatus.setStatusType(statusType);
		}
		if (StringUtil.isNotEmpty(date)) {
			newStatus.setDate(date);
		}
		if (StringUtil.isNotEmpty(title)) {
			newStatus.setTitle(title);
		}

		mList = new ArrayList<NewStatus>();
		mList = newStatusDao.queryData(newStatus, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
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
	@RequestMapping(value = {"/deleteNewStatus", "/deleteFavouredPolicy",
			"/deleteIndustryNews", "/deletePoliciesRegulations",
			"/deleteBiddingInfo", "/deleteNoticeBulletin",
			"/deleteVolunteerRecruitment" })
	public void deleteNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession().getServletContext()
				.getRealPath("/");

		boolean deleteInfo = newStatusDao.deleteData(delIds, imagePath);
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
	@RequestMapping(value = {"/addNewStatusInfo", "/addFavouredPolicyInfo",
			"/addIndustryNewsInfo", "/addPoliciesRegulationsInfo",
			"/addBiddingInfoInfo", "/addNoticeBulletinInfo",
			"/addVolunteerRecruitmentInfo" })
	public ModelAndView addNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String statusId = request.getParameter("statusId");
		Map model = new HashMap();
		if (StringUtil.isNotEmpty(statusId)) {
			NewStatus newStatus = newStatusDao.queryNewStatusById(statusId);
			model.put("status", newStatus);
		}
		ModelAndView mav = null;
		String url = request.getRequestURL().toString();
		String redirct = url.substring(url.lastIndexOf("/") + 1);
		if(redirct.equals("addNewStatusInfo")){
			mav = new ModelAndView("addNewStatus",model);
			return mav;
		}
		if(redirct.equals("addFavouredPolicyInfo")){
			mav = new ModelAndView("addFavouredPolicy",model);
			return mav;
		}
		if(redirct.equals("addIndustryNewsInfo")){
			mav = new ModelAndView("addIndustryNews",model);
			return mav;
		}
		if(redirct.equals("addPoliciesRegulationsInfo")){
			mav = new ModelAndView("addPoliciesRegulations",model);
			return mav;
		}
		if(redirct.equals("addBiddingInfoInfo")){
			mav = new ModelAndView("addBiddingInfo",model);
			return mav;
		}
		if(redirct.equals("addNoticeBulletinInfo")){
			mav = new ModelAndView("addNoticeBulletin",model);
			return mav;
		}
		if(redirct.equals("addVolunteerRecruitmentInfo")){
			mav = new ModelAndView("addVolunteerRecruitment",model);
			return mav;
		}
//		switch (redirct) {
//		case "addNewStatusInfo":
//			mav = new ModelAndView("addNewStatus", model);
//			break;
//		case "addFavouredPolicyInfo":
//			mav = new ModelAndView("addFavouredPolicyInfo", model);
//			break;
//		case "addIndustryNewsInfo":
//			mav = new ModelAndView("addIndustryNewsInfo", model);
//			break;
//		case "addPoliciesRegulationsInfo":
//			mav = new ModelAndView("addPoliciesRegulationsInfo", model);
//			break;
//		case "addBiddingInfoInfo":
//			mav = new ModelAndView("addBiddingInfoInfo", model);
//			break;
//		case "addNoticeBulletinInfo":
//			mav = new ModelAndView("addNoticeBulletinInfo", model);
//			break;
//		case "addVolunteerRecruitmentInfo":
//			mav = new ModelAndView("addVolunteerRecruitmentInfo", model);
//			break;
//		}
		return null;
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
	@RequestMapping(value = { "/saveNewStatus", "/saveFavouredPolicy",
			"/saveIndustryNews", "/savePoliciesRegulations",
			"/saveBiddingInfo", "/saveNoticeBulletin",
			"/saveCommunityBulletin", "/saveVolunteerRecruitment" })
	public void saveNewStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		String statusId = request.getParameter("statusId");
		String statusType = request.getParameter("statusType");
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

					if (StringUtil.isNotEmpty(statusId)) {
						NewStatus status;
						if (StringUtil.isEmpty(imageUrl)) {
							status = new NewStatus(statusId, statusType,
									oldImage, title, null, detail);
						} else {
							status = new NewStatus(statusId, statusType,
									imageUrl, title, null, detail);
						}
						boolean successs = newStatusDao.updateData(status);
						if (successs) {
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						NewStatus status = new NewStatus(statusId, statusType,
								imageUrl, title, null, detail);
						boolean successs = newStatusDao.addData(status,
								statusType);
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
