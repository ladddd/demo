package com.jincaizi.kuaiwin.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jincaizi.R;

/**
 * 下拉刷新控件
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

    private static final String TAG = "PullToRefreshListView";

    // 下拉刷新标志
    private static final int PULL_TO_REFRESH = 0;
    // 松开刷新标志
    private static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新标志
    private static final int REFRESHING = 2;
    // 刷新完成标志
    private static final int DONE = 3;

    private LayoutInflater mInflater;

    private LinearLayout mHeadView;
    private TextView mTipsTextview;
    private TextView mLastUpdatedTextView;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    // 用来设置箭头图标动画效果
    private RotateAnimation mAnimation;
    private RotateAnimation mReverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean mIsRecored;

    @SuppressWarnings("unused")
    private int mHeadContentWidth;
    private int mHeadContentHeight;
    private int mHeadContentOriginalTopPadding;

    private int mStartY;
    private int mFirstItemIndex;
    // private int currentScrollState;

    private int mState;

    private boolean mIsBack;

    public OnRefreshListener mRefreshListener;
    

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _init(context);
    }

    public PullToRefreshListView(Context context) {
        super(context);
        _init(context);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void _init(Context context) {
        if (isInEditMode()) {
            return;
        }
        // 设置滑动效果
        mAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setDuration(100);
        mAnimation.setFillAfter(true);

        mReverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseAnimation.setInterpolator(new LinearInterpolator());
        mReverseAnimation.setDuration(100);
        mReverseAnimation.setFillAfter(true);

        mInflater = LayoutInflater.from(context);
        mHeadView = (LinearLayout) mInflater.inflate(R.layout.pull_to_refresh_head, null);

        mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
        mArrowImageView.setMinimumWidth(50);
        mArrowImageView.setMinimumHeight(50);
        mProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
        mTipsTextview = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
        mLastUpdatedTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);

        mHeadContentOriginalTopPadding = mHeadView.getPaddingTop();

        _measureView(mHeadView);
        mHeadContentHeight = mHeadView.getMeasuredHeight();
        mHeadContentWidth = mHeadView.getMeasuredWidth();

        mHeadView.setPadding(mHeadView.getPaddingLeft(), -1 * mHeadContentHeight, mHeadView.getPaddingRight(),
                mHeadView.getPaddingBottom());
        mHeadView.invalidate();

        // System.out.println("初始高度："+mHeadContentHeight);
        // System.out.println("初始TopPad："+mHeadContentOriginalTopPadding);

        // 设置没有点击事件
        addHeaderView(mHeadView, null, false);
        
        setOnScrollListener(this);
        // TODO 取消 android系统的overscroll效果
        // 当滑动到边界的时候，如果再滑动，就会触发这个效果，产生渐变
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk > android.os.Build.VERSION_CODES.FROYO) {
            // 这是 android2.3之后才有的方法！
            setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        mFirstItemIndex = firstVisiableItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // currentScrollState = scrollState;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsListenScroll) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mFirstItemIndex == 0 && !mIsRecored) {
                    mStartY = (int) event.getY();
                    mIsRecored = true;
                    // System.out.println("当前-按下高度-ACTION_DOWN-Y："+mStartY);
                }
                break;

            case MotionEvent.ACTION_CANCEL:// 失去焦点&取消动作
            case MotionEvent.ACTION_UP:

                if (mState != REFRESHING) {
                    if (mState == DONE) {
                        Log.d(TAG, "当前-抬起-ACTION_UP：DONE什么都不做");
                        // System.out.println("当前-抬起-ACTION_UP：DONE什么都不做");
                    } else if (mState == PULL_TO_REFRESH) {
                        mState = DONE;
                        _changeHeaderViewByState();
                        // System.out.println("当前-抬起-ACTION_UP：PULL_TO_REFRESH-->DONE-由下拉刷新状态到刷新完成状态");
                    } else if (mState == RELEASE_TO_REFRESH) {
                        mState = REFRESHING;
                        _changeHeaderViewByState();
                        _onRefresh();
                        // System.out.println("当前-抬起-ACTION_UP：RELEASE_TO_REFRESH-->REFRESHING-由松开刷新状态，到刷新完成状态");
                    }
                }

                mIsRecored = false;
                mIsBack = false;

                break;

            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                // System.out.println("当前-滑动-ACTION_MOVE Y："+tempY);
                if (!mIsRecored && mFirstItemIndex == 0) {
                    // System.out.println("当前-滑动-记录拖拽时的位置 Y："+tempY);
                    mIsRecored = true;
                    mStartY = tempY;
                }
                if (mState != REFRESHING && mIsRecored) {
                    // 可以松开刷新了
                    if (mState == RELEASE_TO_REFRESH) {
                        // 往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖
                        if ((tempY - mStartY < mHeadContentHeight + 20) && (tempY - mStartY) > 0) {
                            mState = PULL_TO_REFRESH;
                            _changeHeaderViewByState();
                            // System.out.println("当前-滑动-ACTION_MOVE：RELEASE_TO_REFRESH--》PULL_TO_REFRESH
                            // -由松开刷新状态转变到下拉刷新状态");
                        } else if (tempY - mStartY <= 0) {// 一下子推到顶
                            mState = DONE;
                            _changeHeaderViewByState();
                            // System.out.println("当前-滑动-ACTION_MOVE：RELEASE_TO_REFRESH--》DONE-由松开刷新状态转变到done状态");
                        }
                        // 往下拉，或者还没有上推到屏幕顶部掩盖head
                        // else {
                        // 不用进行特别的操作，只用更新paddingTop的值就行了
                        // }
                    } else if (mState == PULL_TO_REFRESH) {// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        // && currentScrollState == SCROLL_STATE_TOUCH_SCROLL
                        if (tempY - mStartY >= mHeadContentHeight + 20) {
                            mState = RELEASE_TO_REFRESH;
                            mIsBack = true;
                            _changeHeaderViewByState();
                            // System.out.println("当前-滑动-PULL_TO_REFRESH--》RELEASE_TO_REFRESH-由done或者下拉刷新状态转变到松开刷新");
                        } else if (tempY - mStartY <= 0) { // 上推到顶了
                            mState = DONE;
                            _changeHeaderViewByState();
                            // System.out.println("当前-滑动-PULL_TO_REFRESH--》DONE-由Done或者下拉刷新状态转变到done状态");
                        }
                    } else if (mState == DONE) {// done状态下
                        if (tempY - mStartY > 0) {
                            mState = PULL_TO_REFRESH;
                            _changeHeaderViewByState();
                            // System.out.println("当前-滑动-DONE--》PULL_TO_REFRESH-由done状态转变到下拉刷新状态");
                        }
                    }

                    // 更新headView的size
                    if (mState == PULL_TO_REFRESH) {
                        int topPadding = ((-1 * mHeadContentHeight + (tempY - mStartY)));
                        mHeadView.setPadding(mHeadView.getPaddingLeft(), topPadding, mHeadView.getPaddingRight(),
                                mHeadView.getPaddingBottom());
                        mHeadView.invalidate();
                        // System.out.println("当前-下拉刷新PULL_To_REFRESH-TopPad："+topPadding);
                    }

                    // 更新headView的paddingTop
                    if (mState == RELEASE_TO_REFRESH) {
                        int topPadding = ((tempY - mStartY - mHeadContentHeight));
                        mHeadView.setPadding(mHeadView.getPaddingLeft(), topPadding, mHeadView.getPaddingRight(),
                                mHeadView.getPaddingBottom());
                        mHeadView.invalidate();
                        // System.out.println("当前-释放刷新RELEASE_To_REFRESH-TopPad："+topPadding);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void _changeHeaderViewByState() {
        switch (mState) {
            case RELEASE_TO_REFRESH:

                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mTipsTextview.setVisibility(View.VISIBLE);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mAnimation);

                mTipsTextview.setText(R.string.pull_to_refresh_release_label);

                // Log.v(TAG, "当前状态，松开刷新");
                break;
            case PULL_TO_REFRESH:

                mProgressBar.setVisibility(View.GONE);
                mTipsTextview.setVisibility(View.VISIBLE);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.VISIBLE);
                if (mIsBack) {
                    mIsBack = false;
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mReverseAnimation);
                }
                mTipsTextview.setText(R.string.pull_to_refresh_pull_label);

                // Log.v(TAG, "当前状态，下拉刷新");
                break;

            case REFRESHING:
                // System.out.println("刷新REFRESHING-TopPad："+mHeadContentOriginalTopPadding);
                mHeadView.setPadding(mHeadView.getPaddingLeft(), mHeadContentOriginalTopPadding,
                        mHeadView.getPaddingRight(), mHeadView.getPaddingBottom());
                mHeadView.invalidate();

                mProgressBar.setVisibility(View.VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.GONE);
                mTipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
                // mLastUpdatedTextView.setVisibility(View.GONE);

                // Log.v(TAG, "当前状态,正在刷新...");
                break;
            case DONE:
                // System.out.println("完成DONE-TopPad："+(-1 * mHeadContentHeight));
                mHeadView.setPadding(mHeadView.getPaddingLeft(), -1 * mHeadContentHeight,
                        mHeadView.getPaddingRight(), mHeadView.getPaddingBottom());
                mHeadView.invalidate();

                mProgressBar.setVisibility(View.GONE);
                mArrowImageView.clearAnimation();
                // 此处更换图标
                mArrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);

                mTipsTextview.setText(R.string.pull_to_refresh_pull_label);
                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                // Log.v(TAG, "当前状态，done");
                break;
            default:
                break;
        }
    }

    // 点击刷新
    public void clickRefresh() {
        Log.v(TAG, "点击刷新");
        setSelection(0);
        mState = REFRESHING;
        _changeHeaderViewByState();
        _onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.mRefreshListener = refreshListener;
    }

    /**
     * 刷新
     *
     * @author kongnan
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public void onRefreshComplete(String update) {
        mLastUpdatedTextView.setText(update);
        onRefreshComplete();
    }

    public void changLastUpdatedText(String update) {
        mLastUpdatedTextView.setText(update);
    }

    public void onRefreshComplete() {
        mState = DONE;
        _changeHeaderViewByState();
    }

    private void _onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    // 计算headView的width及height值
    private void _measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setScrollListenOrNot(boolean listen) {
        if (listen) {
            mIsListenScroll = true;
        } else {
            mIsListenScroll = false;
        }
    }

    private boolean mIsListenScroll = true;
}
