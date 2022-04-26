package com.bruno13palhano.ssapp.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bruno13palhano.ssapp.data.Player;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlayer(Player player);

    @Query("SELECT * FROM player_table")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * FROM player_table")
    List<Player> getPlayers();

    @Update
    void updatePlayer(Player player);

    @Query("UPDATE player_table SET score_match = :scoreMatch WHERE playerId = :id")
    void updatePlayerScoreMatch(int scoreMatch, long id);

    @Query("UPDATE player_table SET score_total = :score WHERE playerId = :id")
    void updatePlayerScoreTotal(int score, long id);

    @Query("UPDATE player_table SET score_series = :score WHERE playerId = :id")
    void updatePlayerScoreSeries(int score, long id);

    @Delete
    void deletePlayer(Player player);

    @Query("UPDATE player_table SET won_tournaments = :numberOfWonTournaments WHERE playerId = :id")
    void updatePlayerWonTournaments(int numberOfWonTournaments, long id);

    @Query("UPDATE player_table SET number_of_matches = :numberOfMatches WHERE playerId = :id")
    void updatePlayerNumberOfMatches(int numberOfMatches, long id);

    @Query("UPDATE player_table SET number_of_wins = :numberOfWins WHERE playerId = :id")
    void updatePlayerNumberOfWins(int numberOfWins, long id);

    //teste de paging 3
    @Query("select * from player_table order by playerId DESC LIMIT :loadSize OFFSET :index * :loadSize")
    List<Player> getPage(int index, int loadSize);

}
