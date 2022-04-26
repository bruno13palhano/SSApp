package com.bruno13palhano.ssapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bruno13palhano.ssapp.data.Rotation;

import java.util.List;

@Dao
public interface RotationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlayerRotation(Rotation playerRotation);

    @Query("SELECT * FROM rotation_table")
    LiveData<List<Rotation>> getAllPlayersRotation();

    @Query("UPDATE rotation_table SET score = :score WHERE playerId = :id")
    void updateScorePlayerRotation(int score, long id);

    @Delete
    void deletePlayerRotation(Rotation playerRotation);
}
