package com.nicanoritorma.mynotes.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nicanoritorma.mynotes.MainActivity;
import com.nicanoritorma.mynotes.R;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private SliderAdapter adapter;
    private ImageView[] dots;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Button btn_prev, btn_next;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);

        adapter = new SliderAdapter(this);

        preferences = getSharedPreferences("first_time", 0);
        editor = preferences.edit();
        editor.putBoolean("FIRST_TIME", false);
        editor.apply();

        viewPager.setAdapter(adapter);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        btn_next.setOnClickListener(v -> {
            if (btn_next.getText().equals("Finish"))
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            else
            {
                viewPager.setCurrentItem(currentPage + 1);
            }
        });
        btn_prev.setOnClickListener(v -> viewPager.setCurrentItem(currentPage - 1));
    }

    public void addDotsIndicator(int position)
    {
        dots = new ImageView[3];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
        if (dots.length > 0)
        {
            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            currentPage = position;
            if (position == dots.length-1)
            {
                btn_prev.setEnabled(true);
                btn_next.setEnabled(true);
                btn_prev.setVisibility(View.VISIBLE);

                btn_next.setText(R.string.finish);
                btn_prev.setText(R.string.back);
            }
            else {
                btn_prev.setEnabled(true);
                btn_next.setEnabled(true);
                btn_prev.setVisibility(View.VISIBLE);

                btn_next.setText(R.string.next);
                btn_prev.setText(R.string.back);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        System.exit(0);
        finish();
    }
}