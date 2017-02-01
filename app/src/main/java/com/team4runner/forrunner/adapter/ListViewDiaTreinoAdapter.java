package com.team4runner.forrunner.adapter;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.TreinoExercicio;

import java.util.List;

/**
 * Created by Lucas on 19/11/2015.
 */
public class ListViewDiaTreinoAdapter extends BaseAdapter {

    private Context context;
    private List<TreinoExercicio> atividades;

    public ListViewDiaTreinoAdapter(Context context, List<TreinoExercicio> atividades){
        this.context = context;
        this.atividades = atividades;
    }

    @Override
    public int getCount() {
        return atividades.size();
    }

    @Override
    public Object getItem(int position) {
        return atividades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Escolhe o layout correto (sem intervalo / com intervalo)
        if (atividades.get(position).getPosIntervalo() == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_diatreino, parent, false);

            TextView txtNomeExercicio = (TextView)view.findViewById(R.id.nome_exercicio);
            txtNomeExercicio.setText(String.valueOf(atividades.get(position).getExercicio().getNomeExercicio()));

            TextView txtIntensidade = (TextView)view.findViewById(R.id.txtIntensidade);
            String auxStringRitmo = String.valueOf(atividades.get(position).getRitmo());
            txtIntensidade.setText(auxStringRitmo.replace(".",":"));

            TextView txtVolume = (TextView)view.findViewById(R.id.txtVolume);
            txtVolume.setText(String.valueOf(atividades.get(position).getVolume()));

            if (atividades.get(position).getQtdRepeticoes() != 1){

                TextView txtQtdRepeticoes = (TextView)view.findViewById(R.id.txtQtdRepeticoes);
                txtQtdRepeticoes.setText(String.valueOf(atividades.get(position).getQtdRepeticoes()));

                TextView txtIntervaloRepeticoes = (TextView)view.findViewById(R.id.txtIntervaloRepeticoes);
                txtIntervaloRepeticoes.setText(String.valueOf(atividades.get(position).getIntervaloRepeticoes()));
            } else{
                LinearLayout layoutRep = (LinearLayout)view.findViewById(R.id.layoutRepeticoes);
                layoutRep.setVisibility(View.GONE);
            }

            ImageView btnDescricaoExercicio = (ImageView)view.findViewById(R.id.btnDescricaoExercicio);
            btnDescricaoExercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle(R.string.descricaoexercicio)
                            .setMessage(atividades.get(position).getExercicio().getDescricaoExercicio())
                            .setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            return view;

        } else{

            View view = LayoutInflater.from(context).inflate(R.layout.item_list_diatreino_intervalo, parent, false);

            TextView txtNomeExercicio = (TextView)view.findViewById(R.id.nome_exercicio);
            txtNomeExercicio.setText(String.valueOf(atividades.get(position).getExercicio().getNomeExercicio()));


            TextView txtIntensidade = (TextView)view.findViewById(R.id.txtIntensidade);
            String auxStringRitmo = String.valueOf(atividades.get(position).getRitmo());
            txtIntensidade.setText(auxStringRitmo.replace(".",":"));

            TextView txtVolume = (TextView)view.findViewById(R.id.txtVolume);
            txtVolume.setText(String.valueOf(atividades.get(position).getVolume()));

            if (atividades.get(position).getQtdRepeticoes() != 1){

                TextView txtQtdRepeticoes = (TextView)view.findViewById(R.id.txtQtdRepeticoes);
                txtQtdRepeticoes.setText(String.valueOf(atividades.get(position).getQtdRepeticoes()));

                TextView txtIntervaloRepeticoes = (TextView)view.findViewById(R.id.txtIntervaloRepeticoes);
                txtIntervaloRepeticoes.setText(String.valueOf(atividades.get(position).getIntervaloRepeticoes()));
            }
            else{
                LinearLayout layoutRep = (LinearLayout)view.findViewById(R.id.layoutRepeticoes);
                layoutRep.setVisibility(View.GONE);
            }


            TextView txtDuracaoIntervalo = (TextView)view.findViewById(R.id.txtDuracaoIntervalo);
            txtDuracaoIntervalo.setText(String.valueOf(atividades.get(position).getPosIntervalo()));

            ImageView btnDescricaoExercicio = (ImageView)view.findViewById(R.id.btnDescricaoExercicio);
            btnDescricaoExercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle(R.string.descricaoexercicio)
                            .setMessage(atividades.get(position).getExercicio().getDescricaoExercicio())
                            .setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });



            return view;

        }
    }
}
