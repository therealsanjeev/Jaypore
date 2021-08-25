package com.groupsale.Ecomm.adapters;

import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.roomdatabase.RoomModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    private List<RoomModel> list;
    private onClicked onClick;

    public DealAdapter(List<RoomModel> list,onClicked onClick){
        this.list = list;
        this.onClick = onClick;
    }

    public void SetData(List<RoomModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new DealViewHolder(view);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {

        String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
        String arr[]={
          "https://static.jaypore.com/tr:w-500,h-662,e-sharpen/media/catalog/product/8/0/800028772-1_2.jpg",
          "https://static.jaypore.com/tr:w-500,h-662,e-sharpen/media/catalog/product/8/0/800028771-3_2.jpg",
                "https://static.jaypore.com/tr:w-500,h-662,e-sharpen/media/catalog/product/8/0/800028769-3_2.jpg",
                "https://static.jaypore.com/tr:w-500,h-662,e-sharpen/media/catalog/product/8/0/800028766-1_2.jpg"
        };
        int rnd = new Random().nextInt(arr.length);
        Picasso.get().load(arr[rnd]).into(holder.imageView);

        Log.d("TAG", "onBindViewHolder: "+list.get(position).getImageUrl().get(0).toString());

        String titleText=list.get(position).getName(),finalAboveText="";
        String[] splited = titleText.split("\\s+");
        for (String s:splited){
            finalAboveText+=s;
            finalAboveText+=" ";
        }
       if(splited.length>=7){
        finalAboveText="";
        for(int i=0;i<7;i++){
            finalAboveText+=splited[i];
            finalAboveText+=" ";
        }
       }

        holder.aboveText.setText(finalAboveText);
        Log.d("DEALADAPTER", "onBindViewHolder: "+list.get(position).getName());

        holder.price.setText("₹ " +(int) Double.parseDouble(list.get(position).getDealPrice()));
        holder.customerName.setText(list.get(position).getCreatorName());

        int price = (int) Double.parseDouble(list.get(position).getDealPrice());

        if (list.get(position).getOriginalPrice() != null) {
            int originalPrice = Integer.parseInt(list.get(position).getOriginalPrice());
            holder.strikePrice.setText("₹ " + String.valueOf(originalPrice));
            Log.d("Price", list.get(position).getOriginalPrice());
            int off = price * 100 / originalPrice;
            String offText = 100-off + "% Off";
            holder.flatOff.setText(offText);

        }
        if(!list.get(position).getPeopleLeft().equals(null))
        holder.peopleLeft.setText("People needed: " + list.get(position).getPeopleLeft());
        long elapsedTime = 86400000 - getElapsedTime(list.get(position).getEpochTime());
        new CountDownTimer(elapsedTime, 1000) {
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                String time = f.format(hour) + ":" + f.format(min) + ":" + f.format(sec);
                holder.timeLeft.setText(time);
            }

            public void onFinish() {
                holder.timeLeft.setText("00:00:00");
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        if (list==null)
            return 0;
        return list.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        public TextView customerName,aboveText,peopleLeft,timeLeft,strikePrice,price,flatOff;
        public Button listBtn;
        public ConstraintLayout constraintLayout;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemimage);

            customerName = itemView.findViewById(R.id.customername);
            aboveText = itemView.findViewById(R.id.abovetext);
            peopleLeft = itemView.findViewById(R.id.peopleleft);
            timeLeft = itemView.findViewById(R.id.timeleft);
            strikePrice = itemView.findViewById(R.id.strikePrice);
            strikePrice.setPaintFlags(strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price = itemView.findViewById(R.id.Price);
            flatOff = itemView.findViewById(R.id.flat_off);
            listBtn = itemView.findViewById(R.id.buttonVerify);
            constraintLayout=itemView.findViewById(R.id.itemView);

            constraintLayout.setOnClickListener(this);
            listBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClick.onItemClicked(getAdapterPosition(),list);
        }
    }

    private long getElapsedTime(long created) {
        Date date = new Date();
        long now = date.getTime();
        return now - created;
    }
    public interface onClicked{
        void onItemClicked(int position, List<RoomModel> list);
    }
}