package com.groupsale.Ecomm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DeeplinkActivity extends AppCompatActivity {
    private WebView webView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);
//
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = intent.getData();



        if(uri !=null)
        {
            List<String> param = uri.getPathSegments();

            webView = (WebView) findViewById(R.id.web_deeplink);
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(uri.toString());

        }


//        webView=(WebView)findViewById(R.id.web_deeplink);
////loads androidride homepage to webview
//        webView.loadUrl("https://www.jaypore.com/");


    }


    private class callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (url.startsWith("https://api.whatsapp.com/send?phone=")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;

            } else {
                return false;
            }

        }

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

