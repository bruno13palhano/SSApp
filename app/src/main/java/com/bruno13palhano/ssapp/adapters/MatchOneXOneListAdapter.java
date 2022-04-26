package com.bruno13palhano.ssapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Match;

import java.util.List;

public class MatchOneXOneListAdapter extends ListAdapter<Match, SSUtil.CardViewHolder> {
    private List<Bitmap> listFirstBitmaps, listSecondBitmaps;
    private MatchOneXOneListAdapter.OnRecyclerItemClickListener onRecyclerItemClickListener;
    private MatchOneXOneListAdapter.OnPlusLessClickListener firstScoreListener, secondScoreListener;
    private MatchOneXOneListAdapter.OnLongItemClickListener onLongItemClickListener;
    private boolean isSeries;
    private int scorePerSeries;

    OnViewUpdate firstPlayerSeriesListener, secondPLayerSeriesListener;

    public void setOnFirstPlayerSeriesListener(OnViewUpdate firstPlayerSeriesListener){
        this.firstPlayerSeriesListener = firstPlayerSeriesListener;
    }

    public void setOnSecondPlayerSeriesListener(OnViewUpdate secondPlayerSeriesListener){
        this.secondPLayerSeriesListener = secondPlayerSeriesListener;
    }

    public interface OnViewUpdate{
        void onViewUpdate(int score, int position);
    }

    public void setSeriesLayout(boolean isSeries){
        this.isSeries = isSeries;
    }
    public interface OnLongItemClickListener{
        void itemLongClick(View v, int position);
    }

    public interface OnRecyclerItemClickListener{
        void onClick(int position);
    }

    public interface OnPlusLessClickListener{
        void onClick(int oldScore, int newScore, int position);
    }

    public void setOnLongItemClickListener(MatchOneXOneListAdapter.OnLongItemClickListener onLongItemClickListener){
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public void setOnRecyclerItemClickListener(MatchOneXOneListAdapter.OnRecyclerItemClickListener onRecyclerItemClickListener){
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void setFirstScoreClickListener(MatchOneXOneListAdapter.OnPlusLessClickListener firstScoreListener){
        this.firstScoreListener = firstScoreListener;
    }

    public void setSecondScoreClickListener(MatchOneXOneListAdapter.OnPlusLessClickListener secondScoreListener){
        this.secondScoreListener = secondScoreListener;
    }

    public void setBitmaps(List<Bitmap> listFirstBitmaps, List<Bitmap> listSecondBitmaps){
        this.listFirstBitmaps = listFirstBitmaps;
        this.listSecondBitmaps = listSecondBitmaps;
    }

    public MatchOneXOneListAdapter(@NonNull DiffUtil.ItemCallback<Match> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SSUtil.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv;

        if(isSeries) {
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.series_card, parent, false);
        }else{
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycer_test, parent, false);
        }

        return new SSUtil.CardViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull SSUtil.CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CardView cardView = holder.cardView;
        Match currentMatch = getItem(position);

        ImageView firstPlayerImageUri = cardView.findViewById(R.id.match_first_player_card_image);
        TextView firstPlayerNickname = cardView.findViewById(R.id.match_first_player_name);
        TextView firstPlayerScore = cardView.findViewById(R.id.match_edit_first_player_score);
        ImageView secondPlayerImageUri = cardView.findViewById(R.id.match_second_player_card_image);
        TextView secondPlayerNickname = cardView.findViewById(R.id.match_second_player_name);
        TextView secondPlayerScore = cardView.findViewById(R.id.match_edit_second_player_score);
        //Botões de aumentar e reduzir  a pontuação dos jogadores.
        ImageView firstPlayerPlusScore = cardView.findViewById(R.id.match_first_player_score_plus);
        ImageView firstPlayerLessScore = cardView.findViewById(R.id.match_first_player_score_less);
        ImageView secondPlayerPlusScore = cardView.findViewById(R.id.match_second_player_score_plus);
        ImageView secondPlayerLessScore = cardView.findViewById(R.id.match_second_player_score_less);

        ImageView firstPlayerStatus = cardView.findViewById(R.id.match_first_player_status_icon);
        ImageView secondPlayerStatus = cardView.findViewById(R.id.match_second_player_status_icon);

        try{
            firstPlayerImageUri.setImageBitmap(listFirstBitmaps.get(position));
            secondPlayerImageUri.setImageBitmap(listSecondBitmaps.get(position));
        }catch (IndexOutOfBoundsException | NullPointerException ignored){

        }

        firstPlayerNickname.setText(currentMatch.getFirstPlayerNickname());
        firstPlayerScore.setText(String.valueOf(currentMatch.getFirstPlayerScoreMatch()));
        secondPlayerNickname.setText(currentMatch.getSecondPlayerNickname());
        secondPlayerScore.setText(String.valueOf(currentMatch.getSecondPlayerScoreMatch()));
        scorePerSeries = currentMatch.getMatchesPerSeries();

        int firstScore = Integer.parseInt(String.valueOf(firstPlayerScore.getText()));
        int secondScore = Integer.parseInt(String.valueOf(secondPlayerScore.getText()));

        if(firstScore == secondScore){
            firstPlayerStatus.setBackgroundColor(cardView.getContext().getColor(R.color.white));
            secondPlayerStatus.setBackgroundColor(cardView.getContext().getColor(R.color.white));

        }else if(firstScore > secondScore){
            firstPlayerStatus.setBackgroundColor(cardView.getContext().getColor(R.color.green));
            secondPlayerStatus.setBackgroundColor(cardView.getContext().getColor(R.color.red));


        }else{
            secondPlayerStatus.setBackgroundColor(cardView.getContext().getColor(R.color.green));
            firstPlayerStatus.setBackgroundColor(cardView.getContext().getColor(R.color.red));
        }

        if(currentMatch.isMatchFinished()){
            cardView.setBackgroundColor(cardView.getContext().getColor(R.color.orange));
            firstPlayerPlusScore.setClickable(false);
            firstPlayerLessScore.setClickable(false);
            secondPlayerPlusScore.setClickable(false);
            secondPlayerLessScore.setClickable(false);
        }else{
            cardView.setBackgroundColor(cardView.getContext().getColor(com.google.android.material.R.color.material_grey_850));

            firstPlayerPlusScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstScoreListener != null) {
                        int newScore = setNewScoreMatch(firstPlayerScore, true);
                        int oldScore = setOldScoreMatch(newScore, true);

                        firstScoreListener.onClick(oldScore, newScore, position);
                    }
                }
            });

            firstPlayerLessScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstScoreListener != null) {
                        int newScore = setNewScoreMatch(firstPlayerScore, false);
                        int oldScore = setOldScoreMatch(newScore, false);

                        firstScoreListener.onClick(oldScore, newScore, position);
                    }
                }
            });

            secondPlayerPlusScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (secondScoreListener != null) {
                        int newScore = setNewScoreMatch(secondPlayerScore, true);
                        int oldScore = setOldScoreMatch(newScore, true);

                        secondScoreListener.onClick(oldScore, newScore, position);
                    }
                }
            });

            secondPlayerLessScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (secondScoreListener != null) {
                        int newScore = setNewScoreMatch(secondPlayerScore, false);
                        int oldScore = setOldScoreMatch(newScore, false);

                        secondScoreListener.onClick(oldScore, newScore, position);
                    }
                }
            });
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecyclerItemClickListener != null){
                    onRecyclerItemClickListener.onClick(position);
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onLongItemClickListener != null){
                    onLongItemClickListener.itemLongClick(v, position);
                }
                return true;
            }
        });


        if(isSeries){
            TextView textViewScorePerSeries = cardView.findViewById(R.id.series_score_per_match);
            TextView textViewScoreFirstPlayerSeries = cardView.findViewById(R.id.series_edit_first_player_score);
            TextView textViewScoreSecondPlayerSeries = cardView.findViewById(R.id.series_edit_second_player_score);


            textViewScoreFirstPlayerSeries.setText(String.valueOf(currentMatch.getFirstPlayerScoreSeries()));
            textViewScoreSecondPlayerSeries.setText(String.valueOf(currentMatch.getSecondPlayerScoreSeries()));

            textViewScorePerSeries.setText(String.valueOf(scorePerSeries));

            if(firstScore > 0 && firstScore == scorePerSeries){
                SSUtil.playerPlusScore(textViewScoreFirstPlayerSeries);
                firstPlayerScore.setText("0");
                secondPlayerScore.setText("0");

                if(firstPlayerSeriesListener != null){
                    firstPlayerSeriesListener.onViewUpdate(SSUtil.getPlayerScore(textViewScoreFirstPlayerSeries), position);
                }

                // TODO: 18/04/2022 os listeners funcionam, falta tira o bug do placar esalvar no db...
            }else if(secondScore > 0 && secondScore == scorePerSeries){
                SSUtil.playerPlusScore(textViewScoreSecondPlayerSeries);
                firstPlayerScore.setText("0");
                secondPlayerScore.setText("0");

                if(secondPLayerSeriesListener != null){
                    secondPLayerSeriesListener.onViewUpdate(SSUtil.getPlayerScore(textViewScoreSecondPlayerSeries), position);
                }
            }
        }
    }

    public int setOldScoreMatch(int newScore, boolean isPlus){
        if(isPlus && newScore > 0){
            return newScore -1;

        }else if(newScore >= 0){
            return newScore +1;
        }

        return 0;
    }

    public int setNewScoreMatch(TextView playerScore, boolean isPlus){
        if(isPlus){
            SSUtil.playerPlusScore(playerScore);
        }else{
            SSUtil.playerLessScore(playerScore);
        }
        return SSUtil.getPlayerScore(playerScore);
    }

    public static class MatchDiff extends DiffUtil.ItemCallback<Match>{
        @Override
        public boolean areItemsTheSame(@NonNull Match oldItem, @NonNull Match newItem) {
            return oldItem.getMatchId() == newItem.getMatchId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Match oldItem, @NonNull Match newItem) {
            return oldItem.getFirstPlayerNickname().equals(newItem.getFirstPlayerNickname());
        }
    }
}
