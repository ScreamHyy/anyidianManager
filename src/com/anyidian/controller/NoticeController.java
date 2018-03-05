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
import com.anyidian.dao.NoticeDao;
import com.anyidian.model.Bill;
import com.anyidian.model.Committee;
import com.anyidian.model.Community;
import com.anyidian.model.Constans;
import com.anyidian.model.Notice;
import com.anyidian.model.PageBean;
import com.anyidian.util.ResponseUtil;
import com.anyidian.util.StringUtil;
import com.google.gson.Gson;

@Controller
public class NoticeController extends HttpServlet {

	private String json;

	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private CommunityDao communityDao;

	public NoticeDao getNoticeDao() {
		return noticeDao;
	}

	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	/**
	 * 居委会通知
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/noticeListData")
	public void committeeNoticeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));

		String noticeType = request.getParameter("noticeType");
		String community = request.getParameter("community");
		String committee = request.getParameter("committee");
		Notice notice = new Notice();
		notice.setNoticeType(noticeType);
		if (StringUtil.isNotEmpty(community)) {
			notice.setCommunity(community);
		}
		if (StringUtil.isNotEmpty(committee)) {
			notice.setCommittee(committee);
		}

		List<Notice> mList = new ArrayList<Notice>();
		mList = noticeDao.queryData(notice, pageBean);
		json = new Gson().toJson(mList);

		JSONObject result = new JSONObject();
		int total = noticeDao.dataCount(notice);
		result.put("rows", json);
		result.put("total", total);
		ResponseUtil.write(response, result);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = {"/addNoticeInfo", "/addCommunityBulletinInfo"})
	public ModelAndView addNotice(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String noticeId = request.getParameter("noticeId");
		Map model = new HashMap();
		List<Community> communityList = new ArrayList<Community>();
		List<Committee> committeeList = new ArrayList<Committee>();

		communityList = communityDao.queryCommunity();
		committeeList = communityDao.queryCommittee();
		model.put("communityList", communityList);
		model.put("committeeList", committeeList);

		if (StringUtil.isNotEmpty(noticeId)) {
			Notice notice = noticeDao.queryById(noticeId);
			model.put("notice", notice);
		}
		
		ModelAndView mav = null;
		String url = request.getRequestURL().toString();
		String redirct = url.substring(url.lastIndexOf("/") + 1);
		if(redirct.equals("addNoticeInfo")){
			mav = new ModelAndView("addNotice", model);
			return mav;
		}
		if(redirct.equals("addCommunityBulletinInfo")){
			mav = new ModelAndView("addCommunityBulletin", model);
			return mav;
		}
		return null;
		//ModelAndView mav = new ModelAndView("addNotice", model);
		//return mav;
	}

	/**
	 * 保存通知消息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/saveCommittee")
	public void saveCommittee(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		String noticeId = request.getParameter("noticeId");
		String noticeType = request.getParameter("noticeType");
		String community = request.getParameter("community");
		String committee = request.getParameter("committee");
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		String oldImage = request.getParameter("image");
		String publisher = request.getParameter("publisher");
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

					if (StringUtil.isNotEmpty(noticeId)) {
						Notice notice;
						if (StringUtil.isEmpty(imageUrl)) {
							notice = new Notice(noticeId, community,
									noticeType, committee, null, title, detail,
									oldImage, publisher);
						} else {
							notice = new Notice(noticeId, community,
									noticeType, committee, null, title, detail,
									imageUrl, publisher);
						}
						boolean successs = noticeDao.updateData(notice);
						if (successs) {
							result.put("success", "操作成功!");
						} else {
							result.put("errorMsg", "操作失败!");
						}
					} else {
						Notice notice = new Notice(noticeId, community,
								noticeType, committee, null, title, detail,
								imageUrl, publisher);
						boolean successs = noticeDao.addData(notice);
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

	/**
	 * 删除通知
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"/deleteCommittee", "/deleteCommunityBulletin"})
	public void deleteCommittee(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String delIds = request.getParameter("delIds");
		JSONObject result = new JSONObject();
		String imagePath = request.getSession().getServletContext()
				.getRealPath("/");

		boolean successs = noticeDao.deleteData(delIds, imagePath);
		if (successs) {
			result.put("success", "删除成功!");
		} else {
			result.put("errorMsg", "删除失败!");
		}
		ResponseUtil.write(response, result);
	}

}
