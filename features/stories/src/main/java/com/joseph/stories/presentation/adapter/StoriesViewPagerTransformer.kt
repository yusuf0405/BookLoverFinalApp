package com.joseph.stories.presentation.adapter

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class StoriesViewPagerTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.cameraDistance = page.height * 4.5f

        if (position < 0) {
            page.pivotX = page.width.toFloat();
        } else {
            page.pivotX = 0f;
        }
        page.pivotY = page.height / 2f;

        when {
            position < -1 -> {
                page.alpha = 0f;
            }
            position <= 1 -> {
                page.alpha = 1f;
                page.rotationY = 90 * position;
            }
            else -> {
                page.alpha = 0f;
            }
        }
    }

}