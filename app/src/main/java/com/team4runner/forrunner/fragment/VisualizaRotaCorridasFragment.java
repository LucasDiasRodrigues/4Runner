package com.team4runner.forrunner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.modelo.PontoRota;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Lucas on 29/12/2015.
 */
public class VisualizaRotaCorridasFragment extends Fragment implements OnMapReadyCallback {

    protected GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;


    private Corrida corrida;
    private List<PontoRota> pontos = new ArrayList<>();
    private List<LatLng> latLngs = new ArrayList<>();
    private TextView txtDistancia;
    private TextView txtDuracao;
    private TextView txtCalorias;
    private TextView txtRitMed;
    private TextView txtRitMax;
    private TextView txtVelMed;
    private TextView txtVelMax;

    private DecimalFormat df = new DecimalFormat("00.00");

    private ProgressBar progressBar;

    boolean canDraw = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_geral_visualiza_corrida, container, false);

        ((VisualizarCorridaActivity)getActivity()).setFragRota(this);

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);

        configureMap();

        txtDistancia = (TextView) fragment.findViewById(R.id.txtDistancia);
        txtDuracao = (TextView) fragment.findViewById(R.id.txtDuracao);
        txtCalorias = (TextView) fragment.findViewById(R.id.txtCalorias);
        txtRitMed = (TextView) fragment.findViewById(R.id.txtRitMed);
        txtRitMax = (TextView) fragment.findViewById(R.id.txtRitMax);
        txtVelMed = (TextView) fragment.findViewById(R.id.txtVelMed);
        txtVelMax = (TextView) fragment.findViewById(R.id.txtVelMax);


        return fragment;
    }


    public void desenha() {

        if (pontos != null && pontos.size() > 0) {

            for (PontoRota pontoRota : pontos) {

                latLngs.add(pontoRota.getLatLng());

            }
            PolylineOptions PolOptions;
            PolOptions = new PolylineOptions();
            PolOptions.color(Color.RED);
            PolOptions.width(8);
            PolOptions.addAll(latLngs);
            mGoogleMap.addPolyline(PolOptions);
            Log.i("DESENHA", pontos.toString());

            CameraPosition position = new CameraPosition.Builder()
                    .target(latLngs.get(0))    // Localiza��o
                    .bearing(0)        // Dire��o em que a c�mera est� apontando em graus
                    .tilt(0)            // �ngulo que a c�mera est� posicionada em graus (0 a 90)
                    .zoom(17)            // Zoom
                    .build();

            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mGoogleMap.animateCamera(update);
        }

    }


    protected void configureMap() {
        if (mGoogleMap == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);

            // Recupera o objeto GoogleMap
            mapFragment.getMapAsync(this);

            if (mGoogleMap != null) {
                // Configura o tipo do mapa
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Configura o tipo do mapa
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(canDraw)
            desenha();
        else
            canDraw = true;

    }

    public void setCorrida(Corrida corrida, List<PontoRota> pontos) {
        this.corrida = corrida;
        this.pontos = pontos;

        preencherCampos();

    }

    private void preencherCampos() {

        txtDistancia.setText(df.format(corrida.getDistancia()));

        //Converter tempo para base 6//
        int auxDuracaoInt = (int) pontos.get(pontos.size() - 1).getTempo();
        double auxDuracaoDouble = pontos.get(pontos.size() - 1).getTempo() - auxDuracaoInt;
        double auxDuracaoFinal = auxDuracaoInt + (auxDuracaoDouble * 60) / 100;
        String auxDuracaoString = String.valueOf(df.format(auxDuracaoFinal));
        String auxDuracaoStringFinal = auxDuracaoString.replace(",", ":");

        txtDuracao.setText(auxDuracaoStringFinal);
        txtCalorias.setText(df.format(corrida.getCalorias()));
        String auxStringTimeFormat;
        auxStringTimeFormat = df.format(corrida.getRitmoMedio()) + " Min/Km";
        txtRitMed.setText(auxStringTimeFormat.replace(",", ":"));

        if (corrida.getVelocidadeMax() > 0) {
            auxStringTimeFormat = df.format((1000 / corrida.getVelocidadeMax() / 60));
            Log.i("LogSplitRit", auxStringTimeFormat + "");
            String auxCalculoRitmo[] = auxStringTimeFormat.split(Pattern.quote(","));
            Log.i("LogSplitRit", auxCalculoRitmo[0] + " - " + auxCalculoRitmo[1]);
            Float auxFloatCalculoRitmo2 = Float.parseFloat(auxCalculoRitmo[1]);
            int auxCalculoRitmo3 = (int) ((60 * auxFloatCalculoRitmo2) / 100);
            DecimalFormat decimalFormat = new DecimalFormat("00");
            String auxRitmoFinal = auxCalculoRitmo[0] + ":" + decimalFormat.format(auxCalculoRitmo3);
            txtRitMax.setText(auxRitmoFinal + " Min/Km");
        } else {
            txtRitMax.setText("00:00 Min/Km");
        }

        txtVelMed.setText(df.format(corrida.getVelocidadeMedia() * 3.6) + " Km/h");
        txtVelMax.setText(df.format(corrida.getVelocidadeMax() * 3.6) + " Km/h");


        if(canDraw)
            desenha();
        else
            canDraw = true;

        progressBar.setVisibility(View.GONE);


    }


}
