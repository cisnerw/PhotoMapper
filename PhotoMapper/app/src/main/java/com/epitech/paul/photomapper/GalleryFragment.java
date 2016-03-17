package com.epitech.paul.photomapper;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitech.william.photomapper.LocatedPictureAdapter;
import com.epitech.william.photomapper.R;

import java.util.List;

public class GalleryFragment extends Fragment {

    // Listener called when an item from the recycler view is clicked
    public interface OnListItemClickedListener {
        boolean onListItemClicked(RecyclerView.ViewHolder viewHolder, LocatedPicture locatedPicture);
    }

    // Listener called when the delete button is clicked
    public interface OnDeleteButtonClickedListener {
        void onDeleteButtonClicked(View v);
    }

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 2;

    private List<LocatedPicture> mLocatedPictureList;

    private GalleryView mGalleryView;

    private GalleryController mGalleryController;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GalleryFragment() {
        mLocatedPictureList = DatabaseHandler.getInstance().getAllPictures();
        mGalleryView = new GalleryView(mLocatedPictureList);
    }

    public void setController(GalleryController controller) {
        mGalleryController = controller;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GalleryFragment newInstance(int columnCount) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = mGalleryView.onCreateView(inflater, container, savedInstanceState);
        mGalleryView.setUpRecyclerView(getResources());
        mGalleryController.setRecyclerView(mGalleryView.getRecyclerView());
        mGalleryController.setAdapter(mGalleryView.getLocatedPictureAdapter());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGalleryView.setListItemClickedListener(mGalleryController);
        mGalleryView.setDeleteButtonClickedListener(mGalleryController);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGalleryView.onDetach();
    }
}
