package ru.xitrix.uikit.Example.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.xitrix.uikit.Example.GenresListController;
import ru.xitrix.uikit.Example.Data.GenresListContent;

public class GengesListAdapter extends RecyclerView.Adapter<GengesListAdapter.ViewHolder> {

    private final List<GenresListContent.GenreItem> items;
    private final GenresListController.OnGenresTapListener listener;

    public GengesListAdapter(final List<GenresListContent.GenreItem> items, GenresListController.OnGenresTapListener listener) {
        this.items = items;
        this.listener = listener;

        if (items.size() == 0)
            GenresListContent.getGenresListItems(new GenresListContent.OnCompletion() {
                @Override
                public void onCompletion(List<GenresListContent.GenreItem> list) {
                    items.clear();
                    items.addAll(list);
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.item = items.get(position);
        holder.title.setText(items.get(position).title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onGenresTapListener(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public GenresListContent.GenreItem item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(android.R.id.text1);
        }
    }
}
