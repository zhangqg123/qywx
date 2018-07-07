package com.jeecg.qywx.base.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 描述：</b>QywxMessagelog:<br>
 * 实体定义规则
 * 字段不允许存在基本类型，必须都是包装类型(因为基本类型有默认值)
 * 基本数据类型  包装类 byte Byte boolean Boolean short Short char Character int Integer long Long float Float double  Double 
 * @author p3.jeecg
 * @since：2016年05月26日 18时54分38秒 星期四 
 * @version:1.0
 */
public class QywxMessagelog implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String fromUserId;
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
}
