package com.team4runner.forrunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.R;

/**
 * Created by Lucas on 30/05/2015.
 */
public class MainFragment extends Fragment {

    private Button btnEntrar;
    private Button btnCadastrese;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View cardEntrar = inflater.inflate(R.layout.fragment_card_main, container, false);

        this.btnEntrar = (Button) cardEntrar.findViewById(R.id.btnEntrar);



        return cardEntrar;
    }


}
