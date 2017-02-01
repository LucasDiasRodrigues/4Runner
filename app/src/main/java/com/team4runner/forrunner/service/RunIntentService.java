package com.team4runner.forrunner.service;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.team4runner.forrunner.AfterRunActivity;
import com.team4runner.forrunner.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RunIntentService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    // Binder given to clients
    private final IBinder mBinder = new RunBinder();

    protected static final String TAG = "Localiza��o";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mPastLocation;
    protected Location mLastLocation;
    private LatLng latLng;

    private Date dtHoraInicio;
    private Date dtHoraFim;
    private float velocidadeMedia;
    private float velocidadeMaxima;
    private float ritmoMedio;


    private float mDistanciaTotal = 0;
    private double mCaloriaTotal = 0;
    private Double peso;
    private String emailCorredor;


    //Resultados finais
    private ArrayList<LatLng> pontos;
    private ArrayList<Float> speed;
    private ArrayList<Date> dataHora;
    private ArrayList<Float> distancia;
    private ArrayList<Float> tempo;


    // define se a corrida esta ligada ou desligada
    protected Boolean mRequestingLocationUpdates;
    protected LocationRequest mLocationRequest;


    //cronometro
    private long milliseconds;
    private long millisecondsStop;
    Chronometer mchronometer;

    //Constantes
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";


    @Override
    public void onCreate() {
        super.onCreate();

        mRequestingLocationUpdates = false;
        velocidadeMaxima = 0;

        pontos = new ArrayList<LatLng>();
        speed = new ArrayList<Float>();
        dataHora = new ArrayList<Date>();
        distancia = new ArrayList<Float>();
        tempo = new ArrayList<Float>();

        buildGoogleApiClient();

        mGoogleApiClient.connect();
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        peso = Double.valueOf(prefs.getString("peso", "0"));
        emailCorredor = prefs.getString("email", "");
        Log.i("Peso", "" + peso);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("ServiceCriado", "uuuuuuulll");


        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("ServiceDestruido", "uuuuuuulll");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Context appCtx = this;
        Intent filterRes = new Intent("location-4runner");
        filterRes.putExtra("mLastLocation", mLastLocation);
        filterRes.putExtra("first", true);
        appCtx.sendBroadcast(filterRes);

        if (mLastLocation != null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //configureMap();
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            // updateUI();
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {


        Log.i("precis�o", String.valueOf(location.getAccuracy()));
        Log.i("ritmo", String.valueOf((1000 / location.getSpeed()) / 60));
        Log.i("velocidade", String.valueOf(location.getSpeed()) + "speed");


        if (location.getAccuracy() < 10) {


            if (mPastLocation == null && mRequestingLocationUpdates) {
                mPastLocation = location;
                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                mp.title("Comecei aqui");
                //   map.addMarker(mp);
            } else {
                mPastLocation = mLastLocation;

            }

            mLastLocation = location;


            if (!location.hasSpeed()) {
                mLastLocation.setSpeed(calcularSpeed(mPastLocation, mLastLocation));
                Log.i("velocidade2", String.valueOf(location.getSpeed()) + "speed");
            }

            //Considerando que o gps atualizara 1,5 vez por segundo (/90) -- cancelado
            //Considerando que o gps atualizara 1 vez por segundo (/60)
            mCaloriaTotal = (((((mLastLocation.getSpeed() * 3.6) * peso) * 0.0175) / 60) + mCaloriaTotal);
            Log.i("Caloria", "" + mCaloriaTotal);

            long elapsedMillis = SystemClock.elapsedRealtime() - mchronometer.getBase();
            long minutos = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis);
            long segundos = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis));

            tempo.add(Float.valueOf(String.valueOf(minutos + ((segundos * 100) / 60) * 0.01)));


            if (mPastLocation != null) {
                mDistanciaTotal = mDistanciaTotal + mPastLocation.distanceTo(mLastLocation);
                distancia.add(mDistanciaTotal);
            }

            Bundle bundle = new Bundle();
            bundle.putDouble("mCaloriaTotal", mCaloriaTotal);
            bundle.putSerializable("tempo", tempo);
            bundle.putFloat("mDistanciaTotal", mDistanciaTotal);
            bundle.putSerializable("distancia", distancia);

            updateUI(bundle);

        }


    }

    private void updateUI(Bundle bundle) {
        //ONDE DEVE GRAVAR NO ARRAY
        if (mLastLocation != null) {

            LatLng latLng1;
            Log.i("Msg", "ADICIONA NA LIST");
            latLng1 = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.i("testo1001", latLng1.toString());

            //Monta array da rota
            pontos.add(latLng1);
            speed.add(mLastLocation.getSpeed());
            dataHora.add(new Date());
            Bundle bundleDesenha = new Bundle();
            bundleDesenha.putSerializable("pontos", pontos);
            bundleDesenha.putSerializable("speed", speed);
            bundleDesenha.putSerializable("dataHora", dataHora);


            /*/Pega a velocidade Max
            for (float aux : speed) {
                if (mLastLocation.getSpeed() > aux) {
                    velocidadeMaxima = mLastLocation.getSpeed();
                } else {
                    velocidadeMaxima = aux;
                }
            }*/
            if(mLastLocation.getSpeed() > velocidadeMaxima){
                velocidadeMaxima = mLastLocation.getSpeed();
            }

            DecimalFormat dfRitmo = new DecimalFormat("0.0");
            DecimalFormat dfDistancia = new DecimalFormat("0.00");
            DecimalFormat dfVelocidade = new DecimalFormat("0.0");
            DecimalFormat dfCaloria = new DecimalFormat("0.0");

            String auxRitmo;
            if (mLastLocation.getSpeed() != 0) {
                float auxRitFloat = ((1000 / mLastLocation.getSpeed()) / 60);
                Log.i("auxFloatRit", ""+auxRitFloat);
                //obter minutos
                int auxMinutosRit = (int) ((1000 / mLastLocation.getSpeed()) / 60);
                //passar segundos para base 6
                int auxSegundosRitMedio = (int) ((auxRitFloat - auxMinutosRit) * 100);
                int finalSegundos = (int)(auxSegundosRitMedio * 60) / 100;
                //junta mitutos e segundos
                DecimalFormat decimalFormat = new DecimalFormat("00");
                String auxRitFinalString = decimalFormat.format(auxMinutosRit) + ":" + decimalFormat.format(finalSegundos);
                Log.i("RitMedio2", ""+auxRitFinalString);
                //
                auxRitmo = auxRitFinalString + "Min/Km";

            } else {
                DecimalFormat decimalFormat = new DecimalFormat("00.00");
                String aux = (String.valueOf(decimalFormat.format(0)) + "Min/Km");
                auxRitmo = aux.replace(",", ":");
            }
            String auxDistancia = (String.valueOf(dfDistancia.format(mDistanciaTotal / 1000)) + "Km");
            String auxVelocidade = (String.valueOf(dfVelocidade.format(mLastLocation.getSpeed() * 3.6)) + "Km/h");
            String auxCalorias = (String.valueOf(dfCaloria.format(mCaloriaTotal)));

            Bundle bundleDados = new Bundle();
            bundleDados.putFloat("velocidadeMaxima", velocidadeMaxima);
            bundleDados.putString("txtRitmo", auxRitmo);
            bundleDados.putString("txtDistancia", auxDistancia);
            bundleDados.putString("txtVelocidade", auxVelocidade);
            bundleDados.putString("txtCalorias", auxCalorias);
            bundleDados.putParcelable("mLastLocation", mLastLocation);
            bundleDados.putParcelable("mPastLocation", mPastLocation);


            Context appCtx = this;
            Intent filterRes = new Intent("location-4runner");
            filterRes.putExtra("first", false);
            filterRes.putExtra("bundleDesenha", bundleDesenha);
            filterRes.putExtra("bundleDados", bundleDados);
            filterRes.putExtra("bundleDados2", bundle);
            appCtx.sendBroadcast(filterRes);

        }
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class RunBinder extends Binder {
        public RunIntentService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RunIntentService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void startLocationUpdates() {
        mRequestingLocationUpdates = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        //Cronometro
        milliseconds = 0;
        millisecondsStop = 0;
        mchronometer = new Chronometer(getApplicationContext());
        millisecondsStop = millisecondsStop > 0 ? System.currentTimeMillis() - millisecondsStop : 0;
        mchronometer.setBase(SystemClock.elapsedRealtime() - (milliseconds + millisecondsStop));
        mchronometer.start();

    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;

        millisecondsStop = System.currentTimeMillis();
        milliseconds = SystemClock.elapsedRealtime() - mchronometer.getBase();
        mchronometer.stop();

    }

    private float calcularSpeed(Location last, Location current) {
        float auxSpeed = 0;
        if (last != null) {

            float auxDistance = last.distanceTo(current);

            if (auxDistance >= 1) {
                long auxTime = (current.getTime() - last.getTime()) / 1000;

                if (auxTime > 0) {
                    auxSpeed = (auxDistance / auxTime);
                }

                Log.d("Calculations", "Distance: " + auxDistance + ", TimeDelta: " + auxTime + " seconds" + ",speed: " + auxSpeed + " Accuracy: " + current.getAccuracy());

                //if there is speed from location
                Log.i("calculoSpeedTime", String.valueOf(current.getTime()) + " --- " + String.valueOf(last.getTime()));
                Log.i("calculoSpeed", String.valueOf(auxSpeed));
            }
        }
        return auxSpeed;
    }


    public Location getLastLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mLastLocation;
    }

    public Chronometer getChronometer(){
        return mchronometer;
    }


}
