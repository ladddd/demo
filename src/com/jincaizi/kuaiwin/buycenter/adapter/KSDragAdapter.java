package com.jincaizi.kuaiwin.buycenter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.K3_hz_fragment;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/3/12.
 */
public class KSDragAdapter extends BaseAdapter {

    public static final int FIRST = 0;
    public static final int SECOND = 1;

    public static final int THREE_DRAG = 2;
    public static final int TWO_DRAG = 1;

    private K3_hz_fragment fragment;

    private boolean[] selectedNumber;

    private int type;

    private int maxFirst;

    private ArrayList<String> missCount;

    private int maxIndex;

    public KSDragAdapter(K3_hz_fragment fragment, boolean[] selectedNumber, int type)
    {
        this(fragment, selectedNumber, type, 0);
    }


    public KSDragAdapter(K3_hz_fragment fragment, boolean[] selectedNumber, int type, int maxFirst)
    {
        this.fragment = fragment;
        this.selectedNumber = selectedNumber;
        this.type = type;
        this.maxFirst = maxFirst;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(fragment.getActivity().getApplicationContext()).inflate(R.layout.choice_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder) convertView.getTag();
        }

        holder.selectCube.setSelected(selectedNumber[i + type*6]);
        holder.type.setText(String.valueOf((i+1)));

        holder.tips.setVisibility(View.GONE);

        holder.leak.setVisibility(View.VISIBLE);

        if (missCount != null && missCount.size() == 6)
        {
            holder.leak.setTextColor(fragment.getResources().getColor(R.color.setting_text));
            if (i == maxIndex)
            {
                holder.leak.setTextColor(fragment.getResources().getColor(R.color.buyer_red));
            }

            holder.leak.setText(missCount.get(i));
        }

        holder.selectCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected())
                {
                    if (type == FIRST) {
                        int count = 0;
                        for (int j = 0; j < 6; j++) {
                            if (selectedNumber[j]) count++;
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
                        for (int j = 0; j < 6; j++) {
                            if (selectedNumber[j + 6]) count++;
                        }
                        if (count >= 5) {
                            Toast.makeText(fragment.getActivity(), "至少选择一个胆码",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                view.setSelected(!view.isSelected());
//                selectedNumber[i + type*6] = view.isSelected();
                fragment.updateDragChoice(i + type*6, view.isSelected());
            }
        });

        return convertView;
    }

    private class ItemViewHolder
    {
        public TextView type;
        public TextView tips;
        public TextView leak;

        public RelativeLayout selectCube;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            type = (TextView) view.findViewById(R.id.type);
            tips = (TextView) view.findViewById(R.id.tips);
            leak = (TextView) view.findViewById(R.id.leak);
            selectCube = (RelativeLayout) view.findViewById(R.id.select_cube);
        }
    }

    public void setMissCount(ArrayList<String> missCount)
    {
        this.missCount = missCount;

        findMaxIndex();
    }

    private void findMaxIndex()
    {
        int max = 0;
        for (int i = 1; i < missCount.size(); i++) {
            if (Integer.valueOf(missCount.get(i)) >
                    Integer.valueOf(missCount.get(i-1)))
            {
                max = i;
            }
        }
        maxIndex = max;
    }
}
