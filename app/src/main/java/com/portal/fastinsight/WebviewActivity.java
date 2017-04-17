package com.portal.fastinsight;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WebviewActivity extends AppCompatActivity {
    WebView webView;
    Bundle webviewBundle;
    int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        MyWebViewClient webViewClient = new MyWebViewClient();

        webView.clearCache(true);
        webView.clearHistory();

        webView.setWebViewClient(webViewClient);

        if (checkNetworkConnection()) {
            webView.loadUrl("https://voc-russia.shopmetrics.com");
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Please check your network connection.");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        ImageView imageLoder = (ImageView) findViewById(R.id.imageloader);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        imageLoder.setAnimation(animation);
        StartAnimations();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( (keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack() ) {
            webView.restoreState(webviewBundle);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.activity_main);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.animation);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.imageloader);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            findViewById(R.id.imageloader).setVisibility(View.GONE);
            findViewById(R.id.webview).setVisibility(View.VISIBLE);
            Handler handler = new Handler();

            if (state == 0) {
                webviewBundle = new Bundle();
                webView.saveState(webviewBundle);
                state = 1;
            }
        }
    }
}
