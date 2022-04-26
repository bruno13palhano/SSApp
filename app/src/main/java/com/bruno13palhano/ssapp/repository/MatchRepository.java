package com.bruno13palhano.ssapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bruno13palhano.ssapp.SSRoomDatabase;
import com.bruno13palhano.ssapp.dao.MatchDao;
import com.bruno13palhano.ssapp.data.Match;

import java.util.List;

public class MatchRepository {
    private MatchDao matchDao;
    private LiveData<List<Match>> allRankMatches, allTournamentMatches, allSeriesMatches;

    public MatchRepository(Application application){
        SSRoomDatabase db = SSRoomDatabase.getDatabase(application);
        matchDao = db.matchDao();
        allRankMatches = matchDao.getAllRankMatches();
        allTournamentMatches = matchDao.getAllMatchesTournament();
        allSeriesMatches = matchDao.getAllMatchesSeries();
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
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.insertMatch(match);
        });
    }

    public void deleteMatch(Match match){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.deleteMatch(match);
        });
    }

    public void updateMatch(Match match){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateMatch(match);
        });
    }

    public void updateFirstPlayerScoreMatch(int scoreMatch, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateFirstPlayerScoreMatch(scoreMatch, id);
        });
    }

    public void updateSecondPlayerScoreMatch(int scoreMatch, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateSecondPlayerScoreMatch(scoreMatch, id);
        });
    }

    public void updateFirstPlayerScoreSeries(int scoreSeries, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateFirstPlayerScoreSeries(scoreSeries, id);
        });
    }

    public void updateSecondPlayerScoreSeries(int scoreSeries, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateSecondPlayerScoreSeries(scoreSeries, id);
        });
    }

    public void updateFirstPlayerNumberOfMatches(int numberOfMatches, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateFirstPlayerNumberOfMatches(numberOfMatches, id);
        });
    }

    public void updateSecondPlayerNumberOfMatches(int numberOfMatches, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateSecondPlayerNumberOfMatches(numberOfMatches, id);
        });
    }

    public void updateFirstPlayerNumberOfWins(int numberOfWins, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateFirstPlayerNumberOfWins(numberOfWins, id);
        });
    }

    public void updateSecondPlayerNumberOfWins(int numberOfWins, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateSecondPlayerNumberOfWins(numberOfWins, id);
        });
    }

    public void setMatchFinished(boolean finished, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.setMatchFinished(finished, id);
        });
    }

    public void updateMatchesPerSeries(int numberOfMatches, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDao.updateMatchesPerSeries(numberOfMatches, id);
        });
    }
}
