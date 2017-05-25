package com.zy.animaldemo1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by silei on 2017/5/25.
 * e-mail: scokmi2015@163.com
 */

public class DetailActivity extends AppCompatActivity {
    private TextView tvName;
    private LinearLayout layoutAbout;
    private ImageView ivAdd;
    private boolean finishEnter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        long transitionDuration = 800;
        if (null != savedInstanceState) {
            transitionDuration = 0;
        }
        finishEnter = false;
        EasyTransition.enter(
                this,
                transitionDuration,
                new DecelerateInterpolator(),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishEnter = true;
                        initOtherViews();
                    }
                }
        );

    }

    private void initOtherViews() {
        layoutAbout = (LinearLayout) findViewById(R.id.layout_about);
        layoutAbout.setVisibility(View.VISIBLE);
        layoutAbout.setAlpha(0);
        layoutAbout.setTranslationX(-30);
        layoutAbout.animate()
                .setDuration(300)
                .alpha(1)
                .translationY(0);

        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);
        ivAdd.setScaleX(0);
        ivAdd.setScaleY(0);
        ivAdd.animate()
                .setDuration(200)
                .scaleX(1)
                .scaleY(1);
    }

    private void initViews() {
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvName.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public void onBackPressed() {
        if (finishEnter) {
            finishEnter = false;
            startBackAnim();
        }
    }

    private void startBackAnim() {
        // forbidden scrolling
        ScrollView sv = (ScrollView) findViewById(R.id.sv);
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // start our anim
        ivAdd.animate()
                .setDuration(200)
                .scaleX(0)
                .scaleY(0);

        layoutAbout.animate()
                .setDuration(300)
                .alpha(0)
                .translationY(-30)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // transition exit after our anim
                        EasyTransition.exit(DetailActivity.this, 800, new DecelerateInterpolator());
                    }
                });
    }


}