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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.ChatActivity;
import com.team4runner.forrunner.LocalizarTreinadorActivity;
import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.AvaliaTreinadorTask;
import com.team4runner.forrunner.tasks.CadastroTesteDeCampoTask;
import com.team4runner.forrunner.tasks.ConsultarPerfilTreinadorTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.Line;

/**
 * Created by Lucas on 11/06/2015.
 */
public class HomeCorredorTreinadorFragment extends Fragment {

    NestedScrollView layoutTreinador;
    LinearLayout clickAvalia;
    LinearLayout layoutTelefone;
    FrameLayout frameLayout;
    private String emailTreinador;
    private String emailCorredor;

    private Treinador treinador;
    private int ultimoVotoCorredor;
    private Date dataUltimoVotoCorredor;

    TextView txtNome;
    TextView txtSexo;
    TextView txtIdade;

    TextView txtEmail;
    TextView txtTelefone;

    TextView txtSobreMim;
    TextView txtCref;
    TextView txtFormacao;
    RatingBar ratingBar;
    TextView txtQtdVotos;


    private Corredor corredor;
    private Toolbar toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        corredor = new Corredor();

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("emailTreinador", "");
        emailCorredor = prefs.getString("email", "");



        corredor.setEmail(emailCorredor);
        corredor.setEmailTreinador(emailTreinador);

        //possuiTreinador();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_meutreinador_home_corredor, container, false);


        //Caso nao possua treinador
        layoutTreinador = (NestedScrollView) fragment.findViewById(R.id.layoutTreinador);
        frameLayout = (FrameLayout) fragment.findViewById(R.id.frameLayout);
        Button btnProcurarTreinador = (Button) fragment.findViewById(R.id.btnProcurarTreinador);
        btnProcurarTreinador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LocalizarTreinadorActivity.class);
                getActivity().startActivity(intent);

            }
        });


        //Caso possua treinador

        layoutTelefone = (LinearLayout) fragment.findViewById(R.id.layoutTelefone);

        txtNome = (TextView) fragment.findViewById(R.id.txtNome);
        txtSexo = (TextView) fragment.findViewById(R.id.txtSexo);
        txtIdade = (TextView) fragment.findViewById(R.id.txtIdade);
        txtEmail = (TextView) fragment.findViewById(R.id.txtEmail);
        txtTelefone = (TextView) fragment.findViewById(R.id.txtTelefone);

        txtSobreMim = (TextView) fragment.findViewById(R.id.txtSobreMim);
        txtCref = (TextView) fragment.findViewById(R.id.txtCref);
        txtFormacao = (TextView) fragment.findViewById(R.id.txtFormacao);
        txtQtdVotos = (TextView) fragment.findViewById(R.id.txtQtdVotos);
        toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_desassociar);


        //RatingBar
        ratingBar = (RatingBar) fragment.findViewById(R.id.ratingTreinador);

        clickAvalia = (LinearLayout) fragment.findViewById(R.id.clickAvalia);
        clickAvalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click", "clickrating");

                DialogFragment newFragment = MyDialogFragment.newInstance(ultimoVotoCorredor, emailCorredor, emailTreinador, HomeCorredorTreinadorFragment.this);
                newFragment.show(getFragmentManager(), "dialog");
            }
        });


        //Chat
        FloatingActionButton fabChat = (FloatingActionButton) fragment.findViewById(R.id.fabChat);
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("origem", "corredor");
                intent.putExtra("treinador", treinador);
                startActivity(intent);

            }
        });

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("emailTreinador", "");
        emailCorredor = prefs.getString("email", "");
        corredor.setEmail(emailCorredor);
        corredor.setEmailTreinador(emailTreinador);

        possuiTreinador(false);

        ((MainCorredorActivity) getActivity()).vinculafragmentTreinador(this);

    }


    public boolean possuiTreinador(boolean buscaFinalizada) {
        boolean possuiTreinador;
        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("emailTreinador", "");
        Log.i("emailtreinador ===++ ", emailTreinador);
        if (!emailTreinador.equals("") && !emailTreinador.equals("null")) {
            possuiTreinador = true;
            atualizaTreinador();
            layoutTreinador.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

        } else {
            possuiTreinador = false;
            layoutTreinador.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }
        return possuiTreinador;
    }


    public void atualizaTreinador() {

        ConsultarPerfilTreinadorTask task = new ConsultarPerfilTreinadorTask(getActivity(), emailTreinador, emailCorredor, this);
        task.execute();
    }

    public void atualizaInterfaceTreinador(Bundle bundle) {

        treinador = (Treinador) bundle.getSerializable("treinador");
        ultimoVotoCorredor = bundle.getInt("ultimoVotoCorredor");
        dataUltimoVotoCorredor = (Date) bundle.getSerializable("dataUltimoVotoCorredor");

        txtNome.setText(treinador.getNome());
        txtEmail.setText(treinador.getEmail());

        if (!String.valueOf(treinador.getTelefone()).equals("")) {
            txtTelefone.setText(String.valueOf(treinador.getTelefone()) + "");
        } else {
            layoutTelefone.setVisibility(View.GONE);
        }

        txtSexo.setText(treinador.getSexo());

        int auxInt = getIdade(treinador.getDataNasc());
        txtIdade.setText(String.valueOf(auxInt));


        txtSobreMim.setText(treinador.getSobreMim());
        txtCref.setText(treinador.getCref());
        txtFormacao.setText(treinador.getFormacao());
        txtQtdVotos.setText(String.valueOf(treinador.getQtdAvaliacoes()) + " " + getActivity().getResources().getString(R.string.avaliacoes));

        ImageView imagemTreinador = (ImageView) getActivity().findViewById(R.id.imageTreinador);

        if (!treinador.getImagemPerfil().equals("")) {
            Picasso.with(getActivity()).load(getActivity().getResources().getString(R.string.imageserver) + treinador.getImagemPerfil()).into(imagemTreinador);
        }
        float aux = (float) treinador.getMediaAvaliacao() / 2;
        ratingBar.setRating((float) aux);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.desassociar:
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.desassociarTreinador)
                                .setMessage(R.string.desejadesassociartreinador)
                                .setNegativeButton(R.string.cancelar, null)
                                .setPositiveButton(R.string.simcerteza, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //desvincular-se
                                        corredor.desassociarDeTreinador(getActivity());
                                    }
                                }).show();
                        break;
                }
                return false;
            }
        });


    }


    public int getIdade(Date dataNasc) {
        // Data de hoje.
        GregorianCalendar agora = new GregorianCalendar();
        int ano = 0, mes = 0, dia = 0;
        // Data do nascimento.
        GregorianCalendar nascimento = new GregorianCalendar();
        int anoNasc = 0, mesNasc = 0, diaNasc = 0;
        // Idade.
        int idade = 0;


        nascimento.setTime(dataNasc);
        ano = agora.get(Calendar.YEAR);
        mes = agora.get(Calendar.MONTH) + 1;
        dia = agora.get(Calendar.DAY_OF_MONTH);
        anoNasc = nascimento.get(Calendar.YEAR);
        mesNasc = nascimento.get(Calendar.MONTH) + 1;
        diaNasc = nascimento.get(Calendar.DAY_OF_MONTH);
        idade = ano - anoNasc;
        // Calculando diferencas de mes e dia.
        if (mes < mesNasc) {
            idade--;
        } else {
            if (dia < diaNasc) {
                idade--;
            }
        }
        // Ultimo teste, idade "negativa".
        if (idade < 0) {
            idade = 0;
        }
        Log.i("idade", idade + "  lalala");

        return (idade);
    }


    //Fragment

    public static class MyDialogFragment extends DialogFragment {

        int ultimoVoto;
        int voto;
        RatingBar ratingTreinador;
        Button btnAvaliar;
        Button btnCancelar;
        String emailAluno;
        String emailTreinador;

        public static MyDialogFragment newInstance(int ultimoVoto, String emailAluno, String emailTreinador, HomeCorredorTreinadorFragment fragment) {
            MyDialogFragment f = new MyDialogFragment();

            Bundle args = new Bundle();
            args.putInt("ultimoVoto", ultimoVoto);
            args.putString("emailAluno", emailAluno);
            args.putString("emailTreinador", emailTreinador);
            f.setArguments(args);
            f.setTargetFragment(fragment, 0);

            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog_avalia_treinador, container, false);


            Bundle args = getArguments();
            ultimoVoto = args.getInt("ultimoVoto");
            emailAluno = args.getString("emailAluno");
            emailTreinador = args.getString("emailTreinador");


            btnAvaliar = (Button) v.findViewById(R.id.btnAvaliar);
            btnAvaliar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    float auxRating = ratingTreinador.getRating() * 2;
                    int auxRating2 = (int) auxRating;
                    AvaliaTreinadorTask task = new AvaliaTreinadorTask(getActivity(), MyDialogFragment.this, emailAluno, emailTreinador, auxRating2, (HomeCorredorTreinadorFragment) getTargetFragment());
                    task.execute();

                }
            });
            btnCancelar = (Button) v.findViewById(R.id.btnCancelar);
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


            ratingTreinador = (RatingBar) v.findViewById(R.id.ratingTreinador);

            if (ultimoVoto > 1) {
                float auxUltimoVoto = ultimoVoto / 2;
                ratingTreinador.setRating(auxUltimoVoto);
            }


            //  getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //getDialog().setTitle(R.string.cadastrateste);

            return v;
        }

    }

}
