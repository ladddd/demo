package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.bean.AccountTkRecordEntity;
import com.jincaizi.R;

/**
 * 账户明细适配器
 *
 * @author yj
 */
public class MyAccountTkAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<AccountTkRecordEntity> mRecordList;

    public MyAccountTkAdapter(Context context,LinkedList<AccountTkRecordEntity>list) {
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
            holder.tv_typeTitle = (TextView)convertView.findViewById(R.id.trans_type_title);
            holder.tv_transjine = (TextView)convertView.findViewById(R.id.trans_jine_content);
            holder.tv_transleft = (TextView)convertView.findViewById(R.id.trans_left_content);
            holder.tv_leftTitle = (TextView)convertView.findViewById(R.id.trans_left_title);
            holder.tv_bank = (TextView)convertView.findViewById(R.id.bank_content);
            holder.tv_bank.setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.bank_title).setVisibility(View.VISIBLE);
            holder.tv_bankcard = (TextView)convertView.findViewById(R.id.bankcard_content);
            holder.tv_bankcard.setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.bankcard_title).setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_transtime.setText(mRecordList.get(position).getTkTime());
        holder.tv_transtype.setText(mRecordList.get(position).getTkStatus());
        	holder.tv_transjine.setText(mRecordList.get(position).getFee());
        	holder.tv_transjine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_account_outgoing, 0, 0, 0);
        holder.tv_transleft.setText(mRecordList.get(position).getTkJine());
        holder.tv_leftTitle.setText("提款金额：");
        holder.tv_typeTitle.setText("提款状态：");
        holder.tv_bank.setText(mRecordList.get(position).getBank());
        holder.tv_bankcard.setText(mRecordList.get(position).getBankCard());
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_transtime;
        TextView tv_transtype;
        TextView tv_typeTitle;
        TextView tv_transjine;
        TextView tv_transleft;
        TextView tv_leftTitle;
        TextView tv_bank;
        TextView tv_bankcard;
    }
  
}
