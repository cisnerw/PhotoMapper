package com.epitech.william.photomapper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.epitech.paul.photomapper.DatabaseHandler;
import com.epitech.paul.photomapper.GalleryController;
import com.epitech.paul.photomapper.LocatedPicture;
import com.google.android.gms.maps.model.LatLng;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.epitech.paul.photomapper.GalleryFragment;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
//        , LocatedPictureAdapter.OnItemClickListener
{

    private PictureCaptureHandler mPictureCaptureHandler;
    private Fragment mCurrentFragment = null;
    private FragmentManager mFragmentManager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FacebookSdk.sdkInitialize(this);

        mPictureCaptureHandler = new PictureCaptureHandler(this);
        mFragmentManager = getSupportFragmentManager();

        new LocationHandler(this);  // instanciate location singleton
        new DatabaseHandler(this);  // instanciate db singleton
        new SocialHandler(this);    // instanciate social singleton

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mCurrentFragment != null) {
            mFragmentManager.beginTransaction().remove(mCurrentFragment).commit();
            mCurrentFragment = null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = getFragmentFromMenuId(id);
        changeCurrentFragment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotoMap(int position) {
        MapsFragment fragment = (MapsFragment) getFragmentFromMenuId(R.id.nav_map);
        fragment.setSelectedPosition(position);
        changeCurrentFragment(fragment);
    }

    private void changeCurrentFragment(Fragment fragment) {
        if (mCurrentFragment != null) {
            mFragmentManager.beginTransaction().remove(mCurrentFragment).commit();
        }
        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            mCurrentFragment = fragment;
        }
    }

    private Fragment getFragmentFromMenuId(int id) {
        Fragment fragment = null;
        Class fragmentClass;
        if (id == R.id.nav_camera) {
            mPictureCaptureHandler.takePicture();
        } else if (id == R.id.nav_gallery) {
            //openGallery();
            fragmentClass = GalleryFragment.class;
            try {
                GalleryFragment galleryFragment = (GalleryFragment) fragmentClass.newInstance();
                galleryFragment.setController(new GalleryController(this));
                fragment = galleryFragment;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_map) {
            fragmentClass = MapsFragment.class;
            try {
                MapsFragment mapsFragment = (MapsFragment) fragmentClass.newInstance();
                mapsFragment.setController(new MapsController(this, mapsFragment));
                fragment = mapsFragment;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_home && mCurrentFragment != null) {
            mFragmentManager.beginTransaction().remove(mCurrentFragment).commit();
            mCurrentFragment = null;
        } else if (id == R.id.nav_share) {
            SocialHandler.getInstance().share();
        } else if (id == R.id.nav_send) {
            SocialHandler.getInstance().send();
        }
        return fragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        SocialHandler.getInstance().onActivityResult(requestCode,resultCode,intent);
        if (requestCode == PictureCaptureHandler.TAKE_PICTURE && resultCode == RESULT_OK) {
            mPictureCaptureHandler.onPictureResult();
        }
    }

    public void onItemClick(int position) {
        gotoMap(position);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.epitech.william.photomapper/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.epitech.william.photomapper/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}
