package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.bean.AccountCzRecordEntity;
import com.jincaizi.R;

/**
 * 充值明细适配器
 *
 * @author yj
 */
public class MyAccountCzAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<AccountCzRecordEntity> mRecordList;

    public MyAccountCzAdapter(Context context,LinkedList<AccountCzRecordEntity>list) {
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
            holder.tv_leftTitle = (TextView)convertView.findViewById(R.id.trans_left_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_transtime.setText(mRecordList.get(position).getCzTime());
        holder.tv_transtype.setText(mRecordList.get(position).getCzType());
        	holder.tv_transjine.setText(mRecordList.get(position).getCzJine());
        	holder.tv_transjine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_account_incoming, 0, 0, 0);
        holder.tv_transleft.setText(mRecordList.get(position).getCzStatus());
        holder.tv_leftTitle.setText("充值状态：");
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_transtime;
        TextView tv_transtype;
        TextView tv_transjine;
        TextView tv_transleft;
        TextView tv_leftTitle;
        
    }
  
}
