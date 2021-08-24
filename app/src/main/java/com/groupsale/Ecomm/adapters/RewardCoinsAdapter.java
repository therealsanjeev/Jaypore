package com.groupsale.Ecomm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.models.RewardCoinsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RewardCoinsAdapter extends RecyclerView.Adapter<RewardCoinsAdapter.RewardsCoinViewHolder>  {

    private List<RewardCoinsModel> list;
    private RewardCoinsAdapter.onClicked onClick;
    private Context context;
    public RewardCoinsAdapter(onClicked onClick, Context context){
        this.onClick=onClick;
        this.context=context;
        
    }
    public class RewardsCoinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvCoins,rewardName;
        public ImageView imageView;
        public MaterialCardView cardView;
        public RewardsCoinViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoins=itemView.findViewById(R.id.tvCoins);
            cardView=itemView.findViewById(R.id.coinCardView);
            rewardName=itemView.findViewById(R.id.rewardCardName);
            imageView=itemView.findViewById(R.id.cardImage);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClick.onItemClicked(getAdapterPosition(),list);
        }
    }
    @NonNull
    @NotNull
    @Override
    public RewardsCoinViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_rewards_item, parent, false);
        return new RewardCoinsAdapter.RewardsCoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RewardsCoinViewHolder holder, int position) {
        holder.tvCoins.setText(list.get(position).getCoins());
        holder.rewardName.setText((CharSequence) list.get(position).getCardName());
        Glide.with(context).load(list.get(position).getCardImg()).error(R.drawable.ic_gift_rafiki__1_).into(holder.imageView);


    }


    public void SetData(List<RewardCoinsModel> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if(list==null)
            return 0;
        return list.size();
    }
    public interface onClicked{
        void onItemClicked(int position, List<RewardCoinsModel> list);
    }
}