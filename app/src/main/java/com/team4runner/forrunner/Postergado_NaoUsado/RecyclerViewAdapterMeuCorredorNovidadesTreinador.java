package com.team4runner.forrunner.Postergado_NaoUsado;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team4runner.forrunner.R;

import java.util.List;

/**
 * Created by Lucas on 05/07/2015.
 */
public class RecyclerViewAdapterMeuCorredorNovidadesTreinador extends RecyclerView.Adapter<RecyclerViewAdapterMeuCorredorNovidadesTreinador.MyViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<String> mList;
    //para teste
    private int count = 0;


    public RecyclerViewAdapterMeuCorredorNovidadesTreinador(Context context, List<String> list) {
        mList = list;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Aqui se declara qual sera o layout de cada View (item da lista)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (count % 2 == 0) {

            View v = mLayoutInflater.inflate(R.layout.item_list_home_novidades_solicitacao_corredor, viewGroup, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            count++;

            return myViewHolder;


        } else {
            View v = mLayoutInflater.inflate(R.layout.item_list_home_novidades_medium, viewGroup, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            count++;

            return myViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //ViewHolder  //Aqui se declara e vincula os componentes de cada item da lista
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);


        }
    }

}