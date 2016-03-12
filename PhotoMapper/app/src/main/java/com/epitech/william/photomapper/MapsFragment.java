package com.epitech.william.photomapper;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.epitech.paul.photomapper.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
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
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

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
        
        upLocatedPictureList();
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

    private void setUpMap() {
        LatLng coordinate = LocationHandler.getInstance().getCoordinate();
        // Enable location button and set my location marker
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, MAP_CAMERA_ZOOM));

        for (LocatedPicture item : mLocatedPictureList) {
            LatLng itemCd = new LatLng(item.getLatitude(),item.getLongitude());
            setNewMarker(itemCd, item.getTitle());
        }
    }

    private void setNewMarker(LatLng coordinate, String title) {
        mMap.addMarker(new MarkerOptions().position(coordinate).title(title));
    }
}
