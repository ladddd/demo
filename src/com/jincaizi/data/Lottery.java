package com.jincaizi.data;

import java.util.List;

/**
 * Created by chenweida on 2015/2/12.
 */
public class Lottery
{

    private int result;
    private int size;
    private List<LotteryDetail> detail;

    public Lottery(int result, int size, List<LotteryDetail> lotteryDetailList)
    {
        this.result = result;
        this.size = size;
        this.detail = lotteryDetailList;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<LotteryDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<LotteryDetail> detail) {
        this.detail = detail;
    }

}
