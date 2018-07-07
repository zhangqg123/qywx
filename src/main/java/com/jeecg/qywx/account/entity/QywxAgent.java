package com.jeecg.qywx.account.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeecg.qywx.util.SystemUtil;

/**
 * 描述：</b>QywxAgent:应用信息表<br>
 * 实体定义规则
 * 字段不允许存在基本类型，必须都是包装类型(因为基本类型有默认值)
 * 基本数据类型  包装类 byte Byte boolean Boolean short Short char Character int Integer long Long float Float double  Double 
 * @author Alex
 * @since：2016年03月24日 14时55分38秒 星期四 
 * @version:1.0
 */
public class QywxAgent implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String agentSecret;

	/**
	 *回调token
	 */
	private String token;
	/**
	 *回调EncodingAESKey
	 */
	private String encodingAESKey;
	
	 *AccessToken
	 */
	private String accessToken;
	
	private String jsTicket;
	
	public String getId() {
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}
	public String getEncodingAESKey() {
		return encodingAESKey;
	}
	public String getAgentSecret() {
		return agentSecret;
	}
	public void setAgentSecret(String agentSecret) {
		this.agentSecret = agentSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getJsTicket() {
		return jsTicket;
	}
	public void setJsTicket(String jsTicket) {
		this.jsTicket = jsTicket;
	}
}
