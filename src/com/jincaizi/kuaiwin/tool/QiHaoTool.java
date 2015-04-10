package com.jincaizi.kuaiwin.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QiHaoTool {
	public static String getNextQihao(String theQihao, int perYearTermCount) {
		//int year=Integer.valueOf(theQihao.substring(0,4));
		int lastNo=Integer.valueOf(theQihao.substring(4,7));
		Date myDate=new Date();
		if(lastNo==perYearTermCount){
			//myDate.setDate(myDate.getDate()+365);
			myDate.setTime(myDate.getTime() + 365 * 1000 * 60 * 60 * 24);
			lastNo=1;
		}else{
			lastNo++;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.CHINA);
		String qihao=df.format(myDate)+lastNo;
		if(lastNo<10)
		    qihao=df.format(myDate)+"00"+lastNo;
		else if(lastNo<100)
		    qihao=df.format(myDate)+"0"+lastNo;
		return qihao;
	}
	
	public static String  get11x5NextQihao(String theQihao, int perDayTermCount) {
	    int year= Integer.parseInt(theQihao.substring(0,4));
		int month=Integer.parseInt(theQihao.substring(4,6));
		int  day=Integer.parseInt(theQihao.substring(6,8));
		int lastNo=Integer.parseInt(theQihao.substring(8,10));
		Date myDate=new Date();
		//myDate.setFullYear(year,month-1,day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA); 
		Date d;
		try {
			String monthStr;
			String dayStr;
			if(month<10) {
				monthStr = "0"+month;
			} else {
				monthStr = ""+ month;
			}
			if(day<10) {
				dayStr = "0"+day;
			} else {
				dayStr = ""+ day;
			}
			d = sdf.parse(year+monthStr+dayStr);
			myDate.setTime(d.getTime());
			if(lastNo==perDayTermCount){
				myDate.setTime(d.getTime()+24*60*60*1000);
				lastNo=1;
			}else{
				lastNo++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} 
		
		String qihao=sdf.format(myDate)+lastNo;
		if(lastNo<10)qihao=sdf.format(myDate)+"0"+lastNo;
		return qihao;
	}
	public static String  getK3NextQihao(String theQihao, int perDayTermCount) {
		int year=Integer.parseInt(theQihao.substring(0,4));
		int month=Integer.parseInt(theQihao.substring(4,6));
		int day=Integer.parseInt(theQihao.substring(6,8));
		int lastNo=Integer.parseInt(theQihao.substring(8,11));
		Date myDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA); 
		Date d;
		try {
			String monthStr;
			String dayStr;
			if(month<10) {
				monthStr = "0"+month;
			} else {
				monthStr = ""+ month;
			}
			if(day<10) {
				dayStr = "0"+day;
			} else {
				dayStr = ""+ day;
			}
			d = sdf.parse(year+monthStr+dayStr);
			myDate.setTime(d.getTime());
			if(lastNo==perDayTermCount){
				myDate.setTime(d.getTime()+24*60*60*1000);
				lastNo=1;
			}else{
				lastNo++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} 
		String qihao=sdf.format(myDate)+lastNo;
		if(lastNo<10)qihao=sdf.format(myDate)+"00"+lastNo;
		else if(lastNo<100)qihao=sdf.format(myDate)+"0"+lastNo;
		return qihao;
	}
    public static String  getSscNextQihao(String theQihao, int perDayTermCount) {
        if (theQihao == null || theQihao.length() < 12)
        {
            return theQihao;
        }
    	int year=Integer.parseInt(theQihao.substring(0,4));
		int month=Integer.parseInt(theQihao.substring(4,6));
		int day=Integer.parseInt(theQihao.substring(6,8));
		int lastNo=Integer.parseInt(theQihao.substring(8,11));
		Date myDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA); 
		Date d;
		try {
			String monthStr;
			String dayStr;
			if(month<10) {
				monthStr = "0"+month;
			} else {
				monthStr = ""+ month;
			}
			if(day<10) {
				dayStr = "0"+day;
			} else {
				dayStr = ""+ day;
			}
			d = sdf.parse(year+monthStr+dayStr);
			myDate.setTime(d.getTime());
			if(lastNo==perDayTermCount){
				myDate.setTime(d.getTime()+24*60*60*1000);
				lastNo=1;
			}else{
				lastNo++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} 
		String qihao;
		if(lastNo<10)qihao=sdf.format(myDate)+"00"+lastNo;
		else if(lastNo<100)qihao=sdf.format(myDate)+"0"+lastNo;
		else qihao=sdf.format(myDate)+lastNo;
		return qihao;
	}
}
