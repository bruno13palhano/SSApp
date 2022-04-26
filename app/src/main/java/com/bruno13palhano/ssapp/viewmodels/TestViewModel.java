package com.bruno13palhano.ssapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.bruno13palhano.ssapp.SSRoomDatabase;
import com.bruno13palhano.ssapp.TestPaging;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.repository.PlayerRepository;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.CoroutineScope;

public class TestViewModel extends AndroidViewModel {
    private Application application;

    public TestViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<PagingData<Player>> getLiveData(){
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Player> pager = new Pager<>(
                new PagingConfig(5),
                ()-> new TestPaging(application)
        );

        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }
}
