package com.bruno13palhano.ssapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bruno13palhano.ssapp.dao.MatchDao;
import com.bruno13palhano.ssapp.dao.PlayerDao;
import com.bruno13palhano.ssapp.dao.RotationDao;
import com.bruno13palhano.ssapp.data.Match;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.data.Rotation;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Database(entities = {Player.class, Match.class, Rotation.class},
        version = 14, exportSchema = false)
public abstract class SSRoomDatabase extends RoomDatabase {


    public abstract PlayerDao playerDao();
    public abstract MatchDao matchDao();
    public abstract RotationDao rotationDao();

    private static volatile SSRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SSRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (SSRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SSRoomDatabase.class, "player_database")
//                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//        private static RoomDatabase.Callback roomDatabaseCallback = new Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//
//            databaseWriteExecutor.execute(() ->{
//
//            });
//        }
//    };
}
