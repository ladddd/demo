package com.jincaizi.kuaiwin.utils;

import android.content.Context;

import com.jincaizi.R;

public class Constants {
	 public static final String ENTITY = "lotteryEntity";
	 public static final String USERID = "userid";
	 public static final String LOTTERYDETAILTYPE ="lotteryDetailType";
	 public static final String constants = "samddyjincaizi";

		/**
		 * enum of LotteryDetailType
		 */
		public enum LotteryDetailType {
			/**
			 * 代购，合买，追号
			 */
			DAIGOU(0), HEMAI(1), ZHUIHAO(2);
			private final int mType;

			LotteryDetailType(final int i) {
				this.mType = i;
			}

			@Override
			public String toString() {
				return String.valueOf(mType);
			}

		}
	/**
	 * enum of pls type
	 */
	public enum PlsType {
		ZHIXUAN(0), ZUSAN(1), ZULIU(2), HEZHIZHIXUAN(3), HEZHIZUSAN(4), HEZHIZULIU(
				5), OTHER(6);
		private final int mPlsType;

		PlsType(final int i) {
			this.mPlsType = i;
		}

		@Override
		public String toString() {
			return String.valueOf(mPlsType);
		}

		public static PlsType getPlsType(String type) {
			if (type.equals("0")) {
				return PlsType.ZHIXUAN;
			} else if (type.equals("1")) {
				return PlsType.ZUSAN;
			} else if (type.equals("2")) {
				return PlsType.ZULIU;
			} else if (type.equals("3")) {
				return PlsType.HEZHIZHIXUAN;
			} else if (type.equals("4")) {
				return PlsType.HEZHIZUSAN;
			} else if (type.equals("5")){
				return PlsType.HEZHIZULIU;
			} else {
				return PlsType.OTHER;
			}
		}

	}

	/**
	 * enum of 十一运夺金 type
	 */
	public enum ShiyiyunType {
		ANYTWO(0), ANYTHREE(1), ANYFOUR(2), ANYFIVE(3), ANYSIX(4), ANYSEVEN(5), ANYEIGHT(
				6), FRONTONEZHI(7), FRONTTWOZHI(8), FRONTTWOZU(9), FRONTTHREEZHI(
				10), FRONTTHREEZU(11),ANYTWODRAG(12), ANYTHREEDRAG(13), ANYFOURDRAG(14), ANYFIVEDRAG(15), ANYSIXDRAG(16), ANYSEVENDRAG(17), ANYEIGHTDRAG(
						18),FRONTTWOZUDRAG(19),FRONTTHREEZUDRAG(20) ;
		private final int mPlsType;

		ShiyiyunType(final int i) {
			this.mPlsType = i;
		}

		@Override
		public String toString() {
			return String.valueOf(mPlsType);
		}

		public static ShiyiyunType getSyyType(String type) {
			if (type.equals("0")) {
				return ShiyiyunType.ANYTWO;
			} else if (type.equals("1")) {
				return ShiyiyunType.ANYTHREE;
			} else if (type.equals("2")) {
				return ShiyiyunType.ANYFOUR;
			} else if (type.equals("3")) {
				return ShiyiyunType.ANYFIVE;
			} else if (type.equals("4")) {
				return ShiyiyunType.ANYSIX;
			} else if (type.equals("5")) {
				return ShiyiyunType.ANYSEVEN;
			} else if (type.equals("6")) {
				return ShiyiyunType.ANYEIGHT;
			} else if (type.equals("7")) {
				return ShiyiyunType.FRONTONEZHI;
			} else if (type.equals("8")) {
				return ShiyiyunType.FRONTTWOZHI;
			} else if (type.equals("9")) {
				return ShiyiyunType.FRONTTWOZU;
			} else if (type.equals("10")) {
				return ShiyiyunType.FRONTTHREEZHI;
			} else if(type.equals("11")){
				return ShiyiyunType.FRONTTHREEZU;
			} else if (type.equals("12")) {
				return ShiyiyunType.ANYTWODRAG;
			} else if (type.equals("13")) {
				return ShiyiyunType.ANYTHREEDRAG;
			} else if (type.equals("14")) {
				return ShiyiyunType.ANYFOURDRAG;
			} else if (type.equals("15")) {
				return ShiyiyunType.ANYFIVEDRAG;
			} else if (type.equals("16")) {
				return ShiyiyunType.ANYSIXDRAG;
			} else if (type.equals("17")) {
				return ShiyiyunType.ANYSEVENDRAG;
			} else if (type.equals("18")) {
				return ShiyiyunType.ANYEIGHTDRAG;
			} else if (type.equals("19")) {
				return ShiyiyunType.FRONTTWOZUDRAG;
			} else{
				return ShiyiyunType.FRONTTHREEZUDRAG;
			} 
		}
		public static int getPerAward(String type) {
			if (type.equals("0")) {
				//return ShiyiyunType.ANYTWO;
				return 6;
			} else if (type.equals("1")) {
				//return ShiyiyunType.ANYTHREE;
				return 19;
			} else if (type.equals("2")) {
				//return ShiyiyunType.ANYFOUR;
				return 78;
			} else if (type.equals("3")) {
				//return ShiyiyunType.ANYFIVE;
				return 540;
			} else if (type.equals("4")) {
				//return ShiyiyunType.ANYSIX;
				return 90;
			} else if (type.equals("5")) {
				//return ShiyiyunType.ANYSEVEN;
				return 26;
			} else if (type.equals("6")) {
				//return ShiyiyunType.ANYEIGHT;
				return 9;
			} else if (type.equals("7")) {
				//return ShiyiyunType.FRONTONEZHI;
				return 13;
			} else if (type.equals("8")) {
				//return ShiyiyunType.FRONTTWOZHI;
				return 130;
			} else if (type.equals("9")) {
				//return ShiyiyunType.FRONTTWOZU;
				return 65;
			} else if (type.equals("10")) {
				//return ShiyiyunType.FRONTTHREEZHI;
				return 1170;
			} else if(type.equals("11")){
				//return ShiyiyunType.FRONTTHREEZU;
				return 195;
			} else if (type.equals("12")) {
				//return ShiyiyunType.ANYTWODRAG;
				return 6;
			} else if (type.equals("13")) {
				//return ShiyiyunType.ANYTHREEDRAG;
				return 19;
			} else if (type.equals("14")) {
				//return ShiyiyunType.ANYFOURDRAG;
				return 78;
			} else if (type.equals("15")) {
				//return ShiyiyunType.ANYFIVEDRAG;
				return 540;
			} else if (type.equals("16")) {
				//return ShiyiyunType.ANYSIXDRAG;
				return 90;
			} else if (type.equals("17")) {
				//return ShiyiyunType.ANYSEVENDRAG;
				return 26;
			} else if (type.equals("18")) {
				//return ShiyiyunType.ANYEIGHTDRAG;
				return 9;
			} else if (type.equals("19")) {
				//return ShiyiyunType.FRONTTWOZUDRAG;
				return 65;
			} else{
				//return ShiyiyunType.FRONTTHREEZUDRAG;
				return 195;
			} 
		}
		
		public static String getSelectedType(ShiyiyunType type) {
			if (type == ANYTWO) {
				return "R2";
			} else if (type == ANYTHREE) {
				return "R3";
			} else if (type == ANYFOUR) {
				return "R4";
			} else if (type == ANYFIVE) {
				return "R5";
			} else if (type == ANYSIX) {
				return "R6";
			} else if (type == ANYSEVEN) {
				return "R7";
			} else if (type == ANYEIGHT) {
				return "R8";
			} else if (type == FRONTONEZHI) {
				return "Q1";
			} else if (type == FRONTTWOZHI) {
				return "Q2";
			} else if (type == FRONTTWOZU) {
				return "Z2";
			} else if (type == FRONTTHREEZHI) {
				return "Q3";
			} else if(type == FRONTTHREEZU){
				return "Z3";
			} else if (type == ANYTWODRAG) {
				return "R2";
			} else if (type == ANYTHREEDRAG) {
				return "R3";
			} else if (type == ANYFOURDRAG) {
				return "R4";
			} else if (type == ANYFIVEDRAG) {
				return "R5";
			} else if (type == ANYSIXDRAG) {
				return "R6";
			} else if (type == ANYSEVENDRAG) {
				return "R7";
			} else if (type == ANYEIGHTDRAG) {
				return "R8";
			} else if (type == FRONTTWOZUDRAG) {
				return "Z2";
			} else{
				return "Z3";
			} 
		}

	}
	
	/**
	 * enum of pls type江西11选5" ,"广东11选5","安徽11选5", "重庆11选5", "辽宁11选5", "上海11选5","黑龙江11选5", "江苏快3",  "安徽快3", "内蒙古快3",
			"重庆时时彩",   "江西时时彩"， “十一运夺金”};
	 */
	public enum City {
		jiangxi(0), guangdong(1), anhui(2), chongqing(3), liaoning(4), shanghai(
				5), heilongjiang(6), neimenggu(7), jiangsu(8), shandong(9);
		private final int mCity;

		City(final int i) {
			this.mCity = i;
		}

		@Override
		public String toString() {
			return String.valueOf(mCity);
		}

		public static City getCity(String type) {
			if (type.equals("0")) {
				return City.jiangxi;
			} else if (type.equals("1")) {
				return City.guangdong;
			} else if (type.equals("2")) {
				return City.anhui;
			} else if (type.equals("3")) {
				return City.chongqing;
			} else if (type.equals("4")) {
				return City.liaoning;
			} else if (type.equals("5")){
				return City.shanghai;
			} else if(type.equals("6")){
				return City.heilongjiang;
			} else if(type.equals("7")) {
				return City.neimenggu;
			} else if(type.equals("8")){
				return City.jiangsu;
			} else {
				return City.shandong;
			}
		}
		public static String getCityName(String type) {
			if (type.equals("0")) {
				return "江西";
			} else if (type.equals("1")) {
				return "广东";
			} else if (type.equals("2")) {
				return "安徽";
			} else if (type.equals("3")) {
				return "重庆";
			} else if (type.equals("4")) {
				return "辽宁";
			} else if (type.equals("5")){
				return "上海";
			} else if(type.equals("6")){
				return "黑龙江";
			} else if(type.equals("7")) {
				return "内蒙古";
			} else if(type.equals("8")){
				return "江苏";
			} else {
				return "山东";
			}
		}
		public static String getCityBetType(String city, String lotteryType) {
			String cStr = "";
			if (city.equals("0")) {
				cStr = "jx";
			} else if (city.equals("1")) {
				cStr = "gd";
			} else if (city.equals("2")) {
				cStr = "ah";
			} else if (city.equals("3")) {
				cStr = "cq";
			} else if (city.equals("4")) {
				cStr = "ln";
			} else if (city.equals("5")){
				cStr = "sh";
			} else if(city.equals("6")){
				cStr =  "hlj";
			} else if(city.equals("7")) {
				cStr = "nmg";
			} else if(city.equals("8")){
				cStr = "js";
			} else {
				cStr = "sd";
			}
			return cStr+lotteryType;
		}
	}
	
	/**
	 * enum of K3 type
	 */
	public enum K3Type {
		hezhi(0), threesamesingle(1), twosamesingle(2), threedifsingle(3), twodif(4), dragthree(5), dragtwo(6), threesamedouble(7), twosamedouble(8),
		threedifdouble(9);
		private final int mK3Type;

		K3Type(final int i) {
			this.mK3Type = i;
		}

		@Override
		public String toString() {
			return String.valueOf(mK3Type);
		}

		public static K3Type getK3Type(String type) {
			if (type.equals("0")) {
				return K3Type.hezhi;
			} else if (type.equals("1")) {
				return K3Type.threesamesingle;
			} else if (type.equals("2")) {
				return K3Type.twosamesingle;
			} else if (type.equals("3")) {
				return K3Type.threedifsingle;
			} else if (type.equals("4")) {
				return K3Type.twodif;
			} else if (type.equals("5")) {
				return K3Type.dragthree;
			} else if(type.equals("6")){
				return K3Type.dragtwo;
			} else if(type.equals("7")){
				return K3Type.threesamedouble;
			}else if(type.equals("8")){
				return K3Type.twosamedouble;
			} else {
				return K3Type.threedifdouble;
			}
		}
		public static String getSelectedType(String type) {
			if (type.equals(hezhi.toString())) {
				return "H";
			} else if (type.equals(threesamesingle.toString())) {
				return "SD";
			} else if (type.equals(twosamesingle.toString())) {
				return "LD";
			} else if (type.equals(threedifsingle.toString())) {
				return "SB";
			} else if (type.equals(twodif.toString())) {
				return "LB";
			} else if (type.equals(dragthree.toString())) {
				return "SB";
			} else if(type.equals(dragtwo.toString())){
				return "LB";
			} else if(type.equals(threesamedouble.toString())){
				return "ST";
			}else if(type.equals(twosamedouble.toString())){
				return "LF";
			} else {
				return "SL";
			}
		}
	}
	/**
	 * enum of K3 type
	 */
	public enum SscType {
		fivestar_zhixuan(0), fivestar_tongxuan(1), fivestar_fuxuan(2), threestar_zhixuan(3), threestar_fuxuan(4), 
		twostar_zhixuan(5), twostar_fuxuan(6),twostar_zhixuan_hezhi(7), twostar_zuxuan(8), twostar_zuxuan_hezhi(9),
		onestar_zhixuan(10),dxds(11), twostar_zuxuan_baohao(12), fourstar_zhixuan(13), fourstar_fuxuan(14), threestar_zhixuan_hezhi(15),
		threestar_zusan(16), threestar_zusan_baohao(17), threestar_zusan_hezhi(18), threestar_zuliu(19), threestar_zuliu_baohao(20),
		threestar_zuliu_hezhi(21), renxuan_two(22), renxuan_one(23);
		private final int mSscType;

		SscType(final int i) {
			this.mSscType = i;
		}

		@Override
		public String toString() {
			return String.valueOf(mSscType);
		}

		public static SscType getSscType(String type) {
			if (type.equals("0")) {
				return SscType.fivestar_zhixuan;
			} else if (type.equals("1")) {
				return SscType.fivestar_tongxuan;
			} else if (type.equals("2")) {
				return SscType.fivestar_fuxuan;
			} else if (type.equals("3")) {
				return SscType.threestar_zhixuan;
			} else if (type.equals("4")) {
				return SscType.threestar_fuxuan;
			} else if (type.equals("5")) {
				return SscType.twostar_zhixuan;
			} else if (type.equals("6")) {
				return SscType.twostar_fuxuan;
			} else if (type.equals("7")) {
				return SscType.twostar_zhixuan_hezhi;
			}  else if (type.equals("8")) {
				return SscType.twostar_zuxuan;
			} else if (type.equals("9")) {
				return SscType.twostar_zuxuan_hezhi;
			}else if (type.equals("10")) {
				return SscType.onestar_zhixuan;
			}else if(type.equals("11")) {
				return SscType.dxds;
			}else if(type.equals("12")){
				return SscType.twostar_zuxuan_baohao;
			}else if(type.equals("13")){
				return SscType.fourstar_zhixuan;
			}else if(type.equals("14")){
				return SscType.fourstar_fuxuan;
			}else if(type.equals("15")){
				return SscType.threestar_zhixuan_hezhi;
			}else if(type.equals("16")){
				return SscType.threestar_zusan;
			}else if(type.equals("17")){
				return SscType.threestar_zusan_baohao;
			}else if(type.equals("18")){
				return SscType.threestar_zusan_hezhi;
			}else if(type.equals("19")){
				return SscType.threestar_zuliu;
			}else if(type.equals("20")){
				return SscType.threestar_zuliu_baohao;
			}else if(type.equals("21")){
				return SscType.threestar_zuliu_hezhi;
			}else if(type.equals("22")){
				return SscType.renxuan_two;
			}else{
				return SscType.renxuan_one;
			}
		}
		public static String getSscHint(Context context,String type) {
			if (type.equals("0")) {//直选
				return context.getResources().getString(R.string.ssc_fivestar_zhixuan);
			} else if (type.equals("1")) {//通选
				return context.getResources().getString(R.string.ssc_fivestar_tongxuan);
			} else if (type.equals("2")) {//复选
				return context.getResources().getString(R.string.ssc_fivestar_fuxuan);
			} else if (type.equals("3")) {//三星直选
				return context.getResources().getString(R.string.ssc_threestar_zhixuan);
			} else if (type.equals("4")) {//三星复选
				return context.getResources().getString(R.string.ssc_threestar_fuxuan);
			} else if (type.equals("5")) {//二星直选
				return context.getResources().getString(R.string.ssc_twostar_zhixuan);
			} else if (type.equals("6")) {//二星复选
				return context.getResources().getString(R.string.ssc_twostar_fuxuan);
			} else if (type.equals("7")) {//二星直选和值
				return context.getResources().getString(R.string.ssc_twostar_zhixuanhezhi);
			}  else if (type.equals("8")) {//二星组选
				return context.getResources().getString(R.string.ssc_twostar_zuxuan);
			} else if (type.equals("9")) {//二星组选和值
				return context.getResources().getString(R.string.ssc_twostar_zuxuanhezhi);
			}else if (type.equals("10")) {//一星直选
				return context.getResources().getString(R.string.ssc_onestar_zhixuan);
			}else if(type.equals("11")){//大小单双
				return context.getResources().getString(R.string.ssc_dxds);
			}else {
				return context.getResources().getString(R.string.ssc_twostar_zuxuan_baohao);
			}
		}
		
		public static String getJxSscHint(Context context,String type) {
			if (type.equals("0")) {
				//return SscType.fivestar_zhixuan;
				return context.getResources().getString(R.string.jx_ssc_fivestar_zhixuan);
			} else if (type.equals("1")) {
				//return SscType.fivestar_tongxuan;
				return context.getResources().getString(R.string.jx_ssc_fivestar_tongxuan);
			} else if (type.equals("2")) {
				//return SscType.fivestar_fuxuan;
				return context.getResources().getString(R.string.jx_ssc_fivestar_fuxuan);
			} else if (type.equals("3")) {
				//return SscType.threestar_zhixuan;
				return context.getResources().getString(R.string.jx_ssc_threestar_zhixuan);
			} else if (type.equals("4")) {
				//return SscType.threestar_fuxuan;
				return context.getResources().getString(R.string.jx_ssc_threestar_fuxuan);
			} else if (type.equals("5")) {
				//return SscType.twostar_zhixuan;
				return context.getResources().getString(R.string.jx_ssc_twostar_zhixuan);
			} else if (type.equals("6")) {
				//return SscType.twostar_fuxuan;
				return context.getResources().getString(R.string.jx_ssc_twostar_fuxuan);
			} else if (type.equals("7")) {
				//return SscType.twostar_zhixuan_hezhi;
				return context.getResources().getString(R.string.jx_ssc_twostar_zhixuanhezhi);
			}  else if (type.equals("8")) {
				//return SscType.twostar_zuxuan;
				return context.getResources().getString(R.string.jx_ssc_twostar_zuxuan);
			} else if (type.equals("9")) {
				//return SscType.twostar_zuxuan_hezhi;
				return context.getResources().getString(R.string.jx_ssc_twostar_zuxuanhezhi);
			}else if (type.equals("10")) {
				//return SscType.onestar_zhixuan;
				return context.getResources().getString(R.string.jx_ssc_onestar_zhixuan);
			}else if(type.equals("11")) {
				//return SscType.dxds;
				return context.getResources().getString(R.string.jx_ssc_dxds);
			}else if(type.equals("12")){
				//return SscType.twostar_zuxuan_baohao;
				return context.getResources().getString(R.string.jx_ssc_twostar_zuxuan_baohao);
			}else if(type.equals("13")){
				//return SscType.fourstar_zhixuan;
				return context.getResources().getString(R.string.jx_ssc_fourstar_zhixuan);
			}else if(type.equals("14")){
				//return SscType.fourstar_fuxuan;
				return context.getResources().getString(R.string.jx_ssc_fourstar_fuxuan);
			}else if(type.equals("15")){
				//return SscType.threestar_zhixuan_hezhi;
				return context.getResources().getString(R.string.jx_ssc_threestar_zhixuanhezhi);
			}else if(type.equals("16")){
				//return SscType.threestar_zusan;
				return context.getResources().getString(R.string.jx_ssc_threestar_zusan);
			}else if(type.equals("17")){
				//return SscType.threestar_zusan_baohao;
				return context.getResources().getString(R.string.jx_ssc_threestar_zusanbaohao);
			}else if(type.equals("18")){
				//return SscType.threestar_zusan_hezhi;
				return context.getResources().getString(R.string.jx_ssc_threestar_zusanhezhi);
			}else if(type.equals("19")){
				//return SscType.threestar_zuliu;
				return context.getResources().getString(R.string.jx_ssc_threestar_zuliu);
			}else if(type.equals("20")){
				//return SscType.threestar_zuliu_baohao;
				return context.getResources().getString(R.string.jx_ssc_threestar_zuliubaohao);
			}else if(type.equals("21")){
				//return SscType.threestar_zuliu_hezhi;
				return context.getResources().getString(R.string.jx_ssc_threestar_zuliuhezhi);
			}else if(type.equals("22")){
				//return SscType.renxuan_two;
				return context.getResources().getString(R.string.jx_ssc_renxuan_two);
			}else{
				//return SscType.renxuan_one;
				return context.getResources().getString(R.string.jx_ssc_renxuan_one);
			}
		}
		
		public static String getSelectedType(String type) {
			if (type.equals(fivestar_zhixuan.toString())) {
				return "D5";
			} else if (type.equals(fivestar_tongxuan.toString())) {
				return "T5";
			} else if (type.equals(fivestar_fuxuan.toString())) {
				return "C5";
			} else if (type.equals(threestar_zhixuan.toString())) {
				return "D3";
			} else if (type.equals(threestar_fuxuan.toString())) {
				return "C3";
			} else if (type.equals(twostar_zhixuan.toString())) {
				return "D2";
			} else if (type.equals(twostar_fuxuan.toString())) {
				return "C2";
			} else if (type.equals(twostar_zhixuan_hezhi.toString())) {
				return "H2";
			}  else if (type.equals(twostar_zuxuan.toString())) {
				return "Z2";
			} else if (type.equals(twostar_zuxuan_hezhi.toString())) {
				return "S2";
			}else if (type.equals(onestar_zhixuan.toString())) {
				return "D1";
			}else if(type.equals(dxds.toString())) {
				return "DD";
			}else if(type.equals(twostar_zuxuan_baohao.toString())){
				return "F2";
			}else if(type.equals(fourstar_zhixuan.toString())){
				return "D4";
			}else if(type.equals(fourstar_fuxuan.toString())){
				return "C4";
			}else if(type.equals(threestar_zhixuan_hezhi.toString())){
				return "H3";
			}else if(type.equals(threestar_zusan.toString())){
				return "Z3";
			}else if(type.equals(threestar_zusan_baohao.toString())){
				return "F3";
			}else if(type.equals(threestar_zusan_hezhi.toString())){
				return "S3";
			}else if(type.equals(threestar_zuliu.toString())){
				return "Z6";
			}else if(type.equals(threestar_zuliu_baohao.toString())){
				return "F6";
			}else if(type.equals(threestar_zuliu_hezhi.toString())){
				return "S6";
			}else if(type.equals(renxuan_two.toString())){
				return "R2";
			}else{
				return "R1";
			}
		}

	}
	
	/**
	 * 所有彩票类型 { "全部彩种","十一运夺金","江西11选5" ,"广东11选5","安徽11选5", "重庆11选5", "辽宁11选5", "上海11选5","黑龙江11选5", "江苏快3",  "安徽快3", "内蒙古快3",
		"重庆时时彩",   "江西时时彩"};
	 * @author yj
	 *
	 */
	public enum LotteryType {
		sd11x5(0), jx11x5(1), gd11x5(2), ah11x5(3),cq11x5(4), ln11x5(
			5),sh11x5(6), hlj11x5(7), jsk3(8), ahk3(9), nmgk3(10), cqssc(11), jxssc(12),ALLTYPE(13),UNDEFINETYPE(14), SSQ(15), 
			FC3D(16), QLC(17), DLT(18), PL3(19), PL5(20), QXC(21), TC22x5(22);
		private final int mLotteryType;

		LotteryType(final int i) {
			this.mLotteryType = i;
		}

		@Override
		public String toString() {
			return String.valueOf(mLotteryType);
			}
		public static LotteryType getLotteryType(String type) {
			if(type.equals("十一运夺金")) {
				return LotteryType.sd11x5;
			} else if(type.equals("江西11选5")) {
				return LotteryType.jx11x5;
			} else if(type.equals("广东11选5")) {
				return LotteryType.gd11x5;
			} else if(type.equals("安徽11选5")) {
				return LotteryType.ah11x5;
			} else if(type.equals("重庆11选5")) {
				return LotteryType.cq11x5;
			} else if(type.equals("辽宁11选5")) {
				return LotteryType.ln11x5;
			} else if(type.equals("上海11选5")) {
				return LotteryType.sh11x5;
			} else if(type.equals("黑龙江11选5")) {
				return LotteryType.hlj11x5;
			} else if(type.equals("江苏快3")) {
				return LotteryType.jsk3;
			} else if(type.equals("安徽快3")) {
				return LotteryType.ahk3;
			}  else if(type.equals("内蒙古快3")) {
				return LotteryType.nmgk3;
			} else if(type.equals("重庆时时彩")) {
				return LotteryType.cqssc;
			} else if(type.equals("江西时时彩")) {
				return LotteryType.jxssc;
			} else if(type.equals("全部彩种")) {
				return LotteryType.ALLTYPE;
			}  else if(type.equals("双色球")) {
				return LotteryType.SSQ;
			}  else if(type.equals("福彩3D")) {
				return LotteryType.FC3D;
			} else if(type.equals("七乐彩")) {
				return LotteryType.QLC;
			} else if(type.equals("大乐透")) {
				return LotteryType.DLT;
			} else if(type.equals("排列3")) {
				return LotteryType.PL3;
			} else if(type.equals("排列5")) {
				return LotteryType.PL5;
			} else if(type.equals("七星彩")) {
				return LotteryType.QXC;
			} else if(type.equals("22x5")) {
				return LotteryType.TC22x5;
			}else{
				return LotteryType.UNDEFINETYPE;
			}
		}
		
		public static String getLotteryTypeString(LotteryType type) {
			if(type == sd11x5) {
				return "SD11x5";
			} else if(type == jx11x5) {
				return "JX11x5";
			} else if(type == gd11x5) {
				return "GD11x5";
			}else if(type == ah11x5) {
				return "AH11x5";
			}else if(type == cq11x5) {
				return "CQ11x5";
			}else if(type == ln11x5) {
				return "LN11x5";
			}else if(type == sh11x5) {
				return "SH11x5";
			}else if(type == hlj11x5) {
				return "HLJ11x5";
			}else if(type == jsk3) {
				return "JSK3";
			}else if(type == ahk3) {
				return "AHK3";
			}else if(type == nmgk3) {
				return "NMGK3";
			}else if(type == cqssc) {
				return "CQSSC";
			}else if(type == jxssc) {
				return "JXSSC";
			} else if(type == SSQ) {
				return "SSQ";
			} else if(type == FC3D) {
				return "FC3D";
			} else if(type == QLC) {
				return "QLC";
			} else if(type == DLT) {
				return "DLT";
			} else if(type == PL3) {
				return "PL3";
			} else if(type == PL5) {
				return "PL5";
			} else if(type == QXC) {
				return "QXC";
			} else if(type == TC22x5) {
				return "TC22x5";
			}
			else {
				return "";
			}
		}
	}

}
