package com.jincaizi.data;

/**
 * Created by chenweida on 2015/2/12.
 */
public class LotteryDetail
{
    public LotteryDetail(String LotteryType, String LotteryDes, String Qihao,
                         String Haoma, String JiangChi, String DayTime)
    {
        this.LotteryType = LotteryType;
        this.LotteryDes = LotteryDes;
        this.Qihao = Qihao;
        this.Haoma = Haoma;
        this.JiangChi = JiangChi;
        this.DayTime = DayTime;
    }

    public String getLotteryType() {
        return LotteryType;
    }

    public void setLotteryType(String lotteryType) {
        LotteryType = lotteryType;
    }

    public String getLotteryDes() {
        return LotteryDes;
    }

    public void setLotteryDes(String lotteryDes) {
        LotteryDes = lotteryDes;
    }

    public String getQihao() {
        return Qihao;
    }

    public void setQihao(String qihao) {
        Qihao = qihao;
    }

    public String getHaoma() {
        return Haoma;
    }

    public void setHaoma(String haoma) {
        Haoma = haoma;
    }

    public String getJiangChi() {
        return JiangChi;
    }

    public void setJiangChi(String jiangChi) {
        JiangChi = jiangChi;
    }

    public String getDayTime() {
        return DayTime;
    }

    public void setDayTime(String dayTime) {
        DayTime = dayTime;
    }

    private String LotteryType;
    private String LotteryDes;
    private String Qihao;
    private String Haoma;
    private String JiangChi;
    private String DayTime;
}
