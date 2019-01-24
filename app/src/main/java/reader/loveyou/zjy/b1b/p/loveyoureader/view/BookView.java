package reader.loveyou.zjy.b1b.p.loveyoureader.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 Created by 张建宇 on 2019/1/22. */
public class BookView extends ViewGroup{
    public BookView(Context context) {
        super(context);
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewPager v;
        return super.onTouchEvent(event);
    }
}
