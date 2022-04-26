package com.bruno13palhano.ssapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "match_table")
public class Match {

    @PrimaryKey(autoGenerate = true)
    private long matchId;

    @ColumnInfo(name = "first_player_id")
    private long firstPlayerId;

    @ColumnInfo(name = "first_player_nickname")
    private String firstPlayerNickname;

    @ColumnInfo(name = "first_player_image_uri")
    private String firstPlayerImageUri;

    @ColumnInfo(name = "first_player_score_match")
    private int firstPlayerScoreMatch;

    @ColumnInfo(name = "first_player_score_series")
    private int firstPlayerScoreSeries;

    @ColumnInfo(name = "second_player_id")
    private long secondPlayerId;

    @ColumnInfo(name = "second_player_nickname")
    private String secondPlayerNickname;

    @ColumnInfo(name = "second_player_image_uri")
    private String secondPlayerImageUri;

    @ColumnInfo(name = "second_player_score_match")
    private int secondPlayerScoreMatch;

    @ColumnInfo(name = "second_player_score_series")
    private int secondPlayerScoreSeries;

    @ColumnInfo(name = "first_player_number_of_matches")
    private int firstPlayerNumberOfMatches;

    @ColumnInfo(name = "first_player_number_of_wins")
    private int firstPlayerNumberOfWins;

    @ColumnInfo(name = "second_player_number_of_matches")
    private int secondPlayerNumberOfMatches;

    @ColumnInfo(name = "second_player_number_of_wins")
    private int secondPlayerNumberOfWins;

    @ColumnInfo(name = "is_tournament")
    private boolean isTournament;

    @ColumnInfo(name = "is_series")
    private boolean isSeries;

    @ColumnInfo(name = "is_match_finished")
    private boolean isMatchFinished;

    @ColumnInfo(name = "matches_per_series")
    private int matchesPerSeries;

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public long getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(long firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public String getFirstPlayerNickname() {
        return firstPlayerNickname;
    }

    public void setFirstPlayerNickname(String firstPlayerNickname) {
        this.firstPlayerNickname = firstPlayerNickname;
    }

    public String getFirstPlayerImageUri() {
        return firstPlayerImageUri;
    }

    public void setFirstPlayerImageUri(String firstPlayerImageUri) {
        this.firstPlayerImageUri = firstPlayerImageUri;
    }

    public int getFirstPlayerScoreMatch() {
        return firstPlayerScoreMatch;
    }

    public void setFirstPlayerScoreMatch(int firstPlayerScoreMatch) {
        this.firstPlayerScoreMatch = firstPlayerScoreMatch;
    }

    public int getFirstPlayerScoreSeries() {
        return firstPlayerScoreSeries;
    }

    public void setFirstPlayerScoreSeries(int firstPlayerScoreSeries) {
        this.firstPlayerScoreSeries = firstPlayerScoreSeries;
    }

    public long getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(long secondPlayerId) {
        this.secondPlayerId = secondPlayerId;
    }

    public String getSecondPlayerNickname() {
        return secondPlayerNickname;
    }

    public void setSecondPlayerNickname(String secondPlayerNickname) {
        this.secondPlayerNickname = secondPlayerNickname;
    }

    public String getSecondPlayerImageUri() {
        return secondPlayerImageUri;
    }

    public void setSecondPlayerImageUri(String secondPlayerImageUri) {
        this.secondPlayerImageUri = secondPlayerImageUri;
    }

    public int getSecondPlayerScoreMatch() {
        return secondPlayerScoreMatch;
    }

    public void setSecondPlayerScoreMatch(int firstSecondScoreMatch) {
        this.secondPlayerScoreMatch = firstSecondScoreMatch;
    }

    public int getSecondPlayerScoreSeries() {
        return secondPlayerScoreSeries;
    }

    public void setSecondPlayerScoreSeries(int secondPlayerScoreSeries) {
        this.secondPlayerScoreSeries = secondPlayerScoreSeries;
    }

    public boolean isTournament() {
        return isTournament;
    }

    public void setTournament(boolean tournament) {
        isTournament = tournament;
    }

    public boolean isMatchFinished() {
        return isMatchFinished;
    }

    public void setMatchFinished(boolean matchFinished) {
        isMatchFinished = matchFinished;
    }

    public boolean isSeries() {
        return isSeries;
    }

    public void setSeries(boolean series) {
        isSeries = series;
    }

    public int getMatchesPerSeries() {
        return matchesPerSeries;
    }

    public void setMatchesPerSeries(int matchesPerSeries) {
        this.matchesPerSeries = matchesPerSeries;
    }

    public int getFirstPlayerNumberOfMatches() {
        return firstPlayerNumberOfMatches;
    }

    public void setFirstPlayerNumberOfMatches(int firstPlayerNumberOfMatches) {
        this.firstPlayerNumberOfMatches = firstPlayerNumberOfMatches;
    }

    public int getFirstPlayerNumberOfWins() {
        return firstPlayerNumberOfWins;
    }

    public void setFirstPlayerNumberOfWins(int firstPlayerNumberOfWins) {
        this.firstPlayerNumberOfWins = firstPlayerNumberOfWins;
    }

    public int getSecondPlayerNumberOfMatches() {
        return secondPlayerNumberOfMatches;
    }

    public void setSecondPlayerNumberOfMatches(int secondPlayerNumberOfMatches) {
        this.secondPlayerNumberOfMatches = secondPlayerNumberOfMatches;
    }

    public int getSecondPlayerNumberOfWins() {
        return secondPlayerNumberOfWins;
    }

    public void setSecondPlayerNumberOfWins(int secondPlayerNumberOfWins) {
        this.secondPlayerNumberOfWins = secondPlayerNumberOfWins;
    }
}
