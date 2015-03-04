package com.jincaizi.kuaiwin.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {
	private static final String TAG = "Utils";
	/**
	 * 计算组合数
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static int getZuHeNum(int n, int m) {
		int fenmu = 1;
		int fenzi = 1;
		if (n < m) {
			return 0;
		}
		if (n == m) {
			return 1;
		}
		for (int i = n; i >= n - Math.min(m, n - m) + 1; i--) {
			fenmu *= i;
		}
		for (int i = 1; i <= Math.min(m, n - m); i++) {
			fenzi *= i;
		}
		return fenmu / fenzi;
	}

	/**
	 * 从n个数中，选出m个排列的可能排列数
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static int getPaiLieNum(int n, int m) {
		if (n < m) {
			return 0;
		}
		if (n == m) {
			return 1;
		}
		int result = 1;
		for (int i = n; i >= n - m + 1; i--) {
			result *= i;
		}
		return result;
	}

	public static void sortArrayList(ArrayList<String> arrayList) {
		Collections.sort(arrayList, new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				// TODO Auto-generated method stub
				return lhs.compareToIgnoreCase(rhs);
			}
		});
	}
	
 
    public static Date covertStringToTime(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date = new Date();
        try {
            date = df.parse(dateString);
        } catch (Exception ex) {
            Log.d("StringUtil", "exception");
        }
        return date;
    }
	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		if(days > 0 ) {
			return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
		} else if(hours > 0 ){
			return hours + "小时" + minutes + "分" + seconds + "秒";
		} else if(minutes>0){
			return  minutes + "分" + seconds + "秒";
		} else {
			return seconds + "秒";
		}
		
	}
	
	public static HashMap<String, String>getBetType() {
		HashMap<String,String>map = new HashMap<String, String>();
		
			map.put("DXF", "大小分");
		
			map.put("SXP", "上下单双");
		
			map.put("JQS", "总进球");
		
			map.put("BF", "比分");
		
			map.put("CBF", "比分");
		
			map.put("SPF", "胜平负");
		
			map.put("RQSPF", "让球胜平负");
		
			map.put("BQC", "半全场");
		
			map.put("HH", "混合过关");
		
			map.put("SFC", "胜分差");
		
			map.put("SF", "胜负");
		
			map.put("RFSF", "让分胜负");

		return map;
	}
	
	public static String formatDoubleForTwo(double input) throws Exception{
		//保留2位小数，四舍五入
	    BigDecimal   b   =   new   BigDecimal(Double.toString(input));  
	    double   f1   =   b.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue(); 
	    java.text.NumberFormat nf = java.text.NumberFormat.getInstance(); 
	    nf.setGroupingUsed(false); 
	    return nf.format(f1);
	}

	/**
	 * 判断当前网络模式是否为CMWAP
	 * @param context
	 * @return
	 */
	public static boolean isCmwapNet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();  
		if(netWrokInfo == null || !netWrokInfo.isAvailable()) { 
			return false;
		} else if (netWrokInfo.getTypeName().equals("mobile")
				&& netWrokInfo.getExtraInfo().equals("cmwap")){
			return true;
        }
		return false;
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		// logger.info(m.matches()+"---");
		return m.matches();
	}

	public static boolean isEmail(String email) {
		// String
		// str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		String str = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		// logger.info(m.matches()+"---");
		return m.matches();
		// String format =
		// "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
		// return email.matches(format);
	}

	/** */
	/**
	 * ======================================================================
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @return 有效：true 无效：false
	 * @throws ParseException
	 */
	public static boolean IDCardValidate(String IDStr) throws ParseException {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		// String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
		String Ai = "";

		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "号码长度应该为15位或18位。";
			Log.d(TAG,IDStr+ " -- " + IDStr.length() + " -- " + errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			Log.d(TAG,errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份

		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "生日无效。";
			Log.d(TAG,errorInfo);
			return false;
		}

		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - s.parse(
						strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			errorInfo = "生日不在有效范围。";
			Log.d(TAG,errorInfo);
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "月份无效";
			Log.d(TAG,errorInfo);
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "日期无效";
			Log.d(TAG,errorInfo);
			return false;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "地区编码错误。";
			Log.d(TAG,errorInfo);
			return false;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，最后一位字母错误";
				Log.d(TAG,errorInfo);
				return false;
			}
		} else {
			Log.d(TAG,"所在地区:" + h.get(Ai.substring(0, 2).toString()));
			Log.d(TAG,"新身份证号:" + Ai);
			return true;
		}
		// =====================(end)=====================
		Log.d(TAG,"所在地区:" + h.get(Ai.substring(0, 2).toString()));
		return true;
	}

	/**
	 * ======================================================================
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/** */
	/**
	 * ======================================================================
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
		/**//*
			 * 判断一个字符时候为数字 if(Character.isDigit(str.charAt(0))) { return true; }
			 * else { return false; }
			 */
	}

	/** */
	/**
	 * ======================================================================
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^(\\d{4})\\-(\\d{2})\\-(\\d{2})$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}



}
