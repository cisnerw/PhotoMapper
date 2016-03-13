package com.epitech.william.photomapper;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.epitech.paul.photomapper.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import com.epitech.paul.photomapper.LocatedPicture;

public class MapsFragment extends Fragment {

    private static final float MAP_CAMERA_ZOOM = 15;

    private List<LocatedPicture> mLocatedPictureList;
    private LocatedPictureAdapter mLocatedPictureAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FragmentActivity mFragmentActivity;
    private LinearLayout mLinearLayout;
    private MapView mMapView;
    private List<Marker> markers;
    private Marker selectedMarker = null;
    private View selectedImage = null;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int mSelectedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFragmentActivity = super.getActivity();
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = (MapView) mLinearLayout.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView) mLinearLayout.findViewById(R.id.photoList);
        mLinearLayoutManager = new LinearLayoutManager(mFragmentActivity);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLinearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            mLinearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        }
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        markers = new ArrayList<>();

        upLocatedPictureList();
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            int i = 0;

            @Override
            public void onChildViewAttachedToWindow(View view) {
                i++;
                if (i >= mLocatedPictureList.size()) {
                    if (mSelectedPosition == 0) {
                        changeSelectedMarker(mSelectedPosition);
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                i--;
            }
        });

        setUpRecyclerView();
        setUpMapIfNeeded();

        return mLinearLayout;
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        setUpMapIfNeeded();
    }

    /*
    ** update picture list from local database
     */
    private void upLocatedPictureList() {
        //TODO: get the picture list from the database
        mLocatedPictureList = DatabaseHandler.getInstance().getAllPictures();
    }

    private void setUpRecyclerView() {
        mLocatedPictureAdapter = new LocatedPictureAdapter(mLocatedPictureList, getResources());
        mLocatedPictureAdapter.setItemClickListener(new LocatedPictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeSelectedMarker(position);
            }
        });
        mRecyclerView.setAdapter(mLocatedPictureAdapter);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the MapView.
            if (mMapView != null) {
                mMap = mMapView.getMap();
            }
            setUpMap();
        }
    }

    private void changeSelectedMarker(int position) {
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(position);
        View view = holder.itemView;
        Marker marker = markers.get(position);
        if (selectedMarker != null)
            selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
        if (selectedImage != null)
            selectedImage.setSelected(false);
        if (selectedMarker != marker) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), MAP_CAMERA_ZOOM));
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            view.setSelected(true);
            selectedImage = view;
            selectedMarker = marker;
        } else {
            selectedMarker = null;
            selectedImage = null;
        }
    }

    private void setUpMap() {
        LatLng coordinate = LocationHandler.getInstance().getCoordinate();
        // Enable location button and set my location marker
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        for (LocatedPicture item : mLocatedPictureList) {
            LatLng itemCd = new LatLng(item.getLatitude(),item.getLongitude());
            setNewMarker(itemCd, item.getTitle());
        }
    }

    private void setNewMarker(LatLng coordinate, String title) {
        MarkerOptions marker = new MarkerOptions().position(coordinate).title(title);
        markers.add(mMap.addMarker(marker));
    }

    private void moveCamera(LatLng coordinate) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, MAP_CAMERA_ZOOM));
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

}
