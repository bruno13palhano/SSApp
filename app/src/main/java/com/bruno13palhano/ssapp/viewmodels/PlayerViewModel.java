package com.bruno13palhano.ssapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.repository.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private PlayerRepository repository;
    private final LiveData<List<Player>>  allPlayers;
    private int playerScoreTotal;
    private MutableLiveData<List<Player>> allPlayersFirebase;

    public PlayerViewModel(@NonNull Application application){
        super(application);
        repository = new PlayerRepository(application);
        allPlayers = repository.getAllPlayers();
//        allPlayersFirebase = repository.getAllPlayersFirebase();
    }

    public LiveData<List<Player>> getAllPlayers(){
        return allPlayers;
    }

    public MutableLiveData<List<Player>> getAllPlayersFirebase(){
        return allPlayersFirebase;
    }

    public void insertPlayer(Player player){
        repository.insertPlayer(player);
    }

    public void deletePlayer(Player player){
        repository.deletePlayer(player);
    }

    public void updatePlayer(Player player){
        repository.updatePlayer(player);
    }

    public void updatePlayerScoreMatch(int scoreMatch, long id){
        repository.updatePlayerScoreMatch(scoreMatch, id);
    }

    public void updatePlayerScoreTotal(int score, long id){
        repository.updatePlayerScoreTotal(score, id);
    }

    public void updatePlayerScoreSeries(int score, long id){
        repository.updatePlayerScoreSeries(score, id);
    }

    public void updatePlayerWonTournaments(int numberOfWonTournaments, long id){
        repository.updatePlayerWonTournaments(numberOfWonTournaments, id);
    }

    public void updatePlayerNumberOfMatches(int numberOfMatches, long id){
        repository.updatePlayerNumberOfMatches(numberOfMatches, id);
    }

    public void updatePlayerNumberOfWins(int numberOfWins, long id){
        repository.updatePlayerNumberOfWins(numberOfWins, id);
    }
}
