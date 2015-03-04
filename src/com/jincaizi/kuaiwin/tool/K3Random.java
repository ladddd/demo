package com.jincaizi.kuaiwin.tool;

import java.util.Random;

public class K3Random {
	/**
	 * 
	 * @return
	 */
	public static int[] getThreeDiff() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[3];
		 boolean[] used = new boolean[6]; 
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(6);
			if (used[index]) {     
			      continue;//结束本次循环，开始下一次循环 
			}  
			result[i++] = index;
			used[index] = true;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
	public static int[] getTwoDiff() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[2];
		 boolean[] used = new boolean[6]; 
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(6);
			if (used[index]) {     
			      continue;//结束本次循环，开始下一次循环 
			}  
			result[i++] = index;
			used[index] = true;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
}
