package com.jincaizi.kuaiwin.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jincaizi.R;

public class WanFaIntroActivity extends Activity implements OnClickListener{

	private ListView mListView;
	private ImageView mBackBtn;
	private String[] nameItems;
	private String[] urlItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wanfa_activity);
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mBackBtn.setOnClickListener(this);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("玩法说明");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mListView = (ListView)findViewById(R.id.wanfa_list);
		nameItems = this.getResources().getStringArray(R.array.help_wanfa_name_items);
		urlItems = this.getResources().getStringArray(R.array.help_wanfa_url_items);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,nameItems);  
		mListView.setAdapter(arrayAdapter);  
		mListView.setOnItemClickListener(new myItemClickListener());

	}
    class myItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(WanFaIntroActivity.this, WanfaDetailActivity.class);
			intent.putExtra("WanfaUrl", urlItems[arg2]);
			intent.putExtra("lotteryName",nameItems[arg2]);
			startActivity(intent);
		}
    	
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
