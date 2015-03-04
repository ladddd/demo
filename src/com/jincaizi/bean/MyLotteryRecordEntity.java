package com.jincaizi.bean;

import java.io.Serializable;

/**
 * 我的购彩记录，数据实体
 * @author zhao
 *
 */
public class MyLotteryRecordEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4434368976367274708L;

	String id;//代购id或者合买id
    String qiHao;//第几期
    String time;//时间
    String buyType;//认购类型:复式合买，复式认购，单式合买，单式认购
    String lotteryType;//认购彩票类型：双色球，排列3，排列5，竞彩足球等等
    String money;//合买认购金额或者代购金额
    String awardStatus;//奖金状态：未中奖,已中奖(输入我的具体中奖金额)
    String procedureStatus;//认购处理状态：未出票，已出票，已派奖
    String beishu;//倍数
    String betAverage;//每份金额
    String ticheng; //发起人提成
    String faqiren;//发起人
    String betAmount;//总份数
    String baodi;//保底
    String jindu;//购买进度
    String zongjine;//合买总金额
    String AllBonus;//方案的总中奖金额
    
	
	public String getAllBonus() {
		return AllBonus;
	}
	public void setAllBonus(String allBonus) {
		AllBonus = allBonus;
	}
	public String getZongjine() {
		return zongjine;
	}
	public void setZongjine(String zongjine) {
		this.zongjine = zongjine;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQiHao() {
		return qiHao;
	}
	public void setQiHao(String qiHao) {
		this.qiHao = qiHao;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBuyType() {
		return buyType;
	}
	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}
	public String getLotteryType() {
		return lotteryType;
	}
	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAwardStatus() {
		return awardStatus;
	}
	public void setAwardStatus(String awardStatus) {
		this.awardStatus = awardStatus;
	}
	public String getProcedureStatus() {
		return procedureStatus;
	}
	public void setProcedureStatus(String procedureStatus) {
		this.procedureStatus = procedureStatus;
	}
	public String getBeishu() {
		return beishu;
	}
	public void setBeishu(String beishu) {
		this.beishu = beishu;
	}
	public String getBetAverage() {
		return betAverage;
	}
	public void setBetAverage(String betAverage) {
		this.betAverage = betAverage;
	}
	public String getTicheng() {
		return ticheng;
	}
	public void setTicheng(String ticheng) {
		this.ticheng = ticheng;
	}
	public String getFaqiren() {
		return faqiren;
	}
	public void setFaqiren(String faqiren) {
		this.faqiren = faqiren;
	}
	public String getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(String betAmount) {
		this.betAmount = betAmount;
	}
	public String getBaodi() {
		return baodi;
	}
	public void setBaodi(String baodi) {
		this.baodi = baodi;
	}
	public String getJindu() {
		return jindu;
	}
	public void setJindu(String jindu) {
		this.jindu = jindu;
	}
    
}
