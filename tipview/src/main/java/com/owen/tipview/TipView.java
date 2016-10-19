package com.owen.tipview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class TipView extends RelativeLayout {

    private static final String TAG = "TipViewOwen";

    private Bitmap mMaskBitmap;
    private Canvas mMaskCanvas;
    private Paint mMaskPaint;

    private Paint mTransparentPaint;
    private PorterDuffXfermode mPorterDuffXfermode;

    private Paint mSystemCanvasPaint;

    private Activity mActivity;
    private View mTargetView;

    public TipView(Context context) {
        super(context);

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("You should pass Activity as parameter");
        }
        mActivity = (Activity) context;

        setClickable(true);  // 这句话可以拦截点击事件，这样 contentView 里的控件就不能点击了

        /*
         * let ViewGroup to invoke onDraw() method,
         * also, you can override dispatchDraw() method instead of override onDraw() method
         */
        setWillNotDraw(false);

        mMaskPaint = new Paint();
        mMaskPaint.setColor(0xC8000000);

        mTransparentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mTransparentPaint.setXfermode(mPorterDuffXfermode);

        mSystemCanvasPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas systemCanvas) {
        super.onDraw(systemCanvas);

        // 绘制遮罩层
        if (mMaskBitmap == null) {
            mMaskBitmap = Bitmap.createBitmap(systemCanvas.getWidth(), systemCanvas.getHeight(), Bitmap.Config.ARGB_8888);
            mMaskCanvas = new Canvas(mMaskBitmap);
        }
        mMaskCanvas.drawRect(0, 0, mMaskCanvas.getWidth(), mMaskCanvas.getHeight(), mMaskPaint);
        // 在遮罩层上挖一个洞
//        mMaskCanvas.drawCircle(180, 230, 100, mTransparentPaint);
        mMaskCanvas.drawCircle(mPoint.x, mPoint.y, mTargetView.getWidth() / 2, mTransparentPaint);

        systemCanvas.drawBitmap(mMaskBitmap, 0, 0, mSystemCanvasPaint);
    }

    private Point mPoint;

    public void setTargetView(View targetView) {
        mTargetView = targetView;
    }

    public void show() {
        ((ViewGroup) mActivity.getWindow().getDecorView()).addView(this);

        mTargetView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int[] location = new int[2];
                        mTargetView.getLocationInWindow(location);
                        int x =  location[0] + mTargetView.getWidth() / 2;
                        int y = location[1] + mTargetView.getHeight() / 2;
                        mPoint = new Point(x, y);

                        if (Build.VERSION.SDK_INT<16) {
                            mTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mTargetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
    }

    public static class Builder {

        TipView mTipView;

        public Builder(Context context) {
            mTipView = new TipView(context);
        }

        public Builder setTargetView(View view) {
            mTipView.setTargetView(view);
            return this;
        }

        public TipView build() {
            return mTipView;
        }
    }
}
