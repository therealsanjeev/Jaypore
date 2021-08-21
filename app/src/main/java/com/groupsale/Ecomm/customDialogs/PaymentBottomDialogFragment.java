package com.groupsale.Ecomm.customDialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.databinding.ActivityMainBinding;
import com.groupsale.Ecomm.databinding.FragmentPaymentBottomDialogBinding;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.customer;
import com.groupsale.Ecomm.models.productInfo;
import com.groupsale.Ecomm.models.products;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PaymentBottomDialogFragment extends BottomSheetDialogFragment {


    TextView TvContinue;
    FragmentPaymentBottomDialogBinding binding;
    LottieAnimationView confetti;
    Context mContext;
    String dealID;
    int price;
    private static List<products> product;





    public static PaymentBottomDialogFragment newInstance(List<products> products) {

        product = products;
        final PaymentBottomDialogFragment fragment = new PaymentBottomDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBottomDialogBinding.inflate(inflater,container,false);





        confetti = ((Activity) mContext).findViewById(R.id.confetti_main);
        TvContinue = binding.tvContinue;

        double Price = Double.parseDouble(product.get(0).getPrice());

        double doubleGroupPrice = 0.9 * Price;

        boolean flag = true;

        for (int i = 0; i < product.get(0).getAttributes().size(); i++) {
            if (product.get(0).getAttributes().get(i).getAttributeCode().equals("groupprice")) {
                doubleGroupPrice = Double.parseDouble(product.get(0).getAttributes().get(i).getValue().toString());
                flag = false;

            } else if (product.get(0).getAttributes().get(i).getAttributeCode().equals("special_price") && flag) {
                doubleGroupPrice = 0.9 * Double.parseDouble(product.get(0).getAttributes().get(i).getValue().toString());
            }

        }

        price = (int) doubleGroupPrice;

        binding.price.setText("₹ "+String.valueOf(price));
        binding.shippingPrice.setText("₹ "+String.valueOf(19));
        binding.GTotal.setText("₹ "+String.valueOf(price+19));
        binding.FinalTotal.setText("₹ "+String.valueOf(price+19));
        binding.title.setText(product.get(0).getName());



        TvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               AddtoFirebase(product);

            }
        });



        return binding.getRoot();
    }


    public void getContext(Context context){
        mContext = context;
    }


    private void AddtoFirebase(List<products> products) {

        //Adding to DealDB
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dealsDB");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String customerID = currentCustomer.currentUser.getCustomerID();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        dealID = formatter.format(date) + customerID.substring(0, 4);

        dealID = dealID.replaceAll("\\s", "");
        dealID = dealID.replaceAll("/", "");
        dealID = dealID.replaceAll(":", "");
        int intTeamSize = 2;
        double Price = Double.parseDouble(products.get(0).getPrice());
        double doubleGroupPrice = 0.9 * Price;
        String description = "No description yet";
        boolean flag = true;

        for (int i = 0; i < products.get(0).getAttributes().size(); i++) {
            if (products.get(0).getAttributes().get(i).getAttributeCode().equals("teamsize")) {
                intTeamSize = Integer.parseInt(products.get(0).getAttributes().get(i).getValue().toString());

            } else if (products.get(0).getAttributes().get(i).getAttributeCode().equals("groupprice")) {
                doubleGroupPrice = Double.parseDouble(products.get(0).getAttributes().get(i).getValue().toString());
                flag = false;

            } else if (products.get(0).getAttributes().get(i).getAttributeCode().equals("short_description")) {
                description = products.get(0).getAttributes().get(i).getValue().toString();
            } else if (products.get(0).getAttributes().get(i).getAttributeCode().equals("special_price") && flag) {
                doubleGroupPrice = 0.9 * Double.parseDouble(products.get(0).getAttributes().get(i).getValue().toString());
            }

        }

        int intpeopleleft = intTeamSize - 1;
        String groupPrice = Double.toString(doubleGroupPrice);
        String teamSize = Integer.toString(intTeamSize);
        String poepleLeft = Integer.toString(intpeopleleft);


        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("pinCode", currentCustomer.currentUser.getPinCode());
        userMap.put("teamSize", teamSize);
        userMap.put("dealPrice", groupPrice);
        userMap.put("epochTime", date.getTime());
        userMap.put("dealID", dealID);
        userMap.put("creatorName", currentCustomer.currentUser.getName());
        userMap.put("peopleLeft", poepleLeft);
        userMap.put("status", 1);
        userMap.put("unique", 1);
        userMap.put("likeCounter", 1);
        userMap.put("dateTime", formatter.format(date));
        userMap.put("productID", products.get(0).getSku());
        userMap.put("textMessage", "Hi please team up to buy " + products.get(0).getName());
        userMap.put("description", description);
        userMap.put("name", products.get(0).getName());
        userMap.put("creatorID", customerID);
        userMap.put("originalPrice", products.get(0).getPrice());
        userMap.put("ImageUrl", products.get(0).getImages());
        userMap.put("uniqueFilter", currentCustomer.currentUser.getPinCode() + "_1_1");
        ref.child(dealID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {


                } else {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //update subDealsDB
        DatabaseReference subref = FirebaseDatabase.getInstance().getReference().child("subDealsDB");
        userMap.put("subscriberID", currentCustomer.currentUser.getName());

        subref.child(dealID + customerID.substring(0, 4)).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {


                } else {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //update productsDB
        DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("productsDB").child(products.get(0).getSku());
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
                    Toast.makeText(mContext, "productsDB: " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        //Updating to UserDB->User
        ref = FirebaseDatabase.getInstance().getReference().child("customerDB");
        List<String> currentDealList = new ArrayList<String>();
        if (currentCustomer.currentUser.getLeadDeal() != null) {
            currentDealList = currentCustomer.currentUser.getLeadDeal();
        }

        currentDealList.add(dealID);

        HashMap<String, Object> dealMap = new HashMap<>();
        dealMap.put("leadDeal", currentDealList);
        String finalDealID = dealID;
        ref.child(customerID).updateChildren(dealMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(mContext, "Deal added successfully to user", Toast.LENGTH_SHORT).show();
                    updateUserData();


                    dismiss();

                    confetti.setVisibility(View.VISIBLE);
                    confetti.playAnimation();


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            sharePopup popup = new sharePopup(mContext, dealID);
                            popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popup.show();

                            confetti.setVisibility(View.INVISIBLE);

                        }
                    },1500);



                } else {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void updateUserData() {

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("customerDB");
        RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(currentCustomer.currentUser.getCustomerID()).exists()) {

                    currentCustomer.currentUser = dataSnapshot.child(currentCustomer.currentUser.getCustomerID()).getValue(customer.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //Toast.makeText(getApplicationContext(), "Error: " +databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}