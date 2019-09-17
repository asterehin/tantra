package com.tantra.tantrayoga.widget;

import android.content.Context;
import android.util.AttributeSet;

public class NoSwipeViewPager extends BaseViewPager {

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSwipeEnabled(false);
    }
}
