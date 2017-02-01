package com.team4runner.forrunner.Postergado_NaoUsado;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;

import java.util.List;

/**
 * Created by Lucas on 01/07/2015.
 */
public class RecyclerViewAdapterAmigosAmigosCorredor extends RecyclerView.Adapter<RecyclerViewAdapterAmigosAmigosCorredor.MyViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Corredor> mList;


    public RecyclerViewAdapterAmigosAmigosCorredor(Context context, List<Corredor> list){
        mList = list;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Aqui se declara qual sera o layout de cada View (item da lista)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.item_list_amigos_amigos_corredor, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.nomeAmigo.setText(mList.get(position).getNome());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //ViewHolder  //Aqui se declara e vincula os componentes de cada item da lista
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nomeAmigo;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeAmigo = (TextView)itemView.findViewById(R.id.textViewNomeAmigo);
        }
    }


}
