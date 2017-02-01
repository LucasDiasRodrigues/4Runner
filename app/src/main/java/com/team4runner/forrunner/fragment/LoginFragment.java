package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.GCM.RegistrationIntentService;
import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.tasks.LoginTask;
import com.team4runner.forrunner.web.LoginJson;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lucas on 30/05/2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText etEmail;
    private EditText etSenha;
    private Button btnEntrar;
    private Button btnCadastrese;
    private TextView esqueciSenha;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View cardLogin = inflater.inflate(R.layout.fragment_card_login, container, false);
        onRegistrar();


        this.etEmail = (EditText) cardLogin.findViewById(R.id.etEmail);
        this.etSenha = (EditText) cardLogin.findViewById(R.id.etSenha);
        this.btnEntrar = (Button) cardLogin.findViewById(R.id.btnEntrar);
        this.esqueciSenha = (TextView) cardLogin.findViewById(R.id.esqueciSenha);
        this.btnCadastrese = (Button) cardLogin.findViewById(R.id.btnCadastrese);

        esqueciSenha.setOnClickListener(this);

        btnEntrar.setOnClickListener(this);

        btnCadastrese.setOnClickListener(this);

        return cardLogin;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEntrar:

                if (validarCampos()) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
                    LoginJson loginJson = new LoginJson();
                    String data = loginJson.loginToJson(etEmail.getText().toString().toLowerCase(), etSenha.getText().toString(), prefs.getString("gcmId", ""));
                    LoginTask task = new LoginTask(data, (MainActivity) getActivity());
                    task.execute();
                }
                break;
            case R.id.esqueciSenha:
                ((MainActivity)getActivity()).RecuperarSenha();
                break;


            case R.id.btnCadastrese:
                Intent intent = new Intent(getActivity(), CadastroUsuarioActivity.class);
                startActivity(intent);
        }

    }

    // Faz o registro no GCM
    public void onRegistrar() {

        Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
        getActivity().startService(intent);

    }

    public Boolean validarCampos() {

        Boolean aux = true;

        if (etEmail.getText().toString().equals("")) {
            aux = false;
            etEmail.setError(getResources().getString(R.string.preenchacampo));
        } else {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(etEmail.getText().toString());
            aux = matcher.matches();
            if (!matcher.matches()) {
                etEmail.setError(getResources().getString(R.string.emailInvalido));
            }
        }

        if (etSenha.getText().toString().equals("")) {
            aux = false;
            etEmail.setError(getResources().getString(R.string.preenchacampo));
        } else {
            if (etSenha.getText().toString().length() < 5) {
                etSenha.setError(getResources().getString(R.string.senhacurta));
                aux = false;
            }
        }

        return aux;
    }

}
