package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;

import java.io.InputStream;
import java.util.ArrayList;


public class GiftCardAdapter extends PagerAdapter {
    private Activity _activity;
    private int TOTAL_SLIDE;
    private LayoutInflater inflater;
    private ArrayList<String> images;
    private ArrayList<Bitmap> tampImage;

    // constructor
    public GiftCardAdapter(Activity activity, ArrayList<Bitmap> images) {
        _activity = activity;
        this.tampImage = images;
        Log.i("elang", "elang gc: " + images.size());
        TOTAL_SLIDE = images.size();
    }

    @Override
    public int getCount() {
        return TOTAL_SLIDE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_slide_giftcard,
                container, false);

        ImageView imgDisplay = (ImageView) viewLayout
                .findViewById(R.id.imgDisplay);

        for (int i = 0; i < tampImage.size(); i++) {
            if (position == i) {
                imgDisplay.setImageBitmap(tampImage.get(i));

            }
        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
