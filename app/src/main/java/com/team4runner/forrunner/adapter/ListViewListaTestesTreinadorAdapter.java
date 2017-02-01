package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team4runner.forrunner.CadastroTesteActivity;
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
 * Created by Lucas on 06/01/2016.
 */
public class ListViewListaTestesTreinadorAdapter extends BaseAdapter {

    private Activity activity;
    private List<TesteDeCampo> mList;
    private Corredor corredor;
    private MeusCorredoresTreinadorTestesFragment fragment;

    private Toolbar toolbar;


    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    public ListViewListaTestesTreinadorAdapter(Activity activity, List<TesteDeCampo> mList, Corredor corredor, MeusCorredoresTreinadorTestesFragment fragment) {
        this.activity = activity;
        this.mList = mList;
        this.corredor = corredor;
        this.fragment = fragment;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_teste, parent, false);

        //Toolbar com menu
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_teste_campo);

        TextView txtVolume = (TextView) view.findViewById(R.id.txtVolume);
        if(mList.get(position).getTipoVolume().equals("minutos")){
            DecimalFormat df = new DecimalFormat("00.00");
            String auxVolume = df.format(mList.get(position).getVolume());
            txtVolume.setText(auxVolume.replace(".",":") + " " + mList.get(position).getTipoVolume());
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
            toolbar.getMenu().removeItem(R.id.visualizar_corrida);
        } else if (mList.get(position).getSituacao().equals("cancelado")) {
            toolbar.getMenu().clear();
        }



        //Click no menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.editar:
                        DialogFragment newFragment = MeusCorredoresTreinadorTestesFragment.MyDialogFragment.newInstance(mList.get(position), fragment);
                        newFragment.show(fragment.getChildFragmentManager(), "Dialog");
                        break;
                    case R.id.cancelar:
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.cancelarteste)
                                .setMessage(R.string.desejacancelarteste)
                                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        CancelarTesteDeCampoTask task = new CancelarTesteDeCampoTask(activity, mList.get(position));
                                        task.execute();

                                        try {
                                            Boolean resultado = (Boolean) task.get();
                                            if (resultado) {

                                                ((MeuCorredorTreinadorActivity) activity).atualizaListaTeste();
                                            }


                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }).setNegativeButton(R.string.nao, null).show();
                        break;
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
