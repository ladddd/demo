package com.jincaizi.kuaiwin.chart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.chart.TableFixHeaders.adapters.BaseTableAdapter;

/**
 * Created by chenweida on 2015/2/9.
 */
public class BottomAdapter extends BaseTableAdapter {

    private Context context;
    private float density;

    private boolean[] chosenNumber;

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 16;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.bottom_grid_item, null);
            convertView.setTag(column);

            if (column >= 0) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (Integer) view.getTag();
                        chosenNumber[index] = !chosenNumber[index];
                        view.setSelected(chosenNumber[index]);
                    }
                });
            }
        }
        else
        {
            convertView.setTag(column);
        }
        if (column >= 0) {
            convertView.setSelected(chosenNumber[column]);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.grid_item_text);
        TextView number = (TextView) convertView.findViewById(R.id.grid_item_number);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        if (column == -1)
        {
            textView.setTextSize(12);
            textView.setText("选号(和值)");
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
        }
        else {
            number.setText(column + 3 + "");
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            number.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public int getWidth(int column) {
        int width = 30;
        if (column == -1)
        {
            width = 60;
        }
        return Math.round(width * density);
    }

    @Override
    public int getHeight(int row) {
        return Math.round(40 * density);
    }

    @Override
    public int getItemViewType(int row, int column) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasLineView() {
        return false;
    }

    public BottomAdapter(Context context)
    {
        this.context = context;
        density = context.getResources().getDisplayMetrics().density;

        chosenNumber = new boolean[16];
    }

    public boolean[] getChosenNumber()
    {
        return chosenNumber;
    }
}
