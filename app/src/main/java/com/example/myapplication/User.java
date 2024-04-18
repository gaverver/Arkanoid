package com.example.myapplication;

public class User {
    private String email;
    private String password;
    private String username;
    private int numGames;
    private int gamesWon;
    public User() {

    }
    public User(String password, String email, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.numGames = 0;
        this.gamesWon = 0;
    }
    public String getEmail() {
        return this.email;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }

    public int getNumGames() {
        return this.numGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumGames(int numGames) {
        this.numGames = numGames;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void IncreaseGames() {
        this.numGames++;
    }
    public void IncreaseGamesWon() {
        this.gamesWon++;
    }
}
