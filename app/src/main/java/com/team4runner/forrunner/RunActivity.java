package com.team4runner.forrunner;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.team4runner.forrunner.adapter.ListViewDiaTreinoAdapter;
import com.team4runner.forrunner.adapter.ListViewListaTestesCorredorAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.service.RunIntentService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RunActivity extends ActionBarActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback {


    //Lista de atividades
    DrawerLayout Drawer;
    private NavigationView navigationView;
    private ListView listaAtividades;
    private ArrayList atividadesTreino;
    private ArrayList atividadesTeste = new ArrayList();
    private Corredor corredor;
    private boolean isLivre;

    //Service
    RunIntentService mService;
    boolean mBound = false;

    Intent intentCreateService;

    boolean isOrigemNotification;


    //Permission
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private boolean locationPermission = false;

    //Tipo corrida
    private String tipoCorrida;
    String auxRitmo;
    String auxDistancia;
    String auxVelocidade;
    String auxCalorias;


    //Resultados finais
    private ArrayList<LatLng> pontos;
    private ArrayList<Float> speed;
    private ArrayList<Date> dataHora;
    private ArrayList<Float> distancia;
    private ArrayList<Float> tempo;

    private Date dtHoraInicio;
    private Date dtHoraFim;
    private float velocidadeMedia;
    private float velocidadeMaxima;
    private float ritmoMedio;


    protected static final String TAG = "Localiza��o";
    protected GoogleMap map;
    private SupportMapFragment mapFragment;
    protected Location mPastLocation;
    protected Location mLastLocation;
    private LatLng latLng;

    private Polyline polyline;

    private TextView mDistancia;
    private float mDistanciaTotal = 0;
    private TextView mRitmo;
    private TextView mVelocidade;
    private TextView mCalorias;
    private double mCaloriaTotal = 0;
    private Double peso;
    private String emailCorredor;


    // define se a corrida esta ligada ou desligada
    protected Boolean mRequestingLocationUpdates;
    protected LocationRequest mLocationRequest;


    // UI Widgets.
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;


    //Toolbar
    Toolbar toolbar;


    //CRONOMETRO
    private Chronometer mchronometer;
    private long milliseconds;
    private long millisecondsStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);


        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        peso = Double.valueOf(prefs.getString("peso", "0"));
        emailCorredor = prefs.getString("email", "");
        Log.i("Peso", "" + peso);

        Intent intent = getIntent();
        tipoCorrida = intent.getStringExtra("tipo");
        isOrigemNotification = intent.getBooleanExtra("isOrigemNotification", false);
        if (isOrigemNotification) {
            mRequestingLocationUpdates = true;
            dtHoraInicio = (Date) intent.getSerializableExtra("dtHoraInicio");
            Log.e("recebeData", "intentNotif");
        } else {
            mRequestingLocationUpdates = false;
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.correr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Lista de atividades
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.inflateHeaderView(R.layout.header_drawer_run);
        listaAtividades = (ListView) findViewById(R.id.list_view);
        if (tipoCorrida.equals("livre")) {
            navigationView.setVisibility(View.GONE);
            Drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            isLivre = true;
        } else if (tipoCorrida.equals("teste")) {
            Bundle bundleTeste = intent.getBundleExtra("bundleTeste");
            atividadesTeste = (ArrayList) bundleTeste.getSerializable("atividadesTeste");
            corredor = (Corredor) bundleTeste.getSerializable("corredor");

            ListViewListaTestesCorredorAdapter adapter = new ListViewListaTestesCorredorAdapter(this, atividadesTeste, corredor, true);
            listaAtividades.setAdapter(adapter);

        } else if (tipoCorrida.equals("treino")) {
            Bundle bundleTreino = intent.getBundleExtra("bundleTreino");
            atividadesTreino = (ArrayList) bundleTreino.getSerializable("atividadesTreino");

            ListViewDiaTreinoAdapter adapter = new ListViewDiaTreinoAdapter(this, atividadesTreino);
            listaAtividades.setAdapter(adapter);
        }

        //CRONOMETRO
        mchronometer = (Chronometer) findViewById(R.id.chronometer);
        //mchronometer.setText(DateFormat.format("h:mm:ss",0));

        mchronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long aux = SystemClock.elapsedRealtime() - chronometer.getBase();
                //  chronometer.setText(DateFormat.format("h:mm:ss", aux));
            }
        });
        milliseconds = 0;
        millisecondsStop = 0;

        //

        mDistancia = (TextView) findViewById(R.id.txtDistancia);
        mRitmo = (TextView) findViewById(R.id.txtRitmo);
        mVelocidade = (TextView) findViewById(R.id.txtVelocidade);
        mCalorias = (TextView) findViewById(R.id.txtCalorias);


        //Botoes
        mStartUpdatesButton = (Button) findViewById(R.id.btnIniciar);
        mStartUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdatesButtonHandler(v);
            }
        });
        mStopUpdatesButton = (Button) findViewById(R.id.btnParar);
        mStopUpdatesButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        mStopUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpdatesButtonHandler(v);
            }
        });

        pontos = new ArrayList<LatLng>();
        speed = new ArrayList<Float>();
        dataHora = new ArrayList<Date>();
        distancia = new ArrayList<Float>();
        tempo = new ArrayList<Float>();

        setButtonsEnabledState();

        checkPermission();

        if (!isOrigemNotification) {
            //Inicia o Service
            intentCreateService = new Intent(this, RunIntentService.class);
            startService(intentCreateService);
        }

    }


    protected void onResume() {
        super.onResume();


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (locationPermission) {

                // if (mRequestingLocationUpdates) {
                //   mService.startLocationUpdates();
                // }

            }
        } else {
            // Solicita ao usuario para ligar o GPS
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.gpsDesligado))
                    .setCancelable(false).setPositiveButton(
                    getString(R.string.sim),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Intent para entrar nas configuracoes de localizacao
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        }
                    });
            alertDialogBuilder.setNegativeButton(R.string.nao,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }


    }

    protected void configureMap() {
        if (map == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);

            // Recupera o objeto GoogleMap
            mapFragment.getMapAsync(this);


        }
    }


    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();

            mService.startLocationUpdates();
            //Iniciar a location do service

            //cronometro
            millisecondsStop = millisecondsStop > 0 ? System.currentTimeMillis() - millisecondsStop : 0;
            mchronometer.setBase(SystemClock.elapsedRealtime() - (milliseconds + millisecondsStop));
            mchronometer.start();
            millisecondsStop = 0;

            dtHoraInicio = new Date();

        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();

            mService.stopLocationUpdates();
            //parar a location do service

            //cronometro
            millisecondsStop = System.currentTimeMillis();
            milliseconds = SystemClock.elapsedRealtime() - mchronometer.getBase();
            mchronometer.stop();

            dtHoraFim = new Date();


            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle(R.string.salvarcorrida)
                    .setMessage(R.string.desejasalvarcorrida)
                    .setCancelable(false)
                    .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if ((pontos.size() > 5)) {


                                //Transformao Array list em uma lista float simples pra coloca-la no bundle
                                float[] speedPontos = new float[speed.size()];
                                int count = 0;
                                for (float aux : speed) {
                                    speedPontos[count] = aux;
                                    count++;
                                }

                                //Transformao Array list em uma lista float simples pra coloca-la no bundle
                                float[] distanciaPontos = new float[distancia.size()];
                                int counta = 0;
                                for (float aux : distancia) {
                                    distanciaPontos[counta] = aux;
                                    counta++;
                                }

                                //Transformao Array list em uma lista float simples pra coloca-la no bundle
                                float[] tempoPontos = new float[tempo.size()];
                                int countb = 0;
                                for (float aux : tempo) {
                                    tempoPontos[countb] = aux;
                                    countb++;
                                }

                                //Pega a velocidade media e ritmo medio
                                float velSoma = 0;
                                for (float aux : speed) {
                                    velSoma = velSoma + aux;
                                }
                                velocidadeMedia = velSoma / speed.size();

                                ritmoMedio = ((1000 / velocidadeMedia) / 60);
                                Log.i("RitMedio1", "" + ritmoMedio);
                                //obter minutos
                                int auxMinutosRitMedio = (int) ((1000 / velocidadeMedia) / 60);
                                //passar segundos para base 6
                                int auxSegundosRitMedio = (int) ((ritmoMedio - auxMinutosRitMedio) * 100);
                                int finalSegundos = (int) (auxSegundosRitMedio * 60) / 100;
                                //junta mitutos e segundos
                                DecimalFormat decimalFormat = new DecimalFormat("00");
                                String auxRitFinalString = decimalFormat.format(auxMinutosRitMedio) + "." + decimalFormat.format(finalSegundos);
                                ritmoMedio = Float.parseFloat(auxRitFinalString);
                                Log.i("RitMedio2", "" + ritmoMedio);
                                //

                                Bundle bundleCorrida = new Bundle();
                                bundleCorrida.putParcelableArrayList("Pontos", pontos);
                                bundleCorrida.putFloatArray("Speed", speedPontos);
                                bundleCorrida.putFloatArray("distancia", distanciaPontos);
                                bundleCorrida.putFloatArray("tempo", tempoPontos);
                                bundleCorrida.putFloat("distanciaTotal", mDistanciaTotal);
                                bundleCorrida.putSerializable("dataHora", dataHora);
                                bundleCorrida.putSerializable("dtHoraInicio", dtHoraInicio);
                                bundleCorrida.putSerializable("dtHoraFim", dtHoraFim);
                                bundleCorrida.putFloat("velocidadeMaxima", velocidadeMaxima);
                                bundleCorrida.putFloat("velocidadeMedia", velocidadeMedia);
                                bundleCorrida.putFloat("ritmoMedio", ritmoMedio);
                                bundleCorrida.putDouble("Calorias", mCaloriaTotal);
                                bundleCorrida.putString("emailCorredor", emailCorredor);
                                bundleCorrida.putString("tipo", tipoCorrida);

                                Log.i("Next", "Activity");
                                mService.stopSelf();
                                Intent intent = new Intent(RunActivity.this, AfterRunActivity.class);
                                intent.putExtras(bundleCorrida);
                                startActivity(intent);

                            } else { //qdt pontos insuficiente

                                new AlertDialog.Builder(RunActivity.this)
                                        .setTitle(R.string.ops)
                                        .setMessage(R.string.pontosInsufucientes)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                (RunActivity.this).finish();
                                            }
                                        }).setCancelable(false)
                                        .show();

                            }

                        }
                    }).setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    (RunActivity.this).finish();
                }
            }).show();


        }
    }


    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStartUpdatesButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            mStopUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setBackgroundColor(getResources().getColor(R.color.accent));


        } else {
            mStartUpdatesButton.setEnabled(true);
            mStartUpdatesButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            mStopUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void desenha() {

        if (pontos != null && pontos.size() > 0) {
            PolylineOptions PolOptions;
            PolOptions = new PolylineOptions();
            PolOptions.color(Color.RED);
            PolOptions.width(7);
            PolOptions.addAll(pontos);
            map.addPolyline(PolOptions);
            Log.i("DESENHA", pontos.toString());

        }
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            CameraPosition position = new CameraPosition.Builder()
                    .target(latLng)    // Localiza��o
                    .bearing(0)        // Dire��o em que a c�mera est� apontando em graus
                    .tilt(45)            // �ngulo que a c�mera est� posicionada em graus (0 a 90)
                    .zoom(17)            // Zoom
                    .build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            map.animateCamera(update);

        }

    }


    private void updateUI() {


        desenha();

        /*Pega a velocidade Max
        for (float aux : speed) {
            if (mLastLocation.getSpeed() > aux) {
                velocidadeMaxima = mLastLocation.getSpeed();
            } else {
                velocidadeMaxima = aux;
            }
        }*/


        mRitmo.setText(String.valueOf(auxRitmo));
        mDistancia.setText(String.valueOf(auxDistancia));
        mVelocidade.setText(String.valueOf(auxVelocidade));
        mCalorias.setText(String.valueOf(auxCalorias));

    }


    @Override
    protected void onStart() {
        super.onStart();


        //Connectar ao Service
        Intent intent = new Intent(this, RunIntentService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //Ligar reeeiver
        this.registerReceiver(mMessageReceiver, new IntentFilter("location-4runner"));
        this.registerReceiver(mMessageReceiver, new IntentFilter("firstLocation-4runner"));

        //Dismiss notification
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel("run", 3);

    }

    @Override
    protected void onStop() {
        super.onStop();

        //Desconectar do Sevice
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
            //Desligar Receiver
            this.unregisterReceiver(mMessageReceiver);
        }

        if (mRequestingLocationUpdates) {


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_walk_white_36dp)
                            .setColor(getResources().getColor(R.color.primary))
                            .setOngoing(true)
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.corridaEmAndamento));

            Intent resultIntent = new Intent(this, RunActivity.class);
            resultIntent.putExtra("tipo", tipoCorrida);
            if(tipoCorrida.equals("treino")){
                Bundle bundleTreino = new Bundle();
                bundleTreino.putSerializable("atividadesTreino",atividadesTreino);
                resultIntent.putExtra("bundleTreino", bundleTreino);
            }
            if(tipoCorrida.equals("teste")){
                Bundle bundleTeste = new Bundle();
                bundleTeste.putSerializable("atividadesTeste",atividadesTeste);
                bundleTeste.putSerializable("corredor", corredor);
                resultIntent.putExtra("bundleTeste",bundleTeste);
            }
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.putExtra("dtHoraInicio", dtHoraInicio);
            resultIntent.putExtra("isOrigemNotification", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainCorredorActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify("run", 3, mBuilder.build());
        }
    }


    @Override
    public void onCameraChange(CameraPosition position) {
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationPermission = true;

                    Intent intent = getIntent();
                    startActivity(intent);
                    finish();

                    // mGoogleApiClient.connect();


                    //// GPS estah ligado
                    //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);


                } else {

                    locationPermission = false;

                    new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.permissaoNegada))
                            .setMessage(getString(R.string.permissaoNegadaMsg))
                            .setPositiveButton(getString(R.string.permitir), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPermission();
                                }
                            })
                            .setNegativeButton(getString(R.string.negar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkPermission() {

        // Here, thisActivity is the current activity

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            //      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
            //            android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.


            //  } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            //}

        } else {
            locationPermission = true;
        }

    }


    //Service
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            RunIntentService.RunBinder binder = (RunIntentService.RunBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.e(TAG, "onServiceConnected");

            if (isOrigemNotification) {
                Chronometer auxChronometer = mService.getChronometer();
                long auxlong = auxChronometer.getBase();
                mchronometer.setBase(auxlong);
                mchronometer.start();
            }

            mLastLocation = mService.getLastLocation();
            configureMap();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  stopService(intentCreateService);
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("first", false)) {

                mLastLocation = intent.getParcelableExtra("mLastLocation");
                if (mLastLocation != null) {
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                    CameraPosition position = new CameraPosition.Builder()
                            .target(latLng)    // Localizacao
                            .bearing(0)        // Direcao em que a camera esta apontando em graus
                            .tilt(0)            // angulo que a camera esta posicionada em graus (0 a 90)
                            .zoom(17)            // Zoom
                            .build();
                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                    map.animateCamera(update);

                }

            } else {
                Log.i("reeebndo", "broadcast");

                Bundle bundleDesenha = intent.getBundleExtra("bundleDesenha");
                pontos = (ArrayList<LatLng>) bundleDesenha.getSerializable("pontos");
                speed = (ArrayList<Float>) bundleDesenha.getSerializable("speed");
                dataHora = (ArrayList<Date>) bundleDesenha.getSerializable("dataHora");

                Bundle bundleDados = intent.getBundleExtra("bundleDados");
                velocidadeMaxima = bundleDados.getFloat("velocidadeMaxima");
                auxRitmo = bundleDados.getString("txtRitmo");
                auxDistancia = bundleDados.getString("txtDistancia");
                auxVelocidade = bundleDados.getString("txtVelocidade");
                auxCalorias = bundleDados.getString("txtCalorias");
                mLastLocation = bundleDados.getParcelable("mLastLocation");
                mPastLocation = bundleDados.getParcelable("mPastLocation");


                Bundle bundleDados2 = intent.getBundleExtra("bundleDados2");
                mCaloriaTotal = bundleDados2.getDouble("mCaloriaTotal");
                tempo = (ArrayList<Float>) bundleDados2.getSerializable("tempo");
                mDistanciaTotal = bundleDados2.getFloat("mDistanciaTotal");
                distancia = (ArrayList<Float>) bundleDados2.getSerializable("distancia");

                updateUI();
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBoolean("requestionsUpdate", mRequestingLocationUpdates);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mRequestingLocationUpdates = savedInstanceState.getBoolean("requestionsUpdate");
        setButtonsEnabledState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_run, menu);

        if(isLivre){
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.atividades) {
            Drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Configura o tipo do mapa
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (mLastLocation != null) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                CameraPosition position = new CameraPosition.Builder()
                        .target(latLng)    // Localiza��o
                        .bearing(0)        // Dire��o em que a c�mera est� apontando em graus
                        .tilt(0)            // �ngulo que a c�mera est� posicionada em graus (0 a 90)
                        .zoom(17)            // Zoom
                        .build();
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                map.animateCamera(update);

            } else {
                Log.i(TAG, "LOCALIZA��O N�O ENCONTRADA!");
            }


            // Eventos
            map.setOnMapClickListener(this);

            // Localiza��o
            map.setMyLocationEnabled(true);
            mLastLocation = map.getMyLocation();


            // Centraliza o mapa com animacao de 10 segundos
            // CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            // map.animateCamera(update);


        }

    }
}
