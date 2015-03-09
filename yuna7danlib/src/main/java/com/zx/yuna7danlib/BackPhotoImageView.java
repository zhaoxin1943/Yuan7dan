package com.zx.yuna7danlib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zhaoxin on 15/3/9.
 * QQ:343986392
 * https://github.com/zhaoxin1943
 */
public class BackPhotoImageView extends FrameLayout {
    public BackPhotoImageView(Context context) {
        super(context);
    }

    public BackPhotoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackPhotoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }


}
