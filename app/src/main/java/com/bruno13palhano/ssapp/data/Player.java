package com.bruno13palhano.ssapp.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_table")
public class Player {

    @PrimaryKey(autoGenerate = true)
    private long playerId;

    @NonNull
    @ColumnInfo(name = "nickname")
    private String playerNickName;

    @ColumnInfo(name = "date")
    private long playerDateInMillis;

    @ColumnInfo(name = "image")
    private String playerImageUri;

    @ColumnInfo(name = "create_date")
    private long playerCreateProfileDateInMillis;

    @ColumnInfo(name = "score_total")
    private int playerScoreTotal;

    @ColumnInfo(name = "score_match")
    private int playerScoreMatch;

    @ColumnInfo(name = "score_series")
    private int playerScoreSeries;

    @ColumnInfo(name = "won_tournaments")
    private int playerWonTournaments;

    @ColumnInfo(name = "number_of_matches")
    private int playerNumberOfMatches;

    @ColumnInfo(name = "number_of_wins")
    private int playerNumberOfWins;

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setPlayerNickName(@NonNull String playerNickName) {
        this.playerNickName = playerNickName;
    }

    public void setPlayerDateInMillis(long playerDateInMillis) {
        this.playerDateInMillis = playerDateInMillis;
    }

    public void setPlayerImageUri(String playerImageUri) {
        this.playerImageUri = playerImageUri;
    }

    public void setPlayerCreateProfileDateInMillis(long playerCreateProfileDateInMillis) {
        this.playerCreateProfileDateInMillis = playerCreateProfileDateInMillis;
    }

    public void setPlayerScoreTotal(int playerScoreTotal) {
        this.playerScoreTotal = playerScoreTotal;
    }

    public void setPlayerScoreMatch(int playerScoreMatch) {
        this.playerScoreMatch = playerScoreMatch;
    }

    public void setPlayerScoreSeries(int playerScoreSeries) {
        this.playerScoreSeries = playerScoreSeries;
    }

    public Player(@NonNull String playerNickName){
        this.playerNickName = playerNickName;
    }

    public long getPlayerId() {
        return playerId;
    }

    @NonNull
    public String getPlayerNickName() {
        return playerNickName;
    }

    public long getPlayerDateInMillis() {
        return playerDateInMillis;
    }

    public String getPlayerImageUri() {
        return playerImageUri;
    }

    public long getPlayerCreateProfileDateInMillis() {
        return playerCreateProfileDateInMillis;
    }

    public int getPlayerScoreTotal() {
        return playerScoreTotal;
    }

    public int getPlayerScoreMatch() {
        return playerScoreMatch;
    }

    public int getPlayerScoreSeries() {
        return playerScoreSeries;
    }

    public int getPlayerWonTournaments() {
        return playerWonTournaments;
    }

    public void setPlayerWonTournaments(int playerWonTournaments) {
        this.playerWonTournaments = playerWonTournaments;
    }

    public int getPlayerNumberOfMatches() {
        return playerNumberOfMatches;
    }

    public void setPlayerNumberOfMatches(int numberOfMatches) {
        this.playerNumberOfMatches = numberOfMatches;
    }

    public int getPlayerNumberOfWins() {
        return playerNumberOfWins;
    }

    public void setPlayerNumberOfWins(int numberOfWins) {
        this.playerNumberOfWins = numberOfWins;
    }
}
