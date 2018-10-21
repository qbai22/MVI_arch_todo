package com.commonsware.todo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

public class AboutActivity extends FragmentActivity {

    WebView aboutWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutWebView = findViewById(R.id.about_web_view);
        aboutWebView.loadUrl("file:///android_asset/about.html");
    }


}
