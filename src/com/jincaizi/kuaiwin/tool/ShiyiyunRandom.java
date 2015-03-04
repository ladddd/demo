package com.jincaizi.kuaiwin.tool;

import java.util.ArrayList;
import java.util.Random;

public class ShiyiyunRandom {
	public static ArrayList<String> getSyyBallNoRePeat(int count) {
		// 2、创建生成随即数的对象
		Random random = new Random();
		// 3、创建空白数组，用于存放红球
		String[] pool = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11" };
		ArrayList<String> result = new ArrayList<String>();
		// 4、从pool中随即产生红球
		int i = 0;
		while (i < count) {
			int index = random.nextInt(11);
			if (result.contains(pool[index])) {
				continue;
			}
			result.add(pool[index]);
			i++;
		}

		return result;
	}
}
