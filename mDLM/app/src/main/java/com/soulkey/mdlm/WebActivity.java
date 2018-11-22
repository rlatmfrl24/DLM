package com.soulkey.mdlm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.JsonElement;
import com.soulkey.mdlm.Model.JsonGenerator;
import com.soulkey.mdlm.Model.NetRetrofit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends Activity {

    private WebView webView;
    private FloatingActionsMenu fab_menu;
    private FloatingActionButton fab_check;
    private FloatingActionButton fab_close;
    private FloatingActionButton fab_bmk;
    private String link_url;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_web);

        fab_menu = findViewById(R.id.fab_menu);
        fab_check = findViewById(R.id.fab_check);
        fab_close = findViewById(R.id.fab_close);
        fab_bmk = findViewById(R.id.fab_bmk);

        webView = findViewById(R.id.main_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        intent = getIntent();
        link_url = intent.getStringExtra("url");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(link_url);

        fab_check.setOnClickListener(fab_listener);
        fab_close.setOnClickListener(fab_listener);
        fab_bmk.setOnClickListener(fab_listener);
    }
    protected View.OnClickListener fab_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<String> list = new ArrayList<>();
            list.add(link_url);
            fab_menu.collapse();
            fab_menu.setEnabled(false);
            Call<JsonElement> callback;
            switch (v.getId()){
                case R.id.fab_check:
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
                case R.id.fab_close:
                    break;
                case R.id.fab_bmk:
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertBMKJSON(list));
                    callback.enqueue(fab_callback);
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
            }
            finish();
        }
        public Callback<JsonElement> fab_callback = new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("muta", response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("muta", t.getMessage());
            }
        };
    };
}
