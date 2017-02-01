package com.team4runner.forrunner;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.fragment.ConfiguracoesTreinadorFragment;

import com.team4runner.forrunner.fragment.ExerciciosTreinadorFragment;

import com.team4runner.forrunner.fragment.HomeTreinadorFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.AtualizarUsuarioTask;
import com.team4runner.forrunner.tasks.LogoffTask;
import com.team4runner.forrunner.tasks.VerificaNotificacoesTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainTreinadorActivity extends ActionBarActivity {


    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout Drawer;
    private Boolean listaCorredors = false;
    ActionBarDrawerToggle mDrawerToggle;
    private Menu menu;

    public CircleImageView headerFotoPerfil;
    public TextView headerNome;

    public Treinador treinador;

    private boolean atualizarUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_treinador);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);


        String appKey = "69c3539fddc137f58983a2300770c8b444254286050d25cf";
        Appodeal.initialize(this, appKey, Appodeal.BANNER | Appodeal.INTERSTITIAL);
        Appodeal.setTesting(true);
        Appodeal.setLogging(true);


        //componentes do header
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        treinador = new Treinador();
        treinador.setEmail(prefs.getString("email", ""));
        treinador.setNome(prefs.getString("nome", ""));


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.inflateHeaderView(R.layout.header_navigation_drawer_treinador);

        headerFotoPerfil = (CircleImageView) headerView.findViewById(R.id.circleView);
        headerNome = (TextView) headerView.findViewById(R.id.headerNome);
        headerNome.setText(prefs.getString("nome", ""));
        TextView headerEmail = (TextView) headerView.findViewById(R.id.headerEmail);
        headerEmail.setText(prefs.getString("email", ""));
        headerFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainTreinadorActivity.this, PerfilTreinadorActivity.class);
                startActivity(intent);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Fecha o drawer qualdo algo eh clicado
                Drawer.closeDrawers();

                //Verifica o que foi clicado
                switch (menuItem.getItemId()) {

                    case R.id.home:
                        HomeTreinadorFragment fragmentHome = new HomeTreinadorFragment();
                        android.support.v4.app.FragmentTransaction transactionHome = getSupportFragmentManager().beginTransaction();
                        transactionHome.replace(R.id.frameTabsLayout, fragmentHome);
                        transactionHome.commit();
                        return true;


                    case R.id.exercicios:
                        ExerciciosTreinadorFragment fragmentExercicios = new ExerciciosTreinadorFragment();
                        android.support.v4.app.FragmentTransaction transactionExercicios = getSupportFragmentManager().beginTransaction();
                        transactionExercicios.replace(R.id.frameTabsLayout, fragmentExercicios);
                        transactionExercicios.commit();
                        return true;

                    case R.id.configuracoes:
                        ConfiguracoesTreinadorFragment fragmentConfig = new ConfiguracoesTreinadorFragment();
                        FragmentTransaction transactionConfig = getSupportFragmentManager().beginTransaction();
                        transactionConfig.replace(R.id.frameTabsLayout, fragmentConfig);
                        transactionConfig.commit();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Algo deu errado", Toast.LENGTH_SHORT).show();

                        return true;

                }

            }
        });

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        FragmentTransaction tabs = getSupportFragmentManager().beginTransaction();
        tabs.replace(R.id.frameTabsLayout, new HomeTreinadorFragment());
        tabs.commit();


        //Anuncio Appodeal  = Ryco
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Desligar Receiver
        this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        atualizarUser = true;
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (atualizarUser) {
            AtualizarUsuarioTask atualizacao = new AtualizarUsuarioTask(this);
            atualizacao.execute();
            atualizarUser = false;
        }

        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        if (!prefs.getString("imagemperfil", "").equals("")) {
            Picasso.with(this).load(getResources().getString(R.string.imageservermini) + prefs.getString("imagemperfil", "")).into(headerFotoPerfil);
        }

        VerificaNotificacoesTask notificacoesTask = new VerificaNotificacoesTask(this, treinador.getEmail());
        notificacoesTask.execute();

        //Ligar reeeiver
        this.registerReceiver(mMessageReceiver, new IntentFilter("chat-4runner"));
        this.registerReceiver(mMessageReceiver, new IntentFilter("solicitacao-4runner"));



        Appodeal.onResume(this,Appodeal.BANNER);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_treinador, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOf) {

            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage(R.string.desejasair)
                    .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onLogoff();
                        }
                    }).setNegativeButton(R.string.nao, null).show();

            return true;
        }

        if (id == R.id.novoCorredor) {

            Intent intent = new Intent(this, NovosCorredoresActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.conversas) {

            //Abrir conversas
            Intent intent = new Intent(this, ConversasActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCorredorClicado(Corredor corredorSelecionado) {

        Intent intent = new Intent(MainTreinadorActivity.this, MeuCorredorTreinadorActivity.class);
        intent.putExtra("Corredor", corredorSelecionado);
        startActivity(intent);

    }

    public void onNovaMensagem(boolean bool) {
        if (bool) {
            menu.findItem(R.id.conversas).setIcon(getResources().getDrawable(R.drawable.ic_chat_red2));
        } else {
            menu.findItem(R.id.conversas).setIcon(getResources().getDrawable(R.drawable.ic_chat));
        }
    }

    public void onNovaSolicitacao(boolean bool) {
        if (bool) {
            menu.findItem(R.id.novoCorredor).setIcon(getResources().getDrawable(R.drawable.ic_account_multiple_white_48dp_red2));
        } else {
            menu.findItem(R.id.novoCorredor).setIcon(getResources().getDrawable(R.drawable.ic_account_multiple_white_48dp));
        }
    }


    //broadcast Receiver
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("chat-4runner")) {

                onNovaMensagem(true);

            }
            if (intent.getAction().equals("solicitacao-4runner")) {

                onNovaSolicitacao(true);

            }


        }
    };


    public void onLogoff() {
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        LogoffTask task = new LogoffTask(prefs.getString("email", ""), "treinador", this);
        task.execute();
    }


    // Verifica se a origem e  � uma notificacao
    public Boolean origemNotificacao() {
        Intent intent = getIntent();
        if (intent.hasExtra("listaCorredor")) {
            listaCorredors = true;
        } else {
            listaCorredors = false;
        }
        return listaCorredors;
    }

    private boolean gettingOut = false;

    @Override
    public void onBackPressed() {

        if(gettingOut){
            super.onBackPressed();
            finish();
        } else{
            gettingOut = true;
            Appodeal.show(this, Appodeal.INTERSTITIAL);
            Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                @Override
                public void onInterstitialLoaded(boolean b) {

                }

                @Override
                public void onInterstitialFailedToLoad() {
                    onBackPressed();
                    (MainTreinadorActivity.this).finish();
                }

                @Override
                public void onInterstitialShown() {

                }

                @Override
                public void onInterstitialClicked() {
                    onBackPressed();
                }

                @Override
                public void onInterstitialClosed() {
                    onBackPressed();
                    (MainTreinadorActivity.this).finish();
                }
            });
        }

    }
}
