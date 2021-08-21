package com.groupsale.Ecomm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.customer;

import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String verificationId;
    Button buttonVerify;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;
    private EditText editText;

    FirebaseAnalytics firebaseAnalytics;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        preferenceManager = new PreferenceManager(getApplicationContext());
        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        setupOTPInputs();

        final ProgressBar progressBar = findViewById(R.id.progressOTP);
        final Button buttonVerify = findViewById(R.id.buttonVerify);


        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");


        String phoneNumber = getIntent().getStringExtra("mobile");
        sendVerificationCode(phoneNumber);

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please Enter Valid Code", Toast.LENGTH_LONG).show();
                    return;
                }

                String code =
                        inputCode1.getText().toString() +
                                inputCode2.getText().toString() +
                                inputCode3.getText().toString() +
                                inputCode4.getText().toString() +
                                inputCode5.getText().toString() +
                                inputCode6.getText().toString();
                if ((code.isEmpty() || code.length() < 6)) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });

        findViewById(R.id.ResendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        OtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                String code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    verifyCode(code);
                                } else {

                                    signInWithCredential(phoneAuthCredential);

                                }

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(OtpActivity.this, "OTP Sent", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        });
    }


    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            } else {

                signInWithCredential(phoneAuthCredential);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            addtoFirebase();

                            //ADDING DATA TO FIRESTORE :
                            AddDataToFireStore();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Write whatever to want to do after delay specified (1 sec)
                                    Log.d("Handler", "Running Handler");
                                    Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("OTP","Sign In");
                                    firebaseAnalytics.logEvent("SignIN_Successfully",bundle);
                                }
                            }, 2000);


                        } else {
                            Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void AddDataToFireStore() {

        Toast.makeText(this, "ADD TO FIRESTORE!!!", Toast.LENGTH_SHORT).show();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("username");
        String phonenumber = getIntent().getStringExtra("mobile");
        String pincode = getIntent().getStringExtra("pincode");
        HashMap<String, Object> user = new HashMap<>();


        user.put(Constants.KEY_NAME, name);
        user.put(Constants.KEY_CONTACT, phonenumber);
        user.put(Constants.KEY_PINCODE, pincode);
        user.put(Constants.KEY_HOUSE_NO, "");
        user.put(Constants.KEY_STREET_NAME, "");
        user.put(Constants.KEY_PROFILE, "");
        user.put(Constants.KEY_USER_REALTIME_ID, userId);
        user.put(Constants.KEY_DAILY_REWARDS_LAST_TIME, 0);
        user.put(Constants.KEY_TOTAL_POINTS,0);

        database.collection(Constants.KEY_USERS)
                .document(userId)
                .set(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putString(Constants.KEY_SIGN_IN, "1");
                    preferenceManager.putString(Constants.KEY_NAME, name);
                    preferenceManager.putString(Constants.KEY_CUSTOMER_ID, userId);
                    preferenceManager.putString(Constants.KEY_CONTACT, phonenumber);
                    preferenceManager.putString(Constants.KEY_PINCODE, pincode);
                })
                .addOnFailureListener(e -> {

                    Log.e("SIGNUP_ERROR", e.getMessage());
                    Toast.makeText(OtpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void addtoFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customerDB");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String phonenumber = getIntent().getStringExtra("mobile");
        String name = getIntent().getStringExtra("username");
        String pincode = getIntent().getStringExtra("pincode");


        String customerID = user.getUid();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        HashMap<String, Object> customerMap = new HashMap<>();
        customerMap.put("pinCode", pincode);
        customerMap.put("name", name);
        customerMap.put("phoneNumber", phonenumber);
        customerMap.put("customerID", customerID);
        customerMap.put("createdAt", formatter.format(date));
        ref.child(customerID).updateChildren(customerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();

                    updateCurrentUser(customerID);

                } else {
                    //TODO take him back to Register page
                    Toast.makeText(getApplicationContext(), "Network Error try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCurrentUser(String customerID) {
        currentCustomer currentCustomer = null;
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("customerDB");
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(customerID).exists()) {

                    currentCustomer.currentUser = dataSnapshot.child(customerID).getValue(customer.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + getIntent().getStringExtra("mobile"),
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }


    private void setupOTPInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}