package com.bruno13palhano.ssapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rotation_table")
public class Rotation {

    @PrimaryKey(autoGenerate = true)
    private long rotationId;

    @ColumnInfo(name = "playerId")
    private long playerId;

    @ColumnInfo(name = "nickname")
    private String nickname;

    @ColumnInfo(name = "image")
    private String imageUri;

    @ColumnInfo(name = "score")
    private int score;

    public long getRotationId() {
        return rotationId;
    }

    public void setRotationId(long rotationId) {
        this.rotationId = rotationId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
