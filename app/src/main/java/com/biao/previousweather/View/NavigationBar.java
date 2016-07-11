package com.biao.previousweather.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.biao.previousweather.R;

/**
 * Created by Administrator on 2016/7/11.
 */
public class NavigationBar extends View {

    private int mColor;
    private Bitmap icon;
    private String text = "";
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha;
    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";


    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
    }

    public NavigationBar(Context context) {
        super(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.NavigationBar_navigation_color:
                    mColor = typedArray.getColor(attr, 0xffcccccc);
                    break;
                case R.styleable.NavigationBar_navigation_icon:
                    BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    assert drawable != null;
                    icon = drawable.getBitmap();
                    break;
                case R.styleable.NavigationBar_navigation_text:
                    text = typedArray.getString(attr);
                    break;
                case R.styleable.NavigationBar_navigation_text_size:
                    textSize = (int) typedArray.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        mIconRect = new Rect();
        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(0xffcccccc);
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBound);
        //抗锯齿
        mTextPaint.setAntiAlias(true);
        //防抖动
        mTextPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(0, 0);
//        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(0, heightSpecSize);
//        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(widthSpecSize, 0);
//        }
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2);
        int left = (getMeasuredWidth() - iconWidth) / 2;
        int top = (getMeasuredHeight() - mTextBound.height() - iconWidth - mTextBound.height() / 4) / 2;
        mIconRect.set(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制原始图标
        canvas.drawBitmap(icon, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        setupTargetBitmap(alpha);

        //todo 绘制原文本
        drawSourceText(canvas, alpha);
        //todo 绘制变色的文本
        drawTargetText(canvas, alpha);
    }

    /**
     * 在内存中绘制可变色的icon
     */
    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();

        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        //按图标rect绘制纯色
        mCanvas.drawRect(mIconRect, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(icon, null, mIconRect, mPaint);
    }

    /**
     * 绘制原文本
     *
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff7C7C7C);
        mTextPaint.setAlpha(255 - alpha);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        int x = (getMeasuredWidth() - mTextBound.width()) / 2;
        int y = mIconRect.bottom + mTextBound.height() + mTextBound.height() / 4;
        canvas.drawText(text, x, y, mTextPaint);
    }

    /**
     * 绘制变色文本
     *
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);

        int x = (getMeasuredWidth() - mTextBound.width()) / 2;
        int y = mIconRect.bottom + mTextBound.height() + mTextBound.height() / 4;
        canvas.drawText(text, x, y, mTextPaint);
    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
