package com.bruno13palhano.ssapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bruno13palhano.ssapp.SSRoomDatabase;
import com.bruno13palhano.ssapp.dao.RotationDao;
import com.bruno13palhano.ssapp.data.Rotation;

import java.util.List;

public class RotationRepository {
    private RotationDao rotationDao;
    private LiveData<List<Rotation>> allPlayersRotation;

    public RotationRepository(Application application){
        SSRoomDatabase db = SSRoomDatabase.getDatabase(application);
        rotationDao = db.rotationDao();
        allPlayersRotation = rotationDao.getAllPlayersRotation();
    }

    public LiveData<List<Rotation>> getAllPlayerRotation(){
        return allPlayersRotation;
    }

    public void insertPlayerRotation(Rotation playerRotation){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            rotationDao.insertPlayerRotation(playerRotation);
        });
    }

    public void deletePlayerRotation(Rotation playerRotation){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            rotationDao.deletePlayerRotation(playerRotation);
        });
    }

    public void updateScorePlayerRotation(int score, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            rotationDao.updateScorePlayerRotation(score, id);
        });
    }
}
