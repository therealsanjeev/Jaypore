package com.groupsale.Ecomm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.groupsale.Ecomm.viewHolder.RewardModelClass;

public class RewardAdapter extends FirebaseRecyclerAdapter<RewardModelClass,RewardAdapter.RewardsViewHolder> {

    LayoutInflater inflater;


    public RewardAdapter(@NonNull FirebaseRecyclerOptions<RewardModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RewardsViewHolder holder, int position, @NonNull RewardModelClass model) {

        holder.title.setText(model.getCardName());
        Glide.with(holder.gridIcon.getContext()).load(model.getCardImg()).into(holder.gridIcon);
        holder.progressBar.setProgress(model.getCardProgress());
        holder.progressNum.setText(String.valueOf((model.getCardPercentage()))+"%");
    }

    @NonNull
    @Override
    public RewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_grid,parent,false);
        return new RewardsViewHolder(view);
    }

    public class RewardsViewHolder extends RecyclerView.ViewHolder{

        TextView title; // Reward Type
        ImageView gridIcon; // Icon
        ProgressBar progressBar;
        TextView progressNum;
        public RewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.RewardName);
            gridIcon = itemView.findViewById(R.id.RewardImg);
            progressBar= itemView.findViewById(R.id.progressBarReward);
            progressNum=itemView.findViewById(R.id.loadingNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(),scratchCardActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(myIntent);
                    Toast.makeText(v.getContext(), "Clicked -> " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
