package com.jincaizi.kuaiwin.buycenter.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jincaizi.R;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.buycenter.*;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/3/12.
 */
public class ElevenFiveDragAdapter extends BaseAdapter {

    public static final int DRAG_FIRST = 0;
    public static final int DRAG_SECOND = 1;

    private final Fragment fragment;
    private final ArrayList<Boolean> mChecked;
    private final int type;
    private final int maxFirst;

    private ArrayList<String> currentMiss;
    private ArrayList<Integer> maxIndex;

    private boolean showLeak;
    public ElevenFiveDragAdapter(Fragment fragment, ArrayList<Boolean>checked,
                                 int type, int maxFirst, boolean showLeak) {
        this.fragment = fragment;
        this.mChecked = checked;
        this.type = type;
        this.maxFirst = maxFirst;
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.selectCube.setSelected(mChecked.get(position));
        holder.type.setText(StringUtil.getResultNumberString(position + 1));

        holder.leak.setVisibility(showLeak?View.VISIBLE:View.GONE);

        if (currentMiss != null && currentMiss.size() == 11)
        {
            holder.leak.setTextColor(fragment.getResources().getColor(R.color.setting_text));
            if (maxIndex.contains(position))
            {
                holder.leak.setTextColor(fragment.getResources().getColor(R.color.buyer_red));
            }

            holder.leak.setText(currentMiss.get(position));
        }

        holder.selectCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!view.isSelected())
                {
                    if (type == DRAG_FIRST) {
                        int count = 0;
                        for (int j = 0; j < 11; j++) {
                            if (mChecked.get(j)) count++;
                        }
                        if (count >= maxFirst) {
                            Toast.makeText(fragment.getActivity(), "此玩法最多只能存在" + maxFirst + "个胆码",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else
                    {
                        int count = 0;
                        for (int j = 0; j < 11; j++) {
                            if (mChecked.get(j)) count++;
                        }
                        if (count >= 10) {
                            Toast.makeText(fragment.getActivity(), "至少选择一个胆码",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                }

                view.setSelected(!view.isSelected());
                if (fragment instanceof ElevenFiveBaseDragFragment)
                {
                    ((ElevenFiveBaseDragFragment) fragment).updateSelection(position, type);
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
