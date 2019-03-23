package com.example.colormemory;

public class Test {

    /*




    package com.example.colormemory;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class Jeu extends AppCompatActivity {


    //Déclaration
    private TextView points, start, difficulte_text, niveau_text;
    private ArrayList<Integer> la_serie = new ArrayList<>();
    private ArrayList<Button> la_verif = new ArrayList<>();
    private Integer nbBloc, niveau, difficulte, nbAppuie_min, nbAppuie_max, nbVies, index;
    private AlphaAnimation user_appuie = new AlphaAnimation(0f, 1f);
    private final Object attente = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        niveau = 1;

        Intent difficulte_mode = getIntent();
        difficulte_text = findViewById(R.id.textViewDifficulte);
        niveau_text = findViewById(R.id.textViewNiveau);

        difficulte = difficulte_mode.getIntExtra("int_value", 0);
        switch (difficulte) {
            case 1:
                nbAppuie_min = 1;
                nbAppuie_max = 7;
                difficulte_text.setText("Mode Facile");
                break;
            case 2:
                nbAppuie_min = 3;
                nbAppuie_max = 10;
                difficulte_text.setText("Mode Difficile");
                break;
            case 3:
                nbAppuie_min = 4;
                nbAppuie_max = 12;
                difficulte_text.setText("Mode Expert");
                break;
            default:
                break;
        }


        points = findViewById(R.id.points);
        start = findViewById(R.id.start);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/edosz.ttf");
        points.setTypeface(font);
        start.setTypeface(font);
        difficulte_text.setTypeface(font);
        niveau_text.setTypeface(font);

        user_appuie.setDuration(500);

        Base(niveau);
    }


    public void Base(Integer lvl) {

        niveau_text.setText("Niveau " + lvl);
        start.setVisibility(View.VISIBLE);

        switch (lvl) {
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


    public void Lancer(View v) {

        start.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Button boutonRouge, boutonBleu, boutonVert, boutonJaune, boutonOrange, boutonRose, boutonTurquoise, boutonMarron, boutonGris, boutonViolet;


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


                Integer j, k, l, nbAppuie = 0;
                Random alea = new Random();

                nbBloc = niveau + 3;
                la_serie.clear();


                while (nbAppuie < nbAppuie_max) {
                    j = 0;

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

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }


                    while (j < nbAppuie + 1) {
                        index = 0;

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {

                        }
                        if (j > 0) {
                            l = la_serie.get(j - 1);
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
                        j++;
                    }

                    if (j == 1) {
                        while (j < nbAppuie_min) {
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

                            nbAppuie++;
                            j++;
                        }
                    }

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

                    nbAppuie++;

                    synchronized (attente){
                        while (index == 0) {
                            try {
                                attente.wait();
                            } catch (InterruptedException ie) {
                            }
                        }
                    }

                }

                niveau++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Base(niveau);
                    }
                });
            }
        }).start();
    }

    public void verif(View bouton){

        bouton.startAnimation(user_appuie);
        if (bouton.getId() == la_verif.get(0).getId()){
            la_verif.remove(0);
        }
        else {
            nbVies--;
            la_verif.clear();
            }

        if (la_verif.isEmpty()){
            synchronized (attente){
                index++;
                attente.notify();
            }
        }
    }

    public void appuie(Button b) {
        b.setBackgroundResource(R.drawable.custom_blanc);
    }

    public void allume_rouge(Button b) {
        b.setBackgroundResource(R.drawable.custom_rouge);
    }

    public void allume_bleu(Button b) {
        b.setBackgroundResource(R.drawable.custom_bleu);
    }

    public void allume_vert(Button b) {
        b.setBackgroundResource(R.drawable.custom_vert);
    }

    public void allume_jaune(Button b) {
        b.setBackgroundResource(R.drawable.custom_jaune);
    }

    public void allume_orange(Button b) {
        b.setBackgroundResource(R.drawable.custom_orange);
    }

    public void allume_rose(Button b) {
        b.setBackgroundResource(R.drawable.custom_rose);
    }

    public void allume_turquoise(Button b) {
        b.setBackgroundResource(R.drawable.custom_turquoise);
    }

    public void allume_marron(Button b) {
        b.setBackgroundResource(R.drawable.custom_marron);
    }

    public void allume_gris(Button b) {
        b.setBackgroundResource(R.drawable.custom_gris);
    }

    public void allume_violet(Button b) {
        b.setBackgroundResource(R.drawable.custom_violet);
    }
}





     */


}
