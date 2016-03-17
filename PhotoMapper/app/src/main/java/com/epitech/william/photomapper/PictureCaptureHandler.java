package com.epitech.william.photomapper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.epitech.paul.photomapper.DatabaseHandler;
import com.epitech.paul.photomapper.LocatedPicture;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;

/**
 * Created by willi_000 on 16/03/2016.
 */
public class PictureCaptureHandler {
    public static final int TAKE_PICTURE = 1;
    private static final String IMAGE_FORMAT = ".jpg";
    private static final String IMAGE_NAME = "image_";
    private static String mCurrentPhotoPath;
    private static String mCurrentFileName;

    private Activity mActivity;

    public PictureCaptureHandler(Activity activity) {
        mActivity = activity;
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

    public void takePicture() {
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
            mActivity.startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    public void onPictureResult() {

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
