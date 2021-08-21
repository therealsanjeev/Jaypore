package com.groupsale.Ecomm.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.Register;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.imageUrl;
import com.groupsale.Ecomm.roomdatabase.LocalDataBase;
import com.groupsale.Ecomm.roomdatabase.RoomDao;
import com.groupsale.Ecomm.roomdatabase.RoomModel;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    ImageView ivTop, ivShop, ivBottom;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();
    SharedPreferences introScreen;
    String TAG="SPLASHDATA";
    RoomDao roomDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivTop = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);
        ivShop = findViewById(R.id.iv_Shop);
        textView = findViewById(R.id.text_view);

        LocalDataBase localDataBase = LocalDataBase.getInstance(this);
        roomDao = localDataBase.roomDao();

        deleteAllData();

//        Log.d(TAG, "onCreate: "+ currentCustomer.currentUser.getCustomerID());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("dealsDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                DealModel model=  dataSnapshot.getValue(DealModel.class);
                List<String> imageList = new ArrayList<>();
                for (imageUrl img : model.getImageUrl()) {
                    imageList.add(img.getFile());
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

                Log.d(TAG, "onChildAdded: "+model.getOriginalPrice());
                insertDataToRoom(roomModel);

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.top_wave);

        ivTop.setAnimation(animation1);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivShop,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        );
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        animatText("Jaypore is about bringing the world a little closer together");


        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bottom_wave);

        ivBottom.setAnimation(animation2);

        FirebaseAuth mauth;

        new Handler().postDelayed(() -> {


            if (FirebaseAuth.getInstance().getCurrentUser() == null) {

                startActivity(new Intent(SplashActivity.this, Register.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                finish();
            } else {
                updateCustomer();
                startActivity(new Intent(SplashActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

        }, 4000);

    }

    private void updateCustomer() {
        String id  = FirebaseAuth.getInstance().getUid();
        PreferenceManager preferenceManager = new PreferenceManager(SplashActivity.this);
        preferenceManager.putString(Constants.KEY_CUSTOMER_ID,id);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(charSequence.subSequence(0, index++));


            if (index <= charSequence.length()) {
                handler.postDelayed(runnable, delay);

            }
        }
    };

    public void animatText(CharSequence cs) {
        charSequence = cs;
        index = 0;
        textView.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);

    }

    //Insert
    public void insertDataToRoom(final RoomModel roomModel) {

        Completable.fromAction(() ->
                roomDao.insert(roomModel)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        Log.d("TAG", "onSubscribe: Called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: Called");
//                        isLoading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "onError: Called" + e.getMessage());
                    }
                });
    }
    //Delete all Genre
    public void deleteAllData() {

//        isLoading.setValue(true);
        Completable.fromAction(() -> roomDao.deleteAllData()).subscribeOn(Schedulers.io())
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
                        Log.d(TAG, "onError: Called" + e.getMessage());
                    }
                });
    }
}

