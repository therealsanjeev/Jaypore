package com.groupsale.Ecomm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.groupsale.Ecomm.customDialogs.sharePopup;

public class TestActivity extends AppCompatActivity {

    Button buttonTest;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        buttonTest = findViewById(R.id.buttonTest);
        text = findViewById(R.id.textViewTest);


        String link = "https://firebasestorage.googleapis.com/v0/b/lootllo-46bfc.appspot.com/o/UploadAudio%2F896745ghty%2FdealAudio896745ghty.mp3?alt=media&token=3621db65-98a6-473a-a5f1-cc39200514ce";

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                sharePopup popup = new sharePopup(TestActivity.this,"01062021003214mtb0");
                popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popup.show();


            }
        });

    }
}