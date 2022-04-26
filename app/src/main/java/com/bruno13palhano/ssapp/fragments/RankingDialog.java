package com.bruno13palhano.ssapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.adapters.RankingAdapter;
import com.bruno13palhano.ssapp.data.Player;

import java.util.List;

public class RankingDialog extends DialogFragment {
    private List<Bitmap> playersBitmapList;
    private List<Player> playerList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_ranking_dialog, null);

        RecyclerView recyclerView = view.findViewById(R.id.ranking_recycler);
        RankingAdapter rankingAdapter = new RankingAdapter(new RankingAdapter.PlayerDiff());
        recyclerView.setAdapter(rankingAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        if(playerList != null){
            rankingAdapter.submitList(playerList);
        }
        if(playersBitmapList != null){
            rankingAdapter.setPlayerBitmaps(playersBitmapList);
        }

        builder.setView(view)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public void setPlayersBitmapList(List<Bitmap> playersBitmapList){
        this.playersBitmapList = playersBitmapList;
    }

    public void setPlayerList(List<Player> playerList){
        this.playerList = playerList;
    }
}
