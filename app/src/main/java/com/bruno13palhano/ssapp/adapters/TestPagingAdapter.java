package com.bruno13palhano.ssapp.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class TestPagingAdapter extends PagingDataAdapter<Player, SSUtil.CardViewHolder> {
    private Map<Long, Bitmap> bitmaps;

    public void setBitmaps( Map<Long, Bitmap> bitmaps){
        this.bitmaps = bitmaps;
    }

    public TestPagingAdapter(@NotNull DiffUtil.ItemCallback<Player> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SSUtil.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_card, parent, false);

        return new SSUtil.CardViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull SSUtil.CardViewHolder holder, int position) {
        Player currentPLayer = getItem(position);
        CardView cardView = holder.cardView;

        TextView playerName = cardView.findViewById(R.id.player_card_name);
        ImageView playerImage = cardView.findViewById(R.id.player_card_image);

        try{
            playerName.setText(currentPLayer.getPlayerNickName());
            playerImage.setImageBitmap(bitmaps.get(currentPLayer.getPlayerId()));

        }catch (NullPointerException | IndexOutOfBoundsException ignored){

        }
    }

    public static class PlayerComparator extends DiffUtil.ItemCallback<Player> {
        @Override
        public boolean areItemsTheSame(@NonNull Player oldItem,
                                       @NonNull Player newItem) {

            return oldItem.getPlayerId() == newItem.getPlayerId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Player oldItem,
                                          @NonNull Player newItem) {

            return oldItem.getPlayerNickName().equals(newItem.getPlayerNickName());
        }
    }
}
