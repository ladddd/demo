package com.jincaizi.kuaiwin.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.text.TextUtils;
import android.util.Log;

import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.K3Type;
import com.jincaizi.kuaiwin.utils.Constants.SscType;
import com.jincaizi.kuaiwin.utils.Utils;

public class BeiTouGenerator {
	
/**
 * 根据当前期号currentQihao生成之后的count期期号数组resultQihao
 * @param termCountPerDay 每天总的期数  11x5 = 78期， K3 = 82 期 ； 重庆时时彩 = 120 ； 江西时时彩 = 84期
 * @param currentQihao
 * @param count
 * @return
 */
	public static String[] generateQihaos(int termCountPerDay,String currentQihao, int count, String lotteryType) {
		String[] resultQihao = new String[count];
		resultQihao[0] = currentQihao;
		if(lotteryType.equals("11x5")) {
			int i=1;
			String tempQihao = currentQihao;
			while(i<count) {
				tempQihao = QiHaoTool.get11x5NextQihao(tempQihao, termCountPerDay);
				resultQihao[i++] = tempQihao;
			}
			
		} else if(lotteryType.equals("K3")){
			int i=1;
			String tempQihao = currentQihao;
			while(i<count) {
				tempQihao = QiHaoTool.getK3NextQihao(tempQihao, termCountPerDay);
				resultQihao[i++] = tempQihao;
			}
		} else {//ssc
			int i=1;
			String tempQihao = currentQihao;
			while(i<count) {
				tempQihao = QiHaoTool.getSscNextQihao(tempQihao, termCountPerDay);
				resultQihao[i++] = tempQihao;
			}
		}
			
		
		return resultQihao;
	}
	
//11x5 R2
	public static int getBonus(LinkedList<String> data) {
		ArrayList<String>target = new ArrayList<String>();
		//产生所有投注方案的不重复数字
		for(int i=0; i< data.size(); i++) {
			String[] str = data.get(i).split(" ");
			if(target.size() == 0) {
				target.add(str[0]);
				target.add(str[1]);
			} else {
				if(target.indexOf(str[0]) == -1) {
					target.add(str[0]);
				}
				if(target.indexOf(str[1]) == -1) {
					target.add(str[1]);
				}
			}
		}
		if(target.size() <= 5) {
			return data.size()*6;
		}
		ArrayList<String[]> combineResult = DoubleColorFromMachine.combine(target, 5);
		int max = 0;
		for(int i=0; i<combineResult.size(); i++) {
			List<String> compare = Arrays.asList(combineResult.get(i));
			int count = 0;
			for(int j=0; j<data.size(); j++) {
				String[] str = data.get(j).split(" ");
				if(compare.indexOf(str[0]) != -1 && compare.indexOf(str[1]) != -1) {
					count++;
				}
			}
			if(max < count) {
				max = count;
			}
		}
		return max*6;
	}
	//11x5 common R1-5
	public static int getBonusR2to5(LinkedList<String> data , int perAward) {
		ArrayList<String>target = new ArrayList<String>();
		//产生所有投注方案的不重复数字
		for(int i=0; i< data.size(); i++) {
			String[] str = data.get(i).split(" ");
			if(target.size() == 0) {
				for(int index = 0; index < str.length; index++) {
					target.add(str[index]);
				}
			} else {
				for(int index = 0; index < str.length; index++) {
					if(target.indexOf(str[index]) == -1) {
					    target.add(str[index]);
					}
				}
			}
		}
		if(target.size() <= 5) {
			return data.size()* perAward;
		}
		ArrayList<String[]> combineResult = DoubleColorFromMachine.combine(target, 5);
		int max = 0;
		for(int i=0; i<combineResult.size(); i++) {
			List<String> compare = Arrays.asList(combineResult.get(i));
			int count = 0;
			for(int j=0; j<data.size(); j++) {
				String[] str = data.get(j).split(" ");
				boolean isCount = true;
				for(int index = 0; index < str.length; index++) {
					if(compare.indexOf(str[index]) == -1 ) {
						isCount = false;
					}
				}
				if(isCount) {
					count++;
				}
			}
			if(max < count) {
				max = count;
			}
		}
		Log.d("SyxwPick", "maxZhu = " + max);
		return (max * perAward);
	}
	//11x5 common R6-8
		public static int getBonusR6to8(LinkedList<String> data , int perAward) {
			ArrayList<String>target = new ArrayList<String>();
			//产生所有投注方案的不重复数字
			for(int i=0; i< data.size(); i++) {
				String[] str = data.get(i).split(" ");
				if(target.size() == 0) {
					for(int index = 0; index < str.length; index++) {
						target.add(str[index]);
					}
				} else {
					for(int index = 0; index < str.length; index++) {
						if(target.indexOf(str[index]) == -1) {
						    target.add(str[index]);
						}
					}
				}
			}
			if(target.size() <= 5) {
				return data.size()* perAward;
			}
			ArrayList<String[]> combineResult = DoubleColorFromMachine.combine(target, 5);
			int max = 0;
			for(int i=0; i<combineResult.size(); i++) {
				int count = 0;
				for(int j=0; j<data.size(); j++) {
					String[] str = data.get(j).split(" ");
					List<String> compare = Arrays.asList(str);
					boolean isCount = true;
					for(int index = 0; index < combineResult.get(i).length; index++) {
						if(compare.indexOf((combineResult.get(i))[index]) == -1 ) {
							isCount = false;
						}
					}
					if(isCount) {
						count++;
					}
				}
				if(max < count) {
					max = count;
				}
			}
			Log.d("SyxwPick", "maxZhu = " + max);
			return (max * perAward);
		}
		//11x5 common Q1-3
		public static int getBonusQ1to3(LinkedList<String> data , int perAward) {
			int max = 1;
			for(int i = 0; i < data.size(); i++) {
				int count = 1;
				for(int j=0; j < data.size(); j++) {
					if(i == j) {
						continue;
					}
					if(data.get(i).equals(data.get(j))) {
						count++;
					}
				}
				if(max < count) {
					max = count;
				}
			}
			return max * perAward;
		}
		private static HashMap<String, Integer>k3_hezhi_perAward = new HashMap<String, Integer>();
		private static void getK3HezhiPeraward() {
			k3_hezhi_perAward.put("3", 240);
			k3_hezhi_perAward.put("18", 240);
			   //和值4或17
			k3_hezhi_perAward.put("4", 80);
			k3_hezhi_perAward.put("17", 80);
			   //和值5或16
			k3_hezhi_perAward.put("5", 40);
			k3_hezhi_perAward.put("16", 40);
			   //和值6或15
			k3_hezhi_perAward.put("6", 25);
			k3_hezhi_perAward.put("15", 25);
			   //和值7或14
			k3_hezhi_perAward.put("7", 16);
			k3_hezhi_perAward.put("14", 16);
			   //和值8或13
			k3_hezhi_perAward.put("8", 12);
			k3_hezhi_perAward.put("13", 12);
			   //和值9或12
			k3_hezhi_perAward.put("9", 10);
			k3_hezhi_perAward.put("12", 10);
			   //和值10或11
			k3_hezhi_perAward.put("10", 9);
			k3_hezhi_perAward.put("11", 9);
		}
		public static int[] getK3Bonus(LinkedList<String>data, String type) {
			int result[] = new int[2];
			result[0] = 0;
			result[1] = 0;
			//
			if (type.equals(K3Type.hezhi.toString())) {
				getK3HezhiPeraward();
				int[]sums = new int[16];
				for(int i=0; i<16; i++) {
					sums[i] = 0;
				}
				for(int i=0; i< data.size(); i++) {
					String strs[] = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						sums[Integer.parseInt(strs[index]) - 3] += k3_hezhi_perAward.get(strs[index]);
					}
				}
				int max = 0;
				int min = 0;
				boolean isFirst = true;
				for(int i=0; i<16; i++) {
					if(sums[i] > 0) {
						if(isFirst) {
							max = sums[i];
							min = sums[i];
							isFirst = false;
						} else {
							max = Math.max(max, sums[i]);
							min = Math.min(min, sums[i]);
						}
					}
				}
				result[0] = min;
				result[1] = max;
			} else if (type.equals(K3Type.threesamesingle.toString())) {
				int[]counts = new int[6];
				for(int i=0; i<6; i++) {
					counts[i] = 0;
				}
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					for(int index=0; index<strs.length; index++) {
						if(strs[index].equals("111")) {
							counts[0]++;
						} else if(strs[index].equals("222")) {
							counts[1]++;
						}else if(strs[index].equals("333")) {
							counts[2]++;
						}else if(strs[index].equals("444")) {
							counts[3]++;
						}else if(strs[index].equals("555")) {
							counts[4]++;
						}else if(strs[index].equals("666")) {
							counts[5]++;
						}
					}
				}
				int max = 0;
				int min = 0;
				boolean isFirst = true;
				for(int i=0; i<6; i++) {
					if(counts[i] > 0) {
						if(isFirst) {
							max = counts[i];
							min = counts[i];
							isFirst = false;
						} else {
							max = Math.max(max, counts[i]);
							min = Math.min(min, counts[i]);
						}
					}
				}
				result[0] = min*240;
				result[1] = max*240;
			} else if (type.equals(K3Type.twosamesingle.toString())) {
				int[][]counts = new int[6][];
				for(int i=0; i<6; i++) {
					counts[i] = new int[6];
				}
				for(int i=0; i<6; i++){
					for(int j=0; j<6; j++) {
						counts[i][j] = 0;
					}
				}
				int index = 0;
				boolean isFirst = true;
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split("#");
						if(strs[0].equals("11")) {
							index = 0;
						} else if(strs[0].equals("22")) {
							index = 1;
						}else if(strs[0].equals("33")) {
							index = 2;
						}else if(strs[0].equals("44")) {
							index = 3;
						}else if(strs[0].equals("55")) {
							index = 4;
						}else if(strs[0].equals("66")) {
							index = 5;
						}
					String[]tuoStrs = strs[1].split(" ");
					for(int j=0; j<tuoStrs.length; j++) {
						if(tuoStrs[j].equals("1")) {
							counts[index][0]++;
						} else if(tuoStrs[j].equals("2")) {
							counts[index][1]++;
						}else if(tuoStrs[j].equals("3")) {
							counts[index][2]++;
						}else if(tuoStrs[j].equals("4")) {
							counts[index][3]++;
						}else if(tuoStrs[j].equals("5")) {
							counts[index][4]++;
						}else if(tuoStrs[j].equals("6")) {
							counts[index][5]++;
						}
					}
				}
				int max = 0;
				int min = 0;
				
				for(int i=0; i<6; i++){
					int subMax = 0;
					int subMin = 0;
					boolean isInnerFirst = true;
					for(int j=0; j<6; j++) {
						if(counts[i][j] > 0 && isInnerFirst) {
							subMax = counts[i][j];
							subMin = counts[i][j];
							isInnerFirst = false;
						} else if(counts[i][j] > 0) {
							subMax = Math.max(subMax,counts[i][j]);
							subMin = Math.min(subMin,counts[i][j]);
						}
					}
					if(subMax != 0 && subMin != 0 && isFirst) {
						max = subMax;
						min = subMin;
						isFirst = false;
					} else if(subMax != 0 && subMin != 0){
						max = Math.max(max, subMax);
						min = Math.min(subMin, min);
					}
				}
				result[0] = min*80;
				result[1] = max*80;
			} else if (type.equals(K3Type.threedifsingle.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(strs)), 3);
					for(int index = 0; index < combines.size(); index++) {
						String[] subCombines = combines.get(index);
						StringBuilder builder = new StringBuilder();
						for(int j = 0; j<subCombines.length; j++) {
							builder.append(subCombines[j]);
						}
						combineResult.add(builder.toString());
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*40;
				result[1] = maxList.get(maxList.size() -1)*40;
			} else if (type.equals(K3Type.twodif.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(strs)), 2);
					for(int index = 0; index < combines.size(); index++) {
						String[] subCombines = combines.get(index);
						StringBuilder builder = new StringBuilder();
						for(int j = 0; j<subCombines.length; j++) {
							builder.append(subCombines[j]);
						}
						combineResult.add(builder.toString());
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0);
				if(maxList.size() > 1) {
					isAllSame = true;
					for(int i= maxList.size() -1; i>=1; i--) {
						if(maxList.get(i) != maxList.get(i-1) ) {
							result[1] = maxList.get(i)+ maxList.get(i-1);
							isAllSame = false;
							break;
						}
					}
					if(isAllSame) {
						result[1] = maxList.get(0)+maxList.get(1);
					}
				} else {
					result[1] = maxList.get(0);
				}
				result[0] = result[0]*8;
				result[1] = result[1]*8;
			} else if (type.equals(K3Type.dragthree.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split("#");
					String[]dan = strs[0].split(" ");
					String[]tuo = strs[1].split(" ");
					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(tuo)), 3-dan.length);
					StringBuilder builderDan = new StringBuilder();
					for(int index = 0; index < dan.length; index++) {
						builderDan.append(dan[index]);
					}
					for(int index = 0; index < combines.size(); index++) {
						String[] subCombines = combines.get(index);
						StringBuilder builder = new StringBuilder();
						for(int j = 0; j<subCombines.length; j++) {
							builder.append(subCombines[j]);
						}
						combineResult.add(builderDan.toString() + builder.toString());
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*40;
				result[1] = maxList.get(maxList.size() -1)*40;
			} else if(type.equals(K3Type.dragtwo.toString())){
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split("#");
					String[]dan = strs[0].split(" ");
					String[]tuo = strs[1].split(" ");
					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(tuo)), 2-dan.length);
					StringBuilder builderDan = new StringBuilder();
					for(int index = 0; index < dan.length; index++) {
						builderDan.append(dan[index]);
					}
					for(int index = 0; index < combines.size(); index++) {
						String[] subCombines = combines.get(index);
						StringBuilder builder = new StringBuilder();
						for(int j = 0; j<subCombines.length; j++) {
							builder.append(subCombines[j]);
						}
						combineResult.add(builderDan.toString() + builder.toString());
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0);
				if(maxList.size() > 1) {
					isAllSame = true;
					for(int i= maxList.size() -1; i>=1; i--) {
						if(maxList.get(i) != maxList.get(i-1) ) {
							result[1] = maxList.get(i)+ maxList.get(i-1);
							isAllSame = false;
							break;
						}
					}
					if(isAllSame) {
						result[1] = maxList.get(0)+maxList.get(1);
					}
				} else {
					result[1] = maxList.get(0);
				}
				result[0] = result[0]*8;
				result[1] = result[1]*8;
			} else if(type.equals(K3Type.threesamedouble.toString())){
				result[0] = data.size()*40;
				result[1] = data.size()*40;
			}else if(type.equals(K3Type.twosamedouble.toString())){
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						combineResult.add(strs[index]);
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*15;
				result[1] = maxList.get(maxList.size() -1)*15;
			} else {
				//K3Type.threedifdouble;
				result[0] = data.size()*10;
				result[1] = data.size()*10;
			}
			
			return result;
		}
//		/**
//		 * 江西时时彩
//		 * @param data
//		 * @param mGameType
//		 * @return
//		 */
//		public static int[] getSscBonus(LinkedList<String>data, String mGameType, String city) {
//			int result[] = new int[2];
//			result[0] = 0;
//			result[1] = 0;
//			if(mGameType.equals(SscType.fivestar_zhixuan.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 100000;
//				} else {
//					factor = 116000;
//				}
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int indexW = 0; indexW<strs[0].length(); indexW++) {
//						for(int indexQ = 0; indexQ<strs[1].length(); indexQ++) {
//							for(int indexB = 0; indexB<strs[2].length(); indexB++) {
//								for(int indexS = 0; indexS < strs[3].length(); indexS++) {
//									for(int indexG = 0; indexG < strs[4].length(); indexG++) {
//										combineResult.add(strs[0].substring(indexW, indexW+1)
//												+strs[1].substring(indexQ, indexQ+1)
//												+strs[2].substring(indexB, indexB+1)
//												+strs[3].substring(indexS, indexS+1)
//												+strs[4].substring(indexG, indexG+1));
//									}
//								}
//							}
//						}
//					}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*factor;
//				result[1] = maxList.get(maxList.size() -1)*factor;
//			}
////			else if(mGameType.equals(SscType.fivestar_fuxuan.toString())) {
////				//不支持
////			} 
//			else if(mGameType.equals(SscType.fivestar_tongxuan.toString())) {
//				int factorMax = 0;
//				int factorMin = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factorMax = 20440;
//					factorMin = 20;
//				} else {
//					factorMax = 20460;
//					factorMin = 30;
//				}
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int indexW = 0; indexW<strs[0].length(); indexW++) {
//						for(int indexQ = 0; indexQ<strs[1].length(); indexQ++) {
//							for(int indexB = 0; indexB<strs[2].length(); indexB++) {
//								for(int indexS = 0; indexS < strs[3].length(); indexS++) {
//									for(int indexG = 0; indexG < strs[4].length(); indexG++) {
//										combineResult.add(strs[0].substring(indexW, indexW+1)
//												+strs[1].substring(indexQ, indexQ+1)
//												+strs[2].substring(indexB, indexB+1)
//												+strs[3].substring(indexS, indexS+1)
//												+strs[4].substring(indexG, indexG+1));
//									}
//								}
//							}
//						}
//					}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(maxList.size() -1)*factorMin;
//				result[1] = maxList.get(maxList.size() -1)*factorMax;
//				
//			} 
////			else if(mGameType.equals(SscType.twostar_fuxuan.toString())) {
////				//不支持
////			} 
//			else if(mGameType.equals(SscType.twostar_zhixuan.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 100;
//				} else {
//					factor = 116;
//				}
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//								for(int indexS = 0; indexS < strs[0].length(); indexS++) {
//									for(int indexG = 0; indexG < strs[1].length(); indexG++) {
//										combineResult.add(strs[0].substring(indexS, indexS+1)
//												+strs[1].substring(indexG, indexG+1));
//									}
//								}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*factor;
//				result[1] = maxList.get(maxList.size() -1)*factor;
//			} else if(mGameType.equals(SscType.twostar_zuxuan.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 50;
//				} else {
//					factor = 58;
//				}
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					combineResult.add(data.get(i).replace(",",""));
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*factor;
//				result[1] = maxList.get(maxList.size() -1)*factor;
//			} else if(mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 100;
//				} else {
//					factor = 116;
//				}
//				int[]sums = new int[19];
//				for(int i=0; i<19; i++) {
//					sums[i] = 0;
//				}
//				for(int i=0; i< data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int index = 0; index < strs.length; index++) {
//						sums[Integer.parseInt(strs[index])] += factor;
//					}
//				}
//				int max = 0;
//				int min = 0;
//				boolean isFirst = true;
//				for(int i=0; i<19; i++) {
//					if(sums[i] > 0) {
//						if(isFirst) {
//							max = sums[i];
//							min = sums[i];
//							isFirst = false;
//						} else {
//							max = Math.max(max, sums[i]);
//							min = Math.min(min, sums[i]);
//						}
//					}
//				}
//				result[0] = min;
//				result[1] = max;
//			} else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 50;
//				} else {
//					factor = 58;
//				}
//				int[]sums = new int[17];
//				for(int i=0; i<17; i++) {
//					sums[i] = 0;
//				}
//				for(int i=0; i< data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int index = 0; index < strs.length; index++) {
//						sums[Integer.parseInt(strs[index])-1] += factor;
//					}
//				}
//				int max = 0;
//				int min = 0;
//				boolean isFirst = true;
//				for(int i=0; i<17; i++) {
//					if(sums[i] > 0) {
//						if(isFirst) {
//							max = sums[i];
//							min = sums[i];
//							isFirst = false;
//						} else {
//							max = Math.max(max, sums[i]);
//							min = Math.min(min, sums[i]);
//						}
//					}
//				}
//				result[0] = min;
//				result[1] = max;
//			}else if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 50;
//				} else {
//					factor = 58;
//				}
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(strs)), 2);
//					for(int index = 0; index < combines.size(); index++) {
//						String[] subCombines = combines.get(index);
//						StringBuilder builder = new StringBuilder();
//						for(int j = 0; j<subCombines.length; j++) {
//							builder.append(subCombines[j]);
//						}
//						combineResult.add(builder.toString());
//					}
//				}
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				boolean[]used = new boolean[combineResult.size()];
//				for(int i=0; i<used.length; i++) {
//					used[i] = false;
//				}
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					int count = 0;
//					for(int index = i+1; index < combineResult.size(); index++) {
//						if(!used[i] && !used[index]) {
//							String str0 = combineResult.get(i);
//							String str1 = combineResult.get(index);
//							if(str0.contains(str1.subSequence(0, 1)) && str0.contains(str1.subSequence(1, 2))) {
//								count++;
//								used[index] = true;
//							}
//						}
//					}
//					maxList.add(count);
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*factor;
//				result[1] = maxList.get(maxList.size()-1)*factor;
//			}else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 10;
//				} else {
//					factor = 11;
//				}
//				int size = 10;
//				int[]sums = new int[size];
//				for(int i=0; i<size; i++) {
//					sums[i] = 0;
//				}
//				for(int i=0; i< data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int index = 0; index < strs.length; index++) {
//						sums[Integer.parseInt(strs[index])] += factor;
//					}
//				}
//				int max = 0;
//				int min = 0;
//				boolean isFirst = true;
//				for(int i=0; i<size; i++) {
//					if(sums[i] > 0) {
//						if(isFirst) {
//							max = sums[i];
//							min = sums[i];
//							isFirst = false;
//						} else {
//							max = Math.max(max, sums[i]);
//							min = Math.min(min, sums[i]);
//						}
//					}
//				}
//				result[0] = min;
//				result[1] = max;
//			} else if(mGameType.equals(SscType.dxds.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//								for(int indexS = 0; indexS < strs[0].length(); indexS++) {
//									for(int indexG = 0; indexG < strs[1].length(); indexG++) {
//										combineResult.add(strs[0].substring(indexS, indexS+1)
//												+strs[1].substring(indexG, indexG+1));
//									}
//								}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*4;
//				result[1] = maxList.get(maxList.size() -1)*4;
//			} 
////			else if(mGameType.equals(SscType.threestar_fuxuan.toString())) {
////				//不支持
////			}
//			else if(mGameType.equals(SscType.threestar_zhixuan.toString())){
//				int factor = 0;
//				if(city.equals(Constants.City.chongqing.toString())) {
//					factor = 1000;
//				} else {
//					factor = 1160;
//				}
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//							for(int indexB = 0; indexB<strs[0].length(); indexB++) {
//								for(int indexS = 0; indexS < strs[1].length(); indexS++) {
//									for(int indexG = 0; indexG < strs[2].length(); indexG++) {
//										combineResult.add(strs[0].substring(indexB, indexB+1)
//												+strs[1].substring(indexS, indexS+1)
//												+strs[2].substring(indexG, indexG+1));
//									}
//								}
//							}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*factor;
//				result[1] = maxList.get(maxList.size() -1)*factor;
//			} else if(mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())) {
//				int size = 28;
//				int[]sums = new int[size];
//				for(int i=0; i<size; i++) {
//					sums[i] = 0;
//				}
//				for(int i=0; i< data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int index = 0; index < strs.length; index++) {
//						sums[Integer.parseInt(strs[index])] += 1160;
//					}
//				}
//				int max = 0;
//				int min = 0;
//				boolean isFirst = true;
//				for(int i=0; i<size; i++) {
//					if(sums[i] > 0) {
//						if(isFirst) {
//							max = sums[i];
//							min = sums[i];
//							isFirst = false;
//						} else {
//							max = Math.max(max, sums[i]);
//							min = Math.min(min, sums[i]);
//						}
//					}
//				}
//				result[0] = min;
//				result[1] = max;
//			} else if(mGameType.equals(SscType.threestar_zuliu_hezhi.toString())) {
//				int size = 22;
//				int[]sums = new int[size];
//				for(int i=0; i<size; i++) {
//					sums[i] = 0;
//				}
//				for(int i=0; i< data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					for(int index = 0; index < strs.length; index++) {
//						sums[Integer.parseInt(strs[index])-3] += 190;
//					}
//				}
//				int max = 0;
//				int min = 0;
//				boolean isFirst = true;
//				for(int i=0; i<size; i++) {
//					if(sums[i] > 0) {
//						if(isFirst) {
//							max = sums[i];
//							min = sums[i];
//							isFirst = false;
//						} else {
//							max = Math.max(max, sums[i]);
//							min = Math.min(min, sums[i]);
//						}
//					}
//				}
//				result[0] = min;
//				result[1] = max;
//			} else if(mGameType.equals(SscType.threestar_zusan_baohao.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String str = data.get(i).replace(",", "");
//					for(int indexOne=0; indexOne<str.length(); indexOne++) {
//						for(int indexTwo=0; indexTwo<str.length(); indexTwo++) {
//							if(indexOne != indexTwo) {
//								combineResult.add(str.substring(indexOne, indexOne+1)
//										+str.substring(indexOne, indexOne+1)
//										+str.substring(indexTwo, indexTwo+1));
//							}
//						}
//					}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*385;
//				result[1] = maxList.get(maxList.size() -1)*385;
//			}else if(mGameType.equals(SscType.threestar_zuliu_baohao.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(strs)), 3);
//					for(int index = 0; index < combines.size(); index++) {
//						String[] subCombines = combines.get(index);
//						StringBuilder builder = new StringBuilder();
//						for(int j = 0; j<subCombines.length; j++) {
//							builder.append(subCombines[j]);
//						}
//						combineResult.add(builder.toString());
//					}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0);
//				if(maxList.size() > 1) {
//					isAllSame = true;
//					for(int i= maxList.size() -1; i>=1; i--) {
//						if(maxList.get(i) != maxList.get(i-1) ) {
//							result[1] = maxList.get(i)+ maxList.get(i-1);
//							isAllSame = false;
//							break;
//						}
//					}
//					if(isAllSame) {
//						result[1] = maxList.get(0)+maxList.get(1);
//					}
//				} else {
//					result[1] = maxList.get(0);
//				}
//				result[0] = result[0]*190;
//				result[1] = result[1]*190;
//			}
//			else if(mGameType.equals(SscType.threestar_zuliu.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					combineResult.add(data.get(i).replace(",", ""));
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*190;
//				result[1] = maxList.get(maxList.size() -1)*190;
//			}else if(mGameType.equals(SscType.threestar_zusan.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					combineResult.add(data.get(i).replace(",", ""));
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*385;
//				result[1] = maxList.get(maxList.size() -1)*385;
//			} 
////			else if(mGameType.equals(SscType.fourstar_fuxuan.toString())) {
////				//不支持
////			} 
//			else if(mGameType.equals(SscType.fourstar_zhixuan.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//						for(int indexQ = 0; indexQ<strs[0].length(); indexQ++) {
//							for(int indexB = 0; indexB<strs[1].length(); indexB++) {
//								for(int indexS = 0; indexS < strs[2].length(); indexS++) {
//									for(int indexG = 0; indexG < strs[3].length(); indexG++) {
//										combineResult.add(strs[0].substring(indexQ, indexQ+1)
//												+strs[1].substring(indexB, indexB+1)
//												+strs[2].substring(indexS, indexS+1)
//												+strs[3].substring(indexG, indexG+1));
//									}
//								}
//							}
//						}
//				}
//				Collections.sort(combineResult);
//				boolean isFirst = true;
//				int lastIndex = 0;
//				boolean isAllSame = true;
//				ArrayList<Integer>maxList = new ArrayList<Integer>();
//				for(int i=0; i<combineResult.size() - 1; i++) {
//					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
//						isAllSame = false;
//						if(isFirst) {
//							isFirst = false;
//							maxList.add(i+1);
//						} else {
//							maxList.add(i-lastIndex );
//						}
//						lastIndex = i;
//					}
//				}
//				
//				if(isAllSame) {
//					maxList.add(combineResult.size());
//				} else {
//				    maxList.add(combineResult.size() - 1 - lastIndex );
//				}
//				Collections.sort(maxList);
//				result[0] = maxList.get(0)*88;
//				result[1] = maxList.get(maxList.size() -1)*10088;
//			} else if(mGameType.equals(SscType.renxuan_two.toString())) {
//				ArrayList<String>combineResult = new ArrayList<String>();
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					int outer = 0;
//					while(outer < 4) {
//						String[] target = {"#", "#", "#", "#", "#"};
//						int inner = outer+1;
//						while(inner < 5) {
//							for(int indexOne = 0; indexOne < strs[outer].length(); indexOne++) {
//								 if(!strs[outer].subSequence(indexOne, indexOne + 1).equals("#")) {
//								    for(int indexTwo = 0; indexTwo < strs[inner].length(); indexTwo++) {
//								        if(!strs[inner].subSequence(indexTwo, indexTwo + 1).equals("#")) {
//								        	target[outer] = strs[outer].substring(indexOne, indexOne + 1);
//								        	target[inner] = strs[inner].substring(indexTwo, indexTwo + 1);
//								        	combineResult.add(target[0]+target[1]+target[2]+target[3]+target[4]);
//								        }
//								    	for(int targetIndex = 0; targetIndex < target.length; targetIndex++) {
//								    		target[targetIndex] = "#";
//								    	}
//								    }	
//								 }
//							}
//							inner++;
//						}
//						outer++;
//						
//					}
//				}
////				ArrayList<String>filterResult = new ArrayList<String>();
////				for(int i=0; i<combineResult.size(); i++) {
////					String str = combineResult.get(i);
////					int count = 0;
////					for(int index = 0; index < str.length(); index++) {
////						if(str.substring(index, index+1).equals("#")) {
////							count++;
////						} 
////					}
////					if(count == 3) {
////						filterResult.add(str);
////					}
////				}
//				ArrayList<String>indexList = new ArrayList<String>();
//				HashMap<String,String>map = new HashMap<String, String>();
//				for(int i=0; i<combineResult.size(); i++) {
//					String str = combineResult.get(i);
//					String indexStr = "";
//					for(int index = 0; index < str.length(); index++) {
//						if(!str.substring(index, index+1).equals("#")) {
//							indexStr += index;
//						} 
//					}
//					indexList.add(indexStr);
//					if(TextUtils.isEmpty(map.get(indexStr))) {
//						map.put(indexStr, str);
//					} else {
//						map.put(indexStr, map.get(indexStr) + " "+str);
//					}
//				}
//				
//				int min = -1;
//				Iterator<Entry<String, String>> iter = map.entrySet().iterator();
//				while(iter.hasNext()){
//					int max = 0;
//					Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
//					String str = entry.getValue();
//					String[] strs = str.split(" ");
//					Arrays.sort(strs);
//					for(int j=0; j<strs.length; j++) {
//						strs[j] = strs[j].replaceAll("#", "");
//					}
//					String compare = strs[0];
//					int count = 1;
//					for(int j=1; j<strs.length; j++) {
//						if(!compare.equals(strs[j])){
//							compare = strs[j];
//							max = Math.max(max, count);
//							if(min == -1) {
//								min = count;
//							} else {
//								min = Math.min(min, count);
//							}
//							count = 1;
//						} else {
//							count++;
//						}
//					}
//					max = Math.max(max, count);
//					result[1] += max;
//					if(min == -1) {
//						min = count;
//					} else {
//						min = Math.min(min, count);
//					}
//				}
//				result[0] = min*116;
//				result[1] = result[1]*116;
//			} else if(mGameType.equals(SscType.renxuan_one.toString())) {
//				ArrayList<String>wStr = new ArrayList<String>();
//				ArrayList<String>qStr = new ArrayList<String>();
//				ArrayList<String>bStr = new ArrayList<String>();
//				ArrayList<String>sStr = new ArrayList<String>();
//				ArrayList<String>gStr = new ArrayList<String>();
//				
//				for(int i=0; i<data.size(); i++) {
//					String[] strs = data.get(i).replace(",", "").split("|");
//					wStr.add(strs[0]);
//					qStr.add(strs[1]);
//					bStr.add(strs[2]);
//					sStr.add(strs[3]);
//					gStr.add(strs[4]);
//				}
//				int[] counts = new int[10];
//				//wan
//				for(int i=0; i<counts.length; i++) {
//					counts[i] = 0;
//				}
//				for(int i=0; i<wStr.size(); i++) {
//					for(int index = 0; index<sStr.get(i).length(); index++) {
//						String str = wStr.get(i).substring(index, index+1);
//						if(!str.equals("#")) {
//						   counts[Integer.parseInt(str)]++;
//						}
//					}
//				}
//				Arrays.sort(counts);
//				result[1] += counts[counts.length -1]*11;
//				for(int i = 0; i<counts.length; i++) {
//					if(counts[i] > 0) {
//						if(result[0] == 0) {
//							result[0] = counts[i];
//						} else {
//						    result[0] = Math.min(result[0], counts[i]);
//						}
//						break;
//					}
//				}
//				//qian
//				for(int i=0; i<counts.length; i++) {
//					counts[i] = 0;
//				}
//				for(int i=0; i<qStr.size(); i++) {
//					for(int index = 0; index<sStr.get(i).length(); index++) {
//						String str = qStr.get(i).substring(index, index+1);
//						if(!str.equals("#")) {
//						   counts[Integer.parseInt(str)]++;
//						}
//					}
//				}
//				Arrays.sort(counts);
//				result[1] += counts[counts.length -1]*11;
//				for(int i = 0; i<counts.length; i++) {
//					if(counts[i] > 0) {
//						if(result[0] == 0) {
//							result[0] = counts[i];
//						} else {
//						    result[0] = Math.min(result[0], counts[i]);
//						}
//						break;
//					}
//				}
//				//bai
//				for(int i=0; i<counts.length; i++) {
//					counts[i] = 0;
//				}
//				for(int i=0; i<bStr.size(); i++) {
//					for(int index = 0; index<bStr.get(i).length(); index++) {
//						String str = bStr.get(i).substring(index, index+1);
//						if(!str.equals("#")) {
//						   counts[Integer.parseInt(str)]++;
//						}
//					}
//				}
//				Arrays.sort(counts);
//				result[1] += counts[counts.length -1]*11;
//				for(int i = 0; i<counts.length; i++) {
//					if(counts[i] > 0) {
//						if(result[0] == 0) {
//							result[0] = counts[i];
//						} else {
//						    result[0] = Math.min(result[0], counts[i]);
//						}
//						break;
//					}
//				}
//				//shi
//				for(int i=0; i<counts.length; i++) {
//					counts[i] = 0;
//				}
//				for(int i=0; i<sStr.size(); i++) {
//					for(int index = 0; index<sStr.get(i).length(); index++) {
//						String str = sStr.get(i).substring(index, index+1);
//						if(!str.equals("#")) {
//						   counts[Integer.parseInt(str)]++;
//						}
//					}
//				}
//				Arrays.sort(counts);
//				result[1] += counts[counts.length -1]*11;
//				for(int i = 0; i<counts.length; i++) {
//					if(counts[i] > 0) {
//						if(result[0] == 0) {
//							result[0] = counts[i];
//						} else {
//						    result[0] = Math.min(result[0], counts[i]);
//						}
//						break;
//					}
//				}
//				//ge
//				for(int i=0; i<counts.length; i++) {
//					counts[i] = 0;
//				}
//				for(int i=0; i<gStr.size(); i++) {
//					for(int index = 0; index<sStr.get(i).length(); index++) {
//						String str = gStr.get(i).substring(index, index+1);
//						if(!str.equals("#")) {
//						   counts[Integer.parseInt(str)]++;
//						}
//					}
//				}
//				Arrays.sort(counts);
//				result[1] += counts[counts.length -1]*11;
//				for(int i = 0; i<counts.length; i++) {
//					if(counts[i] > 0) {
//						if(result[0] == 0) {
//							result[0] = counts[i];
//						} else {
//						    result[0] = Math.min(result[0], counts[i]);
//						}
//						break;
//					}
//				}
//				result[0] = result[0]*11;
//			}
//			
//			return result;
//		}
		/**
		 * 江西时时彩
		 * @param data
		 * @param mGameType
		 * @return
		 */
		public static int[] getSscBonus(LinkedList<String>resource, String mGameType, String city) {
			LinkedList<String>data = new LinkedList<String>();
			if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())
					|| mGameType.equals(SscType.onestar_zhixuan.toString())) {
				for(int i=0; i<resource.size(); i++) {
                    data.add(resource.get(i));
				}
			} else {
			for(int i=0; i<resource.size(); i++) {
				String tempStr = resource.get(i).replace(" | ", "|").replace(" ", "");
				data.add(tempStr.replace("|", " "));
			}
			}
			int result[] = new int[2];
			result[0] = 0;
			result[1] = 0;
			if(mGameType.equals(SscType.fivestar_zhixuan.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 100000;
				} else {
					factor = 116000;
				}
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
					for(int indexW = 0; indexW<strs[0].length(); indexW++) {
						for(int indexQ = 0; indexQ<strs[1].length(); indexQ++) {
							for(int indexB = 0; indexB<strs[2].length(); indexB++) {
								for(int indexS = 0; indexS < strs[3].length(); indexS++) {
									for(int indexG = 0; indexG < strs[4].length(); indexG++) {
										combineResult.add(strs[0].substring(indexW, indexW+1)
												+strs[1].substring(indexQ, indexQ+1)
												+strs[2].substring(indexB, indexB+1)
												+strs[3].substring(indexS, indexS+1)
												+strs[4].substring(indexG, indexG+1));
									}
								}
							}
						}
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*factor;
				result[1] = maxList.get(maxList.size() -1)*factor;
			}
//			else if(mGameType.equals(SscType.fivestar_fuxuan.toString())) {
//				//不支持
//			} 
			else if(mGameType.equals(SscType.fivestar_tongxuan.toString())) {
				int factorMax = 0;
				int factorMin = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factorMax = 20440;
					factorMin = 20;
				} else {
					factorMax = 20460;
					factorMin = 30;
				}
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
					for(int indexW = 0; indexW<strs[0].length(); indexW++) {
						for(int indexQ = 0; indexQ<strs[1].length(); indexQ++) {
							for(int indexB = 0; indexB<strs[2].length(); indexB++) {
								for(int indexS = 0; indexS < strs[3].length(); indexS++) {
									for(int indexG = 0; indexG < strs[4].length(); indexG++) {
										combineResult.add(strs[0].substring(indexW, indexW+1)
												+strs[1].substring(indexQ, indexQ+1)
												+strs[2].substring(indexB, indexB+1)
												+strs[3].substring(indexS, indexS+1)
												+strs[4].substring(indexG, indexG+1));
									}
								}
							}
						}
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(maxList.size() -1)*factorMin;
				result[1] = maxList.get(maxList.size() -1)*factorMax;
				
			} 
//			else if(mGameType.equals(SscType.twostar_fuxuan.toString())) {
//				//不支持
//			} 
			else if(mGameType.equals(SscType.twostar_zhixuan.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 100;
				} else {
					factor = 116;
				}
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
								for(int indexS = 0; indexS < strs[0].length(); indexS++) {
									for(int indexG = 0; indexG < strs[1].length(); indexG++) {
										combineResult.add(strs[0].substring(indexS, indexS+1)
												+strs[1].substring(indexG, indexG+1));
									}
								}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*factor;
				result[1] = maxList.get(maxList.size() -1)*factor;
			} else if(mGameType.equals(SscType.twostar_zuxuan.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 50;
				} else {
					factor = 58;
				}
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					combineResult.add(data.get(i));
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*factor;
				result[1] = maxList.get(maxList.size() -1)*factor;
			} else if(mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 100;
				} else {
					factor = 116;
				}
				int[]sums = new int[19];
				for(int i=0; i<19; i++) {
					sums[i] = 0;
				}
				for(int i=0; i< data.size(); i++) {
					String strs[] = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						sums[Integer.parseInt(strs[index])] += factor;
					}
				}
				int max = 0;
				int min = 0;
				boolean isFirst = true;
				for(int i=0; i<19; i++) {
					if(sums[i] > 0) {
						if(isFirst) {
							max = sums[i];
							min = sums[i];
							isFirst = false;
						} else {
							max = Math.max(max, sums[i]);
							min = Math.min(min, sums[i]);
						}
					}
				}
				result[0] = min;
				result[1] = max;
			} else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 50;
				} else {
					factor = 58;
				}
				int[]sums = new int[17];
				for(int i=0; i<17; i++) {
					sums[i] = 0;
				}
				for(int i=0; i< data.size(); i++) {
					String strs[] = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						sums[Integer.parseInt(strs[index])-1] += factor;
					}
				}
				int max = 0;
				int min = 0;
				boolean isFirst = true;
				for(int i=0; i<17; i++) {
					if(sums[i] > 0) {
						if(isFirst) {
							max = sums[i];
							min = sums[i];
							isFirst = false;
						} else {
							max = Math.max(max, sums[i]);
							min = Math.min(min, sums[i]);
						}
					}
				}
				result[0] = min;
				result[1] = max;
			}else if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 50;
				} else {
					factor = 58;
				}
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(strs)), 2);
					for(int index = 0; index < combines.size(); index++) {
						String[] subCombines = combines.get(index);
						StringBuilder builder = new StringBuilder();
						for(int j = 0; j<subCombines.length; j++) {
							builder.append(subCombines[j]);
						}
						combineResult.add(builder.toString());
					}
				}
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				boolean[]used = new boolean[combineResult.size()];
				for(int i=0; i<used.length; i++) {
					used[i] = false;
				}
				for(int i=0; i<combineResult.size() - 1; i++) {
					int count = 1;
					for(int index = i+1; index < combineResult.size(); index++) {
						if(!used[i] && !used[index]) {
							String str0 = combineResult.get(i);
							String str1 = combineResult.get(index);
							if(str0.contains(str1.subSequence(0, 1)) && str0.contains(str1.subSequence(1, 2))) {
								count++;
								used[index] = true;
							}
						}
					}
					maxList.add(count);
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*factor;
				result[1] = maxList.get(maxList.size()-1)*factor;
			}else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 10;
				} else {
					factor = 11;
				}
				int size = 10;
				int[]sums = new int[size];
				for(int i=0; i<size; i++) {
					sums[i] = 0;
				}
				for(int i=0; i< data.size(); i++) {
					String strs[] = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						sums[Integer.parseInt(strs[index])] += factor;
					}
				}
				Arrays.sort(sums);
				for(int i=0; i<size; i++) {
					if(sums[i] != 0 ) {
						result[0] = sums[i];
						break;
					}
				}
//				int max = 0;
//				int min = 0;
//				boolean isFirst = true;
//				for(int i=0; i<size; i++) {
//					if(sums[i] > 0) {
//						if(isFirst) {
//							max = sums[i];
//							min = sums[i];
//							isFirst = false;
//						} else {
//							max = Math.max(max, sums[i]);
//							min = Math.min(min, sums[i]);
//						}
//					}
//				}
				
				result[1] = sums[sums.length - 1];
			} else if(mGameType.equals(SscType.dxds.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
								for(int indexS = 0; indexS < strs[0].length(); indexS++) {
									for(int indexG = 0; indexG < strs[1].length(); indexG++) {
										combineResult.add(strs[0].substring(indexS, indexS+1)
												+strs[1].substring(indexG, indexG+1));
									}
								}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*4;
				result[1] = maxList.get(maxList.size() -1)*4;
			} 
//			else if(mGameType.equals(SscType.threestar_fuxuan.toString())) {
//				//不支持
//			}
			else if(mGameType.equals(SscType.threestar_zhixuan.toString())){
				int factor = 0;
				if(city.equals(Constants.City.chongqing.toString())) {
					factor = 1000;
				} else {
					factor = 1160;
				}
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
							for(int indexB = 0; indexB<strs[0].length(); indexB++) {
								for(int indexS = 0; indexS < strs[1].length(); indexS++) {
									for(int indexG = 0; indexG < strs[2].length(); indexG++) {
										combineResult.add(strs[0].substring(indexB, indexB+1)
												+strs[1].substring(indexS, indexS+1)
												+strs[2].substring(indexG, indexG+1));
									}
								}
							}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*factor;
				result[1] = maxList.get(maxList.size() -1)*factor;
			} else if(mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())) {
				int size = 28;
				int[]sums = new int[size];
				for(int i=0; i<size; i++) {
					sums[i] = 0;
				}
				for(int i=0; i< data.size(); i++) {
					String strs[] = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						sums[Integer.parseInt(strs[index])] += 1160;
					}
				}
				int max = 0;
				int min = 0;
				boolean isFirst = true;
				for(int i=0; i<size; i++) {
					if(sums[i] > 0) {
						if(isFirst) {
							max = sums[i];
							min = sums[i];
							isFirst = false;
						} else {
							max = Math.max(max, sums[i]);
							min = Math.min(min, sums[i]);
						}
					}
				}
				result[0] = min;
				result[1] = max;
			} else if(mGameType.equals(SscType.threestar_zuliu_hezhi.toString())) {
				int size = 22;
				int[]sums = new int[size];
				for(int i=0; i<size; i++) {
					sums[i] = 0;
				}
				for(int i=0; i< data.size(); i++) {
					String strs[] = data.get(i).split(" ");
					for(int index = 0; index < strs.length; index++) {
						sums[Integer.parseInt(strs[index])-3] += 190;
					}
				}
				int max = 0;
				int min = 0;
				boolean isFirst = true;
				for(int i=0; i<size; i++) {
					if(sums[i] > 0) {
						if(isFirst) {
							max = sums[i];
							min = sums[i];
							isFirst = false;
						} else {
							max = Math.max(max, sums[i]);
							min = Math.min(min, sums[i]);
						}
					}
				}
				result[0] = min;
				result[1] = max;
			} else if(mGameType.equals(SscType.threestar_zusan_baohao.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String str = data.get(i);
					for(int indexOne=0; indexOne<str.length(); indexOne++) {
						for(int indexTwo=0; indexTwo<str.length(); indexTwo++) {
							if(indexOne != indexTwo) {
								combineResult.add(str.substring(indexOne, indexOne+1)
										+str.substring(indexOne, indexOne+1)
										+str.substring(indexTwo, indexTwo+1));
							}
						}
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*385;
				result[1] = maxList.get(maxList.size() -1)*385;
			}else if(mGameType.equals(SscType.threestar_zuliu_baohao.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					ArrayList<String[]>combines = DoubleColorFromMachine.combine(new ArrayList<String>(Arrays.asList(strs)), 3);
					for(int index = 0; index < combines.size(); index++) {
						String[] subCombines = combines.get(index);
						StringBuilder builder = new StringBuilder();
						for(int j = 0; j<subCombines.length; j++) {
							builder.append(subCombines[j]);
						}
						combineResult.add(builder.toString());
					}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0);
				if(maxList.size() > 1) {
					isAllSame = true;
					for(int i= maxList.size() -1; i>=1; i--) {
						if(maxList.get(i) != maxList.get(i-1) ) {
							result[1] = maxList.get(i)+ maxList.get(i-1);
							isAllSame = false;
							break;
						}
					}
					if(isAllSame) {
						result[1] = maxList.get(0)+maxList.get(1);
					}
				} else {
					result[1] = maxList.get(0);
				}
				result[0] = result[0]*190;
				result[1] = result[1]*190;
			}
			else if(mGameType.equals(SscType.threestar_zuliu.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					combineResult.add(data.get(i));
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*190;
				result[1] = maxList.get(maxList.size() -1)*190;
			}else if(mGameType.equals(SscType.threestar_zusan.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					combineResult.add(data.get(i));
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*385;
				result[1] = maxList.get(maxList.size() -1)*385;
			} 
//			else if(mGameType.equals(SscType.fourstar_fuxuan.toString())) {
//				//不支持
//			} 
			else if(mGameType.equals(SscType.fourstar_zhixuan.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
						for(int indexQ = 0; indexQ<strs[0].length(); indexQ++) {
							for(int indexB = 0; indexB<strs[1].length(); indexB++) {
								for(int indexS = 0; indexS < strs[2].length(); indexS++) {
									for(int indexG = 0; indexG < strs[3].length(); indexG++) {
										combineResult.add(strs[0].substring(indexQ, indexQ+1)
												+strs[1].substring(indexB, indexB+1)
												+strs[2].substring(indexS, indexS+1)
												+strs[3].substring(indexG, indexG+1));
									}
								}
							}
						}
				}
				Collections.sort(combineResult);
				boolean isFirst = true;
				int lastIndex = 0;
				boolean isAllSame = true;
				ArrayList<Integer>maxList = new ArrayList<Integer>();
				for(int i=0; i<combineResult.size() - 1; i++) {
					if(!combineResult.get(i).equals(combineResult.get(i+1))) {
						isAllSame = false;
						if(isFirst) {
							isFirst = false;
							maxList.add(i+1);
						} else {
							maxList.add(i-lastIndex );
						}
						lastIndex = i;
					}
				}
				
				if(isAllSame) {
					maxList.add(combineResult.size());
				} else {
				    maxList.add(combineResult.size() - 1 - lastIndex );
				}
				Collections.sort(maxList);
				result[0] = maxList.get(0)*88;
				result[1] = maxList.get(maxList.size() -1)*10088;
			} else if(mGameType.equals(SscType.renxuan_two.toString())) {
				ArrayList<String>combineResult = new ArrayList<String>();
				for(int i=0; i<data.size(); i++) {
					String[] strs = data.get(i).split(" ");
					int outer = 0;
					while(outer < 4) {
						String[] target = {"#", "#", "#", "#", "#"};
						int inner = outer+1;
						while(inner < 5) {
							for(int indexOne = 0; indexOne < strs[outer].length(); indexOne++) {
								 if(!strs[outer].subSequence(indexOne, indexOne + 1).equals("#")) {
								    for(int indexTwo = 0; indexTwo < strs[inner].length(); indexTwo++) {
								        if(!strs[inner].subSequence(indexTwo, indexTwo + 1).equals("#")) {
								        	target[outer] = strs[outer].substring(indexOne, indexOne + 1);
								        	target[inner] = strs[inner].substring(indexTwo, indexTwo + 1);
								        	combineResult.add(target[0]+target[1]+target[2]+target[3]+target[4]);
								        }
								    	for(int targetIndex = 0; targetIndex < target.length; targetIndex++) {
								    		target[targetIndex] = "#";
								    	}
								    }	
								 }
							}
							inner++;
						}
						outer++;
						
					}
				}
//				ArrayList<String>filterResult = new ArrayList<String>();
//				for(int i=0; i<combineResult.size(); i++) {
//					String str = combineResult.get(i);
//					int count = 0;
//					for(int index = 0; index < str.length(); index++) {
//						if(str.substring(index, index+1).equals("#")) {
//							count++;
//						} 
//					}
//					if(count == 3) {
//						filterResult.add(str);
//					}
//				}
				ArrayList<String>indexList = new ArrayList<String>();
				HashMap<String,String>map = new HashMap<String, String>();
				for(int i=0; i<combineResult.size(); i++) {
					String str = combineResult.get(i);
					String indexStr = "";
					for(int index = 0; index < str.length(); index++) {
						if(!str.substring(index, index+1).equals("#")) {
							indexStr += index;
						} 
					}
					indexList.add(indexStr);
					if(TextUtils.isEmpty(map.get(indexStr))) {
						map.put(indexStr, str);
					} else {
						map.put(indexStr, map.get(indexStr) + " "+str);
					}
				}
				
				int min = -1;
				Iterator<Entry<String, String>> iter = map.entrySet().iterator();
				while(iter.hasNext()){
					int max = 0;
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
					String str = entry.getValue();
					String[] strs = str.split(" ");
					Arrays.sort(strs);
					for(int j=0; j<strs.length; j++) {
						strs[j] = strs[j].replaceAll("#", "");
					}
					String compare = strs[0];
					int count = 1;
					for(int j=1; j<strs.length; j++) {
						if(!compare.equals(strs[j])){
							compare = strs[j];
							max = Math.max(max, count);
							if(min == -1) {
								min = count;
							} else {
								min = Math.min(min, count);
							}
							count = 1;
						} else {
							count++;
						}
					}
					max = Math.max(max, count);
					result[1] += max;
					if(min == -1) {
						min = count;
					} else {
						min = Math.min(min, count);
					}
				}
				result[0] = min*116;
				result[1] = result[1]*116;
			} else if(mGameType.equals(SscType.renxuan_one.toString())) {
				ArrayList<String>wStr = new ArrayList<String>();
				ArrayList<String>qStr = new ArrayList<String>();
				ArrayList<String>bStr = new ArrayList<String>();
				ArrayList<String>sStr = new ArrayList<String>();
				ArrayList<String>gStr = new ArrayList<String>();
				
				for(int i=0; i<data.size(); i++) {
					String[]strs = data.get(i).split(" ");
					wStr.add(strs[0]);
					qStr.add(strs[1]);
					bStr.add(strs[2]);
					sStr.add(strs[3]);
					gStr.add(strs[4]);
				}
				int[] counts = new int[10];
				//wan
				for(int i=0; i<counts.length; i++) {
					counts[i] = 0;
				}
				for(int i=0; i<wStr.size(); i++) {
					for(int index = 0; index<sStr.get(i).length(); index++) {
						String str = wStr.get(i).substring(index, index+1);
						if(!str.equals("#")) {
						   counts[Integer.parseInt(str)]++;
						}
					}
				}
				Arrays.sort(counts);
				result[1] += counts[counts.length -1]*11;
				for(int i = 0; i<counts.length; i++) {
					if(counts[i] > 0) {
						if(result[0] == 0) {
							result[0] = counts[i];
						} else {
						    result[0] = Math.min(result[0], counts[i]);
						}
						break;
					}
				}
				//qian
				for(int i=0; i<counts.length; i++) {
					counts[i] = 0;
				}
				for(int i=0; i<qStr.size(); i++) {
					for(int index = 0; index<sStr.get(i).length(); index++) {
						String str = qStr.get(i).substring(index, index+1);
						if(!str.equals("#")) {
						   counts[Integer.parseInt(str)]++;
						}
					}
				}
				Arrays.sort(counts);
				result[1] += counts[counts.length -1]*11;
				for(int i = 0; i<counts.length; i++) {
					if(counts[i] > 0) {
						if(result[0] == 0) {
							result[0] = counts[i];
						} else {
						    result[0] = Math.min(result[0], counts[i]);
						}
						break;
					}
				}
				//bai
				for(int i=0; i<counts.length; i++) {
					counts[i] = 0;
				}
				for(int i=0; i<bStr.size(); i++) {
					for(int index = 0; index<bStr.get(i).length(); index++) {
						String str = bStr.get(i).substring(index, index+1);
						if(!str.equals("#")) {
						   counts[Integer.parseInt(str)]++;
						}
					}
				}
				Arrays.sort(counts);
				result[1] += counts[counts.length -1]*11;
				for(int i = 0; i<counts.length; i++) {
					if(counts[i] > 0) {
						if(result[0] == 0) {
							result[0] = counts[i];
						} else {
						    result[0] = Math.min(result[0], counts[i]);
						}
						break;
					}
				}
				//shi
				for(int i=0; i<counts.length; i++) {
					counts[i] = 0;
				}
				for(int i=0; i<sStr.size(); i++) {
					for(int index = 0; index<sStr.get(i).length(); index++) {
						String str = sStr.get(i).substring(index, index+1);
						if(!str.equals("#")) {
						   counts[Integer.parseInt(str)]++;
						}
					}
				}
				Arrays.sort(counts);
				result[1] += counts[counts.length -1]*11;
				for(int i = 0; i<counts.length; i++) {
					if(counts[i] > 0) {
						if(result[0] == 0) {
							result[0] = counts[i];
						} else {
						    result[0] = Math.min(result[0], counts[i]);
						}
						break;
					}
				}
				//ge
				for(int i=0; i<counts.length; i++) {
					counts[i] = 0;
				}
				for(int i=0; i<gStr.size(); i++) {
					for(int index = 0; index<sStr.get(i).length(); index++) {
						String str = gStr.get(i).substring(index, index+1);
						if(!str.equals("#")) {
						   counts[Integer.parseInt(str)]++;
						}
					}
				}
				Arrays.sort(counts);
				result[1] += counts[counts.length -1]*11;
				for(int i = 0; i<counts.length; i++) {
					if(counts[i] > 0) {
						if(result[0] == 0) {
							result[0] = counts[i];
						} else {
						    result[0] = Math.min(result[0], counts[i]);
						}
						break;
					}
				}
				result[0] = result[0]*11;
			}
			
			return result;
		}
}
