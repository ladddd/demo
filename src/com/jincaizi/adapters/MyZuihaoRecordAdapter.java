package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.bean.ZuihaoRecordEntity;
import com.jincaizi.kuaiwin.BuyCenter;
import com.jincaizi.R;

/**
 * 投注详情适配器
 *
 * @author yj
 */
public class MyZuihaoRecordAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<ZuihaoRecordEntity> mRecordList;

    public MyZuihaoRecordAdapter(Context context,LinkedList<ZuihaoRecordEntity>list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_zuihao_listview_item, null, false);
            holder.lotteryName = (TextView)convertView.findViewById(R.id.lotteryName);
            holder.tv_qishu_amount = (TextView)convertView.findViewById(R.id.tv_qishu_amount);
            holder.tv_money = (TextView)convertView.findViewById(R.id.tv_money_content);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_lottery_time);
            holder.tv_zuihao_status = (TextView)convertView.findViewById(R.id.tv_zuihao_status);
            holder.tv_zuihao_current = (TextView)convertView.findViewById(R.id.tv_qihao_current);
            holder.iv_logo = (ImageView)convertView.findViewById(R.id.iv_zuihaolottery_logo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lotteryName.setText(mRecordList.get(position).getType());
        holder.tv_qishu_amount.setText("共" + mRecordList.get(position).getQihaoAmount() + "期");
        holder.tv_money.setText("￥" + mRecordList.get(position).getMoneyAmount());
        holder.tv_time.setText(mRecordList.get(position).getTime());
        holder.tv_zuihao_current.setText("第" + mRecordList.get(position).getQihaoCurrent()+ "期");
        holder.tv_zuihao_status.setText(mRecordList.get(position).getStatus());
        _setLotteryLogo(mRecordList.get(position).getType(), holder.iv_logo);
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_qishu_amount;
        TextView tv_time;
        TextView tv_money;
        TextView tv_zuihao_status;
        TextView tv_zuihao_current;
        ImageView iv_logo;
        TextView lotteryName;
        
    }
    private void _setLotteryLogo (String lotteryType, ImageView iv) {
    	int index = 0;
    	for(int i=0; i<BuyCenter.itemName.length; i++) {
    		if(lotteryType.equals(BuyCenter.itemName[i])) {
    			index = i;
    			break;
    		}
    	}
    	//Picasso.with(mContext).load(BuyCenter.itemImage[index]).into(iv);
    	iv.setBackgroundResource(BuyCenter.itemImage[index]);
    }
}
