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

    private static final String MMOD_TITLE = "map_mode";
    private static final String PACKAGE = "com.epitech.william.photomapper";
    private static final String DMOD_TITLE = "delete_mode";
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
        String title;

        if (!mDeleteMode) {
            v.setSelected(false);
            title = MMOD_TITLE;
        } else {
            v.setSelected(true);
            title = DMOD_TITLE;
        }
        int id = mMainActivity.getResources().getIdentifier(
                title,
                "string",
                PACKAGE);
        String message = mMainActivity.getResources().getString(id);
        Snackbar.make(v, message,Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
