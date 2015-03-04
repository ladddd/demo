package com.jincaizi.kuaiwin.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.util.Log;

public class DoubleColorFromMachine {
 public static final String TAG = "DoubleColorFromMachine";
   public static String[] getDoubleBall() {  
    //1、创建球池（从球池中抽取红球和蓝球）  
    String[] pool = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
      "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
      "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33"};
    //2、创建标记池，用于检测该球的序号是否被用过（为了防止取出相同的球）   
    //used数组每个元素的默认值是false
    boolean[] used = new boolean[pool.length]; 
    //3、创建生成随即数的对象
    Random random = new Random();   
    //4、创建空白数组，用于存放红球   
    String[] result = new String[6];  
    //5、从pool中随即产生红球  
    int i = 0;   
    while (true) {    
     //产生从0到33的下标：再Java中，范围是包前不包后的。[0, 33)    
     int index = random.nextInt(pool.length);    
     //检查该下标是否被用过  
     if (used[index]) {     
      continue;//结束本次循环，开始下一次循环 
      }    
     //如果没有用过   
     result[i++] = pool[index];
     used[index] = true; 
     if (i == result.length) {
      break;    
        }   
     }   
    //6、对红球进行排序 
    Arrays.sort(result);   
    //7、对数组扩容，用于放置蓝球  
    result = Arrays.copyOf(result, result.length + 1);
    result[result.length - 1] = pool[random.nextInt(16)];   
    return result;
      }
   /**
    * 随机产生指定注数的双色球
    * @param count 注数
    * @return
    */
   public static ArrayList<String[]> getSepcificDoubleBall (int count) {
    ArrayList<String[]>results = new ArrayList<String[]>();
    for(int i=0; i<count ; i++) {
     results.add(getDoubleBall());
    }
    return results;
   }
   

     /** 
      * 从n个数字中选择m个数字 
      * @param a 
      * @param m 
      * @return 
      */ 
     public static ArrayList<String[]> combine(ArrayList<String> a,int m){ 
         int n = a.size(); 
         if(m>n){ 
             Log.d(TAG,"错误！数组a中只有"+n+"个元素。"+m+"大于"+2+"!!!"); 
         } 
        
         ArrayList<String[]> result = new ArrayList<String[]>(); 
         if(m == 1) {
        	 for(int i = 0; i< a.size(); i++) {
        		 String[] temp = new String[]{a.get(i)};
        		 result.add(temp);
        	 }
        	 return result;
         }
         if(m == n) {
        	 String[] temp = new String[n];
        	 for(int i = 0; i< n; i++) {
        		temp[i] = a.get(i);
        	 }
        	 result.add(temp);
        	 return result;
         }
          
         int[] bs = new int[n]; 
         for(int i=0;i<n;i++){ 
             bs[i]=0; 
         } 
         //初始化 
         for(int i=0;i<m;i++){ 
             bs[i]=1; 
         } 
         boolean flag = true; 
         boolean tempFlag = false; 
         int pos = 0; 
         int sum = 0; 
         //首先找到第一个10组合，然后变成01，同时将左边所有的1移动到数组的最左边 
         do{ 
             sum = 0; 
             pos = 0; 
             tempFlag = true;  
             result.add(print(bs,a,m)); 
              
             for(int i=0;i<n-1;i++){ 
                 if(bs[i]==1 && bs[i+1]==0 ){ 
                     bs[i]=0; 
                     bs[i+1]=1; 
                     pos = i; 
                     break; 
                 } 
             } 
             //将左边的1全部移动到数组的最左边 
              
             for(int i=0;i<pos;i++){ 
                 if(bs[i]==1){ 
                     sum++; 
                 } 
             } 
             for(int i=0;i<pos;i++){ 
                 if(i<sum){ 
                     bs[i]=1; 
                 }else{ 
                     bs[i]=0; 
                 } 
             } 
              
             //检查是否所有的1都移动到了最右边 
             for(int i= n-m;i<n;i++){ 
                 if(bs[i]==0){ 
                     tempFlag = false; 
                     break; 
                 } 
             } 
             if(tempFlag==false){ 
                 flag = true; 
             }else{ 
                 flag = false; 
             } 
              
         }while(flag); 
         result.add(print(bs,a,m)); 
          
         return result; 
     } 

      
     private static String[] print(int[] bs,ArrayList<String>a,int m){ 
    	 String[] result = new String[m]; 
         int pos= 0; 
         for(int i=0;i<bs.length;i++){ 
             if(bs[i]==1){ 
                 result[pos]=a.get(i); 
                 pos++; 
             } 
         } 
         return result ; 
     } 


}
