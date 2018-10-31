package ru.xitrix.uikit.UIKit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import ru.xitrix.uikit.UIKit.View.UIViewControllerContainer;

public abstract class UIViewController extends Fragment {

    private static final String TITLE = "title";

    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnStartDelayedEvent onStartDelayedEvent;
    private boolean canBeginClosing = true;

    private String title;

    public boolean closeBySwipeGesture = true;
    public boolean userInteractiable = true;

    public void setOnStartDelayedEvent(OnStartDelayedEvent onStartDelayedEvent) {
        if (getView() != null) {
            onStartDelayedEvent.onStartDelayedEvent();
        } else {
            this.onStartDelayedEvent = onStartDelayedEvent;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UIViewController() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            title = savedInstanceState.getString(TITLE);
        }
    }

    protected UIViewControllerContainer createView(LayoutInflater inflater, ViewGroup container, @LayoutRes int resource) {
        final View view = inflater.inflate(resource, container, false);
        if (!(view instanceof UIViewControllerContainer)) {
            throw new RuntimeException(view.getContext().toString()
                    + " layout root must be UIViewControllerContainer");
        }

        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final float[] startTouch = new float[2];

        ((UIViewControllerContainer) view).setOnInterceptTouchEvent(new UIViewControllerContainer.OnInterceptTouchEvent() {

            private final int leftMargin = 80;

            private float mLastX, mLastY, mTouchSlop = 40;
            boolean checking = false;

            @Override
            public boolean onInterceptTouchEvent(MotionEvent event) {
                if (!userInteractiable) return true;
                UINavigationController navigationController = getNavigationController();
                if (closeBySwipeGesture &&
                        navigationController != null &&
                        navigationController.controllers.size() > 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastX = event.getX();
                            mLastY = event.getY();
                            startTouch[0] = mLastX;
                            startTouch[1] = mLastY;
                            checking = true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float x = event.getX();
                            float y = event.getY();
                            float xDelta = Math.abs(x - mLastX);
                            float yDelta = Math.abs(y - mLastY);

                            float xDeltaTotal = x - startTouch[0];
                            float yDeltaTotal = y - startTouch[1];

                            if (checking && canBeginClosing && startTouch[0] < leftMargin) {
                                if (xDelta < yDelta && Math.abs(yDeltaTotal) > mTouchSlop) { // Cancel detecting close gesture
                                    checking = false;
                                } else if (xDelta > yDelta && Math.abs(xDeltaTotal) > mTouchSlop) { // Start close gesture
                                    canBeginClosing = false;
                                    getNavigationController().prepareDismissAnimation();

                                    return true;
                                }
                            }
                            break;
                    }
                }

                return false;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {

            LinkedList<Float> xPoints = new LinkedList<>();

            void addPoint(float point) {
                xPoints.add(point);
                if (xPoints.size() > 5) {
                    xPoints.removeFirst();
                }
            }

            float getVelocity() {
                return xPoints.getLast() - xPoints.getFirst();
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!userInteractiable) return true;
                UINavigationController navigationController = getNavigationController();
                if (closeBySwipeGesture &&
                        navigationController != null &&
                        navigationController.controllers.size() > 1) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        addPoint(event.getRawX());
                        getNavigationController().animation(Math.max(0, event.getRawX() - startTouch[0]), null);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        addPoint(event.getRawX());
                        if (getVelocity() > 50) {
                            getNavigationController().finishDismissAnimation(new UINavigationController.OnCompletionEvent() {
                                @Override
                                public void completionEvent() {
                                    canBeginClosing = true;
                                }
                            });
                        } else {
                            getNavigationController().finishPresentAnimation(new UINavigationController.OnCompletionEvent() {
                                @Override
                                public void completionEvent() {
                                    canBeginClosing = true;
                                }
                            });
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        return (UIViewControllerContainer) view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (onStartDelayedEvent != null) {
            onStartDelayedEvent.onStartDelayedEvent();
            onStartDelayedEvent = null;
        }
    }

    public UINavigationController getNavigationController() {
        if (getParentFragment() instanceof UINavigationController) {
            return (UINavigationController) getParentFragment();
        }
        return null;
    }

    public void present(UIViewController controller) {
        if (getParentFragment() != null) {
            ((UINavigationController)getParentFragment()).present(controller);
        }
    }

    public void dismiss() {
        if (getParentFragment() != null) {
            ((UINavigationController)getParentFragment()).dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE, title);
    }

    public interface OnStartDelayedEvent {
        void onStartDelayedEvent();
    }

}
