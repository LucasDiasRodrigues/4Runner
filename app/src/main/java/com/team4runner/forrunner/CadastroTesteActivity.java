package com.team4runner.forrunner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.tasks.CadastroTesteDeCampoTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastroTesteActivity extends AppCompatActivity {

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

    boolean editarTeste = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_teste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("email", "");

        testeDeCampo = new TesteDeCampo();

        Intent intent = getIntent();
        corredor = (Corredor) intent.getSerializableExtra("corredor");
        editarTeste = intent.getBooleanExtra("editar", false);

        txtVolume = (EditText) findViewById(R.id.txtVolume);
        txtDataTeste = (EditText) findViewById(R.id.txtDataTeste);
        txtDataTeste.addTextChangedListener(Mask.insert("##/##/####",txtDataTeste));
        txtObs = (EditText) findViewById(R.id.txtObs);

        rgTipoVolume = (RadioGroup) findViewById(R.id.radiogrouptpVolume);

        rbMetros = (RadioButton) findViewById(R.id.rbMetros);
        rbMinutos = (RadioButton) findViewById(R.id.rbMinutos);


        //Se for editar um teste existente
        if (editarTeste) {
            testeDeCampo = (TesteDeCampo) intent.getSerializableExtra("testeDeCampo");
            preencheCamposEdicao();
        }


        txtVolume = (EditText) findViewById(R.id.txtVolume);
        txtDataTeste = (EditText) findViewById(R.id.txtDataTeste);
        txtObs = (EditText) findViewById(R.id.txtObs);

        rgTipoVolume = (RadioGroup) findViewById(R.id.radiogrouptpVolume);

        rbMetros = (RadioButton) findViewById(R.id.rbMetros);
        rbMinutos = (RadioButton) findViewById(R.id.rbMinutos);


        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
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


                    if(editarTeste){
                   //     CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(CadastroTesteActivity.this, testeDeCampo, "editar");
                     //   task.execute();
                    }
                    else{
                       // CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(CadastroTesteActivity.this, testeDeCampo, "cadastrar");
                        //task.execute();
                    }



                }

            }
        });

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

            if (auxDate.before(auxDataAgora)){
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


    public void substituirTesteExistente(){

       // CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(CadastroTesteActivity.this, testeDeCampo, "substituir");
      //  task.execute();

    }



}
