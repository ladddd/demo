package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jincaizi.bean.KjxxRecordEntity;
import com.jincaizi.R;

/**
 * 开奖信息适配器
 *
 * @author yj
 */
public class KjxxAdapter extends BaseAdapter {
    private int[]k3_bg_id = {R.drawable.dice_v1_small, R.drawable.dice_v2_small, R.drawable.dice_v3_small,
            R.drawable.dice_v4_small, R.drawable.dice_v5_small, R.drawable.dice_v6_small};
    private final Context mContext;
    private final LinkedList<KjxxRecordEntity> mRecordList;
    private boolean mIsShuZiCaiHistory;
    private String mLotteryType;

    private float density;

    public String getmLotteryType() {
        return mLotteryType;
    }

    public void setmLotteryType(String mLotteryType) {
        this.mLotteryType = mLotteryType;
    }

    public KjxxAdapter(Context context, LinkedList<KjxxRecordEntity> list,
                       boolean isShuZiCaiHistory) {
        this.mContext = context;
        this.mRecordList = list;
        this.mIsShuZiCaiHistory = isShuZiCaiHistory;

        density = context.getResources().getDisplayMetrics().density;
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.main_reward_num_item, null, false);
            holder.tv_lotteryname = (TextView) convertView
                    .findViewById(R.id.kj_lottery_name);
            holder.tv_qihao = (TextView) convertView
                    .findViewById(R.id.kj_qihao);
            holder.tv_time = (TextView) convertView.findViewById(R.id.kj_time);
            holder.tv_result = (LinearLayout) convertView
                    .findViewById(R.id.award_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mIsShuZiCaiHistory) {
            holder.tv_lotteryname.setVisibility(View.GONE);
        }
        holder.tv_lotteryname.setText(mRecordList.get(position)
                .getLotteryName());

        holder.tv_time.setText(mRecordList.get(position).getTime());
        String type = mRecordList.get(position).getLotteryType();
        holder.tv_result.removeAllViews();
        holder.tv_qihao.setText("第" + mRecordList.get(position).getQiHao()+ "期");
        if (mIsShuZiCaiHistory) {
            _showBallHistory(holder.tv_result, mRecordList.get(position)
                    .getKjResult(), position);
        } else {
            _showBall(holder.tv_result, mRecordList.get(position)
                    .getKjResult(), position);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_lotteryname;
        TextView tv_qihao;
        TextView tv_time;
        LinearLayout tv_result;

    }

    /**
     * 数字彩，开奖内容， 最新开奖内容
     *
     * @param lv
     * @param content
     */
    private void _showBall(LinearLayout lv, String content, int position) {
        String[] ball = content.split(" ");
        boolean isRed = true;
        int splitIndex = -21;
        for (int i = 0; i < ball.length; i++) {
            if (ball[i].equals("|")) {
                isRed = false;
                splitIndex = i;
                continue;
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = (int)(2 * density);
            // params.gravity = Gravity.CENTER;
            TextView ballView = new TextView(mContext);
            ballView.setTextColor(mContext.getResources().getColor(
                    android.R.color.white));
            ballView.setTextSize(16);
            ballView.setGravity(Gravity.CENTER);
            if(splitIndex == i-1 && position == 2) {
                ballView.setText(" 试机号 " + ball[i]);
            } else {
                ballView.setText(ball[i]);
            }
            if (isRed) {
                //用position标识彩种不好
                if(position == 17 || position == 18 || position == 19
                        || position == 22 || position == 23) {
                    int value = Integer.parseInt(ball[i]);
                    ballView.setBackgroundResource(k3_bg_id[value-1]);
                    ballView.setText("");
                    params.rightMargin = (int)(5 * density);
                } else {

                    ballView.setBackgroundResource(R.drawable.red_ball_bg);
                }
            } else {
                if(position == 2) {
                    ballView.setTextColor(mContext.getResources().getColor(
                            android.R.color.holo_red_light));
                    ballView.setBackgroundColor(mContext.getResources().getColor(
                            android.R.color.transparent));
                } else {
                    ballView.setTextColor(mContext.getResources().getColor(
                            android.R.color.white));
                    ballView.setBackgroundResource(R.drawable.blue_ball_bg);
                }
            }
            lv.addView(ballView, params);
        }
    }

    /**
     * 数字彩，开奖内容， 最新开奖内容
     *
     * @param lv
     * @param content
     */
    private void _showBallHistory(LinearLayout lv, String content, int position) {
        String[] ball = content.split(" ");
        boolean isRed = true;
        int splitIndex = -21;
        for (int i = 0; i < ball.length; i++) {
            if (ball[i].equals("|")) {
                isRed = false;
                splitIndex = i;
                continue;
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView ballView = new TextView(mContext);
            ballView.setGravity(Gravity.CENTER);
            if(splitIndex == i-1 && mLotteryType.equals("FC3D")) {
                ballView.setText(" 试机号 " + ball[i]);
            } else {
                ballView.setText(ball[i]);
            }
            if (position == 0) {
                params.rightMargin = (int)density;
                ballView.setTextColor(mContext.getResources().getColor(
                        android.R.color.white));
                if (isRed) {
                    if(mLotteryType.equals("JSK3") || mLotteryType.equals("AHK3") || mLotteryType.equals("NMGK3")) {
                        int value = Integer.parseInt(ball[i]);
                        ballView.setBackgroundResource(k3_bg_id[value-1]);
                        ballView.setText("");
                    } else {

                        ballView.setBackgroundResource(R.drawable.red_ball_bg);
                    }
                } else {

                    if(!mLotteryType.equals("FC3D")) {
                        ballView.setBackgroundResource(R.drawable.blue_ball_bg);
                    } else {
                        ballView.setBackgroundColor(mContext.getResources().getColor(
                                android.R.color.transparent));
                        ballView.setTextColor(mContext.getResources().getColor(
                                R.color.blue));
                    }
                }
            }
            else {
                params.rightMargin = (int)(5*density);
                ballView.setTextSize(16);
                if (isRed) {
                    ballView.setTextColor(mContext.getResources().getColor(
                            R.color.red));
                } else {
                    ballView.setTextColor(mContext.getResources().getColor(
                            R.color.blue));
                }
            }
            lv.addView(ballView, params);
        }
    }

}
