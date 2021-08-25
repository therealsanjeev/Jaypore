package com.groupsale.Ecomm;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.databinding.ActivityProductDescriptionBinding;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class productDescription extends AppCompatActivity {

    ImageSlider mainslider;
    ActivityProductDescriptionBinding binding;

    private DealModel currrentDeal;
    String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
    TextView productDescription;
    ImageView backBtn;


    final List<SlideModel> remoteimages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Bundle bundle = getIntent().getExtras();
        String dealID = bundle.getString("passedDealID");

        productDescription = binding.productDescription;
        mainslider = binding.imageSlider;
        backBtn =binding.backProductBtn;
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        setData(dealID);
    }

    private void setData(String dealID) {

        String substring = dealID.length() > 2 ? dealID.substring(dealID.length() - 2) : dealID;

        DatabaseReference dealref;

        if(substring.equals("SW")){
           dealref = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_REWARD_DEAL_DB);
        }
        else {
           dealref = FirebaseDatabase.getInstance().getReference().child("dealsDB");
        }

        dealref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(dealID).exists()){

                    currrentDeal = dataSnapshot.child(dealID).getValue(DealModel.class);

                    for(int i=0; i<currrentDeal.getImageUrl().size();i++){



                        remoteimages.add(new SlideModel(currrentDeal.getImageUrl().get(i).getFile(), ScaleTypes.FIT)); // for one image

                    }

                    mainslider.setImageList(remoteimages,ScaleTypes.FIT);


                    productDescription.setText(Html.fromHtml(currrentDeal.getDescription()));
                    binding.title.setText(currrentDeal.getName());
                    binding.Price.setText("₹ "+(int)Double.parseDouble(currrentDeal.getDealPrice()));
                    binding.strikePrice.setPaintFlags(binding.strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.strikePrice.setText("₹ "+String.valueOf((int)Double.parseDouble(currrentDeal.getOriginalPrice())));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(productDescription.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}