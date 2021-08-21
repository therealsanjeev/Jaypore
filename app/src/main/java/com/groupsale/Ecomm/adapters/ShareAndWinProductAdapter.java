package com.groupsale.Ecomm.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.activities.RewardProductDetailsActivity;
import com.groupsale.Ecomm.databinding.SingleReOneProductItemBinding;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.utilities.ObjectWrapperForBinder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShareAndWinProductAdapter extends RecyclerView.Adapter<ShareAndWinProductAdapter.ShareAndWinProductViewHolder> {

    SingleReOneProductItemBinding binding;
    private List<DealModel> dealList;
    private Context context;
    private int pos;
    String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";

    @NonNull
    @NotNull
    @Override
    public ShareAndWinProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = SingleReOneProductItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new ShareAndWinProductAdapter.ShareAndWinProductViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShareAndWinProductViewHolder holder, int position) {

        holder.coinsNeeded.setText((int)Double.parseDouble(dealList.get(position).getOriginalPrice())/10+" ");
        binding.singleItemCoinImage.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_image_small));
        Picasso.get().load(imageBaseUrl+ dealList.get(position).getImageUrl().get(0).getFile()).into(holder.image);

    }

    public void SetData(List<DealModel> list) {
        this.dealList = list;
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }


    public class ShareAndWinProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView coinsNeeded;
        MaterialCardView card;

        public ShareAndWinProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            image = binding.re1image;
            coinsNeeded = binding.re1Coins;
            card = binding.coinCardView;

            binding.coinCardView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onClick(View v) {


            final Bundle bundle = new Bundle();
            bundle.putString("passingActivity","ShareAndWin");
            bundle.putBinder("passedDealFromReward", new ObjectWrapperForBinder(dealList.get(getBindingAdapterPosition())));
            context.startActivity(new Intent(context, RewardProductDetailsActivity.class).putExtras(bundle));
        }
    }


}
