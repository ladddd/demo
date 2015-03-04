package com.jincaizi.kuaiwin.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlsRandom {
 
 public static final String TAG = "PlsRandom";

 public static ArrayList<String> getPLSBallRePeat(int count) {
  // 2、创建生成随即数的对象
  Random random = new Random();
  // 3、创建空白数组，用于存放红球
  ArrayList<String> result = new ArrayList<String>();
  // 4、从pool中随即产生红球
  int i = 0;
  while( i < count) {
   int index = random.nextInt(10);
   result.add(String.valueOf(index));
            i++;
  }
  
  return result;
 }
 public static ArrayList<String> getPLSBallNoRePeat(int count) {
	  // 2、创建生成随即数的对象
	  Random random = new Random();
	  // 3、创建空白数组，用于存放红球
	  ArrayList<String> result = new ArrayList<String>();
	  // 4、从pool中随即产生红球
	  int i = 0;
	  while( i < count) {
	   int index = random.nextInt(10);
	   if(result.contains(String.valueOf(index))) {
	    continue;
	   }
	   result.add(String.valueOf(index));
	            i++;
	  }
	  
	  return result;
	 }
 public static ArrayList<String> getPLSBallZS() {
  // 2、创建生成随即数的对象
  Random random = new Random();
  // 3、创建空白数组，用于存放红球
  ArrayList<String> result = new ArrayList<String>();
  // 4、从pool中随即产生红球
  int i = 0;
  while( i < 2) {
   int index = random.nextInt(10);
   if(i == 0 ) {
   result.add(String.valueOf(index));
   }
   result.add(String.valueOf(index));
            i++;
  }
  Collections.sort(result);
  return result;
 }

 public static String getPlsHZZX() {
  // 2、创建生成随即数的对象
  Random random = new Random();
  // 3、创建空白数组，用于存放红球
  String result;
  // 4、从pool中随即产生红球
  int index = random.nextInt(28);
  result = String.valueOf(index);

  return result;
 }

 public static String getPlsHZZS() {
  // 2、创建生成随即数的对象
  Random random = new Random();
  // 3、创建空白数组，用于存放红球
  String result;
  // 4、从pool中随即产生红球
  while (true) {
   int index = random.nextInt(27);
   if (index > 0) {
    result = String.valueOf(index);
    break;
   }
  }
  return result;
 }

 public static String getPlsHZZL() {
  // 2、创建生成随即数的对象
  Random random = new Random();
  // 3、创建空白数组，用于存放红球
  String result;
  // 4、从pool中随即产生红球
  while (true) {
   int index = random.nextInt(25);
   if (index > 2) {
    result=String.valueOf(index);
    break;
   }
  }
  return result;
 }

}
