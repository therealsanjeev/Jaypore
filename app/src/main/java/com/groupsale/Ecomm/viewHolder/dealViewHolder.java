package com.groupsale.Ecomm.viewHolder;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.interfaces.ItemClickListener;

public class dealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView itemImage;
    public TextView customerName,aboveText,peopleLeft,timeLeft,strikePrice,price,flatOff;
    public ItemClickListener listener;

    public dealViewHolder(@NonNull View itemView) {
        super(itemView);

        itemImage = itemView.findViewById(R.id.itemimage);
        customerName = itemView.findViewById(R.id.customername);
        aboveText = itemView.findViewById(R.id.abovetext);
        peopleLeft = itemView.findViewById(R.id.peopleleft);
        timeLeft = itemView.findViewById(R.id.timeleft);
        strikePrice = itemView.findViewById(R.id.strikePrice);
        strikePrice.setPaintFlags(strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        price = itemView.findViewById(R.id.Price);
        flatOff = itemView.findViewById(R.id.flat_off);


    }


    @Override
    public void onClick(View v) {

        listener.onClick(v, getAbsoluteAdapterPosition(),false);

    }
}
