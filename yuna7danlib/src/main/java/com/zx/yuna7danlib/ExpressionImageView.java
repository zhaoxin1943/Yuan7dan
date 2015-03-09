package com.zx.yuna7danlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.graphics.Matrix;

/**
 * Created by zhaoxin on 15/3/5.
 * QQ:343986392
 * https://github.com/zhaoxin1943
 */
public class ExpressionImageView extends ImageView {

    private Bitmap deleteBitmap;
    private Bitmap flipBitmap;
    private Bitmap resizeBitmap;
    private Bitmap mBitmap;
    private Rect dst_delete;
    private Rect dst_resize;
    private Rect dst_flip;
    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    private int flipBitmapWidth;
    private int flipBitmapHeight;
    private Paint localPaint;
    private int mScreenwidth, mScreenHeight;
    private static final float BITMAP_SCALE = 0.7f;
    private PointF mid = new PointF();
    private OperationListener operationListener;
    private float lastRotateDegree;
    /**
     * 对角线的长度
     */
    private float lastLength;
    private boolean isInResize = false;

    private Matrix matrix = new Matrix();
    /**
     * 是否在四条线内部
     */
    private boolean isInSide;

    private float lastX, lastY;

    public ExpressionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpressionImageView(Context context) {
        super(context);
        init();
    }

    public ExpressionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flip = new Rect();
        localPaint = new Paint();
        localPaint.setColor(getResources().getColor(R.color.line_blue));
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(2.0f);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenwidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];


        dst_delete.left = (int) (f1 - deleteBitmapWidth / 2);
        dst_delete.top = (int) (f2 - deleteBitmapHeight / 2);
        dst_delete.right = (int) (f1 + deleteBitmapWidth / 2);
        dst_delete.bottom = (int) (f2 + deleteBitmapHeight / 2);

        dst_resize.left = (int) (f7 - resizeBitmapWidth / 2);
        dst_resize.right = (int) (f7 + resizeBitmapWidth / 2);
        dst_resize.top = (int) (f8 - resizeBitmapHeight / 2);
        dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 2);

        dst_flip.left = (int) (f5 - flipBitmapWidth / 2);
        dst_flip.right = (int) (f5 + flipBitmapWidth / 2);
        dst_flip.top = (int) (f6 - flipBitmapHeight / 2);
        dst_flip.bottom = (int) (f6 + flipBitmapHeight / 2);

        canvas.save();
        canvas.drawBitmap(mBitmap, matrix, null);
        canvas.drawLine(f1, f2, f3, f4, localPaint);
        canvas.drawLine(f3, f4, f7, f8, localPaint);
        canvas.drawLine(f5, f6, f7, f8, localPaint);
        canvas.drawLine(f5, f6, f1, f2, localPaint);

        canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
        canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
        canvas.drawBitmap(flipBitmap, null, dst_flip, null);
        canvas.restore();
    }

    @Override
    public void setImageResource(int resId) {
        matrix.reset();
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        initBitmaps();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        matrix.postTranslate(mScreenwidth / 2 - w / 2, mScreenHeight / 2 - h / 2);

        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        initBitmaps();
        invalidate();
    }

    private void initBitmaps() {
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delete_icon);
        flipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flip_icon);
        resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.resize_icon);

        deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);

        flipBitmapWidth = (int) (flipBitmap.getWidth() * BITMAP_SCALE);
        flipBitmapHeight = (int) (flipBitmap.getHeight() * BITMAP_SCALE);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInButton(event, dst_delete)) {
                    if (operationListener != null) {
                        operationListener.onDeleteClick();
                    }
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    lastLength = diagonalLength(event);
                    midPointToStartPoint(event);
                } else if (isInButton(event, dst_flip)) {
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    matrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    invalidate();
                    return false;
                } else if (isInBitmap(event)) {
                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInResize) {

                    matrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    float scale = diagonalLength(event) / lastLength;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                    lastLength = diagonalLength(event);

                    invalidate();
                } else if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    matrix.postTranslate(x - lastX, y - lastY);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isInResize = false;
                isInSide = false;
                break;

        }
        return true;
    }

    /**
     * 是否在四条线内部
     *
     * @return
     */
    private boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
        //左上角
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //右上角
        float f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //左下角
        float f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        //右下角
        float f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];
        //确定X方向的范围
        arrayOfFloat2[0] = f1;//左上的左
        arrayOfFloat2[1] = f3;//右上的右
        arrayOfFloat2[2] = f7;//右下的右
        arrayOfFloat2[3] = f5;//左下的左
        //确定Y方向的范围
        arrayOfFloat3[0] = f2;//左上的上
        arrayOfFloat3[1] = f4;//右上的上
        arrayOfFloat3[2] = f8;
        arrayOfFloat3[3] = f6;
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    /**
     * 判断点是否在一个矩形内部
     *
     * @param xRange
     * @param yRange
     * @param x
     * @param y
     * @return
     */
    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        //四条边的长度
        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);
        //待检测点到四个点的距离
        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        //矩形的面积
        double s = a1 * a2;
        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return s >= ss;


    }


    private boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private boolean isInResize(MotionEvent event) {
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private void midPointToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    private void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        paramPointF.set(f5 / 2.0F, f6 / 2.0F);
    }


    /**
     * 在滑动过车中X,Y是不会改变的，这里减Y，减X，其实是相当于把X,Y当做原点
     *
     * @param event
     * @return
     */
    private float rotationToStartPoint(MotionEvent event) {

        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);
        return (float) Math.toDegrees(arc);
    }

    private float diagonalLength(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float diagonalLength = (float) Math.hypot(event.getX(0) - x, event.getY(0) - y);
        return diagonalLength;
    }

    public interface OperationListener {
        void onDeleteClick();

    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }
}
