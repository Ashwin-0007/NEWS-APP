package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class NewsWebViewActivity extends AppCompatActivity {

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);

        url = getIntent().getStringExtra("url");
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().getJavaScriptEnabled();

        webView.loadUrl(url);
    }
}