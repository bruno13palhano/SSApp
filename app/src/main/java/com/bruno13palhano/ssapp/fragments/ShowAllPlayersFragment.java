package com.bruno13palhano.ssapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;

public class ShowAllPlayersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_player, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_player_fragment);

        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), players -> {

        });

        ImageView playerImage = view.findViewById(R.id.show_player_image_icon);
        TextView playerName = view.findViewById(R.id.show_text_player_name);
        TextView playerScoreTotal = view.findViewById(R.id.show_text_player_score_total);
        TextView playerScoreMatch = view.findViewById(R.id.show_text_player_score_match);
        TextView playerScoreSeries = view.findViewById(R.id.show_text_player_score_series);
        TextView playerNumberOfWonTournaments = view.findViewById(R.id.show_text_player_number_of_won_tournaments);
        TextView inputPlayerNumberOfWins = view.findViewById(R.id.show_text_player_number_of_wins);
        TextView playerNumberOfMatches = view.findViewById(R.id.show_text_player_number_of_matches);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            // TODO: 15/04/2022 comprimir essas imagens.
            playerImage.setImageURI(Uri.parse(bundle.getString("imageUri")));
            playerName.setText(bundle.getString("nickname"));
            playerScoreTotal.setText(String.valueOf(bundle.getInt("scoreTotal")).concat(" Pts."));
            playerScoreMatch.setText(String.valueOf(bundle.getInt("scoreMatch")).concat(" Pts."));
            playerScoreSeries.setText(String.valueOf(bundle.getInt("scoreSeries")).concat(" Pts."));
            playerNumberOfWonTournaments.setText(String.valueOf(bundle.getInt("numberOfWonTournaments")).concat("."));
            inputPlayerNumberOfWins.setText(String.valueOf(bundle.getInt("numberOfWins")));
            playerNumberOfMatches.setText(String.valueOf(bundle.getInt("numberOfMatches")).concat("."));
        }

        return view;
    }
}
