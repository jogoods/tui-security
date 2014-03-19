package com.tvh.security.utils;

import android.content.Context;
import android.location.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Get Latitue, Longtitue to show in Google Maps
 */
public class LocationsUtils {

    /**
     *
     */
    public static final double[] DEFAULT_LOCATION = {0.0, 0.0};
    private static final String TAG = LocationsUtils.class.getName();
    private boolean gpsEnable = false;
    private boolean networkEnable = false;
    private boolean simAvailable = false;
    private Context m_context;

    public LocationsUtils(Context context) {
        m_context = context;
    }

    public double[] getLocation() {

        /**
         * LocationManager handle when GPS enable
         */
        LocationManager locManager = (LocationManager) m_context
                .getSystemService(m_context.LOCATION_SERVICE);

        /**
         * ConnectivityManager hadle when GPS disable and network is enable.
         * Network maybe are: Wifi, UTMS, GPRS
         */
        ConnectivityManager connecManager = (ConnectivityManager) m_context
                .getSystemService(m_context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfor = connecManager.getActiveNetworkInfo();

        /**
         * TelephonyManager handle when both GPS and Network were disable and
         * SIM card is enable
         */
        TelephonyManager telephonyManager = (TelephonyManager) m_context
                .getSystemService(m_context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();

        /**
         * Check conditions
         */
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsEnable = true;
        } else if (networkInfor != null && networkInfor.isConnected()) {
            networkEnable = true;
        } else if (simState == TelephonyManager.SIM_STATE_READY) {
            simAvailable = true;
        }

        /**
         * GetLocation belong once state: has GPS, has Network, or SIM cell
         */
        if (gpsEnable || networkEnable) {
            return indentifyLocation(m_context);
        }
        return DEFAULT_LOCATION;

    }

    /**
     * Get location follow: GPS first, Network second, and SIM cell third. These
     * priority will decrease belong exactly of provider.
     */
    public double[] indentifyLocation(Context context) {
        double[] loc = DEFAULT_LOCATION;
        if (this.gpsEnable) {
            /**
             * I can't handle for first run. Result is 0. I want use Thread, but
             * i don't know where will use. Please help me.
             */
            loc = gpsLocation(context);
            if (loc == null || loc[0] == 0 || loc[1] == 0) {
                loc = gpsLocation(context);
            }
        } else if (this.networkEnable) {
            // Get network location
            loc = networkLocation(context);
            if (loc == null || loc[0] == 0 || loc[1] == 0) {
                loc = networkLocation(context);
            }
        } else if (this.simAvailable) {
            // Get cellLocation

        }
        return loc;
    }

    public double[] gpsLocation(Context context) {
        System.out.println("Get location follow GPS");
        // Get location follow GPS location.
        LocationManager mLocationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mListener = new MyLocationListener();
        double longitude = 0;
        double latitude = 0;

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, mListener);

            Location lastLocation = mLocationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null && lastLocation.getLongitude() != 0
                    && lastLocation.getLatitude() != 0
                    && !mListener.isDisable()) {
                longitude = lastLocation.getLongitude();
                latitude = lastLocation.getLatitude();
            } else {
                longitude = mListener.getLongitude();
                latitude = mListener.getLatitude();
            }
            if (longitude != 0 && latitude != 0)
                mLocationManager.removeUpdates(mListener);

            double[] result = {longitude, latitude};
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Exeption in get Address" + e.getMessage());
            return DEFAULT_LOCATION;
        }
    }

    public double[] networkLocation(Context context) {
        System.out.println("Get location follow Network");
        // Get location follow GPS location.
        LocationManager mLocationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mListener = new MyLocationListener();
        double longitude = 0;
        double latitude = 0;

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, mListener);

            Location lastLocation = mLocationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (lastLocation != null && lastLocation.getLongitude() != 0
                    && lastLocation.getLatitude() != 0
                    && !mListener.isDisable()) {
                longitude = lastLocation.getLongitude();
                latitude = lastLocation.getLatitude();
            } else {
                longitude = mListener.getLongitude();
                latitude = mListener.getLatitude();
            }
            if (longitude != 0 && latitude != 0)
                mLocationManager.removeUpdates(mListener);

            double[] result = {longitude, latitude};
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Exeption in get Address" + e.getMessage());
            return DEFAULT_LOCATION;
        }
    }

    public Address getAddress(double latitue, double longtitue) {
        Geocoder gCoder = new Geocoder(m_context, Locale.getDefault());
        ArrayList<Address> addresses;
        Address address = null;
        try {
            if (Geocoder.isPresent()) {
                addresses = (ArrayList<Address>) gCoder.getFromLocation(
                        latitue, longtitue, 1);
                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Exeption in get Address" + e.getMessage());
        }
        return address;
    }

	/*
     * public static String[] getAddressFromLocation(double lat, double lon, int
	 * maxResults) { StrictMode.ThreadPolicy policy = new
	 * StrictMode.ThreadPolicy.Builder().permitAll().build();
	 * 
	 * StrictMode.setThreadPolicy(policy); String urlStr =
	 * "http://maps.google.com/maps/geo?q=" + lat + "," + lon +
	 * "&output=json&sensor=false"; String response = ""; String[] results = new
	 * String[maxResults]; String addressLine; HttpClient client = new
	 * DefaultHttpClient();
	 * 
	 * try { HttpResponse hr = client.execute(new HttpGet(urlStr)); HttpEntity
	 * entity = hr.getEntity();
	 * 
	 * BufferedReader br = new BufferedReader(new
	 * InputStreamReader(entity.getContent()));
	 * 
	 * String buff = null; while ((buff = br.readLine()) != null) response +=
	 * buff; } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * JSONArray responseArray = null; try { JSONObject jsonObject = new
	 * JSONObject(response); responseArray =
	 * jsonObject.getJSONArray("Placemark"); } catch (JSONException e) {
	 * Log.e(TAG, "Get Address Placemark failed"); return null; }
	 * 
	 * for (int i = 0; i < responseArray.length() && i < maxResults; i++) {
	 * 
	 * try { JSONObject jsl = responseArray.getJSONObject(i); addressLine =
	 * jsl.getString("address"); results[i] = addressLine; } catch
	 * (JSONException e) { e.printStackTrace(); results[i] = ""; continue; } }
	 * return results; }
	 */

    public static String getAddressWithGeoCoder(Context context,
                                                double latitude, double longitude, int maxResults) {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(latitude, longitude,
                    maxResults);
            if (addresses != null && addresses.size() > 0) {
                for (Address address : addresses) {
                    String temp = "";
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        temp += address.getAddressLine(i) + " ";
                    }
                    return temp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

class MyLocationListener implements LocationListener {
    private static double longitude = 0;
    private static double latitude = 0;
    private static boolean isDisable = false;

    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        isDisable = false;
    }

    public void onProviderDisabled(String provider) {
        isDisable = true;
    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isDisable() {
        return isDisable;
    }
}
