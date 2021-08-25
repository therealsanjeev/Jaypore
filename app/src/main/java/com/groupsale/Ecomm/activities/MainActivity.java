package com.groupsale.Ecomm.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.groupsale.Ecomm.FragmentAdapter;
import com.groupsale.Ecomm.HelpActivity;
import com.groupsale.Ecomm.OrderDetails;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.customer;
import com.groupsale.Ecomm.models.imageUrl;
import com.groupsale.Ecomm.roomdatabase.LocalDataBase;
import com.groupsale.Ecomm.roomdatabase.RoomDao;
import com.groupsale.Ecomm.roomdatabase.RoomModel;
import com.groupsale.Ecomm.utilities.Constants;

import org.jetbrains.annotations.NotNull;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.SSLContext;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private final int srchcount = 0;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    private FirebaseAnalytics mFirebaseAnalytics;



    private static String data;
    private WebView web2;
    public static final String USER_AGENT_FAKE = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    FragmentAdapter fragmentAdapter;
    //addition
    private LinearLayout layout;
    private ImageView menu, cart, search, wishlist, account;
    private EditText editText;
    private TextView totalCoins;

    protected static void mainlink(String link) {
        data = link;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTotalCoins();
    }

    @Override
    public boolean onPreparePanel(int featureId, @Nullable @org.jetbrains.annotations.Nullable View view, @NonNull @NotNull Menu menu) {
        final MenuItem coinMenu = menu.findItem(R.id.rewardscoin);
        LinearLayout rootView = (LinearLayout) coinMenu.getActionView();
        totalCoins =rootView.findViewById(R.id.totalCoins);
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Profile:

                String name = "Profile";
                Bundle params = new Bundle();
                params.putString("btn_name", name);
                mFirebaseAnalytics.logEvent("Profile_btn", params);
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

                return true;
            case R.id.RewardMenu:
                Intent rewardIntent = new Intent(MainActivity.this, RewardActivity.class);
                startActivity(rewardIntent);
                return true;
            case R.id.Order_details:
                Intent OrderIntent = new Intent(MainActivity.this, OrderDetails.class);
                startActivity(OrderIntent);
                return true;

            case R.id.help:

                 Intent HelpIntent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(HelpIntent);


                return true;


            default:

                return super.onOptionsItemSelected(item);
        }
    }

    String TAG="DATA";
    RoomDao roomDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        checkTotalCoins();
        LocalDataBase localDataBase = LocalDataBase.getInstance(this);
        roomDao = localDataBase.roomDao();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                Log.d(TAG, "onChildAdded: "+dataSnapshot+" "+s);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

                DealModel model=  dataSnapshot.getValue(DealModel.class);
                    List<String> imageList = new ArrayList<String>();
                    for (imageUrl img : model.getImageUrl()) {
                        imageList.add(img.getFile());
                        Log.d(TAG, "onChildChanged: "+img.getFile());
                    }

                    RoomModel roomModel = new RoomModel(
                            model.getDealID(),
                            model.getProductID(),
                            model.getTextMessage(),
                            model.getDescription(),
                            model.getName(),
                            model.getCreatorID(),
                            model.getDateTime(),
                            model.getPeopleLeft(),
                            model.getPinCode(),
                            model.getTeamSize(),
                            model.getDealPrice(),
                            model.getCreatorName(),
                            model.getUniqueFilter(),
                            model.getAudioFileLink(),
                            model.getSubscriberID(),
                            model.getOriginalPrice(),
                            model.getEpochTime(),
                            model.getStatus(),
                            model.getUnique(),
                            model.getLikeCounter(),
                            model.getCurrentSubscribers(),
                            imageList
                    );

                Log.d(TAG, "onChildChanged: "+roomModel.getCreatorName());
                    UpdateData(roomModel);
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: "+dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                Log.d(TAG, "onChildMoved: "+dataSnapshot+" "+s);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError);
            }
        };
        ref.child("dealsDB").addChildEventListener(childEventListener);

        check();

        // create user id n token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        String token = task.getResult().getToken();

                        FirebaseDatabase.getInstance().getReference("Tokens").child(userId).child("notificationTokens").setValue(token);
                        Log.d("TAG", "onComplete: Token" + token);
                    } else {
                        task.getException();
                    }
                });


        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        setSupportActionBar(toolbar);


        tabLayout.addTab(tabLayout.newTab().setText("JOIN"));
        tabLayout.addTab(tabLayout.newTab().setText("EXPLORE"));
        tabLayout.addTab(tabLayout.newTab().setText("STORE"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                Filldata(user.getUid());
            }

            handler.post(() -> {
                //Any work on linking UI to be done here
            });
        });


        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        //  deferred deep activity fire any
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if (appLinkAction != null) {

            if(appLinkData.toString().equals("https://www.grousale.com/main/join") ) {
                viewPager.setCurrentItem(0);
                Toast.makeText(MainActivity.this, "join" +appLinkData, Toast.LENGTH_SHORT).show();

            }
           else if(appLinkData.toString().equals("https://www.grousale.com/main/store") ){
                viewPager.setCurrentItem(1);
                Toast.makeText(MainActivity.this, "store" +appLinkData, Toast.LENGTH_SHORT).show();

            }

            else if(appLinkData.toString().equals("https://www.grousale.com/main/rewards")) {
                viewPager.setCurrentItem(2);
            }

            else{
                viewPager.setCurrentItem(2);
                Toast.makeText(MainActivity.this, "else" +appLinkData, Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void checkTotalCoins() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        String userFirestoreID = FirebaseAuth.getInstance().getUid();

        AtomicLong previousTime = new AtomicLong();
        database.collection(Constants.KEY_USERS)
                .document(userFirestoreID)
                .get()
                .addOnCompleteListener(documentReference -> {
                    if (documentReference.isSuccessful() && documentReference.getResult() != null) {
//                        previousTime.set(documentReference.getResult().getLong(Constants.KEY_DAILY_REWARDS_LAST_TIME));
                        totalCoins.setText(documentReference.getResult().getLong(Constants.KEY_TOTAL_POINTS).toString());
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void UpdateData(RoomModel roomModel) {

        Completable.fromAction(() ->
                roomDao.update(roomModel)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Called");
                    }
                });
    }
    private void Filldata(String uid) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("CustomerDB").child(uid).exists())) {

                    currentCustomer.currentUser = dataSnapshot.child("customerDB").child(uid).getValue(customer.class);

                } else {
                    Toast.makeText(MainActivity.this, "Error: 403", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void check() {
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(this);
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }


}