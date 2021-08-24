package com.groupsale.Ecomm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.groupsale.Ecomm.databinding.SinglePostItemBinding;
import com.groupsale.Ecomm.models.PostModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {


    SinglePostItemBinding binding;
    private List<PostModel> postList;


    public class PostViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{

        ImageView postImage, profileImage;
        TextView text, name,likeCount;
        LottieAnimationView like;

        public PostViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            postImage = binding.postImage;
            profileImage=binding.postProfilePic;
            text = binding.postText;
            name = binding.postProfileName;
            like = binding.postLikeButton;
            likeCount= binding.postLikeCount;

            like.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
                    postList.get(getBindingAdapterPosition()).setLikes(postList.get(getBindingAdapterPosition()).getLikes()+1);
        }
    }

    @NonNull
    @NotNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        binding = SinglePostItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new PostViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostViewHolder holder, int position) {

        Picasso.get().load(postList.get(position).getPost_image_url()).into(holder.postImage);
        Picasso.get().load(postList.get(position).getProfile_image_url()).into(holder.profileImage);
        holder.text.setText(postList.get(position).getText());
        holder.likeCount.setText(String.valueOf(postList.get(position).getLikes()));
        holder.name.setText(postList.get(position).getName());
    }

    public void SetData(List<PostModel> list) {
        this.postList = list;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
