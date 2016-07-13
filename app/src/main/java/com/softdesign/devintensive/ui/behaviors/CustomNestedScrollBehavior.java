package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.util.TypedValue;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.UiHelper;


/**
 * Created by Alex on 13.07.2016.
 */
public class CustomNestedScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

    private final int mMaxAppbarHeight;
    private final int mMinAppbarHeight;
    private final int mMaxUserInfoHeight;

    public CustomNestedScrollBehavior(Context context, AttributeSet attrs){
        mMinAppbarHeight = UiHelper.getStatusBarHeight() + UiHelper.getActionBarHeight();
        mMaxAppbarHeight = context.getResources().getDimensionPixelOffset(R.dimen.profile_image_size_256); //256dp
        mMaxUserInfoHeight = context.getResources().getDimensionPixelOffset(R.dimen.user_info_size_112); //112dp
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float friction = UiHelper.currentFriction(mMinAppbarHeight,mMaxAppbarHeight,dependency.getBottom());
        int offsetY = UiHelper.lerp(mMaxUserInfoHeight / 2, mMaxUserInfoHeight, friction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.topMargin = offsetY;
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}