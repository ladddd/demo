package com.jincaizi.kuaiwin.mylottery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jincaizi.R;


public class IdAuthActivity extends Activity implements OnClickListener{

	private RelativeLayout mLeftBack;
	private LinearLayout mRealNameView;
	private LinearLayout mPhoneView;
	private LinearLayout mEmailView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.id_auth);
		_findViews();
		_setListener();
	}
    private void _findViews() {
    	mLeftBack = (RelativeLayout)findViewById(R.id.left_layout);
    	TextView titleView = (TextView)findViewById(R.id.current_lottery);
    	titleView.setText("身份认证");
    	titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    	mRealNameView = (LinearLayout)findViewById(R.id.realname_auth);
    	mPhoneView = (LinearLayout)findViewById(R.id.phone_auth);
    	mEmailView = (LinearLayout)findViewById(R.id.email_auth);
        findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
    }
    private void _setListener() {
    	mLeftBack.setOnClickListener(this);
    	mRealNameView.setOnClickListener(this);
    	mPhoneView.setOnClickListener(this);
    	mEmailView.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.left_layout:
			finish();
			break;
		case R.id.realname_auth:
			Intent rlIntent = new Intent(this, RealnameAuth.class);
			startActivity(rlIntent);
			break;
		case R.id.phone_auth:
			Intent phoneIntent = new Intent(this, PhoneAuth.class);
			startActivity(phoneIntent);
			break;
		case R.id.email_auth:
			Intent emailIntent = new Intent(this, EmailAuth.class);
			startActivity(emailIntent);
			break;
			default:
				break;
		}
	}
}
