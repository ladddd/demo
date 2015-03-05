package com.jincaizi.data;

import com.google.gson.Gson;
import com.jincaizi.common.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweida on 2015/2/12.
 */
public class ElevenFiveData {

    private int totalIssueCount = 0;
    private int[][] totalResult;

    private int[] firstMissCount;
    private int[] firstMissSum;
    private int[] firstMaxMiss;
    private int[] firstResultCount;
    private int[] firstResult;
    private List<Result> firstResultList;

    private int[] secondMissCount;
    private int[] secondMissSum;
    private int[] secondMaxMiss;
    private int[] secondResultCount;
    private int[] secondResult;
    private List<Result> secondResultList;

    private int[] thirdMissCount;
    private int[] thirdMissSum;
    private int[] thirdMaxMiss;
    private int[] thirdResultCount;
    private int[] thirdResult;

    private List<Result> thirdResultList;

    //全5
    private int[] totalMissCount;
    private int[] totalMissSum;
    private int[] totalMaxMiss;
    private int[] totalResultCount;
    private List<Result> totalResultList;
    private List<CountResult> totalCountList;

    //前2
    private int[] frontTwoMissCount;
    private int[] frontTwoMissSum;
    private int[] frontTwoMaxMiss;
    private int[] frontTwoResultCount;
    private List<Result> frontTwoResultList;
    private List<CountResult> frontTwoCountList;

    //前3
    private int[] frontThreeMissCount;

    private int[] frontThreeMissSum;
    private int[] frontThreeMaxMiss;
    private int[] frontThreeResultCount;
    private List<Result> frontThreeResultList;
    private List<CountResult> frontThreeCountList;
    private int[] formMissCount;

    private int[] formMissSum;
    private int[] formMaxMiss;
    private int[] formResultCount;
    private List<Result> formResultList;
    public ElevenFiveData(String jsonSource)
    {
        totalResult = new int[100][5];

        firstMissCount = new int[11];
        firstMissSum = new int[11];
        firstMaxMiss = new int[11];
        firstResultCount = new int[11];
        firstResult = new int[100];

        firstResultList = new ArrayList<Result>();

        secondMissCount = new int[11];
        secondMissSum = new int[11];
        secondMaxMiss = new int[11];
        secondResultCount = new int[11];
        secondResult = new int[100];
        secondResultList = new ArrayList<Result>();

        thirdMissCount = new int[11];
        thirdMissSum = new int[11];
        thirdMaxMiss = new int[11];
        thirdResultCount = new int[11];
        thirdResult = new int[100];
        thirdResultList = new ArrayList<Result>();

        totalMissCount = new int[11];
        totalMissSum = new int[11];
        totalMaxMiss = new int[11];
        totalResultCount = new int[11];
        totalResultList = new ArrayList<Result>();
        totalCountList = new ArrayList<CountResult>();

        frontTwoMissCount = new int[11];
        frontTwoMissSum = new int[11];
        frontTwoMaxMiss = new int[11];
        frontTwoResultCount = new int[11];
        frontTwoResultList = new ArrayList<Result>();
        frontTwoCountList = new ArrayList<CountResult>();

        frontThreeMissCount = new int[11];
        frontThreeMissSum = new int[11];
        frontThreeMaxMiss = new int[11];
        frontThreeResultCount = new int[11];
        frontThreeResultList = new ArrayList<Result>();
        frontThreeCountList = new ArrayList<CountResult>();

        formMissCount = new int[7];
        formMissSum = new int[7];
        formMaxMiss = new int[7];
        formResultCount = new int[7];
        formResultList = new ArrayList<Result>();

        if (jsonSource != null && jsonSource.length() > 0) {
            Gson gson = new Gson();
            Lottery lottery = gson.fromJson(jsonSource, Lottery.class);

            totalIssueCount = lottery.getSize();
            List<LotteryDetail> list = lottery.getDetail();
            if (list != null && list.size() == totalIssueCount && totalIssueCount > 0) {

                for (int i = 0; i < totalIssueCount; i++) {
                    int issue = StringUtil.getIssue(list.get(i).getQihao());
                    int[] resultNumber = StringUtil.getElevenFiveResultNumber(list.get(i).getHaoma());
                    if (issue > 0) {
                        totalResult[issue - 1] = resultNumber;
                    }
                }

                initTotalData();
                initFrontTwoData();
                initFrontThreeData();
                initFirstData();
                initFormData();
            }
        }
    }
    private void initFrontTwoData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            int sum = 0;
            int max = totalResult[i][0];
            int min = totalResult[i][0];
            int bigCount = 0;
            int smallCount = 0;
            int oddCount = 0;
            int evenCount = 0;
            int primeCount = 0;
            int compositeCount = 0;
            for (int j = 0; j < 11; j++) {
                boolean isContain = false;
                for (int k = 0; k < 2; k++) {
                    if (totalResult[i][k] == j+1)
                    {
                        sum += j+1;
                        max = Math.max(max, j+1);
                        min = Math.min(min, j+1);
                        if (j+1 > 5)
                        {
                            bigCount++;
                        }
                        else
                        {
                            smallCount++;
                        }
                        if (isOdd(j+1))
                        {
                            oddCount++;
                        }
                        else
                        {
                            evenCount++;
                        }
                        if (isComposite(j+1))
                        {
                            compositeCount++;
                        }
                        else
                        {
                            primeCount++;
                        }

                        isContain = true;
                        break;
                    }
                }
                if (isContain)
                {
                    frontTwoMissCount[j] = 0;
                    frontTwoResultCount[j]++;
                }
                else
                {
                    frontTwoMissCount[j]++;
                    frontTwoMaxMiss[j] = Math.max(frontTwoMaxMiss[j], frontTwoMissCount[j]);
                    frontTwoMissSum[j] += frontTwoMissCount[j];
                }
            }

            frontTwoCountList.add(i, new CountResult(sum, max, min, bigCount, smallCount, oddCount,
                    evenCount, primeCount, compositeCount));
            frontTwoResultList.add(i, new Result(frontTwoMissCount));
        }
    }

    private void initFrontThreeData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            int sum = 0;
            int max = totalResult[i][0];
            int min = totalResult[i][0];
            int bigCount = 0;
            int smallCount = 0;
            int oddCount = 0;
            int evenCount = 0;
            int primeCount = 0;
            int compositeCount = 0;
            for (int j = 0; j < 11; j++) {
                boolean isContain = false;
                for (int k = 0; k < 3; k++) {
                    if (totalResult[i][k] == j+1)
                    {
                        sum += j+1;
                        max = Math.max(max, j+1);
                        min = Math.min(min, j+1);
                        if (j+1 > 5)
                        {
                            bigCount++;
                        }
                        else
                        {
                            smallCount++;
                        }
                        if (isOdd(j+1))
                        {
                            oddCount++;
                        }
                        else
                        {
                            evenCount++;
                        }
                        if (isComposite(j+1))
                        {
                            compositeCount++;
                        }
                        else
                        {
                            primeCount++;
                        }

                        isContain = true;
                        break;
                    }
                }
                if (isContain)
                {
                    frontThreeMissCount[j] = 0;
                    frontThreeResultCount[j]++;
                }
                else
                {
                    frontThreeMissCount[j]++;
                    frontThreeMaxMiss[j] = Math.max(frontThreeMaxMiss[j], frontThreeMissCount[j]);
                    frontThreeMissSum[j] += frontThreeMissCount[j];
                }
            }

            frontThreeCountList.add(i, new CountResult(sum, max, min, bigCount, smallCount, oddCount,
                    evenCount, primeCount, compositeCount));
            frontThreeResultList.add(i, new Result(frontThreeMissCount));
        }
    }

    private void initTotalData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            int sum = 0;
            int max = totalResult[i][0];
            int min = totalResult[i][0];
            int bigCount = 0;
            int smallCount = 0;
            int oddCount = 0;
            int evenCount = 0;
            int primeCount = 0;
            int compositeCount = 0;
            for (int j = 0; j < 11; j++) {
                boolean isContain = false;
                for (int k = 0; k < 5; k++) {
                    if (totalResult[i][k] == j+1)
                    {
                        sum += j+1;
                        max = Math.max(max, j+1);
                        min = Math.min(min, j+1);
                        if (j+1 > 5)
                        {
                            bigCount++;
                        }
                        else
                        {
                            smallCount++;
                        }
                        if (isOdd(j+1))
                        {
                            oddCount++;
                        }
                        else
                        {
                            evenCount++;
                        }
                        if (isComposite(j+1))
                        {
                            compositeCount++;
                        }
                        else
                        {
                            primeCount++;
                        }

                        isContain = true;
                        break;
                    }
                }
                if (isContain)
                {
                    totalMissCount[j] = 0;
                    totalResultCount[j]++;
                }
                else
                {
                    totalMissCount[j]++;
                    totalMaxMiss[j] = Math.max(totalMaxMiss[j], totalMissCount[j]);
                    totalMissSum[j] += totalMissCount[j];
                }
            }

            totalCountList.add(i, new CountResult(sum, max, min, bigCount, smallCount, oddCount,
                    evenCount, primeCount, compositeCount));
            totalResultList.add(i, new Result(totalMissCount));
        }
    }

    private void initFirstData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            for (int j = 0; j < 11; j++) {
                if (totalResult[i][0] == j+1)
                {
                    firstMissCount[j] = 0;
                    firstResultCount[j]++;
                    firstResult[i] = j+1;
                }
                else
                {
                    firstMissCount[j]++;
                    firstMaxMiss[j] = Math.max(firstMaxMiss[j], firstMissCount[j]);
                    firstMissSum[j] += firstMissCount[j];
                }
                if (totalResult[i][1] == j+1)
                {
                    secondMissCount[j] = 0;
                    secondResultCount[j]++;
                    secondResult[i] = j+1;
                }
                else
                {
                    secondMissCount[j]++;
                    secondMaxMiss[j] = Math.max(secondMaxMiss[j], secondMissCount[j]);
                    secondMissSum[j] += secondMissCount[j];
                }
                if (totalResult[i][2] == j+1)
                {
                    thirdMissCount[j] = 0;
                    thirdResultCount[j]++;
                    thirdResult[i] = j+1;
                }
                else
                {
                    thirdMissCount[j]++;
                    thirdMaxMiss[j] = Math.max(thirdMaxMiss[j], thirdMissCount[j]);
                    thirdMissSum[j] += thirdMissCount[j];
                }
            }

            firstResultList.add(i, new Result(firstMissCount));
            secondResultList.add(i, new Result(secondMissCount));
            thirdResultList.add(i, new Result(thirdMissCount));
        }
    }

    private void initFormData()
    {
        for (int i = 0; i < totalIssueCount; i++) {
            int first = totalResult[i][0];

            if (isOdd(first))
            {
                formMissCount[0] = 0;
                formMissCount[1]++;
                formResultCount[0]++;
                formMissSum[1] += formMissCount[1];
                formMaxMiss[1] = Math.max(formMaxMiss[1], formMissCount[1]);
            }
            else
            {
                formMissCount[0]++;
                formMissCount[1] = 0;
                formResultCount[1]++;
                formMissSum[0] += formMissCount[0];
                formMaxMiss[0] = Math.max(formMaxMiss[0], formMissCount[0]);
            }

            if (isComposite(first))
            {
                formMissCount[3] = 0;
                formMissCount[2]++;
                formResultCount[3]++;
                formMissSum[2] += formMissCount[2];
                formMaxMiss[2] = Math.max(formMaxMiss[2], formMissCount[2]);
            }
            else
            {
                formMissCount[2] = 0;
                formMissCount[3]++;
                formResultCount[2]++;
                formMissSum[3] += formMissCount[3];
                formMaxMiss[3] = Math.max(formMaxMiss[3], formMissCount[3]);
            }

            if (first % 3 == 0)
            {
                formMissCount[4] = 0;
                formMissCount[5]++;
                formMissCount[6]++;
                formResultCount[4]++;
                formMissSum[5] += formMissCount[5];
                formMaxMiss[5] = Math.max(formMaxMiss[5], formMissCount[5]);
                formMissSum[6] += formMissCount[6];
                formMaxMiss[6] = Math.max(formMaxMiss[6], formMissCount[6]);
            }
            else if (first % 3 == 1)
            {
                formMissCount[5] = 0;
                formMissCount[4]++;
                formMissCount[6]++;
                formResultCount[5]++;
                formMissSum[4] += formMissCount[4];
                formMaxMiss[4] = Math.max(formMaxMiss[4], formMissCount[4]);
                formMissSum[6] += formMissCount[6];
                formMaxMiss[6] = Math.max(formMaxMiss[6], formMissCount[6]);
            }
            else
            {
                formMissCount[6] = 0;
                formMissCount[4]++;
                formMissCount[5]++;
                formResultCount[6]++;
                formMissSum[4] += formMissCount[4];
                formMaxMiss[4] = Math.max(formMaxMiss[4], formMissCount[4]);
                formMissSum[5] += formMissCount[5];
                formMaxMiss[5] = Math.max(formMaxMiss[5], formMissCount[5]);
            }

            formResultList.add(i, new Result(formMissCount));
        }
    }

    private boolean isOdd(int number)
    {
        return number%2 == 1;
    }

    private boolean isComposite(int number)
    {
        if (number == 1 || number == 2)
        {
            return false;
        }
        for (int i = 2; i < number/2 + 1; i++) {
            if (number%i == 0)
            {
                return true;
            }
        }
        return false;
    }

    public int[] getTotalMissCount() {
        return totalMissCount;
    }

    public int[] getTotalMissSum() {
        return totalMissSum;
    }

    public int[] getTotalMaxMiss() {
        return totalMaxMiss;
    }

    public int[] getTotalResultCount() {
        return totalResultCount;
    }

    public int[] getFormResultCount() {
        return formResultCount;
    }

    public int[] getFormMaxMiss() {
        return formMaxMiss;
    }

    public int[] getFormMissSum() {
        return formMissSum;
    }

    public int[] getFormMissCount() {
        return formMissCount;
    }

    public int[] getFrontThreeMissCount() {
        return frontThreeMissCount;
    }

    public int[] getFrontThreeMissSum() {
        return frontThreeMissSum;
    }

    public int[] getFrontThreeMaxMiss() {
        return frontThreeMaxMiss;
    }

    public int[] getFrontThreeResultCount() {
        return frontThreeResultCount;
    }

    public int[] getFrontTwoResultCount() {
        return frontTwoResultCount;
    }

    public int[] getFrontTwoMaxMiss() {
        return frontTwoMaxMiss;
    }

    public int[] getFrontTwoMissSum() {
        return frontTwoMissSum;
    }

    public int[] getFrontTwoMissCount() {
        return frontTwoMissCount;
    }

    public int[] getThirdResultArray() {
        return thirdResult;
    }

    public int[] getThirdMissSum() {
        return thirdMissSum;
    }

    public int[] getThirdMaxMiss() {
        return thirdMaxMiss;
    }

    public int[] getThirdResultCount() {
        return thirdResultCount;
    }

    public int[] getThirdMissCount() {
        return thirdMissCount;
    }

    public int[] getSecondResultArray() {
        return secondResult;
    }

    public int[] getFirstResultArray() {
        return firstResult;
    }

    public int getFirstResult(int row, int column)
    {
        return firstResultList.get(row).resultAtIndex(column);
    }

    public int getSecondResult(int row, int column)
    {
        return secondResultList.get(row).resultAtIndex(column);
    }

    public int getThirdResult(int row, int column)
    {
        return thirdResultList.get(row).resultAtIndex(column);
    }

    public int getFormResult(int row, int column)
    {
        return formResultList.get(row).resultAtIndex(column);
    }

    public int getTotalResult(int row, int column)
    {
        return totalResultList.get(row).resultAtIndex(column);
    }

    public String getTotalCount(int row, int column)
    {
        return totalCountList.get(row).resultAtIndex(column);
    }

    public int getFrontTwoResult(int row, int column)
    {
        return frontTwoResultList.get(row).resultAtIndex(column);
    }

    public String getFrontTwoCount(int row, int column) {
        return frontTwoCountList.get(row).resultAtIndex(column);
    }

    public int getFrontThreeResult(int row, int column)
    {
        return frontThreeResultList.get(row).resultAtIndex(column);
    }

    public String getFrontThreeCount(int row, int column) {
        return frontThreeCountList.get(row).resultAtIndex(column);
    }

    public int[][] getTotalResult() {
        return totalResult;
    }

    public int[] getFirstMissCount() {
        return firstMissCount;
    }

    public int[] getFirstMissSum() {
        return firstMissSum;
    }

    public int[] getFirstMaxMiss() {
        return firstMaxMiss;
    }

    public int[] getSecondMissCount() {
        return secondMissCount;
    }

    public int[] getSecondMissSum() {
        return secondMissSum;
    }

    public int[] getSecondMaxMiss() {
        return secondMaxMiss;
    }

    public int[] getSecondResultCount() {
        return secondResultCount;
    }

    public int[] getFirstResultCount() {
        return firstResultCount;
    }

    public int getTotalIssueCount() {
        return totalIssueCount;
    }

    private class Result
    {
        private int[] result;

        public Result(int[] result)
        {
            this.result = result.clone();
        }

        public int resultAtIndex(int index)
        {
            return result[index];
        }
    }

    private class CountResult
    {
        private String[] result;

        public CountResult(int sum, int max, int min, int bigCount, int smallCount,
                           int oddCount, int evenCount, int primeCount, int compositeCount)
        {
            result = new String[5];
            result[0] = String.valueOf(sum);
            result[1] = String.valueOf(max - min);
            result[2] = String.valueOf(bigCount) + ":" + String.valueOf(smallCount);
            result[3] = String.valueOf(oddCount) + ":" + String.valueOf(evenCount);
            result[4] = String.valueOf(primeCount) + ":" + String.valueOf(compositeCount);
        }

        public String resultAtIndex(int index)
        {
            return result[index];
        }
    }
}
