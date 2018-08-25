package com.volleyandtracker;

import android.app.Application;

import com.ak.app.wetzel.activity.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.util.ClientSSLSocketFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by gjokoP on 12/12/2017.
 */

public class VolleyControllerAndTracker extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public static final String TAG = VolleyControllerAndTracker.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    //private ImageLoader mImageLoader;

    private static VolleyControllerAndTracker mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    public static synchronized VolleyControllerAndTracker getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack(null, ClientSSLSocketFactory.getSocketFactory(getApplicationContext())));
        }

        return mRequestQueue;
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    /*    public ImageLoader getImageLoader() {
            getRequestQueue();
            if (mImageLoader == null) {
                mImageLoader = new ImageLoader(this.mRequestQueue,
                        new LruBitmapCache());
            }
            return this.mImageLoader;
        }
    */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        // req.setTag(OrderConstants.isEmptyString(tag) ? TAG : tag);
        System.setProperty("http.keepAlive", "false");
        req.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(30),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
