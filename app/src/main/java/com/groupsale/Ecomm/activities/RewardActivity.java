package com.groupsale.Ecomm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.RewardAdapter;
import com.groupsale.Ecomm.adapters.ReOneProductAdapter;
import com.groupsale.Ecomm.adapters.RewardCoinsAdapter;
import com.groupsale.Ecomm.adapters.ShareAndWinProductAdapter;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.RewardCoinsModel;
import com.groupsale.Ecomm.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RewardActivity extends AppCompatActivity  implements RewardCoinsAdapter.onClicked{

    ProgressBar progressBar;
    TextView textView;

    RecyclerView RewardCoinsRecyclerView,ReOneProductRecyclerView,ShareAndWinProductRecyclerView;
    LinearLayoutManager layoutManager;
    RewardAdapter rewardsAdapter;
    ReOneProductAdapter reOneProductAdapter;
    RewardCoinsAdapter rewardCoinsAdapter;
    ShareAndWinProductAdapter shareAndWinProductAdapter;
    ArrayList<RewardCoinsModel> list;
    ArrayList<DealModel> dealList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        findViewById(R.id.btnBackReward).setOnClickListener(v -> onBackPressed());


        RewardCoinsRecyclerView = findViewById(R.id.rewardCoinAC);
        ReOneProductRecyclerView= findViewById(R.id.rewardReOneProductAC);
        ShareAndWinProductRecyclerView = findViewById(R.id.rewardShareAndWinProductAC);

        progressBar = findViewById(R.id.progressBarReward);
        textView = findViewById(R.id.loadingNumber);

        rewardCoinsAdapter=new RewardCoinsAdapter(this, Objects.requireNonNull(this).getApplicationContext());
        reOneProductAdapter = new ReOneProductAdapter();
        shareAndWinProductAdapter = new ShareAndWinProductAdapter();


        ReOneProductRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ShareAndWinProductRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        RewardCoinsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

//        RewardCoinsModel rewardModelClass=new RewardCoinsModel("Daily Rewards","10");
//        RewardCoinsModel rewardModelClass2=new RewardCoinsModel("2000 Thousand Purchase","2000");
//        RewardCoinsModel rewardModelClass3=new RewardCoinsModel("Free Order","1000");
        list=new ArrayList<>();
//        list.add(rewardModelClass);
//        list.add(rewardModelClass3);
//        list.add(rewardModelClass2);
//        rewardCoinsAdapter.SetData(list);


        String customerID = "ibirm9N2QJeoSH9bSkVznmLtUtV2";
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_SHAREANDWIN_DB);
        DatabaseReference dref1 = FirebaseDatabase.getInstance().getReference().child("RewardsDB");

        dref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Log.d("RewardModel", "onDataChange: "+postSnapshot);
                    RewardCoinsModel d = postSnapshot.getValue(RewardCoinsModel.class);
                    list.add(d);
                }
                Log.d("RewardModel", "onDataChange: "+list);

                rewardCoinsAdapter.SetData(list);
                RewardCoinsRecyclerView.setAdapter(rewardCoinsAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("RewardModel", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dealList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DealModel d = data.getValue(DealModel.class);
                    dealList.add(d);
                    Log.d("TAG", d.getDealID());
                }

                shareAndWinProductAdapter.SetData(dealList);
                reOneProductAdapter.SetData(dealList);
                ShareAndWinProductRecyclerView.setAdapter(shareAndWinProductAdapter);
                ReOneProductRecyclerView.setAdapter(reOneProductAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClicked(int position, List<RewardCoinsModel> list) {
        Intent intent = new Intent(this, RewardCoinsActivity.class);
        intent.putExtra("rewardName",String.valueOf(position+1));
        intent.putExtra("reward",  list.get(position));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}