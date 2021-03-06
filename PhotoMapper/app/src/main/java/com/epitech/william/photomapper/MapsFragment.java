package com.epitech.william.photomapper;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.epitech.paul.photomapper.AutofitRecyclerView;
import com.epitech.paul.photomapper.DatabaseHandler;
import com.epitech.paul.photomapper.GalleryController;
import com.epitech.paul.photomapper.GalleryView;
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
    private static final long DELAYED = 50;

    private List<LocatedPicture> mLocatedPictureList;
    private LocatedPictureAdapter mLocatedPictureAdapter;
    private AutofitRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentActivity mFragmentActivity;
    private LinearLayout mLinearLayout;
    private MapView mMapView;
    private List<Marker> markers;
    private Marker selectedMarker = null;
    private View selectedImage = null;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int mSelectedPosition = -1;
    private MapsController mMapsController;
    private GalleryView mGalleryView;

    public MapsFragment() {
        upLocatedPictureList();
        mGalleryView = new GalleryView(mLocatedPictureList);
    }

    public void setController(MapsController controller) {
        mMapsController = controller;
        mGalleryView.setDeleteButtonClickedListener(mMapsController);
        mGalleryView.setListItemClickedListener(mMapsController);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFragmentActivity = super.getActivity();
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = (MapView) mLinearLayout.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mGalleryView.onCreateInflatedView(mLinearLayout.findViewById(R.id.fragment_locatedpicture_layout), container.getResources());
        mRecyclerView = mGalleryView.getRecyclerView();
        mLayoutManager = mRecyclerView.getLayoutManager();
        mGalleryView.setUpRecyclerView(getResources());
        mMapsController.setRecyclerView(mGalleryView.getRecyclerView());
        mMapsController.setAdapter(mGalleryView.getLocatedPictureAdapter());

//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mLinearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
//        } else {
//            mLinearLayoutManager.setOrientation(LinearLayout.VERTICAL);
//        }
        markers = new ArrayList<>();


        setUpMapIfNeeded();

        return mLinearLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        mLocatedPictureList = DatabaseHandler.getInstance().getAllPictures();
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

    public void changeSelectedMarker(int position) {
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            postDelayedMarkerChange(position);
            return;
        }
        View view = holder.itemView;
        Marker marker = markers.get(position);
        if (selectedMarker != null)
            selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
        if (selectedImage != null)
            selectedImage.setSelected(false);
        if (selectedMarker != marker) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), MAP_CAMERA_ZOOM));
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, MAP_CAMERA_ZOOM));

        for (LocatedPicture item : mLocatedPictureList) {
            LatLng itemCd = new LatLng(item.getLatitude(),item.getLongitude());
            setNewMarker(itemCd, item.getTitle());
        }

        if (mSelectedPosition >= 0) {
            postDelayedMarkerChange(mSelectedPosition);
        }
    }

    public void deleteMarker(int position) {
        Marker item = markers.get(position);
        markers.remove(position);
        item.remove();
    }

    private void postDelayedMarkerChange(final int position) {
        Handler handler = new Handler();

        mLayoutManager.scrollToPosition(position);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeSelectedMarker(position);
            }
        }, DELAYED);
    }

    private void setNewMarker(LatLng coordinate, String title) {
        MarkerOptions marker = new MarkerOptions().position(coordinate).title(title);
        markers.add(mMap.addMarker(marker));
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

}
