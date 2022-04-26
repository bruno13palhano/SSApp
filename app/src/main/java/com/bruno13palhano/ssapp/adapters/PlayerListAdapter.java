package com.bruno13palhano.ssapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Player;

import java.util.List;

public class PlayerListAdapter extends ListAdapter<Player, SSUtil.CardViewHolder> {
    private PlayerListAdapter.OnRecyclerItemClickListener deleteListener, recyclerListener;

    private List<Bitmap> bitmaps;

    public interface OnRecyclerItemClickListener{
        void onClick(int position);
    }

    public void setOnDeleteIconClickListener(OnRecyclerItemClickListener deleteListener){
        this.deleteListener = deleteListener;
    }

    public void setOnRecyclerClickListener(OnRecyclerItemClickListener recyclerListener){
        this.recyclerListener = recyclerListener;
    }

    public PlayerListAdapter(@NonNull DiffUtil.ItemCallback<Player> diffCallback) {
        super(diffCallback);
    }

    public void setBitmaps( List<Bitmap> bitmaps){
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public SSUtil.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_card, parent, false);

        return new SSUtil.CardViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull SSUtil.CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CardView cardView = holder.cardView;
        Player currentPlayer = getItem(position);

        TextView name = cardView.findViewById(R.id.player_card_name);
        name.setText(currentPlayer.getPlayerNickName());
        ImageView imageURI = cardView.findViewById(R.id.player_card_image);
        FrameLayout deleteIcon = cardView.findViewById(R.id.player_card_delete_icon);
        deleteIcon.setContentDescription("Delete Button");

        try{
            imageURI.setImageBitmap(bitmaps.get(position));
        }catch (NullPointerException | IndexOutOfBoundsException ignored){

        }

        cardView.setOnClickListener((view)->{
            if(recyclerListener != null){
                recyclerListener.onClick(position);
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteListener != null){
                    deleteListener.onClick(position);
                }
            }
        });
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
