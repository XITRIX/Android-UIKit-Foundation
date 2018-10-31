package ru.xitrix.uikit.UIKit.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class UIViewControllerContainer extends ConstraintLayout {

    private OnInterceptTouchEvent mOnInterceptTouchEvent;

    public void setOnInterceptTouchEvent(OnInterceptTouchEvent mOnInterceptTouchEvent) {
        this.mOnInterceptTouchEvent = mOnInterceptTouchEvent;
    }

    public UIViewControllerContainer(Context context) {
        super(context);
    }

    public UIViewControllerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIViewControllerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mOnInterceptTouchEvent != null) {
            return mOnInterceptTouchEvent.onInterceptTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }

    public interface OnInterceptTouchEvent {
        boolean onInterceptTouchEvent(MotionEvent event);
    }

    private float foregroundShadow;
    public void setForegroundShadow(float foregroundShadow) {
        this.foregroundShadow = foregroundShadow;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setForeground(new ColorDrawable(Color.argb((int)(foregroundShadow * 255), 0, 0, 0)));
        }
    }

    public float getForegroundShadow() {
        return foregroundShadow;
    }
}
