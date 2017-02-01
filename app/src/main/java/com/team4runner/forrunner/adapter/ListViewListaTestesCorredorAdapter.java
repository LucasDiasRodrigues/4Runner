package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.tasks.CancelarTesteDeCampoTask;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 20/01/2016.
 */
public class ListViewListaTestesCorredorAdapter extends BaseAdapter {
    private Activity activity;
    private List<TesteDeCampo> mList;
    private Corredor corredor;

    private Toolbar toolbar;

    boolean isInHomeFragment;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    public ListViewListaTestesCorredorAdapter(Activity activity, List<TesteDeCampo> mList, Corredor corredor) {
        this.activity = activity;
        this.mList = mList;
        this.corredor = corredor;
    }

    public ListViewListaTestesCorredorAdapter(Activity activity, List<TesteDeCampo> mList, Corredor corredor, boolean isInHomeFragment) {
        this.activity = activity;
        this.mList = mList;
        this.corredor = corredor;
        this.isInHomeFragment = isInHomeFragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getCodTeste();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_teste, parent, false);

        //Toolbar com menu
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_teste_campo);

        TextView txtVolume = (TextView) view.findViewById(R.id.txtVolume);
        if (mList.get(position).getTipoVolume().equals("minutos")) {
            DecimalFormat df = new DecimalFormat("00.00");
            String auxVolume = df.format(mList.get(position).getVolume());
            txtVolume.setText(auxVolume.replace(".", ":") + " " + mList.get(position).getTipoVolume());
        } else {
            txtVolume.setText(mList.get(position).getVolume() + " " + mList.get(position).getTipoVolume());
        }
        TextView txtDataTeste = (TextView) view.findViewById(R.id.txtDataTeste);
        txtDataTeste.setText(dateFormat.format(mList.get(position).getDataTeste()) + " ");

        TextView txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtStatus.setText(mList.get(position).getSituacao());

        TextView txtObs = (TextView) view.findViewById(R.id.txtObs);
        txtObs.setText(mList.get(position).getObs() + "");

        if (mList.get(position).getSituacao().equals("concluido")) {
            toolbar.getMenu().removeItem(R.id.editar);
            toolbar.getMenu().removeItem(R.id.cancelar);

        } else if (mList.get(position).getSituacao().equals("pendente")) {
            toolbar.getMenu().clear();
        } else if (mList.get(position).getSituacao().equals("cancelado")) {
            toolbar.getMenu().clear();
        }

        if(isInHomeFragment){
            TextView txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
            txtTitulo.setVisibility(View.VISIBLE);
        }

        //Click no menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.visualizar_corrida:
                        Intent intent = new Intent(activity, VisualizarCorridaActivity.class);
                        intent.putExtra("dia", dateFormat.format(mList.get(position).getDataTeste()));
                        intent.putExtra("corredor", corredor);
                        intent.putExtra("teste", true);

                        activity.startActivity(intent);
                        break;
                }

                return false;
            }
        });


        return view;
    }
}
