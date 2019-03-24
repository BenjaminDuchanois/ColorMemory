package com.example.colormemory;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Jeu extends AppCompatActivity {


    private DatabaseReference scoreUser;
    private FirebaseAuth mAuth;
    private String userID;


    //Déclaration de toutes les variables :
    //Les affichages :
    private TextView points_text, difficulte_text, niveau_text, nbVies_text, scoreFinal, scoreMeilleur, chrono_text,
            inforecup, infoniveau;

    private Timer chrono;

    private boolean chrono_erreur;

    //La liste à generer :
    private ArrayList<Integer> la_serie = new ArrayList<>();

    //La liste pour la vérification :
    private ArrayList<Button> la_verif = new ArrayList<>();

    //Les entiers tels que le niveau ou la difficulté et les constantes engendrées :
    private Integer nbBloc, niveau, difficulte, nbAppuie_min, nbAppuie_max, nbVies, nbAppuie, time,

    //Les variables de stockage
            index, k, i, j, l;

    //Pour le score :
    private double score, poid;

    //Les animations :
    private AlphaAnimation user_appuie = new AlphaAnimation(0f, 1f);

    //Les sons :
    private MediaPlayer son_rouge, son_bleu, son_vert, son_jaune, son_orange, son_rose,
            son_turquoise, son_marron, son_gris, son_violet;

    //L'object pour la synchronisation :
    private final Object attente = new Object();


    //Les boutons :
    private Button boutonRouge, boutonBleu, boutonVert, boutonJaune, boutonOrange, boutonRose,
            boutonTurquoise, boutonMarron, boutonGris, boutonViolet, boutonStart,
            chargerniveau, reprendreniveau;

    //L'aléatoire :
    private Random alea = new Random();

    private ImageView infofond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        scoreUser = FirebaseDatabase.getInstance().getReference().child("ID").child(userID);

        chargerPartie();


        //Récupère l'ID des affichages et de start :
        difficulte_text = findViewById(R.id.textViewDifficulte);
        niveau_text = findViewById(R.id.textViewNiveau);
        nbVies_text = findViewById(R.id.textViewVies);
        points_text = findViewById(R.id.textViewPoints);
        infofond = findViewById(R.id.infofond);
        infoniveau = findViewById(R.id.infoniveau);
        inforecup = findViewById(R.id.inforecup);
        reprendreniveau = findViewById(R.id.reprendreniveau);
        chargerniveau = findViewById(R.id.chargerniveau);
        boutonStart = findViewById(R.id.start);
        scoreFinal = findViewById(R.id.scoreFinal);
        scoreMeilleur = findViewById(R.id.scoreMeilleur);
        chrono_text = findViewById(R.id.chronometre);

        time = 1;

        //Définition de la police :
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/edosz.ttf");
        points_text.setTypeface(font);
        boutonStart.setTypeface(font);
        difficulte_text.setTypeface(font);
        niveau_text.setTypeface(font);
        nbVies_text.setTypeface(font);
        scoreMeilleur.setTypeface(font);
        scoreFinal.setTypeface(font);
        chrono_text.setTypeface(font);
        inforecup.setTypeface(font);
        chargerniveau.setTypeface(font);
        reprendreniveau.setTypeface(font);

        //Définition des son :
        son_rouge = MediaPlayer.create(this, R.raw.sound81);
        son_bleu = MediaPlayer.create(this, R.raw.sound85);
        son_vert = MediaPlayer.create(this, R.raw.sound90);
        son_jaune = MediaPlayer.create(this, R.raw.sound92);
        son_orange = MediaPlayer.create(this, R.raw.sound93);
        son_rose = MediaPlayer.create(this, R.raw.sound96);
        son_turquoise = MediaPlayer.create(this, R.raw.sound97);
        son_marron = MediaPlayer.create(this, R.raw.sound99);
        son_gris = MediaPlayer.create(this, R.raw.sound100);
        son_violet = MediaPlayer.create(this, R.raw.sound110);

        //Initialisation du niveau à 1 à la création :
        niveau = 1;

        //Initialisation de la difficulté selon le mode choisit :
        Intent difficulte_mode = getIntent();
        difficulte = difficulte_mode.getIntExtra("int_value", 0);
        switchDifficulte();

        //Initialisation de base des boutons (sera ecrasé ensuite) :
        initialiseBoutons();

        //Temps d'animation quand l'user appuie :
        user_appuie.setDuration(200);

        //Fin des initialisations de base :
        Base();
    }

    //A chaque début de niveau :
    public void Base() {
        //Mise à jour de l'affichage du niveau et des vies :
        switchDifficulte();
        if(niveau<8)
        {
            niveau_text.setText("Niveau " + niveau);
            nbVies_text.setText(nbVies + " Vies");

            //Rend visible le bouton Start :
            boutonStart.setVisibility(View.VISIBLE);

            switchLvl();
        }
        else
        {
            FinDePartie();
        }
    }

    //Quand l'user clique sur Start :
    public void Lancer(View v) {

        //Rend le bouton Start invisible :
        boutonStart.setVisibility(View.INVISIBLE);

        //Lance un nouveau Thread pour gérer les pauses :
        new Thread(new Runnable() {
            @Override
            public void run() {

                switchBoutons();

                //Réinitialise la série et le nombre d'appuie à chaque nouveau niveau :
                nbAppuie = 0;
                la_serie.clear();
                //Et change la valeur max de l'aléatoire :
                nbBloc = niveau + 3;


                //Faire pour chaque appuie demandé par la difficulté :
                while (nbAppuie < nbAppuie_max) {

                    //Réinitialiser j et rendre les boutons indisponibles pendant la séquence :
                    j = 0;
                    setBoutonsDisable();

                    //Pause d'une seconde :
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                    //Pour la séquence en cours :
                    while (j < nbAppuie + 1) {
                        //Reinitialisé l'index qui sert à la synchronisation :
                        index = 0;

                        //Pause entre chaque animation :
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }

                        //Si ce n'est pas la première la série existante sans r'ajouter à la liste :
                        if (j > 0) {
                            serie_exist();
                        }
                        j++;
                    }

                    //Si c'est la première fois (j a été incrémenté), commence le nombre d'appuie minimum
                    //Correspondant à la difficulté choisie
                    if (j == 1) {
                        while (j < nbAppuie_min) {
                            serie_random();
                            j++;
                            nbAppuie++;
                        }
                    }

                    //Génère un nouvel appuie aléatoire :
                    nbAppuie++;

                    serie_random();

                    //Si mode chrono, lance le chronomètre :
                    if (difficulte==6)
                        modeChrono();

                    //Rend les boutons cliquables par le joueur :
                    setBoutonsEnable();



                    //Attend que le joueur ait fini de jouer son tour :
                    synchronized (attente){
                        while (index == 0) {
                            try {
                                attente.wait();
                            } catch (InterruptedException ie) {
                            }
                        }
                    }
            }

                //Niveau terminé, on passe au suivant !
                niveau++;

                //On rappelle la base pour faire réapparaitre le bouton Start et mettre à jour le niveau :
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Base();
                    }
                });
            }
        }).start();
    }

    //Quand l'user clique sur un bouton :
    public void verif(View bouton){

        switchSon(bouton);

        bouton.startAnimation(user_appuie);

        if (bouton.getId() == la_verif.get(0).getId()){
            la_verif.remove(0);

            //Si la verification est terminé, on passe à la suite :
            if (la_verif.isEmpty()){
                score = score + (poid*niveau);
                points_text.setText(score + " points");
                synchronized (attente){
                    index++;
                    attente.notify();
                }
            }
        }
        else {
            erreur();
        }

    }

    //Programmation du chrono :
    public void modeChrono()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chrono_text.setVisibility(View.VISIBLE);
            }
        });
        chrono = new Timer();
        time = nbAppuie + 2;
        chrono.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chrono_text.setText(Integer.toString(time));
                        if (time == 0)
                        {
                            if (la_verif.isEmpty() == false)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        chrono_text.setVisibility(View.INVISIBLE);
                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Pas assez rapide ! Vous perdez une vie", Toast.LENGTH_SHORT).show();
                                erreur();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chrono_text.setVisibility(View.INVISIBLE);
                                }
                            });
                            cancel();
                        }

                        if (la_verif.isEmpty() || chrono_erreur)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chrono_text.setVisibility(View.INVISIBLE);
                                }
                            });
                            cancel();
                        }
                        time--;
                    }
                });
            }
        }, 0, 1000);
    }

    //Quand l'utilisateur se trompe ou ne joue pas assez vite :
    public void erreur(){
        //Perd une vie :
        nbVies = nbVies-1;
        if (nbVies>0)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (time != 0)
                        Toast.makeText(getApplicationContext(), "Non ! Vous perdez une vie !", Toast.LENGTH_SHORT).show();
                }
            });
        else //Il n'y a plus de vie la partie est perdue :
        {
            GameOver();
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (nbVies == 1)
                    nbVies_text.setText("1 Vie");
                else
                    nbVies_text.setText(nbVies + " Vies");
            }
        });

        //Si mode chrono, lui indique qu'une erreur a été faite :
        if (difficulte==6)
            chrono_erreur = true;


        //Refais la série après 1 seconde:
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Redesactive les boutons
                setBoutonsDisable();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                j=1;
                la_verif.clear();
                while (j < nbAppuie+1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    serie_exist();
                    j++;
                }

                if (difficulte == 6) {
                    chrono_erreur = false;
                    modeChrono();
                }

                //Réactive les boutons :
                setBoutonsEnable();
            }
        }).start();
        }

    //Intialisation du nombre de boutons à appuyer selon la difficulté :
    public void switchDifficulte(){

        switch (difficulte) {
            case 1:
                nbAppuie_min = 1;
                nbAppuie_max = 7;
                poid = 1;
                nbVies = 2;
                difficulte_text.setText("Mode Facile");
                break;
            case 2:
                nbAppuie_min = 3;
                nbAppuie_max = 10;
                poid = 1.5;
                nbVies = 2;
                difficulte_text.setText("Mode Difficile");
                break;
            case 3:
                nbAppuie_min = 4;
                nbAppuie_max = 12;
                poid = 2;
                nbVies = 2;
                difficulte_text.setText("Mode Expert");
                break;
            case 4:
                nbAppuie_min = 1;
                nbAppuie_max = 1;
                poid = 0;
                nbVies = 2;
                difficulte_text.setText("Mode de test");
                break;
            case 5:
                nbAppuie_min = 12;
                nbAppuie_max = 12;
                poid = 15;
                nbVies = 3;
                difficulte_text.setText("Mode Spécial");
                break;
            case 6:
                nbAppuie_min = 1;
                nbAppuie_max = 8;
                poid = 1.5;
                nbVies = 3;
                difficulte_text.setText("Mode Chrono");
                break;
            default:
                break;
        }
    }

    //Disposition du nombre de boutons selon le niveau :
    public void switchLvl(){
        switch (niveau) {
            case 1:
                findViewById(R.id.vue_niveau1).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.vue_niveau1).setVisibility(View.GONE);
                findViewById(R.id.vue_niveau2).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.vue_niveau2).setVisibility(View.GONE);
                findViewById(R.id.vue_niveau3).setVisibility(View.VISIBLE);
                break;
            case 4:
                findViewById(R.id.vue_niveau3).setVisibility(View.GONE);
                findViewById(R.id.vue_niveau4).setVisibility(View.VISIBLE);
                break;
            case 5:
                findViewById(R.id.vue_niveau4).setVisibility(View.GONE);
                findViewById(R.id.vue_niveau5).setVisibility(View.VISIBLE);
                break;
            case 6:
                findViewById(R.id.vue_niveau5).setVisibility(View.GONE);
                findViewById(R.id.vue_niveau6).setVisibility(View.VISIBLE);
                break;
            case 7:
                findViewById(R.id.vue_niveau6).setVisibility(View.GONE);
                findViewById(R.id.vue_niveau7).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    //Déclaration des boutons (sera écrasé) :
    public void initialiseBoutons(){
        boutonRouge = findViewById(R.id.rouge7);
        boutonBleu = findViewById(R.id.bleu7);
        boutonVert = findViewById(R.id.vert7);
        boutonJaune = findViewById(R.id.jaune7);
        boutonOrange = findViewById(R.id.orange7);
        boutonRose = findViewById(R.id.orange7);
        boutonTurquoise = findViewById(R.id.turquoise7);
        boutonMarron = findViewById(R.id.marron7);
        boutonGris = findViewById(R.id.gris7);
        boutonViolet = findViewById(R.id.violet7);
    }

    //Changement des ID selon le niveau :
    public void switchBoutons(){
        switch (niveau) {
            case 1:
                boutonRouge = findViewById(R.id.rouge);
                boutonBleu = findViewById(R.id.bleu);
                boutonVert = findViewById(R.id.vert);
                boutonJaune = findViewById(R.id.jaune);
                break;
            case 2:
                boutonRouge = findViewById(R.id.rouge2);
                boutonBleu = findViewById(R.id.bleu2);
                boutonVert = findViewById(R.id.vert2);
                boutonJaune = findViewById(R.id.jaune2);
                boutonOrange = findViewById(R.id.orange2);
                break;
            case 3:
                boutonRouge = findViewById(R.id.rouge3);
                boutonBleu = findViewById(R.id.bleu3);
                boutonVert = findViewById(R.id.vert3);
                boutonJaune = findViewById(R.id.jaune3);
                boutonOrange = findViewById(R.id.orange3);
                boutonRose = findViewById(R.id.rose3);
                break;
            case 4:
                boutonRouge = findViewById(R.id.rouge4);
                boutonBleu = findViewById(R.id.bleu4);
                boutonVert = findViewById(R.id.vert4);
                boutonJaune = findViewById(R.id.jaune4);
                boutonOrange = findViewById(R.id.orange4);
                boutonRose = findViewById(R.id.rose4);
                boutonTurquoise = findViewById(R.id.turquoise4);
                break;
            case 5:
                boutonRouge = findViewById(R.id.rouge5);
                boutonBleu = findViewById(R.id.bleu5);
                boutonVert = findViewById(R.id.vert5);
                boutonJaune = findViewById(R.id.jaune5);
                boutonOrange = findViewById(R.id.orange5);
                boutonRose = findViewById(R.id.rose5);
                boutonTurquoise = findViewById(R.id.turquoise5);
                boutonMarron = findViewById(R.id.marron5);
                break;
            case 6:
                boutonRouge = findViewById(R.id.rouge6);
                boutonBleu = findViewById(R.id.bleu6);
                boutonVert = findViewById(R.id.vert6);
                boutonJaune = findViewById(R.id.jaune6);
                boutonOrange = findViewById(R.id.orange6);
                boutonRose = findViewById(R.id.rose6);
                boutonTurquoise = findViewById(R.id.turquoise6);
                boutonMarron = findViewById(R.id.marron6);
                boutonGris = findViewById(R.id.gris6);
                break;
            case 7:
                boutonRouge = findViewById(R.id.rouge7);
                boutonBleu = findViewById(R.id.bleu7);
                boutonVert = findViewById(R.id.vert7);
                boutonJaune = findViewById(R.id.jaune7);
                boutonOrange = findViewById(R.id.orange7);
                boutonRose = findViewById(R.id.rose7);
                boutonTurquoise = findViewById(R.id.turquoise7);
                boutonMarron = findViewById(R.id.marron7);
                boutonGris = findViewById(R.id.gris7);
                boutonViolet = findViewById(R.id.violet7);
                break;
            default:
                break;
        }
    }

    //Rend les boutons incliquables pendant la séquence :
    public void setBoutonsDisable(){
        boutonRouge.setClickable(false);
        boutonBleu.setClickable(false);
        boutonVert.setClickable(false);
        boutonJaune.setClickable(false);
        boutonOrange.setClickable(false);
        boutonRose.setClickable(false);
        boutonTurquoise.setClickable(false);
        boutonMarron.setClickable(false);
        boutonGris.setClickable(false);
        boutonViolet.setClickable(false);
    }

    //Rend les boutons de nouveau disponibles :
    public void setBoutonsEnable(){
        boutonRouge.setClickable(true);
        boutonBleu.setClickable(true);
        boutonVert.setClickable(true);
        boutonJaune.setClickable(true);
        boutonOrange.setClickable(true);
        boutonRose.setClickable(true);
        boutonTurquoise.setClickable(true);
        boutonMarron.setClickable(true);
        boutonGris.setClickable(true);
        boutonViolet.setClickable(true);
    }

    //Répète la série existante :
    public void serie_exist(){
        l = la_serie.get(j - 1);
        //Réinitialise la verification :

        switch (l) {
            case 0:
                appuie(boutonRouge);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_rouge(boutonRouge);
                la_verif.add(boutonRouge);
                break;

            case 1:
                appuie(boutonBleu);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_bleu(boutonBleu);
                la_verif.add(boutonBleu);
                break;

            case 2:
                appuie(boutonVert);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_vert(boutonVert);
                la_verif.add(boutonVert);
                break;

            case 3:
                appuie(boutonJaune);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_jaune(boutonJaune);
                la_verif.add(boutonJaune);
                break;


            case 4:
                appuie(boutonOrange);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_orange(boutonOrange);
                la_verif.add(boutonOrange);
                break;

            case 5:
                appuie(boutonRose);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_rose(boutonRose);
                la_verif.add(boutonRose);
                break;

            case 6:
                appuie(boutonTurquoise);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_turquoise(boutonTurquoise);
                la_verif.add(boutonTurquoise);
                break;

            case 7:
                appuie(boutonMarron);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_marron(boutonMarron);
                la_verif.add(boutonMarron);
                break;

            case 8:
                appuie(boutonGris);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_gris(boutonGris);
                la_verif.add(boutonGris);
                break;

            case 9:
                appuie(boutonViolet);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_violet(boutonViolet);
                la_verif.add(boutonViolet);
                break;

            default:
                break;
        }
    }

    //Ajoute un appuie à la série :
    public void serie_random(){
        k = alea.nextInt(nbBloc);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        switch (k) {
            case 0:
                appuie(boutonRouge);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_rouge(boutonRouge);
                la_serie.add(0);
                la_verif.add(boutonRouge);
                break;

            case 1:
                appuie(boutonBleu);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_bleu(boutonBleu);
                la_serie.add(1);
                la_verif.add(boutonBleu);
                break;

            case 2:
                appuie(boutonVert);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_vert(boutonVert);
                la_serie.add(2);
                la_verif.add(boutonVert);
                break;

            case 3:
                appuie(boutonJaune);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_jaune(boutonJaune);
                la_serie.add(3);
                la_verif.add(boutonJaune);
                break;

            case 4:
                appuie(boutonOrange);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_orange(boutonOrange);
                la_serie.add(4);
                la_verif.add(boutonOrange);
                break;

            case 5:
                appuie(boutonRose);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_rose(boutonRose);
                la_serie.add(5);
                la_verif.add(boutonRose);
                break;

            case 6:
                appuie(boutonTurquoise);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_turquoise(boutonTurquoise);
                la_serie.add(6);
                la_verif.add(boutonTurquoise);
                break;

            case 7:
                appuie(boutonMarron);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_marron(boutonMarron);
                la_serie.add(7);
                la_verif.add(boutonMarron);
                break;

            case 8:
                appuie(boutonGris);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_gris(boutonGris);
                la_serie.add(8);
                la_verif.add(boutonGris);
                break;

            case 9:
                appuie(boutonViolet);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }
                allume_violet(boutonViolet);
                la_serie.add(9);
                la_verif.add(boutonViolet);
                break;

            default:
                break;
        }
    }

    //Partie perdue :
    public void GameOver(){
        InterfaceFin();

        //Rend les vues GameOver et TryAgain disponible
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.gameover).setVisibility(View.VISIBLE);
                findViewById(R.id.tryagain).setVisibility(View.VISIBLE);
            }
        });

    }

    //Partie gagnée :
    public void FinDePartie(){
        //Message de félicitations
        findViewById(R.id.congrats).setVisibility(View.VISIBLE);

        switch (difficulte)
        {
            case 1:
                scoreUser.child("faccomplete").setValue(true);
                break;
            case 2:
                scoreUser.child("diffcomplete").setValue(true);
                break;
            case 3:
                scoreUser.child("expcomplete").setValue(true);
                break;
            case 5:
                scoreUser.child("specomplete").setValue(true);
                break;
            case 6:
                scoreUser.child("chrcomplete").setValue(true);
                break;
            default:
                break;
        }

        InterfaceFin();
    }

    //Redémarrage de partie à partir du même niveau
    public void tryagain(View view){

        //Pense à réinitialiser les listes
        la_verif.clear();
        la_serie.clear();

        //Remise des scores à zéros pour plus d'équilibre
        score = 0;
        points_text.setText("0 points");

        InterfaceJeu();

        Base();
    }

    //Mise en place des boutons et de l'interface de jeu :
    public void InterfaceJeu(){

        //Réactive les boutons et retire l'interface de fin
        boutonRouge.setVisibility(View.VISIBLE);
        boutonBleu.setVisibility(View.VISIBLE);
        boutonVert.setVisibility(View.VISIBLE);
        boutonJaune.setVisibility(View.VISIBLE);
        boutonOrange.setVisibility(View.VISIBLE);
        boutonRose.setVisibility(View.VISIBLE);
        boutonTurquoise.setVisibility(View.VISIBLE);
        boutonMarron.setVisibility(View.VISIBLE);
        boutonGris.setVisibility(View.VISIBLE);
        boutonViolet.setVisibility(View.VISIBLE);
        scoreMeilleur.setVisibility(View.INVISIBLE);
        scoreFinal.setVisibility(View.INVISIBLE);
        points_text.setVisibility(View.VISIBLE);
        nbVies_text.setVisibility(View.VISIBLE);
        difficulte_text.setVisibility(View.VISIBLE);
        niveau_text.setVisibility(View.VISIBLE);
        inforecup.setVisibility(View.INVISIBLE);
        infoniveau.setVisibility(View.INVISIBLE);
        infofond.setVisibility(View.INVISIBLE);
        chargerniveau.setVisibility(View.INVISIBLE);
        reprendreniveau.setVisibility(View.INVISIBLE);
        findViewById(R.id.gameover).setVisibility(View.GONE);
        findViewById(R.id.tryagain).setVisibility(View.GONE);
        findViewById(R.id.congrats).setVisibility(View.GONE);
        findViewById(R.id.menu).setVisibility(View.GONE);
    }

    //Retrait des boutons, passage en interface de fin :
    public void InterfaceFin(){
        MajScore();

        //Désactive tous les boutons pour donner place à un écran de fin
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boutonStart.setVisibility(View.GONE);
                boutonRouge.setVisibility(View.GONE);
                boutonBleu.setVisibility(View.GONE);
                boutonVert.setVisibility(View.GONE);
                boutonJaune.setVisibility(View.GONE);
                boutonOrange.setVisibility(View.GONE);
                boutonRose.setVisibility(View.GONE);
                boutonTurquoise.setVisibility(View.GONE);
                boutonMarron.setVisibility(View.GONE);
                boutonGris.setVisibility(View.GONE);
                boutonViolet.setVisibility(View.GONE);
                points_text.setVisibility(View.GONE);
                nbVies_text.setVisibility(View.GONE);
                difficulte_text.setVisibility(View.GONE);
                niveau_text.setVisibility(View.GONE);
                findViewById(R.id.menu).setVisibility(View.VISIBLE);
                scoreMeilleur.setVisibility(View.VISIBLE);
                scoreFinal.setVisibility(View.VISIBLE);
                scoreFinal.setText("Score : " + score);
                chrono_text.setVisibility(View.INVISIBLE);
            }
        });
    }

    //Mise à jour des scores :
    public void MajScore(){
        scoreUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    double myBestScore = dataSnapshot.child("bestScore").getValue(double.class);
                    scoreMeilleur.setText("Record : " + dataSnapshot.child("bestScore").getValue());

                    if (score>myBestScore) {
                        scoreUser.child("bestScore").setValue(score);
                        scoreUser.child("bestScoreNeg").setValue(0-score);}
                }
                else {
                    scoreUser.child("bestScore").setValue(score);
                    scoreUser.child("bestScoreNeg").setValue(0-score);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Programmation du son :
    public void switchSon(View b){
        switch (b.getId()){
            case R.id.rouge:
                son_rouge.start();
                break;

            case R.id.rouge2:
                son_rouge.start();
                break;

            case R.id.rouge3:
                son_rouge.start();
                break;

            case R.id.rouge4:
                son_rouge.start();
                break;

            case R.id.rouge5:
                son_rouge.start();
                break;

            case R.id.rouge6:
                son_rouge.start();
                break;

            case R.id.rouge7:
                son_rouge.start();
                break;

            case R.id.bleu:
                son_bleu.start();
                break;

            case R.id.bleu2:
                son_bleu.start();
                break;

            case R.id.bleu3:
                son_bleu.start();
                break;

            case R.id.bleu4:
                son_bleu.start();
                break;

            case R.id.bleu5:
                son_bleu.start();
                break;

            case R.id.bleu6:
                son_bleu.start();
                break;

            case R.id.bleu7:
                son_bleu.start();
                break;

            case R.id.vert:
                son_vert.start();
                break;

            case R.id.vert2:
                son_vert.start();
                break;

            case R.id.vert3:
                son_vert.start();
                break;

            case R.id.vert4:
                son_vert.start();
                break;

            case R.id.vert5:
                son_vert.start();
                break;

            case R.id.vert6:
                son_vert.start();
                break;

            case R.id.vert7:
                son_vert.start();
                break;

            case R.id.jaune:
                son_jaune.start();
                break;

            case R.id.jaune2:
                son_jaune.start();
                break;

            case R.id.jaune3:
                son_jaune.start();
                break;

            case R.id.jaune4:
                son_jaune.start();
                break;

            case R.id.jaune5:
                son_jaune.start();
                break;

            case R.id.jaune6:
                son_jaune.start();
                break;

            case R.id.jaune7:
                son_jaune.start();
                break;

            case R.id.orange2:
                son_orange.start();
                break;

            case R.id.orange3:
                son_orange.start();
                break;

            case R.id.orange4:
                son_orange.start();
                break;

            case R.id.orange5:
                son_orange.start();
                break;

            case R.id.orange6:
                son_orange.start();
                break;

            case R.id.orange7:
                son_orange.start();
                break;

            case R.id.rose3:
                son_rose.start();
                break;

            case R.id.rose4:
                son_rose.start();
                break;

            case R.id.rose5:
                son_rose.start();
                break;

            case R.id.rose6:
                son_rose.start();
                break;

            case R.id.rose7:
                son_rose.start();
                break;

            case R.id.turquoise4:
                son_turquoise.start();
                break;

            case R.id.turquoise5:
                son_turquoise.start();
                break;

            case R.id.turquoise6:
                son_turquoise.start();
                break;

            case R.id.turquoise7:
                son_turquoise.start();
                break;

            case R.id.marron5:
                son_marron.start();
                break;

            case R.id.marron6:
                son_marron.start();
                break;

            case R.id.marron7:
                son_marron.start();
                break;

            case R.id.gris6:
                son_gris.start();
                break;

            case R.id.gris7:
                son_gris.start();
                break;

            case R.id.violet7:
                son_violet.start();
                break;

            default:
                break;
        }
    }

    //Restauration du dernier niveau atteint :
    public void chargerPartie(){
        scoreUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (difficulte)
                {
                    case 1:
                        if (dataSnapshot.child("Facile").getValue(Integer.class) != null)
                            if (dataSnapshot.child("Facile").getValue(Integer.class) > 1)
                                InterfaceChargement(dataSnapshot.child("Facile").getValue(Integer.class));
                        break;
                    case 2:
                        if (dataSnapshot.child("Difficile").getValue(Integer.class) != null)
                            if (dataSnapshot.child("Difficile").getValue(Integer.class) > 1)
                                InterfaceChargement(dataSnapshot.child("Difficile").getValue(Integer.class));
                        break;
                    case 3:
                        if (dataSnapshot.child("Expert").getValue(Integer.class) != null)
                            if (dataSnapshot.child("Expert").getValue(Integer.class) > 1)
                                InterfaceChargement(dataSnapshot.child("Expert").getValue(Integer.class));
                        break;
                    case 4:
                        if (dataSnapshot.child("Test").getValue(Integer.class) != null)
                            if (dataSnapshot.child("Test").getValue(Integer.class) > 1)
                                InterfaceChargement(dataSnapshot.child("Test").getValue(Integer.class));
                        break;
                    case 5:
                        if (dataSnapshot.child("Special").getValue(Integer.class) != null)
                            if (dataSnapshot.child("Special").getValue(Integer.class) > 1)
                                InterfaceChargement(dataSnapshot.child("Special").getValue(Integer.class));
                        break;
                    case 6:
                        if (dataSnapshot.child("Chrono").getValue(Integer.class) != null)
                            if (dataSnapshot.child("Chrono").getValue(Integer.class) > 1)
                                InterfaceChargement(    dataSnapshot.child("Chrono").getValue(Integer.class));
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Interface pour reprendre au niveau enregistrer ou non :
    public void InterfaceChargement(final Integer n)
    {
        InterfaceFin();
        scoreMeilleur.setVisibility(View.INVISIBLE);
        scoreFinal.setVisibility(View.INVISIBLE);

        findViewById(R.id.vue_niveau1).setVisibility(View.GONE);
        inforecup.setVisibility(View.VISIBLE);
        infoniveau.setVisibility(View.VISIBLE);
        infofond.setVisibility(View.VISIBLE);
        chargerniveau.setVisibility(View.VISIBLE);
        reprendreniveau.setVisibility(View.VISIBLE);
        difficulte_text.setVisibility(View.VISIBLE);

        inforecup.setText("Vous avez une sauvegarde pour ce mode de jeu, souhaitez vous retourner au niveau " + n + " ?");
        reprendreniveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                niveau = 1;
                InterfaceJeu();
                Base();
            }
        });
        chargerniveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                niveau = n;
                InterfaceJeu();
                Base();
            }
        });
    }

    //Retour au menu :
    public void menu(View view){
        Intent back = new Intent(Jeu.this, MainActivity.class);
        startActivity(back);
    }

    //Pression d'un bouton :
    public void appuie(final Button b) {
        switchSon(b);
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_blanc);
                          }
                      }
        );

    }


    //Retour à sa couleur initiale :
    public void allume_rouge(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_rouge);
                          }
                      }
        );

    }

    public void allume_bleu(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_bleu);
                          }
                      }
        );

    }

    public void allume_vert(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_vert);
                          }
                      }
        );
    }

    public void allume_jaune(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_jaune);
                          }
                      }
        );
    }

    public void allume_orange(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_orange);
                          }
                      }
        );
    }

    public void allume_rose(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_rose);
                          }
                      }
        );
    }

    public void allume_turquoise(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_turquoise);
                          }
                      }
        );
    }

    public void allume_marron(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_marron);
                          }
                      }
        );
    }

    public void allume_gris(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_gris);
                          }
                      }
        );
    }

    public void allume_violet(final Button b) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              b.setBackgroundResource(R.drawable.custom_violet);
                          }
                      }
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        MajScore();
        chrono_erreur = true;
        switch ((difficulte))
        {
            case 1:
                scoreUser.child("Facile").setValue(niveau);
                break;
            case 2:
                scoreUser.child("Difficile").setValue(niveau);
                break;
            case 3:
                scoreUser.child("Expert").setValue(niveau);
                break;
            case 4:
                scoreUser.child("Test").setValue(niveau);
                break;
            case 5:
                scoreUser.child("Special").setValue(niveau);
                break;
            case 6:
                scoreUser.child("Chrono").setValue(niveau);
                break;
            default:
                break;
        }
    }
}

