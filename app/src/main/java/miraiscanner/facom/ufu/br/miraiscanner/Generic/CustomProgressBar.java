package miraiscanner.facom.ufu.br.miraiscanner.Generic;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import miraiscanner.facom.ufu.br.miraiscanner.R;


public class CustomProgressBar extends View {
    private Paint mArcPaintBackground;
    private Paint mArcPaintPrimary;
    private final Rect mTextBounds = new Rect();
    private int mProgress;
    private Paint mTextPaint;
    private int centerX;
    private int centerY;
    private int mWidthArcBG;
    private int mWidthAcrPrimary;
    private int mTextSizeProgress;
    public CustomProgressBar(Context context) {
        super(context);
        init(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidthHeight = MeasureSpec.getSize(getResources().getDimensionPixelSize(R.dimen.width_height_progress));
        centerX=viewWidthHeight/2;
        centerY=viewWidthHeight/2;
        setMeasuredDimension(viewWidthHeight,viewWidthHeight);
    }
    private void initPixelSize(){
        mWidthArcBG=getResources().getDimensionPixelSize(R.dimen.width_arc_background);
        mWidthAcrPrimary=getResources().getDimensionPixelSize(R.dimen.width_arc_primary);
        mTextSizeProgress=getResources().getDimensionPixelSize(R.dimen.text_size_progress);

    }

    private void init(Context ctx) {
        initPixelSize();
        mArcPaintBackground = new Paint();
        mArcPaintBackground.setDither(true);
        mArcPaintBackground.setStyle(Paint.Style.STROKE);
        mArcPaintBackground.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        mArcPaintBackground.setStrokeWidth(mWidthArcBG);
        mArcPaintBackground.setAntiAlias(true);

        mArcPaintPrimary = new Paint();
        mArcPaintPrimary.setDither(true);
        mArcPaintPrimary.setStyle(Paint.Style.STROKE);
        mArcPaintPrimary.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mArcPaintPrimary.setStrokeWidth(mWidthAcrPrimary);
        mArcPaintPrimary.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSizeProgress);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(ContextCompat.getColor(getContext(),android.R.color.black));
        mTextPaint.setStrokeWidth(2);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(0+10, 0+10, canvas.getWidth()-10, canvas.getHeight()-10);
        canvas.drawArc(rect, 270, 360, false, mArcPaintBackground);
        canvas.drawArc(rect, 270, -(360 * (mProgress / 100f)), false, mArcPaintPrimary);
        drawTextCentred(canvas);
    }
    public void drawTextCentred(Canvas canvas) {
        String text=mProgress+"%";
        mTextPaint.getTextBounds(text, 0,text.length() , mTextBounds);
        canvas.drawText(text, centerX - mTextBounds.exactCenterX(), centerY - mTextBounds.exactCenterY(), mTextPaint);
    }
}
