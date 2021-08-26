package com.groupsale.Ecomm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.databinding.ActivityStatusDealBinding;
import com.groupsale.Ecomm.models.DealModel;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.customer;
import com.groupsale.Ecomm.models.productInfo;
import com.groupsale.Ecomm.productDescription;
import com.groupsale.Ecomm.roomdatabase.LocalDataBase;
import com.groupsale.Ecomm.roomdatabase.RoomDao;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StatusDealActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private static int PROGRESS_COUNT;

    FirebaseAnalytics firebaseAnalytics;


    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private TextView textView;
    private ImageView share, info;
    private ActivityStatusDealBinding binding;
    private ProgressBar loading;
    private Button joinNow;
    List<DealModel> dealArray = new ArrayList<>();
    private int flag;
    private LottieAnimationView heart;
    String imageBaseUrl = "";//"https://lootllo.com/pub/media/catalog/product";
    private customer customer;
    private static List<String> deals;
    private DealModel currentDeal;
    private static int counter = 0;
    Handler handler = new Handler();
    MediaPlayer player = new MediaPlayer();
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();


                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    RoomDao roomDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStatusDealBinding.inflate(getLayoutInflater());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //setDealList();
        setDealArray();
        info = binding.INFO;
        share = binding.storyPageShare;
        loading = binding.progressbarStatusDeal;

        info.setOnClickListener(v -> {
            Intent intent = new Intent(StatusDealActivity.this, productDescription.class);
            intent.putExtra("passedDealID", dealArray.get(counter).getDealID());

            startActivity(intent);
        });

        //            public Uri getLocalBitmapUri(Bitmap bmp) {
//                Uri bmpUri = null;
//                try {
//                   File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
//                    FileOutputStream out = new FileOutputStream(file);
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                    out.close();
//                    bmpUri = Uri.fromFile(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return bmpUri;
//            }
        share.setOnClickListener(v -> {
            String image_url =imageBaseUrl+dealArray.get(counter).getImageUrl().get(0).getFile();

            Log.d("SHAREIMAGE", "onCreate: "+imageBaseUrl+dealArray.get(counter).getImageUrl().get(0).getFile());
            Picasso.get().load(image_url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    String fileUri = null;
                    try {
                        File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                        if (!mydir.exists()) {
                            mydir.mkdirs();
                        }

                          fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                        FileOutputStream outputStream = new FileOutputStream(fileUri);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
                    // use intent to share image
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                     String body = "Please join my deal by clicking on https://www.grousale.com/deal/" + dealArray.get(counter).getDealID();
                         share.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(Intent.createChooser(share, "Share Deal"));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
//                Picasso.get().load(image_url).into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        Intent myIntent = new Intent();
//                        myIntent.setType("image/*");
//                        myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        myIntent.setAction(Intent.ACTION_SEND);
//                        // String body = "Please join my deal by clicking on https://www.grousale.com/deal/" + dealArray.get(counter).getDealID();
//                        // myIntent.putExtra(Intent.EXTRA_TEXT, body);
//                        myIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
//                            startActivity(myIntent);
//
//
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });

        });
        storiesProgressView = binding.stories;
        storiesProgressView.setStoriesListener(this);

        image = binding.image;
        textView = binding.Title;
        heart = binding.storyPageLike;
        joinNow = binding.storyPageJoinNow;

        heart.setOnClickListener(v -> {
            heart.playAnimation();
            //Add like in dealsDB
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dealsDB").child(dealArray.get(counter).getDealID());
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

                    mutableData.setValue(d);

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    Log.d("Transaction Error", "postTransaction:onComplete:" + databaseError);
                }
            });


            //Add deal in productsDB
            if (dealArray.get(counter).getProductID() != null) {

                DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("productsDB").child(dealArray.get(counter).getProductID());
                prodref.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        productInfo p = mutableData.getValue(productInfo.class);

                        if (p == null) {
                            p = new productInfo();
                            p.setLikeCounter(1);
                            p.setSubscriberCounter(1);

                            mutableData.setValue(p);

                            return Transaction.success(mutableData);
                        }
                        int sub = p.getLikeCounter();
                        p.setLikeCounter(++sub);
                        mutableData.setValue(p);
                        return Transaction.success(mutableData);

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                        //TODO Remove below
                        if (!b) {
                            Toast.makeText(StatusDealActivity.this, "productsDB: " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        });


        joinNow.setOnClickListener(v -> {

            flag = 0;

            loading.setVisibility(View.VISIBLE);
            new Handler().post(() -> {
                if (storiesProgressView != null)
                    storiesProgressView.pause();
            });

            addToSubscribersInDeal();

            Bundle bundle = new Bundle();
            bundle.putString("OTP","Sign In");
            firebaseAnalytics.logEvent("SignIN_Successfully",bundle);

        });


        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    private void setDealArray() {
        LocalDataBase localDataBase = LocalDataBase.getInstance(this);
        roomDao = localDataBase.roomDao();

        Bundle bundle = getIntent().getExtras();
        String customerID = bundle.getString("passedCustomerID");

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("dealsDB");
        Query query = dref.orderByChild("creatorID").equalTo(customerID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dealArray.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DealModel d = data.getValue(DealModel.class);
                    dealArray.add(d);
                    Log.d("TAG", d.getDealID());
                }

                PROGRESS_COUNT = dealArray.size();
                storiesProgressView.setStoriesCount(PROGRESS_COUNT);
                storiesProgressView.setStoryDuration(3000L);
                storiesProgressView.startStories();
                setStatusFromArray(counter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private long getElapsedTime(long created) {
        Date date = new Date();
        long now = date.getTime();
        return now - created;
    }
    private void setStatusFromArray(int counter) {

        new Handler().post(() -> {
            if (storiesProgressView != null)

                storiesProgressView.pause();
        });

        long elapsedTime = 86400000 - getElapsedTime(dealArray.get(counter).getEpochTime());
        new CountDownTimer(elapsedTime, 1000) {
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                String time = "Join Now "+f.format(hour) + ":" + f.format(min) + ":" + f.format(sec) +" Left";
                joinNow.setText(time);
            }

            public void onFinish() {
                joinNow.setText("00:00:00");
            }
        }.start();


        Picasso.get().load(imageBaseUrl + dealArray.get(counter).getImageUrl().get(0).getFile()).into(image);
        textView.setText(dealArray.get(counter).getName());
        binding.Price.setText("₹"+String.valueOf((int)Double.parseDouble(dealArray.get(counter).getDealPrice())));
        binding.strikePrice.setPaintFlags(binding.strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.strikePrice.setText("₹"+String.valueOf((int)Double.parseDouble(dealArray.get(counter).getOriginalPrice())));
        binding.flatOff.setText("Flat Off : "+String.valueOf(100-((int)Double.parseDouble(dealArray.get(counter).getDealPrice())*100/(int)Double.parseDouble(dealArray.get(counter).getOriginalPrice())))+"%");
        heart.cancelAnimation();
        heart.clearAnimation();
        heart.setProgress(0f);
        long audioDuration = 0L;
        boolean didPlay = false;


        if (dealArray.get(counter).getAudioFileLink() != null) {

            mmr.setDataSource(dealArray.get(counter).getAudioFileLink(), new HashMap<String, String>());
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            audioDuration = Integer.parseInt(durationStr);


            Uri uri = Uri.parse(dealArray.get(counter).getAudioFileLink());
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(StatusDealActivity.this, uri);
                Toast.makeText(StatusDealActivity.this, "Audio should start: " + String.valueOf(audioDuration / 1000), Toast.LENGTH_SHORT).show();

                player.prepare();
                player.start();
                didPlay = true;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        boolean finalDidPlay = didPlay;
        handler.postDelayed(() -> {
            storiesProgressView.resume();
            if (finalDidPlay) {
                if (player != null) player.reset();
            }

        }, audioDuration);


    }

    private void addToSubscribedDealInCustomer() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customerDB").child(currentCustomer.currentUser.getCustomerID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customer c = dataSnapshot.getValue(customer.class);
                List<String> list = new ArrayList<>();
                if (c.getCurrentDeal() != null) list = c.getCurrentDeal();
                list.add(dealArray.get(counter).getDealID());
                c.setCurrentDeal(list);

                ref.child("currentDeal").setValue(list).addOnFailureListener(e -> {
                    Toast.makeText(StatusDealActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    storiesProgressView.resume();
                })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(StatusDealActivity.this, "Deal joined successfully!.", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                storiesProgressView.resume();
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Status Deal", databaseError.getDetails());

            }
        });

    }

    private void addToSubscribersInDeal() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dealsDB").child(dealArray.get(counter).getDealID());
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                DealModel d = mutableData.getValue(DealModel.class);

                if (d == null) {
                    return Transaction.success(mutableData);
                }

                int peopleNeeded = Integer.parseInt(d.getPeopleLeft());

                if (peopleNeeded == 0) {
                    flag = 2;
                    return Transaction.success(mutableData);
                } else {
                    peopleNeeded--;
                    d.setPeopleLeft(String.valueOf(peopleNeeded));
                }

                List<String> list = new ArrayList<>();

                if (d.getCurrentSubscribers().size() > 0) {
                    list = d.getCurrentSubscribers();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals(currentCustomer.currentUser.getCustomerID())) {
                            flag = 3;
                            return Transaction.success(mutableData);
                        }
                    }
                }
                list.add(currentCustomer.currentUser.getCustomerID());
                d.setCurrentSubscribers(list);

                mutableData.setValue(d);

                flag = 1;
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                if (!b) {
                    Log.d("StatusDeal", databaseError.getDetails());
                    flag = 0;
                }

                if (b && flag == 1 || flag == 2) {
                    addToSubDeal(dataSnapshot);
                    updateProductsDB();
                    addToSubscribedDealInCustomer();
                }


                if (flag == 3) {
                    Toast.makeText(StatusDealActivity.this, "You have already subscribed to this deal.", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    storiesProgressView.resume();
                }


                loading.setVisibility(View.GONE);


            }
        });
    }

    private void updateProductsDB() {

        if (dealArray.get(counter).getProductID() != null) {

            DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("productsDB").child(dealArray.get(counter).getProductID());
            prodref.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    productInfo p = mutableData.getValue(productInfo.class);

                    if (p == null) {
                        p = new productInfo();
                        p.setLikeCounter(1);
                        p.setSubscriberCounter(1);

                        mutableData.setValue(p);

                        return Transaction.success(mutableData);
                    }

                    int sub = p.getSubscriberCounter();
                    p.setSubscriberCounter(++sub);

                    mutableData.setValue(p);

                    return Transaction.success(mutableData);


                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    //TODO Remove below
                    if (!b) {
                        Toast.makeText(StatusDealActivity.this, "productsDB: " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private void addToSubDeal(DataSnapshot dataSnapshot) {

        DealModel d = dataSnapshot.getValue(DealModel.class);
        assert d != null;
        d.setSubscriberID(currentCustomer.currentUser.getCustomerID());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("subDealsDB");
        ref.child(d.getDealID() + currentCustomer.currentUser.getCustomerID().substring(0, 4)).setValue(d).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(StatusDealActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onNext() {

        if (player != null) player.reset();
        if (counter < PROGRESS_COUNT - 1) setStatusFromArray(++counter);


    }


    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

        if (player != null) player.reset();
        setStatusFromArray(--counter);
    }

    @Override
    public void onComplete() {
        if (player != null) {
            player.release();
            player = null;
        }
        counter = 0;
        finish();
    }

    @Override
    protected void onDestroy() {
        counter = 0;
        if (player != null) {
            player.release();
            player = null;
        }
        storiesProgressView.destroy();
        super.onDestroy();
    }


    private void setDealList() {

        Bundle bundle = getIntent().getExtras();
        String customerID = bundle.getString("passedCustomerID");


        DatabaseReference userref;
        userref = FirebaseDatabase.getInstance().getReference().child("customerDB").child(customerID);


        userref.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    customer = dataSnapshot.getValue(customer.class);
                    deals = customer.getLeadDeal();

                    if (deals != null) {
                        Log.d("Tag", "deals set: " + customer.getName());
                        PROGRESS_COUNT = deals.size();
                        Log.d("Tag", "Should call setStories: " + PROGRESS_COUNT + " " + deals.get(0));
                        storiesProgressView.setStoriesCount(PROGRESS_COUNT);


                        storiesProgressView.setStoryDuration(3000L);
                        storiesProgressView.startStories();
                        Log.d("Status", "Story start called");
                        setStatus(counter);

                        heart.setProgress(0f);

                    }
                    if (deals == null) Log.d("Tag", "deals not set");


                } else {
                    Log.d("Tag", "datasnapshot do not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Tag", "setDeal Cancelled");
                Toast.makeText(StatusDealActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setStatus(int i) {
        DatabaseReference dealref;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (storiesProgressView != null)

                    storiesProgressView.pause();
            }
        });

        dealref = FirebaseDatabase.getInstance().getReference().child("dealsDB");
        if (deals == null) {
            Log.d("Tag", "deals not set in set status");
            return;
        }
        String dealID = deals.get(i);


        dealref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(dealID).exists()) {

                    currentDeal = dataSnapshot.child(dealID).getValue(DealModel.class);
                    Picasso.get().load(imageBaseUrl + currentDeal.getImageUrl().get(0).getFile()).into(image);
                    textView.setText(currentDeal.getName());
                    heart.cancelAnimation();
                    heart.clearAnimation();
                    heart.setProgress(0f);
                    long audioDuration = 0L;


                    if (currentDeal.getAudioFileLink() != null) {

                        mmr.setDataSource(currentDeal.getAudioFileLink(), new HashMap<String, String>());
                        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        audioDuration = Integer.parseInt(durationStr);


                        Uri uri = Uri.parse(currentDeal.getAudioFileLink());
                        try {
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(StatusDealActivity.this, uri);
                            Toast.makeText(StatusDealActivity.this, "Audio should start: " + String.valueOf(audioDuration / 1000), Toast.LENGTH_SHORT).show();

                            player.prepare();
                            player.start();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            storiesProgressView.resume();

                        }
                    }, audioDuration);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatusDealActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        counter = 0;
        if (player != null) {
            player.release();
            player = null;
        }
        super.onBackPressed();
    }
}