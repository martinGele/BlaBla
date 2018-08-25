package com.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ak.app.wetzel.activity.R;

@SuppressLint({ "UseValueOf", "UseValueOf" })
public class RoundProgress extends RelativeLayout {
    private ImageView progressDrawableImageView;
    private ImageView trackDrawableImageView;
    private double max = 100;
    
    public int getMax() {
    	Double d = new Double(max);
    	return d.intValue();
    }
    
    public double getMaxDouble() {
    	return max;
    }

    public void setMax(int max) {
    	Integer maxInt = new Integer(max);
    	maxInt.doubleValue();
        this.max = max;
    }
    
    public void setMax(double max) {
    	this.max = max;
    }

	public RoundProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.round_progress, this);
        setup(context, attrs);
	}

    protected void setup(Context context, AttributeSet attrs)
    {

        final String xmlns="http://schemas.android.com/apk/res/com.ak.app.wetzel.activity";
        int bgResource = attrs.getAttributeResourceValue(xmlns,
                "progressDrawable", 0);
        progressDrawableImageView = (ImageView) findViewById(
                R.id.progress_drawable_image_view);
        progressDrawableImageView.setBackgroundResource(bgResource);

        int trackResource = attrs.getAttributeResourceValue(xmlns, "track", 0);
        trackDrawableImageView = (ImageView) findViewById(R.id.track_image_view);
        trackDrawableImageView.setBackgroundResource(trackResource);

        int progress = attrs.getAttributeIntValue(xmlns, "progress", 0);
        setProgress(progress);
        int max = attrs.getAttributeIntValue(xmlns, "max", 100);
        setMax(max);

//        int numTicks = attrs.getAttributeIntValue(xmlns, "numTicks", 0);



        ProgressBarOutline outline = new ProgressBarOutline(context);
        addView(outline);
    }

    public void setProgress(Integer value)
    {
        setProgress((double) value);
    }
    
    public void setProgress(double value) {
    	ClipDrawable drawable = (ClipDrawable)
                progressDrawableImageView.getBackground();
		double percent = (double) value/ (double)max;
		int level = (int)Math.floor(percent*10000);

        drawable.setLevel(level);
    }
}
