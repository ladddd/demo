package com.jincaizi.kuaiwin.buycenter.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.buycenter.BaseElevenFiveFragment;
import com.jincaizi.kuaiwin.buycenter.FrontThreezhixuanFragment;
import com.jincaizi.kuaiwin.buycenter.FrontTwozhixuanFragment;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/3/12.
 */
public class ElevenFiveCommonAdapter extends BaseAdapter {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;

    private final Fragment fragment;
    private final ArrayList<Boolean> mChecked;
    private final int type;

    private ArrayList<String> currentMiss;
    private ArrayList<Integer> maxIndex;

    private boolean showLeak;

    public ElevenFiveCommonAdapter(Fragment fragment, ArrayList<Boolean>checked, boolean showLeak)
    {
        this(fragment, checked, FIRST, showLeak);
    }

    public ElevenFiveCommonAdapter(Fragment fragment, ArrayList<Boolean>checked, int type, boolean showLeak) {
        this.fragment = fragment;
        this.mChecked = checked;
        this.type = type;
        this.showLeak = showLeak;
        maxIndex = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 11;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return 11;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(fragment.getActivity().getApplicationContext()).
                    inflate(R.layout.ball, null, false);
            holder = new ViewHolder(convertView);
//            convertView.setLayoutParams(new GridView.LayoutParams(67, 67));//重点行
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.selectCube.setSelected(mChecked.get(position));
        holder.type.setText(StringUtil.getResultNumberString(position + 1));

        if (currentMiss != null && currentMiss.size() == 11)
        {
            holder.leak.setTextColor(fragment.getResources().getColor(R.color.setting_text));
            if (maxIndex.contains(position))
            {
                holder.leak.setTextColor(fragment.getResources().getColor(R.color.buyer_red));
            }

            holder.leak.setText(currentMiss.get(position));
        }

        holder.leak.setVisibility(showLeak?View.VISIBLE:View.GONE);

        holder.selectCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                if (fragment instanceof BaseElevenFiveFragment) {
                    ((BaseElevenFiveFragment)fragment).updateSelection(position);
                }
                else if (fragment instanceof FrontTwozhixuanFragment)
                {
                    ((FrontTwozhixuanFragment) fragment).updateSelection(position, type);
                }
                else if (fragment instanceof FrontThreezhixuanFragment)
                {
                    ((FrontThreezhixuanFragment) fragment).updateSelection(position, type);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder
    {
        public TextView type;
        public TextView leak;

        public RelativeLayout selectCube;

        public ViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            type = (TextView) view.findViewById(R.id.tv_ssq_ball);
            leak = (TextView) view.findViewById(R.id.leak);
            selectCube = (RelativeLayout) view.findViewById(R.id.select_cube);
        }
    }

    public void setCurrentMiss(ArrayList<String> currentMiss) {
        this.currentMiss = currentMiss;
        findMaxIndex();
    }

    private void findMaxIndex()
    {
        maxIndex.clear();
        maxIndex.add(0);
        for (int i = 1; i < currentMiss.size(); i++) {
            if (Integer.valueOf(currentMiss.get(i)) >
                    Integer.valueOf(currentMiss.get(maxIndex.get(0))))
            {
                maxIndex.clear();
                maxIndex.add(i);
            }
            else if (Integer.valueOf(currentMiss.get(i)).equals(
                    Integer.valueOf(currentMiss.get(maxIndex.get(0)))))
            {
                maxIndex.add(i);
            }
        }
    }
}
