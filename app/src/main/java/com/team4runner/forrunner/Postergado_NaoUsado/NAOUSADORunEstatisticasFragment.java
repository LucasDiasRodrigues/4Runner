package com.team4runner.forrunner.Postergado_NaoUsado;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.team4runner.forrunner.R;

/**
 * Created by Lucas on 17/09/2015.
 */
public class NAOUSADORunEstatisticasFragment extends Fragment {

    private TextView mDistancia;
    private TextView mRitmo;
    private Chronometer mchronometer;
    private long milliseconds;
    private long millisecondsStop;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout._naousado_fragment_run_estatisticas,container,false);

        //CRONOMETRO
        mchronometer = (Chronometer) fragment.findViewById(R.id.chronometer);
        mchronometer.setText(DateFormat.format("hh:mm:ss", 0));

        mchronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long aux = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.setText(DateFormat.format("hh:mm:ss", aux));
            }
        });
        milliseconds = 0;
        millisecondsStop = 0;

        millisecondsStop = millisecondsStop > 0 ? System.currentTimeMillis() - millisecondsStop : 0;
        mchronometer.setBase(SystemClock.elapsedRealtime() - (milliseconds + millisecondsStop));
        mchronometer.start();
        millisecondsStop = 0;

        //

        mDistancia = (TextView)fragment.findViewById(R.id.txtDistancia);
        mRitmo = (TextView)fragment.findViewById(R.id.txtRitmo);









        return fragment;
    }
}
