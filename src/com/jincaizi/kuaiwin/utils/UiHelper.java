package com.jincaizi.kuaiwin.utils;

import android.app.Activity;
import android.content.Intent;

import com.jincaizi.kuaiwin.buycenter.SmartFollow;
import com.jincaizi.kuaiwin.buycenter.SumbitGroupBuy;
import com.jincaizi.kuaiwin.buycenter.yilou.CustomYilou;
import com.jincaizi.kuaiwin.buycenter.yilou.SyxwYilou;

public class UiHelper {
	/**
	 * 
	 * @param activity
	 * @param zhuihaoNums
	 * @param beishu
	 * @param zhushu
	 * @param bettype
	 * @param city
	 * @param lotterytype
	 * @param maxBonus
	 * @param minBonus
	 */
   public static void startSmartFollow(Activity activity, int zhuihaoNums, int beishu, int zhushu, String bettype, String city, String lotterytype,
		   int maxBonus, int minBonus, String itemContent, String typeContent, String betCount, boolean isStopZhui) {
	   Intent intent = new Intent(activity, SmartFollow.class);
		intent.putExtra(SmartFollow.ZHUIHAOCOUNT, zhuihaoNums);
		intent.putExtra(SmartFollow.STARTTIMES, beishu);
		intent.putExtra(SmartFollow.ZHUSHU, zhushu);
		intent.putExtra(SmartFollow.CITY, city);
		intent.putExtra(SmartFollow.BETTYPE, bettype);
		intent.putExtra(SmartFollow.LOTTERYTYPE, lotterytype);
		intent.putExtra(SmartFollow.MAXBONUS, maxBonus);
		intent.putExtra(SmartFollow.MINBONUS, minBonus);
		intent.putExtra(SmartFollow.ITEMCONTENT, itemContent);
		intent.putExtra(SmartFollow.TYPECONTENT, typeContent);
		intent.putExtra(SmartFollow.BETCOUNT, betCount);
		intent.putExtra(SmartFollow.STOPZHUIHAO, isStopZhui);
		activity.startActivity(intent);
   }
   
   public static void startSyxwYilou(Activity activity, String lotteryType, String title) {
	   Intent intent = new Intent(activity, SyxwYilou.class);
	   intent.putExtra(SyxwYilou.LOTTERYTYPE, lotteryType);
	   intent.putExtra(SyxwYilou.TITLE, title);
	   activity.startActivity(intent);
   }
//   public static void startSyxwYilou(Activity activity, String lotteryType, String title) {
//	   Intent intent = new Intent(activity, CustomYilou.class);
//	   intent.putExtra(SyxwYilou.LOTTERYTYPE, lotteryType);
//	   intent.putExtra(SyxwYilou.TITLE, title);
//	   activity.startActivity(intent);
//   }
   /**
    * 
    * @param activity
    * @param fenshu
    * @param iszhuihao
    * @param city
    * @param lotterytype
    * @param qihao
    * @param mutiple
    * @param itemContent
    * @param typeContent
    * @param betCount
    * @param isStopZhui
    */
   public static void startGroupBuy(Activity activity, String curQihao, int fenshu, boolean iszhuihao, String city, String lotterytype,
		   String qihao, String mutiple, String itemContent, String typeContent, String betCount, boolean isStopZhui) {
	   Intent intent = new Intent(activity, SumbitGroupBuy.class);
		intent.putExtra(SumbitGroupBuy.FENSHU, fenshu);
		intent.putExtra("qihao", curQihao);
		intent.putExtra(SumbitGroupBuy.CITY, city);
		intent.putExtra(SumbitGroupBuy.ISZHUIHAO, iszhuihao);
		intent.putExtra(SumbitGroupBuy.LOTTERYTYPE, lotterytype);
		intent.putExtra(SumbitGroupBuy.QIHAOS, qihao);
		intent.putExtra(SumbitGroupBuy.MUTIPLES, mutiple);
		intent.putExtra(SumbitGroupBuy.ITEMCONTENT, itemContent);
		intent.putExtra(SumbitGroupBuy.TYPECONTENT, typeContent);
		intent.putExtra(SumbitGroupBuy.BETCOUNT, betCount);
		intent.putExtra(SumbitGroupBuy.STOPZHUIHAO, isStopZhui);
		activity.startActivity(intent);
   }
}
