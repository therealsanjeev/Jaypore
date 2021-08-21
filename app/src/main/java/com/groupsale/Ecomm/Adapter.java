package com.groupsale.Ecomm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<ModelClass> userList;

    public Adapter(List<ModelClass> userList) {
        this.userList = userList;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        int resource = userList.get(position).getImageview();
        String name = userList.get(position).getTextview1();
        String desc = userList.get(position).getTextview2();
        String time = userList.get(position).getTextview3();

        holder.setData(resource, name, desc, time);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;
        private final TextView textView2;
        private final TextView textview3; //clock


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //here use xml ids
            //give different name not like constructor
            imageView = itemView.findViewById(R.id.imageview);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            textview3 = itemView.findViewById(R.id.textview3);


        }

        public void setData(int resource, String name, String desc, String time) {

            imageView.setImageResource(resource);
            textView.setText(name);
            textView2.setText(desc);
            textview3.setText(time);


        }
    }
}