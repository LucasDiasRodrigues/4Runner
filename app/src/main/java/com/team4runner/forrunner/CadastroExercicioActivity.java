package com.team4runner.forrunner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.tasks.CadastroExercicioTask;

import java.util.Calendar;

public class CadastroExercicioActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText nomeExercicio;
    private EditText descricaoExercicio;

    private boolean editar;
    private Exercicio exercicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_exercicio);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cadastrodeexercicios);
        setSupportActionBar(toolbar);

        exercicio = new Exercicio();

        nomeExercicio = (EditText) findViewById(R.id.editTextNomeExercicio);
        descricaoExercicio = (EditText) findViewById(R.id.editTextDescricaoExercicio);

        Intent intent = getIntent();
        editar = intent.getBooleanExtra("editar", false);

        if (editar) {
            exercicio = (Exercicio) intent.getSerializableExtra("exercicio");
            preencherCampos();
        }


    }


    public void preencherCampos() {

        nomeExercicio.setText(exercicio.getNomeExercicio());
        descricaoExercicio.setText(exercicio.getDescricaoExercicio());

    }

    public boolean validaCampos() {
        boolean validado = true;

        if (nomeExercicio.getText().toString().equals("")) {
            nomeExercicio.setError(getResources().getString(R.string.preenchacampo));
            validado = false;
        }

        if (descricaoExercicio.getText().toString().equals("")) {
            descricaoExercicio.setError(getResources().getString(R.string.preenchacampo));
            validado = false;
        }

        return validado;
    }
}
