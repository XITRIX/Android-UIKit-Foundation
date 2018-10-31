package ru.xitrix.uikit.Example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.xitrix.uikit.Example.Adapters.AnimeCollectionAdapter;
import ru.xitrix.uikit.Example.Data.AnimeCollectionContent;
import ru.xitrix.uikit.R;
import ru.xitrix.uikit.UIKit.UIViewController;

public class AnimeCollectionController extends UIViewController {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String COLLECTION = "Collection";
    private static final String LINK = "Link";
    private int mColumnCount = 1;
    private String link;
    private OnListFragmentInteractionListener mListener = new OnListFragmentInteractionListener() {
        @Override
        public void onListFragmentInteraction(AnimeCollectionContent.AnimeItem item) {
            UIViewController controller = AnimeCollectionController.init(link, mColumnCount + 1);
            controller.setTitle("Окно " + (mColumnCount + 1));
            present(controller);
        }
    };

    List<AnimeCollectionContent.AnimeItem> collection = new ArrayList<>();

    public static AnimeCollectionController init(String link) {
        return init(link, 4);
    }

    public static AnimeCollectionController init(String link, int columnCount) {
        AnimeCollectionController fragment = new AnimeCollectionController();
        fragment.setTitle("Anime catalog");
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            link = getArguments().getString(LINK);
        }

        if (savedInstanceState != null) {
            collection = (List<AnimeCollectionContent.AnimeItem>) savedInstanceState.getSerializable(COLLECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, container, R.layout.fragment_item_list);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        recyclerView.setAdapter(new AnimeCollectionAdapter(link, collection, mListener));
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(COLLECTION, (Serializable) collection);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(AnimeCollectionContent.AnimeItem item);
    }
}
