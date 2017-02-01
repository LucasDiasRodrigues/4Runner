package com.team4runner.forrunner.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.Postergado_NaoUsado.OldGCM;
import com.team4runner.forrunner.GCM.RegistrationIntentService;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.CadastroTreinadorBasicoTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 28/06/2015.
 */
public class CadastroTreinadorBasicoFragment extends Fragment {

    Toolbar toolbar;
    Button btnAvancar;

    DateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    Bitmap imagemfoto;

    //para a selecao da imagem de perfil
    private String localArquivoFoto;
    private final int IMG_CAM = 1;
    private final int IMG_SDCARD = 2;
    String imagemDecodificada = "";
    Bitmap imagemfotoReduzida;

    private final Treinador treinador = new Treinador();
    Bundle treinadorBundle;

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    private EditText nome;
    private CircleImageView fotoPerfil;
    private EditText telefone;
    private static EditText dataNasc;
    private RadioGroup rbSexo;
    private String sexo;
    private EditText email;
    private EditText senha;
    private EditText confirmaSenha;


    //gcm
    private String regId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_treinador_basico, container, false);

        onRegistrar();

        toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        ((CadastroUsuarioActivity) getActivity()).setSupportActionBar(toolbar);
        ((CadastroUsuarioActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CadastroUsuarioActivity) getActivity()).getSupportActionBar().setTitle(R.string.cadastrobasico);

        nome = (EditText) fragment.findViewById(R.id.editTextNome);
        email = (EditText) fragment.findViewById(R.id.editTextEmail);
        telefone = (EditText) fragment.findViewById(R.id.editTextTelefone);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BR"));
        }
        dataNasc = (EditText) fragment.findViewById(R.id.editTextDataNasc);
        dataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dataFragment = new DatePickerFragment();
                dataFragment.show(getActivity().getSupportFragmentManager(), "dataNasc");
            }
        });
        senha = (EditText) fragment.findViewById(R.id.editTextSenha);
        confirmaSenha = (EditText) fragment.findViewById(R.id.editTextConfirmaSenha);
        rbSexo = (RadioGroup) fragment.findViewById(R.id.radiogroupsex);

        fotoPerfil = (CircleImageView) fragment.findViewById(R.id.fotoPerfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EscolherImagem();
            }
        });


        /*
        btnAvancar = (Button) fragment.findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validaCampos()) {

                    //Tratar RadioButton
                    switch (rbSexo.getCheckedRadioButtonId()) {
                        case R.id.radiobuttonmasc:
                            sexo = "masculino";
                            break;
                        case R.id.radiobuttonfem:
                            sexo = "feminino";
                            break;
                    }

                    //Tratar ImageViwew
                    //localiza e transforma em um array de bytes
                    if (localArquivoFoto != null) {

                        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
                        imagemfotoReduzida.compress(Bitmap.CompressFormat.JPEG, 20, bAOS);
                        byte[] imagemArrayBytes = bAOS.toByteArray();
                        //decodifica com a classe BASE64 e transforma em string
                        imagemDecodificada = Base64.encodeToString(imagemArrayBytes, Base64.DEFAULT);
                    }

                    SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

                    if (!prefs.getString("gcmId", "").equals("")) {

                        //Salvar as informacoes aqui!!!
                        treinador.setNome(nome.getText().toString());
                        treinador.setEmail(email.getText().toString().toLowerCase());
                        treinador.setTelefone(telefone.getText().toString());
                        try {
                            treinador.setDataNasc(brDateFormat.parse(dataNasc.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        treinador.setSenha(senha.getText().toString());
                        treinador.setSexo(sexo);
                        treinador.setGcmId(prefs.getString("gcmId",""));
                        treinador.setDataCadastro(Calendar.getInstance().getTime());

                        Log.i("coco ======", treinador.getEmail());


                        CadastroTreinadorBasicoTask taskCadastro = new CadastroTreinadorBasicoTask(getActivity(), treinador, imagemfoto);
                        taskCadastro.execute();

                    } else {
                        Toast.makeText(getActivity(), "Erro nas Preferencias", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

            */

        return fragment;
    }

    private void EscolherImagem() {

        final CharSequence[] options = {getText(R.string.tirarfoto), getText(R.string.escolherdagaleria), getText(R.string.cancelar)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.escolhafoto);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getText(R.string.tirarfoto))) {
                    localArquivoFoto = getActivity().getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localArquivoFoto)));
                    irParaCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(irParaCamera, IMG_CAM);
                } else if (options[item].equals(getText(R.string.escolherdagaleria))) {
                    Intent irParaGaleria = new Intent(Intent.ACTION_GET_CONTENT);
                    irParaGaleria.setType("image/*");
                    startActivityForResult(irParaGaleria, IMG_SDCARD);

                } else if (options[item].equals(getText(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Recebe e trata informacoes das aplicacoes responsaveis pela foto
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMG_CAM && resultCode == getActivity().RESULT_OK) {

            imagemfoto = BitmapFactory.decodeFile(localArquivoFoto);

            imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, fotoPerfil.getWidth(), fotoPerfil.getHeight(), true);
            fotoPerfil.setImageBitmap(imagemfotoReduzida);
            fotoPerfil.setTag(localArquivoFoto);


        } else if (data != null && requestCode == IMG_SDCARD && resultCode == getActivity().RESULT_OK) {
            Uri img = data.getData();
            InputStream inputStream;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(img);
                //Imagem original
                imagemfoto = BitmapFactory.decodeStream(inputStream);

                imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, fotoPerfil.getWidth(), fotoPerfil.getHeight(), true);
                fotoPerfil.setImageBitmap(imagemfotoReduzida);
                fotoPerfil.setTag(localArquivoFoto);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Erro ao carregar a imagem", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cadastro_treinador, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.avancar) {

            if (validaCampos()) {

                //Tratar RadioButton
                switch (rbSexo.getCheckedRadioButtonId()) {
                    case R.id.radiobuttonmasc:
                        sexo = "masculino";
                        break;
                    case R.id.radiobuttonfem:
                        sexo = "feminino";
                        break;
                }

                //Tratar ImageViwew
                //localiza e transforma em um array de bytes
                if (localArquivoFoto != null) {

                    ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
                    imagemfotoReduzida.compress(Bitmap.CompressFormat.JPEG, 20, bAOS);
                    byte[] imagemArrayBytes = bAOS.toByteArray();
                    //decodifica com a classe BASE64 e transforma em string
                    imagemDecodificada = Base64.encodeToString(imagemArrayBytes, Base64.DEFAULT);
                }

                SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

                if (!prefs.getString("gcmId", "").equals("")) {

                    //Salvar as informacoes aqui!!!
                    treinador.setNome(nome.getText().toString());
                    treinador.setEmail(email.getText().toString().toLowerCase());
                    treinador.setTelefone(telefone.getText().toString());
                    try {
                        treinador.setDataNasc(brDateFormat.parse(dataNasc.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    treinador.setSenha(senha.getText().toString());
                    treinador.setSexo(sexo);
                    treinador.setGcmId(prefs.getString("gcmId", ""));
                    treinador.setDataCadastro(Calendar.getInstance().getTime());

                    Log.i("coco ======", treinador.getEmail());


                    CadastroTreinadorBasicoTask taskCadastro = new CadastroTreinadorBasicoTask(getActivity(), treinador, imagemfoto);
                    taskCadastro.execute();

                } else {
                    Toast.makeText(getActivity(), "Erro nas Preferencias", Toast.LENGTH_LONG).show();
                }

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Boolean validaCampos() {

        Boolean retorno = true;

        if (nome.getText().toString().equals("")) {
            nome.setError(getActivity().getResources().getString(R.string.preenchacampo));
            nome.setFocusable(true);
            retorno = false;

        }
        if (dataNasc.getText().toString().equals("")) {
            dataNasc.setError(getActivity().getResources().getString(R.string.preenchacampo));
            dataNasc.setFocusable(true);
            retorno = false;

        }
        if (rbSexo.getCheckedRadioButtonId() == View.NO_ID) {
            rbSexo.setFocusable(true);
            Toast.makeText(getActivity(), getString(R.string.sexonaopreenchido), Toast.LENGTH_SHORT).show();

            retorno = false;

        }
        if (email.getText().toString().equals("")) {
            email.setError(getActivity().getResources().getString(R.string.preenchacampo));
            email.setFocusable(true);
            retorno = false;
        } else {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email.getText().toString());
            retorno = matcher.matches();
            if (!matcher.matches()) {
                email.setError(getResources().getString(R.string.emailInvalido));
            }

        }
        if (senha.getText().toString().equals("")) {
            senha.setError(getActivity().getResources().getString(R.string.preenchacampo));
            senha.setFocusable(true);
            retorno = false;
        } else {
            if (senha.getText().toString().length() < 5) {
                senha.setError(getResources().getString(R.string.senhacurta));
                retorno = false;
            }
        }
        if (confirmaSenha.getText().toString().equals("")) {
            confirmaSenha.setError(getActivity().getResources().getString(R.string.preenchacampo));
            confirmaSenha.setFocusable(true);
            retorno = false;
        } else {
            if (!confirmaSenha.getText().toString().equals(senha.getText().toString())) {
                confirmaSenha.setError(getResources().getString(R.string.senhasdiferentes));
                retorno = false;
            }
        }

        return retorno;


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
            DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
            picker.getDatePicker().setSpinnersShown(true);
            picker.getDatePicker().setCalendarViewShown(false);
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
            dataNasc.setText(aux);

        }
    }


    // Faz o registro no GCM
    public void onRegistrar() {


        Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
        getActivity().startService(intent);


    }

    // Cancela o registro no GCM
    public void onCancelar() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OldGCM.unregister(getActivity());
                Log.i("Cancelado com sucesso!", "cancelado");
            }
        }.start();
    }


}