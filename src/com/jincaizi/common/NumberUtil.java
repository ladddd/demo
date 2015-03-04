package com.jincaizi.common;

/**
 * Created by chenweida on 2015/2/12.
 */
public class NumberUtil {

    public static int getHeaderNumber(int column)
    {
        switch (column)
        {
            case 0:return 12;
            case 1:return 13;
            case 2:return 14;
            case 3:return 15;
            case 4:return 16;
            case 5:return 23;
            case 6:return 24;
            case 7:return 25;
            case 8:return 26;
            case 9:return 34;
            case 10:return 35;
            case 11:return 36;
            case 12:return 45;
            case 13:return 46;
            case 14:return 56;
            default:return 0;
        }
    }
}
