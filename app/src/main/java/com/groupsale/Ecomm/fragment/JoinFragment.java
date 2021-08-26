package com.groupsale.Ecomm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.Adapter;
import com.groupsale.Ecomm.ModelClass;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.activities.StatusDealActivity;
import com.groupsale.Ecomm.activities.StatusRewardDealActivity;
import com.groupsale.Ecomm.activities.UserActivity;
import com.groupsale.Ecomm.adapters.DealAdapter;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.imageUrl;
import com.groupsale.Ecomm.roomdatabase.LocalDataBase;
import com.groupsale.Ecomm.roomdatabase.RoomDao;
import com.groupsale.Ecomm.roomdatabase.RoomModel;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;
import com.groupsale.Ecomm.viewHolder.dealViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class JoinFragment extends Fragment implements DealAdapter.onClicked {

    String TAG = "CHILDLISTENER";
    RecyclerView mrecyclerView, pinRecylerView,rewardRecyclerView;
    LinearLayoutManager layoutManager, pinLayoutManager;
    List<ModelClass> userList;
    PreferenceManager preferenceManager;
    String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
    Adapter adapter;

    FirebaseAnalytics firebaseAnalytics;


    ProgressBar progressBarMyDeal, progressBarDealAround;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Join.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinFragment newInstance(String param1, String param2) {
        JoinFragment fragment = new JoinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }

    TextView mydeal,pindeal,rewardDeal;
    RoomDao roomDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ArrayList<RoomModel> myDealsList;
    DealAdapter dealAdapterMyDeal, dealAdapterAround;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocalDataBase localDataBase = LocalDataBase.getInstance(requireContext());
        roomDao = localDataBase.roomDao();

        myDealsList = new ArrayList<>();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_join, container, false);



        mydeal=v.findViewById(R.id.myDeal);
        pindeal=v.findViewById(R.id.pinDeal);
        rewardDeal = v.findViewById(R.id.rewardDeal);
//        mrecyclerView = v.findViewById(R.id.RecyclerView1);
//        mrecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        pinRecylerView = v.findViewById(R.id.RecyclerViewPinCode);
        pinRecylerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        rewardRecyclerView = v.findViewById(R.id.RecyclerViewRewardDeal);
//        rewardRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        progressBarMyDeal = v.findViewById(R.id.progressMyDeal);
        progressBarDealAround = v.findViewById(R.id.progressbarDealAround);

//        showMyRewardDeals();// To be replaced with Room later

//        mydeal.setOnClickListener(v1 -> {
//            if(mrecyclerView.getVisibility()==View.GONE) {
//                mrecyclerView.setVisibility(View.VISIBLE);
//                mydeal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
//
//            }
//            else{
//                mrecyclerView.setVisibility(View.GONE);
//                mydeal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
//
//            }
//
//        });

        pindeal.setOnClickListener(v12 -> {
            if(pinRecylerView.getVisibility()==View.GONE){
                pinRecylerView.setVisibility(View.VISIBLE);
                pindeal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
            }
            else{
                pinRecylerView.setVisibility(View.GONE);
                pindeal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

            }

        });

//        rewardDeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(rewardRecyclerView.getVisibility()==View.GONE){
//                    rewardRecyclerView.setVisibility(View.VISIBLE);
//                    rewardDeal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
//                }
//                else{
//                    rewardRecyclerView.setVisibility(View.GONE);
//                    rewardDeal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
//
//                }
//            }
//        });

        //Reading DATA from Room :
//        Flowable<List<RoomModel>> listFlowableMyDeals = roomDao.myDeals(FirebaseAuth.getInstance().getUid());
//        Disposable disposable = listFlowableMyDeals.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(list -> {
//                    dealAdapterMyDeal = new DealAdapter(list, this::onItemClicked);
//                    mrecyclerView.setAdapter(dealAdapterMyDeal);
//                    progressBarDealAround.setVisibility(View.GONE);
//                    mrecyclerView.setVisibility(View.VISIBLE);
//
//                });
        Flowable<List<RoomModel>> listFlowableDealAroundYou = roomDao.dealAroundYou("110010");

        Disposable disposable1 = listFlowableDealAroundYou.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    for (RoomModel it : list) {
                        Log.d(TAG, "onCreateView: ");
                    }
                    dealAdapterAround = new DealAdapter(list, this);
                    pinRecylerView.setAdapter(dealAdapterAround);
                    progressBarDealAround.setVisibility(View.GONE);
                    pinRecylerView.setVisibility(View.VISIBLE);

                });

//        compositeDisposable.add(disposable);
        compositeDisposable.add(disposable1);
        return v;
    }

    private void showMyRewardDeals() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_REWARD_DEAL_DB);
        String customerID = FirebaseAuth.getInstance().getUid();

        Query queryMyDeal = reference.orderByChild("creatorID").equalTo(customerID);

        Log.d("Tag", customerID);

        FirebaseRecyclerOptions<DealModel> options =
                new FirebaseRecyclerOptions.Builder<DealModel>()
                        .setQuery(reference.orderByChild("creatorID").equalTo(customerID), DealModel.class)
                        .build();


        FirebaseRecyclerAdapter<DealModel, dealViewHolder> adapter =
                new FirebaseRecyclerAdapter<DealModel, dealViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull dealViewHolder holder, int position, @NonNull final DealModel model) {


                        int price = (int) Double.parseDouble(model.getDealPrice());

                        String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
                        holder.customerName.setText(model.getCreatorName());
                        String abovetext = model.getName();
                        holder.aboveText.setText(abovetext);
                        holder.price.setText("₹0 ");
                        if (model.getOriginalPrice() != null) {
                            int originalPrice = Integer.parseInt(model.getOriginalPrice());
                            holder.strikePrice.setText("₹ " + String.valueOf(originalPrice));
                            String offText = "100% Off";
                            holder.flatOff.setText(offText);

                        }
                        holder.peopleLeft.setText("Likes needed: " + model.getLikeLeft());
                        long elapsedTime = 86400000 - getElapsedTime(model.getEpochTime());
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

                        Picasso.get().load(model.getImageUrl().get(0).getFile()).into(holder.itemImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent((Activity)getActivity(), StatusRewardDealActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public dealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
                        dealViewHolder holder = new dealViewHolder(view);
                        return holder;
                    }
                };


        rewardRecyclerView.setVisibility(View.VISIBLE);
        rewardRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(layoutManager);

        pinLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        pinLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pinRecylerView.setLayoutManager(pinLayoutManager);


    }


    private void loadLeadDealsFromFirebase() {
        // myDeals
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("dealsDB");
        String customerID = currentCustomer.currentUser.getCustomerID();

        Query queryMyDeal = reference.orderByChild("creatorID").equalTo(customerID);

        Log.d("Tag", customerID);

        FirebaseRecyclerOptions<DealModel> options =
                new FirebaseRecyclerOptions.Builder<DealModel>()
                        .setQuery(reference.orderByChild("creatorID").equalTo(customerID), DealModel.class)
                        .build();


        FirebaseRecyclerAdapter<DealModel, dealViewHolder> adapter =
                new FirebaseRecyclerAdapter<DealModel, dealViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull dealViewHolder holder, int position, @NonNull final DealModel model) {


                        int price = (int) Double.parseDouble(model.getDealPrice());

                        String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
                        holder.customerName.setText(model.getCreatorName());
                        String abovetext = model.getName();
                        holder.aboveText.setText(abovetext);
                        holder.price.setText("₹ " + price);
                        if (model.getOriginalPrice() != null) {
                            int originalPrice = Integer.parseInt(model.getOriginalPrice());
                            holder.strikePrice.setText("₹ " + String.valueOf(originalPrice));
                            Log.d("Price", model.getOriginalPrice());
                            int off = price * 100 / originalPrice;
                            String offText = off + "% Off";
                            holder.flatOff.setText(offText);

                        }
                        holder.peopleLeft.setText("People needed: " + model.getPeopleLeft());
                        long elapsedTime = 86400000 - getElapsedTime(model.getEpochTime());
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

                        Picasso.get().load(model.getImageUrl().get(0).getFile()).into(holder.itemImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), "Loading Deals...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), StatusDealActivity.class);
                                intent.putExtra("passedCustomerID", model.getCreatorID());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public dealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
                        dealViewHolder holder = new dealViewHolder(view);
                        return holder;
                    }
                };

        progressBarMyDeal.setVisibility(View.GONE);
        mrecyclerView.setVisibility(View.VISIBLE);
        mrecyclerView.setAdapter(adapter);
        adapter.startListening();


        //pinDeals
        DatabaseReference pinReference = FirebaseDatabase.getInstance().getReference().child("dealsDB");
        ;
        String pinCode = currentCustomer.currentUser.getPinCode();

        FirebaseRecyclerOptions<DealModel> pinOptions =
                new FirebaseRecyclerOptions.Builder<DealModel>()
                        .setQuery(pinReference.orderByChild("pinCode").equalTo(pinCode), DealModel.class)
                        .build();


        FirebaseRecyclerAdapter<DealModel, dealViewHolder> pinAdapter =
                new FirebaseRecyclerAdapter<DealModel, dealViewHolder>(pinOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull dealViewHolder holder, int position, @NonNull final DealModel model) {

                        int price = (int) Double.parseDouble(model.getDealPrice());

                        String imageBaseUrl = "https://lootllo.com/pub/media/catalog/product";
                        holder.customerName.setText(model.getCreatorName());
                        String abovetext = model.getName();
                        holder.aboveText.setText(abovetext);
                        holder.price.setText("₹ " + price);
                        if (model.getOriginalPrice() != null) {
                            int originalPrice = Integer.parseInt(model.getOriginalPrice());
                            holder.strikePrice.setText("₹ " + String.valueOf(originalPrice));
                            Log.d("Price", model.getOriginalPrice());
                            int off = price * 100 / originalPrice;
                            String offText = off + "% Off";
                            holder.flatOff.setText(offText);

                        }


                        holder.peopleLeft.setText("People needed: " + model.getPeopleLeft());
                        long elapsedTime = 86400000 - getElapsedTime(model.getEpochTime());
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

                        Picasso.get().load( model.getImageUrl().get(0).getFile()).into(holder.itemImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), "Loading Deals...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), StatusDealActivity.class);
                                intent.putExtra("passedCustomerID", model.getCreatorID());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public dealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
                        dealViewHolder holder = new dealViewHolder(view);
                        return holder;
                    }
                };

        progressBarDealAround.setVisibility(View.GONE);
        pinRecylerView.setVisibility(View.VISIBLE);
        pinRecylerView.setAdapter(pinAdapter);
        pinAdapter.startListening();

    }

    private long getElapsedTime(long created) {
        Date date = new Date();
        long now = date.getTime();
        return now - created;
    }

    public void onStart() {
        super.onStart();

    }


    @Override
    public void onItemClicked(int position, List<RoomModel> list) {

//        Toast.makeText(getActivity(), "Loading Deals...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), StatusDealActivity.class);
        intent.putExtra("passedCustomerID", list.get(position).getCreatorID());
        startActivity(intent);
    }

    @Override
    public void onProfileClicked(int position, List<RoomModel> list) {
//        Toast.makeText(getActivity(), "Loading Deals...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), UserActivity.class);
        intent.putExtra("userName", list.get(position).getCreatorName());
        startActivity(intent);
    }
}