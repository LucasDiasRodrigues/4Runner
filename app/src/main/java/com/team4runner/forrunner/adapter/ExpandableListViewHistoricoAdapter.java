package com.team4runner.forrunner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lucas on 01/07/2015.
 */
public class ExpandableListViewHistoricoAdapter extends BaseExpandableListAdapter {

    Context context;
    Treino treino;
    List<TreinoExercicio> treinoExercicios;
    List<Integer> semanas = new ArrayList<>();
    List<Date> datasPorSemana = new ArrayList<>();
    HashMap<Integer, List<TreinoExercicio>> grupoPorSemana = new HashMap<>();


    public ExpandableListViewHistoricoAdapter(Context context, Treino treino, List<TreinoExercicio> treinoExercicios) {
        this.context = context;
        this.treino = treino;
        this.treinoExercicios = treinoExercicios;


        // verifica a lista toda e cria um hashMap com cada semana a pontando para um array dos dias
        for (TreinoExercicio treinoExercicio : this.treinoExercicios) {

            //se nao tem no array semanas // acressenta no array
            if (!semanas.contains(treinoExercicio.getSemana())) {
                semanas.add(treinoExercicio.getSemana());
                Log.i("passou1 ", "ok");
                List<TreinoExercicio> exerciciosPorSemana = new ArrayList<>();
                exerciciosPorSemana.add(treinoExercicio);
                grupoPorSemana.put(treinoExercicio.getSemana(), exerciciosPorSemana);


            } else {
                for (int i = 0; i < semanas.size(); i++) {
                    Log.i("passouloop ", "ok");
                    if (semanas.get(i).equals(treinoExercicio.getSemana())) {
                        Log.i("passou2 ", "ok");

                        List<TreinoExercicio> exerciciosPorSemana = grupoPorSemana.get(semanas.get(i));

                        //Impede que dois exercicios do mesmo dia aparecam
                        List<Date> auxArray = new ArrayList<>();
                        for (TreinoExercicio aux : exerciciosPorSemana) {
                            auxArray.add(aux.getData());
                        }
                        if (!auxArray.contains(treinoExercicio.getData())) {
                            exerciciosPorSemana.add(treinoExercicio);
                            grupoPorSemana.remove(semanas.get(i));
                            grupoPorSemana.put(semanas.get(i), exerciciosPorSemana);
                        }


                    }
                }
            }


        }

    }

    @Override
    public int getGroupCount() {
        return treino.getQtdSemanas();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return grupoPorSemana.get(getGroup(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return semanas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return grupoPorSemana.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_group_expandablelist_historico, null);
        }

        TextView textViewSemana = (TextView) convertView.findViewById(R.id.textViewSemana);

        textViewSemana.setText("Semana " + getGroup(groupPosition).toString());


        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_child_expandablelist_historico, null);
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        TextView textViewDia = (TextView) convertView.findViewById(R.id.textViewDia);
        TextView textViewDiaSemana = (TextView) convertView.findViewById(R.id.textViewDiaSemana);

        TreinoExercicio tr = (TreinoExercicio) getChild(groupPosition, childPosition);
        textViewDia.setText(dateFormat.format(tr.getData()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tr.getData());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case 1:
                textViewDiaSemana.setText(R.string.domingo);
                break;
            case 2:
                textViewDiaSemana.setText(R.string.segundaFeira);
                break;
            case 3:
                textViewDiaSemana.setText(R.string.tercaFeira);
                break;
            case 4:
                textViewDiaSemana.setText(R.string.quartaFeira);
                break;
            case 5:
                textViewDiaSemana.setText(R.string.quintaFeira);
                break;
            case 6:
                textViewDiaSemana.setText(R.string.sextaFeira);
                break;
            case 7:
                textViewDiaSemana.setText(R.string.sabado);
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
