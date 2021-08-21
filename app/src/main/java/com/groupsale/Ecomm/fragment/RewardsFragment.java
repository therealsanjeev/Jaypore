package com.groupsale.Ecomm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.RewardAdapter;
import com.groupsale.Ecomm.activities.RewardCoinsActivity;
import com.groupsale.Ecomm.adapters.ReOneProductAdapter;
import com.groupsale.Ecomm.adapters.RewardCoinsAdapter;
import com.groupsale.Ecomm.adapters.ShareAndWinProductAdapter;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.RewardCoinsModel;
import com.groupsale.Ecomm.utilities.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RewardsFragment extends Fragment implements RewardCoinsAdapter.onClicked{





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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment;
        View v = inflater.inflate(R.layout.fragment_rewards, container, false);
        RewardCoinsRecyclerView = v.findViewById(R.id.rewardCoin);
        ReOneProductRecyclerView= v.findViewById(R.id.rewardReOneProduct);
        ShareAndWinProductRecyclerView = v.findViewById(R.id.rewardShareAndWinProduct);

        progressBar = v.findViewById(R.id.progressBarReward);
        textView = v.findViewById(R.id.loadingNumber);

        rewardCoinsAdapter=new RewardCoinsAdapter(this, Objects.requireNonNull(getContext()).getApplicationContext());
        reOneProductAdapter = new ReOneProductAdapter();
        shareAndWinProductAdapter = new ShareAndWinProductAdapter();


        ReOneProductRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false));
        ShareAndWinProductRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false));

        RewardCoinsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

//        RewardCoinsModel rewardModelClass=new RewardCoinsModel("Daily Rewards","10");
//        RewardCoinsModel rewardModelClass2=new RewardCoinsModel("2000 Thousand Purchase","2000");
//        RewardCoinsModel rewardModelClass3=new RewardCoinsModel("Free Order","1000");
//
        list=new ArrayList<>();
//        list.add(rewardModelClass);
//        list.add(rewardModelClass3);
//        list.add(rewardModelClass2);
//
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


//        FirebaseRecyclerOptions<RewardModelClass> options =
//                new FirebaseRecyclerOptions.Builder<RewardModelClass>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("RewardsDB"), RewardModelClass.class)
//                        .build();
//
//
//
//        rewardsAdapter = new RewardAdapter(options);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
//        dataList.setLayoutManager(gridLayoutManager);
//        dataList.setAdapter(rewardsAdapter);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        rewardsAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
//        rewardsAdapter.stopListening();
    }

    @Override
    public void onItemClicked(int position, List<RewardCoinsModel> list) {
//        Toast.makeText(requireContext(), list.get(position).getCardName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(requireContext(), RewardCoinsActivity.class);
        intent.putExtra("rewardName",String.valueOf(position+1));
        intent.putExtra("reward",  list.get(position));
        startActivity(intent);

    }
}