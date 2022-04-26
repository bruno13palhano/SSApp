package com.bruno13palhano.ssapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class SelectMatchSecondPlayer extends DialogFragment {
    private List<String> listPlayersNames;
    private int whichDefault = 0;
    private SelectMatchSecondPlayer.NoticeSelectMatchSecondPlayer listener;

    public interface NoticeSelectMatchSecondPlayer{
        void onDialogPositiveClick(int position);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    public void setOnPositiveClickListener(SelectMatchSecondPlayer.NoticeSelectMatchSecondPlayer noticeSelectMatchSecondPlayer) {
        this.listener = noticeSelectMatchSecondPlayer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] playersName = new String[listPlayersNames.size()];

        for(int i = 0; i < listPlayersNames.size(); i++){
            playersName[i] = listPlayersNames.get(i);
        }

        builder.setTitle("Segundo Jogador").setSingleChoiceItems(playersName, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                whichDefault = which;
            }
        })
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(listener != null) {
                    listener.onDialogPositiveClick(whichDefault);
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public void setListPlayers(List<String> listPlayersNames){
        this.listPlayersNames = listPlayersNames;
    }
}
