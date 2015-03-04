package com.jincaizi.kuaiwin.chart.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.data.IssueData;

/**
 * Created by chenweida on 2015/2/25.
 */
public class BaseTableActivity extends Activity {

    private TextView issuePart;
    private TextView timePart;

    private IssueBroadcastReceiver issueBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout back = (RelativeLayout)findViewById(R.id.finish_btn);
        issuePart = (TextView) findViewById(R.id.text_first);
        timePart = (TextView) findViewById(R.id.text_second);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFinished();
                finish();
            }
        });

        issueBroadcastReceiver = new IssueBroadcastReceiver();
        registerReceiver(issueBroadcastReceiver, new IntentFilter("qihao"));
    }

    private class IssueBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra("success",false))
            {
                issuePart.setVisibility(View.GONE);
                timePart.setVisibility(View.GONE);
                return;
            }

            final String data = intent.getStringExtra("JSON");

            IssueData responseData = new IssueData(data);

            int leftSecond = responseData.getLeftSecond();
            issuePart.setText("据" + responseData.getIssue() + "期截止:");

            issuePart.setVisibility(View.VISIBLE);
            timePart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(issueBroadcastReceiver);

        super.onDestroy();
    }

    public void onFinished()
    {

    }
}
