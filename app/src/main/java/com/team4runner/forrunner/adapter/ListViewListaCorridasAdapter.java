package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corrida;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

/**
 * Created by Lucas on 23/10/2015.
 */
public class ListViewListaCorridasAdapter extends BaseAdapter {

    private Activity activity;
    private List<Corrida> mListCorridas;

    private DateFormat dateFormat;


    public ListViewListaCorridasAdapter(Activity activity, List<Corrida> mListCorridas) {
        this.activity = activity;
        this.mListCorridas = mListCorridas;

        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }


    @Override
    public int getCount() {
        return mListCorridas.size();
    }

    @Override
    public Object getItem(int position) {
        return mListCorridas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_corrida, parent, false);

        TextView txtDataCorrida = (TextView) view.findViewById(R.id.txtDataCorrida);
        TextView txtDistancia = (TextView) view.findViewById(R.id.txtDistancia);
        TextView txtDuracao = (TextView) view.findViewById(R.id.txtDuracao);
        txtDuracao.setVisibility(View.VISIBLE);
        txtDataCorrida.setText(dateFormat.format(mListCorridas.get(position).getDtHoraInicio()));
        if (mListCorridas.get(position).getDistancia() > 999.0) {
            DecimalFormat df = new DecimalFormat("0000.00");
            txtDistancia.setText(String.valueOf(df.format(mListCorridas.get(position).getDistancia())));
        } else if (mListCorridas.get(position).getDistancia() > 9999.0) {
            DecimalFormat df = new DecimalFormat("00000.00");
            txtDistancia.setText(String.valueOf(df.format(mListCorridas.get(position).getDistancia())));
        } else {
            DecimalFormat df = new DecimalFormat("000.00");
            txtDistancia.setText(String.valueOf(df.format(mListCorridas.get(position).getDistancia())));
        }
        txtDuracao.setText(obterDuracao(mListCorridas.get(position).getDtHoraInicio(), mListCorridas.get(position).getDtHoraFim()));

        return view;
    }

    private String obterDuracao(Date inicio, Date fim) {

        //in milliseconds
        long diff = fim.getTime() - inicio.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        System.out.print(diffDays + " days, ");
        System.out.print(diffHours + " hours, ");
        System.out.print(diffMinutes + " minutes, ");
        System.out.print(diffSeconds + " seconds.");

        DecimalFormat df = new DecimalFormat("00");

        String duracao = df.format(diffHours) + ":" + df.format(diffMinutes) + ":" + df.format(diffSeconds);

        return duracao;

    }

}
