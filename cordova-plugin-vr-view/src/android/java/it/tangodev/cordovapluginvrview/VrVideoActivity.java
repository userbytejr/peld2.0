package it.tangodev.cordovapluginvrview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.vr.sdk.widgets.common.FullScreenDialog;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.google.vr.sdk.widgets.video.VrVideoView.Options;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.google.vr.sdk.widgets.common.VrWidgetView.DisplayMode.EMBEDDED;
import static com.google.vr.sdk.widgets.common.VrWidgetView.DisplayMode.FULLSCREEN_MONO;

public class VrVideoActivity extends Activity {
    public static final int VIDEO_STATUS_IDLE = 0;
    public static final int VIDEO_STATUS_LOADING = 1;
    public static final int VIDEO_STATUS_LOADED = 2;
    public static final int VIDEO_STATUS_PLAYING = 3;
    public static final int VIDEO_STATUS_PAUSED = 4;

    private static final String TAG = VrVideoActivity.class.getSimpleName();

    private VrVideoView vrVideoView;
    private CircularProgressView loadingIndicatorView;
    private ImageView pauseIndicatorView;
    private int currentVideoStatus = VIDEO_STATUS_IDLE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("vr_video_activity_layout", "layout", getPackageName()));

        vrVideoView = (VrVideoView) findViewById(getResources().getIdentifier("vrVideoView", "id", getPackageName()));
        vrVideoView.setInfoButtonEnabled(false);
        vrVideoView.setDisplayMode(FULLSCREEN_MONO);
        vrVideoView.setEventListener(new VrVideoEventListener() {

            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
                Log.d(TAG, "Video loaded");
                currentVideoStatus = VIDEO_STATUS_LOADED;
                playVideo();
            }

            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
                Log.e(TAG, "Error loading video: " + errorMessage);
                currentVideoStatus = VIDEO_STATUS_IDLE;
                onError();
            }

            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                super.onDisplayModeChanged(newDisplayMode);
                if(newDisplayMode == EMBEDDED) {
                    finish();
                }
            }

            @Override
            public void onClick() {
                super.onClick();
                toggleVideoPlay();
            }

            @Override
            public void onCompletion() {
                super.onCompletion();
                finish();
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

    public void toggleVideoPlay() {
        if(currentVideoStatus == VIDEO_STATUS_PAUSED) {
            playVideo();
        } else {
            pauseVideo();
        }
    }

    private void loadVideo(Uri videoFileUri, String assetsFilePath, Options videoOptions) {
        if(currentVideoStatus == VIDEO_STATUS_IDLE) {
            try {
                Log.d(TAG, "Loading video: " + videoFileUri);
                currentVideoStatus = VIDEO_STATUS_LOADING;
                showLoadingIndicator(true);
                if(videoFileUri != null) {
                    vrVideoView.loadVideo(videoFileUri, videoOptions);
                } else {
                    vrVideoView.loadVideoFromAsset(assetsFilePath, videoOptions);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error loading video", e);
                currentVideoStatus = VIDEO_STATUS_IDLE;
                onError();
            }
        }
    }

    public void pauseVideo() {
        if(currentVideoStatus == VIDEO_STATUS_PLAYING) {
            vrVideoView.pauseVideo();
            currentVideoStatus = VIDEO_STATUS_PAUSED;
            showPauseIndicator(true);
        }
    }

    public void playVideo() {
        if(currentVideoStatus == VIDEO_STATUS_LOADED || currentVideoStatus == VIDEO_STATUS_PAUSED) {
            Log.d(TAG, "Video playing");
            vrVideoView.playVideo();
            currentVideoStatus = VIDEO_STATUS_PLAYING;
            showLoadingIndicator(false);
            showPauseIndicator(false);
        }
    }

    private void initCustomUiElements() {
        try {
            RelativeLayout uiView = Utils.getUiView(this, vrVideoView);
            loadingIndicatorView = Utils.inflateLoadingIndicator(this, uiView);
            pauseIndicatorView = Utils.inflatePauseIndicator(this, uiView);
        } catch (Exception e) {
            Log.e(TAG, "ERROR initCustomUiElements", e);
        }
    }

    private void showLoadingIndicator(boolean show) {
        if(loadingIndicatorView != null) {
            Utils.fadeView(loadingIndicatorView, show);
        }
    }

    private void showPauseIndicator(boolean show) {
        if(pauseIndicatorView != null) {
            Utils.fadeView(pauseIndicatorView, show);
        }
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "Intent received");
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri videoFileUri = intent.getData();
            String videoAssetPath = intent.getStringExtra("videoAssetPath");
            if (videoFileUri == null && videoAssetPath == null) {
                Log.e(TAG, "VideoFileUri & videoAssetPath are null");
                onError();
                return;
            }

            vrVideoView.setDisplayMode(intent.getIntExtra("startDisplayMode", FULLSCREEN_MONO));

            Options videoOptions = new Options();
            videoOptions.inputType = intent.getIntExtra("inputType", VrVideoView.Options.TYPE_MONO);
            videoOptions.inputFormat = intent.getIntExtra("inputFormat", VrVideoView.Options.FORMAT_DEFAULT);

            loadVideo(videoFileUri, videoAssetPath, videoOptions);
        } else {
            Log.e(TAG, "Action not supported");
            onError();
        }
    }

    private void onError() {
        showLoadingIndicator(false);
        Toast toast = Toast.makeText(this, "Error while loading video", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vrVideoView.pauseRendering();
        pauseVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrVideoView.resumeRendering();
        playVideo();
    }

    @Override
    protected void onDestroy() {
        vrVideoView.shutdown();
        super.onDestroy();
    }
}