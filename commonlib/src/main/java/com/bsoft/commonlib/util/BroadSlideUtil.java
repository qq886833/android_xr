package com.bsoft.commonlib.util;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 点击滑动功能
 */
public class BroadSlideUtil {
    /*Default*/
    /*Util*/
    private BroadSlideListener listener;
    private Handler handler = new Handler();
    /*Flag*/
    //相对偏移X
    private float slideX;
    //相对偏移Y
    private float slideY;
    //动画时长
    private long animDuration = 300;
    //自动恢复等待时长
    private long waitTime = 3000;
    private boolean autoBackEnable = true;

    private boolean isSlideOut;
    /*View*/
    private View view;

    public interface BroadSlideListener {
        void onBroadSlideClick(View view);
    }

    public BroadSlideUtil(View view, float slideX, float slideY) {
        this(view, slideX, slideY, null);
    }

    public BroadSlideUtil(View view, float slideX, float slideY, BroadSlideListener listener) {
        this.listener = listener;
        this.slideX = slideX;
        this.slideY = slideY;
        this.view = view;

        init();
    }

    private void init() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSlideOut()) {
                    if (listener != null) {
                        listener.onBroadSlideClick(v);
                    }
                } else {
                    slideOutAnim();
                }
            }
        });
    }

    private Runnable backRun = new Runnable() {
        @Override
        public void run() {
            slideBackAnim();
        }
    };

    public void slideOutAnim() {
        if (autoBackEnable) {
            handler.postDelayed(backRun, waitTime);
        }

        Animation translateAnimation = new TranslateAnimation(0, slideX, 0, slideY);//平移动画  从0,0,平移到100,100
        translateAnimation.setDuration(animDuration);
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        view.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setSlideOut(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMargins(view, slideX, slideY);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void slideBackAnim() {
        handler.removeCallbacks(backRun);

        Animation translateAnimation = new TranslateAnimation(0, -slideX, 0, -slideY);//平移动画  从0,0,平移到100,100
        translateAnimation.setDuration(animDuration);
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        view.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMargins(view, -slideX, -slideY);
                view.clearAnimation();
                setSlideOut(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void setMargins(View v, float x, float y) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins((int) (p.leftMargin + x), (int) (p.topMargin + y),
                    (int) (p.rightMargin - x), (int) (p.bottomMargin - y));
            v.requestLayout();
        }
    }

    public boolean isAutoBackEnable() {
        return autoBackEnable;
    }

    public void setAutoBackEnable(boolean autoBackEnable) {
        this.autoBackEnable = autoBackEnable;
        if (!autoBackEnable) {
            handler.removeCallbacks(backRun);
        }
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    public boolean isSlideOut() {
        return isSlideOut;
    }

    public void setSlideOut(boolean slideOut) {
        isSlideOut = slideOut;
    }

    public BroadSlideListener getListener() {
        return listener;
    }

    public void setListener(BroadSlideListener listener) {
        this.listener = listener;
    }

    public float getSlideX() {
        return slideX;
    }

    public void setSlideX(float slideX) {
        this.slideX = slideX;
    }

    public float getSlideY() {
        return slideY;
    }

    public void setSlideY(float slideY) {
        this.slideY = slideY;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
