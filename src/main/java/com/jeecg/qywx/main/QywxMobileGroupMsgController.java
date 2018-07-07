package com.jeecg.qywx.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.logger.Logger;
import org.jeecgframework.p3.core.logger.LoggerFactory;
import org.jeecgframework.p3.core.page.SystemTools;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.jeecgframework.p3.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.qywx.account.dao.QywxAccountDao;
import com.jeecg.qywx.account.dao.QywxAgentDao;
import com.jeecg.qywx.account.entity.QywxAccount;
import com.jeecg.qywx.account.entity.QywxAgent;
import com.jeecg.qywx.account.service.AccountService;
import com.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeecg.qywx.api.base.JwParamesAPI;
import com.jeecg.qywx.api.core.common.AccessToken;
import com.jeecg.qywx.api.message.JwMessageAPI;
import com.jeecg.qywx.api.message.vo.Image;
import com.jeecg.qywx.api.message.vo.ImageEntity;
import com.jeecg.qywx.api.message.vo.News;
import com.jeecg.qywx.api.message.vo.NewsArticle;
import com.jeecg.qywx.api.message.vo.NewsEntity;
import com.jeecg.qywx.api.message.vo.Text;
import com.jeecg.qywx.api.message.vo.TextEntity;
import com.jeecg.qywx.base.dao.QywxGroupDao;
import com.jeecg.qywx.base.dao.QywxGzuserinfoDao;
import com.jeecg.qywx.base.dao.QywxMessagelogDao;
import com.jeecg.qywx.base.entity.QywxGroup;
import com.jeecg.qywx.base.entity.QywxGzuserinfo;
import com.jeecg.qywx.base.entity.QywxMessagelog;
import com.jeecg.qywx.sucai.dao.QywxNewsitemDao;
import com.jeecg.qywx.sucai.dao.QywxNewstemplateDao;
import com.jeecg.qywx.sucai.entity.QywxNewsitem;
import com.jeecg.qywx.sucai.entity.QywxNewstemplate;
import com.jeecg.qywx.util.ConfigUtil;
import com.jeecg.qywx.util.QywxAccessToken;
import com.jeecg.qywx.util.QywxJSSDKSignUtil;
import com.jeecg.qywx.util.ResourceUtils;
import com.jeecg.qywx.util.SystemUtil;


 /**
 * 描述：</b>QywxMenuController<br>自定义菜单表
 * @author p3.jeecg
 * @since：2016年03月28日 13时37分49秒 星期一 
 * @version:1.0
 */
@Controller
@RequestMapping("/qywx/mobileGroupMsg")
public class QywxMobileGroupMsgController extends BaseController{
  private static final Logger logger = LoggerFactory.getLogger(QywxMobileGroupMsgController.class);
  @Autowired
  private QywxAgentDao qywxAgentDao;
  @Autowired
  private QywxNewstemplateDao qywxNewstemplateDao;
  @Autowired
  private QywxAccountDao qywxAccountDao;
  @Autowired 
   private AccountService accountService;
  @Autowired
  private QywxNewsitemDao qywxNewsitemDao;
  @Autowired
  private QywxGroupDao qywxGroupDao;
  @Autowired
  private QywxMessagelogDao qywxMessagelogDao;
  @Autowired
  private QywxGzuserinfoDao qywxGzuserinfoDao;
	/**
	 * 跳转到编辑页面
	 * @return
	 */
	
//	@RequestMapping(params="jssdkimg",method ={RequestMethod.GET, RequestMethod.POST})
//	public void jssdkimg(@ModelAttribute QywxGroup group , HttpServletResponse response, HttpServletRequest request) throws Exception{
//		StringBuffer requestURL = request.getRequestURL();
//		System.out.println(requestURL.toString() + "==");
//		String queryString = request.getQueryString();
//		String url2 = "http://" + request.getServerName() // 服务器地址
//				+ request.getContextPath() // 项目名称
//				+ request.getServletPath(); // 请求页面或其他地址
//		if (queryString != null) {
//			url2 += "?" + queryString;
//		}
//		AccessToken accessToken = accountService.getAccessToken("1000007");
//
//		Map<String, String> sign = accountService.getSign(url2,accessToken.getAccesstoken());
//		VelocityContext velocityContext = new VelocityContext();
//		velocityContext.put("appId",sign.get("corpId"));
//		velocityContext.put("nonceStr", sign.get("nonceStr"));
//		velocityContext.put("timestamp", sign.get("timestamp"));
//		velocityContext.put("signature", sign.get("signature"));
//		String viewName = "qywx/main/jssdkimg.vm"; 
//		ViewVelocity.view(request,response,viewName,velocityContext);
//	}
		
	//跳转到图文信息详细页面
	@RequestMapping(params="getAllUploadNewsTemplate",method ={RequestMethod.GET, RequestMethod.POST})
	public void getAllUploadNewsTemplate(HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		 VelocityContext velocityContext = new VelocityContext();
		 String viewName = "qywx/msg/showGroupMessageNews.vm";
		  String symbol = request.getParameter("symbol");
		  //选择图文素材
		if("page".equals(symbol)){
		//查询所有模版
			List<QywxNewstemplate> templateList = qywxNewstemplateDao.getAllQywxNewstemplate();
			//遍历模版
			//遍历每条模搬对应的子表记录
			for(QywxNewstemplate  template : templateList){
				String templateId=template.getId();
				List <QywxNewsitem>	item=qywxNewsitemDao.getALLNews(templateId);
				template.setiNewsitem(item);
	        }
		    velocityContext.put("templateList", templateList);
		}	
		
//		String yuming=ConfigUtil.getProperty("ftp_img_domain");
//		velocityContext.put("yuming", yuming);
		try {
			ViewVelocity.view(request,response,viewName,velocityContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// 群发信息
	@RequestMapping(params = "toMobileGroupTextSend", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public AjaxJson toMobileGroupTextSend(HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			JSONObject receive = null;
			int receiveCode=-1;
			String fromUserId=(String) request.getSession().getAttribute("QY_USERID");
//			String fromUserId="zhangqg";
			//发送为全部用户时候
			String touserids="";
			//用户id
			String touserid = request.getParameter("touserId");
			String toallUser=request.getParameter("toModel");
			if("2".equals(toallUser)){
				touserids="@all";
			}else{
				touserids=touserid.replace(",", "|");
			}
			//部门id
			String toparty = null;
			String topartys = null;
			if(touserid==null||touserid ==""){
				toparty = request.getParameter("toparty");// 部门Id
				topartys = toparty.replaceAll(",", "|");
			}
			// 把它拼接成用户列表格
//			String toAgent = request.getParameter("toAgent");// 应用id
			String toAgent=ConfigUtil.getProperty("tssqAgentId");
			Integer agenid = null;
			if (toAgent != null) {
				agenid = Integer.valueOf(toAgent);
			}
			String msgtype = request.getParameter("msgtype");// 类型
			String media_id = request.getParameter("media_id");// 类型
			String param = request.getParameter("param");// 文本框的内容
			QywxMessagelog allmessage = new QywxMessagelog();
			if ("text".equals(msgtype)) {
				Text text = new Text();
				text.setMsgtype(msgtype);
				text.setAgentid(agenid);// 企业应用id整型
				text.setToparty(topartys);// 部门id可以多个部门
				text.setTouser(touserids);//用户成员列表
				TextEntity textentity = new TextEntity();
				textentity.setContent(param);
				text.setText(textentity);
				//--update---author:scott-----date:20161217------------for:获取企业号TOKERN方式改成活的--
				//AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.secret);
				AccessToken accessToken = accountService.getAccessToken(agenid.toString());
				//--update---author:scott-----date:20161217------------for:获取企业号TOKERN方式改成活的--
				receive = JwMessageAPI.sendTextMessage(text, accessToken.getAccesstoken());
				logger.info("message+sendTextMessage",receive );
				// update-begin--Author:malimei Date:2016525 for：添加日志记录
				// 插入日志的数据
				allmessage.setTopartysId(toparty);// 部门id多个用逗号隔开
				allmessage.setFromUserId(fromUserId);
				allmessage.setWxAgentId(toAgent);// 应用id
				allmessage.setMessageType(msgtype);// 消息类型
				allmessage.setMessageContent(param);// 文本消息的内容
				allmessage.setReceiveMessage(receive.toJSONString());
				String randomSeed = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
				allmessage.setId(randomSeed);
				allmessage.setCreateDate(new Date());
				qywxMessagelogDao.insert(allmessage);
				// update-end--Author:malimei Date:2016525 for：添加日志记录
				String code = receive.getString("errcode");
				if("0".equals(code)){
					j.setSuccess(true);
					j.setObj("sucess");
					j.setMsg("发送成功");
				}
			}
			
			if ("image".equals(msgtype)) {
//				Text text = new Text();
				Image image =new Image();
				
				image.setMsgtype(msgtype);
				image.setAgentid(agenid);// 企业应用id整型
				image.setToparty(topartys);// 部门id可以多个部门
				image.setTouser(touserids);//用户成员列表
				
				ImageEntity imageentity = new ImageEntity();
				imageentity.setMedia_id(media_id);
				image.setImage(imageentity);
				//--update---author:scott-----date:20161217------------for:获取企业号TOKERN方式改成活的--
				//AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.secret);
				AccessToken accessToken = accountService.getAccessToken(agenid.toString());
				//--update---author:scott-----date:20161217------------for:获取企业号TOKERN方式改成活的--
				receiveCode = JwMessageAPI.sendImageMessage(image, accessToken.getAccesstoken(),toAgent);
				System.out.println("....receiveCode..."+receiveCode);
				logger.info("message+sendImageMessage",receiveCode );
				// update-begin--Author:malimei Date:2016525 for：添加日志记录
				// 插入日志的数据
				allmessage.setTopartysId(toparty);// 部门id多个用逗号隔开
				allmessage.setFromUserId(fromUserId);
				allmessage.setWxAgentId(toAgent);// 应用id
				allmessage.setMessageType(msgtype);// 消息类型
				allmessage.setMessageContent(param);// 文本消息的内容
				allmessage.setReceiveMessage(receiveCode+"");
				String randomSeed = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
				allmessage.setId(randomSeed);
				allmessage.setCreateDate(new Date());
				qywxMessagelogDao.insert(allmessage);
				// update-end--Author:malimei Date:2016525 for：添加日志记录
//				String code = receive.getString("errcode");
				if(0==receiveCode){
					j.setSuccess(true);
					j.setObj("sucess");
					j.setMsg("发送成功");
				}
			}
			
			// 发送图文信信息
			String templateId = request.getParameter("templateId");
			if ("news".equals(msgtype)) {
				// 获取图文信息
				List<QywxNewsitem> item = qywxNewsitemDao.getALLNews(templateId);
				News news = new News();
				news.setToparty(topartys);// 部门Id
				news.setTouser(touserids);//用户列表id群发
				news.setMsgtype(msgtype);// 参数类型
				news.setAgentid(agenid);// 企业应用id整型

				List<NewsArticle> ls = new ArrayList<NewsArticle>();
				for (int i = 0; i < item.size(); i++) {
					String picurl = item.get(i).getImagePath();
					String title = item.get(i).getTitle();
					String description = item.get(i).getDescription();
					String url = item.get(i).getUrl();
					NewsArticle newsarticle = new NewsArticle();
					newsarticle.setDescription(description);
					// 在配置文件中配置路径
					String domain = ConfigUtil.getProperty("domain");
					newsarticle.setPicurl(domain + "/" + picurl);
					newsarticle.setTitle(title);
					newsarticle.setUrl(domain + "/qywx/qywxNewsitem.do?goContent&id=" + item.get(i).getId());
					ls.add(newsarticle);
				}
				NewsEntity newsEntity = new NewsEntity();
				newsEntity.setArticles(ls.toArray(new NewsArticle[ls.size()]));
				news.setNews(newsEntity);
				//--update---author:scott-----date:20161217------------for:获取企业号TOKERN方式改成活的--
				//AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.secret);// 获取token值
				AccessToken accessToken = accountService.getAccessToken(agenid.toString());
				//--update---author:scott-----date:20161217------------for:获取企业号TOKERN方式改成活的--
				
				receive = JwMessageAPI.sendNewsMessage(news, accessToken.getAccesstoken());
				 logger.info("message+sendNewsMessage",receive );
				 // update-begin--Author:malimei Date:2016525 for：添加日志记录
				allmessage.setTopartysId(toparty);// 部门id多个用逗号隔开
				allmessage.setWxAgentId(toAgent);// 应用id
				allmessage.setMessageType(msgtype);// 消息类型
				allmessage.setContentId(templateId);// 图文消息的模板id
				allmessage.setReceiveMessage(receive.toJSONString());
				allmessage.setCreateDate(new Date());
				String randomSeed = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
				allmessage.setId(randomSeed);
				qywxMessagelogDao.insert(allmessage);// 插入日志
				// update-begin--Author:malimei Date:2016525 for：添加日志记录
				String code = receive.getString("errcode");
				if("0".equals(code)){
					j.setSuccess(true);
					j.setObj("sucess");
					j.setMsg("发送成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			log.error(e.toString());
			j.setSuccess(false);
			j.setMsg("发送失败");
		}
		return j;

	}
	//文本框内显示图片的详细信息
	@RequestMapping(params="toGroupNewsSend",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson toGroupNewsSend(HttpServletResponse response,HttpServletRequest request ){
		AjaxJson j = new AjaxJson();
		try{
			String templateId=request.getParameter("templateId");
			//根据templateId查出item集合。
			List<QywxNewsitem> item=  qywxNewsitemDao.getALLNews(templateId);
			j.setObj(item);
			
	} catch (Exception e) {
	    log.info(e.getMessage());
		j.setSuccess(false);
		j.setMsg("显示失败!");
	}
		return j;
	
	}
	
	@RequestMapping(params="getAuthTree",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson getAuthTree(HttpServletResponse response,HttpServletRequest request ){
		AjaxJson j = new AjaxJson();
		try{
			  List<QywxGroup> allAuthList =qywxGroupDao.getAllQywxpid();
			  List<Map> list=new ArrayList<Map>();
			for (QywxGroup  authList:allAuthList) {
				String id=authList.getId();
				String parentid = authList.getParentid();
				String name = authList.getName();
				String open="false";
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("pId", parentid);
				map.put("name", name);
				map.put("open",open );
				 list.add(map);
			}
			j.setObj(list);
			
	} catch (Exception e) {
	    log.info(e.getMessage());
		j.setSuccess(false);
		j.setMsg("显示失败!");
	}
		return j;
	
	}
	
	//跳转到用户加载页面
	@RequestMapping(params="getUserList",method ={RequestMethod.GET, RequestMethod.POST})
	public void getUserList(HttpServletResponse response,HttpServletRequest request) throws Exception{
		 VelocityContext velocityContext = new VelocityContext();
		 String viewName="qywx/msg/userMessage.vm";
		 ViewVelocity.view(request,response,viewName,velocityContext);
		 
	}
	//跳转到用户加载页面
	@RequestMapping(params="mobileUserList",method ={RequestMethod.GET, RequestMethod.POST})
	public void mobileUserList(HttpServletResponse response,HttpServletRequest request) throws Exception{
		String pid = request.getParameter("pid"); 
		VelocityContext velocityContext = new VelocityContext();
		String viewName="qywx/main/mobileUserMessage.vm";
	    velocityContext.put("pid", pid);
	    ViewVelocity.view(request,response,viewName,velocityContext);
	}
	//返回用户信息页面getUserResult
	@RequestMapping(params="getUserResult",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson getUserResult(HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String parameter = request.getParameter("pid");
		String[] namelist = parameter.split(",");
		List<List<QywxGzuserinfo>>list=new ArrayList<List<QywxGzuserinfo>>();
		for (int i = 0; i < namelist.length; i++) {
			List<QywxGzuserinfo> byDepartment = qywxGzuserinfoDao.getByDepartment(namelist[i]);
			 if(byDepartment!=null){
			    list.add(byDepartment);
			 }
		}
		j.setObj(list);
		return j;
	}
	
//	@RequestMapping(params = "getUserTempletNew", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public AjaxJson getUserTempletNew(HttpServletResponse response, HttpServletRequest request) throws Exception {
//		AjaxJson j = new AjaxJson();
//	
//		LOG.info(request, " back list");
//		String type=request.getParameter("type");
//		String parameter = request.getParameter("pid");//得到节点id即部门id
//	    String[] namelist = parameter.split(",");
//		VelocityContext velocityContext = new VelocityContext();
//		QywxGzuserinfo query=new QywxGzuserinfo();
//		if( parameter==null||"".equals(parameter)){//页面进来就加载全部用户，如果为跟节点加载全部用户,父节点为0根节点的时候
//			List<QywxGzuserinfo> allUser = qywxGzuserinfoDao.getAllUser();
//			velocityContext.put("pageInfos", allUser);
//		}
//			//节点不为空
//		for (int i = 0; i < namelist.length; i++) {// 有节点的情况
//			QywxGroup qywxGroup = qywxGroupDao.get(namelist[i]);
//			String parentid = "";
//			if (qywxGroup != null) {
//				parentid = qywxGroup.getParentid();
//			}
//			if ("0".equals(parentid)) {// 父节点为0的情况
//				List<QywxGzuserinfo> allUser = qywxGzuserinfoDao.getAllUser();
//				velocityContext.put("pageInfos", allUser);
//			} else {// 父节点不为0的情况
//				List<QywxGzuserinfo> byDepartment = qywxGzuserinfoDao.getdepartments(parameter);
//				velocityContext.put("pageInfos", byDepartment);
//			}
//		}
//		velocityContext.put("qywxGzuserinfo", query);
//		String viewName = "qywx/msg/usertemplet.vm";
//		if(type!=null&&type.equals("mobile")){
//			viewName = "qywx/main/mobileUsertemplet.vm";
//		}
//		j.setObj(ViewVelocity.getViewContent(request, response, viewName, velocityContext));
//		return j;
//	}
	
	@RequestMapping(params = "mobileUserTempletNew", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public AjaxJson mobileUserTempletNew(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(required = false, value = "pageSize", defaultValue = "10") int pageSize) throws Exception{
		AjaxJson j = new AjaxJson();
	
		LOG.info(request, " back list");
		String parameter = request.getParameter("pid");//得到节点id即部门id
//	    String[] namelist = parameter.split(",");
		VelocityContext velocityContext = new VelocityContext();
		MiniDaoPage<QywxGzuserinfo> list;
		QywxGzuserinfo query=new QywxGzuserinfo();
		if( parameter==null||"".equals(parameter)){//页面进来就加载全部用户，如果为跟节点加载全部用户,父节点为0根节点的时候
			j.setMsg("页面加载失败!");
		}else{
		    String[] namelist = parameter.split(",");
			StringBuffer sbgroup = new StringBuffer();
			for(int i=0;i<namelist.length;i++){
				List<QywxGroup> childrenGroups = qywxGroupDao.getChildrenGroup(namelist[i]);				
				for (QywxGroup qywxGroup: childrenGroups) {
					if (qywxGroup != null) {
						String id = qywxGroup.getId();
						sbgroup.append(id+",");
					}
				}
			}
			String departments = sbgroup.toString().substring(0,sbgroup.toString().length()-1);
			query.setDepartmentSql(departments);
			list =  qywxGzuserinfoDao.getByDepartments(query,pageNo,pageSize);

			//节点不为空
/*
		    for (int i = 0; i < namelist.length; i++) {// 有节点的情况
				QywxGroup qywxGroup = qywxGroupDao.get(namelist[i]);
				String parentid = "";
				if (qywxGroup != null) {
					parentid = qywxGroup.getParentid();
				}
				if ("0".equals(parentid)) {// 父节点为0的情况
					List<QywxGzuserinfo> allUser = qywxGzuserinfoDao.getAllUser();
					velocityContext.put("pageInfos", allUser);
				} else {// 父节点不为0的情况
					List<QywxGzuserinfo> byDepartment = qywxGzuserinfoDao.getdepartments(parameter);
					velocityContext.put("pageInfos", byDepartment);
				}
			}
*/			
			velocityContext.put("pageInfos", SystemTools.convertPaginatedList(list));
			velocityContext.put("qywxGzuserinfo", query);
			velocityContext.put("pid", parameter);
			String viewName = "qywx/main/mobileUsertemplet.vm";
			j.setObj(ViewVelocity.getViewContent(request, response, viewName, velocityContext));
		}
		return j;
	}
	
}


