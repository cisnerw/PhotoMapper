package com.epitech.william.photomapper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.epitech.paul.photomapper.DatabaseHandler;
import com.epitech.paul.photomapper.LocatedPicture;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.epitech.paul.photomapper.LocatedPictureFragment;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LocatedPictureAdapter.OnItemClickListener {

    private Fragment mCurrentFragment = null;
    private static final int TAKE_PICTURE = 1;
    private static final String IMAGE_FORMAT = ".jpg";
    private static final String CHOOSER_WINDOW_TITLE = "";
    private static final String IMAGE_NAME = "image_";
    private static String mCurrentPhotoPath;
    private static String mCurrentFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LocationHandler.getInstance().init(this);

        new DatabaseHandler(this); // instanciate db singleton

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (mCurrentFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(mCurrentFragment).commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
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

    public void gotoMap(int position)
    {
        MapsFragment fragment = (MapsFragment) getFragmentFromMenuId(R.id.nav_map);
        fragment.setSelectedPosition(position);
        changeCurrentFragment(fragment);
    }

    private void changeCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mCurrentFragment != null) {
            fragmentManager.beginTransaction().remove(mCurrentFragment).commit();
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            mCurrentFragment = fragment;
        }
    }

    private Fragment getFragmentFromMenuId(int id) {
        Fragment fragment = null;
        Class fragmentClass;
        if (id == R.id.nav_camera) {
            takePicture();
        } else if (id == R.id.nav_gallery) {
            //openGallery();
            fragmentClass = LocatedPictureFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
            }
        } else if (id == R.id.nav_map) {
            fragmentClass = MapsFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
            }
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            share();
        } else if (id == R.id.nav_send) {

        }
        return fragment;
    }

    private void share() {
        String message = "Text I want to share.";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(share, CHOOSER_WINDOW_TITLE));
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        int nextid = DatabaseHandler.getInstance().getPicturesCount();
        mCurrentFileName = IMAGE_NAME + String.valueOf(nextid);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                mCurrentFileName,   /* prefix */
                IMAGE_FORMAT,       /* suffix */
                storageDir          /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void takePicture() {
        //TODO: get the nextId from DataBase manager.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        if (photoFile != null) {
            // Handle the camera action
            // create intent with ACTION_IMAGE_CAPTURE action
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            // start camera activity
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            //TODO: save the currentPhotoPath and the coordinate in database.
            LatLng cd = LocationHandler.getInstance().getCoordinate();
            if (imgFile.exists()) {
                LocatedPicture newPicture = new LocatedPicture(
                        mCurrentPhotoPath,
                        mCurrentFileName,
                        null,
                        cd.longitude,
                        cd.latitude);
                DatabaseHandler.getInstance().addPicture(newPicture);
            }
        }
    }
    public void onItemClick(int position){
        gotoMap(position);
    }

}
