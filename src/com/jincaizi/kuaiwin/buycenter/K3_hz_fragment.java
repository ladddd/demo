package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.List;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import com.jincaizi.R;
import com.jincaizi.common.IntentData;
import com.jincaizi.common.NumberUtil;
import com.jincaizi.kuaiwin.buycenter.adapter.*;
import com.jincaizi.kuaiwin.tool.K3Random;
import com.jincaizi.kuaiwin.utils.Constants.K3Type;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.AnimationController;
import com.jincaizi.kuaiwin.widget.ExpandableHeightGridView;

public class K3_hz_fragment extends Fragment{
    public static final String TAG_HZ ="K3_hz_fragment";
    public static final String TAG_THREE_SAME ="K3_threesame_fragment";
    public static final String TAG_TWO_SAME ="K3_twosame_fragment";
    public static final String TAG_THREE_DIF ="K3_threedif_fragment";
    public static final String TAG_TWO_DIF ="K3_twodif_fragment";
    public static final String TAG_THREE_DIF_DRAG ="K3_threedif_drag_fragment";
    public static final String TAG_TWO_DIF_DRAG ="K3_twodif_drag_fragment";
    public static final String BETTYPE ="bettype";

    private static final int TYPE_BIG = 0;
    private static final int TYPE_SMALL = 1;
    private static final int TYPE_ODD = 2;
    private static final int TYPE_EVEN = 3;
    private static final int TYPE_BIG_ODD = 4;
    private static final int TYPE_BIG_EVEN = 5;
    private static final int TYPE_SMALL_ODD = 6;
    private static final int TYPE_SMALL_EVEN = 7;

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
    private ArrayList<Integer> alreadySelected = new ArrayList<Integer>();

    private LinearLayout mLvMatch;
    private String mGameType;
    private K3 mActivity;
    private AnimationDrawable anim1;
    private AnimationDrawable anim2;
    private AnimationDrawable anim3;
    public Dialog myDialog;
    private RelativeLayout rv_anim;
    public int mZhushu = 0;

    private Vibrator vibrator;

    private KSSumAdapter sumAdapter;
    private ThreeSameAdapter threeSameAdapter;
    private ChooseCommonAdapter threeDiffAdapter;
    private TwoSameAdapter twoSameFirstAdapter;
    private TwoSameAdapter twoSameSecondAdapter;
    private TwoSameAdapter twoSameMultiAdapter;
    private ChooseCommonAdapter twoDiffAdapter;

    private KSDragAdapter dragFirstAdapter;
    private KSDragAdapter dragSecondAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        mGameType = bundle.getString(BETTYPE);
        if (bundle.getSerializable(IntentData.SELECT_NUMBERS) != null)
        {
            alreadySelected = (ArrayList<Integer>)bundle.getSerializable(IntentData.SELECT_NUMBERS);
        }
        else
        {
            alreadySelected.clear();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (K3)getActivity();
        vibrator = (Vibrator) getActivity().getApplication()
                .getSystemService(Service.VIBRATOR_SERVICE);
    }
    private void _initBool(int size) {
        if (alreadySelected != null && alreadySelected.size() > 0)
        {
            for (int i = 0; i < size; i++)
            {
                bool[i] = alreadySelected.contains(i);
            }
        }
        else
        {
            for (int i = 0; i < size; i++)
            {
                bool[i] = false;
            }
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

    public void _showDiceFrame(int caseIndex) {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

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
                    updateSelection();
                    myDialog.dismiss();
                    myDialog = null;
                    break;
                case UPDATE:
                    updateSelection();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private void updateSelection()
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        if(mActivity.mCaseIndex == 1)
        {
            threeSameAdapter.notifyDataSetChanged();
            mLvMatch.findViewById(R.id.special_choose).setSelected(bool[6]);
        }
        else if(mActivity.mCaseIndex == 2)
        {
            twoSameFirstAdapter.notifyDataSetChanged();
            twoSameSecondAdapter.notifyDataSetChanged();
            twoSameMultiAdapter.notifyDataSetChanged();
        }
        else if(mActivity.mCaseIndex == 3)
        {
            threeDiffAdapter.notifyDataSetChanged();
            mLvMatch.findViewById(R.id.special_choose).setSelected(bool[6]);
        }
        else if(mActivity.mCaseIndex == 4)
        {
            twoDiffAdapter.notifyDataSetChanged();
        }
        else if (mActivity.mCaseIndex != 5 && mActivity.mCaseIndex != 6)
        {
            sumAdapter.notifyDataSetChanged();
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
            _drawViewDrag(true);
        } else {
            //bool = new boolean[three_diff_content.length];
            _initBool(12);
            _drawViewDrag(false);
        }
    }

    private void _drawViewHZ()
    {
        final LinearLayout body = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).
                inflate(R.layout.ks_sum_main, null);
        ExpandableHeightGridView gridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_main);
        sumAdapter = new KSSumAdapter(this, bool);
        gridView.setAdapter(sumAdapter);
        gridView.setExpanded(true);

        RelativeLayout randomLayout = (RelativeLayout) body.findViewById(R.id.shake_random_layout);
        randomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDiceFrame(mActivity.mCaseIndex);
            }
        });

        final TextView big = (TextView) body.findViewById(R.id.big);
        final TextView small = (TextView) body.findViewById(R.id.small);
        final TextView odd = (TextView) body.findViewById(R.id.odd);
        final TextView even = (TextView) body.findViewById(R.id.even);

        big.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                boolean isOdd = odd.isSelected();
                boolean isEven = even.isSelected();
                if (isOdd)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = ((i % 2) == 0) && (i > 7 || !view.isSelected());
                    }
                }
                else if (isEven)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = ((i % 2) == 1) && (i > 7 || !view.isSelected());
                    }
                }
                else
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = (i > 7) && view.isSelected();
                    }
                }

                if (view.isSelected()) {
                    small.setSelected(false);
                }

                handler.sendEmptyMessage(UPDATE);
            }
        });

        small.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                boolean isOdd = odd.isSelected();
                boolean isEven = even.isSelected();

                if (isOdd)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = ((i % 2) == 0) && (i <= 7 || !view.isSelected());
                    }
                }
                else if (isEven)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = ((i % 2) == 1) && (i <= 7 || !view.isSelected());
                    }
                }
                else
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = (i <= 7) && view.isSelected();
                    }
                }

                if (view.isSelected()) {
                    big.setSelected(false);
                }

                handler.sendEmptyMessage(UPDATE);
            }
        });

        odd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                boolean isBig = big.isSelected();
                boolean isSmall = small.isSelected();

                if (isBig)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] =  (i > 7) && (((i % 2) == 0) || !view.isSelected());
                    }
                }
                else if (isSmall)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] =  (i <= 7) && (((i % 2) == 0) || !view.isSelected());
                    }
                }
                else
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = ((i % 2) == 0) && view.isSelected();
                    }
                }

                if (view.isSelected())
                {
                    even.setSelected(false);
                }
                handler.sendEmptyMessage(UPDATE);
            }
        });

        even.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                boolean isBig = big.isSelected();
                boolean isSmall = small.isSelected();

                if (isBig)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] =  (i > 7) && (((i % 2) == 1) || !view.isSelected());
                    }
                }
                else if (isSmall)
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] =  (i <= 7) && (((i % 2) == 1) || !view.isSelected());
                    }
                }
                else
                {
                    for (int i = 0; i < 16; i++) {
                        bool[i] = ((i % 2) == 1) && view.isSelected();
                    }
                }

                if (view.isSelected())
                {
                    odd.setSelected(false);
                }

                handler.sendEmptyMessage(UPDATE);
            }
        });

        mLvMatch.addView(body);
    }

    private void _drawViewThreeSame() {

        final LinearLayout body = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).
                inflate(R.layout.ks_three_equal_main, null);

        ExpandableHeightGridView gridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_main);
        threeSameAdapter = new ThreeSameAdapter(this, bool);
        gridView.setAdapter(threeSameAdapter);
        gridView.setExpanded(true);

        RelativeLayout randomLayout = (RelativeLayout) body.findViewById(R.id.shake_random_layout);
        randomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDiceFrame(mActivity.mCaseIndex);
            }
        });

        LinearLayout specialChoose = (LinearLayout) body.findViewById(R.id.special_choose);
        specialChoose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                bool[6] = view.isSelected();
                handler.sendEmptyMessage(UPDATE);
            }
        });
        specialChoose.setSelected(bool[6]);

        mLvMatch.addView(body);
    }

    private void _drawViewTwoSame() {
        final LinearLayout body = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).
                inflate(R.layout.ks_two_same_main, null);

        ExpandableHeightGridView firstGridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_main);
        ExpandableHeightGridView secondGridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_sub);
        ExpandableHeightGridView multiGridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_multi);

        twoSameFirstAdapter = new TwoSameAdapter(this, bool, TwoSameAdapter.SINGLE_SELECT_SAME);
        twoSameSecondAdapter = new TwoSameAdapter(this, bool, TwoSameAdapter.SINGLE_SELECT_DIFF);
        twoSameMultiAdapter = new TwoSameAdapter(this, bool, TwoSameAdapter.MULTI_SELECT);

        firstGridView.setAdapter(twoSameFirstAdapter);
        firstGridView.setExpanded(true);
        secondGridView.setAdapter(twoSameSecondAdapter);
        secondGridView.setEnabled(true);
        multiGridView.setAdapter(twoSameMultiAdapter);
        multiGridView.setExpanded(true);

        RelativeLayout randomLayout = (RelativeLayout) body.findViewById(R.id.shake_random_layout);
        randomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDiceFrame(mActivity.mCaseIndex);
            }
        });

        mLvMatch.addView(body);
    }


    private void _drawViewThreeDif() {
        final LinearLayout body = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).
                inflate(R.layout.ks_three_diff_main, null);

        ExpandableHeightGridView gridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_main);
        threeDiffAdapter = new ChooseCommonAdapter(this, bool);
        threeDiffAdapter.setMissCount(((K3)getActivity()).getCurrentMiss());
        gridView.setAdapter(threeDiffAdapter);
        gridView.setExpanded(true);

        RelativeLayout randomLayout = (RelativeLayout) body.findViewById(R.id.shake_random_layout);
        randomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDiceFrame(mActivity.mCaseIndex);
            }
        });

        LinearLayout specialChoose = (LinearLayout) body.findViewById(R.id.special_choose);
        specialChoose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                bool[6] = view.isSelected();
                handler.sendEmptyMessage(UPDATE);
            }
        });
        specialChoose.setSelected(bool[6]);

        mLvMatch.addView(body);
    }
    private void _drawViewTwoDif() {
        final LinearLayout body = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).
                inflate(R.layout.ks_two_diff_main, null);

        ExpandableHeightGridView gridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_main);
        twoDiffAdapter = new ChooseCommonAdapter(this, bool);
        twoDiffAdapter.setMissCount(((K3)getActivity()).getCurrentMiss());
        gridView.setAdapter(twoDiffAdapter);
        gridView.setExpanded(true);

        RelativeLayout randomLayout = (RelativeLayout) body.findViewById(R.id.shake_random_layout);
        randomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDiceFrame(mActivity.mCaseIndex);
            }
        });

        mLvMatch.addView(body);

    }
    private void _drawViewDrag(boolean isThreeDrag) {
        final LinearLayout body = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).
                inflate(R.layout.ks_drag_main, null);

        if (mActivity.mCaseIndex == 6)
        {
            ((TextView) body.findViewById(R.id.text_first)).setText("选两个不同号码，猜中开奖的任意两位即中");
            ((TextView) body.findViewById(R.id.text_second)).setText("8");
        }

        ExpandableHeightGridView firstGridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_main);
        ExpandableHeightGridView secondGridView = (ExpandableHeightGridView)body.findViewById(R.id.selector_sub);

        dragFirstAdapter = new KSDragAdapter(this, bool, KSDragAdapter.FIRST,
                isThreeDrag? KSDragAdapter.THREE_DRAG: KSDragAdapter.TWO_DRAG);
        dragSecondAdapter = new KSDragAdapter(this, bool, KSDragAdapter.SECOND);
        dragFirstAdapter.setMissCount(((K3)getActivity()).getCurrentMiss());
        dragSecondAdapter.setMissCount(((K3)getActivity()).getCurrentMiss());

        firstGridView.setAdapter(dragFirstAdapter);
        firstGridView.setExpanded(true);
        secondGridView.setAdapter(dragSecondAdapter);
        secondGridView.setEnabled(true);

        mLvMatch.addView(body);
    }

    public void computeZhushu() {
        mZhushu = 0;

        int max = 0;
        int min = 0;

        if (mGameType.equals(K3Type.hezhi.toString()))
        {
            for (int i = 0; i < 16; i++) {
                if (bool[i]) {
                    mZhushu++;
                    if (max == 0) {
                        min = NumberUtil.getSumLotteryMoney(i);
                        max = NumberUtil.getSumLotteryMoney(i);
                    } else {
                        max = Math.max(max, NumberUtil.getSumLotteryMoney(i));
                        min = Math.min(min, NumberUtil.getSumLotteryMoney(i));
                    }
                }
            }
            setSpecial();
        }
        else if (mGameType.equals(K3Type.threesamesingle.toString()))
        {
            for (int i = 0; i < 6; i++)
            {
                if (bool[i])
                {
                    min = 240;
                    max = 240;
                    mZhushu++;
                }
            }

            if (bool[6])
            {
                min = 40;
                mZhushu++;
            }

            //全选
            if (mZhushu == 7)
            {
                min = 280;
                max = 280;
            }
        }
        else if (mGameType.equals(K3Type.twosamesingle.toString()))
        {
            int sameCount = 0;
            int diffCount = 0;
            boolean bonus = false;

            for (int i = 0; i < 6; i++) {
                if (bool[i])
                {
                    sameCount++;
                    if (bool[i + 12])
                    {
                        bonus = true;
                    }
                }
                else if (bool[i + 6])
                {
                    diffCount++;
                }
                if (bool[i + 12])
                {
                    mZhushu++;
                }
            }
            if (mZhushu > 0)
            {
                min = 15;
                max = 15;

                if (sameCount * diffCount != 0)
                {
                    max = bonus?95:80;
                }
            }
            else
            {
                min = (sameCount * diffCount == 0)?0:80;
                max = min;
            }

            mZhushu += sameCount * diffCount;
        }
        else if (mGameType.equals(K3Type.threedifsingle.toString()))
        {
            int selection = 0;

            for (int i = 0; i < 6; i++)
            {
                if (bool[i])
                {
                    min = 40;
                    max = 40;
                    selection++;
                }
            }

            mZhushu = Utils.getZuHeNum(selection, 3) + (bool[6]?1:0);

            if (bool[6])
            {
                min = 10;
                selection++;
            }

            //全选
            if (selection == 7)
            {
                min = 40;
                max = 50;
            }
        }
        else if (mGameType.equals(K3Type.twodif.toString()))
        {
            for (int i = 0; i < 6; i++)
            {
                if (bool[i])
                {
                    min = 8;
                    max = 8;
                    mZhushu++;
                }
            }

            mZhushu = Utils.getZuHeNum(mZhushu, 2);
        }
        else if (mGameType.equals(K3Type.dragthree.toString()))
        {
            int dragFirst = 0;
            int dragSecond = 0;
            for(int i=0; i<6; i++) {
                if(bool[i]) {
                    dragFirst++;
                }
                else if (bool[i + 6])
                {
                    dragSecond++;
                }
            }

            if(dragFirst == 0) {
                mZhushu = 0;
            } else {
                mZhushu = Utils.getZuHeNum(dragSecond, 3-dragFirst);
                min = 40;
                max = min;
            }
        }
        else if (mGameType.equals(K3Type.dragtwo.toString()))
        {
            int dragFirst = 0;
            int dragSecond = 0;

            for (int i = 0; i < 6; i++) {
                if (bool[i])
                {
                    dragFirst++;
                }
                else if (bool[i + 6])
                {
                    dragSecond++;
                }
            }

            mZhushu = dragFirst * dragSecond;

            min = 8;
            max = min;
        }

        mActivity.setBuyTips(max, min, mZhushu);
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
            }
            setSpecial();
            sumAdapter.notifyDataSetChanged();
        } else if(mGameType.equals(K3Type.threesamesingle.toString())) {
            //bool = new boolean[three_same_content.length];
            for(int i=0; i<three_same_content.length; i++) {
                bool[i] = false;
            }

            mLvMatch.findViewById(R.id.special_choose).setSelected(false);
            threeSameAdapter.notifyDataSetChanged();
        } else if(mGameType.equals(K3Type.twosamesingle.toString())) {
            //bool = new boolean[two_same_content.length];
            for(int i=0; i<two_same_content.length; i++) {
                bool[i] = false;
            }

            twoSameFirstAdapter.notifyDataSetChanged();
            twoSameSecondAdapter.notifyDataSetChanged();
            twoSameMultiAdapter.notifyDataSetChanged();
        } else if(mGameType.equals(K3Type.threedifsingle.toString())) {
            //bool = new boolean[three_diff_content.length];
            for(int i=0; i<three_diff_content.length; i++) {
                bool[i] = false;
            }

            mLvMatch.findViewById(R.id.special_choose).setSelected(false);
            threeDiffAdapter.notifyDataSetChanged();
        } else if(mGameType.equals(K3Type.twodif.toString())) {
            //bool = new boolean[three_diff_content.length];
            for(int i=0; i<three_diff_content.length - 1; i++) {
                bool[i] = false;
            }

            twoDiffAdapter.notifyDataSetChanged();
        } else if(mGameType.equals(K3Type.dragthree.toString()) ||
                mGameType.equals(K3Type.dragtwo.toString()))
        {
            for (int i = 0; i < mActivity.tbDragArrays_three.length; i++) {
                bool[i] = false;
            }

            dragFirstAdapter.notifyDataSetChanged();
            dragSecondAdapter.notifyDataSetChanged();
        }

        computeZhushu();
    }

    public String betTypes = "";
    public String betResult = "";
    public ArrayList<Integer> selectedNumber = new ArrayList<Integer>();
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
                    selectedNumber.add(i);
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
            boolean hasSame = false;
            boolean hasDiff = false;
            for(int i=0; i<two_same_content.length; i++) {
                if(bool[i]) {
                    if(i<=17 && i>=12) {
                        buildercouple.append(two_same_content[i] + " ");
                        selectedNumber.add(i);
                    } else if(i>=0 && i<=5){
                        hasSame = true;
                        buildersinglesame.append(two_same_content[i] + " ");
                    } else {
                        hasDiff = true;
                        buildersinglediff.append(two_same_content[i] + " ");
                    }
                }
            }
            if (hasSame && hasDiff)
            {
                for (int i = 0; i < 12; i++)
                {
                    if (bool[i]) {
                        selectedNumber.add(i);
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
                    selectedNumber.add(i);
                }
            }
            String[] result = builder.toString().split(" ");
            if(TextUtils.isEmpty(tongxuan)) {
                betResult = (builder.toString().trim());
                betTypes = K3Type.threedifsingle.toString();
            } else if(result.length < 3) {
                betResult = tongxuan;
                betTypes = K3Type.threedifdouble.toString() + " ";
                selectedNumber.clear();
                selectedNumber.add(6);
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
                        selectedNumber.add(i);
                    }
                }
                betResult = builder.toString().trim();
            } else if(mGameType.equals(K3Type.twodif.toString())) {
                for(int i=0; i<three_diff_content.length - 1; i++) {
                    if(bool[i]) {
                        builder.append(three_diff_content[i] + " ");
                        selectedNumber.add(i);
                    }
                }
                betResult = builder.toString().trim();
            } else{
                StringBuilder danBuilder = new StringBuilder();
                StringBuilder tuoBuilder = new StringBuilder();
                for(int i=0; i<6; i++) {
                    if(bool[i]) {
                        danBuilder.append((i+1) + " ");
                        selectedNumber.add(i);
                    }
                }
                for(int i=6; i<12; i++) {
                    if(bool[i]) {
                        tuoBuilder.append((i-5) + " ");
                        selectedNumber.add(i);
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

    /**
     * 从走势图返回后更新选号
     * @param updateData
     * @param gameType
     */
    public void updateChoice(ArrayList<Boolean> updateData, String gameType)
    {
        if (!mGameType.equals(gameType))
        {
            return;
        }

        for (int i = 0; i < updateData.size(); i++) {
            bool[i] = updateData.get(i);
        }

        handler.sendEmptyMessage(UPDATE);
    }

    public ArrayList<Boolean> getSelectionList(String gameType)
    {
        ArrayList<Boolean> list = new ArrayList<Boolean>();

        if (mGameType.equals(gameType))
        {
            if (mGameType.equals(K3Type.hezhi.toString()))
            {
                for (int i = 0; i < 16; i++) {
                    list.add(i, bool[i]);
                }
            }
            else if (mGameType.equals(K3Type.threesamesingle.toString()) ||
                    mGameType.equals(K3Type.threedifsingle.toString()))
            {
                for (int i = 0; i < 7; i++) {
                    list.add(i, bool[i]);
                }
            }
            else if (mGameType.equals(K3Type.twodif.toString()))
            {
                for (int i = 0; i < 6; i++) {
                    list.add(i, bool[i]);
                }
            }
        }

        return list;
    }

    public void updateSumChoice(int position, boolean selected)
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        if (position < 0 || position >= 16)
        {
            return;
        }

        bool[position] = selected;

        computeZhushu();
    }

    public void updateCommonChoice(int position, boolean selected)
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        if (position < 0 || position >= 6)
        {
            return;
        }

        bool[position] = selected;

        computeZhushu();
    }

    public void updateTwoEqualChoice(int position, boolean selected)
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        if (position < 0 || position >= 18)
        {
            return;
        }

        //同号与不同号数字不同
        if (position >= 0 && position < 6 && selected)
        {
            bool[position + 6] = false;
            twoSameSecondAdapter.notifyDataSetChanged();
        }
        else if (position >= 6 && position < 12 && selected)
        {
            bool[position - 6] = false;
            twoSameFirstAdapter.notifyDataSetChanged();
        }

        bool[position] = selected;

        computeZhushu();
    }

    public void updateDragChoice(int position, boolean selected)
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        if (position < 0 || position >= 12)
        {
            return;
        }

        //同号与不同号数字不同
        if (position >= 0 && position < 6 && selected)
        {
            bool[position + 6] = false;
            dragSecondAdapter.notifyDataSetChanged();
        }
        else if (position >= 6 && position < 12 && selected)
        {
            bool[position - 6] = false;
            dragFirstAdapter.notifyDataSetChanged();
        }

        bool[position] = selected;

        computeZhushu();
    }

    private void setSpecial()
    {
        int type = -1;

        List<Integer> selectList = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++) {
            if (bool[i])
            {
                selectList.add(i);
            }
        }

        if (selectList.size() == 4)
        {
            int first = selectList.get(0);
            int second = selectList.get(1);
            int third = selectList.get(2);
            int forth = selectList.get(3);

            if (first == 0 && second == 2 && third == 4 && forth == 6)
            {
                type = TYPE_SMALL_ODD;
            }
            else if (first == 1 && second == 3 && third == 5 && forth == 7)
            {
                type = TYPE_SMALL_EVEN;
            }
            else if (first == 8 && second == 10 && third == 12 && forth == 14)
            {
                type = TYPE_BIG_ODD;
            }
            else if (first == 9 && second == 11 && third == 13 && forth == 15)
            {
                type = TYPE_BIG_EVEN;
            }
        }
        else if (selectList.size() == 8)
        {
            boolean big = true;
            boolean small = true;
            boolean odd = true;
            boolean even = true;

            for (int i = 0; i < 8; i++) {
                if (selectList.get(i) <= 7)
                {
                    big = false;
                }
                else
                {
                    small = false;
                }
                if ((selectList.get(i) % 2) == 1)
                {
                    odd = false;
                }
                else
                {
                    even = false;
                }
            }

            if (big)
            {
                type = TYPE_BIG;
            }
            else if (small)
            {
                type = TYPE_SMALL;
            }
            else if (odd)
            {
                type = TYPE_ODD;
            }
            else if (even)
            {
                type = TYPE_EVEN;
            }
        }

        View bigBtn = mLvMatch.findViewById(R.id.big);
        View smallBtn = mLvMatch.findViewById(R.id.small);
        View oddBtn = mLvMatch.findViewById(R.id.odd);
        View evenBtn = mLvMatch.findViewById(R.id.even);
        switch (type)
        {
            case -1:
                bigBtn.setSelected(false);
                smallBtn.setSelected(false);
                oddBtn.setSelected(false);
                evenBtn.setSelected(false);
                break;
            case TYPE_BIG:
                bigBtn.setSelected(true);
                smallBtn.setSelected(false);
                oddBtn.setSelected(false);
                evenBtn.setSelected(false);
                break;
            case TYPE_SMALL:
                bigBtn.setSelected(false);
                smallBtn.setSelected(true);
                oddBtn.setSelected(false);
                evenBtn.setSelected(false);
                break;
            case TYPE_ODD:
                bigBtn.setSelected(false);
                smallBtn.setSelected(false);
                oddBtn.setSelected(true);
                evenBtn.setSelected(false);
                break;
            case TYPE_EVEN:
                bigBtn.setSelected(false);
                smallBtn.setSelected(false);
                oddBtn.setSelected(false);
                evenBtn.setSelected(true);
                break;
            case TYPE_BIG_ODD:
                bigBtn.setSelected(true);
                smallBtn.setSelected(false);
                oddBtn.setSelected(true);
                evenBtn.setSelected(false);
                break;
            case TYPE_BIG_EVEN:
                bigBtn.setSelected(true);
                smallBtn.setSelected(false);
                oddBtn.setSelected(false);
                evenBtn.setSelected(true);
                break;
            case TYPE_SMALL_ODD:
                bigBtn.setSelected(false);
                smallBtn.setSelected(true);
                oddBtn.setSelected(true);
                evenBtn.setSelected(false);
                break;
            case TYPE_SMALL_EVEN:
                bigBtn.setSelected(false);
                smallBtn.setSelected(true);
                oddBtn.setSelected(false);
                evenBtn.setSelected(true);
                break;
            default:
                break;
        }
    }

    public void notifyLeakUpdate()
    {
        if (!(getActivity() instanceof K3))
        {
            return;
        }

        ArrayList<String> currentMiss = ((K3)getActivity()).getCurrentMiss();

        if (mActivity.mCaseIndex == 3) {
            threeDiffAdapter.setMissCount(currentMiss);
            threeDiffAdapter.notifyDataSetChanged();
        }
        else if (mActivity.mCaseIndex == 4) {
            twoDiffAdapter.setMissCount(currentMiss);
            twoDiffAdapter.notifyDataSetChanged();
        }
        else if (mActivity.mCaseIndex == 5 || mActivity.mCaseIndex == 6) {
            dragFirstAdapter.setMissCount(currentMiss);
            dragFirstAdapter.notifyDataSetChanged();
            dragSecondAdapter.setMissCount(currentMiss);
            dragSecondAdapter.notifyDataSetChanged();
        }

    }
}
