package com.epitech.william.photomapper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private static final float MAP_CAMERA_ZOOM = 15;

    private List<LocatedPicture> mLocatedPictureList;
    private LocatedPictureAdapter mLocatedPictureAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FragmentActivity mFragmentActivity;
    private LinearLayout mLinearLayout;
    private MapView mMapView;
    private LocationHandler mLocationHandler;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentActivity = super.getActivity();
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_maps, container, false);
        mLocationHandler = LocationHandler.getInstance();
        mLocationHandler.init(mFragmentActivity);
        mMapView = (MapView) mLinearLayout.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView) mLinearLayout.findViewById(R.id.photoList);
        mLinearLayoutManager = new LinearLayoutManager(mFragmentActivity);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mLocatedPictureList = new ArrayList<>();
        
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
    ** get picture list from local data base
     */
    private void upLocatedPictureList() {
        mLocatedPictureList.clear();
    }

    private void setUpRecyclerView() {
        mLocatedPictureAdapter = new LocatedPictureAdapter(mLocatedPictureList);
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
        LatLng coordinate = mLocationHandler.getCoordinate();
        // Enable location button and set my location marker
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, MAP_CAMERA_ZOOM));

        for (LocatedPicture item : mLocatedPictureList) {
            LatLng itemCd = new LatLng(item.mLatitude,item.mLongitude);
            setNewMarker(itemCd, String.valueOf(item.mId));
        }
    }

    private void setNewMarker(LatLng coordinate, String title) {
        mMap.addMarker(new MarkerOptions().position(coordinate).title(title));
    }
}
