package com.groupsale.Ecomm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupsale.Ecomm.activities.MainActivity;
import com.groupsale.Ecomm.activities.OtpActivity;
import com.groupsale.Ecomm.models.currentCustomer;
import com.groupsale.Ecomm.models.customer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    private EditText Username;
    private EditText PhNumber;
    private int pincode = 110010; //dummy variable not the actual pinCode

    ImageSlider Regslider;

    final List<SlideModel> remoteimagesReg = new ArrayList<>();

    FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);



        Regslider = findViewById(R.id.image_slider_reg);


        remoteimagesReg.add(new SlideModel(R.drawable.img1, ScaleTypes.FIT));
        remoteimagesReg.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));
        remoteimagesReg.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));

        Regslider.setImageList(remoteimagesReg,ScaleTypes.FIT);


        final ProgressBar progressBar = findViewById(R.id.progressOTP);
        final Button ButtonContinue = findViewById(R.id.buttonContinue);

        Username = findViewById(R.id.usname);  // Username
        final EditText inputMobile = findViewById(R.id.inputMobile);  // phone number
       // pincode = findViewById(R.id.Deliverypincode); // delivery Pincodes



        ButtonContinue.setOnClickListener(v -> {


            boolean granted;


            granted = checkPermissions();

            if(inputMobile.getText().toString().trim().isEmpty()){
                Toast.makeText(Register.this,"Enter Mobile",Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(Username.getText()) ) {
                Username.setError("Name is required");
                Username.requestFocus();
                return;
            }


            progressBar.setVisibility(View.VISIBLE);

            if(granted) {
                Intent intent = new Intent(Register.this, OtpActivity.class);
                intent.putExtra("mobile", inputMobile.getText().toString());
                intent.putExtra("username", Username.getText().toString());
                intent.putExtra("pincode", String.valueOf(pincode));
                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("Login","Sign Up");
                firebaseAnalytics.logEvent("SignUp_btn",bundle);
            }

            else{
                Toast.makeText(Register.this, "All permissions are required. Try again", Toast.LENGTH_SHORT).show();


            }

        });

    }

    private boolean checkPermissions() {


        /*if(ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(Register.this)
                    .withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    if (permissionDeniedResponse.isPermanentlyDenied()) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    Toast.makeText(Register.this, "Permission is required to make deals. Please Accept", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();

                }
            }).onSameThread()
        .check();
        }

        if(ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(Register.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    if (permissionDeniedResponse.isPermanentlyDenied()) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    Toast.makeText(Register.this, "Permission is required to make deals. Please Accept", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();

                }
            }).onSameThread()
        .check();
        }*/

        Dexter.withContext(Register.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    
                }

                
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    
                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();

            }
        }).onSameThread()
                .check();

        return (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);


    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Permissions Required");
        builder.setMessage("This app needs Audio and Storage permission to work properly. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(Register.this, R.color.Red));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(Register.this, R.color.DARKGREEN));
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String customerID = FirebaseAuth.getInstance().getCurrentUser().getUid() ;

            updateCurrentUser(customerID);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    private void updateCurrentUser(String customerID) {
        currentCustomer currentCustomer = null;
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("customerDB");
        RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(customerID).exists()) {

                    currentCustomer.currentUser = dataSnapshot.child(customerID).getValue(customer.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error: " +databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}