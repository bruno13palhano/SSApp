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

import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Rotation;

import java.util.List;

public class RotationMatchListAdapter extends ListAdapter<Rotation, SSUtil.CardViewHolder> {
    private OnScoreClickListener onScoreClickListener;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private OnLongItemClickListener onLongItemClickListener;

    private List<Bitmap> bitmaps;

    public interface OnLongItemClickListener{
        void onLongClick(View v, int position);
    }

    public interface OnRecyclerItemClickListener{
        void onClick(int position);
    }

    public interface OnScoreClickListener{
        void onClick(int score, int position);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener){
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener){
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void setOnScoreClickListener(OnScoreClickListener onScoreClickListener){
        this.onScoreClickListener = onScoreClickListener;
    }

    public void setBitmaps(List<Bitmap> bitmaps){
        this.bitmaps = bitmaps;
    }

    public RotationMatchListAdapter(@NonNull DiffUtil.ItemCallback<Rotation> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SSUtil.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rotation_match_card, parent, false);

        return new SSUtil.CardViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull SSUtil.CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CardView cardView = holder.cardView;
        Rotation currentRotationPlayer = getItem(position);

        ImageView image = cardView.findViewById(R.id.rotation_match_card_image);
        TextView nickname = cardView.findViewById(R.id.rotation_match_card_name);
        TextView score = cardView.findViewById(R.id.rotation_match_card_score);
        FrameLayout plusIcon = cardView.findViewById(R.id.rotation_match_card_plus_icon);
        FrameLayout lessIcon = cardView.findViewById(R.id.rotation_match_card_less_icon);

        nickname.setText(currentRotationPlayer.getNickname());
        score.setText(String.valueOf(currentRotationPlayer.getScore()));

        try{
            image.setImageBitmap(bitmaps.get(position));
        }catch (IndexOutOfBoundsException | NullPointerException ignored){

        }

        plusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onScoreClickListener != null){
                    SSUtil.playerPlusScore(score);
                    onScoreClickListener.onClick(SSUtil.getPlayerScore(score), position);
                }
            }
        });

        lessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onScoreClickListener != null){
                    SSUtil.playerLessScore(score);
                    onScoreClickListener.onClick(SSUtil.getPlayerScore(score), position);
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onLongItemClickListener != null){
                    onLongItemClickListener.onLongClick(v, position);
                }
                return true;
            }
        });
    }

    public static class PlayerDiff extends DiffUtil.ItemCallback<Rotation>{

        @Override
        public boolean areItemsTheSame(@NonNull Rotation oldItem, @NonNull Rotation newItem) {
            return oldItem.getPlayerId() == newItem.getPlayerId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Rotation oldItem, @NonNull Rotation newItem) {
            return oldItem.getNickname().equals(newItem.getNickname());
        }
    }
}
