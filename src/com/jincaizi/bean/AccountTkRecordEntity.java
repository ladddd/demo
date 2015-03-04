package com.jincaizi.bean;

public class AccountTkRecordEntity {
String  tkTime;//提款时间
String tkStatus;//提款状态
String openSpace;//开户行
String bank;//收款银行
String bankCard;//银行卡号
String free;//手续费
String tkJine;//提款金额
public String getTkTime() {
	return tkTime;
}
public void setTkTime(String tkTime) {
	this.tkTime = tkTime;
}
public String getTkStatus() {
	return tkStatus;
}
public void setTkStatus(String tkStatus) {
	this.tkStatus = tkStatus;
}
public String getOpenSpace() {
	return openSpace;
}
public void setOpenSpace(String openSpace) {
	this.openSpace = openSpace;
}
public String getBank() {
	return bank;
}
public void setBank(String bank) {
	this.bank = bank;
}
public String getBankCard() {
	return bankCard;
}
public void setBankCard(String bankCard) {
	this.bankCard = bankCard;
}
public String getFee() {
	return free;
}
public void setFee(String free) {
	this.free = free;
}
public String getTkJine() {
	return tkJine;
}
public void setTkJine(String tkJine) {
	this.tkJine = tkJine;
}

}
