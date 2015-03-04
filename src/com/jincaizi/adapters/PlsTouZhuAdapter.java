package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.PlsType;

/**
 * 投注详情适配器
 *
 * @author yj
 */
public class PlsTouZhuAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<String> mBall;
    private final LinkedList<PlsType> mType;
    private LinkedList<String> mZhushuArray = new LinkedList<String>();
    

    public LinkedList<String> getmZhushuArray() {
		return mZhushuArray;
	}

	public void setmZhushuArray(LinkedList<String> list) {
		this.mZhushuArray = list;
	}

	public PlsTouZhuAdapter(Context context,LinkedList<String> ball, LinkedList<PlsType> type) {
        this.mContext = context;
        this.mBall = ball;
        this.mType = type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mBall.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mBall.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pls_touzhu_item, null, false);
            holder.tv_ball = (TextView) convertView.findViewById(R.id.tv_pls_item);
            holder.tv_type = (TextView)convertView.findViewById(R.id.tv_pls_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_ball.setText(mBall.get(position));
        if(mType.get(position) == Constants.PlsType.ZHIXUAN) {
         holder.tv_type.setText("[直选] "+  mZhushuArray.get(position)+"注");
        } else if(mType.get(position) == Constants.PlsType.ZUSAN ) {
         holder.tv_type.setText("[组三] "+  mZhushuArray.get(position)+"注");
        } else if(mType.get(position) == Constants.PlsType.ZULIU) {
         holder.tv_type.setText("[组六] "+  mZhushuArray.get(position)+"注");
        } else if(mType.get(position) == Constants.PlsType.HEZHIZHIXUAN) {
         holder.tv_type.setText("[直选和值] "+  mZhushuArray.get(position)+"注");
        } else if(mType.get(position) == Constants.PlsType.HEZHIZUSAN) {
         holder.tv_type.setText("[组三和值] " + mZhushuArray.get(position)+"注");
        } else if(mType.get(position) == Constants.PlsType.HEZHIZULIU){
         holder.tv_type.setText("[组六和值] " + mZhushuArray.get(position)+"注");
        } else {
        	holder.tv_type.setText("");
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_ball;
        TextView tv_type;
        
    }
}
