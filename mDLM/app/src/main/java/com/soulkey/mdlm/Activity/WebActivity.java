package com.soulkey.mdlm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.JsonElement;
import com.soulkey.mdlm.APICall.Dapina;
import com.soulkey.mdlm.APICall.JsonGenerator;
import com.soulkey.mdlm.APICall.NetRetrofit;
import com.soulkey.mdlm.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    private FloatingActionButton fab_dapina;
    private Intent intent;

    private String link_url;
    private String link_title;
    private String dapina_save_directory = "/dapina/";

    @Override
    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_web);

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

        webView = findViewById(R.id.main_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "Dapina");
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
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
                case R.id.fab_close:
                    //Do Nothing
                    break;
                case R.id.fab_bmk:
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertBMKJSON(list));
                    callback.enqueue(fab_callback);
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
                case R.id.fab_dapina:
                    if (link_url.contains("v12.battlepage.com")){
                        webView.loadUrl("javascript:window.Dapina.DapinaBP(document.getElementsByTagName('html')[0].innerHTML);");
                    }else if(link_url.contains("www.dogdrip.net")){
                        webView.loadUrl("javascript:window.Dapina.DapinaDD(document.getElementsByTagName('html')[0].innerHTML);");
                    }else{
                        //Do Nothing
                    }
                    callback = NetRetrofit.getInstance().getService().Insert_Query(new JsonGenerator().makeInsertLinkJSON(list));
                    callback.enqueue(fab_callback);
                    break;
            }
        }
        public Callback<JsonElement> fab_callback = new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("muta", response.body().toString());
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
    class JavaScriptInterface{
        @JavascriptInterface
        public void DapinaBP(String html){
            try{
                Document document = Jsoup.parse(html);
                Elements img_list = document.selectFirst(".search_content").getElementsByTag("img");
                if(img_list.size() == 1){
                    String img_url = img_list.get(0).attr("src");
                    String filename = link_title.replaceAll(" ", "_")+img_url.substring(img_url.lastIndexOf('.'));
                    Dapina.newInstance().getClient().files().saveUrl(dapina_save_directory+filename, img_url);
                } else {
                    int cnt = 0;
                    String directory_name = link_title.replaceAll(" ", "_");
                    for(Element element : img_list){
                        cnt++;
                        String img_url = element.attr("src");
                        String save_path = dapina_save_directory+directory_name+"/"+String.format("%03d", cnt)+img_url.substring(img_url.lastIndexOf('.'));
                        Dapina.newInstance().getClient().files().saveUrl(save_path, img_url);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void DapinaDD(String html){
            try{
                Document document = Jsoup.parse(html);
                Elements img_list = document.selectFirst("#article_1 > div").getElementsByTag("img");
                if(img_list.size() == 1){
                    String img_url = img_list.get(0).attr("src");
                    if(img_url.startsWith("/")){    //inner Dogdrip link
                        img_url = "https://www.dogdrip.net"+img_url;
                    }
                    String filename = link_title.replaceAll(" ", "_")+img_url.substring(img_url.lastIndexOf('.'));
                    Dapina.newInstance().getClient().files().saveUrl(dapina_save_directory+filename, img_url);
                } else {
                    int cnt = 0;
                    String directory_name = link_title.replaceAll(" ", "_");
                    for(Element element : img_list){
                        cnt++;
                        String img_url = element.attr("src");
                        if(img_url.startsWith("/")){    //inner Dogdrip link
                            img_url = "https://www.dogdrip.net"+img_url;
                        }
                        String save_path = dapina_save_directory+directory_name+"/"+String.format("%03d", cnt)+img_url.substring(img_url.lastIndexOf('.'));
                        Dapina.newInstance().getClient().files().saveUrl(save_path, img_url);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
