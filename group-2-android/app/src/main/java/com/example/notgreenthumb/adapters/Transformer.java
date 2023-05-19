package com.example.notgreenthumb.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class Transformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;



    // This has been found online and adapter to our app's needs to display the carousel like effect
    // Source: https://github.com/ChaMinZi/Diary/blob/master/app/src/main/java/com/example/diary/Canvas/CanvasViewPager.java
    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();

        // Adjust the scale and translation values based on the position
        float scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f;
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);

        float translationX = -position * pageWidth * 0.15f;
        view.setTranslationX(translationX);

        // Adjust visibility of edge cards
        if (position < -1 || position > 1) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

    }

}


