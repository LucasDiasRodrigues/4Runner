package com.team4runner.forrunner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.adapter.AbasPagerAdapterMeuCorredorTreinador;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTreinosFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;

public class MeuCorredorTreinadorActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private int numTabs = 3;
    private TabLayout mTabLayout;
    private Toolbar toolbar;

    private CoordinatorLayout mCoordinator;
    private ImageView imageViewToolbar;

    private Corredor corredor;
    private Treinador treinador;

    //Referencias para as fragments criadas com o adapter
    MeusCorredoresTreinadorTreinosFragment treinosFragment;
    MeusCorredoresTreinadorTestesFragment testesFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_corredor_treinador);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);

        //Recebendo os dados do corredor
        Intent intent = getIntent();
        corredor = (Corredor) intent.getSerializableExtra("Corredor");

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        treinador = new Treinador();
        treinador.setEmail(prefs.getString("email",""));

        //Configurando a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getCorredor().getNome());
        toolbar.setSubtitle(getCorredor().getEmail());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Configurando as tabs e fragments
        AbasPagerAdapterMeuCorredorTreinador pagerAdapter = new AbasPagerAdapterMeuCorredorTreinador(this, getSupportFragmentManager(), numTabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meu_corredor_treinador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.chat) {
            Intent it = new Intent(this, ChatActivity.class);
            it.putExtra("corredor", corredor);
            it.putExtra("origem","treinador");
            startActivity(it);
            return true;
        }

        if (id == R.id.desassociarCorredor) {

            new AlertDialog.Builder(this).setTitle(R.string.desassociarCorredor)
                    .setMessage(R.string.desejadesassociarcorredor)
                    .setNegativeButton(R.string.cancelar, null)
                    .setPositiveButton(R.string.simcerteza, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //desvincular-se
                            treinador.desassociarDeCorredor(MeuCorredorTreinadorActivity.this, corredor);
                        }
                    }).show();


            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public Corredor getCorredor() {
        return corredor;
    }

    //M�todos para receber as referencias das Fragments
    public void setFragmentTreinos(MeusCorredoresTreinadorTreinosFragment fragment){
        this.treinosFragment = fragment;
    }

    public void setFragmentTestes(MeusCorredoresTreinadorTestesFragment fragment){
        this.testesFragments = fragment;
    }

    public void atualizaListaTreinos(){
        treinosFragment.AtualizaListaTreino();
    }
    public void atualizaListaTeste(){
        testesFragments.ListaTestes();
    }


}
