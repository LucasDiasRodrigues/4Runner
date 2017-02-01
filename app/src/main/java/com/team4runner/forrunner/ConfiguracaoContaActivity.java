package com.team4runner.forrunner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.ConfiguraContaTask;
import com.team4runner.forrunner.tasks.DesativaContaTask;

public class ConfiguracaoContaActivity extends AppCompatActivity {
    private String perfil;
    private Corredor corredor;
    private Treinador treinador;

    EditText txtSenhaAtual;
    EditText txtNovaSenha;
    EditText txtNovaSenha2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_conta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.configConta));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        perfil = prefs.getString("perfil","");

        if(perfil.equals("corredor")){
            corredor = new Corredor();
            corredor.setEmail(prefs.getString("email", ""));
        } else if (perfil.equals("treinador")){
            treinador = new Treinador();
            treinador.setEmail(prefs.getString("email",""));
        }


        Switch switchLocStatus = (Switch)findViewById(R.id.switchLocStatus);
        if (prefs.getString("locStatus","").equals("ativo")){
            switchLocStatus.setChecked(true);
        } else {
            switchLocStatus.setChecked(false);
        }
        switchLocStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(perfil.equals("corredor")){
                    if(isChecked){
                        corredor.setLocStatus("ativo");
                    } else {
                        corredor.setLocStatus("inativo");
                    }
                    ConfiguraContaTask contaTask = new ConfiguraContaTask(ConfiguracaoContaActivity.this, corredor,"alterarLocStatus");
                    contaTask.execute();

                } else if (perfil.equals("treinador")){
                        if(isChecked){
                            treinador.setLocStatus("ativo");
                        } else {
                            treinador.setLocStatus("inativo");
                        }
                    ConfiguraContaTask contaTask = new ConfiguraContaTask(ConfiguracaoContaActivity.this,treinador,"alterarLocStatus");
                    contaTask.execute();
                }

            }
        });

        txtSenhaAtual = (EditText)findViewById(R.id.txtSenhaAtual);
        txtNovaSenha = (EditText)findViewById(R.id.txtNovaSenha);
        txtNovaSenha2 = (EditText)findViewById(R.id.txtNovaSenha2);

        Button btnAlterarSenha = (Button) findViewById(R.id.btnAlterarSenha);
        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //AlterarSenha

                if(verificarSenha()){

                    if(perfil.equals("corredor")){
                        corredor.setSenha(txtSenhaAtual.getText().toString());
                        corredor.setNome(txtNovaSenha2.getText().toString());
                        ConfiguraContaTask contaTask = new ConfiguraContaTask(ConfiguracaoContaActivity.this, corredor,"alterarSenha");
                        contaTask.execute();
                    } else if (perfil.equals("treinador")){
                        treinador.setSenha(txtSenhaAtual.getText().toString());
                        treinador.setNome(txtNovaSenha2.getText().toString());
                        ConfiguraContaTask contaTask = new ConfiguraContaTask(ConfiguracaoContaActivity.this,treinador,"alterarSenha");
                        contaTask.execute();
                    }
                }

            }
        });




        Button btnDesativarConta = (Button)findViewById(R.id.btnDesativarConta);
        btnDesativarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ConfiguracaoContaActivity.this).setTitle(R.string.desativarconta)
                        .setMessage(R.string.desejadesativarconta)
                        .setNegativeButton(R.string.cancelar, null)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //desativar
                                if (perfil.equals("corredor")) {
                                    DesativaContaTask task = new DesativaContaTask(corredor, ConfiguracaoContaActivity.this);
                                    task.execute();
                                } else if (perfil.equals("treinador")) {
                                    DesativaContaTask task = new DesativaContaTask(treinador,ConfiguracaoContaActivity.this);
                                    task.execute();
                                }


                            }
                        }).show();

            }
        });

    }

    public boolean verificarSenha(){

        boolean correto = true;

        if(txtSenhaAtual.getText().toString().equals("")){
            txtSenhaAtual.setError(getString(R.string.preenchacampo));
            correto = false;
        }
        if(txtNovaSenha.getText().toString().equals("")){
            txtNovaSenha.setError(getString(R.string.preenchacampo));
            correto = false;
        }
        if(txtNovaSenha2.getText().toString().equals("")){
            txtNovaSenha2.setError(getString(R.string.preenchacampo));
            correto = false;
        }
        if(!(txtNovaSenha.getText().toString()).equals(txtNovaSenha2.getText().toString())){
            txtNovaSenha2.setError(getString(R.string.senhasdiferentes));
            correto = false;
        }

        return correto;
    }

    public void senhaincorreta(){
        txtSenhaAtual.setFocusable(true);
        txtSenhaAtual.setError(getString(R.string.senhaincorreta));
    }

    public void limparCampos(){
        txtSenhaAtual.setText("");
        txtNovaSenha.setText("");
        txtNovaSenha2.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
