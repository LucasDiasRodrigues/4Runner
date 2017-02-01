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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.fragment.ConfiguracoesCorredorFragment;
import com.team4runner.forrunner.fragment.EstatisticasCorredorFragment;
import com.team4runner.forrunner.fragment.HistoricoLivreCorredorFragment;
import com.team4runner.forrunner.fragment.HistoricoTreinosCorredorFragment;
import com.team4runner.forrunner.fragment.HomeCorredorFragment;
import com.team4runner.forrunner.fragment.HomeCorredorTreinadorFragment;
import com.team4runner.forrunner.fragment.TesteDeCampoCorredorFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.tasks.AtualizarUsuarioTask;
import com.team4runner.forrunner.tasks.LogoffTask;
import com.team4runner.forrunner.tasks.VerificaNotificacoesTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainCorredorActivity extends ActionBarActivity {


    public Toolbar toolbar;
    private NavigationView navigationView;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle; //controla a abertura e fechamento do drawer pelo botao da toolbar


    public CircleImageView headerFotoPerfil;
    public TextView headerNome;

    View headerView;

    //Vinculo com a fragment do treinador para atualizacao
    private HomeCorredorTreinadorFragment fragment;

    Corredor corredor;

    private Menu menu;

    private boolean atualizaUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_corredor);
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        String appKey = "69c3539fddc137f58983a2300770c8b444254286050d25cf";
        Appodeal.initialize(this, appKey, Appodeal.BANNER | Appodeal.INTERSTITIAL);
        Appodeal.setTesting(true);
        Appodeal.setLogging(true);

        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));
        corredor.setNome(prefs.getString("nome", ""));



        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        headerView = navigationView.inflateHeaderView(R.layout.header_navigation_drawer_corredor);

        //componentes do header
        headerFotoPerfil = (CircleImageView) headerView.findViewById(R.id.circleView);

        headerNome = (TextView) headerView.findViewById(R.id.headerNome);
        headerNome.setText(prefs.getString("nome", " "));
        TextView headerEmail = (TextView) headerView.findViewById(R.id.headerEmail);
        headerEmail.setText(prefs.getString("email"," "));
        headerFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCorredorActivity.this, PerfilCorredorActivity.class);
                startActivity(intent);
            }
        });


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
                        HomeCorredorFragment fragmentHome = new HomeCorredorFragment();
                        android.support.v4.app.FragmentTransaction transactionHome = getSupportFragmentManager().beginTransaction();
                        transactionHome.replace(R.id.frameTabsLayout, fragmentHome);
                        transactionHome.commit();
                        return true;


                    case R.id.treino:
                        HistoricoTreinosCorredorFragment fragmentHist = new HistoricoTreinosCorredorFragment();
                        FragmentTransaction transactionHist = getSupportFragmentManager().beginTransaction();
                        transactionHist.replace(R.id.frameTabsLayout, fragmentHist);
                        transactionHist.commit();
                        return true;

                    case R.id.corridaLivre:
                        HistoricoLivreCorredorFragment fragmentHistLivre = new HistoricoLivreCorredorFragment();
                        FragmentTransaction transactionHistLivre = getSupportFragmentManager().beginTransaction();
                        transactionHistLivre.replace(R.id.frameTabsLayout, fragmentHistLivre);
                        transactionHistLivre.commit();
                        return true;

                    case R.id.testeCampo:
                        TesteDeCampoCorredorFragment fragmentTeste = new TesteDeCampoCorredorFragment();
                        FragmentTransaction transactionTeste = getSupportFragmentManager().beginTransaction();
                        transactionTeste.replace(R.id.frameTabsLayout, fragmentTeste);
                        transactionTeste.commit();
                        return true;

                    case R.id.estatisticas:
                        EstatisticasCorredorFragment fragmentEstat = new EstatisticasCorredorFragment();
                        FragmentTransaction transactionEstat = getSupportFragmentManager().beginTransaction();
                        transactionEstat.replace(R.id.frameTabsLayout, fragmentEstat);
                        transactionEstat.commit();
                        return true;

                  //  case R.id.amigos:
                    //    AmigosCorredorFragment fragmentAmigos = new AmigosCorredorFragment();
                      //  FragmentTransaction transactionAmigos = getSupportFragmentManager().beginTransaction();
                       // transactionAmigos.replace(R.id.frameTabsLayout, fragmentAmigos);
                        //transactionAmigos.commit();
                       // return true;

                    case R.id.configuracoes:
                        ConfiguracoesCorredorFragment fragmentConfig = new ConfiguracoesCorredorFragment();
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
        tabs.replace(R.id.frameTabsLayout, new HomeCorredorFragment());
        tabs.commit();

        //Anuncio Appodeal  = Ryco
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        atualizaUser = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Desligar Receiver
        this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Ligar reeeiver
        this.registerReceiver(mMessageReceiver, new IntentFilter("chat-4runner"));


        if(atualizaUser){
            AtualizarUsuarioTask atualizacao = new AtualizarUsuarioTask(this,null);
            atualizacao.execute();
            atualizaUser = false;
        }

        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        if (!prefs.getString("imagemperfil", "").equals("")) {
            Picasso.with(this).load(getResources().getString(R.string.imageservermini) + prefs.getString("imagemperfil", "")).into(headerFotoPerfil);
        }
        VerificaNotificacoesTask notificacoesTask = new VerificaNotificacoesTask(this, corredor.getEmail());
        notificacoesTask.execute();

        Appodeal.onResume(this,Appodeal.BANNER);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_corredor, menu);

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


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

        if (id == R.id.conversas) {

            //Abrir conversas
            Intent intent = new Intent(this, ConversasActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNovaMensagem(boolean bool){

        if(bool){
            menu.findItem(R.id.conversas).setIcon(getResources().getDrawable(R.drawable.ic_chat_red2));
            Log.i("onNovaMessage", "true");
        } else {
            menu.findItem(R.id.conversas).setIcon(getResources().getDrawable(R.drawable.ic_chat));
            Log.i("onNovaMessage", "false");
        }
    }


    //broadcast Receiver
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("recieverChat","recebido");
            onNovaMensagem(true);

        }
    };


    public void onLogoff() {

        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        LogoffTask task = new LogoffTask(prefs.getString("email", ""), "corredor", this);
        task.execute();

    }


    //vinculo com a Fragment de Treinador para atualizacao
    public void vinculafragmentTreinador(HomeCorredorTreinadorFragment fragment){
        this.fragment = fragment;
        AtualizarUsuarioTask atualizacao = new AtualizarUsuarioTask(this,fragment);
        atualizacao.execute();
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
                    (MainCorredorActivity.this).finish();
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

                    (MainCorredorActivity.this).finish();
                }
            });
        }

    }


}