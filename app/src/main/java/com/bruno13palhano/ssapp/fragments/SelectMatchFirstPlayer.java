package com.bruno13palhano.ssapp.fragments;


import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class SelectMatchFirstPlayer extends DialogFragment {
    private List<String> listPlayersNames;
    private int whichDefault = 0;
    private SelectMatchFirstPlayer.NoticeSelectMatchFirstPlayer listener;

    public interface NoticeSelectMatchFirstPlayer{
        void onDialogPositiveClick(int positionFirstPlayer, int positionSecondPlayer);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    public void setOnPositiveClickListener(SelectMatchFirstPlayer.NoticeSelectMatchFirstPlayer noticeSelectMatchFirstPlayer){
        this.listener = noticeSelectMatchFirstPlayer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] playersName = new String[listPlayersNames.size()];

        for(int i = 0; i < listPlayersNames.size(); i++){
            playersName[i] = listPlayersNames.get(i);
        }

        builder.setTitle("Primeiro Jogador").setSingleChoiceItems(playersName, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whichDefault = which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectMatchSecondPlayer selectMatchSecondPlayer = new SelectMatchSecondPlayer();
                        selectMatchSecondPlayer.setOnPositiveClickListener(new SelectMatchSecondPlayer.NoticeSelectMatchSecondPlayer() {
                            @Override
                            public void onDialogPositiveClick(int position) {
                                if(listener != null) {
                                    listener.onDialogPositiveClick(whichDefault, position);
                                }
                            }

                            @Override
                            public void onDialogNegativeClick(DialogFragment dialogFragment) {

                            }
                        });
                        selectMatchSecondPlayer.setListPlayers(listPlayersNames);
                        selectMatchSecondPlayer.show(getActivity().getSupportFragmentManager(), "primeirojogador");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public void setListPlayers(List<String> listPlayers){
        this.listPlayersNames = listPlayers;
    }
}
