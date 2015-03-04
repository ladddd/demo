package com.jincaizi.kuaiwin.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.R;

public class WanfaDetailActivity extends Activity implements OnClickListener{

	private ImageView mBackBtn;
	private String url;
	private String lotteryName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wanfa_detail_layout);
		_initData(getIntent());
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mBackBtn.setOnClickListener(this);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText(lotteryName);
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		WebView webView = (WebView)findViewById(R.id.wanfa_webview);
		webView.loadUrl(url);
	}
	private void _initData(Intent intent) {
		url = intent.getStringExtra("WanfaUrl");
		lotteryName = intent.getStringExtra("lotteryName");
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
			default:
				break;
		}
		
	}
}
