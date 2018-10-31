package ru.xitrix.uikit.UIKit;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import ru.xitrix.uikit.R;
import ru.xitrix.uikit.UIKit.View.UIViewControllerContainer;

public class UINavigationController extends Fragment {

    private static final String FRAGMENT = "fragment";
    private static final String FRAGMENT_COUNT = "fragment_count";

    private DisplayMetrics displayMetrics = new DisplayMetrics();

    public LinkedList<UIViewController> controllers;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rootController root controller of toolbar.
     * @return A new instance of fragment UINavigationController.
     */
    public static UINavigationController init(UIViewController rootController) {
        UINavigationController fragment = new UINavigationController();
        Bundle args = new Bundle();
        LinkedList<UIViewController> root = new LinkedList<>();
        root.add(rootController);
        fragment.controllers = root;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (savedInstanceState != null) {
                controllers = new LinkedList<>();
                for (int i = 0; i < savedInstanceState.getInt(FRAGMENT_COUNT); i++) {
                    controllers.add((UIViewController) getChildFragmentManager().getFragment(savedInstanceState, FRAGMENT+i));
                }
            }
            for (int i = 0; i < controllers.size(); i++) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.toolbarContent, controllers.get(i))
                        .addToBackStack(FRAGMENT+i)
                        .commit();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_uinavigation, container, false);

        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setDrawingCacheBackgroundColor(Color.WHITE);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(controllers.size() > 1);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(controllers.get(controllers.size() - 1).getTitle());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public void present(UIViewController controller) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.toolbarContent, controller).addToBackStack(FRAGMENT+(controllers.size() - 1)).commit();
        controllers.add(controller);

        controllers.getLast().setOnStartDelayedEvent(new UIViewController.OnStartDelayedEvent() {
            @Override
            public void onStartDelayedEvent() {
                preparePresentAnimation();
                finishPresentAnimation(null);
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(controllers.size() > 1);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(controllers.get(controllers.size() - 1).getTitle());
    }

    public void dismiss() {
        if (controllers.size() <= 1) {
            return;
        }

        prepareDismissAnimation();
        animationBuffer[0].setOnStartDelayedEvent(new UIViewController.OnStartDelayedEvent() {
            @Override
            public void onStartDelayedEvent() {
                animationBuffer[0].getView().setX(-displayMetrics.widthPixels * 0.25f);
                ((UIViewControllerContainer) animationBuffer[0].getView()).setForegroundShadow(0.25f);
                finishDismissAnimation(null);
            }
        });
    }

    UIViewController[] animationBuffer = new UIViewController[2];
    public void prepareDismissAnimation() {
        if (controllers.size() >= 2) {
            animationBuffer[0] = controllers.get(controllers.size() - 2);
            animationBuffer[1] = controllers.getLast();

            if (!animationBuffer[0].isAdded()) {
                animationBuffer[0].userInteractiable = false;
                getChildFragmentManager().beginTransaction().add(R.id.toolbarContent, animationBuffer[0]).commit();
                animationBuffer[0].setOnStartDelayedEvent(new UIViewController.OnStartDelayedEvent() {
                    @Override
                    public void onStartDelayedEvent() {
                        animationBuffer[0].getView().setTranslationZ(controllers.size() - 2);
                        animationBuffer[1].getView().setTranslationZ(controllers.size() - 1);
                    }
                });
            } else {
                animationBuffer[0].getView().setTranslationZ(controllers.size() - 2);
                animationBuffer[1].getView().setTranslationZ(controllers.size() - 1);
            }
        }
    }

    public void finishDismissAnimation(final OnCompletionEvent completionEvent) {
//        try {
            if (controllers.size() >= 2) {
                animation(displayMetrics.widthPixels, 250, new OnCompletionEvent() {
                    @Override
                    public void completionEvent() {
                        animationBuffer[0].userInteractiable = true;
                        animationBuffer[1].userInteractiable = true;
                        getChildFragmentManager().beginTransaction().remove(animationBuffer[1]).commitAllowingStateLoss();

                        controllers.removeLast();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(controllers.size() > 1);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(controllers.getLast().getTitle());

                        if (completionEvent != null) {
                            completionEvent.completionEvent();
                        }
                    }
                });
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void preparePresentAnimation() {
        if (controllers.size() >= 2) {
            animationBuffer[0] = controllers.get(controllers.size() - 2);
            animationBuffer[1] = controllers.getLast();

            animationBuffer[0].userInteractiable = false;
            animationBuffer[1].userInteractiable = false;

            animationBuffer[1].setOnStartDelayedEvent(new UIViewController.OnStartDelayedEvent() {
                @Override
                public void onStartDelayedEvent() {
                    animationBuffer[0].getView().setX(0);
                    animationBuffer[1].getView().setX(displayMetrics.widthPixels);

                    animationBuffer[0].getView().setTranslationZ(controllers.size() - 2);
                    animationBuffer[1].getView().setTranslationZ(controllers.size() - 1);
                }
            });
        }
    }

    public void finishPresentAnimation(final OnCompletionEvent completionEvent) {
        try {
            if (controllers.size() >= 2) {
                animation(0, 350, new OnCompletionEvent() {
                    @Override
                    public void completionEvent() {
                        animationBuffer[0].userInteractiable = true;
                        animationBuffer[1].userInteractiable = true;
                        getChildFragmentManager().beginTransaction().remove(controllers.get(controllers.size() - 2)).commitAllowingStateLoss();

                        if (completionEvent != null) {
                            completionEvent.completionEvent();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void animation(float position, final OnCompletionEvent completionEvent) {
        animation(position, 0, completionEvent);
    }

    public void animation(float position, int duration, final OnCompletionEvent completionEvent) {
        if (animationBuffer[0].getView() == null || animationBuffer[1].getView() == null) return;

        animationBuffer[1].getView().animate()
                .x(position)
                .setDuration(duration)
                .start();
        float percent = position / displayMetrics.widthPixels;
        animationBuffer[0].getView().animate()
                .x(percent * displayMetrics.widthPixels * 0.25f - displayMetrics.widthPixels * 0.25f)
                .setDuration(duration)
                .start();

        if (animationBuffer[0].getView() instanceof UIViewControllerContainer) {
            UIViewControllerContainer container = (UIViewControllerContainer) animationBuffer[0].getView();
            ObjectAnimator animator = ObjectAnimator.ofFloat(container, "foregroundShadow", container.getForegroundShadow(), (0.25f - percent * 0.25f));
            animator.setDuration(duration);
            animator.start();
        }
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (completionEvent != null) {
                    completionEvent.completionEvent();
                }
            }
        }, duration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_COUNT, controllers.size());
        for (int i = 0; i < controllers.size(); i++) {
            getChildFragmentManager().putFragment(outState, FRAGMENT+i, controllers.get(i));
        }
    }

    public interface OnCompletionEvent {
        void completionEvent();
    }

}
