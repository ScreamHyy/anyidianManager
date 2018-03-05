package com.anyidian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页左边导航栏菜单
 * @author Administrator
 *
 */
@Controller
public class HomeMenuController {

	@RequestMapping(value="/repairList")
	public ModelAndView repairList() {
		return new ModelAndView("repairList");
	}

	@RequestMapping(value="/bill")
	public ModelAndView paymentList() {
		return new ModelAndView("bill");
	}

	@RequestMapping(value="/complaintProposal")
	public ModelAndView complaintProposal() {
		return new ModelAndView("complaints");
	}

	@RequestMapping(value="/committeeNotice")
	public ModelAndView committeeNotice() {
		return new ModelAndView("committeeNotice");
	}
	
	@RequestMapping(value="/lifeCircle")
	public ModelAndView lifeCircle() {
		return new ModelAndView("lifeCircle");
	}
	
	@RequestMapping(value="/helper")
	public ModelAndView helper() {
		return new ModelAndView("helper");
	}

	@RequestMapping(value="/newHouse")
	public ModelAndView newHouse() {
		return new ModelAndView("newHouse");
	}
	
	@RequestMapping(value="/oldHouse")
	public ModelAndView oldHouse() {
		return new ModelAndView("oldHouse");
	}
	
	@RequestMapping(value="/topBanner")
	public ModelAndView topBanner(){
		return new ModelAndView("topBanner");
	}

	@RequestMapping(value="/nationalMarketing")
	public ModelAndView nationalMarketing() {
		return new ModelAndView("nationalMarketing");
	}

	@RequestMapping(value="/favouredPolicy")
	public ModelAndView favouredPolicy() {
		return new ModelAndView("favouredPolicy");
	}

	@RequestMapping(value="/newStatus")
	public ModelAndView newStatus() {
		return new ModelAndView("newStatus");
	}

	@RequestMapping(value="/industryNews")
	public ModelAndView industryNews() {
		return new ModelAndView("industryNews");
	}

	@RequestMapping(value="/policiesRegulations")
	public ModelAndView policiesRegulations() {
		return new ModelAndView("policiesRegulations");
	}

	@RequestMapping(value="/biddingInfo")
	public ModelAndView biddingInfo() {
		return new ModelAndView("biddingInfo");
	}

	@RequestMapping(value="/noticeBulletin")
	public ModelAndView noticeBulletin() {
		return new ModelAndView("noticeBulletin");
	}

	@RequestMapping(value="/communityBulletin")
	public ModelAndView communityBulletin() {
		return new ModelAndView("communityBulletin");
	}

	@RequestMapping(value="/volunteerRecruitment")
	public ModelAndView volunteerRecruitment() {
		return new ModelAndView("volunteerRecruitment");
	}

	@RequestMapping(value="/community")
	public ModelAndView community() {
		return new ModelAndView("community");
	}
	
	@RequestMapping(value="/repairMan")
	public ModelAndView repairMan() {
		return new ModelAndView("repairMan");
	}
	
	@RequestMapping(value="/repairItem")
	public ModelAndView propertyPhone() {
		return new ModelAndView("repairItem");
	}

	@RequestMapping(value="/friendCircle")
	public ModelAndView friendCircle() {
		return new ModelAndView("friendCircle");
	}
	
	@RequestMapping(value="/popularityList")
	public ModelAndView popularityList() {
		return new ModelAndView("popularityList");
	}
	
	@RequestMapping(value="/interestGroup")
	public ModelAndView interestGroup() {
		return new ModelAndView("interestGroup");
	}
	
	@RequestMapping(value="/manager")
	public ModelAndView managerInfo(){
		return new ModelAndView("manager");
	}

	@RequestMapping(value="/user")
	public ModelAndView gradeInfoManage() {
		return new ModelAndView("user");
	}

	@RequestMapping(value="/convertibilityRecord")
	public ModelAndView convertibilityRecord() {
		return new ModelAndView("convertibilityRecord");
	}
	
	@RequestMapping(value="/billStatistics")
	public ModelAndView billStatistics() {
		return new ModelAndView("billStatistics");
	}

	@RequestMapping(value="/businessManage")
	public ModelAndView serviceStation() {
		return new ModelAndView("businessManage");
	}

	@RequestMapping(value="/integralMall")
	public ModelAndView integralMall() {
		return new ModelAndView("integralMall");
	}

	@RequestMapping(value="/commodity")
	public ModelAndView commodity() {
		return new ModelAndView("commodity");
	}

	@RequestMapping(value="/shoppingCart")
	public ModelAndView shoppingCart() {
		return new ModelAndView("shoppingCart");
	}

	@RequestMapping(value="/order")
	public ModelAndView order() {
		return new ModelAndView("order");
	}

}
