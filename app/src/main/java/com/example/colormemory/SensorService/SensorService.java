package com.example.colormemory.SensorService;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.example.colormemory.NotifManager;
import com.example.colormemory.Score.Joueur;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SensorService extends Service {

/*    public static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SensorService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        startTimer();
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {

        timer = new Timer();

        Log.i("Timer", "Start");
        initializeTimerTask();

        timer.schedule(timerTask, 1000, 5000); //
    }


    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {

                Log.i("Switch", "OK");
            }
        };
    }*/

    private ArrayList<Joueur> listJoueur = new ArrayList<>();
    private ArrayList<Joueur> listJoueur2 = new ArrayList<>();
    private Integer rang, rang2;
    private boolean userTop, userTop2, switchtest;
    private FirebaseAuth mAuth;
    private String userID;

    @Override
    public void onCreate() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public SensorService(Context applicationContext) {
        super();
    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("Service", "Start");
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service", "Destroy");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {

        timer = new Timer();

        Log.i("Timer", "Start");
        initializeTimerTask();

        timer.schedule(timerTask, 1000, 5000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {

                Log.i("Switch", "OK");
                switchtest = !switchtest;

                if ((mAuth.getCurrentUser() != null)&& userID != null) {
                    if (switchtest)
                        Test();
                    else
                        Test2();}
            }
        };
    }

    public void Test() {

        //Toutes les 10 secondes que l'app soit ouverte ou non

        Query query = FirebaseDatabase.getInstance().getReference("ID")
                .orderByChild("bestScoreNeg")
                .limitToFirst(10);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listJoueur.clear();
                userTop = false;
                Integer i = 0;

                for (DataSnapshot joueurSnap : dataSnapshot.getChildren()) {
                    String idJoueur = joueurSnap.getKey();
                    String nomJoueur = joueurSnap.child("pseudo").getValue(String.class);
                    String mailJoueur = joueurSnap.child("mail").getValue(String.class);
                    double scoreJoueur = joueurSnap.child("bestScore").getValue(double.class);
                    i++;

                    Joueur joueur = new Joueur(idJoueur, nomJoueur, mailJoueur, scoreJoueur);

                    listJoueur.add(joueur);


                    if (userID.equals(idJoueur)) {
                        userTop = true;
                        rang = i;
                    }
                }

                if ((rang != null) && (rang2 != null))
                    if ((rang > rang2) || (!userTop && userTop2))
                        createNotification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Test2() {

                //Toutes les 10 secondes que l'app soit ouverte ou non

                Query query2 = FirebaseDatabase.getInstance().getReference("ID")
                        .orderByChild("bestScoreNeg")
                        .limitToFirst(10);


                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        listJoueur2.clear();
                        userTop2 = false;
                        Integer i = 0;

                        for (DataSnapshot joueurSnap : dataSnapshot.getChildren()) {
                            String idJoueur = joueurSnap.getKey();
                            String nomJoueur = joueurSnap.child("pseudo").getValue(String.class);
                            String mailJoueur = joueurSnap.child("mail").getValue(String.class);
                            double scoreJoueur = joueurSnap.child("bestScore").getValue(double.class);
                            i++;

                            Joueur joueur2 = new Joueur(idJoueur, nomJoueur, mailJoueur, scoreJoueur);

                            listJoueur2.add(joueur2);

                            if (userID.equals(idJoueur)) {
                                userTop2 = true;
                                rang2 = i;
                            }
                        }

                        if ((rang!=null)&&(rang2!=null))
                            if ((rang2 > rang) || (userTop && !userTop2))
                                createNotification();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void createNotification(){
        NotifManager maNotif = new NotifManager(this, "channel Name");
        maNotif.sendNotification("Vous êtes descendu dans le classement !", "Quelqu'un vous a dépassé !", 1);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}