package com.groupsale.Ecomm.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.databinding.ActivityRewardCoinsBinding;
import com.groupsale.Ecomm.models.RewardCoinsModel;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RewardCoinsActivity extends AppCompatActivity {

    private static final String TAG = "REWARDS";
    ActivityRewardCoinsBinding binding;
    PreferenceManager preferenceManager;
    ImageSlider imageSlider;
    RewardCoinsModel rewardDetails;
    final List<SlideModel> list = new ArrayList<>();
    AtomicLong currTime = new AtomicLong();
    AtomicLong totalPoints = new AtomicLong();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardCoinsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FirebaseFunctions.getInstance().getHttpsCallable("getTime")
                .call().addOnSuccessListener(httpsCallableResult -> {
            long time=(long) httpsCallableResult.getData();
            Log.d(TAG, "onCreate: c "+time);
            currTime.set(time);

        });

        Init();

    }

    private void Init() {
        preferenceManager = new PreferenceManager(this);

        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("rewardName");
        rewardDetails = (RewardCoinsModel) getIntent().getSerializableExtra("reward");
        Log.d(TAG, "onCreate: "+rewardDetails.getCardName());
        binding.toolBar.setTitle(rewardDetails.getCardName());
        binding.claim1.setText(rewardDetails.getClaim1());
        binding.claim2.setText(rewardDetails.getClaim2());
        binding.claim3.setText(rewardDetails.getClaim3());
        binding.howto1.setText(rewardDetails.getDescline1());
        binding.howto2.setText(rewardDetails.getDescline2());
        binding.howto3.setText(rewardDetails.getDescline3());
        binding.claimButtonText.setText(rewardDetails.getButton());

        Glide.with(this).load(rewardDetails.getCardImg()).error(R.drawable.ic_gift_rafiki__1_).into(binding.imageView);

        switch (key) {
            case "1":
                getDailyReward();
                break;
            case "2":
                getTwoThousandPoint();
                break;
            case "3":
                getFreeOrderPoint();

        }

        binding.rewardLayout.setOnClickListener(v ->
                binding.rewardLayout.setVisibility(View.GONE)
        );
    }

    private void getDailyReward() {

        binding.claimBtn.setOnClickListener(v -> {
            CheckReward();
        });


    }
    private void getTwoThousandPoint() {
        binding.claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTwoThousandPoint();
            }
        });
    }
    private void getFreeOrderPoint() {

        binding.claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFreeOrderPoint();
            }
        });
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        String userID = FirebaseAuth.getInstance().getUid();
//        database.collection(Constants.KEY_USERS)
//                .document(userID)
//                .collection(Constants.KEY_ORDERS)
//                .get().addOnCompleteListener(documentReference -> {
//                    if (documentReference.isSuccessful()&& documentReference.getResult() != null) {
//
//                    }
//
//        }).addOnFailureListener(e -> {
//
//        });
    }

    private void addDailyRewards() {
        binding.rewardLayout.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userFirestoreID = FirebaseAuth.getInstance().getUid();

        HashMap<String, Object> daily = new HashMap<>();
        daily.put(Constants.KEY_DAILY_REWARDS_POINT, "10");
        daily.put(Constants.KEY_REWARDS_TIME, currTime.longValue());
        database.collection(Constants.KEY_USERS)
                .document(userFirestoreID)
                .collection(Constants.KEY_REWARDS)
                .document("Path")
                .collection(Constants.KEY_DAILY_REWARDS)
                .add(daily)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getApplicationContext(),"Congratulations!!! you get 10 points.",Toast.LENGTH_SHORT).show());


    }


    private void addTwoThousandPoint() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userFirestoreID = FirebaseAuth.getInstance().getUid();

        HashMap<String, Object> reward = new HashMap<>();
        reward.put(Constants.KEY_DAILY_REWARDS_POINT, "2000");
        reward.put(Constants.KEY_REWARDS_TIME, FieldValue.serverTimestamp());
        database.collection(Constants.KEY_USERS)
                .document(userFirestoreID)
                .collection(Constants.KEY_REWARDS)
                .document("Path")
                .collection(Constants.KEY_TWO_THOUSAND_REWARDS)
                .add(reward)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Congratulations!!! you get 2000 points.",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void addFreeOrderPoint(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userFirestoreID = FirebaseAuth.getInstance().getUid();

        HashMap<String, Object> reward = new HashMap<>();
        reward.put(Constants.KEY_DAILY_REWARDS_POINT, "1000");
        reward.put(Constants.KEY_REWARDS_TIME, FieldValue.serverTimestamp());
        database.collection(Constants.KEY_USERS)
                .document(userFirestoreID)
                .collection(Constants.KEY_REWARDS)
                .document("Path")
                .collection(Constants.KEY_FREE_ORDER_REWARDS)
                .add(reward)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Congratulations!!! you get 1000 points.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CheckReward() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        String userFirestoreID = FirebaseAuth.getInstance().getUid();

        AtomicLong previousTime = new AtomicLong();
        database.collection(Constants.KEY_USERS)
                .document(userFirestoreID)
                .get()
                .addOnCompleteListener(documentReference -> {
                    if (documentReference.isSuccessful() && documentReference.getResult() != null) {
                        previousTime.set(documentReference.getResult().getLong(Constants.KEY_DAILY_REWARDS_LAST_TIME));
                        totalPoints.set(documentReference.getResult().getLong(Constants.KEY_TOTAL_POINTS));
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


        binding.rewardLayoutProgress.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Log.d(TAG, "onCreate: a "+currTime);
            Log.d(TAG, "CheckReward: "+previousTime);
            if((previousTime.longValue() +24*60*60*1000)<= currTime.longValue()){
                Log.d(TAG, "CheckReward: currTime"+currTime.get()+" "+"prevTime "+previousTime);
                upDatePreviousTimeAndTotalPoints();
                addDailyRewards();
                binding.rewardLayoutProgress.setVisibility(View.GONE);
            }else{

                Toast.makeText(getApplication(),"Please try after some time :(",Toast.LENGTH_LONG).show();
                binding.rewardLayoutProgress.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void upDatePreviousTimeAndTotalPoints() {

        Log.d(TAG, "upDatePreviousTime: "+currTime);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userFirestoreID = FirebaseAuth.getInstance().getUid();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_DAILY_REWARDS_LAST_TIME, currTime.longValue());
        totalPoints.set(totalPoints.longValue()+10);
        user.put(Constants.KEY_TOTAL_POINTS, totalPoints.longValue());

        database.collection(Constants.KEY_USERS)
                .document(userFirestoreID)
                .update(user)
                .addOnSuccessListener(unused ->{
//                    Toast.makeText(this, "last time updated", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: "+currTime);
                        }

                );
    }


}