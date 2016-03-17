package com.epitech.paul.photomapper;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitech.william.photomapper.LocatedPictureAdapter;
import com.epitech.william.photomapper.R;

import java.util.List;

/**
 * Created by Paul on 15/03/2016.
 */
public class GalleryView {

    // views
    private AutofitRecyclerView mRecyclerView;
    private FloatingActionButton mDeleteButton;

    private List<LocatedPicture> mLocatedPictureList;
    private GalleryFragment.OnListItemClickedListener mOnListItemClickedListener;
    private GalleryFragment.OnDeleteButtonClickedListener mOnDeleteButtonClickedListener;
    private LocatedPictureAdapter mLocatedPictureAdapter;

    public GalleryView(List<LocatedPicture> pictures) {
        mLocatedPictureList = pictures;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return onCreateInflatedView(inflater.inflate(R.layout.fragment_locatedpicture, container, false), container.getResources());
    }

    public View onCreateInflatedView(View view, Resources resources) {
        View listView = view.findViewById(R.id.locatedpicture_list);

        FloatingActionButton deleteButton = (FloatingActionButton) view.findViewById(R.id.fab_deleteMode);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeleteButtonClickedListener.onDeleteButtonClicked(v);
            }
        });

        // Set the adapter
        if (listView instanceof AutofitRecyclerView) {
            Context context = listView.getContext();
            mRecyclerView = (AutofitRecyclerView) listView;
        }
        return view;
    }

    public void setUpRecyclerView(Resources resources) {
        mLocatedPictureAdapter = new LocatedPictureAdapter(mLocatedPictureList, resources);
        mLocatedPictureAdapter.setItemClickListener(mOnListItemClickedListener);
        mRecyclerView.setAdapter(mLocatedPictureAdapter);
    }

    public void setListItemClickedListener(GalleryFragment.OnListItemClickedListener listener) {
        mOnListItemClickedListener = listener;
    }

    public void setDeleteButtonClickedListener(GalleryFragment.OnDeleteButtonClickedListener listener) {
        mOnDeleteButtonClickedListener = listener;
    }

    public void onDetach() {
        mOnListItemClickedListener = null;
        mOnDeleteButtonClickedListener = null;
    }

    public AutofitRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public LocatedPictureAdapter getLocatedPictureAdapter() {
        return mLocatedPictureAdapter;
    }
}
