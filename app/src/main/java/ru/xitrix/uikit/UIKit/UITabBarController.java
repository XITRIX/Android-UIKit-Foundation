package ru.xitrix.uikit.UIKit;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.xitrix.uikit.R;

public class UITabBarController extends Fragment {

    private static final String MENU = "Menu";
    private static final String FRAGMENT = "fragment";
    private static final String FRAGMENT_COUNT = "fragment_count";
    private static final String TABBAR_STATE = "tabbar_state";

    private int menu;
    private Fragment[] rootFragments;

    private BottomNavigationView tabBar;

    private BottomNavigationView.OnNavigationItemSelectedListener tabBarSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            for (int i = 0; i < tabBar.getMenu().size(); i++) {
                if (tabBar.getMenu().getItem(i).getItemId() == item.getItemId()) {
                    getChildFragmentManager().beginTransaction().replace(R.id.tabbar_container, rootFragments[i]).addToBackStack(null).commit();
                    return true;
                }
            }
            return false;
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menu          Menu resource for Tab Bar
     * @param rootFragments Map of ResIDs of tabs and fragments for it.
     * @return A new instance of fragment UITabBarController.
     */

    public static UITabBarController init(@MenuRes int menu, Fragment[] rootFragments) {
        UITabBarController view = new UITabBarController();
        Bundle bundle = new Bundle();
        bundle.putInt(MENU, menu);
        view.rootFragments = rootFragments;
        view.setArguments(bundle);
        return view;
    }

    public UITabBarController() {
    }

    private Integer selectItemId = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            menu = getArguments().getInt(MENU);
        }

        if (savedInstanceState != null) {
            rootFragments = new Fragment[savedInstanceState.getInt(FRAGMENT_COUNT)];
            for (int i = 0; i < savedInstanceState.getInt(FRAGMENT_COUNT); i++) {
                rootFragments[i] = getChildFragmentManager().getFragment(savedInstanceState, FRAGMENT + i);
            }
            selectItemId = savedInstanceState.getInt(TABBAR_STATE);
        }

        for (Fragment rootFragment : rootFragments) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.tabbar_container, rootFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uitabbar, container, false);

        tabBar = view.findViewById(R.id.tabBarView);
        tabBar.setOnNavigationItemSelectedListener(tabBarSelectedListener);
        tabBar.inflateMenu(menu);

        if (tabBar.getMenu().size() != rootFragments.length) {
            throw new RuntimeException(view.getContext().toString()
                    + " Menu items count and Fragments length must be equal");
        }

        if (selectItemId == null) {
            selectItemId = tabBar.getMenu().getItem(0).getItemId();
        }

        tabBar.setSelectedItemId(selectItemId);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TABBAR_STATE, tabBar.getSelectedItemId());

        outState.putInt(FRAGMENT_COUNT, rootFragments.length);
        for (int i = 0; i < rootFragments.length; i++) {
            if (rootFragments[i] != null)
                getChildFragmentManager().putFragment(outState, FRAGMENT + i, rootFragments[i]);
        }
    }
}
