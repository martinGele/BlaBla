package com.util;

import com.ak.app.wetzel.activity.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TutorialAdapter extends PagerAdapter {

	private Activity _activity;
	private final static int TOTAL_SLIDE = 5;
	private LayoutInflater inflater;

	// constructor
	public TutorialAdapter(Activity activity) {
		_activity = activity;
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
		View viewLayout = inflater.inflate(R.layout.layout_slide_tutorial,
				container, false);

		ImageView imgDisplay = (ImageView) viewLayout
				.findViewById(R.id.imgDisplay);

		if (position == 0) {
			imgDisplay.setImageResource(R.drawable.tut_01);
		} else if (position == 1) {
			imgDisplay.setImageResource(R.drawable.tut_02);
		} else if (position == 2) {
			imgDisplay.setImageResource(R.drawable.tut_03);
		} else if (position == 3) {
			imgDisplay.setImageResource(R.drawable.tut_04);
		} else if (position == 4) {
			imgDisplay.setImageResource(R.drawable.tut_05);
		}

		((ViewPager) container).addView(viewLayout);

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}

}
