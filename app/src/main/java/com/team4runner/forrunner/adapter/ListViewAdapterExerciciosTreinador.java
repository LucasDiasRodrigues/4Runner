package com.team4runner.forrunner.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroExercicioActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.fragment.ExerciciosTreinadorFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.tasks.CadastroExercicioTask;
import com.team4runner.forrunner.tasks.CancelarTesteDeCampoTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 29/06/2015.
 */
public class ListViewAdapterExerciciosTreinador extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private static List<Exercicio> mList;
    private static Context context;
    ExerciciosTreinadorFragment fragment;
    Toolbar toolbar;


    public ListViewAdapterExerciciosTreinador(Context context, List<Exercicio> list,ExerciciosTreinadorFragment fragment) {
        mList = list;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
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
        return mList.get(position).getCodExercicio();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_list_exercicio_treinador, parent, false);

        //Toolbar com menu
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_cadastro_exercicio);

        TextView nomeExercicio = (TextView) v.findViewById(R.id.nomeExercicio);
        nomeExercicio.setText(mList.get(position).getNomeExercicio());

        if(mList.get(position).getEmailTreinador().equals(context.getResources().getString(R.string.admtreinador))){
            toolbar.setVisibility(View.GONE);
        }

        //Click no menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.editar:
                        DialogFragment newFragment = ExerciciosTreinadorFragment.MyDialogFragment.newInstance(mList.get(position));
                        newFragment.show(fragment.getChildFragmentManager(), "Dialog");
                        break;
                    case R.id.cancelar:
                        //Inativar
                        new AlertDialog.Builder(context).setTitle(R.string.inativarexercicio)
                                .setMessage(R.string.desejainativarexercicio)
                                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Exercicio exercicio = mList.get(position);
                                        exercicio.inativarExercicio(context, fragment);
                                    }
                                })
                                .setNegativeButton(R.string.cancelar, null)
                                .show();
                        break;
                }


                return false;
            }
        });


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(mList.get(position).getNomeExercicio())
                        .setMessage(mList.get(position).getDescricaoExercicio())
                        .show();
            }
        });


        return v;
    }

}
