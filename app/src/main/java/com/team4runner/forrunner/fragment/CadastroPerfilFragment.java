package com.team4runner.forrunner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.R;

/**
 * Created by Lucas on 17/06/2015.
 */
public class CadastroPerfilFragment extends Fragment {

    Button btnCorredor;
    Button btnTreinador;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_perfil, container, false);

        btnCorredor = (Button)fragment.findViewById(R.id.btnCorredor);
        btnCorredor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CadastroUsuarioActivity)getActivity()).onCadastroBasicoCorredor();

            }
        });

        btnTreinador = (Button)fragment.findViewById(R.id.btnTreinador);
        btnTreinador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CadastroUsuarioActivity)getActivity()).onCadastroBasicoTreinador();


            }
        });





        return fragment;
    }
}
