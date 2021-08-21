package com.groupsale.Ecomm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    Button WhatsappBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        WhatsappBtn = findViewById(R.id.message_Us);

        WhatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                String url = "https://api.whatsapp.com/send?phone=+91" + "7042870887";
                sendIntent.setData(Uri.parse(url));
                if(sendIntent.resolveActivity(getApplicationContext().getPackageManager()) != null){
                    startActivity(sendIntent);
                }
            }
        });




    }
}