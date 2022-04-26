package com.bruno13palhano.ssapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;

public class SelectSeriesDialog extends DialogFragment {
    private NoticeNumberOfMatches listener;

    public void setOnPositiveClickListener(NoticeNumberOfMatches listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_series, null);

        Button buttonLess = view.findViewById(R.id.dialog_series_button_less);
        Button buttonPlus = view.findViewById(R.id.dialog_series_button_plus);
        TextView score = view.findViewById(R.id.dialog_series_score);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSUtil.playerPlusScore(score);
            }
        });

        buttonLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSUtil.playerLessScore(score);
            }
        });

        builder.setView(view)
                .setTitle(R.string.label_menu_item_matches_per_series)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listener != null){
                            listener.onDialogPositiveClick(Integer.parseInt(score.getText().toString()));
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                }
        });

        return builder.create();
    }

    public interface NoticeNumberOfMatches{
        void onDialogPositiveClick(int numberOfMatches);
    }
}
