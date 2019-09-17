package com.tantra.tantrayoga.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.tantra.tantrayoga.R;


/**
 * View pager variant that can be locked (disabled)
 */
public class BaseViewPager extends ViewPager {

    private boolean enabled;

    public BaseViewPager(Context context) {
        this(context, null, 0);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
//        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
//        init(context, attrs, defStyleAttr, defStyleRes);
    }

//    private void init(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
//        // Load attributes
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseViewPager,
//                                                      defStyle, defStyleRes);
//
//        this.enabled = a.getBoolean(R.styleable.BaseViewPager_swipeEnabled, true);
//        a.recycle();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        setSwipeEnabled(enabled);
    }

    public void setSwipeEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
