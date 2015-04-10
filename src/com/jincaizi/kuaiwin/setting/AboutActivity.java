package com.jincaizi.kuaiwin.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jincaizi.R;
/**
 * 关于喜彩网
 * @author zhao
 *
 */
public class AboutActivity extends Activity implements OnClickListener{

	private RelativeLayout mBackBtn;
	private TextView tvCall;
	private Dialog myDialog;
	private TextView dialogCancel;
	private TextView dialogOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_kw);
		mBackBtn = (RelativeLayout) findViewById(R.id.left_layout);
		mBackBtn.setOnClickListener(this);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("关于");
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		tvCall = (TextView)findViewById(R.id.tv_tel);
		tvCall.setOnClickListener(this);
		
		//call dialog
		 myDialog = new Dialog(this, R.style.Theme_dialog);
			View dialogView = LayoutInflater.from(this).inflate(
					R.layout.dialog_submit_bet, null);
			TextView dialogTitle = (TextView) dialogView
					.findViewById(R.id.submit_dialog_title);
			dialogTitle.setText("提示");
			TextView dialogContent = (TextView) dialogView
					.findViewById(R.id.submit_dialog_content);
			dialogContent.setText("拨打4006-999-721客服电话");
			dialogCancel = (TextView) dialogView.findViewById(R.id.tv_submit_cancel);
			dialogCancel.setOnClickListener(this);
			dialogOK = (TextView) dialogView.findViewById(R.id.tv_submit_ok);
			dialogOK.setText("确定");
			myDialog.setContentView(dialogView);
			Window dialogWindow = myDialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = 300; // 宽度
			dialogWindow.setAttributes(lp);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tv_submit_cancel:
			myDialog.dismiss();
			break;
		case R.id.tv_tel:
			dialogOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					  Intent intent = new Intent();
					   intent.setAction("android.intent.action.CALL");
					   intent.setData(Uri.parse("tel:4006999721"));//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
					   startActivity(intent);
					myDialog.dismiss();
				}
			});
			myDialog.show();
			break;
		case R.id.left_layout:
			finish();
			break;
			default:
				break;
		}
		
	}
}
