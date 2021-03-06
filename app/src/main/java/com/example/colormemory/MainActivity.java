package com.example.colormemory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colormemory.AccountActivity.LoginActivity;
import com.example.colormemory.AccountActivity.SignupActivity;
import com.example.colormemory.Score.HightScore;
import com.example.colormemory.SensorService.SensorService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

//julliard.anthony@gmail.com
//aby_t@esiee-amiens.fr
//pillon.tibo@gmail.com

public class MainActivity extends AppCompatActivity {

    //Pour faire tourner l'app en tache de fond :
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    private DatabaseReference scoreUser;

    private String userID, pseudo, lang;

    private Button changePassword;

    private TextView email, score, text_chrono;

    private ImageView cch_fac, cch_dif, cch_exp, cch_spe, cch_chr;

    private EditText newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button facile, difficile, expert, special, chrono, test;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loadLocale();

        //Pour faire tourner l'app en tache de fond :
        ctx = this;
        setContentView(R.layout.activity_main);
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }

        //SensorService.enqueueWork(this, new Intent());


        auth = FirebaseAuth.getInstance();
        email = (TextView) findViewById(R.id.useremail);

        final Intent getPseudo = getIntent();
        pseudo = getPseudo.getStringExtra("Pseudo");

        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
            scoreUser = FirebaseDatabase.getInstance().getReference().child("ID").child(userID);
        }
        score = findViewById(R.id.score);


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        facile = findViewById(R.id.facile);
        difficile = findViewById(R.id.difficile);
        expert = findViewById(R.id.expert);
        test = findViewById(R.id.test);
        special = findViewById(R.id.special);
        chrono = findViewById(R.id.chrono);
        text_chrono = findViewById(R.id.text_chrono);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/edosz.ttf");
        facile.setTypeface(font);
        difficile.setTypeface(font);
        expert.setTypeface(font);
        test.setTypeface(font);
        special.setTypeface(font);
        chrono.setTypeface(font);
        text_chrono.setTypeface(font);
        email.setTypeface(font);
        score.setTypeface(font);

        cch_fac = findViewById(R.id.coche_facile);
        cch_dif = findViewById(R.id.coche_difficile);
        cch_exp = findViewById(R.id.coche_expert);
        cch_spe = findViewById(R.id.coche_special);
        cch_chr = findViewById(R.id.coche_chrono);


        facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(MainActivity.this, Jeu.class);
                lance_jeu.putExtra("int_value", 1);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
            }
        });

        difficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(MainActivity.this, Jeu.class);
                lance_jeu.putExtra("int_value", 2);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(MainActivity.this, Jeu.class);
                lance_jeu.putExtra("int_value", 3);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_bot,R.anim.slide_out_bot);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(MainActivity.this, Jeu.class);
                lance_jeu.putExtra("int_value", 4);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
            }
        });

        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(MainActivity.this, Jeu.class);
                lance_jeu.putExtra("int_value", 5);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(MainActivity.this, Jeu.class);
                lance_jeu.putExtra("int_value", 6);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });


        if (auth.getCurrentUser() != null) {
            scoreUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    score.setText("Record : " + dataSnapshot.child("bestScore").getValue());

                    if (dataSnapshot.child("pseudo").getValue() != null)
                        email.setText("Pseudo : " + dataSnapshot.child("pseudo").getValue());

                    if ((dataSnapshot.child("pseudo").getValue(String.class) == null) && (pseudo != null))
                        scoreUser.child("pseudo").setValue(pseudo);

                    if (dataSnapshot.child("mail").getValue(String.class) == null)
                        scoreUser.child("mail").setValue(user.getEmail());

                    if (dataSnapshot.child("bestScore").getValue(double.class) == null)
                    {scoreUser.child("bestScore").setValue(0);
                    scoreUser.child("bestScoreNeg").setValue(0);}

                    if (dataSnapshot.child("testmode").getValue(boolean.class) != null)
                        test.setVisibility(View.VISIBLE);
                    else
                        test.setVisibility(View.INVISIBLE);

                    if (dataSnapshot.child("faccomplete").getValue() != null)
                        cch_fac.setVisibility(View.VISIBLE);
                    else
                        cch_fac.setVisibility(View.INVISIBLE);

                    if (dataSnapshot.child("diffcomplete").getValue() != null) {
                        cch_dif.setVisibility(View.VISIBLE);
                        special.setVisibility(View.VISIBLE);
                    } else {
                        cch_dif.setVisibility(View.INVISIBLE);
                        special.setVisibility(View.INVISIBLE);
                    }

                    if (dataSnapshot.child("expcomplete").getValue() != null)
                        cch_exp.setVisibility(View.VISIBLE);
                    else
                        cch_exp.setVisibility(View.INVISIBLE);

                    if (dataSnapshot.child("specomplete").getValue() != null)
                        cch_spe.setVisibility(View.VISIBLE);
                    else
                        cch_spe.setVisibility(View.INVISIBLE);

                    if (dataSnapshot.child("chrcomplete").getValue() != null)
                        cch_chr.setVisibility(View.VISIBLE);
                    else
                        cch_chr.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        changePassword = (Button) findViewById(R.id.changePass);

        newPassword = (EditText) findViewById(R.id.newPassword);

        newPassword.setVisibility(View.GONE);

        changePassword.setVisibility(View.GONE);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError(getString(R.string.bad_password));
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, R.string.mdp_change, Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(MainActivity.this, R.string.mdp_changen, Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError(getString(R.string.prompt_password));
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deconnexion)
        {
            signOut();
        }
        else if (item.getItemId() == R.id.suppression)
        {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null) {
                if (scoreUser != null)
                    scoreUser.removeValue();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, R.string.prof_supr, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                    finish();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.prof_suprn, Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }
        else if (item.getItemId() == R.id.change_mdp)
        {
            newPassword.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.VISIBLE);
        }
        else if (item.getItemId() == R.id.language)
        {
            ChangeLanguage();
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }


    };


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    public void signOut() {
        auth.signOut();


        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    private void ChangeLanguage(){
        final String[] listItems = {"Français", "Belge", "English", "Español"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        setLocale("fr-rFR");
                        recreate();
                        break;
                    case 1:
                        setLocale("fr-rBE");
                        recreate();
                        break;
                    case 2:
                        setLocale("en");
                        recreate();
                        break;
                    case 3:
                        setLocale("es");
                        recreate();
                        break;
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
    }

    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    public void HighScore(View view){
        Intent score = new Intent(MainActivity.this, HightScore.class);
        startActivity(score);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
