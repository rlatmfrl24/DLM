package com.soulkey.mdlm.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soulkey.mdlm.Adapter.LinkAdapter;
import com.soulkey.mdlm.Model.JsonGenerator;
import com.soulkey.mdlm.Model.NetRetrofit;
import com.soulkey.mdlm.R;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinkAdapter mAdapter;
    private Toolbar mToolbar;
    private JsonArray item_array = new JsonArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        mToolbar = findViewById(R.id.toolbar_bookmark);
        mRecyclerView = findViewById(R.id.recyclerview_bookmark);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new LinkAdapter(item_array, false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                item.setEnabled(false);
                item_array = new JsonArray();
                Log.d("muta", "refresh button called");
                Call<JsonElement> callback_data = NetRetrofit.getInstance().getService().Call_DBQuery(new JsonGenerator().makeSelectJSON("tb_bookmark_info", "link", ""));
                callback_data.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        for(JsonElement element : response.body().getAsJsonArray()){
                            JsonObject obj = element.getAsJsonObject();
                            String authority;
                            try {
                                authority = new URL(obj.get("link").getAsString()).getAuthority();
                            } catch (MalformedURLException e) {
                                authority = "Undefined";
                                Log.e("muta", "Unexceptable URL Error..");
                                e.printStackTrace();
                            }
                            obj.addProperty("title", authority);
                            item_array.add(obj);
                        }
                        mAdapter = new LinkAdapter(item_array, false);
                        mRecyclerView.setAdapter(mAdapter);
                        Toast.makeText(getApplicationContext(), R.string.toast_db_access_msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        Log.d("muta", t.getMessage());
                    }
                });
                item.setEnabled(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
