package com.epitech.william.photomapper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

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

            shareDialog.show(linkContent);
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

/*    private void printHashKey() {
        PackageInfo packageInfo;
        String key;

        String packageName = getApplicationContext().getPackageName();
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.e("Packege name = ", packageName);

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key Hashes = ", key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/
}
