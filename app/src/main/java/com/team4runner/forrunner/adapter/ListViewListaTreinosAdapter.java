package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team4runner.forrunner.DetalheTreinoActivity;
import com.team4runner.forrunner.HistoricoPorTreinoActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.tasks.CancelarTesteDeCampoTask;
import com.team4runner.forrunner.tasks.CancelarTreinoTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 27/08/2015.
 */
public class ListViewListaTreinosAdapter extends BaseAdapter {

    private Activity activity;
    private List<Treino> mList;
    private String perfil;
    private Corredor corredor;

    private Toolbar toolbar;

    public ListViewListaTreinosAdapter(Activity activity, List<Treino> mList, String perfil, Corredor corredor) {
        this.activity = activity;
        this.mList = mList;
        this.perfil = perfil;
        this.corredor = corredor;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_treino, parent, false);

        //Toolbar com menu
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_treino);

        TextView txtNomeTreino = (TextView) view.findViewById(R.id.nomeTreino);
        txtNomeTreino.setText((mList.get(position)).getNome());

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        TextView txtDataInicio = (TextView) view.findViewById(R.id.txtDataInicio);
        txtDataInicio.setText(format.format((mList.get(position)).getDtInicio()));

        TextView txtDataTermino = (TextView) view.findViewById(R.id.txtDataTermino);
        txtDataTermino.setText(format.format((mList.get(position)).getDtFim()));

        TextView txtNomeTreinador = (TextView) view.findViewById(R.id.txtNomeTreinador);
        txtNomeTreinador.setText(mList.get(position).getEmailTreinador());

        TextView txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        Date data = new Date();
        Date dataHoje = new Date();
        String dataHojeS = format.format(data);
        try {
            dataHoje = format.parse(dataHojeS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("DataConvertida", dataHojeS);


//        int dataint = mList.get(position).getDtFim().compareTo(dataHoje);

        if (mList.get(position).getDtInicio().before(new Date()) && (mList.get(position).getDtFim().after(dataHoje) || mList.get(position).getDtFim().compareTo(dataHoje) == 0) && mList.get(position).getSituacao().equals("ativo")) {
            txtStatus.setText("Em andamento");
        } else if (mList.get(position).getDtFim().before(dataHoje) && mList.get(position).getSituacao().equals("ativo")) {
            txtStatus.setText("Concluido");
        } else if (mList.get(position).getSituacao().equals("ativo")) {
            txtStatus.setText("Agendado");
        } else if (mList.get(position).getDtFim().before(dataHoje) && mList.get(position).getSituacao().equals("concluido")) {
            txtStatus.setText("Concluido");
        } else if (mList.get(position).getSituacao().equals("inativo")) {
            txtStatus.setText("Cancelado");
        } else {
            txtStatus.setText(mList.get(position).getSituacao());
        }


        if (mList.get(position).getSituacao().equals("concluido") || mList.get(position).getSituacao().equals("inativo")) {
            toolbar.getMenu().removeItem(R.id.cancelar);
        }
        if (!perfil.equals("treinador")) {
            toolbar.getMenu().removeItem(R.id.cancelar);
        }

        //Click no menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                               @Override
                                               public boolean onMenuItemClick(MenuItem item) {

                                                   switch (item.getItemId()) {
                                                       case R.id.cancelar:
                                                           new AlertDialog.Builder(activity).setTitle(R.string.cancelarTreino)
                                                                   .setMessage(R.string.cancelarTreinoMsg)
                                                                   .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                           CancelarTreinoTask task = new CancelarTreinoTask(activity, mList.get(position));
                                                                           task.execute();

                                                                           try {
                                                                               Boolean resultado = (Boolean) task.get();
                                                                               if (resultado) {
                                                                                   //AtualizarLista

                                                                                   ((MeuCorredorTreinadorActivity) activity).atualizaListaTreinos();


                                                                               }
                                                                           } catch (InterruptedException e) {
                                                                               e.printStackTrace();
                                                                           } catch (ExecutionException e) {
                                                                               e.printStackTrace();
                                                                           }

                                                                       }
                                                                   })
                                                                   .setNegativeButton(R.string.nao, null).show();
                                                           break;
                                                       case R.id.detalhesTreino:
                                                           Intent intent = new Intent(activity, DetalheTreinoActivity.class);
                                                           intent.putExtra("treino", mList.get(position));
                                                           activity.startActivity(intent);
                                                           break;
                                                   }

                                                   return false;
                                               }
                                           }

        );


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Treino treinoSelecionado = mList.get(position);
                Intent intent = new Intent(activity, HistoricoPorTreinoActivity.class);
                intent.putExtra("treino", treinoSelecionado);
                intent.putExtra("corredor", corredor);
                activity.startActivity(intent);
            }
        });

        return view;
    }
}
