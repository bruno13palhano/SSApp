package com.bruno13palhano.ssapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bruno13palhano.ssapp.data.Match;
import com.bruno13palhano.ssapp.repository.MatchRepository;

import java.util.List;

public class MatchViewModel extends AndroidViewModel {
    private MatchRepository repository;
    private final LiveData<List<Match>> allRankMatches, allTournamentMatches, allSeriesMatches;

    public MatchViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchRepository(application);
        allRankMatches = repository.getAllRankMatches();
        allTournamentMatches = repository.getAllTournamentMatches();
        allSeriesMatches = repository.getAllSeriesMatches();
    }

    public LiveData<List<Match>> getAllRankMatches(){
        return allRankMatches;
    }

    public LiveData<List<Match>> getAllTournamentMatches(){
        return allTournamentMatches;
    }

    public LiveData<List<Match>> getAllSeriesMatches(){
        return allSeriesMatches;
    }

    public void insertMatch(Match match){
        repository.insertMatch(match);
    }

    public void deleteMatch(Match match){
        repository.deleteMatch(match);
    }

    public void updateMatch(Match match){
        repository.updateMatch(match);
    }

    public void updateFirstPlayerScoreMatch(int scoreMatch, long id){
        repository.updateFirstPlayerScoreMatch(scoreMatch, id);
    }

    public void updateSecondPlayerScoreMatch(int scoreMatch, long id){
        repository.updateSecondPlayerScoreMatch(scoreMatch, id);
    }

    public void updateFirstPlayerScoreSeries(int scoreSeries, long id){
        repository.updateFirstPlayerScoreSeries(scoreSeries, id);
    }

    public void updateSecondPlayerScoreSeries(int scoreSeries, long id){
        repository.updateSecondPlayerScoreSeries(scoreSeries, id);
    }

    public void updateFirstPlayerNumberOfMatches(int numberOfMatches, long id){
        repository.updateFirstPlayerNumberOfMatches(numberOfMatches, id);
    }

    public void updateSecondPlayerNumberOfMatches(int numberOfMatches, long id){
        repository.updateSecondPlayerNumberOfMatches(numberOfMatches, id);
    }

    public void updateFirstPlayerNumberOfWins(int numberOfWins, long id){
        repository.updateFirstPlayerNumberOfWins(numberOfWins, id);
    }

    public void updateSecondPlayerNumberOfWins(int numberOfWins, long id){
        repository.updateSecondPlayerNumberOfWins(numberOfWins, id);
    }

    public void setMatchFinished(boolean finished, long id){
        repository.setMatchFinished(finished, id);
    }

    public void updateMatchesPerSeries(int numberOfMatches, long id){
        repository.updateMatchesPerSeries(numberOfMatches, id);
    }
}
