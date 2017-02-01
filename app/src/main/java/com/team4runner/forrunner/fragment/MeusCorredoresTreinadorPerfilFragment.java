package com.team4runner.forrunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.ChatActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 05/07/2015.
 */
public class MeusCorredoresTreinadorPerfilFragment extends Fragment {

    private FloatingActionButton fabChat;
    Corredor corredor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_meucorredor_treinador_perfil, container, false);

        corredor = ((MeuCorredorTreinadorActivity) getActivity()).getCorredor();

        //1 Card
        ImageView imagemPerfil = (ImageView) fragment.findViewById(R.id.fotoPerfil);

        if (!corredor.getImagemPerfil().equals("")) {
            Picasso.with(getActivity()).load(getResources().getString(R.string.imageserver) + corredor.getImagemPerfil()).into(imagemPerfil);
        }
        //2 Card
        TextView txtNome = (TextView) fragment.findViewById(R.id.txtNome);
        TextView txtEmail = (TextView) fragment.findViewById(R.id.txtEmail);
        TextView txtSexo = (TextView) fragment.findViewById(R.id.txtSexo);
        TextView txtTelefone = (TextView) fragment.findViewById(R.id.txtTelefone);
        TextView txtSobreMim = (TextView) fragment.findViewById(R.id.txtSobreMim);
        TextView txtIdade = (TextView) fragment.findViewById(R.id.txtIdade);

        txtNome.setText(corredor.getNome());
        txtEmail.setText(corredor.getEmail());
        txtSexo.setText(corredor.getSexo());
        Integer auxInt = getIdade(corredor.getDataNasc());
        String auxString = auxInt + " " + getResources().getString(R.string.anos);
        txtIdade.setText(auxString);

        if (corredor.getTelefone().equals("")) {
            LinearLayout layoutTelefone = (LinearLayout) fragment.findViewById(R.id.layoutTelefone);
            layoutTelefone.setVisibility(View.GONE);
        } else {
            txtTelefone.setText(corredor.getTelefone());
        }
        if (corredor.getSobreMim().equals("")) {
            LinearLayout layoutSobreMim = (LinearLayout) fragment.findViewById(R.id.layoutSobreMim);
            layoutSobreMim.setVisibility(View.GONE);
        } else {
            txtSobreMim.setText(corredor.getSobreMim());
        }


        //3 Card
        TextView txtAltura = (TextView) fragment.findViewById(R.id.txtAltura);
        TextView txtPeso = (TextView) fragment.findViewById(R.id.txtPeso);
        TextView txtObj = (TextView) fragment.findViewById(R.id.txtObj);
        TextView txtCorredorDesde = (TextView) fragment.findViewById(R.id.txtCorredorDesde);


        txtAltura.setText(String.valueOf(corredor.getAltura()));
        String auxPeso = String.valueOf(corredor.getPeso()) + " Kg";
        txtPeso.setText(auxPeso);
        txtObj.setText(corredor.getObjetivo() + "K");


        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        txtCorredorDesde.setText(format.format(corredor.getDataCadastro()));

        //Chat
        fabChat = (FloatingActionButton)fragment.findViewById(R.id.fabChat);
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), ChatActivity.class);
                it.putExtra("corredor", corredor);
                it.putExtra("origem","treinador");
                startActivity(it);
            }
        });


        return fragment;
    }



    public int getIdade(Date dataNasc) {
        // Data de hoje.
        GregorianCalendar agora = new GregorianCalendar();
        int ano = 0, mes = 0, dia = 0;
        // Data do nascimento.
        GregorianCalendar nascimento = new GregorianCalendar();
        int anoNasc = 0, mesNasc = 0, diaNasc = 0;
        // Idade.
        int idade = 0;


        nascimento.setTime(dataNasc);
        ano = agora.get(Calendar.YEAR);
        mes = agora.get(Calendar.MONTH) + 1;
        dia = agora.get(Calendar.DAY_OF_MONTH);
        anoNasc = nascimento.get(Calendar.YEAR);
        mesNasc = nascimento.get(Calendar.MONTH) + 1;
        diaNasc = nascimento.get(Calendar.DAY_OF_MONTH);
        idade = ano - anoNasc;
        // Calculando diferencas de mes e dia.
        if (mes < mesNasc) {
            idade--;
        } else {
            if (dia < diaNasc) {
                idade--;
            }
        }
        // Ultimo teste, idade "negativa".
        if (idade < 0) {
            idade = 0;
        }
        Log.i("idade",idade+"  lalala");

        return (idade);
    }
}
