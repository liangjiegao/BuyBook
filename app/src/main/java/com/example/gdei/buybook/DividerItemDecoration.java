package com.example.gdei.buybook;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import static android.graphics.PorterDuff.Mode.CLEAR;

/**
 * Created by gdei on 2018/5/11.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayout.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayout.VERTICAL;
    private int mOrientation;
    private Drawable mDrawable;
    private Context mContext;
    private Paint mPaint;

    public DividerItemDecoration(Activity context, int orientation){
        System.out.println("DividerItemDecoration");
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDrawable =  a.getDrawable(0);
        mContext = context;
        a.recycle();
        setOrientation(orientation);
        mPaint = new Paint();
        mPaint.setColor(mContext.getResources().getColor(R.color.divideColor));
    }
    private void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("参数异常");
        }
        mOrientation = orientation;
    }
    /*
    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        System.out.println("onDraw");
        if (mOrientation == HORIZONTAL_LIST){
            drawHorizontal(c, parent);
        }else if (mOrientation == VERTICAL_LIST){
            drawVertical(c, parent);
        }
    }
    */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + 2;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getPaddingRight() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        System.out.println(childCount);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            System.out.println(left+""+top+""+right+""+bottom);
            //mDrawable.setBounds(left, top, right, bottom);
            Paint paint = new Paint();
            paint.setColor(mContext.getResources().getColor(R.color.colorAccent));
            c.drawRect(left, top, right, bottom,paint);
            //mDrawable.draw(c);
        }
    }
    private void drawHorizontal(Canvas c, RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getPaddingBottom() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getBottom() + params.rightMargin;
            final int right = left + mDrawable.getIntrinsicWidth();
            Paint paint = new Paint();
            paint.setColor(mContext.getResources().getColor(R.color.colorAccent));
            c.drawRect(left, top, right, bottom,paint);
            //mDrawable.setBounds(left, top, right, bottom);
            //c.drawColor(999);
            //mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = 2;
    }

}
