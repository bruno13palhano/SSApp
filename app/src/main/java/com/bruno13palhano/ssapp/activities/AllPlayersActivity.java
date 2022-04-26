package com.bruno13palhano.ssapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.fragments.ShowAllPlayersFragment;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;

import java.util.List;
import java.util.Objects;

public class AllPlayersActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private PlayerViewModel playerViewModel;

    private List<Player> playerList;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_players);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //Ativando up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.all_players_view_page);
        pagerAdapter = new SectionsPagerAdapter(this);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getAllPlayers().observe(this, (players) -> {
            playerList = players;
            viewPager.setAdapter(pagerAdapter);
        });


    }

    private class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Bundle bundle = new Bundle();

            try {
                bundle.putLong("id", playerList.get(position).getPlayerId());
                bundle.putString("nickname", playerList.get(position).getPlayerNickName());
                bundle.putString("imageUri", playerList.get(position).getPlayerImageUri());
                bundle.putLong("dateInMillis", playerList.get(position).getPlayerDateInMillis());
                bundle.putInt("scoreMatch", playerList.get(position).getPlayerScoreMatch());
                bundle.putInt("scoreTotal", playerList.get(position).getPlayerScoreTotal());
                bundle.putInt("scoreSeries", playerList.get(position).getPlayerScoreSeries());
                bundle.putInt("numberOfWonTournaments", playerList.get(position).getPlayerWonTournaments());
                bundle.putInt("numberOfWins", playerList.get(position).getPlayerNumberOfWins());
                bundle.putInt("numberOfMatches", playerList.get(position).getPlayerNumberOfMatches());
                bundle.putInt("position", position);

            }catch (NullPointerException ignore){

            }

            Fragment fragment = new ShowAllPlayersFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }

    //Método auxiliar para ativar e desativar o upButton nos fragmentos.
    public void enableUpButton(){
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException ignored){

        }
    }
    public void disableUpButton(){
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }catch (NullPointerException ignored){

        }
    }
}