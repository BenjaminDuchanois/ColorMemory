package com.example.colormemory.Score;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colormemory.R;

import java.util.List;

//Liste des joueurs
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
        TextView textRang = listViewItem.findViewById(R.id.joueurrang);
        ImageView imageRang1 = listViewItem.findViewById(R.id.joueurrang1);
        ImageView imageRang2 = listViewItem.findViewById(R.id.joueurrang2);
        ImageView imageRang3 = listViewItem.findViewById(R.id.joueurrang3);

        Joueur joueur = joueurList.get(position);


        textName.setText(joueur.getPseudo());
        textMail.setText(joueur.getMail());
        textScore.setText(Double.toString(joueur.getBestScore()));

        //Si le joueur est dans le top 3, lui donne un rang spécial avec une médaille.
        if(position>2)
        {
            textRang.setVisibility(View.VISIBLE);
            textRang.setText(Integer.toString(position+1) + ".");
        }
        else
            if (position==0)
                imageRang1.setVisibility(View.VISIBLE);
            else
                if (position==1)
                    imageRang2.setVisibility(View.VISIBLE);
                else
                    imageRang3.setVisibility(View.VISIBLE);

        return listViewItem;
    }
}
