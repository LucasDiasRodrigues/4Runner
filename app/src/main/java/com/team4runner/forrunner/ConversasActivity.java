package com.team4runner.forrunner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.adapter.ListViewConversasAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;

import org.w3c.dom.Text;

import java.util.List;

public class ConversasActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView mListView;
    ListViewConversasAdapter adapter;

    private TextView txtSemConversas;

    private Corredor corredor;
    private Treinador treinador;

    private String perfil;

    //listas
    List<Bundle> mList;

    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conversas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        if (prefs.getString("perfil", "").equals("corredor")) {
            corredor = new Corredor();
            corredor.setEmail(prefs.getString("email", ""));
            perfil = "corredor";

        } else if (prefs.getString("perfil", "").equals("treinador")) {
            treinador = new Treinador();
            treinador.setEmail(prefs.getString("email",""));
            perfil = "treinador";
        }

        mListView = (ListView)findViewById(R.id.listViewConversas);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               if(perfil.equals("corredor")){
                   Treinador treinadorSelec = new Treinador();
                   treinadorSelec.setEmail(mList.get(position).getString("email",""));
                   treinadorSelec.setNome(mList.get(position).getString("nome", ""));
                   treinadorSelec.setImagemPerfil(mList.get(position).getString("profPic",""));

                   Intent intent= new Intent(ConversasActivity.this, ChatActivity.class);
                   intent.putExtra("origem","corredor");
                   intent.putExtra("treinador", treinadorSelec);
                   startActivity(intent);

               } else if (perfil.equals("treinador")){
                   Corredor corredorSelec = new Corredor();
                   corredorSelec.setEmail(mList.get(position).getString("email",""));
                   corredorSelec.setNome(mList.get(position).getString("nome", ""));
                   corredorSelec.setImagemPerfil(mList.get(position).getString("profPic",""));

                   Intent intent= new Intent(ConversasActivity.this, ChatActivity.class);
                   intent.putExtra("origem","treinador");
                   intent.putExtra("corredor", corredorSelec);
                   startActivity(intent);
               }


              }
        });

        txtSemConversas = (TextView) findViewById(R.id.txtSemConversas);
        txtSemConversas.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaConversas();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        listaConversas();

    }

    public void atualizaLista(List<Bundle> conversas){
        mList = conversas;

        progressBar.setVisibility(View.GONE);

        if(perfil.equals("corredor")){
            adapter = new ListViewConversasAdapter(mList,this);
            mListView.setAdapter(adapter);
        } else if(perfil.equals("treinador")){
            adapter = new ListViewConversasAdapter(mList,this);
            mListView.setAdapter(adapter);
        }

        if(mList.size() < 1){
            txtSemConversas.setVisibility(View.VISIBLE);
        } else {
            txtSemConversas.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setRefreshing(false);

    }

    public void listaConversas(){


        if(perfil.equals("corredor")){
           corredor.buscaConversas(this);
        } else if(perfil.equals("treinador")){
          treinador.buscaConversas(this);
        }


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
