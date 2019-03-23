package com.example.colormemory;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Lancement extends AppCompatActivity {
    private static final String TAG = "Main";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button facile, difficile, expert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        facile = findViewById(R.id.facile);
        difficile = findViewById(R.id.difficile);
        expert = findViewById(R.id.expert);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/edosz.ttf");
        facile.setTypeface(font);
        difficile.setTypeface(font);
        expert.setTypeface(font);


        facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(Lancement.this, Jeu.class);
                lance_jeu.putExtra("int_value", 1);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
            }
        });

        difficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(Lancement.this, Jeu.class);
                lance_jeu.putExtra("int_value", 2);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lance_jeu = new Intent(Lancement.this, Jeu.class);
                lance_jeu.putExtra("int_value", 3);
                startActivity(lance_jeu);

                overridePendingTransition(R.anim.slide_in_bot,R.anim.slide_out_bot);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void senregistrer(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "CreateUserWithEmail:onComplete:" + task.isSuccessful());

                        Toast.makeText(Lancement.this, "Authentification réussi.",
                                Toast.LENGTH_SHORT).show();

                        if(!task.isSuccessful()){
                            Toast.makeText(Lancement.this, "Authentification failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void seConnecter(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "SignInWithEmail:onComplete:" + task.isSuccessful());

                        Toast.makeText(Lancement.this, "Vous êtes connecté.",
                                Toast.LENGTH_SHORT).show();

                        if(!task.isSuccessful()){
                            Log.v(TAG, "signWithEmail", task.getException());
                            Toast.makeText(Lancement.this, "Authentification failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
