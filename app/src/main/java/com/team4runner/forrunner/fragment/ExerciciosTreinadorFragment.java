package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroExercicioActivity;
import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.adapter.ListViewAdapterExerciciosTreinador;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.tasks.CadastroTesteDeCampoTask;
import com.team4runner.forrunner.tasks.ListaExerciciosTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 29/06/2015.
 */
public class ExerciciosTreinadorFragment extends Fragment {


    private FloatingActionButton fabCadastrarExercicio;

    private ListView mListView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListViewAdapterExerciciosTreinador mAdapter;
    List<Exercicio> mList = new ArrayList<>();

    private ProgressBar progress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_exercicios_treinador, container, false);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        progress = (ProgressBar) getView().findViewById(R.id.progress);

        mListView = (ListView) getView().findViewById(R.id.exercicios_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("clicou", "clicou");

            }
        });
        //FAB
        fabCadastrarExercicio = (FloatingActionButton) getView().findViewById(R.id.fabCadastrarExercicio);
        fabCadastrarExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = MyDialogFragment.newInstance(ExerciciosTreinadorFragment.this);
                newFragment.show(getFragmentManager(), "dialog");


                //Intent intent = new Intent(getActivity(), CadastroExercicioActivity.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        listaExercicios();
        Log.i("onResume", "paasou");
    }

    public void atualizaLista(List<Exercicio> exercicios) {
        if (exercicios.size() > 0) {
            mList = exercicios;
            mAdapter = new ListViewAdapterExerciciosTreinador(getActivity(), mList, this);
            mListView.setAdapter(mAdapter);
            progress.setVisibility(View.GONE);
        }
    }

    public void listaExercicios() {
        progress.setVisibility(View.VISIBLE);
        ListaExerciciosTask task = (ListaExerciciosTask) new ListaExerciciosTask(getActivity(), this, ((MainTreinadorActivity) getActivity()).treinador).execute();
    }


    public static class MyDialogFragment extends DialogFragment {

        static boolean editarExercicio = false;

        public static MyDialogFragment newInstance(Exercicio exercicio) {
            MyDialogFragment f = new MyDialogFragment();


            Bundle args = new Bundle();
            args.putSerializable("exercicio", exercicio);
            f.setArguments(args);
            editarExercicio = true;

            Bundle bundle = new Bundle();

            return f;
        }

        static MyDialogFragment newInstance(ExerciciosTreinadorFragment fragment) {
            MyDialogFragment f = new MyDialogFragment();
            editarExercicio = false;
            f.setTargetFragment(fragment,0);
            return f;
        }

        String emailTreinador;

        Exercicio exercicio;

        EditText nomeExerc;
        EditText descExerc;

        Button btnCadastrar;
        Button btnCancelar;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog_cadastra_exercicio, container, false);

            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            emailTreinador = prefs.getString("email", "");

            exercicio = new Exercicio();

            nomeExerc = (EditText) v.findViewById(R.id.editTextNomeExercicio);
            descExerc = (EditText) v.findViewById(R.id.editTextDescricaoExercicio);


            //Se for editar um teste existente
            if (editarExercicio) {
                Bundle args = getArguments();
                exercicio = (Exercicio) args.getSerializable("exercicio");
                preencheCamposEdicao();
            }


            btnCancelar = (Button) v.findViewById(R.id.btnCancelar);
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnCadastrar = (Button) v.findViewById(R.id.btnCadastrar);
            btnCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Cadastra

                    if (validaCampos()) {

                        new android.app.AlertDialog.Builder(getActivity())
                                //.setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(getActivity().getResources().getString(R.string.cadastrodeexercicios))
                                .setMessage(getActivity().getResources().getString(R.string.confirma_cadastro_exercicio))
                                .setPositiveButton(getActivity().getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

                                        exercicio.setNomeExercicio(nomeExerc.getText().toString());
                                        exercicio.setDescricaoExercicio(descExerc.getText().toString());
                                        exercicio.setDataCadastro(Calendar.getInstance().getTime());
                                        exercicio.setEmailTreinador(prefs.getString("email", ""));
                                        exercicio.setSituacao("1");

                                        if (editarExercicio) {
                                            boolean dismiss = exercicio.editarExercicio(getActivity());
                                            if (dismiss) {
                                                dismiss();
                                            }
                                        } else {
                                            boolean dismiss = exercicio.cadastrarExercicio(getActivity(), (ExerciciosTreinadorFragment) getTargetFragment());
                                            if (dismiss) {
                                                dismiss();
                                            }
                                        }
                                    }
                                }).setNegativeButton(getActivity().getResources().getString(R.string.nao), null).show();

                    }


                }
            });

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //getDialog().setTitle(R.string.cadastrateste);

            return v;
        }


        public boolean validaCampos() {
            boolean validado = true;

            if (nomeExerc.getText().toString().equals("")) {
                nomeExerc.setError(getResources().getString(R.string.preenchacampo));
                validado = false;
            }

            if (descExerc.getText().toString().equals("")) {
                descExerc.setError(getResources().getString(R.string.preenchacampo));
                validado = false;
            }

            return validado;
        }

        public void preencheCamposEdicao() {
            nomeExerc.setText(exercicio.getNomeExercicio());
            descExerc.setText(exercicio.getDescricaoExercicio());
        }

    }


}
