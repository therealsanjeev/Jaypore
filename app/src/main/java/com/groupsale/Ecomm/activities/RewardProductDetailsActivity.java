package com.groupsale.Ecomm.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.customDialogs.sharePopup;
import com.groupsale.Ecomm.databinding.ActivityRewardProductDetailsBinding;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.ObjectWrapperForBinder;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardProductDetailsActivity extends AppCompatActivity {

    ActivityRewardProductDetailsBinding binding;
    DealModel deal;
    TextView text;
    long coinsAvailable;
    String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
    ImageSlider slider;
    final List<SlideModel> remoteimages = new ArrayList<>();
    LottieAnimationView confetti;
    String passingActivity;
    int likes;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String userFirestoreID = FirebaseAuth.getInstance().getUid();
    DocumentReference ref = database.collection(Constants.KEY_USERS)
            .document(userFirestoreID);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardProductDetailsBinding.inflate(getLayoutInflater());

        binding.btnBackProduct.setOnClickListener(v->onBackPressed());
        setContentView(binding.getRoot());
        slider= binding.imageView;
        confetti = binding.confettiReward;


        deal = ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("passedDealFromReward")).getData();
        passingActivity = getIntent().getExtras().getString("passingActivity");
        binding.toolbar.setTitle(deal.getName());
        binding.productInfo.setText(Html.fromHtml(deal.getDescription()));

        if(passingActivity.equals("ReOne")) {
            int coins = (int) Double.parseDouble(deal.getOriginalPrice())/10;
            binding.claimButtonText.setText("Claim for ₹1 + " + coins + " ");
            binding.descSecondLine.setText("Spend "+coins+" coins from wallet");
        }
        else {
            likes = (int) Double.parseDouble(deal.getOriginalPrice()) / 10;
            binding.claimButtonText.setText("Claim for ₹1 + " + likes + " ");
            Drawable heart = getResources().getDrawable(R.drawable.heart_icon);
            binding.claimButtonText.setCompoundDrawablesWithIntrinsicBounds(null, null, heart, null);
            binding.descSecondLine.setText("Share and get "+likes+" likes within 24 hours.");
        }

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get(Constants.KEY_TOTAL_POINTS) != null) {
                            coinsAvailable = (long) (document.get(Constants.KEY_TOTAL_POINTS));
                        } else {
                            coinsAvailable = 0;
                        }
                        text = binding.toolbar.getMenu().getItem(0).getActionView().findViewById(R.id.totalCoins);
                        text.setText(String.valueOf(coinsAvailable));
                    }
                }
            }
        });



        for(int i=0; i<deal.getImageUrl().size();i++){
            remoteimages.add(new SlideModel(imageBaseUrl+deal.getImageUrl().get(i).getFile(), ScaleTypes.FIT)); // for one image
        }

        slider.setImageList(remoteimages,ScaleTypes.FIT);

        binding.claimBtn.setOnClickListener(new View.OnClickListener() {


            long time;


            @Override
            public void onClick(View v) {



                if(passingActivity.equals("ReOne")){
                    if(coinsAvailable>(int)Double.parseDouble(deal.getOriginalPrice())/10) {
                        Map<String, Object> coins = new HashMap<>();
                        coinsAvailable = coinsAvailable - (int)Double.parseDouble(deal.getOriginalPrice())/10;
                        coins.put(Constants.KEY_TOTAL_POINTS, coinsAvailable);
                        ref.update(coins);
                        text.setText(String.valueOf(coinsAvailable));
                        Log.d("Coins",String.valueOf(coinsAvailable)+", "+String.valueOf((int)Double.parseDouble(deal.getDealPrice())));
                        addToFirebaseOrdersDb();
                    }
                    else
                        Toast.makeText(RewardProductDetailsActivity.this, "Required coins not available", Toast.LENGTH_SHORT).show();
                }

                else if(passingActivity.equals("ShareAndWin")){
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.get(Constants.KEY_DAILY_REWARDS_LAST_TIME)!=null) {
                                        time = (long) (document.get(Constants.KEY_DAILY_REWARDS_LAST_TIME));
                                    }
                                    else
                                        time =0L;

                                    if(System.currentTimeMillis()-time<345600000){
                                        Toast.makeText(RewardProductDetailsActivity.this, "Please wait 4 days after last share deal to start a new one", Toast.LENGTH_SHORT).show();
                                    }

                                    else {

                                        Map<String, Object> lasttime = new HashMap<>();
                                        lasttime.put(Constants.KEY_DAILY_REWARDS_LAST_TIME, System.currentTimeMillis());
                                        ref.update(lasttime);
                                        addToFirebaseRewardDealsDb();
                                    }



                                } else {
                                    Toast.makeText(RewardProductDetailsActivity.this, "Fetching went wrong, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RewardProductDetailsActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

               else  Toast.makeText(RewardProductDetailsActivity.this, "Something went wrong. Please Try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addToFirebaseRewardDealsDb() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String dealID = formatter.format(date) + currentCustomer.currentUser.getCustomerID().substring(0,4)+"SW";

        dealID = dealID.replaceAll("\\s", "");
        dealID = dealID.replaceAll("/", "");
        dealID = dealID.replaceAll(":", "");


        deal.setPeopleLeft(null);
        deal.setCreatorID(currentCustomer.currentUser.getCustomerID());
        deal.setCreatorName(currentCustomer.currentUser.getName());
        deal.setDateTime(formatter.format(date));
        deal.setTeamSize(null);
        deal.setLikeLeft(likes);
        deal.setLikeCounter(0);
        deal.setPeopleLeft(null);
        deal.setTeamSize(null);
        deal.setCurrentSubscribers(null);
        deal.setDealID(dealID);
        deal.setEpochTime(System.currentTimeMillis());
        deal.setPinCode(currentCustomer.currentUser.getPinCode());
        deal.setUnique(1);
        deal.setUniqueFilter(null);
        deal.setSubscriberID(null);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_REWARD_DEAL_DB);

        ref.child(deal.getDealID()).setValue(deal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(RewardProductDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

                else{

                    confetti.setVisibility(View.VISIBLE);
                    confetti.playAnimation();


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sharePopup popup = new sharePopup(RewardProductDetailsActivity.this, deal.getDealID());
                            popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popup.show();

                            confetti.setVisibility(View.INVISIBLE);

                        }
                    },2000);

                    Toast.makeText(RewardProductDetailsActivity.this, "Reward Deal created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addToFirebaseOrdersDb() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String dealID = formatter.format(date) + currentCustomer.currentUser.getCustomerID().substring(0,4);

        dealID = dealID.replaceAll("\\s", "");
        dealID = dealID.replaceAll("/", "");
        dealID = dealID.replaceAll(":", "");


        deal.setPeopleLeft(null);
        deal.setCreatorID(currentCustomer.currentUser.getCustomerID());
        deal.setCreatorName(currentCustomer.currentUser.getName());
        deal.setDateTime(formatter.format(date));
        deal.setTeamSize(null);
        deal.setLikeLeft(4);
        deal.setLikeCounter(0);
        deal.setDealPrice("-1");
        deal.setPeopleLeft(null);
        deal.setTeamSize(null);
        deal.setCurrentSubscribers(null);
        deal.setDealID(dealID);
        deal.setEpochTime(System.currentTimeMillis());
        deal.setPinCode(currentCustomer.currentUser.getPinCode());
        deal.setUnique(1);
        deal.setUniqueFilter(null);
        deal.setSubscriberID(null);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_ORDERS_DB);

        ref.child(deal.getDealID()).setValue(deal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(RewardProductDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

                else{

                    Toast.makeText(RewardProductDetailsActivity.this, "Deal Claimed!", Toast.LENGTH_SHORT).show();
                    confetti.setVisibility(View.VISIBLE);
                    confetti.playAnimation();


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            confetti.setVisibility(View.INVISIBLE);

                        }
                    },2000);

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}