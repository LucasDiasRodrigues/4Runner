package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 22/12/2015.
 */
public class ListViewAdapterNovidades extends BaseAdapter {
    private Activity activity;
    private List<Bundle> mList;
    private String tipo;
    DecimalFormat df = new DecimalFormat("00.00");


    public ListViewAdapterNovidades(Activity activity, List<Bundle> bundles, String tipo) {
        this.activity = activity;
        this.mList = bundles;
        this.tipo = tipo;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        switch (tipo) {
            case "treinoavencer":

                Bundle bundle = mList.get(position);
                String nomeCorredor = bundle.getString("nomeCorredor");
                String profPic = bundle.getString("profPic","");
                String emailCorredor = bundle.getString("emailCorredor");
                int codTreino = bundle.getInt("codTreino");
                String nomeTreino = bundle.getString("nomeTreino");
                Date dtFim = (Date) bundle.getSerializable("dtFim");
                int diasRestantes = bundle.getInt("diasRestantes");

                View view = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_treinoavencer, parent, false);

                CircleImageView imagemPerfil = (CircleImageView) view.findViewById(R.id.imagemPerfil);
                if (!profPic.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic).into(imagemPerfil);
                }
                TextView txtNome = (TextView) view.findViewById(R.id.txtNomeCorredor);
                txtNome.setText(nomeCorredor);
                txtNome.setTag(emailCorredor);

                TextView txtNomeTreino = (TextView) view.findViewById(R.id.txtNomeTreino);

                txtNomeTreino.setText(nomeTreino);
                txtNomeTreino.setTag(codTreino);

                TextView txtDiasRestantes = (TextView) view.findViewById(R.id.txtDiasRestantes);
                txtDiasRestantes.setText(String.valueOf(diasRestantes));

                return view;

            case "treinoconcluido":


                Bundle bundle2 = mList.get(position);
                String nomeCorredor2 = bundle2.getString("nomeCorredor");
                String profPic2 = bundle2.getString("profPic","");
                int codTreino2 = bundle2.getInt("codTreino");
                String nomeTreino2 = bundle2.getString("nomeTreino");
                Date dtFim2 = (Date) bundle2.getSerializable("dtFim");


                View view2 = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_treinoconcluido, parent, false);


                CircleImageView imagemPerfil2 = (CircleImageView) view2.findViewById(R.id.imagemPerfil);

                if (!profPic2.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic2).into(imagemPerfil2);
                }
                TextView txtNome2 = (TextView) view2.findViewById(R.id.txtNomeCorredor);
                txtNome2.setText(nomeCorredor2);

                TextView txtNomeTreino2 = (TextView) view2.findViewById(R.id.txtNomeTreino);

                txtNomeTreino2.setText(nomeTreino2);
                txtNomeTreino2.setTag(codTreino2);

                TextView txtConclusao = (TextView) view2.findViewById(R.id.txtConclusao);

                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                txtConclusao.setText(String.valueOf(format.format(dtFim2)));


                return view2;
            case "ultimascorridas":

                Bundle bundle3 = mList.get(position);
                String nomeCorredor3 = bundle3.getString("nomeCorredor");
                String profPic3 = bundle3.getString("profPic","");
                int codCorrida3 = bundle3.getInt("codCorrida");
                String distancia = bundle3.getString("distancia");
                String ritMedio = bundle3.getString("ritMedio");
                Date dataInicio = (Date) bundle3.getSerializable("dtInicio");
                Date duracao = (Date) bundle3.getSerializable("duracao");

                View view3 = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_ultimascorridas, parent, false);


                CircleImageView imagemPerfil3 = (CircleImageView) view3.findViewById(R.id.imagemPerfil);

                if (!profPic3.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic3).into(imagemPerfil3);
                }
                TextView txtNomeCorredor3 = (TextView) view3.findViewById(R.id.txtNomeCorredor);
                txtNomeCorredor3.setText(nomeCorredor3);

                TextView txtData3 = (TextView) view3.findViewById(R.id.txtData);
                txtData3.setTag(codCorrida3);
                DateFormat format3 = new SimpleDateFormat("dd/MM/yyyy");
                txtData3.setText(String.valueOf(format3.format(dataInicio)));

                TextView txtDuracao = (TextView) view3.findViewById(R.id.txtDuracao);
                DateFormat format33 = new SimpleDateFormat("HH:mm:ss");
                txtDuracao.setText(String.valueOf(format33.format(duracao)));

                TextView txtDistancia = (TextView) view3.findViewById(R.id.txtDistancia);
                txtDistancia.setText(distancia);

                TextView txtRitmo = (TextView) view3.findViewById(R.id.txtRitmo);
                DecimalFormat decimalFormat = new DecimalFormat("00.00");
                double auxRitmoDouble = Double.parseDouble(ritMedio);
                txtRitmo.setText(decimalFormat.format(auxRitmoDouble));

                return  view3;
            case "ultimostestes":

                Bundle bundle4 = mList.get(position);
                String nomeCorredor4 = bundle4.getString("nome");
                String profPic4 = bundle4.getString("profPic","");
                int codCorrida4 = bundle4.getInt("codCorrida");
                String distancia4 = bundle4.getString("distancia");
                String ritMedio4 = bundle4.getString("ritMedio");
                Date dataInicio4 = (Date) bundle4.getSerializable("dtInicio");
                Date duracao4 = (Date) bundle4.getSerializable("duracao");

                View view4 = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_ultimascorridas, parent, false);


                CircleImageView imagemPerfil4 = (CircleImageView) view4.findViewById(R.id.imagemPerfil);

                if (!profPic4.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic4).into(imagemPerfil4);
                }
                TextView txtNomeCorredor4 = (TextView) view4.findViewById(R.id.txtNomeCorredor);
                txtNomeCorredor4.setText(nomeCorredor4);

                TextView txtData4 = (TextView) view4.findViewById(R.id.txtData);
                txtData4.setTag(codCorrida4);
                DateFormat format4 = new SimpleDateFormat("dd/MM/yyyy");
                txtData4.setText(String.valueOf(format4.format(dataInicio4)));

                TextView txtDuracao4 = (TextView) view4.findViewById(R.id.txtDuracao);
                DateFormat format44 = new SimpleDateFormat("HH:mm:ss");
                txtDuracao4.setText(String.valueOf(format44.format(duracao4)));

                TextView txtDistancia4 = (TextView) view4.findViewById(R.id.txtDistancia);
                txtDistancia4.setText(distancia4);

                TextView txtRitmo4 = (TextView) view4.findViewById(R.id.txtRitmo);
                DecimalFormat decimalFormat2 = new DecimalFormat("00.00");
                double auxRitmoDouble4 = Double.parseDouble(ritMedio4);
                txtRitmo4.setText(decimalFormat2.format(auxRitmoDouble4));

                return  view4;
            case "novoscorredores":

                Bundle bundle5 = mList.get(position);
                final String nomeCorredor5 = bundle5.getString("nomeCorredor");
                final String profPic5 = bundle5.getString("profPic","");
                final String emailCorredor5 = bundle5.getString("emailCorredor");
                Date dataInicio5 = (Date) bundle5.getSerializable("dtInicio");

                View view5 = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_novoscorredores, parent, false);


                CircleImageView imagemPerfil5 = (CircleImageView) view5.findViewById(R.id.imagemPerfil);

                if (!profPic5.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic5).into(imagemPerfil5);
                }
                TextView txtNomeCorredor5 = (TextView) view5.findViewById(R.id.txtNomeCorredor);
                txtNomeCorredor5.setText(nomeCorredor5);
                txtNomeCorredor5.setTag(emailCorredor5);

                TextView txtCorredorDesde = (TextView) view5.findViewById(R.id.txtCorredorDesde);
                DateFormat format5 = new SimpleDateFormat("dd/MM/yyyy");
                txtCorredorDesde.setText(String.valueOf(format5.format(dataInicio5)));


                return  view5;

            case "ausencias":

                Bundle bundle6 = mList.get(position);
                final String nomeCorredor6 = bundle6.getString("nomeCorredor");
                final String profPic6 = bundle6.getString("profPic","");
                final String nomeTreino6 = bundle6.getString("nomeTreino");
                final int codTreino6 = bundle6.getInt("codTreino");
                final String emailCorredor6 = bundle6.getString("emailCorredor");
                Date dtAusencia6 = (Date) bundle6.getSerializable("dtAusencia");

                View view6 = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_ausenciatreino, parent, false);


                CircleImageView imagemPerfil6 = (CircleImageView) view6.findViewById(R.id.imagemPerfil);

                if (!profPic6.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic6).into(imagemPerfil6);
                }
                TextView txtNomeCorredor6 = (TextView) view6.findViewById(R.id.txtNomeCorredor);
                txtNomeCorredor6.setText(nomeCorredor6);
                txtNomeCorredor6.setTag(emailCorredor6);

                TextView txtNomeTreino6 = (TextView) view6.findViewById(R.id.txtNomeTreino);
                txtNomeTreino6.setText(nomeTreino6);
                txtNomeTreino6.setTag(codTreino6);

                TextView txtAusenciaTreino = (TextView) view6.findViewById(R.id.txtAusencia);
                DateFormat format6 = new SimpleDateFormat("dd/MM/yyyy");
                txtAusenciaTreino.setText(format6.format(dtAusencia6));



                return  view6;

            case "ausenciasteste":

                Bundle bundle7 = mList.get(position);
                final String nomeCorredor7 = bundle7.getString("nomeCorredor");
                final String profPic7 = bundle7.getString("profPic","");
                final int codTeste7 = bundle7.getInt("codTeste");
                final String emailCorredor7 = bundle7.getString("emailCorredor");
                Date dtTeste7 = (Date) bundle7.getSerializable("dtTeste");

                View view7 = activity.getLayoutInflater().inflate(R.layout.item_list_home_novidades_treinador_cards_ausenciateste, parent, false);


                CircleImageView imagemPerfil7 = (CircleImageView) view7.findViewById(R.id.imagemPerfil);

                if (!profPic7.equals("")) {
                    Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + profPic7).into(imagemPerfil7);
                }
                TextView txtNomeCorredor7 = (TextView) view7.findViewById(R.id.txtNomeCorredor);
                txtNomeCorredor7.setText(nomeCorredor7);
                txtNomeCorredor7.setTag(emailCorredor7);

                TextView txtAusenciaTeste = (TextView) view7.findViewById(R.id.txtAusencia);
                DateFormat format7 = new SimpleDateFormat("dd/MM/yyyy");
                txtAusenciaTeste.setText(format7.format(dtTeste7));
                txtAusenciaTeste.setTag(codTeste7);



                return  view7;
        }


        return null;
    }
}
