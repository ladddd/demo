package com.jincaizi.adapters;

import java.util.ArrayList;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.SmartFollow;
import com.jincaizi.kuaiwin.utils.Utils;

/**
 * SmartFollowAdapter适配器
 *
 * @author yj
 */
public class SmartFollowAdapter extends BaseAdapter {
    private SmartFollow mActivity;
    private  ArrayList<SmartFollowElement> mData;
    private int[] mPerAward;
    public SmartFollowAdapter(SmartFollow activity, ArrayList<SmartFollowElement> data, int[] perAward) {
        this.mActivity = activity;
        this.mData = data;
        this.mPerAward = perAward;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
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
        //TODO 为什么此处重用view 反而卡的动不了
//        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.smartfollow_list_item, null, false);
            holder.xuhao = (TextView) convertView.findViewById(R.id.bet_zhu_tv);
            holder.qihao = (TextView)convertView.findViewById(R.id.plan_period_tv);
            holder.beishu = (EditText)convertView.findViewById(R.id.bet_time_tv);
            holder.touruAmount = (TextView)convertView.findViewById(R.id.total_count);
            holder.yingliAmount = (TextView)convertView.findViewById(R.id.profit_count);
            holder.yingliProfit = (TextView)convertView.findViewById(R.id.profit_precent);
            holder.add = (ImageView)convertView.findViewById(R.id.tv_add);
            holder.sub = (ImageView)convertView.findViewById(R.id.tv_sub);
            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        holder.xuhao.setText(position+"");
        if(mActivity.mLotteryType.equals("11x5")) {
             holder.qihao.setText(mData.get(position).qihao.substring(8,10)+"");
        } else {
        	 holder.qihao.setText(mData.get(position).qihao.substring(8,11)+"");	
        }
        holder.beishu.setText(mData.get(position).beishu+"");
        holder.touruAmount.setText(mData.get(position).touruAmount+"");
        holder.yingliAmount.setText(mData.get(position).yingliAmount+"");
        holder.yingliProfit.setText(mData.get(position).yingliProfit);
        
        String[] str = mData.get(position).yingliAmount.split("至");
        if(str.length>1) {
        	String frontColor;
        	String endColor;
        	if(str[0].startsWith("-")) {
        		frontColor = "<font color='#6e940e'>"+str[0]+"</font>";
        	} else {
        		frontColor = "<font color='#b00f0f'>"+str[0]+"</font>";
        	}
            if(str[1].startsWith("-")) {
        		endColor = "<font color='#6e940e'>"+str[1]+"</font>";
        	} else {
        		endColor = "<font color='#b00f0f'>"+str[1]+"</font>";
        	}
            String source = frontColor +"<br>至<br>" + endColor;  
            holder.yingliAmount.setText(Html.fromHtml(source));
        } else {
        	String frontColor;
        	if(str[0].startsWith("-")) {
        		frontColor = "<font color='#6e940e'>"+str[0]+"</font>";
        	} else {
        		frontColor = "<font color='#b00f0f'>"+str[0]+"</font>";
        	}
            String source = frontColor;  
            holder.yingliAmount.setText(Html.fromHtml(source));
        }
        
        str = mData.get(position).yingliProfit.split("至");
        if(str.length>1) {
        	String frontColor;
        	String endColor;
        	if(str[0].startsWith("-")) {
        		frontColor = "'#6e940e'";
        	} else {
        		frontColor = "'#b00f0f'";
        	}
            if(str[1].startsWith("-")) {
        		endColor = "'#6e940e'";
        	} else {
        		endColor = "'#b00f0f'";
        	}
            String source = "<font color="+frontColor+">"+str[0]+"</font><br>至<br>" + "<font color="+endColor+">"+str[1]+"</font>";  
            holder.yingliProfit.setText(Html.fromHtml(source));
        } else {
        	String frontColor;
        	if(str[0].startsWith("-")) {
        		frontColor = "'#6e940e'";
        	} else {
        		frontColor = "'#b00f0f'";
        	}
            String source = "<font color="+frontColor+">"+str[0]+"</font>";  
            holder.yingliProfit.setText(Html.fromHtml(source));
        }
        
    
        holder.beishu.addTextChangedListener(new MyWatcher(holder.beishu, position));
        holder.add.setOnClickListener(new MyAdd(holder.beishu, position));
        holder.sub.setOnClickListener(new MySub(holder.beishu, position));
        
        return convertView;
    }
    class MyAdd implements OnClickListener {
    	EditText mEt;
        int mPosition;
        
		public MyAdd(EditText mEt, int mPosition) {
			super();
			this.mEt = mEt;
			this.mPosition = mPosition;
		}

		@Override
		public void onClick(View v) {
			int beishu = mData.get(mPosition).beishu + 1;
			if(beishu > 9999) {
				return;
			}
			mEt.setText(beishu+"");
		}
    	
    }
    class MySub implements OnClickListener {
    	EditText mEt;
        int mPosition;
        
		public MySub(EditText mEt, int mPosition) {
			super();
			this.mEt = mEt;
			this.mPosition = mPosition;
		}

		@Override
		public void onClick(View v) {
			int beishu = mData.get(mPosition).beishu - 1;
			if(beishu < 1) {
				return;
			}
			mEt.setText(beishu+"");
			
		}
    	
    }
   
    class MyWatcher implements TextWatcher {
    	EditText mEt;
        int mPosition;
		public MyWatcher(EditText mEt, int postion) {
			super();
			this.mEt = mEt;
			this.mPosition = postion;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(TextUtils.isEmpty(s) || Integer.valueOf(s.toString()) <1) {
				mEt.setText("1");
			} else if(Integer.valueOf(s.toString()) >9999) {
				mEt.setText("9999");
			}
			reComputeYingShou(mEt.getText().toString(), mPosition);
			
		}
    	
    }
    private static class ViewHolder {
    	TextView xuhao;
    	TextView qihao;
    	EditText beishu;
    	TextView touruAmount;
    	TextView yingliAmount;
    	TextView yingliProfit;
    	ImageView sub;
        ImageView add;
    	
    }
    public static class SmartFollowElement {
    	public int zhushu;
    	public String qihao;
    	public int beishu;
    	public int touruAmount;
    	public String yingliAmount;
    	public String yingliProfit;
    }
    private void reComputeYingShou(String input, int postion) {
    	
    	//SmartFollowElement element = new SmartFollowElement();
		for(int i=0; i<mData.size(); i++) {
			mData.get(postion).beishu =  Integer.valueOf(input);
			if(i ==0) {
				mData.get(i).touruAmount =  mData.get(i).beishu * mData.get(i).zhushu*2;
			} else {
				mData.get(i).touruAmount =  mData.get(i).beishu * mData.get(i).zhushu*2 + mData.get(i-1).touruAmount;
				
			}
			int[] yinli = new int[mPerAward.length];
			double[] percent = new double[mPerAward.length];
			StringBuilder yinliStr = new StringBuilder();
			StringBuilder percentStr = new StringBuilder();
			for(int index=0; index< mPerAward.length; index++) {
				yinli[index] = mData.get(i).beishu *mPerAward[index] - mData.get(i).touruAmount ;
				percent[index] = (yinli[index]*100.0)/(mData.get(i).touruAmount*1.0);
				if(index !=0) {
					yinliStr.append("至");
					percentStr.append("至");
				}
				yinliStr.append(yinli[index]);
				try {
					percentStr.append(Utils.formatDoubleForTwo(percent[index]) +"%");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					percentStr.append("--%");
				}
			}
			mData.get(i).yingliAmount  = yinliStr.toString();
			mData.get(i).yingliProfit = percentStr.toString();
			if(i ==0) {
				mActivity.mMinProfitRate = percent[0];
			}
			if(percent[0] < mActivity.mMinProfitRate) {
				mActivity.mMinProfitRate = percent[0];
			}
			
		}
		notifyDataSetChanged();
		mActivity.mBetMoney.setText(mData.get(mData.size()-1).touruAmount+"");
		mActivity.profitMode = 0;
		mActivity.updatePlanInfo();
    }
}