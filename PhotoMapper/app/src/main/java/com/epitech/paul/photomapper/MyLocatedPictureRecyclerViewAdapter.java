package com.epitech.paul.photomapper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitech.paul.photomapper.LocatedPictureFragment.OnListFragmentInteractionListener;

import java.io.File;
import java.util.List;
import com.epitech.paul.photomapper.LocatedPictureFragment;
import com.epitech.william.photomapper.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LocatedPicture} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLocatedPictureRecyclerViewAdapter extends RecyclerView.Adapter<MyLocatedPictureRecyclerViewAdapter.ViewHolder> {

    private final List<LocatedPicture> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Resources mResources;

    public MyLocatedPictureRecyclerViewAdapter(List<LocatedPicture> items, OnListFragmentInteractionListener listener, Resources resources) {
        mValues = items;
        mListener = listener;
        mResources = resources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_locatedpicture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LocatedPicture item = mValues.get(position);
        holder.mItem = item;
        holder.mIdView.setText(item.getTitle());

        File imgFile = new File(item.getPicturePath());
        if (imgFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(item.getPicturePath(), options);
            options.inSampleSize = Math.max(1, (int)(options.outWidth / mResources.getDimension(R.dimen.thumbnail_size)));
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(item.getPicturePath(), options);
            holder.mContentView.setImageBitmap(bitmap);
        }

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
        public final TextView mIdView;
        public final ImageView mContentView;
        public LocatedPicture mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (ImageView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getTitle() + "'";
        }
    }
}
