package com.zx.yuna7danlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.Matrix;

/**
 * Created by zhaoxin on 15/3/9.
 * QQ:343986392
 * https://github.com/zhaoxin1943
 */
public class FrameImageView extends ImageView {

    private Bitmap mBitmap;
    private int screenWidth;


    public FrameImageView(Context context) {
        super(context);
        init(context);
    }

    public FrameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FrameImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        setBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mBitmap != null) {
            int bitmapWidth = mBitmap.getWidth();
            float scale = 1.0f * screenWidth / bitmapWidth;
            Matrix localMatrix = new Matrix();
            localMatrix.postScale(scale, scale);
            canvas.drawBitmap(mBitmap, localMatrix, null);
        } else {
            canvas.drawColor(0);
        }
    }

}
