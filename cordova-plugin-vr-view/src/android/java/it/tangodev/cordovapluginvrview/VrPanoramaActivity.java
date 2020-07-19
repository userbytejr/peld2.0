package it.tangodev.cordovapluginvrview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.vr.sdk.widgets.common.FullScreenDialog;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView.Options;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

import static com.google.vr.sdk.widgets.common.VrWidgetView.DisplayMode.EMBEDDED;
import static com.google.vr.sdk.widgets.common.VrWidgetView.DisplayMode.FULLSCREEN_MONO;

public class VrPanoramaActivity extends Activity {
    public static final int PHOTO_STATUS_IDLE = 0;
    public static final int PHOTO_STATUS_LOADING = 1;
    public static final int PHOTO_STATUS_LOADED = 2;

    private static final String TAG = VrPanoramaActivity.class.getSimpleName();

    private VrPanoramaView vrPanoramaView;
    private CircularProgressView loadingIndicatorView;
    private ImageLoaderTask imageLoaderTask;

    private int currentPhotoStatus = PHOTO_STATUS_IDLE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("vr_panorama_activity_layout", "layout", getPackageName()));

        vrPanoramaView = (VrPanoramaView) findViewById(getResources().getIdentifier("vrPanoramaView", "id", getPackageName()));
        vrPanoramaView.setInfoButtonEnabled(false);
        vrPanoramaView.setDisplayMode(FULLSCREEN_MONO);
        vrPanoramaView.setEventListener(new VrPanoramaEventListener() {

            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
                Log.d(TAG, "Photo loaded");
                currentPhotoStatus = PHOTO_STATUS_LOADED;
                showLoadingIndicator(false);
            }

            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
                Log.e(TAG, "Error loading photo: " + errorMessage);
                currentPhotoStatus = PHOTO_STATUS_IDLE;
                onError();
            }

            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                super.onDisplayModeChanged(newDisplayMode);
                if(newDisplayMode == EMBEDDED) {
                    finish();
                }
            }
        });
        initCustomUiElements();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void loadPhoto (Uri photoFileUri, String photoAssetsPath, Options photoOptions) {
        if(currentPhotoStatus == PHOTO_STATUS_IDLE) {
            currentPhotoStatus = PHOTO_STATUS_LOADING;
            showLoadingIndicator(true);
            if (imageLoaderTask != null) {
                imageLoaderTask.cancel(true);
            }
            imageLoaderTask = new ImageLoaderTask(getApplicationContext(), photoFileUri, photoAssetsPath, photoOptions);
            imageLoaderTask.execute();
        }
    }

    private void initCustomUiElements() {
        try {
            RelativeLayout uiView = Utils.getUiView(this, vrPanoramaView);
            loadingIndicatorView = Utils.inflateLoadingIndicator(this, uiView);
        } catch (Exception e) {
            Log.e(TAG, "ERROR initCustomUiElements", e);
        }
    }

    private void showLoadingIndicator(boolean show) {
        if(loadingIndicatorView != null) {
            Utils.fadeView(loadingIndicatorView, show);
        }
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "Intent received");
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri photoFileUri = intent.getData();
            String photoAssetPath = intent.getStringExtra("photoAssetPath");
            if (photoFileUri == null && photoAssetPath == null) {
                Log.e(TAG, "PhotoFileUri & photoAssetPath are null");
                onError();
                return;
            }

            vrPanoramaView.setDisplayMode(intent.getIntExtra("startDisplayMode", FULLSCREEN_MONO));

            Options photoOptions = new Options();
            photoOptions.inputType = intent.getIntExtra("inputType", Options.TYPE_MONO);

            loadPhoto(photoFileUri, photoAssetPath, photoOptions);
        } else {
            Log.e(TAG, "Action not supported");
            onError();
        }
    }

    private void onError() {
        showLoadingIndicator(false);
        Toast toast = Toast.makeText(this, "Error while loading photo", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @Override
    protected void onPause() {
        vrPanoramaView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrPanoramaView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        vrPanoramaView.shutdown();
        if (imageLoaderTask != null) {
            imageLoaderTask.cancel(true);
        }
        super.onDestroy();
    }

    private class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
        private Context context;
        private Uri photoFileUri;
        private String photoAssetPath;
        private Options options;

        ImageLoaderTask(Context context, Uri photoFileUri, String photoAssetPath, Options options) {
            this.context = context;
            this.photoFileUri = photoFileUri;
            this.photoAssetPath = photoAssetPath;
            this.options = options;
        }

        @Override
        protected Bitmap doInBackground(Void ... voids) {
            InputStream inputStream = null;
            try {
                if(photoFileUri != null) {
                    Log.d(TAG, "Loading photo: " + photoFileUri.toString());
                    if(photoFileUri.getScheme().equals("content")) {
                        inputStream = getApplicationContext().getContentResolver().openInputStream(photoFileUri);
                    } else {
                        inputStream = new URL(photoFileUri.toString()).openStream();
                    }
                } else {
                    Log.d(TAG, "Loading photo: " + photoAssetPath);
                    AssetManager assetManager = context.getAssets();
                    inputStream = assetManager.open(photoAssetPath);
                }
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Log.e(TAG, "Could not load photo: " + photoFileUri, e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close input stream: " + e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null) {
                vrPanoramaView.loadImageFromBitmap(bitmap, options);
            } else {
                currentPhotoStatus = PHOTO_STATUS_IDLE;
                onError();
            }
        }
    }
}
