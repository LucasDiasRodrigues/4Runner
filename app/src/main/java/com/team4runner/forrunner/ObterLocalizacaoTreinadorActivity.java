package com.team4runner.forrunner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;



import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.CadastroLocalizacaoTask;



public class ObterLocalizacaoTreinadorActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    //Mapa
    GoogleMap mGoogleMap;
    LatLng mLatLng;

    MarkerOptions marker = new MarkerOptions();

    Boolean apiConnect;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    //Permissao
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;



    private ProgressDialog progress;


    Boolean mapAtualizado = false;

    CheckBox checkBox;
    String locStatus;

    Treinador treinador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obter_localizacao_treinador);

        //Conexao com a API de Localizacao do PlayServices
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Configuracao do objeto de monitoramento de localizacao
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 segundo
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        //Receber Dados da activity anterior
        Intent it = getIntent();
        treinador = (Treinador)it.getSerializableExtra("treinador");
        if (treinador == null){//caso essa tela seja acessada das configs
            treinador = new Treinador();
            SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
            treinador.setEmail(prefs.getString("email",""));
            Log.i("LocConf ===",treinador.getEmail());
        }

        checkPermission();

        //Alerta
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.informalocalizacao))
                .setIcon(R.drawable.ic_map_marker_grey600_36dp)
                .setMessage(R.string.descricaolocalizacao)
                .setPositiveButton(R.string.okentendi, null).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    // Solicita ao usu�rio para ligar o GPS
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ObterLocalizacaoTreinadorActivity.this);
                    alertDialogBuilder.setMessage(R.string.gpsDesligado)
                            .setCancelable(false).setPositiveButton(
                            R.string.sim,
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
                    android.app.AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }

            }
        }).show();

        //Mapa
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);



        checkBox = (CheckBox)findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    if(apiConnect){
                        startLocationUpdates();
                        progress = ProgressDialog.show(ObterLocalizacaoTreinadorActivity.this, "Aguarde...",getResources().getString(R.string.obtendoLocalizacao), true, true);
                        progress.setCancelable(false);
                    } else{
                        Snackbar.make(buttonView ,R.string.falhaLocalizacao,Snackbar.LENGTH_LONG).show();
                    }

                }


            }
        });


        Button btnAvancar = (Button)findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkBox.isChecked()){
                    locStatus = "ativo";

                    new AlertDialog.Builder(ObterLocalizacaoTreinadorActivity.this).setTitle(getResources().getString(R.string.cadastrar))
                            .setIcon(R.drawable.ic_map_marker_grey600_36dp)
                            .setMessage(R.string.cadastrarlocalizacao)
                            .setPositiveButton(R.string.tenhocerteza, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Cadastrar aqui
                                    Log.i("localizacao string", mLatLng.toString());
                                    String localizacao = mLatLng.latitude+","+mLatLng.longitude;
                                    treinador.setLocalizacao(localizacao);
                                    treinador.setLocStatus(locStatus);
                                    CadastroLocalizacaoTask task = new CadastroLocalizacaoTask(ObterLocalizacaoTreinadorActivity.this,treinador);
                                    task.execute();

                                    Intent intent = new Intent(ObterLocalizacaoTreinadorActivity.this, MainActivity.class);
                                    ObterLocalizacaoTreinadorActivity.this.startActivity(intent);

                                }
                            })
                            .setNegativeButton(R.string.esperevoutentar, null).show();

                }else{
                    locStatus = "inativo";
                    new AlertDialog.Builder(ObterLocalizacaoTreinadorActivity.this).setTitle(getResources().getString(R.string.avancar))
                            .setIcon(R.drawable.ic_map_marker_grey600_36dp)
                            .setMessage(ObterLocalizacaoTreinadorActivity.this.getResources().getString(R.string.msgnaolocalizacaotitulo))
                            .setPositiveButton(R.string.tenhocerteza, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(ObterLocalizacaoTreinadorActivity.this, MainActivity.class);
                                    ObterLocalizacaoTreinadorActivity.this.startActivity(intent);

                                }
                            })
                            .setNegativeButton(R.string.nao, null).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_obter_localizacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        apiConnect = true;

    }

    @Override
    public void onConnectionSuspended(int i) {
        apiConnect = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        apiConnect = false;
        Log.i("api FALHA","api FALHA");
    }

    protected  void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }



    private void atualizarMapa(){
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 10.0f));
        marker.position(mLatLng).title(treinador.getNome()).snippet(getResources().getString(R.string.voceestaaqui));
        mGoogleMap.addMarker(marker);
        mapAtualizado = true;
    }


    @Override
    public void onLocationChanged(Location location) {
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(location.getAccuracy() <= 20){
            stopLocationUpdates();
            progress.dismiss();
            atualizarMapa();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = getIntent();
                    startActivity(intent);
                    finish();

                    // mGoogleApiClient.connect();


                    //// GPS estah ligado
                    //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);


                } else {

                    new android.app.AlertDialog.Builder(this).setTitle(getResources().getString(R.string.permissaoNegada))
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

                                    Intent intent = new Intent(ObterLocalizacaoTreinadorActivity.this, MainActivity.class);
                                    ObterLocalizacaoTreinadorActivity.this.startActivity(intent);

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
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            //}

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);

    }
}

