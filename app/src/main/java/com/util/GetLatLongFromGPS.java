package com.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GetLatLongFromGPS {
    private LocationManager lmGPS;
    private LocationListener locationListener;
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private static Context myCtx;
    public boolean mbUpdatesStopped = true;//PP
//	private LocationManager lmNET;
//	private Location currentLocation = null;
//	private String  cityName = "";


    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    static GetLatLongFromGPS mGetLatLongFromGPS;

    public static GetLatLongFromGPS getinstance(Activity ctx) {
        myCtx = ctx;
        if (mGetLatLongFromGPS == null)
            mGetLatLongFromGPS = new GetLatLongFromGPS(ctx);
        return mGetLatLongFromGPS;
    }

    public GetLatLongFromGPS(Context ctx) {
        myCtx = ctx;
        lmGPS = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void startGPS() {
        Log.d("START GPS RUNNING", "RUNNING");

        if (lmGPS == null && myCtx == null)
            lmGPS = (LocationManager) myCtx.getSystemService(Context.LOCATION_SERVICE);

        if (locationListener == null)
            locationListener = new MyLocationListener();

        lmGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, locationListener);
        Log.e("Request Updates", "GPS UPDATES");

        lmGPS.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, locationListener);
        Log.e("Request Updates", "NETWORK UPDATES");
        Location loc = lmGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateLocation(loc);
        if (loc == null)
            loc = lmGPS.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updateLocation(loc);
        mbUpdatesStopped = false;
    }

    @SuppressLint("LongLogTag")
    public void stopLocationListening() {
        Log.e("inside stopLocationListening", "stopLocationListening");
        if (lmGPS != null) {
            lmGPS.removeUpdates(locationListener);
            mbUpdatesStopped = true;
        }
    }

    private class MyLocationListener implements LocationListener {
        @SuppressLint("LongLogTag")
        public void onLocationChanged(Location loc) {
            Location curLoc = loc;
            Log.e("loc in MyLocationListener", loc.toString());
            if (curLoc.hasAccuracy()) {
                updateLocation(curLoc);
                Log.e("myCtx 1 ", myCtx + "");

            } else {
                loc = null;
            }
        }

        public void onProviderDisabled(String provider) {
            Log.e("ProviderDisable", provider + "   *?*");
        }

        public void onProviderEnabled(String provider) {
            Log.e("ProviderEnable", provider + "   *?*");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("StatusChange",
                    provider + " : " + status + " : " + extras.toString());
        }
    }

    protected boolean isRouteDisplayed() {
        return false;
    }

    public void updateLocation(Location loc) {
        if (loc != null) {
            mLatitude = loc.getLatitude();
            mLongitude = loc.getLongitude();
            Log.e("Lat  &  Lon", mLatitude + "  :  " + mLongitude + "  in updateLocation  ");
        } else {
            Log.e("Sorry", "LOC not found");
            mLatitude = 0.0;
            mLongitude = 0.0;
        }
    }
}


//public GetLatLongFromGPS(Context ctx)
//{
//	myCtx = ctx;
//	lmGPS = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
////	new fetchUpdatedLatLong().execute("");
//}

//private class fetchUpdatedLatLong extends AsyncTask<String, Void, String> {
//	@Override
//	protected void onPreExecute() 
//	{
//
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//		try 
//		{
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
//
//	@Override
//	protected void onProgressUpdate(Void... values) {
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//	}
//}


//public void updateLocation(Location loc)
//{
//	if (loc != null) 
//	{
//		mLatitude = loc.getLatitude();
//		mLongitude = loc.getLongitude();
//		Log.e("Lat  &  Lon", mLatitude + "  :  " + mLongitude + "  in updateLocation  ");
////        int maxResults = 1;
//        
////        Geocoder gc = new Geocoder(myCtx, Locale.getDefault());
////        List<Address> addresses = null;
////        try 
////		{
////		addresses = gc.getFromLocation(mLatitude, mLongitude, maxResults);
////        	//addresses = gc.getFromLocation(40.714623,-74.006605, maxResults);
////			Address addr = addresses.get(0);
////			addr.getAddressLine(0);
////			
////			    myAddress= addr.getAddressLine(0).toString();
////				Log.e("Address Line  GPS", addr.getAddressLine(0).toString());
////				Address addrs = addresses.get(0);
////				mAddress+=addrs;
////				Log.e("Address in GPS", addrs.toString());
////				
////				mStreetName = addrs.getThoroughfare();
////				mCity = addrs.getLocality();
////				mZipCode = addrs.getPostalCode();
////				mState = addrs.getAdminArea();
////				mStreeNumber=addrs.getFeatureName();
////				
////			    
////	        if (addresses.size() == 1) 
////	        {
////	        } else {
////	        }
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//	} 
//	else 
//	{
//		Log.e("Sorry", "LOC not found");
//		mLatitude = 0.0;
//		mLongitude = 0.0;
//	}
//}
//
//private String myAddress = "";
//private String mStreetName = "";
//private String mCity = "";
//private String mZipCode = "";
//private String mState = "";
////private String mAddress = "";
//private String mStreeNumber = "";
//
//public String getStreeNumber() {
//	return mStreeNumber;
//}
//public String getStreetName() {
//	return mStreetName;
//}
//
//public String getCity() {
//	return mCity;
//}
//
//public String getZipCode() {
//	return mZipCode;
//}
//
//public String getState() {
//	return mState;
//}
//public String getmyAddress() {
//	return myAddress;
//}
