package com.bruno13palhano.ssapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bruno13palhano.ssapp.data.Match;

import java.util.List;

@Dao
public interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMatch(Match match);

    @Query("SELECT * FROM match_table WHERE is_tournament = 0 AND is_series = 0")
    LiveData<List<Match>> getAllRankMatches();

    @Update
    void updateMatch(Match match);

    @Query("UPDATE match_table SET first_player_score_match = :scoreMatch WHERE matchId = :id")
    void updateFirstPlayerScoreMatch(int scoreMatch, long id);

    @Query("UPDATE match_table SET second_player_score_match = :scoreMatch WHERE matchId = :id")
    void updateSecondPlayerScoreMatch(int scoreMatch, long id);

    @Query("UPDATE match_table SET first_player_score_series = :scoreSeries WHERE matchId = :id")
    void updateFirstPlayerScoreSeries(int scoreSeries, long id);

    @Query("UPDATE match_table SET second_player_score_series = :scoreSeries WHERE matchId = :id")
    void updateSecondPlayerScoreSeries(int scoreSeries, long id);

    @Query("UPDATE match_table SET first_player_number_of_matches = :numberOfMatches WHERE matchId = :id")
    void updateFirstPlayerNumberOfMatches(int numberOfMatches, long id);

    @Query("UPDATE match_table SET first_player_number_of_wins = :numberOfWins WHERE matchId = :id")
    void updateFirstPlayerNumberOfWins(int numberOfWins, long id);

    @Query("UPDATE match_table SET second_player_number_of_matches = :numberOfMatches WHERE matchId = :id")
    void updateSecondPlayerNumberOfMatches(int numberOfMatches, long id);

    @Query("UPDATE match_table SET second_player_number_of_wins = :numberOfWins WHERE matchId = :id")
    void updateSecondPlayerNumberOfWins(int numberOfWins, long id);

    @Delete
    void deleteMatch(Match match);

    @Query("SELECT * FROM match_table WHERE is_tournament = 1")
    LiveData<List<Match>> getAllMatchesTournament();

    @Query("UPDATE match_table SET is_match_finished = :finished WHERE matchId = :id")
    void setMatchFinished(boolean finished, long id);

    @Query("SELECT * FROM match_table WHERE is_series = 1")
    LiveData<List<Match>> getAllMatchesSeries();

    @Query("UPDATE match_table SET matches_per_series = :numberOfMatches WHERE matchId = :id")
    void updateMatchesPerSeries(int numberOfMatches, long id);
}
