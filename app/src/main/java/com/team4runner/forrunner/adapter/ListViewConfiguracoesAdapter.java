package com.team4runner.forrunner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team4runner.forrunner.R;

import java.util.List;

/**
 * Created by Lucas on 20/08/2015.
 */
public class ListViewConfiguracoesAdapter extends BaseAdapter {
    Activity activity;
    List<String> mList;

    public ListViewConfiguracoesAdapter(Activity activity, List<String> mList){
        this.activity = activity;
        this.mList = mList;

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
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_configuracoes, parent, false);

        String option = mList.get(position);
        TextView textView = (TextView)view.findViewById(R.id.textView);
        textView.setText(option);

        ImageView imageView = (ImageView)view.findViewById(R.id.icon);

        switch (option){
            case "Perfil":
                imageView.setImageResource(R.drawable.ic_account_grey600_48dp);
                break;
            case "Localizacao":
                imageView.setImageResource(R.drawable.ic_map_marker_grey600_36dp);
                textView.setText(R.string.localizacao);
                break;
            case "Conta":
                imageView.setImageResource(R.drawable.ic_account_key_grey600_48dp);

        }


        return view ;
    }
}
