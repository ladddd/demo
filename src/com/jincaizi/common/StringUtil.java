package com.jincaizi.common;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/2/3.
 */
public class StringUtil {

    public static final String[] SUM_CONTENT = { "3", "4", "5", "6", "7", "8", "9",
            "10", "11","12","13","14","15","16","17","18" };
    public static String[] THREE_SAME_CONTENT = { "111", "222", "333",
            "444", "555", "666", "三同号通选" };
    public static String[] TWO_SAME_CONTENT = {"11", "22", "33","44","55","66",
            "1", "2", "3","4","5","6",
            "11*", "22*", "33*","44*","55*","66*"};
    public static String[] THREE_DIFF_CONTENT = {
            "1", "2", "3","4","5","6",
            "三连号通选"};

    //字符串工具函数 取最后两位
    public static int getIssue(String source)
    {
        if (source.length() < 2)
        {
            return -1;
        }

        return Integer.valueOf(source.substring(source.length() - 2));
    }

    //字符串工具函数 取三个开奖号码，计算和值
    public static int getWinningNumberSum(String source)
    {
        if (!source.contains(" ") || source.length() < 5)
        {
            return 0;
        }
        int sum = 0;

        sum += Integer.valueOf(String.valueOf(source.charAt(0)));
        sum += Integer.valueOf(String.valueOf(source.charAt(2)));
        sum += Integer.valueOf(String.valueOf(source.charAt(4)));

        return sum;
    }

    public static String getIssue(int i)
    {
        if (i > 0 && i < 10)
        {
            return "0" + String.valueOf(i) + "期";
        }
        else if (i >= 10 && i < 100)
        {
            return String.valueOf(i) + "期";
        }
        else
        {
            return "xx期";
        }
    }

    public static String getResultNumberString(int number)
    {
        if (number > 0 && number < 10)
        {
            return "0" + String.valueOf(number);
        }
        else if (number >= 10 && number < 100)
        {
            return String.valueOf(number);
        }
        else
        {
            return "";
        }
    }

    public static int getResultNumber(String source)
    {
        if (source == null || source.length() <= 0)
        {
            return 0;
        }

        return Integer.valueOf(String.valueOf(source.charAt(0)))*100 +
                Integer.valueOf(String.valueOf(source.charAt(2)))*10 +
                Integer.valueOf(String.valueOf(source.charAt(4)));
    }

    public static String getTwoEqualString(int sameNumber)
    {
        return String.valueOf(sameNumber) + String.valueOf(sameNumber);
    }

    public static int[] getElevenFiveResultNumber(String source)
    {
        if (source == null || source.length() < 14)
        {
            return new int[5];
        }
        int[] result = new int[5];

        result[0] = Integer.valueOf(source.substring(0, 2));
        result[1] = Integer.valueOf(source.substring(3, 5));
        result[2] = Integer.valueOf(source.substring(6, 8));
        result[3] = Integer.valueOf(source.substring(9, 11));
        result[4] = Integer.valueOf(source.substring(12, 14));

        return result;
    }

    public static int getIssuefromString(String source)
    {
        if (source.length() < 2)
        {
            return 0;
        }
        return Integer.valueOf(source.substring(source.length()-2, source.length()));
    }

    public static String getPickAnyResultString(ArrayList<Integer> selectedNumbers)
    {
        if (selectedNumbers == null || selectedNumbers.size() == 0)
        {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (Integer selectedNumber : selectedNumbers) {
            builder.append(getResultNumberString(selectedNumber + 1));
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public static String getFrontTwoResultString(ArrayList<Integer> selectedNumbers)
    {
        if (selectedNumbers == null || selectedNumbers.size() < 2)
        {
            return "";
        }

        return (getResultNumberString(selectedNumbers.get(0) + 1) + " | " + getResultNumberString(selectedNumbers.get(1) - 10)).trim();
    }

    public static String getFrontThreeResultString(ArrayList<Integer> selectedNumbers)
    {
        if (selectedNumbers == null || selectedNumbers.size() < 3)
        {
            return "";
        }

        return (getResultNumberString(selectedNumbers.get(0) + 1) + " | "
                + getResultNumberString(selectedNumbers.get(1) - 10) + " | " +
        getResultNumberString(selectedNumbers.get(2) - 21)).trim();
    }
}
