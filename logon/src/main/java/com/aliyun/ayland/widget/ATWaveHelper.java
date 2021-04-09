package com.aliyun.ayland.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class ATWaveHelper {
    private ATWaveView mATWaveView;
    private AnimatorSet mAnimatorSet;
    private ObjectAnimator mWaterLevelAnim;

    public ATWaveHelper(ATWaveView ATWaveView) {
        mATWaveView = ATWaveView;
        initAnimation();
    }

    public void start() {
        mATWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mATWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        //waveShiftAnim.setRepeatMode(ValueAnimator.REVERSE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mATWaveView, "waterLevelRatio", 0f, 0.5f);
        waterLevelAnim.setDuration(3000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);


        // amplitude animation.
        // wave grows big then grows small, repeatedly
/*
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(3000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);
*/
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);

        mWaterLevelAnim = ObjectAnimator.ofFloat(
                mATWaveView, "waterLevelRatio", 0.5f, 1f);
        mWaterLevelAnim.setDuration(6000);
        mWaterLevelAnim.setInterpolator(new DecelerateInterpolator());
        mWaterLevelAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mAnimatorSet.end();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void cancel() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            // vertical animation.
            // water level increases from 0 to center of WaveView
            mAnimatorSet.end();
        }
    }

    public void playNext() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            // vertical animation.
            // water level increases from 0 to center of WaveView
            mWaterLevelAnim.start();
        }
    }
}