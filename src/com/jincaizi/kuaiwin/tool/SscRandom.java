package com.jincaizi.kuaiwin.tool;

import java.util.Arrays;
import java.util.Random;

import com.jincaizi.kuaiwin.utils.Constants.SscType;

public class SscRandom {
	public static int[] getFiveStar() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[5];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
			// 
			result[i++] = index;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
	public static int[] getFourStar() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[4];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
			// 
			result[i++] = index;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
	public static int[] getThreeStar() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[3];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
			// 
			result[i++] = index;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
	public static int[] getDXDS() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[2];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(4);
			// 
			result[i++] = index;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
	public static int getOneStar() {
		Random random = new Random();
		return random.nextInt(10);
	}
	public static int getTwoStarZuHZ() {
		Random random = new Random();
		int result = 0;
		while(true) {
		 result = random.nextInt(18);
		 if(result !=0) {
			 break;
		 }
		}
		return result;
	}
	public static int getTwoStarZhiHZ() {
		Random random = new Random();
		return random.nextInt(19);
	}
	/**
	 * 两星直选，复选，可十位，个位号码可相同
	 * @return
	 */
	public static int[] getTwoStarSame() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[2];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
			// 
			result[i++] = index;
			if (i == result.length) {
				break;
			}
		}
		return result;
	}
	/**
	 * 两星组选，可十位，个位号码不同
	 * @return
	 */
	public static int[] getTwoStarDiff() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[2];
		 boolean[] used = new boolean[10]; 
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
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
	
	public static int getThreeStarHZOfZhixuan() {
		Random random = new Random();
		return random.nextInt(28);
	}
	public static int getThreeStarHZOfZusan() {
		Random random = new Random();
		int result;
		while(true) {
		    result = random.nextInt(27);
		    if(result != 0) {
		    	break;
		    }
		}
		return result;
	}
	public static int getThreeStarHZOfZuliu() {
		Random random = new Random();
		int result;
		while(true) {
		    result = random.nextInt(25);
		    if(result != 0 && result != 1 && result != 2) {
		    	break;
		    }
		}
		return result;
	}
	public static int[] getThreeStarBHOfZuliu() {
		Random random = new Random();
		int[] result = new int[3];
		boolean[]used = new boolean[10];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
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
	public static int[] getThreeStarBHOfZusan() {
		Random random = new Random();
		int[] result = new int[2];
		boolean[]used = new boolean[10];
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
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
	public static int[] getThreeStarOfZusan() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[2];
		 boolean[] used = new boolean[10]; 
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
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
	public static int[] getThreeStarOfZuliu() {
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		int[] result = new int[3];
		 boolean[] used = new boolean[10]; 
		int i = 0;
		while (true) {
			// 产生从0到10的下标：再Java中，范围是包前不包后的。[0, 10)
			int index = random.nextInt(10);
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
	public static int[] getRenXuanOne() {
		Random random = new Random();
		int[]result = new int[2];
		result[0] = random.nextInt(5);//index
		result[1] = random.nextInt(10);//value
		return result;
	}
	public static int[] getRenXuanTwo() {
		Random random = new Random();
		int[]result = new int[4];
		result[0] = random.nextInt(5);//index
		while(true) {
		 int index = random.nextInt(5);//index
		 if(result[0] != index) {
			 result[1] = index;
			 break;
		 }
		}
		result[2] = random.nextInt(10);//value
		result[3] = random.nextInt(10);//value
		return result;
	}
}
