package com.jincaizi.kuaiwin.chart.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.jincaizi.R;

/**
 * Created by chenweida on 2015/1/28.
 */
public class LineView extends RelativeLayout {

    private int itemWidth;
    private int itemHeight;
    private float density;
    private boolean showLine = true;

    private Paint paint;
    private Paint textPaint;

    //TODO 支持100， 50， 30等不同统计期数
    private int totalIssueCount = 0;
    private int[] totalResultList = new int[100];

    public LineView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        setWillNotDraw(false);

        initPaint();
    }

    public void setParam(int itemWidth, int itemHeight, int[] totalResultList, int totalIssueCount, boolean showLine)
    {
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.totalResultList = totalResultList;
        this.totalIssueCount = totalIssueCount;
        this.showLine = showLine;

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
        float startX = ((totalResultList[startIndex] - 3) * itemWidth + itemWidth/2) * density;
        float startY = (startIndex * itemHeight + itemHeight/2) * density;
        float endX = ((totalResultList[endIndex] - 3) * itemWidth + itemWidth/2) * density;
        float endY = ((endIndex) * itemHeight + itemHeight/2) * density;
        canvas.drawLine(startX, startY, endX , endY , paint);
    }

    //TODO 可以更加抽象，把成员变量提出作为参数
    private void drawItem(int index, Canvas canvas)
    {
        float top = itemHeight * index * density;
        float left = (totalResultList[index] - 3) * itemWidth * density;
        float bottom = top + itemHeight * density;
        float right = left + itemWidth * density;
        canvas.drawRect(left, top, right, bottom, paint);

        canvas.drawText(String.valueOf(totalResultList[index]), left + itemWidth/2 * density,
                top + (6 + itemHeight/2) * density, textPaint);
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
