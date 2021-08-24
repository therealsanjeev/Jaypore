package com.groupsale.Ecomm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.databinding.ActivityProfileBinding;
import com.groupsale.Ecomm.utilities.Constants;
import com.groupsale.Ecomm.utilities.PreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton Editbtn;
    ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;
    ImageView profile;
    TextView name, contact, pincode, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.btnBackProfile).setOnClickListener(v -> onBackPressed());

        preferenceManager = new PreferenceManager(getApplicationContext());
        Editbtn = binding.editBtn;
        profile = binding.profileImg;
        name = binding.usname;
        contact = binding.contact;
        pincode = binding.pincode;
        address = binding.address;

        Glide.with(this)
                .load(preferenceManager.getString(Constants.KEY_PROFILE))
                .error(R.drawable.ic_baseline_person_24)
                .into(profile);

        name.setText(preferenceManager.getString(Constants.KEY_NAME).toUpperCase());
        contact.setText(preferenceManager.getString(Constants.KEY_CONTACT));
        pincode.setText(preferenceManager.getString(Constants.KEY_PINCODE));
        String add=preferenceManager.getString(Constants.KEY_HOUSE_NO)+" "+preferenceManager.getString(Constants.KEY_STREET_NAME);
        if(!add.equals(null))
        address.setText(add);

        Editbtn.setOnClickListener(v -> {

            Intent intent = new Intent(ProfileActivity.this, ProfileEditPageActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}