package com.zy.animaldemo1;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class EasyTransition {

    public static final String EASY_TRANSITION_OPTIONS = "easy_transition_options";
    public static final long DEFAULT_TRANSITION_ANIM_DURATION = 1000;

    public static void startActivity(Intent intent, EasyTransitionOptions options) {
        options.update();
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, options.getAttrs());
        Activity activity = options.getActivity();
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);//overridePendingTransition(0, 0)将系统的转场动画覆盖，0表示没有转场动画
    }

    public static void startActivityForResult(Intent intent, int requestCode, EasyTransitionOptions options) {
        options.update();
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, options.getAttrs());
        Activity activity = options.getActivity();
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }


    public static void enter(Activity activity, long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        Intent intent = activity.getIntent();
        ArrayList<EasyTransitionOptions.ViewAttrs> attrs =
                intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runEnterAnimation(activity, attrs, duration, interpolator, listener);
    }

    public static void enter(Activity activity, long duration, Animator.AnimatorListener listener) {
        enter(activity, duration, null, listener);
    }

    public static void enter(Activity activity, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, interpolator, listener);
    }

    public static void enter(Activity activity, Animator.AnimatorListener listener) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, null, listener);
    }

    public static void enter(Activity activity) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, null, null);
    }

    private static void runEnterAnimation(Activity activity,
                                          ArrayList<EasyTransitionOptions.ViewAttrs> attrs,
                                          final long duration,
                                          final TimeInterpolator interpolator,
                                          final Animator.AnimatorListener listener) {
        if (null == attrs || attrs.size() == 0)
            return;

        for (final EasyTransitionOptions.ViewAttrs attr : attrs) {
            final View view = activity.findViewById(attr.id);

            if (null == view)
                continue;

            view.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);

                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            view.setPivotX(0);
                            view.setPivotY(0);
                            view.setScaleX(attr.width / view.getWidth());
                            view.setScaleY(attr.height / view.getHeight());
                            view.setTranslationX(attr.startX - location[0]); // xDelta
                            view.setTranslationY(attr.startY - location[1]); // yDelta

                            view.animate()
                                    .scaleX(1)
                                    .scaleY(1)
                                    .translationX(0)
                                    .translationY(0)
                                    .setDuration(duration)
                                    .setInterpolator(interpolator)
                                    .setListener(listener);
                            return true;
                        }
                    });
        }
    }

    public static void exit(Activity activity, long duration, TimeInterpolator interpolator) {
        Intent intent = activity.getIntent();
        ArrayList<EasyTransitionOptions.ViewAttrs> attrs = intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runExitAnimation(activity, attrs, duration, interpolator);
    }

    public static void exit(Activity activity, TimeInterpolator interpolator) {
        exit(activity, DEFAULT_TRANSITION_ANIM_DURATION, interpolator);
    }


    public static void exit(Activity activity, long duration) {
        exit(activity, duration, null);
    }


    public static void exit(Activity activity) {
        exit(activity, DEFAULT_TRANSITION_ANIM_DURATION, null);
    }

    private static void runExitAnimation(final Activity activity,
                                         ArrayList<EasyTransitionOptions.ViewAttrs> attrs,
                                         long duration,
                                         TimeInterpolator interpolator) {
        if (null == attrs || attrs.size() == 0)
            return;

        for (final EasyTransitionOptions.ViewAttrs attr : attrs) {
            View view = activity.findViewById(attr.id);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            view.setPivotX(0);
            view.setPivotY(0);

            view.animate()
                    .scaleX(attr.width / view.getWidth())
                    .scaleY(attr.height / view.getHeight())
                    .translationX(attr.startX - location[0])
                    .translationY(attr.startY - location[1])
                    .setInterpolator(interpolator)
                    .setDuration(duration);
        }

        activity.findViewById(attrs.get(0).id).postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }, duration);
    }
}
