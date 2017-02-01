package com.team4runner.forrunner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.tasks.AtualizaPerfilCorredorTask;
import com.team4runner.forrunner.tasks.ListaObjetivosTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PerfilCorredorActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    private Spinner spnObj;
    List<Objetivo> objetivos;

    //para a selecao da imagem de perfil
    private String localArquivoFoto;
    private final int IMG_CAM = 1;
    private final int IMG_SDCARD = 2;
    Bitmap imagemfotoReduzida;
    Bitmap imagemfoto;


    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    ImageView imagemPerfil;
    EditText txtNome;
    EditText txtEmail;
    EditText txtTelefone;
    EditText txtDataNasc;
    EditText txtAltura;
    EditText txtPeso;
    EditText txtSobreMim;
    private RadioGroup rbSexo;
    RadioButton rbMasc;


    private String sexo;
    RadioButton rbFem;

    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_corredor);
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        //ColapsingToolbar com imagem e nome
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(prefs.getString("nome", "nome"));
        imagemPerfil = (ImageView) findViewById(R.id.toolbarFotoPerfil);
        if (!prefs.getString("imagemperfil", "").equals("")) {
            Picasso.with(this).load(getResources().getString(R.string.imageserver) + prefs.getString("imagemperfil", "")).into(imagemPerfil);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Demais componentes

        rbSexo = (RadioGroup) findViewById(R.id.radiogroupsex);
        rbMasc = (RadioButton) findViewById(R.id.radiobuttonmasc);
        rbFem = (RadioButton) findViewById(R.id.radiobuttonfem);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtTelefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BR"));
        }
        txtDataNasc = (EditText) findViewById(R.id.txtDataNasc);
        txtSobreMim = (EditText) findViewById(R.id.txtSobreMim);

        txtAltura = (EditText) findViewById(R.id.txtAltura);
        txtPeso = (EditText) findViewById(R.id.txtPeso);

        txtNome.setText(prefs.getString("nome", ""));
        txtEmail.setText(prefs.getString("email", ""));
        txtTelefone.setText(prefs.getString("telefone", ""));
        txtDataNasc.addTextChangedListener(Mask.insert("##/##/####", txtDataNasc));
        txtDataNasc.setText(prefs.getString("dataNasc", ""));
        txtAltura.setText(prefs.getString("altura", ""));
        txtPeso.setText(prefs.getString("peso", ""));
        txtSobreMim.setText(prefs.getString("sobreMim", ""));

        if (prefs.getString("sexo", "").equals("masculino")) {
            rbMasc.setChecked(true);
            rbFem.setChecked(false);
        } else if (prefs.getString("sexo", "").equals("feminino")) {
            rbFem.setChecked(true);
            rbMasc.setChecked(false);
        }


        spnObj = (Spinner) findViewById(R.id.spinnerObj);
        configuraSpinner();


        int auxObj = (Integer.parseInt(prefs.getString("objetivo", "1"))) - 1;
        spnObj.setSelection(auxObj);


        FloatingActionButton fabImagemPerfil = (FloatingActionButton) findViewById(R.id.fabButton);
        fabImagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EscolherImagem();
            }
        });

        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
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

                    //Salvar as informacoes aqui!!!
                    Corredor corredor = new Corredor();

                    corredor.setNome(txtNome.getText().toString());
                    corredor.setEmail(txtEmail.getText().toString());
                    corredor.setTelefone(txtTelefone.getText().toString());
                    try {
                        corredor.setDataNasc(brDateFormat.parse(txtDataNasc.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    corredor.setAltura(Double.valueOf(txtAltura.getText().toString()));
                    corredor.setPeso(Double.valueOf(txtPeso.getText().toString()));
                    corredor.setSexo(sexo);
                    corredor.setSobreMim(txtSobreMim.getText().toString());
                    Objetivo obj = (Objetivo) spnObj.getSelectedItem();
                    corredor.setObjetivo(obj.getCodObj());

                    Log.i("coco ======", corredor.getEmail());


                    AtualizaPerfilCorredorTask taskCadastro = new AtualizaPerfilCorredorTask(PerfilCorredorActivity.this, corredor, imagemfoto);
                    taskCadastro.execute();

                }
            }
        });


    }


    private void EscolherImagem() {

        final CharSequence[] options = {getText(R.string.tirarfoto), getText(R.string.escolherdagaleria), getText(R.string.cancelar)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.escolhafoto);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getText(R.string.tirarfoto))) {
                    localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMG_CAM && resultCode == RESULT_OK) {

            imagemfoto = BitmapFactory.decodeFile(localArquivoFoto);

            imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imagemPerfil.getWidth(), imagemPerfil.getHeight(), true);
            imagemPerfil.setImageBitmap(imagemfotoReduzida);
            imagemPerfil.setTag(localArquivoFoto);


        } else if (data != null && requestCode == IMG_SDCARD && resultCode == RESULT_OK) {
            Uri img = data.getData();

            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(img);
                //Imagem original
                imagemfoto = BitmapFactory.decodeStream(inputStream);

                imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imagemPerfil.getWidth(), imagemPerfil.getHeight(), true);
                imagemPerfil.setImageBitmap(imagemfotoReduzida);
                imagemPerfil.setTag(localArquivoFoto);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar a imagem", Toast.LENGTH_LONG).show();
            }


        }

    }

    private void configuraSpinner() {

        //pegar dados do server aqui
        ListaObjetivosTask lObjTask = new ListaObjetivosTask(this);
        lObjTask.execute();
        try {
            objetivos = (List) lObjTask.get();


            // String[] objts = new String[];
            ArrayAdapter<Objetivo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, objetivos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spnObj.setAdapter(adapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public boolean validaCampos() {
        boolean valida = true;

        if (txtNome.getText().toString().equals("")) {
            valida = false;
            txtNome.setError(getResources().getString(R.string.preenchacampo));
        }
        if (txtDataNasc.getText().toString().equals("")) {
            valida = false;
            txtDataNasc.setError(getResources().getString(R.string.preenchacampo));
        }
        if (txtAltura.getText().toString().equals("")) {
            valida = false;
            txtAltura.setError(getResources().getString(R.string.preenchacampo));
        }
        if (txtPeso.getText().toString().equals("")) {
            valida = false;
            txtPeso.setError(getResources().getString(R.string.preenchacampo));
        }
        return valida;
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
