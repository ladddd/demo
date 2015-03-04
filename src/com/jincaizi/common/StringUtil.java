package com.jincaizi.common;

/**
 * Created by chenweida on 2015/2/3.
 */
public class StringUtil {
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
}
