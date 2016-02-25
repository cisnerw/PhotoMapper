package com.epitech.william.photomapper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private static final float MAP_CAMERA_ZOOM = 15;
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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng coordinate = mLocationHandler.getCoordinate();
        // Enable location button and set my location marker
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, MAP_CAMERA_ZOOM));
    }

    private void setNewMarker(LatLng coordinate, String title) {
        mMap.addMarker(new MarkerOptions().position(coordinate).title(title));
    }
}
