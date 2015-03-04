package com.jincaizi.bean;

import java.io.Serializable;

public class ZuihaoRecordEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1096297467998418605L;
	String id;//追号id
	String type; // 追号的彩票类型
	String time; // 追号开始时间
	String moneyAmount;// 追号总金额
	String qihaoAmount;// 追号总期数
	String qihaoCurrent;// 当前期号
	String status;// 追号状态 如进行中，已完成，已终止
	
	String hasBetAmount;//已追金额
	String autoStop;//中奖后是否停止

	public String getHasBetAmount() {
		return hasBetAmount;
	}

	public void setHasBetAmount(String hasBetAmount) {
		this.hasBetAmount = hasBetAmount;
	}

	public String getAutoStop() {
		return autoStop;
	}

	public void setAutoStop(String autoStop) {
		this.autoStop = autoStop;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoneyAmount() {
		return moneyAmount;
	}

	public void setMoneyAmount(String moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public String getQihaoAmount() {
		return qihaoAmount;
	}

	public void setQihaoAmount(String qihaoAmount) {
		this.qihaoAmount = qihaoAmount;
	}

	public String getQihaoCurrent() {
		return qihaoCurrent;
	}

	public void setQihaoCurrent(String qihaoCurrent) {
		this.qihaoCurrent = qihaoCurrent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
