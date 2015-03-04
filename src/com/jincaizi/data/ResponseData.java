package com.jincaizi.data;

import android.util.Pair;
import com.google.gson.Gson;
import com.jincaizi.common.NumberUtil;
import com.jincaizi.common.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweida on 2015/2/6.
 */
public class ResponseData {

    private int totalIssueCount = 0;
    private int[] totalResultList;

    private int[] missCount;
    private int[] targetCount;//当期出现的次数
    private int[] resultCount;//出现的期数
    private int[] missSum;
    private int[] maxMiss;
    private List<Result> baseResultList;

    private int[] totalSum;

    private int[] sumMissCount;
    private int[] sumResultCount;
    private int[] sumMissSum;
    private int[] sumMaxMiss;
    private List<SumResult> sumResultList;

    private int[] formMissCount;
    private int[] formResultCount;
    private int[] formMissSum;
    private int[] formMaxMiss;
    private List<FormResult> formResultList;

    private int[] twoEqualMissCount;
    private int[] twoEqualResultCount;
    private int[] twoEqualMissSum;
    private int[] twoEqualMaxMiss;
    private List<TwoEqualResult> twoEqualResultList;

    private int[] twoNotEqualMissCount;
    private int[] twoNotEqualResultCount;
    private int[] twoNotEqualMissSum;
    private int[] twoNotEqualMaxMiss;
    private List<TwoNotEqualResult> twoNotEqualResultList;

    public ResponseData(String jsonSource)
    {
        missCount = new int[6];
        targetCount = new int[6];
        resultCount = new int[6];
        missSum = new int[6];
        maxMiss = new int[6];
        baseResultList = new ArrayList<Result>();

        totalSum = new int[100];
        totalResultList = new int[100];

        sumMissCount = new int[16];
        sumResultCount = new int[16];
        sumMissSum = new int[16];
        sumMaxMiss = new int[16];
        sumResultList = new ArrayList<SumResult>();

        formMissCount = new int[4];
        formResultCount = new int[4];
        formMissSum = new int[4];
        formMaxMiss = new int[4];
        formResultList = new ArrayList<FormResult>();

        twoEqualMissCount = new int[6];
        twoEqualResultCount = new int[6];
        twoEqualMissSum = new int[6];
        twoEqualMaxMiss = new int[6];
        twoEqualResultList = new ArrayList<TwoEqualResult>();

        twoNotEqualMissCount = new int[16];
        twoNotEqualResultCount = new int[16];
        twoNotEqualMissSum = new int[16];
        twoNotEqualMaxMiss = new int[16];
        twoNotEqualResultList = new ArrayList<TwoNotEqualResult>();

        if (jsonSource != null && jsonSource.length() > 0) {
            Gson gson = new Gson();
            Lottery lottery = gson.fromJson(jsonSource, Lottery.class);

            totalIssueCount = lottery.getSize();
            List<LotteryDetail> list = lottery.getDetail();
            if (list != null && list.size() == totalIssueCount && totalIssueCount > 0) {

                for (int i = 0; i < totalIssueCount; i++) {
                    int issue = StringUtil.getIssue(list.get(i).getQihao());
                    int winningNumberSum = StringUtil.getWinningNumberSum(list.get(i).getHaoma());
                    int resultNumber = StringUtil.getResultNumber(list.get(i).getHaoma());
                    if (issue > 0 && totalSum[issue - 1] == 0) {
                        totalSum[issue - 1] = winningNumberSum;
                        totalResultList[issue - 1] = resultNumber;
                    }
                }

                initBaseData();
                initSumData();
                initFormData();
            }
        }
    }
    private void initBaseData()
    {
        for (int i = 0; i < totalIssueCount; i++) {

            int first = totalResultList[i]/100;
            int second = (totalResultList[i]/10)%10;
            int third = totalResultList[i]%10;

            targetCount[first-1]++;
            targetCount[second-1]++;
            targetCount[third-1]++;

            for (int j = 0; j < 6; j++) {
                missCount[j]++;
            }

            missCount[first-1] = 0;
            missCount[second-1] = 0;
            missCount[third-1] = 0;

            for (int j = 0; j < 6; j++) {
                missSum[j] += missCount[j];
                maxMiss[j] = Math.max(maxMiss[j], missCount[j]);
                resultCount[j] += targetCount[j]>0?1:0;
            }

            baseResultList.add(i, new Result(missCount, targetCount));

            targetCount[first-1] = 0;
            targetCount[second-1] = 0;
            targetCount[third-1] = 0;
        }
    }

    private void initSumData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            int luckyNumber = totalSum[i];
            sumResultCount[totalSum[i] - 3]++;
            sumResultList.add(i, new SumResult(StringUtil.getIssue(i + 1), luckyNumber));
        }
    }

    private void initFormData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            boolean result[] = new boolean[4];

            int first = totalResultList[i]/100;
            int second = (totalResultList[i]/10)%10;
            int third = totalResultList[i]%10;

            if (first == second && second == third)
            {
                //3equal
                result[0] = true;
            }
            else
            {
                //2not
                result[3] = true;
            }
            if (first != second && first != third && second != third)
            {
                //3not
                result[1] = true;
            }
            else
            {
                //2equal
                result[2] = true;
            }

            for (int j = 0; j < 4; j++) {
                if (result[j])
                {
                    formMissCount[j] = 0;
                    formResultCount[j]++;
                }
                else
                {
                    formMissCount[j]++;
                    formMaxMiss[j] = Math.max(formMaxMiss[j], formMissCount[j]);
                    formMissSum[j] += formMissCount[j];
                }
            }

            formResultList.add(i, new FormResult(formMissCount));

            // two equal
            initTwoEqual(i, getSameNumber(first, second, third));
            // two not equal
            initTwoNotEqual(i, first, second, third);
        }
    }

    private void initTwoNotEqual(int issue, int first, int second, int third)
    {
        for (int i = 0; i < 16; i++) {
            if (containDiffNumber(NumberUtil.getHeaderNumber(i), first, second, third))
            {
                twoNotEqualMissCount[i] = 0;
                twoNotEqualResultCount[i]++;
            }
            else
            {
                twoNotEqualMissCount[i]++;
                twoNotEqualMaxMiss[i] = Math.max(twoNotEqualMaxMiss[i], twoNotEqualMissCount[i]);
                twoNotEqualMissSum[i] += twoNotEqualMissCount[i];
            }
        }

        twoNotEqualResultList.add(issue, new TwoNotEqualResult(twoNotEqualMissCount));
    }

    private void initTwoEqual(int issue, int sameNumber)
    {
        for (int j = 0; j < 6; j++) {
            if (j == sameNumber - 1) {
                twoEqualMissCount[j] = 0;
                twoEqualResultCount[j]++;
            } else {
                twoEqualMissCount[j]++;
                twoEqualMaxMiss[j] = Math.max(twoEqualMaxMiss[j], twoEqualMissCount[j]);
                twoEqualMissSum[j] += twoEqualMissCount[j];
            }

        }

        twoEqualResultList.add(issue, new TwoEqualResult(twoEqualMissCount));
    }


    private boolean containDiffNumber(int headerNumber, int first, int second, int third)
    {
        if (headerNumber < 12 || headerNumber > 57)
        {
            return false;
        }
        int ten = headerNumber/10;
        int figure = headerNumber%10;
        if (ten == figure)
        {
            return false;
        }
        if ((ten == first || ten == second || ten == third) &&
                (figure == first || figure == second || figure == third))
        {
            return true;
        }
        return false;
    }

    private int getSameNumber(int first, int second, int third)
    {
        if (first == second || first == third)
        {
            return first;
        }
        else if (second == third)
        {
            return second;
        }
        return -1;
    }

    public int[] getMissCount() {
        return missCount;
    }

    public int[] getResultCount() {
        return resultCount;
    }

    public int[] getMissSum() {
        return missSum;
    }

    public int[] getMaxMiss() {
        return maxMiss;
    }

    public int getBaseResultMiss(int row, int column)
    {
        return baseResultList.get(row).result.get(column).first;
    }

    public int getBaseResultCount(int row, int column)
    {
        return baseResultList.get(row).result.get(column).second;
    }

    public String getSumResult(int row, int column)
    {
        return sumResultList.get(row).resultAtIndex(column);
    }

    public int getFormResult(int row, int column) {return formResultList.get(row).getResultAtIndex(column); }

    public int getTwoEqualResult(int row, int column)
    {
        return twoEqualResultList.get(row).getResultAtIndex(column);
    }

    public int getTwoNotEqualResult(int row, int column)
    {
        return twoNotEqualResultList.get(row).getResultAtIndex(column);
    }

    public int getTotalIssueCount() {
        return totalIssueCount;
    }

    public int[] getTotalResultList() {
        return totalResultList;
    }

    public int[] getTotalSum() {
        return totalSum;
    }

    public int[] getSumMissCount() {
        return sumMissCount;
    }

    public int[] getSumResultCount() {
        return sumResultCount;
    }

    public int[] getSumMissSum() {
        return sumMissSum;
    }

    public int[] getSumMaxMiss() {
        return sumMaxMiss;
    }

    public int[] getFormMissCount() {
        return formMissCount;
    }

    public int[] getFormResultCount() {
        return formResultCount;
    }

    public int[] getFormMissSum() {
        return formMissSum;
    }

    public int[] getFormMaxMiss() {
        return formMaxMiss;
    }

    public int[] getTwoEqualMissCount() {
        return twoEqualMissCount;
    }

    public int[] getTwoEqualResultCount() {
        return twoEqualResultCount;
    }

    public int[] getTwoEqualMissSum() {
        return twoEqualMissSum;
    }

    public int[] getTwoEqualMaxMiss() {
        return twoEqualMaxMiss;
    }

    public int[] getTwoNotEqualMissCount() {
        return twoNotEqualMissCount;
    }

    public int[] getTwoNotEqualResultCount() {
        return twoNotEqualResultCount;
    }

    public int[] getTwoNotEqualMissSum() {
        return twoNotEqualMissSum;
    }

    public int[] getTwoNotEqualMaxMiss() {
        return twoNotEqualMaxMiss;
    }

    private static class Result
    {

        public List<Pair<Integer, Integer>> result;
        public Result(int[] missCount, int[] targetCount)
        {
            result = new ArrayList<Pair<Integer, Integer>>();

            for (int i = 0; i < 6; i++) {
                result.add(i, new Pair<Integer, Integer>(missCount[i], targetCount[i]));
            }
        }

    }

    private class SumResult
    {

        private final String[] result;

        public SumResult(String issue, int result)
        {
            for (int i = 0; i < 16; i++) {
                if (i + 3 == result)
                {
                    sumMissCount[i] = 0;
                }
                else
                {
                    sumMissCount[i]++;
                }
                sumMaxMiss[i] = Math.max(sumMaxMiss[i], sumMissCount[i]);
                sumMissSum[i] += sumMissCount[i];
            }

            this.result = new String[]{
                    issue, String.valueOf(sumMissCount[0]), String.valueOf(sumMissCount[1]), String.valueOf(sumMissCount[2]),
                    String.valueOf(sumMissCount[3]), String.valueOf(sumMissCount[4]), String.valueOf(sumMissCount[5])
                    , String.valueOf(sumMissCount[6]), String.valueOf(sumMissCount[7]), String.valueOf(sumMissCount[8])
                    , String.valueOf(sumMissCount[9]), String.valueOf(sumMissCount[10]), String.valueOf(sumMissCount[11])
                    , String.valueOf(sumMissCount[12]), String.valueOf(sumMissCount[13]), String.valueOf(sumMissCount[14])
                    , String.valueOf(sumMissCount[15]),
            };
        }

        public String resultAtIndex(int index)
        {
            return result[index];
        }
    }

    private class FormResult
    {
        private int[] result;

        public FormResult(int[] result)
        {
            this.result = result.clone();
        }

        public int getResultAtIndex(int index)
        {
            return result[index];
        }
    }

    private class TwoEqualResult
    {
        private int[] result;

        public TwoEqualResult(int[] result)
        {
            this.result = result.clone();
        }

        public int getResultAtIndex(int index)
        {
            return result[index];
        }
    }

    private class TwoNotEqualResult
    {
        private int[] result;

        public TwoNotEqualResult(int[] result)
        {
            this.result = result.clone();
        }

        public int getResultAtIndex(int index)
        {
            return result[index];
        }
    }
}
