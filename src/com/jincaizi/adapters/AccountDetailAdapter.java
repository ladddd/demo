package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.bean.AccountRecordEntity;

/**
 * 账户明细适配器
 *
 * @author yj
 */
public class AccountDetailAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<AccountRecordEntity> mRecordList;

    public AccountDetailAdapter(Context context,LinkedList<AccountRecordEntity>list) {
        this.mContext = context;
        this.mRecordList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	//Log.d("test", "position = " + position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.myaccount_list_item, null, false);
            holder.tv_transtime = (TextView)convertView.findViewById(R.id.trans_time_content);
            holder.tv_transtype = (TextView)convertView.findViewById(R.id.trans_type_content);
            holder.tv_transjine = (TextView)convertView.findViewById(R.id.trans_jine_content);
            holder.tv_transleft = (TextView)convertView.findViewById(R.id.trans_left_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_transjine.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        holder.tv_transtime.setText(mRecordList.get(position).getTransTime());
        holder.tv_transtype.setText(mRecordList.get(position).getTransType());
        SpannableString ss = new SpannableString("当前余额：" + mRecordList.get(position).getTransLeft()+ "元");	
		ss.setSpan(new ForegroundColorSpan(Color.RED), 5, ss.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_transjine.setText(ss);
       
        
        holder.tv_transleft.setVisibility(View.GONE);
        convertView.findViewById(R.id.trans_left_title).setVisibility(View.GONE);
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_transtime;
        TextView tv_transtype;
        TextView tv_transjine;
        TextView tv_transleft;
        
    }
  
}
