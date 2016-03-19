package com.epitech.william.photomapper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by willi_000 on 16/03/2016.
 */
public class SocialHandler {
    private static final String SEND_TYPE = "text/plain";
    private static final String MSG_TITLE = "msg_send";
    private static SocialHandler instance = null;
    private static final String CHOOSER_WINDOW_TITLE = "";
    private static final String PACKAGE = "com.epitech.william.photomapper";
    private static final String DESCRIPTION_TITLE = "content_description";
    private static final String CONTENT_URL = "https://www.facebook.com/AndroidPhotoMapper";
    private static final String CONTENT_TITLE = "PhotoMapper";
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Activity mActivity;

    public SocialHandler(Activity context) {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(context);
        mActivity = context;
        instance = this;
    }

    public void share() {
        int id = mActivity.getResources().getIdentifier(
                DESCRIPTION_TITLE,
                "string",
                PACKAGE);
        String description = mActivity.getResources().getString(id);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(CONTENT_TITLE)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(CONTENT_URL))
                    .build();

            if (shareDialog != null && linkContent != null) {
                try {
                    shareDialog.show(linkContent);
                } catch (IllegalStateException e) {
                    Log.d("photomapper", "tset");
                }
            }
        }
    }

    public static SocialHandler getInstance() {
        return instance;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        callbackManager.onActivityResult(requestCode, resultCode, intent);
    }

    public void send() {
        int id = mActivity.getResources().getIdentifier(
                MSG_TITLE,
                "string",
                PACKAGE);
        String message = mActivity.getResources().getString(id) + CONTENT_URL;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(SEND_TYPE);
        share.putExtra(Intent.EXTRA_TEXT, message);

        mActivity.startActivity(Intent.createChooser(share, CHOOSER_WINDOW_TITLE));
    }
}
