package estimeet.meetup.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.TimeUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import estimeet.meetup.di.Modules.BackgroundServiceModule;
import estimeet.meetup.di.components.BackgroundServiceComponent;
import estimeet.meetup.di.components.DaggerBackgroundServiceComponent;
import estimeet.meetup.interactor.SendGeoDataInteractor;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 9/04/16.
 */
public class MeetupLocationService implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener {
//      // TODO: 10/04/16  live interval
//    private static final int FASTEST_INTERVAL = 20000;
//    private static final int UPDATE_INTERVAL = 40000;
    //// TODO: 10/04/16 debug interval
    private static final int FASTEST_INTERVAL = 5000;
    private static final int UPDATE_INTERVAL = 10000;

    private static GoogleApiClient googleApiClient;
    private static LocationRequest locationRequest;
    private static MeetupLocationService instance;
    private static Context context;

    private static boolean needsContinuousTracking;

    private SendGeoDataInteractor geoDataInteractor;

    private BackgroundServiceComponent backgroundServiceComponent;

    @Inject ServiceHelper serviceHelper;

    public static MeetupLocationService getInstance(Context ctx) {
        if (instance == null) {
            context = ctx;
            instance = new MeetupLocationService();
        }
        return instance;
    }

    public void setGeoDataInteractor(SendGeoDataInteractor interactor) {
        geoDataInteractor = interactor;
    }

    public void startLocationService(long expiresTime) {
        buildGoogleApiClient();

        if (expiresTime > 0) {
            buildLocationRequest(expiresTime);
            needsContinuousTracking = true;
        } else {
            needsContinuousTracking = false;
        }

        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        } else {
            if (needsContinuousTracking) {
                setupContinuousTracking();
            } else {
                getLastKnownLocationCoord();
            }
        }
    }

    public void disconnectLocation() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
            locationRequest = null;
        }
    }

    private synchronized void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

    private void buildLocationRequest(long expiresTime) {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            //40 seconds
            locationRequest.setInterval(UPDATE_INTERVAL);
            //20 seconds
            locationRequest.setFastestInterval(FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            locationRequest.setExpirationDuration(expiresTime);
        }
        //allow 10 seconds delay
        if (locationRequest.getExpirationTime() - SystemClock.elapsedRealtime() <
                expiresTime - TimeUnit.SECONDS.toMillis(10)) {
            locationRequest.setExpirationDuration(expiresTime);
        }
    }

    private void getLastKnownLocationCoord() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_DENIED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                geoDataInteractor.sendGeoData(location.getLatitude() + "," + location.getLongitude());
            }
        }
    }

    private void setupContinuousTracking() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_DENIED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (needsContinuousTracking) {
            setupContinuousTracking();
        } else {
            getLastKnownLocationCoord();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {
        if (backgroundServiceComponent == null) {
            backgroundServiceComponent = DaggerBackgroundServiceComponent.builder()
                    .backgroundServiceModule(new BackgroundServiceModule(context))
                    .build();

            backgroundServiceComponent.inject(context);
        }

        ServiceHelper serviceHelper = backgroundServiceComponent.serviceHelper();
        MeetUpSharedPreference sp = backgroundServiceComponent.meetUpSharedPreference();
        SendGeoDataInteractor interactor = new SendGeoDataInteractor(serviceHelper, null, sp);
        interactor.sendGeoData(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}
