package com.jincaizi.kuaiwin.tool;

import java.util.Arrays;
import java.util.Random;

public class DltRandom {
	public static String[] getDltBall() {
		// 1、创建球池（从球池中抽取红球和蓝球）
		String[] pool = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
				"20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
				"30", "31", "32", "33", "34", "35" };
		// 2、创建标记池，用于检测该球的序号是否被用过（为了防止取出相同的球）
		// used数组每个元素的默认值是false
		boolean[] used =  new boolean[]{
				false, false, false, false, false, false, false, false, false, false, false,false,
				false, false, false, false, false, false, false, false, false, false, false,false,
				false, false, false, false, false, false, false, false, false, false, false,
			};
		// 3、创建生成随即数的对象
		Random random = new Random();
		// 4、创建空白数组，用于存放红球
		String[] result = new String[5];
		// 5、从pool中随即产生红球
		int i = 0;
		while (true) {
			// 产生从0到33的下标：再Java中，范围是包前不包后的。[0, 33)
			int index = random.nextInt(pool.length);
			// 检查该下标是否被用过
			if (used[index]) {
				continue;// 结束本次循环，开始下一次循环
			}
			// 如果没有用过
			result[i++] = pool[index];
			used[index] = true;
			if (i == result.length) {
				break;
			}
		}
		// 6、对红球进行排序
		Arrays.sort(result);
		// 7、对数组扩容，用于放置蓝球
		result = Arrays.copyOf(result, result.length + 2);
		String[] blueResult = new String[2];
		boolean[]blueUsed = new boolean[]{
			false, false, false, false, false, false, false, false, false, false, false,false
		};
		int j = 0;
		while (true) {
			// 产生从0到33的下标：再Java中，范围是包前不包后的。[0, 33)
			int index = random.nextInt(12);
			// 检查该下标是否被用过
			if (blueUsed[index]) {
				continue;// 结束本次循环，开始下一次循环
			}
			// 如果没有用过
			blueResult[j++] = pool[index];
			blueUsed[index] = true;
			if (j == blueResult.length) {
				break;
			}
		}
		Arrays.sort(blueResult);
		result[result.length - 2] = blueResult[0];
//		
		result[result.length - 1] = blueResult[1];
		return result;
	}

}
