package com.bruno13palhano.ssapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.bruno13palhano.ssapp.dao.PlayerDao;
import com.bruno13palhano.ssapp.data.Player;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class TestPaging extends ListenableFuturePagingSource<Integer, Player> {
    private int count = 0;

    private PlayerDao playerDao;
    private SSRoomDatabase db;

    public TestPaging(Application application){
        db = SSRoomDatabase.getDatabase(application);
        playerDao = db.playerDao();
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Player> state) {
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Player> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, Player>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<LoadResult<Integer, Player>> player = service.submit(
                new Callable<LoadResult<Integer, Player>>() {
                    public LoadResult<Integer, Player> call() {
                        Integer nextKey = count;

                        List<Player> p = playerDao.getPage(nextKey, loadParams.getLoadSize());
                        if(p.isEmpty()){
                            nextKey = null;
                        }else{
                            count += loadParams.getLoadSize();
                        }

                        return new LoadResult.Page<Integer, Player>(p,
                                null,
                                nextKey);
                    }
                });
        Futures.addCallback(
                player,
                new FutureCallback<LoadResult<Integer, Player>>() {
                    @Override
                    public void onSuccess(@Nullable LoadResult<Integer, Player> result) {

                    }

                    public void onFailure(Throwable thrown) {

                    }
                },
                service);

        return player;
    }

    private LoadResult<Integer, Player> toLoadResult(List<Player> playerList){
        Integer nextKey = count;

        return new LoadResult.Page<Integer, Player>(playerList,
                null,
                count++);
    }
}
