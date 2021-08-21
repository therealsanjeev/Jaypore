package com.groupsale.Ecomm.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.databinding.ActivityProfileEditPageBinding;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;

import java.util.HashMap;

public class ProfileEditPageActivity extends AppCompatActivity {

    ActivityProfileEditPageBinding binding;
    private EditText inputName, inputHouse, inputPincode, inputStreet;
    private PreferenceManager preferenceManager;
    private Button buttonSave;
    private String downloadUrl, storageDIR, finalText;
    private StorageReference mStorage;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth user;
    private ProgressDialog progressDialog;
    ImageView profile;
    TextView name, contact, pincode, address, inputContact;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        preferenceManager = new PreferenceManager(getApplicationContext());

        storageDIR = preferenceManager.getString(Constants.KEY_NAME) + preferenceManager.getString(Constants.KEY_USER_ID);
        mStorage = FirebaseStorage.getInstance().getReference(Constants.KEY_USERS).child(storageDIR);

        user = FirebaseAuth.getInstance();

        inputName = binding.etName;
        inputContact = binding.etContact;
        inputPincode = binding.etPincode;
        inputHouse = binding.etHouse;
        inputStreet = binding.etStreet;
        buttonSave = binding.saveBtn;
        progressBar = binding.progress;
        floatingActionButton = binding.addImg;
        profile = binding.profileImg;

        Glide.with(this)
                .load(preferenceManager.getString(Constants.KEY_PROFILE))
                .error(R.drawable.ic_baseline_person_24)
                .into(profile);

        inputName.setText(preferenceManager.getString(Constants.KEY_NAME).toUpperCase());
        inputContact.setText(preferenceManager.getString(Constants.KEY_CONTACT));
        inputPincode.setText(preferenceManager.getString(Constants.KEY_PINCODE));
        inputHouse.setText(preferenceManager.getString(Constants.KEY_HOUSE_NO));
        inputStreet.setText(preferenceManager.getString(Constants.KEY_STREET_NAME));

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/");
            startActivityForResult(intent, 101);
        });

        buttonSave.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (getInputsContentText(inputName).isEmpty()) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else if (getInputsContentText(inputHouse).isEmpty()) {
                Toast.makeText(this, "Enter house no.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else if (getInputsContentText(inputPincode).isEmpty()) {
                Toast.makeText(this, "Enter Pincode no.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else if (getInputsContentText(inputStreet).isEmpty()) {
                Toast.makeText(this, "Enter street", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else {
                updateProfile();
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


       /* if (appLinkAction != null){

            Toast.makeText(ProfileEditPageActivity.this, "join" +appLinkAction, Toast.LENGTH_SHORT).show();

        }

        */



    }

    private void updateProfile() {


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, getInputsContentText(inputName));
        user.put(Constants.KEY_PINCODE, getInputsContentText(inputPincode));
        user.put(Constants.KEY_HOUSE_NO, getInputsContentText(inputHouse));
        user.put(Constants.KEY_STREET_NAME, getInputsContentText(inputStreet));
        user.put(Constants.KEY_PROFILE, downloadUrl);

        database.collection(Constants.KEY_USERS)
                .document(FirebaseAuth.getInstance().getUid()).update(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    preferenceManager.putString(
                            Constants.KEY_NAME,
                            getInputsContentText(inputName)
                    );
                    preferenceManager.putString(
                            Constants.KEY_PINCODE,
                            getInputsContentText(inputPincode)
                    );
                    preferenceManager.putString(
                            Constants.KEY_STREET_NAME,
                            getInputsContentText(inputStreet)
                    );
                    preferenceManager.putString(
                            Constants.KEY_HOUSE_NO,
                            getInputsContentText(inputHouse)
                    );

                    preferenceManager.putString(
                            Constants.KEY_PROFILE, downloadUrl
                    );
                    progressBar.setVisibility(View.GONE);
                    onBackPressed();

                });

    }

    private String getInputsContentText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void uploadFile() {
        FirebaseUser firebaseUser = user.getCurrentUser();
        if (firebaseUser != null) {
            progressDialog.show();
            StorageReference mReference = mStorage.child(finalText);
            try {
                mReference.putFile(uri).addOnSuccessListener(
                        taskSnapshot -> {
                            mReference.getDownloadUrl().addOnCompleteListener(
                                    task -> {
                                        downloadUrl = task.getResult().toString();
                                    }
                            );
                            progressDialog.dismiss();
                            Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG)
                                    .show();

                        }
                ).addOnProgressListener(taskSnapshot -> {
                    long progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setTitle("Uploading");
                    progressDialog.setMessage(progress + " % Uploaded");
                });

            } catch (Exception e) {
                Toast.makeText(this, "Error Occur: $e", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            uri = data.getData();
            String s = uri.toString();

            if (s.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        finalText = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        if (!finalText.equals(null)) {
                            uploadFile();
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}