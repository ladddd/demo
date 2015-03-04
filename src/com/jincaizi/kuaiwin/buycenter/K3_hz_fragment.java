package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.K3Random;
import com.jincaizi.kuaiwin.utils.Constants.K3Type;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.AnimationController;

public class K3_hz_fragment extends Fragment{
    public static final String TAG_HZ ="K3_hz_fragment";
    public static final String TAG_THREE_SAME ="K3_threesame_fragment";
    public static final String TAG_TWO_SAME ="K3_twosame_fragment";
    public static final String TAG_THREE_DIF ="K3_threedif_fragment";
    public static final String TAG_TWO_DIF ="K3_twodif_fragment";
    public static final String TAG_THREE_DIF_DRAG ="K3_threedif_drag_fragment";
    public static final String TAG_TWO_DIF_DRAG ="K3_twodif_drag_fragment";
    public static final String BETTYPE ="bettype";
    private String[] r_content = { "3 \n奖金240元", "4 \n奖金80元", "5 \n奖金40元", "6 \n奖金25元", "7 \n奖金16元", "8 \n奖金12元", "9 \n奖金10元",
            "10 \n奖金9元", "11 \n奖金9元","12 \n奖金10元","13 \n奖金12元","14 \n奖金16元","15 \n奖金25元","16 \n奖金40元","17 \n奖金80元","18 \n奖金240元" };
    private String[] three_same_content = { "111 \n奖金240元", "222 \n奖金240元", "333 \n奖金240元",
            "444 \n奖金240元", "555 \n奖金240元", "666 \n奖金240元", "三同号通选 \n任意一个豹子开出，即中40元" };
    private String[]two_same_content = {"11", "22", "33","44","55","66",
            "1", "2", "3","4","5","6",
            "11*", "22*", "33*","44*","55*","66*"};
    private String[]three_diff_content = {
            "1", "2", "3","4","5","6",
            "三连号通选"};
    private boolean[] bool = new boolean[50];

    private LinearLayout mLvMatch;
    private String mGameType;
    private K3 mActivity;
    private AnimationDrawable anim1;
    private AnimationDrawable anim2;
    private AnimationDrawable anim3;
    public Dialog myDialog;
    private RelativeLayout rv_anim;
    public int mZhushu = 0;

    private boolean changedAfterWatchChart;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        mGameType = bundle.getString(BETTYPE);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (K3)getActivity();
    }
    private void _initBool(int size) {
        for(int i=0; i<size; i++) {
            bool[i] = false;
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvMatch = (LinearLayout)view.findViewById(R.id.k3_hz_linearlayout);
        _drawViews();
        computeZhushu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.k3_hz_fragment_layout, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void updateBallData() {
        Vibrator vibrator = (Vibrator) getActivity().getApplication()
                .getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] { 0, 200 }, -1);


        int factor = 0;
        int length = 16;
        if(mActivity.mCaseIndex == 1) {
            factor = 16;
            length = 7;
        } else if(mActivity.mCaseIndex == 2) {
            factor = 23;
            length = 18;
        } else if(mActivity.mCaseIndex == 3) {
            factor = 41;
            length = 7;
        } else if(mActivity.mCaseIndex == 4) {
            factor = 48;
            length = 6;
        }
        for(int i=0; i<length; i++) {
            Log.d("test1", i+"+"+factor+"=" +(i+factor));
            mActivity.tbArrays[i+factor].setChecked(false);
            //((ToggleButton)mLvMatch.findViewById((i+factor))).setChecked(false);
        }
        _showDiceFrame(mActivity.mCaseIndex);
    }
    private void _showDiceFrame(int caseIndex) {
        myDialog = new Dialog(getActivity(), R.style.Theme_dialog);
        View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(
                R.layout.k3_dice_frame, null);
        myDialog.setContentView(view);
        diceOne = (ImageView)view.findViewById(R.id.iv1);
        diceTwo = (ImageView)view.findViewById(R.id.iv2);
        diceThree = (ImageView)view.findViewById(R.id.iv3);
        rv_anim = (RelativeLayout)view.findViewById(R.id.rv_anim);

        if(caseIndex == 4) {
            diceThree.setVisibility(View.GONE);
        }

        diceOne.setBackgroundResource(R.drawable.throw_shaizi_1);
        diceTwo.setBackgroundResource(R.drawable.throw_shaizi_2);
        diceThree.setBackgroundResource(R.drawable.throw_shaizi_3);


        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

        myDialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub
                AnimationController animationController = new AnimationController();
                animationController.slideFadeIn(rv_anim, 1000, 0);//延时1秒执行动画，动画过程执行时间为2秒
                anim1 = (AnimationDrawable) diceOne.getBackground();
                anim2 = (AnimationDrawable) diceTwo.getBackground();
                anim3 = (AnimationDrawable) diceThree.getBackground();
                anim1.start();
                anim2.start();
                anim3.start();
                Message handleMsg3 = handler.obtainMessage();
                handleMsg3.what = mActivity.mCaseIndex;
                Log.d("test", "index = "+ mActivity.mCaseIndex);
                handler.sendMessageDelayed(handleMsg3, 3000);
            }
        });
        myDialog.show();
    }
    private ImageView diceOne, diceTwo, diceThree;
    private static int[]drawable_dice = {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HEZHI:
                    doAnimationOfHeZhi();
                    break;
                case THREESAME:
                    doAnimationOfThreeSame();
                    break;
                case TWOSAME:
                    doAnimationOfTwoSame();
                    break;
                case THREEDIF:
                    doAnimationOfThreeDif();
                    break;
                case TWODIF:
                    doAnimationOfTwoDif();
                    break;
                case DISMISS:
                    updateSelection(false);
                    myDialog.dismiss();
                    myDialog = null;
                    break;
                case UPDATE:
                    updateSelection(true);
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private void updateSelection(boolean vibrate)
    {
        if (vibrate && changedAfterWatchChart)
        {
            Vibrator vibrator = (Vibrator) getActivity().getApplication()
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[] { 0, 200 }, -1);
            changedAfterWatchChart = false;
        }

        int factor = 0;
        int length = 16;
        if(mActivity.mCaseIndex == 1) {
            factor = 16;
            length = 7;
        } else if(mActivity.mCaseIndex == 2) {
            factor = 23;
            length = 18;
        } else if(mActivity.mCaseIndex == 3) {
            factor = 41;
            length = 7;
        } else if(mActivity.mCaseIndex == 4) {
            factor = 48;
            length = 6;
        }
        for(int i=0; i<length; i++) {
            mActivity.tbArrays[i+factor].setChecked(bool[i]);
            //((ToggleButton)mLvMatch.findViewById((i+factor))).setChecked(bool[i]);
        }
        computeZhushu();
    }

    private void doAnimationOfHeZhi() {
        Random ramdom = new Random();
        int nu1 = ramdom.nextInt(6);
        int nu2 = ramdom.nextInt(6);
        int nu3 = ramdom.nextInt(6);
        anim1.stop();
        anim2.stop();
        anim3.stop();
        diceOne.setBackgroundResource(drawable_dice[nu1]);
        diceTwo.setBackgroundResource(drawable_dice[nu2]);
        diceThree.setBackgroundResource(drawable_dice[nu3]);
        //rv_anim.scrollBy(toX, toY);
        AnimationController animationController = new AnimationController();
        animationController.slideFadeOut(rv_anim, 1000, 2000);//延时1秒执行动画，动画过程执行时间为2秒
        for(int i=0; i<bool.length; i++) {
            bool[i] = false;
            bool[nu1+nu2+nu3] = true;
        }
        Toast.makeText(getActivity(), (nu1+1)+" + "+(nu2+1)+" + "+(nu3+1) + " = " + (nu1+nu2+nu3+3), Toast.LENGTH_SHORT).show();
        Message dismissMsg = handler.obtainMessage();
        dismissMsg.what = DISMISS;
        handler.sendMessageDelayed(dismissMsg, 2500);
    }
    private void doAnimationOfThreeSame() {
        Random ramdom = new Random();
        int nu1 = ramdom.nextInt(6);
        anim1.stop();
        anim2.stop();
        anim3.stop();
        diceOne.setBackgroundResource(drawable_dice[nu1]);
        diceTwo.setBackgroundResource(drawable_dice[nu1]);
        diceThree.setBackgroundResource(drawable_dice[nu1]);
        //rv_anim.scrollBy(toX, toY);
        AnimationController animationController = new AnimationController();
        animationController.slideFadeOut(rv_anim, 1000, 2000);//延时1秒执行动画，动画过程执行时间为2秒
        for(int i=0; i<three_same_content.length; i++) {
            bool[i] = false;
            bool[nu1] = true;
        }
        Message dismissMsg = handler.obtainMessage();
        dismissMsg.what = DISMISS;
        handler.sendMessageDelayed(dismissMsg, 2500);
    }
    private void doAnimationOfTwoSame() {
        Random ramdom = new Random();
        int nu1 = ramdom.nextInt(6);
        int nu2 = ramdom.nextInt(6);
        anim1.stop();
        anim2.stop();
        anim3.stop();
        diceOne.setBackgroundResource(drawable_dice[nu1]);
        diceTwo.setBackgroundResource(drawable_dice[nu1]);
        diceThree.setBackgroundResource(drawable_dice[nu2]);
        //rv_anim.scrollBy(toX, toY);
        AnimationController animationController = new AnimationController();
        animationController.slideFadeOut(rv_anim, 1000, 2000);//延时1秒执行动画，动画过程执行时间为2秒
        for(int i=0; i<two_same_content.length; i++) {
            bool[i] = false;
            bool[nu1] = true;
            bool[nu2+6] = true;
        }
        Message dismissMsg = handler.obtainMessage();
        dismissMsg.what = DISMISS;
        handler.sendMessageDelayed(dismissMsg, 2500);
    }
    private void doAnimationOfThreeDif() {
        int[]result = K3Random.getThreeDiff();
        anim1.stop();
        anim2.stop();
        anim3.stop();
        diceOne.setBackgroundResource(drawable_dice[result[0]]);
        diceTwo.setBackgroundResource(drawable_dice[result[1]]);
        diceThree.setBackgroundResource(drawable_dice[result[2]]);
        //rv_anim.scrollBy(toX, toY);
        AnimationController animationController = new AnimationController();
        animationController.slideFadeOut(rv_anim, 1000, 2000);//延时1秒执行动画，动画过程执行时间为2秒
        for(int i=0; i<three_diff_content.length; i++) {
            bool[i] = false;
            bool[result[0]] = true;
            bool[result[1]] = true;
            bool[result[2]] = true;
        }
        Message dismissMsg = handler.obtainMessage();
        dismissMsg.what = DISMISS;
        handler.sendMessageDelayed(dismissMsg, 2500);
    }
    private void doAnimationOfTwoDif() {
        int[]result = K3Random.getTwoDiff();
        anim1.stop();
        anim2.stop();
        diceOne.setBackgroundResource(drawable_dice[result[0]]);
        diceTwo.setBackgroundResource(drawable_dice[result[1]]);
        //rv_anim.scrollBy(toX, toY);
        AnimationController animationController = new AnimationController();
        animationController.slideFadeOut(rv_anim, 1000, 2000);//延时1秒执行动画，动画过程执行时间为2秒
        for(int i=0; i<three_diff_content.length - 1; i++) {
            bool[i] = false;
            bool[result[0]] = true;
            bool[result[1]] = true;
        }
        Message dismissMsg = handler.obtainMessage();
        dismissMsg.what = DISMISS;
        handler.sendMessageDelayed(dismissMsg, 2500);
    }
    private static final int HEZHI = 0;
    private static final int THREESAME = 1;
    private static final int TWOSAME = 2;
    private static final int THREEDIF = 3;
    private static final int TWODIF = 4;
    private static final int DISMISS = 10;
    private static final int UPDATE = 20;
    private void _drawViews() {
        if(mGameType.equals(K3Type.hezhi.toString())) {
            //bool = new boolean[r_content.length];
            _initBool(r_content.length);
            _drawViewHZ();
        } else if(mGameType.equals(K3Type.threesamesingle.toString())) {
            //bool = new boolean[three_same_content.length];
            _initBool(three_same_content.length);
            _drawViewThreeSame();
        } else if(mGameType.equals(K3Type.twosamesingle.toString())) {
            //bool = new boolean[two_same_content.length];
            _initBool(two_same_content.length);
            _drawViewTwoSame();
        } else if(mGameType.equals(K3Type.threedifsingle.toString())) {
            //bool = new boolean[three_diff_content.length];
            _initBool(three_diff_content.length);
            _drawViewThreeDif();
        } else if(mGameType.equals(K3Type.twodif.toString())) {
            //bool = new boolean[three_diff_content.length];
            _initBool(three_diff_content.length);
            _drawViewTwoDif();
        } else if(mGameType.equals(K3Type.dragthree.toString())) {
            //bool = new boolean[three_diff_content.length];
            _initBool(12);
            _drawViewDrag(getResources().getString(R.string.k3_drag3_num));
        } else {
            //bool = new boolean[three_diff_content.length];
            _initBool(12);
            _drawViewDrag(getResources().getString(R.string.k3_drag2_num));
        }
    }

    private void _drawViewHZ() {
        int tb_id = 0;
        for (int i = 0; i < 4; i++) {
            LinearLayout lv = new LinearLayout(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            lp.leftMargin = 6;
            lp.rightMargin = 10;
            lv.setLayoutParams(lp);
            lv.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 4; j++) {
                int tb_index = i * 4 + j;
                ToggleButton tv1 = new ToggleButton(getActivity());
                SpannableString ss = new SpannableString(r_content[tb_index]);
                int index = r_content[tb_index].indexOf(" ");
                ss.setSpan(new AbsoluteSizeSpan(12), index,
                        r_content[tb_index].length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d("test", tb_id+"");
                tv1.setId(tb_id++);
                tv1.setText(ss);
                tv1.setTextOff(ss);
                tv1.setTextOn(ss);
                tv1.setPadding(10, 5, 10, 5);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(this.getResources().getColor(R.color.footer_black));
                tv1.setBackgroundResource(R.drawable.k3_btn_toggle_bg);
                tv1.setChecked(bool[tb_index]);
                LinearLayout.LayoutParams tp = new LayoutParams(0, LayoutParams.MATCH_PARENT,
                        1);
                tp.leftMargin = 4;
                lv.addView(tv1, tp);

                tv1.setOnClickListener(new MyClick(tb_index));
                mActivity.tbArrays[tb_id-1] = tv1;
                //tb_id++;
            }

            mLvMatch.addView(lv);

        }
        LinearLayout ruleLV = new LinearLayout(getActivity());
        LinearLayout.LayoutParams rulelp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rulelp.topMargin = 20;
        rulelp.leftMargin = 10;
        rulelp.rightMargin = 10;
        ruleLV.setLayoutParams(rulelp);
        ruleLV.setOrientation(LinearLayout.HORIZONTAL);
        TextView ruleHint = new TextView(getActivity());
        ruleHint.setTextColor(this.getResources().getColor((R.color.red)));
        ruleHint.setText(this.getResources().getString(R.string.k3_hz_rulehint));
        ruleLV.addView(ruleHint);
        mLvMatch.addView(ruleLV);
    }
    private void _drawViewThreeSame() {
        int tb_id = 16;
        for (int i = 0; i < 3; i++) {
            LinearLayout lv = new LinearLayout(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            lp.leftMargin = 6;
            lp.rightMargin = 10;
            lv.setLayoutParams(lp);
            lv.setOrientation(LinearLayout.HORIZONTAL);
            int size = i==2?1:3;;

            for (int j = 0; j < size; j++) {
                int tb_index = i * 3 + j;
                ToggleButton tv1 = new ToggleButton(getActivity());
                SpannableString ss = new SpannableString(three_same_content[tb_index]);
                int index = three_same_content[tb_index].indexOf(" ");
                ss.setSpan(new AbsoluteSizeSpan(12), index,
                        three_same_content[tb_index].length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d("test", tb_id+"");
                tv1.setId((tb_id++));
                tv1.setText(ss);
                tv1.setTextOff(ss);
                tv1.setTextOn(ss);
                tv1.setPadding(10, 5, 10, 5);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(this.getResources().getColor(R.color.footer_black));
                tv1.setBackgroundResource(R.drawable.k3_btn_toggle_bg);
                tv1.setChecked(bool[tb_index]);
                LinearLayout.LayoutParams tp = new LayoutParams(0, LayoutParams.MATCH_PARENT,
                        1);
                tp.leftMargin = 4;
                lv.addView(tv1, tp);

                tv1.setOnClickListener(new MyClick(tb_index));
                mActivity.tbArrays[tb_id-1] = tv1;
                //tb_id++;
            }

            mLvMatch.addView(lv);

        }
        LinearLayout ruleLV = new LinearLayout(getActivity());
        LinearLayout.LayoutParams rulelp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rulelp.topMargin = 20;
        rulelp.leftMargin = 10;
        rulelp.rightMargin = 10;
        ruleLV.setLayoutParams(rulelp);
        ruleLV.setOrientation(LinearLayout.HORIZONTAL);
        TextView ruleHint = new TextView(getActivity());
        ruleHint.setTextColor(this.getResources().getColor((R.color.red)));
        ruleHint.setText(this.getResources().getString(R.string.k3_threesame_rulehint));
        ruleLV.addView(ruleHint);
        mLvMatch.addView(ruleLV);
    }
    private void _drawViewTwoSame() {
        int tb_id = 23;
        for (int i = 0; i < 3; i++) {
            switch(i) {
                case 0:
                    LinearLayout lv = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.topMargin = 10;
                    lp.leftMargin = 10;
                    lp.rightMargin = 10;
                    lv.setLayoutParams(lp);
                    lv.setOrientation(LinearLayout.VERTICAL);
                    TextView tv = new TextView(getActivity());
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
                    tv.setText(getResources().getString(R.string.k3_two_single));
                    tv.setTextColor(getResources().getColor(R.color.tc_default));
                    tv.setGravity(Gravity.LEFT);
                    lv.addView(tv);
                    TextView tv_num = new TextView(getActivity());
                    tv_num.setText(getResources().getString(R.string.k3_samenum));
                    tv_num.setTextColor(getResources().getColor(R.color.footer_black));
                    tv_num.setGravity(Gravity.CENTER);
                    lv.addView(tv_num);
                    mLvMatch.addView(lv);
                    break;
                case 1:
                    LinearLayout lv1 = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp1.topMargin = 10;
                    lp1.leftMargin = 10;
                    lp1.rightMargin = 10;
                    lv1.setLayoutParams(lp1);
                    lv1.setGravity(Gravity.CENTER);
                    lv1.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv_num_dif = new TextView(getActivity());
                    tv_num_dif.setText(getResources().getString(R.string.k3_difnum));
                    tv_num_dif.setTextColor(getResources().getColor(R.color.footer_black));
                    lv1.addView(tv_num_dif);
                    mLvMatch.addView(lv1);
                    break;
                case 2:
                    LinearLayout lv2 = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp2.topMargin = 10;
                    lp2.leftMargin = 10;
                    lp2.rightMargin = 10;
                    lv2.setLayoutParams(lp2);
                    lv2.setOrientation(LinearLayout.HORIZONTAL);
                    lv2.setGravity(Gravity.LEFT);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
                    tv2.setText(getResources().getString(R.string.k3_two_mutiple));
                    tv2.setTextColor(getResources().getColor(R.color.tc_default));
                    lv2.addView(tv2);
                    mLvMatch.addView(lv2);
                    break;
                default:
                    break;
            }
            LinearLayout lv = new LinearLayout(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            lp.leftMargin = 6;
            lp.rightMargin = 10;
            lv.setLayoutParams(lp);
            lv.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < 6; j++) {
                int tb_index = i * 6 + j;
                ToggleButton tv1 = new ToggleButton(getActivity());
                Log.d("test", tb_id+"");
                tv1.setId(tb_id++);
                tv1.setText(two_same_content[tb_index]);
                tv1.setTextOff(two_same_content[tb_index]);
                tv1.setTextOn(two_same_content[tb_index]);
                tv1.setPadding(10, 5, 10, 5);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(this.getResources().getColor(R.color.footer_black));
                tv1.setBackgroundResource(R.drawable.k3_btn_toggle_bg);
                tv1.setChecked(bool[tb_index]);
                LinearLayout.LayoutParams tp = new LayoutParams(0, LayoutParams.MATCH_PARENT,
                        1);
                tp.leftMargin = 4;
                lv.addView(tv1, tp);

                tv1.setOnClickListener(new MyClick(tb_index));
                mActivity.tbArrays[tb_id-1] = tv1;
            }

            mLvMatch.addView(lv);

        }
        LinearLayout ruleLV = new LinearLayout(getActivity());
        LinearLayout.LayoutParams rulelp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rulelp.topMargin = 20;
        rulelp.leftMargin = 10;
        rulelp.rightMargin = 10;
        ruleLV.setLayoutParams(rulelp);
        ruleLV.setOrientation(LinearLayout.HORIZONTAL);
        TextView ruleHint = new TextView(getActivity());
        ruleHint.setTextColor(this.getResources().getColor((R.color.red)));
        ruleHint.setText(this.getResources().getString(R.string.k3_twosame_rulehint));
        ruleLV.addView(ruleHint);
        mLvMatch.addView(ruleLV);
    }
    private void _drawViewThreeDif() {
        int tb_id = 41;
        for (int i = 0; i < 2; i++) {
            switch(i) {
                case 0:
                    LinearLayout lv = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.topMargin = 10;
                    lp.leftMargin = 10;
                    lp.rightMargin = 10;
                    lv.setLayoutParams(lp);
                    lv.setOrientation(LinearLayout.VERTICAL);
                    TextView tv = new TextView(getActivity());
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
                    tv.setText(getResources().getString(R.string.k3_three_diff_num));
                    tv.setTextColor(getResources().getColor(R.color.tc_default));
                    tv.setGravity(Gravity.LEFT);
                    lv.addView(tv);
                    mLvMatch.addView(lv);
                    break;
                case 1:
                    LinearLayout lv2 = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp2.topMargin = 10;
                    lp2.leftMargin = 10;
                    lp2.rightMargin = 10;
                    lv2.setLayoutParams(lp2);
                    lv2.setOrientation(LinearLayout.HORIZONTAL);
                    lv2.setGravity(Gravity.LEFT);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
                    tv2.setText(getResources().getString(R.string.k3_three_same_num));
                    tv2.setTextColor(getResources().getColor(R.color.tc_default));
                    lv2.addView(tv2);
                    mLvMatch.addView(lv2);
                    break;
                default:
                    break;
            }
            LinearLayout lv = new LinearLayout(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            lp.leftMargin = 6;
            lp.rightMargin = 10;
            lv.setLayoutParams(lp);
            lv.setOrientation(LinearLayout.HORIZONTAL);
            int size = i==0?6:1;
            for (int j = 0; j < size; j++) {
                int tb_index = i * 6 + j;
                ToggleButton tv1 = new ToggleButton(getActivity());
                Log.d("test", tb_id+"");
                tv1.setId(tb_id++);
                tv1.setText(three_diff_content[tb_index]);
                tv1.setTextOff(three_diff_content[tb_index]);
                tv1.setTextOn(three_diff_content[tb_index]);
                tv1.setPadding(10, 5, 10, 5);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(this.getResources().getColor(R.color.footer_black));
                tv1.setBackgroundResource(R.drawable.k3_btn_toggle_bg);
                tv1.setChecked(bool[tb_index]);
                LinearLayout.LayoutParams tp = new LayoutParams(0, LayoutParams.MATCH_PARENT,
                        1);
                tp.leftMargin = 4;
                lv.addView(tv1, tp);

                tv1.setOnClickListener(new MyClick(tb_index));
                mActivity.tbArrays[tb_id-1] = tv1;
            }

            mLvMatch.addView(lv);

        }

    }
    private void _drawViewTwoDif() {
        int tb_id = 48;
        LinearLayout lv0 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp0.topMargin = 10;
        lp0.leftMargin = 10;
        lp0.rightMargin = 10;
        lv0.setLayoutParams(lp0);
        lv0.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(getActivity());
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
        tv.setText(getResources().getString(R.string.k3_two_diff_num));
        tv.setTextColor(getResources().getColor(R.color.tc_default));
        tv.setGravity(Gravity.LEFT);
        lv0.addView(tv);
        mLvMatch.addView(lv0);

        LinearLayout lv = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.leftMargin = 6;
        lp.rightMargin = 10;
        lv.setLayoutParams(lp);
        lv.setOrientation(LinearLayout.HORIZONTAL);
        for (int j = 0; j < 6; j++) {
            int tb_index = j;
            ToggleButton tv1 = new ToggleButton(getActivity());
            Log.d("test", tb_id+"");
            tv1.setId(tb_id++);
            tv1.setText(three_diff_content[tb_index]);
            tv1.setTextOff(three_diff_content[tb_index]);
            tv1.setTextOn(three_diff_content[tb_index]);
            tv1.setPadding(10, 5, 10, 5);
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextColor(this.getResources().getColor(R.color.footer_black));
            tv1.setBackgroundResource(R.drawable.k3_btn_toggle_bg);
            tv1.setChecked(bool[tb_index]);
            LinearLayout.LayoutParams tp = new LayoutParams(0, LayoutParams.MATCH_PARENT,
                    1);
            tp.leftMargin = 4;
            lv.addView(tv1, tp);

            tv1.setOnClickListener(new MyClick(tb_index));
            mActivity.tbArrays[tb_id-1] = tv1;
        }

        mLvMatch.addView(lv);

    }
    private void _drawViewDrag(String hintStr) {
        int drag_index = 0;
        for (int i = 0; i < 2; i++) {
            switch(i) {
                case 0:
                    LinearLayout lv = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.topMargin = 10;
                    lp.leftMargin = 10;
                    lp.rightMargin = 10;
                    lv.setLayoutParams(lp);
                    lv.setOrientation(LinearLayout.VERTICAL);
                    TextView tv = new TextView(getActivity());
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
                    tv.setText(getResources().getString(R.string.k3_danma));
                    tv.setTextColor(getResources().getColor(R.color.tc_default));
                    tv.setGravity(Gravity.LEFT);
                    lv.addView(tv);
                    mLvMatch.addView(lv);
                    break;
                case 1:
                    LinearLayout lv2 = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp2.topMargin = 10;
                    lp2.leftMargin = 10;
                    lp2.rightMargin = 10;
                    lv2.setLayoutParams(lp2);
                    lv2.setOrientation(LinearLayout.HORIZONTAL);
                    lv2.setGravity(Gravity.LEFT);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_star, 0, 0, 0);
                    tv2.setText(getResources().getString(R.string.k3_tuoma));
                    tv2.setTextColor(getResources().getColor(R.color.tc_default));
                    lv2.addView(tv2);
                    mLvMatch.addView(lv2);
                    break;
                default:
                    break;
            }
            LinearLayout lv = new LinearLayout(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            lp.leftMargin = 6;
            lp.rightMargin = 10;
            lv.setLayoutParams(lp);
            lv.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 6; j++) {
                int j_index = drag_index++;
                int tb_index = j;
                ToggleButton tv1 = new ToggleButton(getActivity());
                tv1.setText(three_diff_content[tb_index]);
                tv1.setTextOff(three_diff_content[tb_index]);
                tv1.setTextOn(three_diff_content[tb_index]);
                tv1.setPadding(10, 5, 10, 5);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(this.getResources().getColor(R.color.footer_black));
                tv1.setBackgroundResource(R.drawable.k3_btn_toggle_bg);
                tv1.setChecked(bool[j_index]);
                LinearLayout.LayoutParams tp = new LayoutParams(0, LayoutParams.MATCH_PARENT,
                        1);
                tp.leftMargin = 4;
                lv.addView(tv1, tp);

                tv1.setOnClickListener(new MyClick(j_index));
                if(mActivity.mCaseIndex == 5) {//三不同号胆拖
                    mActivity.tbDragArrays_three[j_index] = tv1;
                } else if(mActivity.mCaseIndex == 6) {//两不同号胆拖
                    mActivity.tbDragArrays_two[j_index] = tv1;
                }
            }

            mLvMatch.addView(lv);

        }
        LinearLayout ruleLV = new LinearLayout(getActivity());
        LinearLayout.LayoutParams rulelp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rulelp.topMargin = 20;
        rulelp.leftMargin = 10;
        rulelp.rightMargin = 10;
        ruleLV.setLayoutParams(rulelp);
        ruleLV.setOrientation(LinearLayout.HORIZONTAL);
        TextView ruleHint = new TextView(getActivity());
        ruleHint.setTextColor(this.getResources().getColor((R.color.red)));
        ruleHint.setText(hintStr);
        ruleLV.addView(ruleHint);
        mLvMatch.addView(ruleLV);
    }

    class MyClick implements OnClickListener {
        int position;

        public MyClick(int position) {
            super();
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ToggleButton tb = (ToggleButton) v;
            if (mActivity.mCaseIndex == 5) {// 三不同号胆拖
                if (position < 6) {
                    int count = 0;
                    for (int i = 0; i < 6; i++) {
                        if (i != position
                                && mActivity.tbDragArrays_three[i].isChecked()) {
                            count++;
                            if (count >= 2) {
                                mActivity.tbDragArrays_three[position]
                                        .setChecked(false);
                                bool[position] = false;
                                Toast.makeText(mActivity, "此玩法最多只能存在2个胆码",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    bool[position] = tb.isChecked();
                    bool[position+6] = false;
                    mActivity.tbDragArrays_three[position+6].setChecked(false);
                } else {
                    if (mActivity.tbDragArrays_three[position % 6].isChecked()) {
                        mActivity.tbDragArrays_three[position].setChecked(true);
                        mActivity.tbDragArrays_three[position % 6]
                                .setChecked(false);
                        bool[position % 6] = false;
                        bool[position] = true;
                    } else {
                        bool[position] = tb.isChecked();
                    }
                }
            } else if (mActivity.mCaseIndex == 6) {// 两不同号胆拖
                if (position < 6) {
                    for (int i = 0; i < 6; i++) {
                        if (i != position
                                && mActivity.tbDragArrays_two[i].isChecked()) {
                            mActivity.tbDragArrays_two[position]
                                    .setChecked(false);
                            bool[position] = false;
                            Toast.makeText(mActivity, "此玩法最多只能存在1个胆码",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    bool[position] = tb.isChecked();
                    bool[position+6] = false;
                    mActivity.tbDragArrays_two[position+6].setChecked(false);
                } else {
                    if (mActivity.tbDragArrays_two[position % 6].isChecked()) {
                        mActivity.tbDragArrays_two[position].setChecked(true);
                        mActivity.tbDragArrays_two[position % 6]
                                .setChecked(false);
                        bool[position % 6] = false;
                        bool[position] = true;
                    } else {
                        bool[position] = tb.isChecked();
                    }
                }
            } else if(mActivity.mCaseIndex == 2) {//两同号
                if(position <6 && mActivity.tbArrays[position+29].isChecked() ) {
                    mActivity.tbArrays[position+23].setChecked(false);
                    bool[position] = false;
                } else if(position <12 && position > 5 &&  mActivity.tbArrays[position+17].isChecked())  {
                    mActivity.tbArrays[position+23].setChecked(false);
                    bool[position] = false;
                } else {
                    bool[position] = tb.isChecked();
                }
            } else {
                bool[position] = tb.isChecked();
            }
            computeZhushu();
        }

    }
    public void computeZhushu() {
        if(mGameType.equals(K3Type.hezhi.toString())) {
            int count = 0;
            for(int i=0; i<r_content.length; i++) {
                if(bool[i]) {
                    count++;
                }
            }
            mZhushu = count;
            //mActivity.setTouzhuResult(count);
        } else if(mGameType.equals(K3Type.threesamesingle.toString())) {;
            int count = 0;
            for(int i=0; i<three_same_content.length; i++) {
                if(bool[i]) {
                    count++;
                }
            }
            mZhushu = count;
            //mActivity.setTouzhuResult(mZhushu);
        } else if(mGameType.equals(K3Type.twosamesingle.toString())) {
            int count = 0;
            int same = 0;
            int diff = 0;
            for(int i=12; i<two_same_content.length; i++) {
                if(bool[i]) {
                    count++;
                }
            }
            for(int i=0; i<6; i++) {
                if(bool[i]) {
                    same++;
                }
            }
            for(int i=6; i<12; i++) {
                if(bool[i]) {
                    diff++;
                }
            }
            mZhushu = count + same*diff;
            //mActivity.setTouzhuResult(mZhushu);
        } else if(mGameType.equals(K3Type.threedifsingle.toString())) {
            int count = 0;
            for(int i=0; i<three_diff_content.length - 1; i++) {
                if(bool[i]) {
                    count++;
                }
            }
            int tongCount = bool[three_diff_content.length - 1]?1:0;
            mZhushu = Utils.getZuHeNum(count, 3) + tongCount;
            //mActivity.setTouzhuResult(mZhushu);
        } else if(mGameType.equals(K3Type.twodif.toString())) {
            int count = 0;
            for(int i=0; i<three_diff_content.length - 1; i++) {
                if(bool[i]) {
                    count++;
                }
            }
            mZhushu = Utils.getZuHeNum(count, 2);
            //mActivity.setTouzhuResult(mZhushu);
        } else if(mGameType.equals(K3Type.dragthree.toString())) {
            int danCount = 0;
            int tuoCount = 0;
            for(int i=0; i<6; i++) {
                if(bool[i]) {
                    danCount++;
                }
            }
            for(int i=6;i<12; i++) {
                if(bool[i]) {
                    tuoCount++;
                }
            }
            if(danCount == 0) {
                mZhushu = 0;
            } else {
                mZhushu = Utils.getZuHeNum(tuoCount, 3-danCount);
            }
        } else {
            int danCount = 0;
            int tuoCount = 0;
            for(int i=0; i<6; i++) {
                if(bool[i]) {
                    danCount++;
                }
            }
            for(int i=6;i<12; i++) {
                if(bool[i]) {
                    tuoCount++;
                }
            }
            mZhushu = danCount *tuoCount;
        }
        mActivity.setTouzhuResult(mZhushu);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            computeZhushu();
        }
    }
    public void clearTouzhu() {
        if(mGameType.equals(K3Type.hezhi.toString())) {
            //bool = new boolean[r_content.length];
            for(int i=0; i<r_content.length; i++) {
                bool[i] = false;
                mActivity.tbArrays[i].setChecked(false);
            }
        } else if(mGameType.equals(K3Type.threesamesingle.toString())) {
            //bool = new boolean[three_same_content.length];
            for(int i=0; i<three_same_content.length; i++) {
                bool[i] = false;
                mActivity.tbArrays[i+16].setChecked(false);
            }
        } else if(mGameType.equals(K3Type.twosamesingle.toString())) {
            //bool = new boolean[two_same_content.length];
            for(int i=0; i<two_same_content.length; i++) {
                bool[i] = false;
                mActivity.tbArrays[i+23].setChecked(false);
            }
        } else if(mGameType.equals(K3Type.threedifsingle.toString())) {
            //bool = new boolean[three_diff_content.length];
            for(int i=0; i<three_diff_content.length; i++) {
                bool[i] = false;
                mActivity.tbArrays[i+41].setChecked(false);
            }
        } else if(mGameType.equals(K3Type.twodif.toString())) {
            //bool = new boolean[three_diff_content.length];
            for(int i=0; i<three_diff_content.length - 1; i++) {
                bool[i] = false;
                mActivity.tbArrays[i+48].setChecked(false);
            }
        } else if(mGameType.equals(K3Type.dragthree.toString())) {
            for(int i=0; i<mActivity.tbDragArrays_three.length; i++) {
                bool[i] = false;
                mActivity.tbDragArrays_three[i].setChecked(false);
            }
        } else {
            for(int i=0; i<mActivity.tbDragArrays_three.length; i++) {
                bool[i] = false;
                mActivity.tbDragArrays_two[i].setChecked(false);
            }
        }
        computeZhushu();
    }
    public String betTypes = "";
    public String betResult = "";
    public void  getBetResult() {
        if(mGameType.equals(K3Type.threesamesingle.toString())) {
            StringBuilder builder = new StringBuilder();
            String tongxuan = "";
            for(int i=0; i<three_same_content.length; i++) {//length = (1-6) +1
                if(bool[i]) {
                    if(i==6) {
                        tongxuan = "三同号通选";
                    } else {
                        builder.append(three_same_content[i].substring(0, three_same_content[i].indexOf(" ")) + " ");
                    }
                }
            }
            if(TextUtils.isEmpty(tongxuan)) {
                betResult = (builder.toString().trim());
                betTypes = K3Type.threesamesingle.toString();
            } else if(TextUtils.isEmpty(builder.toString().trim())) {
                betResult = tongxuan;
                betTypes =  K3Type.threesamedouble.toString() + " ";
            } else {
                betResult = (builder.toString().trim()+ "-" + tongxuan);
                betTypes = K3Type.threesamesingle.toString() + " " +  K3Type.threesamedouble.toString();
            }
            betTypes = betTypes.trim();
        } else if(mGameType.equals(K3Type.twosamesingle.toString())) {//length = (1-12) + (13-18)
            StringBuilder buildersinglesame = new StringBuilder();
            StringBuilder buildersinglediff = new StringBuilder();
            StringBuilder buildercouple = new StringBuilder();
            for(int i=0; i<two_same_content.length; i++) {
                if(bool[i]) {
                    if(i<=17 && i>=12) {
                        buildercouple.append(two_same_content[i] + " ");
                    } else if(i>=0 && i<=5){
                        buildersinglesame.append(two_same_content[i] + " ");
                    } else {
                        buildersinglediff.append(two_same_content[i] + " ");
                    }
                }
            }
            betTypes = betTypes.trim();
            if(TextUtils.isEmpty(buildercouple.toString().trim())) {
                betResult = buildersinglesame.toString().trim() + "#" + buildersinglediff.toString().trim();
                betTypes = K3Type.twosamesingle.toString();
            } else if(TextUtils.isEmpty(buildersinglesame.toString().trim()) || TextUtils.isEmpty(buildersinglediff.toString().trim())){
                betResult = buildercouple.toString().trim();
                betTypes = K3Type.twosamedouble.toString() + " ";
            } else {
                betResult = buildersinglesame.toString().trim() + "#" + buildersinglediff.toString().trim()+"-"+buildercouple.toString().trim();
                betTypes = K3Type.twosamesingle.toString() + " " + K3Type.twosamedouble.toString() ;
            }
        } else if(mGameType.equals(K3Type.threedifsingle.toString())) {//length = (1-6) +1
            StringBuilder builder = new StringBuilder();
            String tongxuan = "";
            for(int i=0; i<three_diff_content.length; i++) {
                if(bool[i]) {
                    if(i==6) {
                        tongxuan = "三连号通选";
                    } else {
                        builder.append(three_diff_content[i] + " ");
                    }
                }
            }
            if(TextUtils.isEmpty(tongxuan)) {
                betResult = (builder.toString().trim());
                betTypes = K3Type.threedifsingle.toString();
            } else if(TextUtils.isEmpty(builder.toString().trim())) {
                betResult = tongxuan;
                betTypes = K3Type.threedifdouble.toString() + " ";
            } else {
                betResult = (builder.toString().trim()+ "-" + tongxuan);
                betTypes = K3Type.threedifsingle.toString() + " " + K3Type.threedifdouble.toString();
            }
            betTypes = betTypes.trim();
        } else {

            betTypes = mGameType;
            StringBuilder builder = new StringBuilder();
            if(mGameType.equals(K3Type.hezhi.toString())) {
                for(int i=0; i<r_content.length; i++) {
                    if(bool[i]) {
                        builder.append(r_content[i].substring(0, r_content[i].indexOf(" ")) + " ");
                    }
                }
                betResult = builder.toString().trim();
            } else if(mGameType.equals(K3Type.twodif.toString())) {
                for(int i=0; i<three_diff_content.length - 1; i++) {
                    if(bool[i]) {
                        builder.append(three_diff_content[i] + " ");
                    }
                }
                betResult = builder.toString().trim();
            } else{
                StringBuilder danBuilder = new StringBuilder();
                StringBuilder tuoBuilder = new StringBuilder();
                for(int i=0; i<6; i++) {
                    if(bool[i]) {
                        danBuilder.append((i+1) + " ");
                    }
                }
                for(int i=6; i<12; i++) {
                    if(bool[i]) {
                        tuoBuilder.append((i-5) + " ");
                    }
                }
                betResult = danBuilder.toString().trim() + "#"+tuoBuilder.toString().trim();
            }
        }
    }
    public ArrayList<String>getTouZhuList() {
        ArrayList<String>list = new ArrayList<String>();

        return list;
    }

    public void updateChoice(ArrayList<Boolean> updateData, String gameType)
    {
        if (!mGameType.equals(gameType))
        {
            return;
        }

        for (int i = 0; i < updateData.size(); i++) {
            changedAfterWatchChart = changedAfterWatchChart && (bool[i] == updateData.get(i));

            bool[i] = updateData.get(i);
        }

        handler.sendEmptyMessage(UPDATE);
    }
}
