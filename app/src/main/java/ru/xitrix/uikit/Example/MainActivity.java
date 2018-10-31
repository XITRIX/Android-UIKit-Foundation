package ru.xitrix.uikit.Example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import ru.xitrix.uikit.R;
import ru.xitrix.uikit.UIKit.UINavigationController;
import ru.xitrix.uikit.UIKit.UITabBarController;

public class MainActivity extends AppCompatActivity {

    private static final String ROOT = "Root";
    UITabBarController tabBarController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_holder);

        if (savedInstanceState != null) {
            tabBarController = (UITabBarController) getSupportFragmentManager().getFragment(savedInstanceState, ROOT);
        } else {
            Fragment[] fragments = {
                    UINavigationController.init(AnimeCollectionController.init("http://findanime.me/list")),
                    UINavigationController.init(GenresListController.init())
            };
            tabBarController = UITabBarController.init(R.menu.navigation, fragments);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, tabBarController).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, ROOT, tabBarController);
    }
}
