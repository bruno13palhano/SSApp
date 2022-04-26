package com.bruno13palhano.ssapp.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;

import java.io.FileNotFoundException;


public class ShowPlayerFragment extends Fragment {
    private Player player;
    private PlayerViewModel playerViewModel;

    private int playerPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_player, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_player_fragment);

        ImageView inputPlayerImageUri = view.findViewById(R.id.show_player_image_icon);
        TextView inputPlayerNickname = view.findViewById(R.id.show_text_player_name);
        TextView inputPlayerScoreTotal = view.findViewById(R.id.show_text_player_score_total);
        TextView inputPlayerScoreMatch = view.findViewById(R.id.show_text_player_score_match);
        TextView inputPlayerScoreSeries = view.findViewById(R.id.show_text_player_score_series);
        TextView inputPlayerNumberOfWonTournaments = view.findViewById(R.id.show_text_player_number_of_won_tournaments);
        TextView inputPlayerNumberOfWins = view.findViewById(R.id.show_text_player_number_of_wins);
        TextView inputPlayerNumberOfMatches = view.findViewById(R.id.show_text_player_number_of_matches);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            player = new Player(bundle.getString("nickname"));
            player.setPlayerId(bundle.getLong("id"));
            player.setPlayerImageUri(bundle.getString("imageUri"));
            player.setPlayerDateInMillis(bundle.getLong("dateInMillis"));
            player.setPlayerScoreMatch(bundle.getInt("scoreMatch"));
            player.setPlayerScoreTotal(bundle.getInt("scoreTotal"));
            player.setPlayerScoreSeries(bundle.getInt("scoreSeries"));
            player.setPlayerWonTournaments(bundle.getInt("numberOfWonTournaments"));
            player.setPlayerNumberOfWins(bundle.getInt("numberOfWins"));
            player.setPlayerNumberOfMatches(bundle.getInt("numberOfMatches"));
            playerPosition = bundle.getInt("position");

            try{
                inputPlayerImageUri.setImageBitmap(SSUtil.decodeUri(requireActivity(), Uri.parse(bundle.getString("imageUri")),
                        150));
            }catch (NullPointerException | FileNotFoundException ignored){}

            inputPlayerNickname.setText(player.getPlayerNickName());
            inputPlayerScoreTotal.setText(String.valueOf(player.getPlayerScoreTotal()).concat(" Pts."));
            inputPlayerScoreMatch.setText(String.valueOf(player.getPlayerScoreMatch()).concat(" Pts."));
            inputPlayerScoreSeries.setText(String.valueOf(player.getPlayerScoreSeries()).concat(" Pts."));
            inputPlayerNumberOfWonTournaments.setText(String.valueOf(player.getPlayerWonTournaments()).concat("."));
            inputPlayerNumberOfWins.setText(String.valueOf(player.getPlayerNumberOfWins()).concat("."));
            inputPlayerNumberOfMatches.setText(String.valueOf(player.getPlayerNumberOfMatches()));
        }

        setHasOptionsMenu(true);

        try{
            MainActivity mainActivity = (MainActivity) getActivity();
            if(mainActivity != null){
                mainActivity.enableUpButton();
            }
        }catch (ClassCastException ignored){}

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_player_more_vert, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == android.R.id.home){
            requireActivity().onBackPressed();
            return true;

        }else if(id == R.id.action_edit){

            Bundle bundle = new Bundle();

            bundle.putString("fragment", "editPlayer");
            bundle.putLong("id", player.getPlayerId());
            bundle.putString("nickname", player.getPlayerNickName());
            bundle.putString("imageUri", player.getPlayerImageUri());
            bundle.putLong("dateInMillis", player.getPlayerDateInMillis());
            bundle.putInt("scoreMatch", player.getPlayerScoreMatch());
            bundle.putInt("scoreTotal", player.getPlayerScoreTotal());
            bundle.putInt("scoreSeries", player.getPlayerScoreSeries());
            bundle.putInt("numberOfWonTournaments", player.getPlayerWonTournaments());
            bundle.putInt("position", playerPosition);

            Fragment fragment = new EditPlayerFragment();
            fragment.setArguments(bundle);

            SSUtil.setFragment(requireActivity(), fragment, true);

            return true;

        }else if(id == R.id.action_delete){
            playerViewModel.deletePlayer(player);
            requireActivity().onBackPressed();

            return true;
        }

        return (super.onOptionsItemSelected(item));
    }

}