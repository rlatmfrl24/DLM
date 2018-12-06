package com.soulkey.mdlm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.JsonElement;
import com.soulkey.mdlm.Model.JsonGenerator;
import com.soulkey.mdlm.Model.NetRetrofit;
import com.soulkey.mdlm.Model.WebViewInterface;
import com.soulkey.mdlm.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends Activity {

    private final static String DOMAIN_BP = "http://v12.battlepage.com";
    private final static String DOMAIN_DD = "https://www.dogdrip.net";

    private WebView webView;
    private FloatingActionsMenu fab_menu;
    private FloatingActionButton fab_check;
    private FloatingActionButton fab_close;
    private FloatingActionButton fab_bmk;
    private FloatingActionButton fab_dapina;
    private Intent intent;

    private String link_url;
    private String link_title;

    @Override
    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.main_webview);

        fab_menu = findViewById(R.id.fab_menu);
        fab_check = findViewById(R.id.fab_check);
        fab_close = findViewById(R.id.fab_close);
        fab_bmk = findViewById(R.id.fab_bmk);
        fab_dapina = findViewById(R.id.fab_dapina);

        fab_check.setOnClickListener(fab_listener);
        fab_close.setOnClickListener(fab_listener);
        fab_bmk.setOnClickListener(fab_listener);
        fab_dapina.setOnClickListener(fab_listener);

        intent = getIntent();
        link_url = intent.getStringExtra("url");
        link_title = intent.getStringExtra("title");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebViewInterface(link_title, link_url), "Dapina");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        if(Build.VERSION.SDK_INT>=21){
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.loadUrl(link_url);
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
                    callback = NetRetrofit.getInstance().getService().Call_DBQuery(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
                case R.id.fab_close:
                    //Do Nothing
                    break;
                case R.id.fab_bmk:
                    callback = NetRetrofit.getInstance().getService().Call_DBQuery(new JsonGenerator().makeInsertBMKJSON(list));
                    callback.enqueue(fab_callback);
                    callback = NetRetrofit.getInstance().getService().Call_DBQuery(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
                case R.id.fab_dapina:
                    if (link_url.contains(DOMAIN_BP)){
                        webView.loadUrl("javascript:window.Dapina.DapinaBP(document.getElementsByTagName('html')[0].innerHTML);");
                    }else if(link_url.contains(DOMAIN_DD)){
                        webView.loadUrl("javascript:window.Dapina.DapinaDD(document.getElementsByTagName('html')[0].innerHTML);");
                    }else{
                        //Do Nothing
                    }
                    callback = NetRetrofit.getInstance().getService().Call_DBQuery(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
            }
        }
        public Callback<JsonElement> fab_callback = new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //Log.d("muta", response.body().toString());
                Toast.makeText(getApplicationContext(), R.string.toast_db_save_msg , Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("muta", t.getMessage());
                Toast.makeText(getApplicationContext(), R.string.toast_db_fail_msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        };
    };
}
