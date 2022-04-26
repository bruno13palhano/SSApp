package com.bruno13palhano.ssapp;

import com.bruno13palhano.ssapp.data.Match;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.data.Rotation;

import java.util.List;

public class MatchValues {
    private int oldScore;
    private int newScore;
    private int position;
    private boolean isFirstPlayer;
    private List<Player> playerList;
    private List<Match> matchList;
    private List<Rotation> rotationList;

    public int getOldScore() {
        return oldScore;
    }

    public void setOldScore(int oldScore) {
        this.oldScore = oldScore;
    }

    public int getNewScore() {
        return newScore;
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Match> getMatchList() {
        return matchList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public List<Rotation> getRotationList() {
        return rotationList;
    }

    public void setRotationList(List<Rotation> rotationList) {
        this.rotationList = rotationList;
    }
}

