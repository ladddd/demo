package com.jincaizi.adapters;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.Ssc_cq_fragment;
import com.jincaizi.kuaiwin.buycenter.Ssc_jx_fragment;
import com.jincaizi.kuaiwin.utils.Constants.City;
import com.jincaizi.kuaiwin.utils.Constants.SscType;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ToggleButton;

/**
 * 投注适配器
 *
 * @author yj
 */
public class SscGriViewAdapter extends BaseAdapter {
    private final String[] mData;
    private final boolean[]mChecked;
    private Fragment mFragment;
    private String mCity;
    private int mMark;//标示当前万位4，千位3，百位2，十位1，个位0的位置

    public SscGriViewAdapter(Fragment fragment, String[] data, boolean[]checked, String city) {
        this.mData = data;
        this.mChecked = checked;
        this.mFragment = fragment;
        this.mCity = city;
    }
    public SscGriViewAdapter(Fragment fragment, String[] data, boolean[]checked, String city, int mark) {
        this.mData = data;
        this.mChecked = checked;
        this.mFragment = fragment;
        this.mCity = city;
        this.mMark = mark;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData[position];
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
            convertView = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.ssc_gridview_item, null, false);
            convertView.setLayoutParams(new GridView.LayoutParams(67, 67));//重点行

            holder.type = (ToggleButton) convertView.findViewById(R.id.ssc_toggle_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.type.setText(mData[position]);
        holder.type.setTextOff(mData[position]);
        holder.type.setTextOn(mData[position]);
        holder.type.setChecked(mChecked[position]);
        holder.type.setOnClickListener(new MyClick(position));
        return convertView;
    }

    private static class ViewHolder {
        ToggleButton type;
    }
    class MyClick implements OnClickListener {
    	int position;

		public MyClick(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			ToggleButton tb = (ToggleButton)v;
			
			if(mCity.equals(City.chongqing.toString())) {
				Ssc_cq_fragment fragment = ((Ssc_cq_fragment)mFragment);
				if(fragment.mGameType.equals(SscType.twostar_zuxuan.toString())) {
					if(mMark == 1 && fragment.boolGe[position]) {
						mChecked[position] = false;
					} else if(mMark == 0 && fragment.boolShi[position]) {
						mChecked[position] = false;
					} else{
						for(int i=0; i<fragment.boolGe.length; i++) {
							mChecked[i] = false;
							mChecked[position] = tb.isChecked();
						}
					}
					notifyDataSetChanged();
				}else if(fragment.mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())
						|| fragment.mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
					for(int i=0; i< mChecked.length; i++) {
						mChecked[i] = false;
					}
					mChecked[position] = tb.isChecked();
					notifyDataSetChanged();
				} 
				
				else {
					mChecked[position] = tb.isChecked();
				}
				fragment.computeZhuShu();
				
			} else {//江西
				Ssc_jx_fragment fragment = ((Ssc_jx_fragment)mFragment);
				if(fragment.mGameType.equals(SscType.threestar_zusan.toString())) {
					
					if(mMark == 0 && (fragment.boolBai[position] || fragment.boolShi[position])) {
						mChecked[position] = false;
					} else if((mMark == 1 || mMark == 2 )&& fragment.boolGe[position]){
						for(int i=0; i<fragment.boolGe.length; i++) {
							fragment.boolBai[position] = false;
							fragment.boolShi[position] = false;
						}
					} else if(mMark == 1 || mMark == 2) {
						for(int i=0; i<fragment.boolGe.length; i++) {
							//mChecked[i] = false;
							//mChecked[position] = tb.isChecked();
							fragment.boolBai[i] = false;
							fragment.boolBai[position] = tb.isChecked();
							fragment.boolShi[i] = false;
							fragment.boolShi[position] = tb.isChecked();
						}
					} else{
						for(int i=0; i<fragment.boolGe.length; i++) {
							mChecked[i] = false;
							mChecked[position] = tb.isChecked();
						}
					} 
					fragment.threeZusanNotifyAdapter();
				} else if(fragment.mGameType.equals(SscType.threestar_zuliu.toString())) {
					if(mMark == 0 && (fragment.boolBai[position] || fragment.boolShi[position])) {
						mChecked[position] = false;
					} else if(mMark == 1 && (fragment.boolBai[position] || fragment.boolGe[position])) {
						mChecked[position] = false;
					}else if(mMark == 2 && (fragment.boolShi[position] || fragment.boolGe[position])) {
						mChecked[position] = false;
					} else {
						for(int i=0; i<fragment.boolGe.length; i++) {
							mChecked[i] = false;
							mChecked[position] = tb.isChecked();
						}
					}
					notifyDataSetChanged();
				} else if(fragment.mGameType.equals(SscType.twostar_zuxuan.toString())) {
					if(mMark == 1 && fragment.boolGe[position]) {
						mChecked[position] = false;
					} else if(mMark == 0 && fragment.boolShi[position]) {
						mChecked[position] = false;
					} else{
						for(int i=0; i<fragment.boolGe.length; i++) {
							mChecked[i] = false;
							mChecked[position] = tb.isChecked();
						}
					}
					notifyDataSetChanged();
				}  else if(fragment.mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())
						|| fragment.mGameType.equals(SscType.twostar_zhixuan_hezhi.toString()) 
						|| fragment.mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())
						|| fragment.mGameType.equals(SscType.threestar_zuliu_hezhi.toString())
						|| fragment.mGameType.equals(SscType.threestar_zusan_hezhi.toString())
						) {
					for(int i=0; i< mChecked.length; i++) {
						mChecked[i] = false;
					}
					mChecked[position] = tb.isChecked();
					notifyDataSetChanged();
				}
				else {
					mChecked[position] = tb.isChecked();
				}
				fragment.computeZhuShu();
			}
			
		}
    	
    }
    
}
