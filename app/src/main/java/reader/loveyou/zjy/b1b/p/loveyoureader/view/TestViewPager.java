package reader.loveyou.zjy.b1b.p.loveyoureader.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 Created by 张建宇 on 2019/1/23. */
public class TestViewPager extends ViewPager {
    public TestViewPager(@NonNull Context context) {
        super(context);
    }

    public TestViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private CustTouchLisnter mListener;

    public void setmListener(CustTouchLisnter mListener) {
        this.mListener = mListener;
    }

    public interface CustTouchLisnter {
        void show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mListener != null) {
            int action = ev.getAction();
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            float touchY = 0;
            switch (action ) {
                case MotionEvent.ACTION_DOWN:
                    touchY = ev.getY();

                    break;
                case MotionEvent.ACTION_UP:
                    if (touchY > measuredHeight / 2 && touchY < measuredHeight / 2 * 3) {
                        mListener.show();
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

}
