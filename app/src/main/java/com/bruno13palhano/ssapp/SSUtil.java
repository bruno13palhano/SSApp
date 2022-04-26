package com.bruno13palhano.ssapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bruno13palhano.ssapp.data.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SSUtil {
    public static final int RANK_MATCH_VALUE = 1;
    public static final int TOURNAMENT_VALUE = 1;

    public static Bitmap decodeUri(Context context, Uri uri, final int requiredSize)
            throws FileNotFoundException {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        InputStream inputStream1 = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(inputStream1, null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while (width_tmp / 2 >= requiredSize && height_tmp / 2 >= requiredSize) {
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        InputStream inputStream2 = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream2, null, o2);

        try {
            inputStream1.close();
            inputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void setFragment(FragmentActivity activity, Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =
                activity.getSupportFragmentManager().beginTransaction();
        if(addToBackStack) {
            fragmentTransaction.replace(R.id.main_content, fragment).addToBackStack(null);
        }else{
            fragmentTransaction.replace(R.id.main_content, fragment);
        }
        fragmentTransaction.commit();
    }

    public static int getPlayerScore(TextView playerScore){
        int score = 0;
        if(playerScore.getText() != null){
            score = Integer.parseInt(playerScore.getText().toString());
        }

        return score;
    }

    public static void playerPlusScore(TextView playerScore){
        int score = Integer.parseInt(playerScore.getText().toString());
        if(score >= 0){
            playerScore.setText(String.valueOf(++score));
        }
    }

    public static void playerLessScore(TextView playerScore){
        int score = Integer.parseInt(playerScore.getText().toString());
        if(score > 0){
            playerScore.setText(String.valueOf(--score));
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public CardViewHolder(@NonNull CardView itemView){
            super(itemView);
            cardView = itemView;
        }
    }

    public static List<String> parseListPLayerNamesInBackground(List<Player> playersList){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<String> playersNamesList = new ArrayList<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < playersList.size(); i++){
                    playersNamesList.add(playersList.get(i).getPlayerNickName());
                }

                executor.shutdown();
            }
        });

        return playersNamesList;
    }
}
