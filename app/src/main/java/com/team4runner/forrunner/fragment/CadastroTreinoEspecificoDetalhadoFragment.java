package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.Container;
import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Lucas on 15/07/2015.
 * Fragment com o conteudo da aba semanal do cadastro do treino
 * Para cada semana, uma instancia diferente dessa fragment ia� criada
 */
public class CadastroTreinoEspecificoDetalhadoFragment extends Fragment {

    static boolean Ok = true;

    //Informa��es obtidas de primeira fragment
    private Treino treino;
    private List<Bundle> bundles;
    private List<Exercicio> exercicios;
    private String[] exerciciosString;

    //Variavel q indica a qual semana essa fragment sera associada
    Integer semana;

    LayoutInflater layoutInflater;
    LinearLayout layoutPrincipal;
    DateFormat brDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat brDateFormatExtenso = new SimpleDateFormat("dd/MM/yyyy, EEEE");

    List<Date> listDatas = new ArrayList<>();
    List<View> listViews = new ArrayList<View>();
    Integer count = 0;


    //Final
    private List<TreinoExercicio> treinoExercicios = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_treino_especifico_detalhado, container, false);
        setHasOptionsMenu(true);

        //Recebe a semana que sera referenciada
        semana = getArguments().getInt("semana", 1);


        //Variaveis da Activity
        bundles = ((CadastroTreinoActivity) getActivity()).getBundlesDiasDeTreino();
        treino = ((CadastroTreinoActivity) getActivity()).getTreino();
        exercicios = ((CadastroTreinoActivity) getActivity()).getExercicios();
        exerciciosString = new String[exercicios.size()];
        for (int i = 0; i < exercicios.size(); i++) {
            exerciciosString[i] = exercicios.get(i).getNomeExercicio();
        }

        layoutInflater = LayoutInflater.from(getActivity());
        layoutPrincipal = (LinearLayout) fragment.findViewById(R.id.layoutPrincipal);

        criaCardsDias();

        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        ((CadastroTreinoActivity) getActivity()).setFragment(this);
    }

    public void criaCardsDias() {

        Log.i("criaCardDias","criando cards");

        for (Bundle auxBun : bundles) {

            //Teste
            DateFormat auxDat = new SimpleDateFormat("yyyy-MM-dd");
            Log.i("TesteArray2", " Semana " + auxBun.getInt("semana", 666) + " data ="
                    + auxDat.format(auxBun.getSerializable("data")));
            //Fim Teste

            if (auxBun.get("semana") == semana) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date) auxBun.get("data"));

                String textoCard = brDateFormatExtenso.format(calendar.getTime());

                final View dia = layoutInflater.inflate(R.layout.item_dia_cadastro_treino, layoutPrincipal, false);
                String tag = brDateFormat.format(calendar.getTime());
                dia.setTag(tag);
                TextView textViewDia = (TextView) dia.findViewById(R.id.textViewDia);
                textViewDia.setText(textoCard);
                Button novoExercicio = (Button) dia.findViewById(R.id.novoExercicio);
                layoutPrincipal.addView(dia);
                novoExercicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addExercicio(dia, false);
                    }
                });
                Button novoIntervalo = (Button) dia.findViewById(R.id.novoIntervalo);
                novoIntervalo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addIntervalo(dia);
                    }
                });

                addExercicio(dia, true);

                //Preenche a lista das datas desta semana

                Date auxDate = (Date) auxBun.get("data");
                listDatas.add(auxDate);

            }

        }

    }


    public void addExercicio(final View dia, boolean first) {

        final View exercicio = layoutInflater.inflate(R.layout.item_exercicio_cadastro_treino, (LinearLayout) dia.findViewById(R.id.layoutDia), false);
        String tag = "exercicio_" + dia.getTag() + "_" + count.toString();
        exercicio.setTag(tag);
        final EditText txtNomeExercicio = (EditText) exercicio.findViewById(R.id.editTextNomeExercicio);
        final LinearLayout clickExercicio = (LinearLayout) exercicio.findViewById(R.id.clickExercicio);
        clickExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.escolhaoexercicio)
                        .setItems(exerciciosString, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                txtNomeExercicio.setText(exercicios.get(which).getNomeExercicio());
                                txtNomeExercicio.setTag(exercicios.get(which).getCodExercicio());
                            }
                        }).show();
            }
        });

        final EditText txtRitmo = (EditText) exercicio.findViewById(R.id.editTextRitmo);
        txtRitmo.addTextChangedListener(Mask.insert("##:##", txtRitmo));

        final EditText txtRepeticoes = (EditText) exercicio.findViewById(R.id.editTextRepeticoes);
        txtRepeticoes.setText("1");

        final EditText txtIntervalo = (EditText) exercicio.findViewById(R.id.editTextIntervaloRep);
        txtIntervalo.setText("0");

        //Excluir exercicio
        if (!first) {
            ImageView btnExcluir = (ImageView) exercicio.findViewById(R.id.btnExcluir);
            btnExcluir.setVisibility(View.VISIBLE);
            btnExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listViews.remove(exercicio);
                    Log.i("Removido", "removida da lista de Views");
                    ((LinearLayout) dia.findViewById(R.id.layoutDia)).removeView(exercicio);
                }
            });
        }

        ((LinearLayout) dia.findViewById(R.id.layoutDia)).addView(exercicio);

        listViews.add(exercicio);

        count++;

        //Tornar Botao intervalo visivel caso não esteja
       // Button btnIntervalo = (Button)dia.findViewById(R.id.novoIntervalo);
       // btnIntervalo.setVisibility(View.VISIBLE);

    }

    public void addIntervalo(final View dia) {

        final View intervalo = layoutInflater.inflate(R.layout.item_intervalo_cadastro_treino, (LinearLayout) dia.findViewById(R.id.layoutDia), false);
        String tag = "intervalo_" + dia.getTag() + "_" + count.toString();
        intervalo.setTag(tag);

        //Excluir intervalo
        ImageView btnExcluir = (ImageView) intervalo.findViewById(R.id.btnExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViews.remove(intervalo);
                Log.i("Removido", "removida da lista de Views");
                ((LinearLayout) dia.findViewById(R.id.layoutDia)).removeView(intervalo);
            }
        });

        ((LinearLayout) dia.findViewById(R.id.layoutDia)).addView(intervalo);

        listViews.add(intervalo);

        count++;

        //Tornar Botao intervalo invisivel caso não esteja
      //  Button btnIntervalo = (Button)dia.findViewById(R.id.novoIntervalo);
       // btnIntervalo.setVisibility(View.VISIBLE);

    }

    //teste
    public void testeLog() {

        Log.i("testedeLog", "semana= " + semana + " Pelo");

    }

    public void finalizarCadastro() {

        EditText txtNomeExercicio;
        EditText txtVolume;
        EditText txtRitMedio;
        EditText txtRepeticoes;
        EditText txtIntervaloEntreRepeticoes;
        EditText txtTempoPosIntervalo;

        for (Date data : listDatas) {
            int countOrdem = 1;

            List<View> auxViewsData = new ArrayList<View>();

            for (int i = 0; i < listViews.size(); i++) {
                String tag = listViews.get(i).getTag().toString();
                //Pegar data
                String aux[] = tag.split("_");
                Date auxData = new Date(1);
                try {
                    auxData = brDateFormat.parse(aux[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (auxData.compareTo(data) == 0) {

                    //Array com as view de um unico dia
                    auxViewsData.add(listViews.get(i));

                }
            }

            for (int i = 0; i < auxViewsData.size(); i++) {
                String tag = auxViewsData.get(i).getTag().toString();
                //Pegar data
                String aux[] = tag.split("_");

                if (tag.contains("exercicio")) {

                    txtNomeExercicio = (EditText) auxViewsData.get(i).findViewById(R.id.editTextNomeExercicio);
                    txtVolume = (EditText) auxViewsData.get(i).findViewById(R.id.editTextVolume);
                    txtRitMedio = (EditText) auxViewsData.get(i).findViewById(R.id.editTextRitmo);
                    txtRepeticoes = (EditText) auxViewsData.get(i).findViewById(R.id.editTextRepeticoes);
                    txtIntervaloEntreRepeticoes = (EditText) auxViewsData.get(i).findViewById(R.id.editTextIntervaloRep);

                    if (validaCampos(txtNomeExercicio, txtVolume, txtRitMedio, txtRepeticoes, txtIntervaloEntreRepeticoes)) {

                        TreinoExercicio treinoExercicio = new TreinoExercicio();
                        Exercicio exercicio = new Exercicio();
                        exercicio.setNomeExercicio(txtNomeExercicio.getText().toString());
                        exercicio.setCodExercicio((int) txtNomeExercicio.getTag());
                        treinoExercicio.setExercicio(exercicio);
                        treinoExercicio.setVolume(Float.parseFloat(txtVolume.getText().toString()));
                        String auxRitmo = txtRitMedio.getText().toString().replace(":", ".");
                        treinoExercicio.setRitmo(Float.parseFloat(auxRitmo));
                        treinoExercicio.setQtdRepeticoes(Integer.parseInt(txtRepeticoes.getText().toString()));
                        treinoExercicio.setIntervaloRepeticoes(Float.parseFloat(txtIntervaloEntreRepeticoes.getText().toString()));
                        treinoExercicio.setSemana(semana);
                        treinoExercicio.setData(data);
                        treinoExercicio.setOrdem(countOrdem);


                        if ((i + 1) < auxViewsData.size()) {
                            View auxView = auxViewsData.get(i + 1);
                            if (((String) auxView.getTag()).contains("intervalo") && ((String) auxView.getTag()).contains(aux[1])) {
                                txtTempoPosIntervalo = (EditText) auxView.findViewById(R.id.editTextTempo);
                                if (validaCampoIntervalo(txtTempoPosIntervalo)) {
                                    treinoExercicio.setPosIntervalo(Integer.parseInt(txtTempoPosIntervalo.getText().toString()));
                                } else {
                                    Ok = false;
                                }
                            }
                        }


                        treinoExercicios.add(treinoExercicio);


                        countOrdem++;

                    } else {
                        Ok = false;
                    }
                }


            }
            auxViewsData.clear();
        }
        //Mandar para a activity
        ((CadastroTreinoActivity) getActivity()).addArrayTreinoExerciciosFinal(treinoExercicios, Ok);
        Ok = true;
        treinoExercicios.clear();
    }

    public boolean validaCampos(EditText txtNomeExercicio,
                                EditText txtVolume,
                                EditText txtRitMedio,
                                EditText txtRepeticoes,
                                EditText txtIntervaloEntreRepeticoes) {
        boolean valida = true;

        if (txtNomeExercicio.getText().toString().equals("")) {
            txtNomeExercicio.setError(getResources().getString(R.string.preenchacampo));
            txtNomeExercicio.setFocusable(true);
            valida = false;
        }
        if (txtVolume.getText().toString().equals("")) {
            txtVolume.setError(getResources().getString(R.string.preenchacampo));
            txtVolume.setFocusable(true);
            valida = false;
        }
        if (txtRitMedio.getText().toString().equals("")) {
            txtRitMedio.setError(getResources().getString(R.string.preenchacampo));
            txtRitMedio.setFocusable(true);
            valida = false;
        } else {
            String[] auxRitmo = txtRitMedio.getText().toString().split(":");
            Integer auxint = Integer.parseInt(auxRitmo[1]);
            if (auxint > 59) {
                txtRitMedio.setError(getResources().getString(R.string.campopreenchidoincorretamente));
                txtRitMedio.setFocusable(true);
                valida = false;
            }
        }
        if (txtRepeticoes.getText().toString().equals("")) {
            txtRepeticoes.setError(getResources().getString(R.string.preenchacampo));
            txtRepeticoes.setFocusable(true);
            valida = false;
        }
        if (txtIntervaloEntreRepeticoes.getText().toString().equals("")) {
            txtIntervaloEntreRepeticoes.setError(getResources().getString(R.string.preenchacampo));
            txtIntervaloEntreRepeticoes.setFocusable(true);
            valida = false;
        }


        return valida;

    }

    public boolean validaCampoIntervalo(EditText txtTempoPosIntervalo) {
        boolean valida = true;

        if (txtTempoPosIntervalo.getText().toString().equals("")) {
            txtTempoPosIntervalo.setError(getResources().getString(R.string.preenchacampo));
            txtTempoPosIntervalo.setFocusable(true);
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

            new AlertDialog.Builder(getActivity()).setTitle(R.string.salvarTreino)
                    .setMessage(R.string.salvarTreinomsg)
                    .setPositiveButton(R.string.cadastrar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Codigo aqui
                            ((CadastroTreinoActivity) getActivity()).finalizaCadastroDetalhado();
                        }
                    }).setNegativeButton(R.string.cancelar, null)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listViews.clear();
        treinoExercicios.clear();
        Ok = true;
        Log.i("onDestroiView","ApagandoVariaveis");
    }
}
