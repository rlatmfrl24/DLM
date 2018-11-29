package com.soulkey.mdlm.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonElement;
import com.soulkey.mdlm.APICall.NetRetrofit;
import com.soulkey.mdlm.Fragment.PlaceholderFragment;
import com.soulkey.mdlm.Adapter.SectionsPagerAdapter;
import com.soulkey.mdlm.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private Snackbar snk;
    private boolean hrm_request = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        hrm_request = preferences.getBoolean("pf_hrm_option", false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        snk = Snackbar.make(fab, "Loading Item from Sever..", Snackbar.LENGTH_INDEFINITE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("muta", "Retrofit called on Fragment "+String.valueOf(mViewPager.getCurrentItem()));
                final PlaceholderFragment current = (PlaceholderFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
                Call<JsonElement> callback_Data = null;
                switch (mViewPager.getCurrentItem()){
                    case 0:
                        if(hrm_request) callback_Data = NetRetrofit.getInstance().getService().CallData_HRM();
                        else {
                            Snackbar.make(view, "HRM request is disabled..", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    case 1:
                        callback_Data = NetRetrofit.getInstance().getService().CallData_BP();
                        break;
                    case 2:
                        callback_Data = NetRetrofit.getInstance().getService().CallData_DD();
                        break;
                }
                if(callback_Data!=null){
                    current.showLoading();
                    ShowLoadingMsg();
                    callback_Data.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            current.UpdateList(response.body().getAsJsonArray());
                            current.hideLoading();
                            HideLoadingMsg();
                        }
                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            current.hideLoading();
                            HideLoadingMsg();
                            Snackbar.make(view, "Fail to Connect Server!", Snackbar.LENGTH_LONG).show();
                            Log.e("muta", t.getMessage());
                        }
                    });
                }else{
                    Snackbar.make(view, "Fail to Connect Server!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent =  new Intent(this, ActionActivity.class);
        if (id == R.id.action_settings) {
            intent.putExtra("action", "setting");
            startActivity(intent);
            return true;
        }else if(id == R.id.action_bookmark){
            intent.putExtra("action", "bookmark");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowLoadingMsg(){
        mViewPager.beginFakeDrag();
        fab.hide();
        snk.show();
    }

    public void HideLoadingMsg(){
        mViewPager.endFakeDrag();
        fab.show();
        snk.dismiss();
    }
}
