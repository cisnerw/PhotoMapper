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
import com.epitech.william.photomapper.MainActivity;
import com.epitech.william.photomapper.R;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocatedPictureFragment extends Fragment implements View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private LocatedPictureAdapter mLocatedPictureAdapter;
    private List<LocatedPicture> mLocatedPictureList;
    private LocatedPictureAdapter.OnItemClickListener mListener;
    private RecyclerView mRecyclerView;
    private boolean mDeleteMode;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocatedPictureFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LocatedPictureFragment newInstance(int columnCount) {
        LocatedPictureFragment fragment = new LocatedPictureFragment();
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
        View view = inflater.inflate(R.layout.fragment_locatedpicture, container, false);
        
        View listView = view.findViewById(R.id.list);

        FloatingActionButton deleteButton = (FloatingActionButton) view.findViewById(R.id.fab_deleteMode);
        deleteButton.setOnClickListener(this);

        // Set the adapter
        if (listView instanceof RecyclerView) {
            Context context = listView.getContext();
            mRecyclerView = (RecyclerView) listView;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mLocatedPictureList = DatabaseHandler.getInstance().getAllPictures();

            setUpRecyclerView();
        }
        return view;
    }

    public boolean isInDeleteMode() {
        return mDeleteMode;
    }

    public void onClick(View v) {

        if (mDeleteMode) {
            // delete selected images
        } else {
            Snackbar.make(v, "Delete mode : select picture and click this button to delete them",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        mDeleteMode = !mDeleteMode;
    }

    private void setUpRecyclerView() {
        mLocatedPictureAdapter = new LocatedPictureAdapter(mLocatedPictureList, getResources());
        mLocatedPictureAdapter.setItemClickListener(mListener);
        mRecyclerView.setAdapter(mLocatedPictureAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LocatedPictureAdapter.OnItemClickListener) {
            mListener = (LocatedPictureAdapter.OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int position);
    }
}
