package com.jincaizi.kuaiwin.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jincaizi.R;
/**
 * 帮助中心
 * @author zhao
 *
 */
public class HelpCenterActivity extends Activity implements OnClickListener{

	private RelativeLayout mBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wanfa_detail_layout);
		mBackBtn = (RelativeLayout) findViewById(R.id.left_layout);
		mBackBtn.setOnClickListener(this);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("帮助中心");
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		WebView webView = (WebView)findViewById(R.id.wanfa_webview);
		webView.loadUrl("file:///android_asset/help.html");
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.left_layout:
			finish();
			break;
			default:
				break;
		}
		
	}
}
