package com.bruno13palhano.ssapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bruno13palhano.ssapp.data.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectMatchPlayers extends DialogFragment {
    private List<Player> result;
    private List<Player> playersList;
    private SelectMatchPlayers.NoticeSelectPlayers listener;

    interface NoticeSelectPlayers{
        void onDialogPositiveClick(List<Player> players);
    }

    public void setOnPositiveClickListener(SelectMatchPlayers.NoticeSelectPlayers noticeSelectPlayers){
        this.listener = noticeSelectPlayers;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        result = new ArrayList<>();
        String[] playersNames = new String[playersList.size()];

        for(int i = 0; i < playersList.size(); i++){
            playersNames[i] = playersList.get(i).getPlayerNickName();
        }

        builder.setTitle("Selecione os Jogadores").setMultiChoiceItems(playersNames, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            if(!result.contains(playersList.get(which))){
                                try{
                                    result.add(playersList.get(which));
                                }catch (IndexOutOfBoundsException | NullPointerException ignored){

                                }
                            }

                        }else{
                            try{
                                result.remove(playersList.get(which));
                            }catch (IndexOutOfBoundsException | NullPointerException ignored){

                            }
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listener != null){
                            listener.onDialogPositiveClick(result);
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

    public void setListPlayers(List<Player> players){
        this.playersList = players;
    }
}
