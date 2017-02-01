package com.team4runner.forrunner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.LocalizarTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treinador;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 07/08/2015.
 */
public class RecyclerViewAdapterLocalizarTreinadorCorredor extends RecyclerView.Adapter<RecyclerViewAdapterLocalizarTreinadorCorredor.MyViewHolder> {
    private LayoutInflater mLayoutInflater;
    private static LocalizarTreinadorActivity activity;
    private static List<Treinador> mList;
    private  boolean GEO;

    public RecyclerViewAdapterLocalizarTreinadorCorredor(LocalizarTreinadorActivity activity, List<Treinador> mList, boolean GEO) {
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = mList;
        this.GEO = GEO;
    }



    //Declara o layout de cada View
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_list_localizar_treinadores_corredor, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);


        return myViewHolder;
    }

    //Preenche o layout de cada View
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nomeTreinador.setText(mList.get(position).getNome());
        if (!mList.get(position).getImagemPerfil().equals("")) {
            Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + mList.get(position).getImagemPerfil()).into(holder.imagemPerfil);
        }
        float auxMediaAv = (float) mList.get(position).getMediaAvaliacao()/2;
        holder.ratingBar.setRating(auxMediaAv);
        String auxQtdAv = String.valueOf(mList.get(position).getQtdAvaliacoes()) + " " + activity.getResources().getString(R.string.avaliacoes);
        holder.qtdAvaliacoes.setText(auxQtdAv);

        if(GEO){
            int distanciaInt = Integer.parseInt(mList.get(position).getSituacao());
            float distancaFloat = (float) (distanciaInt / 1000.0);
            DecimalFormat df = new DecimalFormat("0.00");
            String distancia = "A " + df.format(distancaFloat) + "Km";
            holder.txtDistancia.setText(distancia);
            holder.txtDistancia.setVisibility(View.VISIBLE);
        } else {
            holder.txtDistancia.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }



    ////ViewHolder  //Aqui se declara e vincula os componentes de cada item da lista
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nomeTreinador;
        public CircleImageView imagemPerfil;
        public RatingBar ratingBar;
        public TextView qtdAvaliacoes;
        public  TextView txtDistancia;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nomeTreinador = (TextView) itemView.findViewById(R.id.textViewNomeTreinador);
            imagemPerfil = (CircleImageView) itemView.findViewById(R.id.circleView);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingTreinador);
            qtdAvaliacoes = (TextView) itemView.findViewById(R.id.qtdAvaliacoes);
            txtDistancia = (TextView) itemView.findViewById(R.id.txtDistancia);
            txtDistancia.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            Treinador treinadorSelecionado = mList.get(getPosition());

            activity.onTreinadorClicado(treinadorSelecionado);

        }
    }
}
