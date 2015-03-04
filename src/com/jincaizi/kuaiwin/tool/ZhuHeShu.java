package com.jincaizi.kuaiwin.tool;

public class ZhuHeShu {
 private int k,count=0;
 public  int init(int n,int m){
  k=m;
  count=0;
  int[] a = new int[n];
  int[] b = new int[m]; 
  for (int i = 0; i < n; i++)
   a[i] = i + 1;
  return combine(a, n, m, b);
 }
 
 public  int combine( int[] a, int n,  int m,  int[] b){
  for (int i = n; i >= m; i--) {
   b[m - 1] = i - 1; 
   if (m > 1)
    combine(a, i - 1, m - 1, b); 
   else 
   {
    for (int j = k - 1; j >= 0; j--)
     System.out.print(a[b[j]]);
    count++; 
    System.out.println("");

   }
  }
  return count;
 }

}
