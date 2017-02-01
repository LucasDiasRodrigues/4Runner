package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.team4runner.forrunner.CadastroTesteActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.adapter.ListViewListaTestesTreinadorAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.tasks.CadastroTesteDeCampoTask;
import com.team4runner.forrunner.tasks.ListaTestesDeCampoTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 06/01/2016.
 */
public class MeusCorredoresTreinadorTestesFragment extends Fragment {

    FloatingActionButton fabCadastrarTeste;
    ListView mListView;
    Corredor corredor;
    String emailTreinador;
    TextView txtSemTestes;
    List<TesteDeCampo> mList1;
    private ProgressBar progress;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MeuCorredorTreinadorActivity) getActivity()).setFragmentTestes(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_meucorredor_treinador_testes, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("email", "");


        fabCadastrarTeste = (FloatingActionButton) fragment.findViewById(R.id.fabCadastrarTeste);
        fabCadastrarTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = MyDialogFragment.newInstance(MeusCorredoresTreinadorTestesFragment.this);
                newFragment.show(getFragmentManager(), "dialog");

            }
        });

        progress = (ProgressBar) fragment.findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                ListaTestes();
            }
        });


        txtSemTestes = (TextView) fragment.findViewById(R.id.txtSemTestes);

        mListView = (ListView) fragment.findViewById(R.id.list_view_testes);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //clicar no teste

            }
        });

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ListaTestes();
    }

    public void ListaTestes(){
        corredor = ((MeuCorredorTreinadorActivity) getActivity()).getCorredor();
        ListaTestesDeCampoTask task = new ListaTestesDeCampoTask(getActivity(), corredor, emailTreinador, false, this, "treinador");
        task.execute();
    }

    public void AtualizaListaTeste(List<TesteDeCampo> testes) {
        mList1 = testes;
        mListView.setAdapter(new ListViewListaTestesTreinadorAdapter(getActivity(), mList1, corredor, this));

        if (mList1.size() < 1) {
            txtSemTestes.setVisibility(View.VISIBLE);
        } else {
            txtSemTestes.setVisibility(View.GONE);
        }

        progress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);


    }




    public static class MyDialogFragment extends DialogFragment {

        static boolean editarTeste = false;

        public static MyDialogFragment newInstance(TesteDeCampo testeDeCampo, MeusCorredoresTreinadorTestesFragment fragment) {
            MyDialogFragment f = new MyDialogFragment();


            Bundle args = new Bundle();
            args.putSerializable("teste", testeDeCampo);
            f.setArguments(args);
            f.setTargetFragment(fragment, 0);
            editarTeste = true;

            return f;
        }

        static MyDialogFragment newInstance(MeusCorredoresTreinadorTestesFragment fragment) {
            MyDialogFragment f = new MyDialogFragment();
            editarTeste = false;
            f.setTargetFragment(fragment, 0);
            return f;
        }

        Corredor corredor;
        String emailTreinador;

        TesteDeCampo testeDeCampo;

        EditText txtVolume;
        EditText txtDataTeste;
        EditText txtObs;
        RadioGroup rgTipoVolume;
        String tipoVolume;
        RadioButton rbMetros;
        RadioButton rbMinutos;

        Button btnCadastrar;
        Button btnCancelar;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog_cadastra_teste, container, false);

            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            emailTreinador = prefs.getString("email", "");

            testeDeCampo = new TesteDeCampo();


            corredor = ((MeuCorredorTreinadorActivity) getActivity()).getCorredor();


            txtVolume = (EditText) v.findViewById(R.id.txtVolume);
            txtDataTeste = (EditText) v.findViewById(R.id.txtDataTeste);
            txtDataTeste.addTextChangedListener(Mask.insert("##/##/####", txtDataTeste));
            txtObs = (EditText) v.findViewById(R.id.txtObs);

            rgTipoVolume = (RadioGroup) v.findViewById(R.id.radiogrouptpVolume);

            rbMetros = (RadioButton) v.findViewById(R.id.rbMetros);
            rbMinutos = (RadioButton) v.findViewById(R.id.rbMinutos);


            //Se for editar um teste existente
            if (editarTeste) {
                Bundle args = getArguments();
                testeDeCampo = (TesteDeCampo) args.getSerializable("teste");
                preencheCamposEdicao();
            }


            txtVolume = (EditText) v.findViewById(R.id.txtVolume);
            txtDataTeste = (EditText) v.findViewById(R.id.txtDataTeste);
            txtObs = (EditText) v.findViewById(R.id.txtObs);

            rgTipoVolume = (RadioGroup) v.findViewById(R.id.radiogrouptpVolume);

            rbMetros = (RadioButton) v.findViewById(R.id.rbMetros);
            rbMinutos = (RadioButton) v.findViewById(R.id.rbMinutos);


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

                        testeDeCampo.setVolume(Double.valueOf(txtVolume.getText().toString()));
                        //tipo volume
                        switch (rgTipoVolume.getCheckedRadioButtonId()) {
                            case R.id.rbMetros:
                                tipoVolume = "metros";
                                break;
                            case R.id.rbMinutos:
                                tipoVolume = "minutos";
                                break;
                        }
                        testeDeCampo.setTipoVolume(tipoVolume);
                        //dataTeste
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date auxDate = new Date();
                        try {
                            auxDate = dateFormat.parse(txtDataTeste.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        testeDeCampo.setDataTeste(auxDate);
                        testeDeCampo.setObs(txtObs.getText().toString());
                        testeDeCampo.setEmailALuno(corredor.getEmail());
                        testeDeCampo.setEmailTreinador(emailTreinador);


                        if (editarTeste) {
                            CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(getActivity(), testeDeCampo, "editar", MyDialogFragment.this, (MeusCorredoresTreinadorTestesFragment) getTargetFragment());
                            task.execute();
                        } else {
                            CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(getActivity(), testeDeCampo, "cadastrar", MyDialogFragment.this, (MeusCorredoresTreinadorTestesFragment) getTargetFragment());
                            task.execute();
                        }


                    }

                }
            });

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //getDialog().setTitle(R.string.cadastrateste);

            return v;
        }


        public boolean validaCampos() {
            boolean validacaoOk = true;

            if (txtVolume.getText().toString().equals("")) {
                txtVolume.setError("Preencha este campo!");
                validacaoOk = false;
            }
            //Campo Data
            if (txtDataTeste.getText().toString().equals("")) {
                txtDataTeste.setError("Preencha este campo!");
                validacaoOk = false;
            }
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date auxDate = new Date();
            //teste
            Date auxDataAgora = new Date();
            String dataAgoraString = dateFormat.format(auxDataAgora);
            try {
                auxDate = dateFormat.parse(txtDataTeste.getText().toString());
                auxDataAgora = dateFormat.parse(dataAgoraString);

                if (auxDate.before(auxDataAgora)) {
                    validacaoOk = false;
                    txtDataTeste.setError("Data invalida!");
                }

            } catch (ParseException e) {
                e.printStackTrace();
                txtDataTeste.setError("Data invalida!");
                validacaoOk = false;
            }

            if (txtObs.getText().toString().equals("")) {
                txtObs.setError("Preencha este campo!");
                validacaoOk = false;
            }

            if (rgTipoVolume.getCheckedRadioButtonId() == 0) {
                rgTipoVolume.setFocusable(true);
                validacaoOk = false;
            }
            return validacaoOk;
        }

        public void preencheCamposEdicao() {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            txtVolume.setText(String.valueOf(testeDeCampo.getVolume()));
            txtDataTeste.setText(dateFormat.format(testeDeCampo.getDataTeste()));
            txtObs.setText(testeDeCampo.getObs());

            if (testeDeCampo.getTipoVolume().equals("metros")) {
                rbMetros.setChecked(true);
            } else if (testeDeCampo.getTipoVolume().equals("minutos")) {
                rbMinutos.setChecked(true);
            }

        }


        //   public void substituirTesteExistente(){

        //     CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(getActivity(), testeDeCampo, "substituir");
        //   task.execute();

        //}

    }


}
