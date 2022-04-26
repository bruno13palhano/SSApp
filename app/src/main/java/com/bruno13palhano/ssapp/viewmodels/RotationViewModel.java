package com.bruno13palhano.ssapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bruno13palhano.ssapp.data.Rotation;
import com.bruno13palhano.ssapp.repository.RotationRepository;

import java.util.List;

public class RotationViewModel extends AndroidViewModel {
    private RotationRepository repository;
    private final LiveData<List<Rotation>> allRotationPlayers;

    public RotationViewModel(@NonNull Application application) {
        super(application);
        repository = new RotationRepository(application);
        allRotationPlayers = repository.getAllPlayerRotation();
    }

    public LiveData<List<Rotation>> getAllRotationPlayers(){
        return allRotationPlayers;
    }

    public void insertRotationPlayer(Rotation playerRotation){
        repository.insertPlayerRotation(playerRotation);
    }

    public void deleteRotationPlayer(Rotation playerRotation){
        repository.deletePlayerRotation(playerRotation);
    }

    public void updateScoreRotationPlayer(int score, long id){
        repository.updateScorePlayerRotation(score, id);
    }
}
