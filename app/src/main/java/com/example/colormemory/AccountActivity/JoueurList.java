package com.example.colormemory.AccountActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.colormemory.R;

import java.util.List;

public class JoueurList extends ArrayAdapter<Joueur> {

    private Activity context;
    private List<Joueur> joueurList;

    public JoueurList(Activity context, List<Joueur> joueurList){
        super(context, R.layout.list_layout, joueurList);
        this.context = context;
        this.joueurList = joueurList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textName = listViewItem.findViewById(R.id.joueurname);
        TextView textMail = listViewItem.findViewById(R.id.joueurmail);
        TextView textScore = listViewItem.findViewById(R.id.joueurscore);

        Joueur joueur = joueurList.get(position);

        textName.setText(joueur.getPseudo());
        textMail.setText(joueur.getMail());
        textScore.setText(Double.toString(joueur.getBestScore()));

        return listViewItem;
    }
}
