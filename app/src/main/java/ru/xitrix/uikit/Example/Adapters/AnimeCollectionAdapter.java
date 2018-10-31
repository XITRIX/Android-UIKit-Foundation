package ru.xitrix.uikit.Example.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.xitrix.uikit.Example.Data.AnimeCollectionContent;
import ru.xitrix.uikit.Example.AnimeCollectionController.OnListFragmentInteractionListener;
import ru.xitrix.uikit.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AnimeCollectionContent.AnimeItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AnimeCollectionAdapter extends RecyclerView.Adapter<AnimeCollectionAdapter.ViewHolder> {

    private final List<AnimeCollectionContent.AnimeItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AnimeCollectionAdapter(String link, List<AnimeCollectionContent.AnimeItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        if (items.size() == 0)
            AnimeCollectionContent.getAnimeCollectionItems(link, new AnimeCollectionContent.OnCompletion() {
                @Override
                public void onCompletion(List<AnimeCollectionContent.AnimeItem> list) {
                    mValues.clear();
                    mValues.addAll(list);
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSubtitleView.setText(mValues.get(position).details);
        holder.mTitleView.setText(mValues.get(position).title);

//        Glide.with(holder.mView.getContext()).load(mValues.get(position).image).into(holder.mImageView);
        Picasso.get().load(mValues.get(position).image).into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSubtitleView;
        public final TextView mTitleView;
        public final ImageView mImageView;
        public AnimeCollectionContent.AnimeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSubtitleView = view.findViewById(R.id.item_number);
            mTitleView = view.findViewById(R.id.title);
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
