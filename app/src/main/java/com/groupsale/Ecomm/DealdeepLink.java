package com.groupsale.Ecomm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.databinding.ActivityDealDeepLinkBinding;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.customer;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DealdeepLink extends AppCompatActivity {


    private ImageView image;
    private TextView title;
    private ImageView share,info;
    private ProgressBar loading;
    private Button joinNow;
    private String dealID,substring,currentUserID;
    private int flag;
    private ActivityDealDeepLinkBinding binding;
    private LottieAnimationView heart;
    private Handler handler = new Handler();
    private DealModel currentDeal;
    private DatabaseReference dealref;
    PreferenceManager preferenceManager;

    MediaPlayer player = new MediaPlayer();
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";

    private customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDealDeepLinkBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(DealdeepLink.this);
        currentUserID = preferenceManager.getString(Constants.KEY_CUSTOMER_ID);

        image=binding.image;
        share = binding.storyPageShare;
        heart = binding.storyPageLike;
        info = binding.INFO;
        title = binding.Title;
        joinNow = binding.storyPageJoinNow;
        loading = binding.progressbarStatusDeal;

        Uri uri = getIntent().getData();

        info.setOnClickListener(v -> {

            if(dealID!=null) {
                Intent intent = new Intent(DealdeepLink.this, productDescription.class);
                intent.putExtra("passedDealID", dealID);
                startActivity(intent);
            }

            else Toast.makeText(DealdeepLink.this, "Error in fetching deal", Toast.LENGTH_SHORT).show();
        });

        share.setOnClickListener(v -> {
            if(dealID!=null) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Please join my deal by clicking on https://www.grousale.com/deal/" + dealID;
                myIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(myIntent, "Share deal using"));
            }
        });

        heart.setOnClickListener(v -> {

            heart.playAnimation();
            if (dealID != null) {

                DatabaseReference ref;
                if(substring.equals("SW")) {
                    ref = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_REWARD_DEAL_DB).child(dealID);
                }
                else {
                    ref = FirebaseDatabase.getInstance().getReference().child("dealsDB").child(dealID);
                }
                ref.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        DealModel d = mutableData.getValue(DealModel.class);

                        if (d == null) {
                            return Transaction.success(mutableData);
                        }



                        Integer likes = d.getLikeCounter();
                        if (likes == null) {
                            d.setLikeCounter(1);
                        } else {
                            d.setLikeCounter(likes + 1);
                        }

                        List<String> list = new ArrayList<>();

                        if (d.getCurrentLikers().size()>0) {
                            list = d.getCurrentLikers();
                            for (int i = 0; i<list.size(); i++) {
                                if (list.get(i).equals(currentUserID)){
                                    flag = 3;
                                    return Transaction.success(mutableData);
                                }
                            }
                        }
                        list.add(currentUserID);
                        d.setCurrentLikers(list);

                        mutableData.setValue(d);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                        if(flag==3){
                            Toast.makeText(DealdeepLink.this, "You have already liked this deal", Toast.LENGTH_SHORT).show();
                        }

                        if(!b){
                            Toast.makeText(DealdeepLink.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                            Log.d("SW",databaseError.getDetails());
                        }

                        if(b && flag!=3){
                            Toast.makeText(DealdeepLink.this, "Liked successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            else Toast.makeText(DealdeepLink.this, "Error in fetching deal", Toast.LENGTH_SHORT).show();
        });

        joinNow.setOnClickListener(v -> {

            if(substring.equals("SW")){
                heart.performClick();
                return;
            }

            flag =0;
            loading.setVisibility(View.VISIBLE);
            addToSubscribersInDeal();

        });


        if (uri != null) {

            List<String> parameters = uri.getPathSegments();

            dealID = parameters.get(parameters.size() - 1);

            substring = dealID.length() > 2 ? dealID.substring(dealID.length() - 2) : dealID;

            //SW checks if its share and win deal
            if(substring.equals("SW")) {
                dealref = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_REWARD_DEAL_DB);
                binding.storyPageJoinNow.setText("Like!");
                binding.INFO.setVisibility(View.INVISIBLE);
            }
            else dealref = FirebaseDatabase.getInstance().getReference().child("dealsDB");

            dealref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(dealID).exists()){

                        currentDeal = dataSnapshot.child(dealID).getValue(DealModel.class);
                        Picasso.get().load(currentDeal.getImageUrl().get(0).getFile()).into(image); // chamged with
                        title.setText(currentDeal.getName());
                        binding.Price.setText("₹"+String.valueOf((int)Double.parseDouble(currentDeal.getDealPrice())));
                        binding.strikePrice.setPaintFlags(binding.strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        binding.strikePrice.setText("₹"+String.valueOf((int)Double.parseDouble(currentDeal.getOriginalPrice())));

                        binding.flatOff.setText("Flat Off : "+String.valueOf(100-((int)Double.parseDouble(currentDeal.getDealPrice())*100/(int)Double.parseDouble(currentDeal.getOriginalPrice())))+"%");

                        long audioDuration=0L;


                        if(currentDeal.getAudioFileLink()!=null){

                            mmr.setDataSource(currentDeal.getAudioFileLink(),new HashMap<String,String>());
                            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            audioDuration = Integer.parseInt(durationStr);


                            Uri uri = Uri.parse(currentDeal.getAudioFileLink());
                            try {
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                player.setDataSource(DealdeepLink.this, uri);

                                player.prepare();
                                player.start();
                            } catch(Exception e) {
                                System.out.println(e.toString());
                            }
                        }

                    }

                    else {
                        Toast.makeText(DealdeepLink.this, "Deal has expired", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DealdeepLink.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    private void addToSubscribedDealInCustomer() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customerDB").child(currentUserID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customer c = dataSnapshot.getValue(customer.class);
                List<String> list = new ArrayList<>();
                if(c.getCurrentDeal()!=null)  list = c.getCurrentDeal();
                list.add(dealID);
                c.setCurrentDeal(list);

                ref.child("currentDeal").setValue(list).addOnFailureListener(e -> {
                    Toast.makeText(DealdeepLink.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                })
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DealdeepLink.this, "Deal joined successfully!.", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Status Deal",databaseError.getDetails());

            }
        });

    }

    private void addToSubscribersInDeal() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dealsDB").child(dealID);
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                DealModel d = mutableData.getValue(DealModel.class);

                if(d==null){
                    return Transaction.success(mutableData);
                }

                int peopleNeeded = Integer.parseInt(d.getPeopleLeft());

                if(peopleNeeded==0){
                    flag=2;
                    return Transaction.success(mutableData);
                }

                else{
                    peopleNeeded--;
                    d.setPeopleLeft(String.valueOf(peopleNeeded));
                }

                List<String> list = new ArrayList<>();

                if (d.getCurrentSubscribers().size()>0) {
                    list = d.getCurrentSubscribers();
                    for (int i = 0; i<list.size(); i++) {
                        if (list.get(i).equals(currentUserID)) {
                            flag = 3;
                            return Transaction.success(mutableData);
                        }
                    }
                }
                list.add(currentUserID);
                d.setCurrentSubscribers(list);

                mutableData.setValue(d);

                flag =1;
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                if(!b){
                    Log.d("StatusDeal",databaseError.getDetails());
                    flag=0;}

                if(b && flag==1||flag==2){
                    addToSubscribedDealInCustomer();
                }


                if(flag==3){
                    Toast.makeText(DealdeepLink.this, "You have already subscribed to this deal.", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);

                }


                loading.setVisibility(View.GONE);



            }
        });
    }



    @Override
    protected void onDestroy() {
        if(player!=null){
            player.release();
            player=null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(player!=null){
            player.release();
            player=null;
        }
        super.onBackPressed();
    }
}