package com.zx.yuan7dan;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.zx.yuna7danlib.ExpressionImageView;
import com.zx.yuna7danlib.FrameImageView;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity {

    private FrameImageView back_frame;
    private FrameLayout edit_bitmap_parent_layout;
    private ArrayList<ExpressionImageView> expressionImageViews = new ArrayList<>();
    private ExpressionImageView currentEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        back_frame = (FrameImageView) findViewById(R.id.back_frame);
        back_frame.setImageResource(R.mipmap.test);
        edit_bitmap_parent_layout = (FrameLayout) findViewById(R.id.edit_bitmap_parent_layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            addExpression();
            return true;
        } else if (id == R.id.action_complete) {
            currentEditView.setInEdit(false);
            back_frame.setBitmap(generateBitmap());
            clearBitmap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearBitmap() {

        Iterator<ExpressionImageView> iterator = expressionImageViews.iterator();
        while (iterator.hasNext()) {
            ExpressionImageView temp = iterator.next();
            edit_bitmap_parent_layout.removeView(temp);
            iterator.remove();
        }
    }

    private Bitmap generateBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(edit_bitmap_parent_layout.getWidth(), edit_bitmap_parent_layout.getHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        edit_bitmap_parent_layout.draw(canvas);
        return bitmap;
    }

    private void addExpression() {

        final ExpressionImageView expressionImageView = new ExpressionImageView(this);
        expressionImageView.setImageResource(R.mipmap.flight);
        expressionImageView.setOperationListener(new ExpressionImageView.OperationListener() {
            @Override
            public void onDeleteClick() {
                expressionImageViews.remove(expressionImageView);
                edit_bitmap_parent_layout.removeView(expressionImageView);
            }

            @Override
            public void onEdit(ExpressionImageView expressionImageView) {
                currentEditView.setInEdit(false);
                currentEditView = expressionImageView;
                currentEditView.setInEdit(true);
            }
        });
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        edit_bitmap_parent_layout.addView(expressionImageView, lp);
        expressionImageViews.add(expressionImageView);
        setCurrentEdit(expressionImageView);
    }

    /**
     * 设置当前处于编辑模式的表情
     */
    private void setCurrentEdit(ExpressionImageView expressionImageView) {
        if (currentEditView != null) {
            currentEditView.setInEdit(false);
        }
        currentEditView = expressionImageView;
        expressionImageView.setInEdit(true);
    }
}
