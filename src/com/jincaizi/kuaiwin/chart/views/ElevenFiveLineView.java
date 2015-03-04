package com.jincaizi.kuaiwin.chart.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.jincaizi.R;

/**
 * Created by chenweida on 2015/2/13.
 */
public class ElevenFiveLineView extends RelativeLayout {

    private float itemWidth;
    private float dividerWidth;
    private float itemHeight;
    private float density;
    private boolean showLine = true;

    private Paint paint;
    private Paint textPaint;

    private int totalIssueCount = 0;
    private int[] totalResultList = new int[100];

    public ElevenFiveLineView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        setWillNotDraw(false);

        initPaint();
    }

    public void setShowLine(boolean showLine)
    {
        this.showLine = showLine;
    }

    public void setParam(float itemHeight, int[] totalResultList, int totalIssueCount, boolean showLine)
    {
        //高度设为比item高度小一像素，能够对齐。
        this.itemHeight = itemHeight - 1.0f;
        this.totalResultList = totalResultList;
        this.totalIssueCount = totalIssueCount;
        this.showLine = showLine;

        WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        this.dividerWidth = density * 0.5f;
        this.itemWidth = (manager.getDefaultDisplay().getWidth() - 11 * dividerWidth) / 13;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (totalResultList == null)
        {
            return;
        }
        if (showLine)
        {
            for (int i = 0; i < totalIssueCount - 1; i++) {
                drawLine(i, i+1, canvas);
            }
        }
        for (int i = 0; i < totalIssueCount - 1; i++) {
            drawItem(i, canvas);
        }

        drawItem(totalIssueCount - 1, canvas);
    }

    //TODO 可以更加抽象，把成员变量提出作为参数
    private void drawLine(int startIndex, int endIndex, Canvas canvas)
    {
        float startX = (totalResultList[startIndex] + 1.5f)*itemWidth + totalResultList[startIndex]*dividerWidth;
        float startY = startIndex * itemHeight + itemHeight/2;
        float endX = (totalResultList[endIndex] + 1.5f)*itemWidth + totalResultList[endIndex]*dividerWidth;
        float endY = endIndex * itemHeight + itemHeight/2;
        canvas.drawLine(startX, startY, endX , endY , paint);
    }

    //TODO 可以更加抽象，把成员变量提出作为参数
    private void drawItem(int index, Canvas canvas)
    {
        float top = itemHeight * index;
        float left = (totalResultList[index] + 1)*itemWidth + totalResultList[index]*dividerWidth;
        float bottom = top + itemHeight;
        float right = left + itemWidth;
        canvas.drawOval(new RectF(left, top, right, bottom), paint);

        canvas.drawText(String.valueOf(totalResultList[index]), left + itemWidth/2,
                top + itemHeight/2 + 6*density, textPaint);
    }

    /**
     * 初始化画笔，一个用来绘制绿色的矩形，折线；另一个绘制白色的数字
     */
    private void initPaint()
    {
        density = getContext().getResources().getDisplayMetrics().density;

        paint = new Paint();
        //TODO 颜色值加入资源文件
        paint.setColor(getResources().getColor(R.color.mark_green));
        paint.setStrokeWidth(5.0f);
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(14 * density);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }
}
