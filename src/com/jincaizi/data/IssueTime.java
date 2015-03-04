package com.jincaizi.data;

/**
 * Created by chenweida on 2015/2/27.
 */
public class IssueTime {

    private String msg;
    private String Diff;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getLeftSecond() {
        return Diff;
    }

    public void setLeftSecond(String leftSecond) {
        this.Diff = leftSecond;
    }

    public String getIssue() {
        return msg;
    }

    public void setIssue(String issue) {
        this.msg = issue;
    }

    private int result;

    public IssueTime(String issue, String leftSecond, int result)
    {
        this.msg = issue;
        this.Diff = leftSecond;
        this.result = result;
    }
}
