package com.jincaizi.data;

import com.google.gson.Gson;
import com.jincaizi.common.StringUtil;

/**
 * Created by chenweida on 2015/2/27.
 */
public class IssueData {

    private int issue;
    private int leftSecond;

    public int getIssue() {
        return issue;
    }

    public int getLeftSecond() {
        return leftSecond;
    }

    public int getResult() {
        return result;
    }

    private int result;

    public IssueData(String jsonSource)
    {
        if (jsonSource != null && jsonSource.length() > 0) {
            Gson gson = new Gson();

            IssueTime issueTime = gson.fromJson(jsonSource, IssueTime.class);

            this.result = issueTime.getResult();
            this.issue = StringUtil.getIssuefromString(issueTime.getIssue());
            if (issueTime.getLeftSecond().length() != 0)
            {
                this.leftSecond = Integer.valueOf(issueTime.getLeftSecond());
            }
            else
            {
                this.leftSecond = 540;
            }
        }
    }
}
