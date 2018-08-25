package com.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerCommunication {
    private DefaultHttpClient mHttpClient;
    private int responseCode;

    Context context;

    public ServerCommunication(Context context) {

        this.context = context;
    }

    public String uploadUserPhoto(String serviceName, String authToken, File imageFile) {
        //String responseData = null;


        String BASE_URL_HTTPS = context.getResources().getString(R.string.BASE_URL_HTTPS);
        String APPKEY = context.getResources().getString(R.string.APPKEY);


        String sResponse = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(BASE_URL_HTTPS + serviceName);
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            FormBodyPart appkey = new FormBodyPart("appkey", new StringBody(
                    APPKEY));
            FormBodyPart token = new FormBodyPart("auth_token", new StringBody(
                    authToken));


            entity.addPart(appkey);
            entity.addPart(token);


            entity.addPart("profilepic", new ByteArrayBody(RotiHomeActivity.getBytesSecond(), "image/jpeg",
                    imageFile.getName()));

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            sResponse = AppConstants.inputStreamToString(
                    response.getEntity().getContent()).toString();
            Log.i("ResponseString", sResponse);
            responseCode = response.getStatusLine().getStatusCode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sResponse;
    }

    private Bitmap bitmapDecode(String path) {
        try {
            InputStream in = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                in = new FileInputStream(path);
            } finally {
                in.close();
            }
            options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            // calc rought re-size (this is no exact resize)

            int imageSize = (int) (in.available() / 1024);
            int memorySize = (int) (Runtime.getRuntime().freeMemory() / 1024);
            Log.v("TAG******", "Image size is " + imageSize
                    + " Memory Size is " + memorySize);

            if (imageSize > 500 && memorySize > 1000) {
                options.inSampleSize = 2;
                Bitmap roughBitmap = BitmapFactory.decodeStream(in, null,
                        options);
                // Bitmap roughBitmap = BitmapFactory.decodeStream(in);

                // calc exact destination size
                Matrix m = new Matrix();
                float[] values = new float[9];
                m.getValues(values);
                Matrix mat = new Matrix();
                mat.preRotate(90);// /in degree
                // resize bitmap
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
                        (int) (roughBitmap.getWidth() * values[0]),
                        (int) (roughBitmap.getHeight() * values[4]), true);

                Bitmap rotateImage = Bitmap.createBitmap(resizedBitmap, 0, 0,
                        resizedBitmap.getWidth(), resizedBitmap.getHeight(),
                        mat, true);

                // save image
                try {
                    FileOutputStream out = new FileOutputStream(path);
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    roughBitmap.recycle();
                    roughBitmap = null;
                    resizedBitmap.recycle();
                    resizedBitmap = null;
                    return rotateImage;
                } catch (Exception e) {
                    Log.e("Image", e.getMessage(), e);
                    return rotateImage;
                }
            } else {
            }

            in.close();

        } catch (IOException e) {
            Log.e("Image", e.getMessage(), e);
        }
        return null;
    }

    private Bitmap bitmapDecodeHor(String path) {
        try {
            InputStream in = new FileInputStream(path);
            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // decode full image pre-resized
            in = new FileInputStream(path);
            options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            // calc rought re-size (this is no exact resize)

            int imageSize = (int) (in.available() / 1024);
            int memorySize = (int) (Runtime.getRuntime().freeMemory() / 1024);
            Log.v("TAG******", "Image size is " + imageSize
                    + " Memory Size is " + memorySize);

            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);
            // calc exact destination size
            Matrix m = new Matrix();
            float[] values = new float[9];
            m.getValues(values);
            Matrix mat = new Matrix();
            mat.preRotate(90);// /in degree
            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
                    (int) (roughBitmap.getWidth() * values[0]),
                    (int) (roughBitmap.getHeight() * values[4]), true);

            Bitmap rotateImage = Bitmap.createBitmap(resizedBitmap, 0, 0,
                    resizedBitmap.getWidth(), resizedBitmap.getHeight(), mat,
                    true);

            // save image
            try {
                FileOutputStream out = new FileOutputStream(path);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                roughBitmap.recycle();
                roughBitmap = null;
                resizedBitmap.recycle();
                resizedBitmap = null;
                return rotateImage;
            } catch (Exception e) {
                Log.e("Image", e.getMessage(), e);
                return rotateImage;
            }
        } catch (IOException e) {
            Log.e("Image", e.getMessage(), e);
        }
        return null;
    }

    private class PhotoUploadResponseHandler implements ResponseHandler<Object> {

        @Override
        public Object handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {

            HttpEntity r_entity = response.getEntity();
            String responseString = EntityUtils.toString(r_entity);
            Log.d("UPLOAD", responseString);

            return null;
        }

    }

}
