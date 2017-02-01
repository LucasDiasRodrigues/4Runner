package com.team4runner.forrunner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.Util.Mask;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.AtualizaPerfilTreinadorTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PerfilTreinadorActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;


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
    EditText txtCref;
    EditText txtFormacao;
    EditText txtSobreMim;

    Button btnSalvar;

    private RadioGroup rbSexo;
    RadioButton rbMasc;
    RadioButton rbFem;

    private String sexo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_treinador);
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        //Collapsing toolbar com nome e imagem
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

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtTelefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BR"));
        }
        txtDataNasc = (EditText) findViewById(R.id.txtDataNasc);
        txtCref = (EditText) findViewById(R.id.txtCref);
        txtFormacao = (EditText) findViewById(R.id.txtFormacao);
        txtSobreMim = (EditText) findViewById(R.id.txtSobreMim);

        rbSexo = (RadioGroup) findViewById(R.id.radiogroupsex);
        rbMasc = (RadioButton) findViewById(R.id.radiobuttonmasc);
        rbFem = (RadioButton) findViewById(R.id.radiobuttonfem);


        txtNome.setText(prefs.getString("nome", ""));
        txtEmail.setText(prefs.getString("email", ""));
        txtTelefone.setText(prefs.getString("telefone", ""));
        txtDataNasc.addTextChangedListener(Mask.insert("##/##/####", txtDataNasc));
        txtDataNasc.setText(prefs.getString("dataNasc", ""));
        txtCref.setText(prefs.getString("cref", ""));
        txtFormacao.setText(prefs.getString("formacao", ""));
        txtSobreMim.setText(prefs.getString("sobreMim", ""));


        if (prefs.getString("sexo", "").equals("masculino")) {
            rbMasc.setChecked(true);
            rbFem.setChecked(false);
        } else if (prefs.getString("sexo", "").equals("feminino")) {
            rbFem.setChecked(true);
            rbMasc.setChecked(false);
        }

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
                    Treinador treinador = new Treinador();

                    treinador.setNome(txtNome.getText().toString());
                    treinador.setEmail(txtEmail.getText().toString());
                    treinador.setSexo(sexo);
                    treinador.setTelefone(txtTelefone.getText().toString());
                    try {
                        treinador.setDataNasc(brDateFormat.parse(txtDataNasc.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    treinador.setCref(txtCref.getText().toString());
                    treinador.setFormacao(txtFormacao.getText().toString());
                    treinador.setSobreMim(txtSobreMim.getText().toString());

                    Log.i("coco ======", treinador.getEmail());


                    AtualizaPerfilTreinadorTask taskCadastro = new AtualizaPerfilTreinadorTask(PerfilTreinadorActivity.this, treinador, imagemfoto);
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
        if (txtCref.getText().toString().equals("")) {
            valida = false;
            txtCref.setError(getResources().getString(R.string.preenchacampo));
        }
        if (txtFormacao.getText().toString().equals("")) {
            valida = false;
            txtFormacao.setError(getResources().getString(R.string.preenchacampo));
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
