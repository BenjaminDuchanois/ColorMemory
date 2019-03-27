package com.example.colormemory.Score;

//Class Joueur pour l'affichage dans les HighScores
public class Joueur {
    String joueurID;
    String pseudo;
    String mail;
    double bestScore;

    public Joueur(){

    }

    public Joueur(String joueurID, String pseudo, String mail, double bestScore) {
        this.joueurID = joueurID;
        this.pseudo = pseudo;
        this.mail = mail;
        this.bestScore = bestScore;
    }

    public String getJoueurID() {
        return joueurID;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMail() {
        return mail;
    }

    public double getBestScore() {
        return bestScore;
    }
}
