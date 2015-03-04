package com.jincaizi.bean;

import java.io.Serializable;

public class HemaiCenterRecordEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3156474240059191312L;
	String hemaiId;//合买ID
	String lotteryType; // 彩票类型
	String betType; // 投注类型，数字彩：如复式，单式等； 竞彩：如胜负，大小分等
	String betJindu; // 合买进度
	String betHost; // 合买发起人
	String betAmount;// 合买总金额
	String betAverage;// 每份金额
	String betLeft;// 剩余份数
	String betBaodi;//保底

	public String getHemaiId() {
		return hemaiId;
	}

	public void setHemaiId(String hemaiId) {
		this.hemaiId = hemaiId;
	}

	public String getBetBaodi() {
		return betBaodi;
	}

	public void setBetBaodi(String betBaodi) {
		this.betBaodi = betBaodi;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public String getBetJindu() {
		return betJindu;
	}

	public void setBetJindu(String betJindu) {
		this.betJindu = betJindu;
	}

	public String getBetHost() {
		return betHost;
	}

	public void setBetHost(String betHost) {
		this.betHost = betHost;
	}

	public String getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(String betAmount) {
		this.betAmount = betAmount;
	}

	public String getBetAverage() {
		return betAverage;
	}

	public void setBetAverage(String betAverage) {
		this.betAverage = betAverage;
	}

	public String getBetLeft() {
		return betLeft;
	}

	public void setBetLeft(String betLeft) {
		this.betLeft = betLeft;
	}

}
