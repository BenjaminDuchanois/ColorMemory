package com.example.colormemory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colormemory.AccountActivity.LoginActivity;
import com.example.colormemory.AccountActivity.SignupActivity;
import com.example.colormemory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference scoreUser;
    private String userID;

    private Button btnChangePassword, btnRemoveUser,
            changePassword, remove, signOut;
    private TextView email, score;

    private ImageView cch_fac, cch_dif, cch_exp, cch_spe;

    private EditText oldEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button facile, difficile, expert, special, test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        email = (TextView) findViewById(R.id.useremail);

        userID = auth.getCurrentUser().getUid();
        scoreUser = FirebaseDatabase.getInstance().getReference().child("ID").child(userID);
        score = findViewById(R.id.score);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
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
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/edosz.ttf");
        facile.setTypeface(font);
        difficile.setTypeface(font);
        expert.setTypeface(font);
        test.setTypeface(font);
        special.setTypeface(font);

        cch_fac = findViewById(R.id.coche_facile);
        cch_dif = findViewById(R.id.coche_difficile);
        cch_exp = findViewById(R.id.coche_expert);
        cch_spe = findViewById(R.id.coche_special);


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


        scoreUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                score.setText("Record : " + dataSnapshot.child("bestScore").getValue());

                if (dataSnapshot.child("mail").getValue(String.class) == null)
                    scoreUser.child("mail").setValue(user.getEmail());
                if (dataSnapshot.child("bestScore").getValue(double.class) == null)
                    scoreUser.child("bestScore").setValue(0);

                if (dataSnapshot.child("testmode").getValue(boolean.class) != null)
                    test.setVisibility(View.VISIBLE);
                else
                    test.setVisibility(View.INVISIBLE);

                if (dataSnapshot.child("faccomplete").getValue() != null)
                    cch_fac.setVisibility(View.VISIBLE);
                else
                    cch_fac.setVisibility(View.INVISIBLE);

                if (dataSnapshot.child("diffcomplete").getValue() != null)
                {   cch_dif.setVisibility(View.VISIBLE);
                    special.setVisibility(View.VISIBLE);}
                else
                {   cch_dif.setVisibility(View.INVISIBLE);
                    special.setVisibility(View.INVISIBLE);}

                if (dataSnapshot.child("expcomplete").getValue() != null)
                    cch_exp.setVisibility(View.VISIBLE);
                else
                    cch_exp.setVisibility(View.INVISIBLE);

                if (dataSnapshot.child("specomplete").getValue() != null)
                    cch_spe.setVisibility(View.VISIBLE);
                else
                    cch_spe.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnChangePassword = (Button) findViewById(R.id.change_password_button);

        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);

        changePassword = (Button) findViewById(R.id.changePass);

        remove = (Button) findViewById(R.id.remove);
        signOut = (Button) findViewById(R.id.sign_out);

        oldEmail = (EditText) findViewById(R.id.old_email);

        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);

        oldEmail.setVisibility(View.GONE);

        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);

        changePassword.setVisibility(View.GONE);

        remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);

                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);

                changePassword.setVisibility(View.VISIBLE);

                remove.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Le mot de passe est trop court, il faut au moins 6 caractères !");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Le mot de passe a été changé !", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Le changement de mot de passe a échoué.", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Entrer votre mot de passe");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    if (scoreUser != null)
                        scoreUser.removeValue();
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Votre profil a été supprimé.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Votre profil n'a pas pu être supprimé !", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        try {
            email.setText("Connecté en temps que : " + user.getEmail());
        } catch (Throwable e){
        }
    }


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                setDataToView(user);

            }
        }


    };

    //sign out method
    public void signOut() {
        auth.signOut();


        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
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
}
