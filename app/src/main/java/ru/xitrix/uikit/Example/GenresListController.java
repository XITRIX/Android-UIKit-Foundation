package ru.xitrix.uikit.Example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.xitrix.uikit.Example.Adapters.GengesListAdapter;
import ru.xitrix.uikit.Example.Data.GenresListContent;
import ru.xitrix.uikit.R;
import ru.xitrix.uikit.UIKit.UIViewController;
import ru.xitrix.uikit.UIKit.View.UIViewControllerContainer;

public class GenresListController extends UIViewController {
    private static final String COLLECTION = "Collection";

    List<GenresListContent.GenreItem> collection = new ArrayList<>();
    OnGenresTapListener onGenresTapListener = new OnGenresTapListener() {
        @Override
        public void onGenresTapListener(GenresListContent.GenreItem item) {
            UIViewController controller = AnimeCollectionController.init("http://findanime.me" + item.link, 4);
            controller.setTitle(item.title);
            present(controller);
        }
    };

    public static GenresListController init() {
        return new GenresListController();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Genres");
        if (savedInstanceState != null) {
            collection = (List<GenresListContent.GenreItem>) savedInstanceState.getSerializable(COLLECTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UIViewControllerContainer view = createView(inflater, container,  R.layout.fragment_item_list);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new GengesListAdapter(collection, onGenresTapListener));

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(COLLECTION, (Serializable) collection);
    }

    public interface OnGenresTapListener {
        void onGenresTapListener(GenresListContent.GenreItem item);
    }
}
