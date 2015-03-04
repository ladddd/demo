package com.jincaizi.bean;

import java.io.Serializable;

/**
 * 账户明细实体
 * @author yj
 *
 */
public class AccountRecordEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 585484542679861408L;
	String transTime;//交易时间
    String transType;//交易类型
    String transInCome;//交易进账
    String transLeft;//账户余额
    String transCost;//交易出账
    String transFreeze; //冻结
    String transUnfreeze;//解冻
    String transFreezeAmount;//冻结总额
    String transDescription;//交易描述
    
	public String getTransFreeze() {
		return transFreeze;
	}
	public void setTransFreeze(String transFreeze) {
		this.transFreeze = transFreeze;
	}
	public String getTransUnfreeze() {
		return transUnfreeze;
	}
	public void setTransUnfreeze(String transUnfreeze) {
		this.transUnfreeze = transUnfreeze;
	}
	public String getTransFreezeAmount() {
		return transFreezeAmount;
	}
	public void setTransFreezeAmount(String transFreezeAmount) {
		this.transFreezeAmount = transFreezeAmount;
	}
	public String getTransDescription() {
		return transDescription;
	}
	public void setTransDescription(String transDescription) {
		this.transDescription = transDescription;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransInCome() {
		return transInCome;
	}
	public void setTransInCome(String transInCome) {
		this.transInCome = transInCome;
	}
	public String getTransLeft() {
		return transLeft;
	}
	public void setTransLeft(String transLeft) {
		this.transLeft = transLeft;
	}
	public String getTransCost() {
		return transCost;
	}
	public void setTransCost(String transCost) {
		this.transCost = transCost;
	}
	
}
