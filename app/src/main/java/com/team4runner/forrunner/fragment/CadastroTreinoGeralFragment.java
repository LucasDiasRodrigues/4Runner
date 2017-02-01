package com.team4runner.forrunner.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.tasks.ConsultaTreinoTesteAtivoTask;
import com.team4runner.forrunner.tasks.ListaObjetivosTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 14/07/2015.
 */
public class CadastroTreinoGeralFragment extends Fragment {

    //Obj
    private Spinner spnObj;
    List<Objetivo> objetivos;

    //Componentes
    SeekBar seekBarSemanas;
    static CheckBox checkDomingo;
    static CheckBox checkSegunda;
    static CheckBox checkTerca;
    static CheckBox checkQuarta;
    static CheckBox checkQuinta;
    static CheckBox checkSexta;
    static CheckBox checkSabado;
    EditText editTextNomeTreino;
    static EditText editTextDataInicio;
    TextView textViewQtdSemanas;
    FloatingActionButton fabCadastrarTreinoEspecifico;

    LinearLayout clickCalendario;

    List<Integer> diasSemana = new ArrayList<>();

    List<Bundle> finalDiasTreino = new ArrayList<>();
    Date dataFinal = new Date();

    //Modelo
    Treino treino;

    static int dayOfWeek;

    static DateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_treino_geral, container, false);


        editTextNomeTreino = (EditText) fragment.findViewById(R.id.editTextNomeTreino);
        spnObj = (Spinner) fragment.findViewById(R.id.spinnerObj);
        configuraSpinner();
        textViewQtdSemanas = (TextView) fragment.findViewById(R.id.textViewQtdSemanas);
        textViewQtdSemanas.setText("1");
        seekBarSemanas = (SeekBar) fragment.findViewById(R.id.seekBarSemanas);
        seekBarSemanas.setMax(23);//qtd maxima de semanas
        seekBarSemanas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String stringProgressSemanas = "" + (progress + 1);
                textViewQtdSemanas.setText(stringProgressSemanas);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editTextDataInicio = (EditText) fragment.findViewById(R.id.dataInicio);
        clickCalendario =(LinearLayout) fragment.findViewById(R.id.clickCalendario);
        clickCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        checkDomingo = (CheckBox) fragment.findViewById(R.id.checkDomingo);
        checkSegunda = (CheckBox) fragment.findViewById(R.id.checkSegunda);
        checkTerca = (CheckBox) fragment.findViewById(R.id.checkTerca);
        checkQuarta = (CheckBox) fragment.findViewById(R.id.checkQuarta);
        checkQuinta = (CheckBox) fragment.findViewById(R.id.checkQuinta);
        checkSexta = (CheckBox) fragment.findViewById(R.id.checkSexta);
        checkSabado = (CheckBox) fragment.findViewById(R.id.checkSabado);

        treino = new Treino();
        /*
        fabCadastrarTreinoEspecifico = (FloatingActionButton) fragment.findViewById(R.id.fabCadastrarTreinoEspecifico);
        fabCadastrarTreinoEspecifico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        */

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        finalDiasTreino.clear();
        ((CadastroTreinoActivity)getActivity()).deleteFragmentsList();

        Log.i("onStart", "fragmentGeral");
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!editTextDataInicio.getText().toString().equals("")) {
            switch (dayOfWeek) {
                case 1:
                    checkDomingo.setChecked(true);
                    checkDomingo.setEnabled(false);
                    break;
                case 2:
                    checkSegunda.setChecked(true);
                    checkSegunda.setEnabled(false);
                    break;
                case 3:
                    checkTerca.setChecked(true);
                    checkTerca.setEnabled(false);
                    break;
                case 4:
                    checkQuarta.setChecked(true);
                    checkQuarta.setEnabled(false);
                    break;
                case 5:
                    checkQuinta.setChecked(true);
                    checkQuinta.setEnabled(false);
                    break;
                case 6:
                    checkSexta.setChecked(true);
                    checkSexta.setEnabled(false);
                    break;
                case 7:
                    checkSabado.setChecked(true);
                    checkSabado.setEnabled(false);
                    break;
            }
        }

    }

    private void configuraCheckSemanas() {

        diasSemana.clear();

        if (checkDomingo.isChecked()) {
            diasSemana.add(1);
        }
        if (checkSegunda.isChecked()) {
            diasSemana.add(2);
        }
        if (checkTerca.isChecked()) {
            diasSemana.add(3);
        }
        if (checkQuarta.isChecked()) {
            diasSemana.add(4);
        }
        if (checkQuinta.isChecked()) {
            diasSemana.add(5);
        }
        if (checkSexta.isChecked()) {
            diasSemana.add(6);
        }
        if (checkSabado.isChecked()) {
            diasSemana.add(7);
        }

        treino.setQtdDias(diasSemana.size());

    }


    private void configuraSpinner() {

        //pegar dados do server aqui
        ListaObjetivosTask lObjTask = new ListaObjetivosTask(getActivity());
        lObjTask.execute();
        try {
            objetivos = (List) lObjTask.get();

            ((CadastroTreinoActivity)getActivity()).setListObj(objetivos);


            // String[] objts = new String[];
            ArrayAdapter<Objetivo> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, objetivos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spnObj.setAdapter(adapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            Date dataHoje = new Date();
            DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);

            picker.getDatePicker().setMinDate(dataHoje.getTime());
            return picker;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String txtmes = "";
            String txtdia = "";

            month = month + 1;

            if (month < 10) {
                txtmes = "0" + String.valueOf(month);
            } else {
                txtmes = String.valueOf(month);
            }
            if (day < 10) {
                txtdia = "0" + String.valueOf(day);
            } else {
                txtdia = String.valueOf(day);
            }

            String aux = txtdia + "/" + txtmes + "/" + year;
            editTextDataInicio.setText(aux);

            try {
                Calendar c = Calendar.getInstance();
                c.setTime(brDateFormat.parse(editTextDataInicio.getText().toString()));
                dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                Log.i("Dia da semana", " " + dayOfWeek + " este");

                checkDomingo.setEnabled(true);
                checkSegunda.setEnabled(true);
                checkTerca.setEnabled(true);
                checkQuarta.setEnabled(true);
                checkQuinta.setEnabled(true);
                checkSexta.setEnabled(true);
                checkSabado.setEnabled(true);

                switch (dayOfWeek) {
                    case 1:
                        checkDomingo.setChecked(true);
                        checkDomingo.setEnabled(false);
                        break;
                    case 2:
                        checkSegunda.setChecked(true);
                        checkSegunda.setEnabled(false);
                        break;
                    case 3:
                        checkTerca.setChecked(true);
                        checkTerca.setEnabled(false);
                        break;
                    case 4:
                        checkQuarta.setChecked(true);
                        checkQuarta.setEnabled(false);
                        break;
                    case 5:
                        checkQuinta.setChecked(true);
                        checkQuinta.setEnabled(false);
                        break;
                    case 6:
                        checkSexta.setChecked(true);
                        checkSexta.setEnabled(false);
                        break;
                    case 7:
                        checkSabado.setChecked(true);
                        checkSabado.setEnabled(false);
                        break;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }


    private void obtemDatas(Date dataInicio) {

        int countSomarData = 0;

        //Primeira semana
        for (int i = dayOfWeek; i <= 7; i++) {
            if (diasSemana.contains(i)) {
                Calendar c = Calendar.getInstance();
                c.setTime(dataInicio);
                c.add(Calendar.DATE, +countSomarData);

                Date data = c.getTime();
                Bundle bundle = new Bundle();
                bundle.putInt("semana", 1);
                bundle.putSerializable("data", data);

                finalDiasTreino.add(bundle);
                dataFinal = data;
            }
            countSomarData++;
        }

        //Demais semanas
        for (int i = 2; i <= treino.getQtdSemanas(); i++) {

            for (int j = 1; j <= 7; j++) {

                if (diasSemana.contains(j)) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(dataInicio);
                    c.add(Calendar.DATE, +countSomarData);

                    Date data = c.getTime();

                    Bundle bundle = new Bundle();
                    bundle.putInt("semana", i);
                    bundle.putSerializable("data", data);

                    finalDiasTreino.add(bundle);
                    dataFinal = data;
                }
                countSomarData++;
            }
        }

        treino.setDtFim(dataFinal);

    }

    public boolean validaCampos() {
        boolean valida = true;

        if (editTextNomeTreino.getText().toString().equals("")) {
            editTextNomeTreino.setError(getResources().getString(R.string.preenchacampo));
            valida = false;
        }
        if (editTextDataInicio.getText().toString().equals("")) {
            editTextDataInicio.setError(getResources().getString(R.string.preenchacampo));
            valida = false;
        }

        return valida;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cadastro_treino, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salvar_treino) {

            if (validaCampos()) {

                Boolean cadastra = true;

                Objetivo obj = (Objetivo) spnObj.getSelectedItem();
                treino.setObjetivo(obj.getCodObj());
                treino.setNome(editTextNomeTreino.getText().toString());
                Corredor corredor = ((CadastroTreinoActivity) getActivity()).getCorredor();
                treino.setEmailCorredor(corredor.getEmail());
                treino.setEmailTreinador(corredor.getEmailTreinador());
                treino.setQtdSemanas(Integer.parseInt(textViewQtdSemanas.getText().toString()));
                try {
                    treino.setDtInicio(brDateFormat.parse(editTextDataInicio.getText().toString()));
                    Calendar c = Calendar.getInstance();
                    c.setTime(treino.getDtInicio());
                    dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    configuraCheckSemanas();

                    obtemDatas(treino.getDtInicio());

                    //print para teste
                    for (Bundle auxBunFinal : finalDiasTreino) {
                        DateFormat auxDat = new SimpleDateFormat("yyyy-MM-dd");
                        Log.i("TesteArray", " Semana " + auxBunFinal.getInt("semana", 666) + " data ="
                                + auxDat.format(auxBunFinal.getSerializable("data")));
                    }
                    //fim do teste

                } catch (ParseException e) {
                    e.printStackTrace();
                    cadastra = false;
                }

                if (cadastra) {
                    ConsultaTreinoTesteAtivoTask task = new ConsultaTreinoTesteAtivoTask(getActivity(), treino, finalDiasTreino, this);
                    task.execute();
                }

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

     public void limpaVariaveis(){
         finalDiasTreino.clear();
     }






}
