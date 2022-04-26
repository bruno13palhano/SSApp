package com.bruno13palhano.ssapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.data.Player;

import java.util.List;

public class TopFiveDialog extends DialogFragment {
    private List<Bitmap> playersBitmapList;
    private List<Player> playerList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_top_five, null);

        ImageView[] playersImages = new ImageView[5];
        playersImages[0] = view.findViewById(R.id.player_1_image);
        playersImages[1] = view.findViewById(R.id.player_2_image);
        playersImages[2] = view.findViewById(R.id.player_3_image);
        playersImages[3] = view.findViewById(R.id.player_4_image);
        playersImages[4] = view.findViewById(R.id.player_5_image);

        TextView[] playersNames = new TextView[5];
        playersNames[0] = view.findViewById(R.id.player_1_name);
        playersNames[1] = view.findViewById(R.id.player_2_name);
        playersNames[2] = view.findViewById(R.id.player_3_name);
        playersNames[3] = view.findViewById(R.id.player_4_name);
        playersNames[4] = view.findViewById(R.id.player_5_name);

        TextView[] playersScores = new TextView[5];
        playersScores[0] = view.findViewById(R.id.player_1_score);
        playersScores[1] = view.findViewById(R.id.player_2_score);
        playersScores[2] = view.findViewById(R.id.player_3_score);
        playersScores[3] = view.findViewById(R.id.player_4_score);
        playersScores[4] = view.findViewById(R.id.player_5_score);

        if(playersBitmapList != null && playerList != null){
            for(int i = 0; i < playerList.size(); i++){
                playersImages[i].setImageBitmap(playersBitmapList.get(i));
                playersNames[i].setText(playerList.get(i).getPlayerNickName());
                playersScores[i].setText(String.valueOf(playerList.get(i).getPlayerScoreTotal()).concat(" Pts."));
            }
        }

        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
