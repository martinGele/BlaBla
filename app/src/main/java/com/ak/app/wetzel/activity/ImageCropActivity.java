package com.ak.app.wetzel.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.util.AppConstants;
import com.util.Fonts;
import com.util.GetFilePathFromDevice;
import com.util.ScaleImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ImageCropActivity extends AppCompatActivity {


    private View viewRect;
    ScaleImageView originalImageView;

    private int targetTop = 0;
    private int targetLeft = 0;

    float heightRatio;
    float widthRatio;


    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_crop_activity);

        Log.d("le6end4", "crop - oncreate");


        BaseActivity.openPhotoBoothPreview();


        RelativeLayout buttonArea = (RelativeLayout) findViewById(R.id.preview_button_area);
        viewRect = (View) findViewById(R.id.selection_part);
        originalImageView = (ScaleImageView) findViewById(R.id.image);
        Button btnSelection = (Button) findViewById(R.id.selectionBtn);

        Fonts.fontFinkHeavyRegularTextView(btnSelection, 18,
                AppConstants.COLORWHITE, this.getAssets());


        Bitmap oriBitmap = null;
        try {
            oriBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), AppConstants.selectedImageUri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String realPath = GetFilePathFromDevice.getPath(this, AppConstants.selectedImageUri);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(realPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);


        Bitmap orientedBitmap = rotateBitmap(oriBitmap, orientation);

        originalImageView.setImageBitmap(orientedBitmap);
        //originalImageView.setImageURI(AppConstants.selectedImageUri);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int maxWidth = size.x;
        final int maxHeight = size.y - buttonArea.getHeight();

        Log.d("wetzel", "maxWidth" + maxWidth);
        Log.d("wetzel", "maxHeight" + maxHeight);

        //************** count ratio

        int intrinsicHeight = orientedBitmap.getHeight();
        int intrinsicWidth = orientedBitmap.getWidth();

        Log.d("wetzel", "intrinsicWidth" + intrinsicWidth);
        Log.d("wetzel", "intrinsicHeight" + intrinsicHeight);

        //Find the ratio of the original image to the scaled image
        //Should normally be equal unless a disproportionate scaling
        //(e.g. fitXY) is used.
        heightRatio = intrinsicHeight / maxHeight;
        widthRatio = intrinsicWidth / maxWidth;

        //************** end count ratio


        viewRect.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //if (currentState != State.EDIT_MOVE) return false;

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewRect.getLayoutParams();
                if (v.getId() != R.id.selection_part) return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        params.topMargin = (int) event.getRawY() - viewRect.getHeight();

                        if (params.topMargin < 0)
                            params.topMargin = 0;
                        else if (params.topMargin + viewRect.getHeight() > maxHeight)
                            params.topMargin = maxHeight - viewRect.getHeight();

                        params.leftMargin = (int) event.getRawX() - (viewRect.getWidth() / 2);

                        if (params.leftMargin < 0)
                            params.leftMargin = 0;
                        else if (params.leftMargin + viewRect.getWidth() > maxWidth)
                            params.leftMargin = maxWidth - viewRect.getWidth();

                        viewRect.setLayoutParams(params);
                        break;

                    case MotionEvent.ACTION_UP:
                        params.topMargin = (int) event.getRawY() - viewRect.getHeight();

                        if (params.topMargin < 0)
                            params.topMargin = 0;
                        else if (params.topMargin + viewRect.getHeight() > maxHeight)
                            params.topMargin = maxHeight - viewRect.getHeight();

                        params.leftMargin = (int) event.getRawX() - (viewRect.getWidth() / 2);

                        if (params.leftMargin < 0)
                            params.leftMargin = 0;
                        else if (params.leftMargin + viewRect.getWidth() > maxWidth)
                            params.leftMargin = maxWidth - viewRect.getWidth();

                        targetTop = params.topMargin;
                        targetLeft = params.leftMargin;

                        viewRect.setLayoutParams(params);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        viewRect.setLayoutParams(params);
                        break;
                }

                return true;
            }
        });

        btnSelection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                originalImageView.buildDrawingCache();
                Bitmap bmapOriginalView = originalImageView.getDrawingCache();

                //Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

                try {


                Bitmap cropBitmap = Bitmap.createBitmap(bmapOriginalView,
                        targetLeft, targetTop, viewRect.getWidth(),
                        viewRect.getHeight());

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(cropBitmap, 350, 350, true);

                Log.d("le6end4", "bytes1=" + RotiHomeActivity.getBytesSecond());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                RotiHomeActivity.setBytesSecond(stream.toByteArray());

                Log.d("le6end4", "bytes2=" + RotiHomeActivity.getBytesSecond());

                BaseActivity.getInstance().setIsFromCamera(true);


                finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseActivity.getInstance().setIsFromCamera(false);

    }
}
