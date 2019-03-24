package com.example.colormemory;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HightScore extends AppCompatActivity {

    private DatabaseReference dataJoueur;
    private ListView listViewJoueur;
    private List<Joueur> listJoueur;
    private TextView _rang, _score, _pseudo, _mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hight_score);

        _rang = findViewById(R.id.highscorerang);
        _score = findViewById(R.id.highscorescore);
        _pseudo = findViewById(R.id.highscorepseudo);
        _mail = findViewById(R.id.highscoremail);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/edosz.ttf");
        _rang.setTypeface(font);
        _score.setTypeface(font);
        _pseudo.setTypeface(font);
        _mail.setTypeface(font);


        listViewJoueur = (ListView) findViewById(R.id.listScore);
        dataJoueur = FirebaseDatabase.getInstance().getReference("ID");
        listJoueur = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("ID")
                .orderByChild("bestScoreNeg")
                .limitToFirst(10);
        query.addListenerForSingleValueEvent(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            listJoueur.clear();

            for (DataSnapshot joueurSnap : dataSnapshot.getChildren()){
                String idJoueur = joueurSnap.child("ID").getValue(String.class);
                String nomJoueur = joueurSnap.child("pseudo").getValue(String.class);
                String mailJoueur = joueurSnap.child("mail").getValue(String.class);
                double scoreJoueur = joueurSnap.child("bestScore").getValue(double.class);

                Joueur joueur = new Joueur(idJoueur, nomJoueur, mailJoueur, scoreJoueur);

                listJoueur.add(joueur);
            }

            JoueurList adapter = new JoueurList(HightScore.this, listJoueur);
            listViewJoueur.setAdapter(adapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



    @Override
    protected void onStart() {
        super.onStart();

        /*dataJoueur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listJoueur.clear();

                for (DataSnapshot joueurSnap : dataSnapshot.getChildren()){
                    String idJoueur = joueurSnap.child("ID").getValue(String.class);
                    String nomJoueur = joueurSnap.child("pseudo").getValue(String.class);
                    String mailJoueur = joueurSnap.child("mail").getValue(String.class);
                    double scoreJoueur = joueurSnap.child("bestScore").getValue(double.class);

                    Joueur joueur = new Joueur(idJoueur, nomJoueur, mailJoueur, scoreJoueur);

                    listJoueur.add(joueur);
                }

                JoueurList adapter = new JoueurList(HightScore.this, listJoueur);
                listViewJoueur.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */
    }
}