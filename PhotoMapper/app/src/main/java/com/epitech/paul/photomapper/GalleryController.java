package com.epitech.paul.photomapper;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.epitech.william.photomapper.LocatedPictureAdapter;
import com.epitech.william.photomapper.MainActivity;

/**
 * Created by Paul on 15/03/2016.
 */
public class GalleryController implements
        GalleryFragment.OnListItemClickedListener,
        GalleryFragment.OnDeleteButtonClickedListener {

    protected boolean mDeleteMode = false;
    protected boolean mCanGotoMap = true;
    private MainActivity mMainActivity;
    private RecyclerView mRecyclerView;
    private LocatedPictureAdapter mAdapter;

    public GalleryController(MainActivity activity) {
        mMainActivity = activity;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setAdapter(LocatedPictureAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean onListItemClicked(RecyclerView.ViewHolder viewHolder, LocatedPicture locatedPicture) {
        if (mDeleteMode) {
            DatabaseHandler.getInstance().deletePicture(locatedPicture);
            mAdapter.removeViewAt(viewHolder.getAdapterPosition());
        } else if (mCanGotoMap) {
            mMainActivity.gotoMap(viewHolder.getAdapterPosition());
            return true;
        }
        return true;
    }

    @Override
    public void onDeleteButtonClicked(View v) {
        mDeleteMode = !mDeleteMode;
        if (!mDeleteMode) {
            Snackbar.make(v, "Map mode : select a picture to show it on map",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            Snackbar.make(v, "Delete mode : select a picture to delete it",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

}
