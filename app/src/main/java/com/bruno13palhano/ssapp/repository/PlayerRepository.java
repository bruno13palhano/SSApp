package com.bruno13palhano.ssapp.repository;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bruno13palhano.ssapp.SSRoomDatabase;
import com.bruno13palhano.ssapp.dao.PlayerDao;
import com.bruno13palhano.ssapp.data.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerRepository {
    private PlayerDao playerDao;
    private LiveData<List<Player>> allPlayers;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private MutableLiveData<List<Player>> allPlayersFirebase;
    private List<Player> playerList;
    private SSRoomDatabase db;

    public PlayerRepository(Application application){
        db = SSRoomDatabase.getDatabase(application);
        playerDao = db.playerDao();
        allPlayers = playerDao.getAllPlayers();
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public LiveData<List<Player>> getAllPlayers(){
        return allPlayers;
    }

    public MutableLiveData<List<Player>> getAllPlayersFirebase(){
        if(allPlayersFirebase == null)
            allPlayersFirebase = new MutableLiveData<>();

        allPlayersFromFirebase();

        return allPlayersFirebase;
    }

    public void allPlayersFromFirebase() {
        database.collection("players")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            playerList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                playerList.add(convertPlayerFromMapToList(document.getData()));
                            }

                            allPlayersFirebase.postValue(playerList);

                        } else {
                            System.out.println("não listou!");
                        }
                    }
                });
    }

    private Player convertPlayerFromMapToList(Map<String, Object> playerMap){
        Player player = new Player(String.valueOf(playerMap.get("playerNickName")));
        player.setPlayerId(Long.parseLong(String.valueOf(playerMap.get("playerId"))));
        player.setPlayerImageUri(String.valueOf(playerMap.get("playerImageUri")));
        player.setPlayerDateInMillis(Long.parseLong(String.valueOf(playerMap.get("playerDateInMillis"))));
        player.setPlayerCreateProfileDateInMillis(Long.parseLong(String.valueOf(playerMap.get("playerCreateProfileDateInMillis"))));
        player.setPlayerScoreMatch(Integer.parseInt(String.valueOf(playerMap.get("playerScoreMatch"))));
        player.setPlayerScoreSeries(Integer.parseInt(String.valueOf(playerMap.get("playerScoreSeries"))));
        player.setPlayerScoreTotal(Integer.parseInt(String.valueOf(playerMap.get("playerScoreTotal"))));
        player.setPlayerWonTournaments(Integer.parseInt(String.valueOf(playerMap.get("playerWonTournaments"))));

        return player;
    }

    public void insertPlayer(Player player){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.insertPlayer(player);
        });
    }

    public void deletePlayer(Player player){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.deletePlayer(player);
        });
    }

    public void updatePlayer(Player player){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayer(player);
        });
    }

    public void updatePlayerScoreMatch(int scoreMatch, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayerScoreMatch(scoreMatch, id);
        });
    }

    public void updatePlayerScoreTotal(int score, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayerScoreTotal(score, id);
        });
    }

    public void updatePlayerScoreSeries(int score, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayerScoreSeries(score, id);
        });
    }

    public void updatePlayerWonTournaments(int numberOfWonTournaments, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayerWonTournaments(numberOfWonTournaments, id);
        });
    }

    public void updatePlayerNumberOfMatches(int numberOfMatches, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayerNumberOfMatches(numberOfMatches, id);
        });
    }

    public void updatePlayerNumberOfWins(int numberOfWins, long id){
        SSRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.updatePlayerNumberOfWins(numberOfWins, id);
        });
    }

    private void createPlayer(Player player){
        database.collection("players")
                .add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        uploadImage(documentReference.getId(), player.getPlayerImageUri());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void uploadImage(String playerUid, String imageUriString){
        StorageReference playerImage = storage.getReference().child(
                "images/".concat(playerUid+"_players_image.png"));

        playerImage.putFile(Uri.parse(imageUriString));
    }

}
