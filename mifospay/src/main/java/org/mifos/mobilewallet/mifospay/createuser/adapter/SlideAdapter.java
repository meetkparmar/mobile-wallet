package org.mifos.mobilewallet.mifospay.createuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.createuser.ui.DemoLoginActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SlideAdapter(@NotNull Context context) {
        this.context = context;
    }

    public int[] slide_images = {R.layout.onboarding_screen_1, R.layout.onboarding_screen_2, R.layout.onboarding_screen_3};

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(slide_images[position], container, false);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
