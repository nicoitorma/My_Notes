package com.nicanoritorma.mynotes.intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.nicanoritorma.mynotes.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slider_images = {
            R.drawable.notepad,
            R.drawable.lock,
            R.drawable.fingerprint
    };

    public int[] slider_text = {
      R.string.slider_text1,
      R.string.slider_text2,
      R.string.slider_text3
    };

    @Override
    public int getCount() {
        return slider_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView intro_iv = view.findViewById(R.id.intro_gif_iv);
        TextView intro_tv = view.findViewById(R.id.intro_tv);

        intro_iv.setImageResource(slider_images[position]);
        intro_tv.setText(slider_text[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);
    }
}
