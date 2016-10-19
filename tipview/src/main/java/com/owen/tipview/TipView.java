package com.owen.tipview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.RelativeLayout;

public class TipView extends RelativeLayout {

    private Bitmap mMaskBitmap;
    private Canvas mMaskCanvas;
    private Paint mMaskPaint;

    private Paint mTransparentPaint;
    private PorterDuffXfermode mPorterDuffXfermode;

    private Paint mSystemCanvasPaint;

    public TipView(Context context) {
        super(context);

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
        mMaskCanvas.drawCircle(180, 230, 100, mTransparentPaint);

        systemCanvas.drawBitmap(mMaskBitmap, 0, 0, mSystemCanvasPaint);
    }
}
