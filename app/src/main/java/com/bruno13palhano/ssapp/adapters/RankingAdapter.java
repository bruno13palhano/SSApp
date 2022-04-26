package com.bruno13palhano.ssapp.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Player;

import java.util.List;

public class RankingAdapter extends ListAdapter<Player, SSUtil.CardViewHolder> {
    private List<Bitmap> playerBitmaps;

    public void setPlayerBitmaps(List<Bitmap> playerBitmaps){
        this.playerBitmaps = playerBitmaps;
    }

    public RankingAdapter(@NonNull DiffUtil.ItemCallback<Player> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SSUtil.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_ranking_card, parent, false);
        return new SSUtil.CardViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull SSUtil.CardViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        Player currentPlayer = getItem(position);

        TextView playerRankingPosition = cardView.findViewById(R.id.player_position);
        TextView playerName = cardView.findViewById(R.id.player_name);
        ImageView playerImage = cardView.findViewById(R.id.player_image);
        TextView playerScore = cardView.findViewById(R.id.player_score);

        playerRankingPosition.setText(String.valueOf(position + 1).concat("°."));
        playerName.setText(currentPlayer.getPlayerNickName());
        playerScore.setText(String.valueOf(currentPlayer.getPlayerScoreTotal()).concat(" Pts."));

        try{
            playerImage.setImageBitmap(playerBitmaps.get(position));

        }catch (NullPointerException | IndexOutOfBoundsException ignored){

        }
    }

    public static class PlayerDiff extends DiffUtil.ItemCallback<Player>{

        @Override
        public boolean areItemsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return oldItem.getPlayerId() == newItem.getPlayerId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return oldItem.getPlayerNickName().equals(newItem.getPlayerNickName());
        }
    }
}
