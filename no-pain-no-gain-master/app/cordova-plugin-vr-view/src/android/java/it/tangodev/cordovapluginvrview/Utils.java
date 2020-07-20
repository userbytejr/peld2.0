package it.tangodev.cordovapluginvrview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.vr.sdk.widgets.common.FullScreenDialog;
import com.google.vr.sdk.widgets.common.VrWidgetView;

import java.lang.reflect.Field;

public class Utils {

    public static void fadeView(final View view, boolean in) {
        int duration = 200;
        if(in && view.getVisibility() != View.VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(duration);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        } else if (!in && view.getVisibility() == View.VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(duration);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            view.startAnimation(animation);
        }
    }

    public static RelativeLayout getUiView(Activity activity, VrWidgetView vrWidgetView) throws Exception {
        // grab the full screen dialog instance
        Field field = vrWidgetView.getClass().getSuperclass().getDeclaredField("fullScreenDialog");
        field.setAccessible(true);
        FullScreenDialog fullScreenDialog = (FullScreenDialog) field.get(vrWidgetView);

        // grab controls container
        RelativeLayout uiView = (RelativeLayout) fullScreenDialog.findViewById(activity.getResources().getIdentifier("control_layout", "id", activity.getPackageName())).getParent();
        return uiView;
    }

    public static CircularProgressView inflateLoadingIndicator(Activity activity, RelativeLayout uiView) {
        LayoutInflater inflater = (LayoutInflater) uiView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate loading indicator
        inflater.inflate(activity.getResources().getIdentifier("vr_loading_indicator_layout", "layout", activity.getPackageName()), uiView, true);
        CircularProgressView loadingIndicatorView = (CircularProgressView) uiView.findViewById(activity.getResources().getIdentifier("vr_loading_indicator", "id", activity.getPackageName()));
        return loadingIndicatorView;
    }

    public static ImageView inflatePauseIndicator(Activity activity, RelativeLayout uiView) {
        LayoutInflater inflater = (LayoutInflater) uiView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate pause indicator
        inflater.inflate(activity.getResources().getIdentifier("vr_pause_indicator_layout", "layout", activity.getPackageName()), uiView, true);
        ImageView pauseIndicatorView = (ImageView) uiView.findViewById(activity.getResources().getIdentifier("vr_pause_indicator", "id", activity.getPackageName()));
        return pauseIndicatorView;
    }
}