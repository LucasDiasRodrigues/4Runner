package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.LocalizarCorredoresActivity;
import com.team4runner.forrunner.NovosCorredoresActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizaPerfilCorredorActivity;
import com.team4runner.forrunner.modelo.Corredor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 14/12/2015.
 */
public class RecyclerViewAdapterNovosCorredoresTreinador extends RecyclerView.Adapter<RecyclerViewAdapterNovosCorredoresTreinador.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private static List mList;
    private String tipo;
    private static Activity activity;


    public RecyclerViewAdapterNovosCorredoresTreinador(Activity activity, List list, String tipo){
        mList = list;
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tipo = tipo;



    }


    //Aqui se declara qual sera o layout de cada View (item da lista)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = mLayoutInflater.inflate(R.layout.item_list_home_novidades_solicitacao_corredor, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.textViewNomeCorredor.setText(((List<Corredor>)mList).get(position).getNome());
        viewHolder.txtSexo.setText(((List<Corredor>)mList).get(position).getSexo());
        Integer auxInt = getIdade(((List<Corredor>)mList).get(position).getDataNasc());
        String auxString = auxInt + " " + activity.getResources().getString(R.string.anos);
        viewHolder.txtIdade.setText(auxString);



        if (!((List<Corredor>) mList).get(position).getImagemPerfil().equals("")) {
            Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + ((List<Corredor>) mList).get(position).getImagemPerfil()).into(viewHolder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //ViewHolder  //Aqui se declara e vincula os componentes de cada item da lista
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView imageView;
        TextView txtTitulo;
        TextView textViewNomeCorredor;
        TextView txtSexo;
        TextView  txtIdade;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (CircleImageView)itemView.findViewById(R.id.circleView);
            textViewNomeCorredor = (TextView)itemView.findViewById(R.id.textViewNomeCorredor);
            txtSexo = (TextView)itemView.findViewById(R.id.txtSexo);
            txtIdade = (TextView)itemView.findViewById(R.id.txtIdade);
        }

        @Override
        public void onClick(View v) {

            Corredor corredorSelecionado = ((List<Corredor>)mList).get(getPosition());

            ((NovosCorredoresActivity)activity).onCorredorClicado(corredorSelecionado);


        }
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
        Log.i("idade", idade + "  lalala");

        return (idade);
    }

}