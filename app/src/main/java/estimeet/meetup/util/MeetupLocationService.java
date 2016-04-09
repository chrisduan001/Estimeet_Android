package estimeet.meetup.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by AmyDuan on 9/04/16.
 */
public class MeetupLocationService implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private static final int FASTEST_INTERVAL = 20000;
    private static final int UPDATE_INTERVAL = 40000;

    private static GoogleApiClient googleApiClient;
    private static LocationRequest locationRequest;
    private static MeetupLocationService instance;
    private static Context context;

    private static boolean needsContinuousTracking;

    private LocationServiceListener listener;

    public static MeetupLocationService getInstance(Context ctx) {
        if (instance == null) {
            context = ctx;
            instance = new MeetupLocationService();
        }
        return instance;
    }

    public void setServiceListener(LocationServiceListener listener) {
        this.listener = listener;
    }

    public void getLastKnownLocation() {
        needsContinuousTracking = false;
        buildGoogleApiClient();
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        } else {
            getLastKnownLocationCoord();
        }
    }

    public void startContinousTracking() {
        needsContinuousTracking = true;
        buildLocationRequest();
    }

    private synchronized void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

    private void buildLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            //40 seconds
            locationRequest.setInterval(UPDATE_INTERVAL);
            //20 seconds
            locationRequest.setFastestInterval(FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        locationRequest.setExpirationDuration(1000000);
    }

    public void disconnectLocation() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private void getLastKnownLocationCoord() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_DENIED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                listener.onLocationDataReceived(location.getLatitude() + "," + location.getLongitude());
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_DENIED) {
            if (needsContinuousTracking) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                getLastKnownLocationCoord();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {
        listener.onLocationDataReceived(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public interface LocationServiceListener {
        void onLocationDataReceived(String geoData);
    }
}
