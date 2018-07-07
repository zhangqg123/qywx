package com.jeecg.qywx.account.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.jeecgframework.p3.core.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecg.qywx.account.dao.QywxAccountDao;
import com.jeecg.qywx.account.dao.QywxAgentDao;
import com.jeecg.qywx.account.entity.QywxAccount;
import com.jeecg.qywx.account.entity.QywxAgent;
import com.jeecg.qywx.account.entity.WXjsTicket;
import com.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeecg.qywx.api.core.common.AccessToken;
import com.jeecg.qywx.auth.OAuthMessageAPI;
import com.jeecg.qywx.util.HttpRequestUtil;
import com.jeecg.qywx.util.QywxJSSDKSignUtil;
import com.jeecg.qywx.util.QywxURLUtil;
import com.jeecg.qywx.util.SystemUtil;
/**
 * 
 * 微信企业号
 *
 */
@Service
public class AccountService{
	@Autowired
	private QywxAccountDao qywxAccountDao;

	@Autowired
	private QywxAgentDao qywxAgentDao;
	
	/**
	 * 获取微信企业号 AccessToken
	 * @return
	 */
	public AccessToken getAccessToken(){
		QywxAccount qywxAccount = qywxAccountDao.get(SystemUtil.QYWX_ACCOUNT_ID);
		//--update---author:scott-----date:20161217------------for:如果默认企业号ID获取不到企业号，则取数据库中随机一条---
		if(qywxAccount==null){
			List<QywxAccount> ls = qywxAccountDao.getAllQywxAccount();
			if(ls!=null &&ls.size()>0){
				qywxAccount = ls.get(0);
			}
		}
		AccessToken accessToken=new AccessToken();
		String token = qywxAccount.getAccessToken();
		if (token != null && !"".equals(token)) {
			// 判断有效时间 是否超过2小时
			java.util.Date end = new java.util.Date();
			java.util.Date start = new java.util.Date(qywxAccount.getCreateDate()
					.getTime());
			if ((end.getTime() - start.getTime()) / 1000 / 3600 >= 2) {
				// 失效 重新获取
				accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAccount.getSecret());
				token=accessToken.getAccesstoken();
				Date getAccessTokenDate = new Date();
				qywxAccount.setAccessToken(token);
				qywxAccount.setCreateDate(getAccessTokenDate);
				qywxAccountDao.update(qywxAccount);
			} else {
//				AccessToken accessToken=new AccessToken();
				accessToken.setAccesstoken(token);
				return accessToken;
			}
		} else {
			accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAccount.getSecret());
			token=accessToken.getAccesstoken();
			Date getAccessTokenDate = new Date();
			qywxAccount.setAccessToken(token);
			qywxAccount.setCreateDate(getAccessTokenDate);
			qywxAccountDao.update(qywxAccount);
		}

		//--update---author:scott-----date:20161217------------for:如果默认企业号ID获取不到企业号，则取数据库中随机一条---
//		AccessToken accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAccount.getSecret());
		return accessToken;
	}
	
	public AccessToken getAccessToken(String agentId){
		QywxAccount qywxAccount = qywxAccountDao.get(SystemUtil.QYWX_ACCOUNT_ID);
		QywxAgent qywxAgent = qywxAgentDao.getAgent(agentId);
		//--update---author:scott-----date:20161217------------for:如果默认企业号ID获取不到企业号，则取数据库中随机一条---

		AccessToken accessToken=new AccessToken();
		String token = qywxAgent.getAccessToken();
		String jsapi_ticket = qywxAgent.getJsTicket();
		
		if (token != null && !"".equals(token)) {
			// 判断有效时间 是否超过2小时
			java.util.Date end = new java.util.Date();
			java.util.Date start = new java.util.Date(qywxAgent.getCreateDate()
					.getTime());
			if ((end.getTime() - start.getTime()) / 1000 / 3600 >= 2) {
				// 失效 重新获取
				accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAgent.getAgentSecret());
				token=accessToken.getAccesstoken();
				jsapi_ticket = getWXjsTicket(token).getJsTicket();
				Date getAccessTokenDate = new Date();
				qywxAgent.setAccessToken(token);
				qywxAgent.setJsTicket(jsapi_ticket);
				qywxAgent.setCreateDate(getAccessTokenDate);
				qywxAgentDao.update(qywxAgent);
			} else {
//				AccessToken accessToken=new AccessToken();
				accessToken.setAccesstoken(token);
				return accessToken;
			}
		} else {
			accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAgent.getAgentSecret());
			token=accessToken.getAccesstoken();
			jsapi_ticket = getWXjsTicket(token).getJsTicket();
			Date getAccessTokenDate = new Date();
			qywxAgent.setAccessToken(token);
			qywxAgent.setJsTicket(jsapi_ticket);
			qywxAgent.setCreateDate(getAccessTokenDate);
			qywxAgentDao.update(qywxAgent);
		}

		//--update---author:scott-----date:20161217------------for:如果默认企业号ID获取不到企业号，则取数据库中随机一条---
//		AccessToken accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAgent.getAgentSecret());
		return accessToken;
	}
	
	
	public WXjsTicket getWXjsTicket(String accessToken) {
		WXjsTicket wXjsTicket = null;
		String requestUrl= QywxURLUtil.JSAPIURL.replace("ACCESS_TOKEN", accessToken);
		// 发起GET请求获取凭证
		JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl, "GET", null);
		System.out.println("CommonUtil.java 调用了一次getWXjsTicket接口");
		if (null != jsonObject) {
			try {
				wXjsTicket = new WXjsTicket();
				wXjsTicket.setJsTicket(jsonObject.getString("ticket"));
				wXjsTicket.setJsTicketExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				wXjsTicket = null;
				e.printStackTrace();
				// 获取wXjsTicket失败
//				log.error("获取wXjsTicket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return wXjsTicket;
	}
	
	public Map<String, String> getSign(String url2,String token,String agentId){
		QywxAccount qywxAccount = qywxAccountDao.get(SystemUtil.QYWX_ACCOUNT_ID);
		QywxAgent qywxAgent = qywxAgentDao.getAgent(agentId);
		String jsapi_ticket=null;
		jsapi_ticket=qywxAgent.getJsTicket();
		if(jsapi_ticket ==null || "".equals(jsapi_ticket)){
			jsapi_ticket = getWXjsTicket(token).getJsTicket();			
			qywxAgent.setJsTicket(jsapi_ticket);
			qywxAgentDao.update(qywxAgent);
		}
		// 获得微信jssdk签名等
		Map<String, String> sign = QywxJSSDKSignUtil.sign(jsapi_ticket, url2);
        sign.put("corpId", qywxAccount.getCorpid());

		return sign;
	}
	
	/**
	 * 获取企业会话Token
	 * @return
	 */
	public AccessToken getConversationAccessToken(){
		 QywxAccount qywxAccount = qywxAccountDao.get(SystemUtil.QYWX_ACCOUNT_ID);
		//--update---author:scott-----date:20161217------------for:如果默认企业号ID获取不到企业号，则取数据库中随机一条---
		 if(qywxAccount==null){
			 List<QywxAccount> ls = qywxAccountDao.getAllQywxAccount();
			 if(ls!=null &&ls.size()>0){
				 qywxAccount = ls.get(0);
			 }
		 }
		//--update---author:scott-----date:20161217------------for:如果默认企业号ID获取不到企业号，则取数据库中随机一条---
		 if(StringUtil.isEmpty(qywxAccount.getConversationSecret())){
			return null; 
		 }else{
			 AccessToken accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAccount.getConversationSecret());
				return accessToken;
		 }
	}
	
	public static void main(String[] args) {
//		AccountService as=new AccountService();
//		AccessToken test = as.getAccessToken();
		AccessToken accessToken = JwAccessTokenAPI.getAccessToken("wxe38a28e07cc15eb5","FRvvkPgnyK63yjj_KAdAP0jRmkJ_SzdTQuy8zoSmHBGUlIEYtFsYPdzRVHDs_ZLa");
		System.out.println("....receive..."+accessToken);
	}

}
