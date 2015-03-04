package com.jincaizi.bean;

public class KjxxRecordEntity {
	String lotteryName;//彩票名称
	String lotteryType; // 彩票类型
	String qiHao; // 期号或者比赛日
	String time; // 开奖时间
	String kjResult; // 开奖结果

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
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

	public String getKjResult() {
		return kjResult;
	}

	public void setKjResult(String kjResult) {
		this.kjResult = kjResult;
	}

}
